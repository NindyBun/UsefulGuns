package com.nindybun.usefulguns.data;

import com.nindybun.usefulguns.UsefulGuns;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;

public class Generator {
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(true, new Recipes(generator));
        generator.addProvider(true, new ItemModels(generator, existingFileHelper));
        generator.addProvider(true, new Lang(generator, "en_us"));
    }
}
