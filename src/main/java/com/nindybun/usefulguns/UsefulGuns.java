package com.nindybun.usefulguns;

import com.nindybun.usefulguns.data.Generator;
import com.nindybun.usefulguns.events.ClientEvents;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.items.bullets.LingeringBullet;
import com.nindybun.usefulguns.items.bullets.MiningBullet;
import com.nindybun.usefulguns.items.bullets.SplashBullet;
import com.nindybun.usefulguns.items.bullets.TippedBullet;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import com.nindybun.usefulguns.modRegistries.*;
import com.nindybun.usefulguns.network.PacketHandler;
import com.nindybun.usefulguns.crafting.TargetNBTIngredient;
import com.nindybun.usefulguns.gui.PouchScreen;
import com.nindybun.usefulguns.util.UtilMethods;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(UsefulGuns.MOD_ID)
public class UsefulGuns
{
    public static final String MOD_ID = "usefulguns";
    public static final Logger LOGGER = LogManager.getLogger();

    public UsefulGuns() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(UsefulGuns::registerCreativeTab);

        ModSounds.SOUNDS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModContainers.CONTAINERS.register(modEventBus);
        ModRecipes.RECIPES.register(modEventBus);
        ModEntities.ENTITY.register(modEventBus);
        modEventBus.addListener(Generator::gatherData);

        //RecipeUnlocker.register(MOD_ID, MinecraftForge.EVENT_BUS, 1);

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> CraftingHelper.register(TargetNBTIngredient.Serializer.NAME, TargetNBTIngredient.SERIALIZER));
        PacketHandler.register();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        MenuScreens.register(ModContainers.POUCH_CONTAINER.get(), PouchScreen::new);
        event.enqueueWork(()->{
            for (BoreKit.Kit kit : BoreKit.Kit.values())
                ItemProperties.register(UtilMethods.createBore(kit), new ResourceLocation(UsefulGuns.MOD_ID, "enchantment_id"), MiningBullet::getEnchantmentID);
        });
    }

    public static CreativeModeTab TAB;

    private static void registerCreativeTab(CreativeModeTabEvent.Register event){
        TAB = event.registerCreativeModeTab(new ResourceLocation(UsefulGuns.MOD_ID, "tab"), builder -> builder
                .icon(() -> new ItemStack(ModItems.ARMOR_PIERCING_BULLET.get()))
                .title(Component.translatable("itemGroup.usefulguns"))
                .displayItems((enabledFeatures, output, displayOperatorCreativeTab) -> {
                    ModItems.ITEMS.getEntries().forEach(itemRegistryObject -> {
                        Item item = itemRegistryObject.get();
                        output.accept(item);
                        if (item instanceof SplashBullet || item instanceof LingeringBullet){
                            for (Potion potion : ForgeRegistries.POTIONS){
                                if (potion != Potions.EMPTY) {
                                    output.accept(PotionUtils.setPotion(new ItemStack(item), potion));
                                }
                            }
                        }if (item instanceof TippedBullet) {
                            for (Potion potion : ForgeRegistries.POTIONS){
                                if (!potion.getEffects().isEmpty()) {
                                    output.accept(PotionUtils.setPotion(new ItemStack(item), potion));
                                }
                            }
                        }else if (item instanceof MiningBullet){
                            ItemStack stack0 = new ItemStack(item);
                            stack0.enchant(Enchantments.BLOCK_FORTUNE, 1);
                            output.accept(stack0);

                            stack0 = new ItemStack(item);
                            stack0.enchant(Enchantments.BLOCK_FORTUNE, 2);
                            output.accept(stack0);

                            stack0 = new ItemStack(item);
                            stack0.enchant(Enchantments.BLOCK_FORTUNE, 3);
                            output.accept(stack0);

                            stack0 = new ItemStack(item);
                            stack0.enchant(Enchantments.SILK_TOUCH, 1);
                            output.accept(stack0);
                        }else if (item instanceof BoreKit) {
                            ItemStack stack0 = new ItemStack(item);
                            stack0.getOrCreateTag().putInt(BoreKit.USES, ((BoreKit)stack0.getItem()).getKit().getUses());
                            output.accept(stack0);
                        }else if (item instanceof AbstractGun){
                            ItemStack stack0 = new ItemStack(item);
                            stack0.getOrCreateTag().putInt(AbstractGun.DIRTINESS, ((AbstractGun) item).getMaxDirtyness());
                            output.accept(stack0);
                        }
                    });
                })

        );
    }

}

