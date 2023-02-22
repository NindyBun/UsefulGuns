package com.nindybun.usefulguns.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class Generator {
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        generator.addProvider(new Recipes(generator));
    }
}
