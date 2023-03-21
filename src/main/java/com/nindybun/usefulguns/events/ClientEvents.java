package com.nindybun.usefulguns.events;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.gui.BulletRadialMenu;
import com.nindybun.usefulguns.items.guns.AbstractGun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static net.minecraft.client.util.InputMappings.Type.KEYSYM;
import static net.minecraft.client.util.InputMappings.Type.MOUSE;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = UsefulGuns.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {
    public static KeyBinding radialMenu_key;

    public static void init(){
        ClientRegistry.registerKeyBinding(radialMenu_key = new KeyBinding("key."+ UsefulGuns.MOD_ID +".radialmenu_key", GLFW.GLFW_KEY_V, "key.categories."+UsefulGuns.MOD_ID));
    }

    private static boolean keyWasDown = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){
        if (event.phase != TickEvent.Phase.START)
            return;

        PlayerEntity player = Minecraft.getInstance().player;
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

    public static boolean isKeyDown(KeyBinding keybind){
        if (keybind.isUnbound())
            return false;
        boolean isDown = false;
        if (keybind.getKey().getType() == KEYSYM)
            isDown = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keybind.getKey().getValue());
        else if (keybind.getKey().getType() == MOUSE)
            isDown = GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), keybind.getKey().getValue()) == GLFW.GLFW_PRESS;
        return isDown && keybind.getKeyConflictContext().isActive() && keybind.getKeyModifier().isActive(keybind.getKeyConflictContext());
    }

    public static void wipeOpen(){
        while(radialMenu_key.consumeClick()){}
    }
}
