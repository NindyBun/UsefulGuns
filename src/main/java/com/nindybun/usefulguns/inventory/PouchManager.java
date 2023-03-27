package com.nindybun.usefulguns.inventory;

import com.nindybun.usefulguns.UsefulGuns;
import com.nindybun.usefulguns.items.PouchTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.WorldWorkerManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class PouchManager extends SavedData {
    private static final String NAME = UsefulGuns.MOD_ID + "_pouch_data";
    private static final HashMap<UUID, PouchData> data = new HashMap<>();
    public static final PouchManager blankManager = new PouchManager();


    public HashMap<UUID, PouchData> getMap() {
        return data;
    }

    public static PouchManager get() {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            return ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(PouchManager::load, PouchManager::new, NAME);
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


    public static PouchManager load(CompoundTag nbt) {
        if (nbt.contains("Pouches")){
            ListTag list = nbt.getList("Pouches", Tag.TAG_COMPOUND);
            list.forEach(pouchNBT -> PouchData.fromNBT((CompoundTag) pouchNBT).ifPresent(pouch -> data.put(pouch.getUuid(), pouch)));
        }
        return new PouchManager();
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag pouches = new ListTag();
        data.forEach((uuid, pouchData) -> pouches.add(pouchData.toNBT()));
        nbt.put("Pouches", pouches);
        return nbt;
    }
}
