package com.nindybun.usefulguns.data;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.BoreKit;
import com.nindybun.usefulguns.modRegistries.ModBlocks;
import com.nindybun.usefulguns.modRegistries.ModItems;
import com.nindybun.usefulguns.util.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.jws.soap.SOAPBinding;

public class ItemModels extends ItemModelProvider {
    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper){
        super(generator, UsefulGuns.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //registerGuns(); //Using a different model for the guns so these won't work
        registerPouchs();
        registerBullets();
        registerBoreKits();
        registerCleaners();
    }

    private void registerBullets(){
        withExistingParent(ModItems.TIPPED_BULLET.get().getRegistryName().getPath(), mcLoc("item/handheld"))
                .texture("layer0", new ResourceLocation(UsefulGuns.MOD_ID, "items/bullet_head"))
                .texture("layer1", new ResourceLocation(UsefulGuns.MOD_ID, "items/bullet_base"));
        withExistingParent(ModItems.SPLASH_BULLET.get().getRegistryName().getPath(), mcLoc("item/handheld"))
                .texture("layer0", new ResourceLocation(UsefulGuns.MOD_ID, "items/glass_bullet_head"))
                .texture("layer1", new ResourceLocation(UsefulGuns.MOD_ID, "items/glass_bullet"));
        withExistingParent(ModItems.LINGERING_BULLET.get().getRegistryName().getPath(), mcLoc("item/handheld"))
                .texture("layer0", new ResourceLocation(UsefulGuns.MOD_ID, "items/glass_bullet_head"))
                .texture("layer1", new ResourceLocation(UsefulGuns.MOD_ID, "items/lingering_bullet"));
        withExistingParent(ModItems.BULLET_CASING.get().getRegistryName().getPath(), mcLoc("item/handheld"))
                .texture("layer0", new ResourceLocation(UsefulGuns.MOD_ID, "items/bullet_base"));
        simpleItem(ModItems.DRAGONS_FIREBALL_BULLET.get());
        simpleItem(ModItems.DRAGONS_BREATH_BULLET.get());
        simpleItem(ModItems.SLUG_BULLET.get());
        simpleItem(ModItems.BIRDSHOT_BULLET.get());
        simpleItem(ModItems.BUCKSHOT_BULLET.get());
        simpleItem(ModItems.FLINT_BULLET.get());
        simpleItem(ModItems.IRON_BULLET.get());
        simpleItem(ModItems.DIAMOND_BULLET.get());
        simpleItem(ModItems.GLASS_BULLET.get());
        simpleItem(ModItems.ARMOR_PIERCING_BULLET.get());
        simpleItem(ModItems.HOLLOW_POINT_BULLET.get());
        simpleItem(ModItems.EXPLOSIVE_BULLET.get());
        simpleItem(ModItems.ENDER_BULLET.get());
        simpleItem(ModItems.TORCH_BULLET.get());

        for (BoreKit.Kit kit : BoreKit.Kit.values()){
            String name = Util.createBore(kit).getRegistryName().getPath();
            withExistingParent(name, mcLoc("item/generated"))
                    .texture("layer0", new ResourceLocation(UsefulGuns.MOD_ID, "items/"+name))
                    .override().predicate(new ResourceLocation(UsefulGuns.MOD_ID, "enchantment_id"), 0).model(
                            withExistingParent(name+"_0", mcLoc("item/generated"))
                                    .texture("layer0", new ResourceLocation(UsefulGuns.MOD_ID, "items/"+name))).end()
                    .override().predicate(new ResourceLocation(UsefulGuns.MOD_ID, "enchantment_id"), 1).model(
                            withExistingParent(name+"_1", mcLoc("item/generated"))
                                    .texture("layer0", new ResourceLocation(UsefulGuns.MOD_ID, "items/"+name))
                                    .texture("layer1", new ResourceLocation(UsefulGuns.MOD_ID, "items/fortune1_overlay"))).end()
                    .override().predicate(new ResourceLocation(UsefulGuns.MOD_ID, "enchantment_id"), 2).model(
                            withExistingParent(name+"_2", mcLoc("item/generated"))
                                    .texture("layer0", new ResourceLocation(UsefulGuns.MOD_ID, "items/"+name))
                                    .texture("layer1", new ResourceLocation(UsefulGuns.MOD_ID, "items/fortune2_overlay"))).end()
                    .override().predicate(new ResourceLocation(UsefulGuns.MOD_ID, "enchantment_id"), 3).model(
                            withExistingParent(name+"_3", mcLoc("item/generated"))
                                    .texture("layer0", new ResourceLocation(UsefulGuns.MOD_ID, "items/"+name))
                                    .texture("layer1", new ResourceLocation(UsefulGuns.MOD_ID, "items/fortune3_overlay"))).end()
                    .override().predicate(new ResourceLocation(UsefulGuns.MOD_ID, "enchantment_id"), 4).model(
                            withExistingParent(name+"_4", mcLoc("item/generated"))
                                    .texture("layer0", new ResourceLocation(UsefulGuns.MOD_ID, "items/"+name))
                                    .texture("layer1", new ResourceLocation(UsefulGuns.MOD_ID, "items/silktouch_overlay"))).end()
            ;
        }



        simpleItem(ModItems.STONE_MINING_BULLET.get());
        simpleItem(ModItems.IRON_MINING_BULLET.get());
        simpleItem(ModItems.GOLD_MINING_BULLET.get());
        simpleItem(ModItems.DIAMOND_MINING_BULLET.get());
        simpleItem(ModItems.NETHERITE_MINING_BULLET.get());
    }

    private void registerCleaners(){
        simpleItem(ModItems.CLEANER.get());
        simpleItem(ModItems.BETTER_CLEANER.get());
        simpleItem(ModItems.BEST_CLEANER.get());
        simpleItem(ModItems.ULTIMATE_CLEANER.get());
    }

    private void registerBoreKits(){
        simpleItem(ModItems.WOOD_BORE_KIT.get());
        simpleItem(ModItems.STONE_BORE_KIT.get());
        simpleItem(ModItems.IRON_BORE_KIT.get());
        simpleItem(ModItems.GOLD_BORE_KIT.get());
        simpleItem(ModItems.DIAMOND_BORE_KIT.get());
        simpleItem(ModItems.NETHERITE_BORE_KIT.get());
    }

    private void registerPouchs(){
        simpleItem(ModItems.LEATHER_POUCH.get());
        simpleItem(ModItems.IRON_POUCH.get());
        simpleItem(ModItems.GOLD_POUCH.get());
        simpleItem(ModItems.DIAMOND_POUCH.get());
        simpleItem(ModItems.OBSIDIAN_POUCH.get());
        simpleItem(ModItems.NETHERITE_POUCH.get());
        simpleItem(ModItems.NETHERSTAR_POUCH.get());
        simpleItem(ModItems.OMEGA_POUCH.get());
    }

    private void registerGuns(){
        simpleItem(ModItems.IRON_GUN.get()).transforms()
                .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT).translation(0, 0.5f, -1).rotation(0, -90, 0).scale(1).end()
                .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT).translation(0, 0.5f, -1).rotation(0, 90, 0).scale(1).end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).translation(0, 2.5f, 0).rotation(0, -90, 0).scale(1).end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT).translation(0, 2.5f, 0).rotation(0, 90, 0).scale(1).end();
        simpleItem(ModItems.GOLD_GUN.get()).transforms()
                .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT).translation(0, 0.5f, -1).rotation(0, -90, 0).scale(1).end()
                .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT).translation(0, 0.5f, -1).rotation(0, 90, 0).scale(1).end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).translation(0, 2.5f, 0).rotation(0, -90, 0).scale(1).end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT).translation(0, 2.5f, 0).rotation(0, 90, 0).scale(1).end();
        simpleItem(ModItems.DIAMOND_SNIPER.get()).transforms()
                .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT).translation(0, 0.5f, -6).rotation(0, -90, 0).scale(2, 2, 1).end()
                .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT).translation(0, 0.5f, -6).rotation(0, 90, 0).scale(2, 2, 1).end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).translation(0, 3.0f, 0).rotation(0, -90, 0).scale(2, 2, 1).end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT).translation(0, 3.0f, 0).rotation(0, 90, 0).scale(2, 2, 1).end()
                .transform(ModelBuilder.Perspective.GUI).rotation(0, -30, 0).scale(2, 2, 1).end();
    }

    private ItemModelBuilder simpleItem(Item item){
        String name = item.getRegistryName().getPath();
        return singleTexture(name, mcLoc("item/handheld"), "layer0", new ResourceLocation(UsefulGuns.MOD_ID, "items/"+name));
    }
}
