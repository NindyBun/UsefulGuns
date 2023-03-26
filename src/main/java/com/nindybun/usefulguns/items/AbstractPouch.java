package com.nindybun.usefulguns.items;

import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.gui.PouchContainer;
import com.nindybun.usefulguns.inventory.PouchData;
import com.nindybun.usefulguns.inventory.PouchManager;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class AbstractPouch extends Item {
    private final String name;
    private final PouchTypes type;

    public AbstractPouch(String name, PouchTypes type) {
        super(type.ordinal() <= PouchTypes.DIAMOND.ordinal() ? ModItems.ITEM_GROUP.stacksTo(1) : ModItems.ITEM_GROUP.stacksTo(1).fireResistant());
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return getType(stack).ordinal() >= PouchTypes.NETHERSTAR.ordinal();
    }

    public static PouchTypes getType(ItemStack stack){
        if (!stack.isEmpty() && stack.getItem() instanceof AbstractPouch)
            return ((AbstractPouch) stack.getItem()).type;
        else
            return PouchTypes.LEATHER;
    }

    public static PouchData getData(ItemStack stack){
        if (!(stack.getItem() instanceof AbstractPouch))
            return null;
        UUID uuid;
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("UUID")){
            uuid = UUID.randomUUID();
            tag.putUUID("UUID", uuid);
        }else{
            uuid = tag.getUUID("UUID");
        }
        return PouchManager.get().getOrCreatePouch(uuid, ((AbstractPouch) stack.getItem()).type);
    }

    @Override
    @Nonnull
    public Component getName(@Nonnull ItemStack stack){
        return new TranslatableComponent(this.getDescriptionId(stack)).withStyle(ChatFormatting.RESET);
    }

    public static boolean isPouch(ItemStack stack){
        return stack.getItem() instanceof AbstractPouch;
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack pouch = player.getItemInHand(hand);

        if (!world.isClientSide && player instanceof ServerPlayer && pouch.getItem() instanceof AbstractPouch){
            PouchData data = AbstractPouch.getData(pouch);

            PouchTypes itemType = ((AbstractPouch) pouch.getItem()).type;
            UUID uuid = data.getUuid();

            //data.updateAccessRecords(player.getName().getString(), System.currentTimeMillis());

            if (data.getType().ordinal() < itemType.ordinal())
                data.upgrade(itemType);

            NetworkHooks.openGui(((ServerPlayer) player), new SimpleMenuProvider(
                    (windowId, playerInventory, playerEntity) ->
                            new PouchContainer(windowId, playerInventory, uuid, data.getType(), (PouchHandler) data.getHandler()), pouch.getHoverName()),
                    (buffer -> buffer.writeUUID(uuid).writeInt(data.getType().ordinal())));
        }

        return InteractionResultHolder.success(pouch);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        if (flag.isAdvanced() && stack.getTag() != null && stack.getTag().contains("UUID")) {
            UUID uuid = stack.getTag().getUUID("UUID");
            tooltip.add(new TextComponent("ID: " + uuid.toString().substring(0, 8)).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        if (stack.getItem().isFireResistant())
            tooltip.add(new TextComponent("Fire Resistant!").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
        LazyOptional<IItemHandler> optional = AbstractPouch.getData(stack).getOptional();
        if (optional.isPresent()){
            IItemHandler handler = optional.resolve().get();
            if (!(handler instanceof IItemHandlerModifiable))
                throw new RuntimeException("IItemHandler instance does not implement IItemHandlerModifiable");
            IItemHandlerModifiable itemHandlerModifiable = (IItemHandlerModifiable) handler;
            ListTag tagList = nbt.getList("ClientInventory", Tag.TAG_COMPOUND);
            for (int i = 0; i < tagList.size(); i++)
            {
                CompoundTag itemTags = tagList.getCompound(i);
                int j = itemTags.getInt("Slot");

                if (j >= 0 && j < handler.getSlots())
                {
                    itemHandlerModifiable.setStackInSlot(j, ItemStack.of(itemTags));
                }
            }
        }
    }

    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        LazyOptional<IItemHandler> optional = AbstractPouch.getData(stack).getOptional();
        CompoundTag tag = super.getShareTag(stack);
        if (optional.isPresent()){
            IItemHandler handler = optional.resolve().get();
            ListTag nbtTagList = new ListTag();
            int size = handler.getSlots();
            for (int i = 0; i < size; i++)
            {
                ItemStack stack1 = handler.getStackInSlot(i);
                if (!stack1.isEmpty())
                {
                    CompoundTag itemTag = new CompoundTag();
                    itemTag.putInt("Slot", i);
                    stack1.save(itemTag);
                    nbtTagList.add(itemTag);
                }
            }
            tag.put("ClientInventory", nbtTagList);
        }

        return tag;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new PouchCapability(stack);
    }

    static class PouchCapability implements ICapabilityProvider {
        private final ItemStack stack;
        private LazyOptional<IItemHandler> optional = LazyOptional.empty();

        public PouchCapability(ItemStack stack){
            this.stack = stack;
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
                if (!optional.isPresent())
                    optional = PouchManager.get().getCapability(stack);
                return optional.cast();
            }else{
                return LazyOptional.empty();
            }
        }
    }
}
