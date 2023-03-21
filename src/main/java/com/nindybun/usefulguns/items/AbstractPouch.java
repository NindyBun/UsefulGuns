package com.nindybun.usefulguns.items;

import com.nindybun.usefulguns.inventory.PouchHandler;
import com.nindybun.usefulguns.items.PouchTypes;
import com.nindybun.usefulguns.gui.PouchContainer;
import com.nindybun.usefulguns.inventory.PouchData;
import com.nindybun.usefulguns.inventory.PouchManager;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.text.html.Option;
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
        return getType(stack) == PouchTypes.NETHERSTAR;
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
        CompoundNBT tag = stack.getOrCreateTag();
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
    public ITextComponent getName(@Nonnull ItemStack stack){
        return new TranslationTextComponent(this.getDescriptionId(stack)).withStyle(TextFormatting.RESET);
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
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack pouch = player.getItemInHand(hand);

        if (!world.isClientSide && player instanceof ServerPlayerEntity && pouch.getItem() instanceof AbstractPouch){
            PouchData data = AbstractPouch.getData(pouch);

            PouchTypes itemType = ((AbstractPouch) pouch.getItem()).type;
            UUID uuid = data.getUuid();

            //data.updateAccessRecords(player.getName().getString(), System.currentTimeMillis());

            if (data.getType().ordinal() < itemType.ordinal())
                data.upgrade(itemType);

            NetworkHooks.openGui(((ServerPlayerEntity) player), new SimpleNamedContainerProvider(
                    (windowId, playerInventory, playerEntity) ->
                            new PouchContainer(windowId, playerInventory, uuid, data.getType(), (PouchHandler) data.getHandler()), pouch.getHoverName()),
                    (buffer -> buffer.writeUUID(uuid).writeInt(data.getType().ordinal())));
        }

        return ActionResult.success(pouch);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        if (flag.isAdvanced() && stack.getTag() != null && stack.getTag().contains("UUID")) {
            UUID uuid = stack.getTag().getUUID("UUID");
            tooltip.add(new StringTextComponent("ID: " + uuid.toString().substring(0, 8)).withStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
        }
        if (stack.getItem().isFireResistant())
            tooltip.add(new StringTextComponent("Fire Resistant!").withStyle(TextFormatting.GOLD));
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        super.readShareTag(stack, nbt);
        LazyOptional<IItemHandler> optional = AbstractPouch.getData(stack).getOptional();
        if (optional.isPresent()){
            IItemHandler handler = optional.resolve().get();
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().readNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, handler, null, nbt.get("ClientInventory"));
        }
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        LazyOptional<IItemHandler> optional = AbstractPouch.getData(stack).getOptional();
        CompoundNBT tag = super.getShareTag(stack);
        if (optional.isPresent()){
            IItemHandler handler = optional.resolve().get();
            INBT capTag = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().writeNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, handler, null);
            tag.put("ClientInventory", capTag);
        }
        return tag;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
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
