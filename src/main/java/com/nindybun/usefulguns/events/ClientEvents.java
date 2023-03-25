package com.nindybun.usefulguns.events;

import com.mojang.blaze3d.platform.InputConstants;
import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.gui.BulletRadialMenu;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;


@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = UsefulGuns.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {
    public static KeyMapping radialMenu_key;

    public static void init(){
        ClientRegistry.registerKeyBinding(radialMenu_key = new KeyMapping("key."+ UsefulGuns.MOD_ID +".radialmenu_key", GLFW.GLFW_KEY_V, "key.categories."+UsefulGuns.MOD_ID));
    }

    private static boolean keyWasDown = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){
        if (event.phase != TickEvent.Phase.START)
            return;

        Player player = Minecraft.getInstance().player;
        if (Minecraft.getInstance().screen == null){

            ItemStack main = player.getMainHandItem();
            ItemStack off = player.getOffhandItem();
            boolean isHoldingGun = false;

            if (main.getItem() instanceof AbstractGun || off.getItem() instanceof AbstractGun)
                isHoldingGun = true;

            boolean keyIsDown = radialMenu_key.isDown();
            if (keyIsDown && !keyWasDown){
                while (radialMenu_key.consumeClick() && isHoldingGun) {
                    if (Minecraft.getInstance().screen == null){
                        Minecraft.getInstance().setScreen(new BulletRadialMenu(player));
                    }
                }
            }
            keyWasDown = keyIsDown;
        }else{
            keyWasDown = true;
        }

    }

    public static boolean isKeyDown(KeyMapping keybind){
        if (keybind.isUnbound())
            return false;
        boolean isDown = switch (keybind.getKey().getType()){
            case KEYSYM -> InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keybind.getKey().getValue());
            case MOUSE -> GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), keybind.getKey().getValue()) == GLFW.GLFW_PRESS;
            default -> false;
        };
        return isDown && keybind.getKeyConflictContext().isActive() && keybind.getKeyModifier().isActive(keybind.getKeyConflictContext());
    }

    public static void wipeOpen(){
        while(radialMenu_key.consumeClick()){}
    }
}
