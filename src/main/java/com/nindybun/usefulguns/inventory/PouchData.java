package com.nindybun.usefulguns.inventory;

import com.nindybun.usefulguns.items.PouchTypes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.Optional;
import java.util.UUID;

public class PouchData {
    private final UUID uuid;
    private PouchTypes type;
    private final PouchHandler inventory;
    private final LazyOptional<IItemHandler> optional;
    //public final Metadata meta = new Metadata();

    public LazyOptional<IItemHandler> getOptional() {
        return optional;
    }

    public IItemHandler getHandler(){
        return inventory;
    }

    public PouchTypes getType(){
        return type;
    }

    /*public void updateAccessRecords(String player, long time) {
        if (meta.firstAccessedTime == 0){
            meta.firstAccessedTime = time;
            meta.firstAccessedPlayer = player;
        }

        meta.setLastAccessedTime(time);
        meta.setLastAccessedPlayer(player);
    }*/

    public PouchData(UUID uuid, PouchTypes type){
        this.uuid = uuid;
        this.type = type;
        inventory = new PouchHandler(type.slots);
        optional = LazyOptional.of(() -> inventory);
    }

    public PouchData(UUID uuid, CompoundNBT nbt) {
        this.uuid = uuid;
        this.type = PouchTypes.values()[Math.min(nbt.getInt("Type"), PouchTypes.NETHERSTAR.ordinal())];
        inventory = new PouchHandler(type.slots);

        if (nbt.getCompound("Inventory").contains("Size")){
            if (nbt.getCompound("Inventory").getInt("Size") != type.slots)
                nbt.getCompound("Inventory").putInt("Size", type.slots);
        }
        inventory.deserializeNBT(nbt.getCompound("Inventory"));
        optional = LazyOptional.of(() -> inventory);

        /*if (nbt.contains("Metadata"))
            meta.deserializeNBT(nbt.getCompound("Metadata"));*/
    }

    public UUID getUuid(){
        return uuid;
    }

    public static Optional<PouchData> fromNBT(CompoundNBT nbt){
        if (nbt.contains("UUID")){
            UUID uuid = nbt.getUUID("UUID");
            return Optional.of(new PouchData(uuid, nbt));
        }
        return Optional.empty();
    }

    public void upgrade(PouchTypes newType){
        if (newType.ordinal() > type.ordinal()){
            type = newType;
            inventory.upgrade(type.slots);
        }
    }

    public CompoundNBT toNBT(){
        CompoundNBT nbt = new CompoundNBT();

        nbt.putUUID("UUID", uuid);
        nbt.putInt("Type", type.ordinal());
        nbt.put("Inventory", inventory.serializeNBT());
        //nbt.put("Metadata", meta.serializeNBT());

        return nbt;
    }


    /*public static class Metadata implements INBTSerializable<CompoundNBT> {

        private String firstAccessedPlayer = "";

        private long firstAccessedTime = 0;
        private String lastAccessedPlayer = "";
        private long lastAccessedTime = 0;
        public long getLastAccessedTime() {
            return lastAccessedTime;
        }

        public void setLastAccessedTime(long lastAccessedTime) {
            this.lastAccessedTime = lastAccessedTime;
        }

        public String getLastAccessedPlayer() {
            return lastAccessedPlayer;
        }

        public void setLastAccessedPlayer(String lastAccessedPlayer) {
            this.lastAccessedPlayer = lastAccessedPlayer;
        }

        public long getFirstAccessedTime() {
            return firstAccessedTime;
        }

        public String getFirstAccessedPlayer() {
            return firstAccessedPlayer;
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();

            nbt.putString("firstPlayer", firstAccessedPlayer);
            nbt.putLong("firstTime", firstAccessedTime);
            nbt.putString("lastPlayer", lastAccessedPlayer);
            nbt.putLong("lastTime", lastAccessedTime);

            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            firstAccessedPlayer = nbt.getString("firstPlayer");
            firstAccessedTime = nbt.getLong("firstTime");
            lastAccessedPlayer = nbt.getString("lastPlayer");
            lastAccessedTime = nbt.getLong("lastTime");
        }
    }*/


}
