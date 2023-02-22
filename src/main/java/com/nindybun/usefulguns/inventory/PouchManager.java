package com.nindybun.usefulguns.inventory;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.PouchTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class PouchManager extends WorldSavedData {
    private static final String NAME = UsefulGuns.MOD_ID + "_pouch_data";
    private static final HashMap<UUID, PouchData> data = new HashMap<>();
    public static final PouchManager blankManager = new PouchManager();

    public PouchManager(){
        super(NAME);
    }

    public HashMap<UUID, PouchData> getMap() {
        return data;
    }

    public static PouchManager get() {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            return ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage().computeIfAbsent(PouchManager::new, NAME);
        else
            return blankManager;
    }

    public Optional<PouchData> getPouch(UUID uuid){
        if (data.containsKey(uuid))
            return Optional.of(data.get(uuid));
        return Optional.empty();
    }

    public PouchData getOrCreatePouch(UUID uuid, PouchTypes type){
        return data.computeIfAbsent(uuid, id -> {
            setDirty();
            return new PouchData(id, type);
        });
    }

    public void removePouch(UUID uuid){
        getPouch(uuid).ifPresent(pouch -> {
            pouch.getOptional().invalidate();
            data.remove(uuid);
            setDirty();
        });
    }

    public LazyOptional<IItemHandler> getCapability(UUID uuid){
        if (data.containsKey(uuid))
            return data.get(uuid).getOptional();
        return LazyOptional.empty();
    }

    public LazyOptional<IItemHandler> getCapability(ItemStack stack){
        if (stack.getOrCreateTag().contains("UUID")){
            UUID uuid = stack.getTag().getUUID("UUID");
            if (data.containsKey(uuid))
                return data.get(uuid).getOptional();
        }
        return LazyOptional.empty();
    }


    @Override
    public void load(CompoundNBT nbt) {
        if (nbt.contains("Pouches")){
            ListNBT list = nbt.getList("Pouches", Constants.NBT.TAG_COMPOUND);
            list.forEach(pouchNBT -> PouchData.fromNBT((CompoundNBT) pouchNBT).ifPresent(pouch -> data.put(pouch.getUuid(), pouch)));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        ListNBT pouches = new ListNBT();
        data.forEach((uuid, pouchData) -> pouches.add(pouchData.toNBT()));
        nbt.put("Pouches", pouches);
        return nbt;
    }
}
