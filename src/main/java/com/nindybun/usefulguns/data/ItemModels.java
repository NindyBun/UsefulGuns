package com.nindybun.usefulguns.data;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.modRegistries.ModItems;
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
        registerPouchs();
        //registerGuns(); //Using a different model for the guns so these won't work
        registerBullets();
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
        simpleItem(ModItems.FLINT_BULLET.get());
        simpleItem(ModItems.IRON_BULLET.get());
        simpleItem(ModItems.GLASS_BULLET.get());
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

    private void registerPouchs(){
        simpleItem(ModItems.LEATHER_POUCH.get());
        simpleItem(ModItems.IRON_POUCH.get());
        simpleItem(ModItems.GOLD_POUCH.get());
        simpleItem(ModItems.DIAMOND_POUCH.get());
        simpleItem(ModItems.OBSIDIAN_POUCH.get());
        simpleItem(ModItems.NETHERITE_POUCH.get());
        simpleItem(ModItems.NETHERSTAR_POUCH.get());
    }

    private ItemModelBuilder simpleItem(Item item){
        String name = item.getRegistryName().getPath();
        return singleTexture(name, mcLoc("item/handheld"), "layer0", new ResourceLocation(UsefulGuns.MOD_ID, "items/"+name));
    }
}
