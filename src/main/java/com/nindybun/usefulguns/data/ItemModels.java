package com.nindybun.usefulguns.data;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.modRegistries.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {
    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper){
        super(generator, UsefulGuns.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerPouchs();
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

    private void simpleItem(Item item){
        String name = item.getRegistryName().getPath();
        singleTexture(name, mcLoc("item/handheld"), "layer0", new ResourceLocation(UsefulGuns.MOD_ID, "items/"+name));
    }
}
