package dev.csilman.modpackutils.data;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.util.AltarEventPhase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AltarSavedData extends SavedData {

    private static final String DATA_KEY = ModpackUtilsMod.MOD_ID + "_altar_state";

    private AltarEventPhase phase = AltarEventPhase.DORMANT;
    private int siegeWave = 0;
    private int ticksInPhase = 0;
    private boolean bossSpawned = false;
    private Set<BlockPos> registeredPedestals = new HashSet<>();
    // Expecting to only need to scan ONCE on server start, as all pedestals will exist at the start of the game.
    private boolean pedestalsScanned = false;
    private BlockPos altarMidpoint = new BlockPos(0, 0, 0);

    public static AltarSavedData get(ServerLevel overworld) {
        return overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                    AltarSavedData::new,
                    AltarSavedData::load
                ),
                DATA_KEY
        );
    }

    public static AltarSavedData load(CompoundTag compoundTag, HolderLookup.Provider provider) {
        AltarSavedData data = new AltarSavedData();
        data.phase = AltarEventPhase.valueOf(compoundTag.getString("phase"));
        data.siegeWave = compoundTag.getInt("siegeWave");
        data.ticksInPhase = compoundTag.getInt("ticksInPhase");
        data.bossSpawned = compoundTag.getBoolean("bossSpawned");
        data.pedestalsScanned = compoundTag.getBoolean("pedestalsScanned");

        if (compoundTag.contains("pedestals")) {
            int[] raw = compoundTag.getIntArray("pedestals");
            for (int i = 0; i+2 < raw.length; i += 3) {
                data.registeredPedestals.add(new BlockPos(raw[i], raw[i+1], raw[i+2]));
            }
        }

        int[] altarMidpointPos = compoundTag.getIntArray("altarMidpointPos");
        data.altarMidpoint = new BlockPos(altarMidpointPos[0], altarMidpointPos[1], altarMidpointPos[2]);

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putString("phase", phase.name());
        compoundTag.putInt("siegeWave", siegeWave);
        compoundTag.putInt("ticksInPhase", ticksInPhase);
        compoundTag.putBoolean("bossSpawned", bossSpawned);
        compoundTag.putBoolean("pedestalsScanned", pedestalsScanned);

        // # of pedestals registered at first server run (See GlobalBeaconTracker)
        int[] raw = new int[registeredPedestals.size()*3];
        int i = 0;
        for (BlockPos pos : registeredPedestals) {
            raw[i++] = pos.getX();
            raw[i++] = pos.getY();
            raw[i++] = pos.getZ();
        }
        compoundTag.putIntArray("pedestals", raw);

        // Altar midpoint calculated at first server run (See GlobalBeaconTracker)
        int[] altarMidpointPosition = new int[3];
        altarMidpointPosition[0] = altarMidpoint.getX();
        altarMidpointPosition[1] = altarMidpoint.getY();
        altarMidpointPosition[2] = altarMidpoint.getZ();
        compoundTag.putIntArray("altarMidpointPos", altarMidpointPosition);

        return compoundTag;
    }

    public AltarEventPhase getPhase() {
        return phase;
    }

    public void setPhase(AltarEventPhase phase) {
        this.phase = phase;
        setDirty();
    }

    public int getSiegeWave() {
        return siegeWave;
    }

    public void setSiegeWave(int siegeWave) {
        this.siegeWave = siegeWave;
        setDirty();
    }

    public int getTicksInPhase() {
        return ticksInPhase;
    }

    public void setTicksInPhase(int ticksInPhase) {
        this.ticksInPhase = ticksInPhase;
        setDirty();
    }

    public boolean isBossSpawned() {
        return bossSpawned;
    }

    public void setBossSpawned(boolean bossSpawned) {
        this.bossSpawned = bossSpawned;
        setDirty();
    }

    public void registerPedestal(BlockPos pos) {
        registeredPedestals.add(pos);
        setDirty();
    }

    public void unregisterPedestal(BlockPos pos) {
        registeredPedestals.remove(pos);
        setDirty();;
    }

    public boolean isPedestalsScanned() {
        return pedestalsScanned;
    }

    public void setPedestalsScanned(boolean pedestalsScanned) {
        this.pedestalsScanned = pedestalsScanned;
        setDirty();
    }

    public Set<BlockPos> getRegisteredPedestals() {
        return Collections.unmodifiableSet(registeredPedestals);
    }

    public void incrementTicks() {
        this.ticksInPhase++;
        setDirty();
    }

    public BlockPos getAltarMidpoint() {
        return altarMidpoint;
    }

    public void setAltarMidpoint(BlockPos altarMidpoint) {
        this.altarMidpoint = altarMidpoint;
        setDirty();
    }

    public boolean isDormant()  { return phase == AltarEventPhase.DORMANT; }
    public boolean isAwakening(){ return phase == AltarEventPhase.AWAKENING; }
    public boolean isSiege()    { return phase == AltarEventPhase.SIEGE; }
    public boolean isDefeated() { return phase == AltarEventPhase.DEFEATED; }
}
