package com.nindybun.usefulguns.data;

import com.nindybun.usefulguns.UsefulGuns;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class Generator {
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(new Recipes(generator));
        generator.addProvider(new ItemModels(generator, existingFileHelper));
        generator.addProvider(new Lang(generator, "en_us"));
    }
}
