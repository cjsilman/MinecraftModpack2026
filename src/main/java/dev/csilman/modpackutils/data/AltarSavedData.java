package dev.csilman.modpackutils.data;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.util.altar.AltarEventPhase;
import dev.csilman.modpackutils.util.altar.siege.SiegePhase;
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

    private AltarEventPhase altarPhase = AltarEventPhase.DORMANT;
    private int siegeWave = 0;
    private int ticksInPhase = 0;
    private int ticksInWave = 0;
    private boolean bossSpawned = false;
    private Set<BlockPos> registeredPedestals = new HashSet<>();
    private boolean pedestalsScanned = false; // Expecting to only need to scan ONCE on server start, as all pedestals will exist at the start of the game.
    private BlockPos altarMidpoint = new BlockPos(0, 0, 0);
    private boolean waveSpawned = false;
    private int siegeParticleStage = 0;
    private int delayWaveStartCounter = 0;
    private SiegePhase siegePhase = SiegePhase.NONE;


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
        // Larger Scope Data
        data.altarPhase = AltarEventPhase.valueOf(compoundTag.getString("phase"));
        data.ticksInPhase = compoundTag.getInt("ticksInPhase");
        data.pedestalsScanned = compoundTag.getBoolean("pedestalsScanned");

        // Siege Specific
        data.ticksInWave = compoundTag.getInt("ticksInWave");
        data.waveSpawned = compoundTag.getBoolean("waveSpawned");
        data.siegeParticleStage = compoundTag.getInt("siegeParticleStage");
        data.delayWaveStartCounter = compoundTag.getInt("delayWaveStartCounter");
        data.bossSpawned = compoundTag.getBoolean("bossSpawned");
        data.siegeWave = compoundTag.getInt("siegeWave");
        data.siegePhase = SiegePhase.valueOf(compoundTag.getString("siegePhase"));


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
        compoundTag.putString("phase", altarPhase.name());
        compoundTag.putInt("ticksInPhase", ticksInPhase);
        compoundTag.putBoolean("pedestalsScanned", pedestalsScanned);

        compoundTag.putInt("ticksInWave", ticksInWave);
        compoundTag.putBoolean("waveSpawned", waveSpawned);
        compoundTag.putInt("siegeParticleStage", siegeParticleStage);
        compoundTag.putInt("delayWaveStartCounter", delayWaveStartCounter);
        compoundTag.putBoolean("bossSpawned", bossSpawned);
        compoundTag.putInt("siegeWave", siegeWave);
        compoundTag.putString("siegePhase", siegePhase.name());

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

    public AltarEventPhase getAltarPhase() {
        return altarPhase;
    }

    public void setAltarPhase(AltarEventPhase altarPhase) {
        this.altarPhase = altarPhase;
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

    public int getTicksInWave() {
        return ticksInWave;
    }

    public void setTicksInWave(int ticksInWave) {
        this.ticksInWave = ticksInWave;
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

    public void incrementPhaseTicks() {
        this.ticksInPhase++;
        setDirty();
    }

    public void incrementWaveTicks() {
        this.ticksInWave++;
        setDirty();
    }

    public BlockPos getAltarMidpoint() {
        return altarMidpoint;
    }

    public void setAltarMidpoint(BlockPos altarMidpoint) {
        this.altarMidpoint = altarMidpoint;
        setDirty();
    }

    public boolean isWaveSpawned() {
        return waveSpawned;
    }

    public void setWaveSpawned(boolean waveSpawned) {
        this.waveSpawned = waveSpawned;
        setDirty();
    }

    public int getSiegeParticleStage() {
        return siegeParticleStage;
    }

    public void setSiegeParticleStage(int siegeParticleStage) {
        this.siegeParticleStage = siegeParticleStage;
        setDirty();
    }

    public int getDelayWaveStartCounter() {
        return delayWaveStartCounter;
    }

    public void setDelayWaveStartCounter(int delayWaveStartCounter) {
        this.delayWaveStartCounter = delayWaveStartCounter;
        setDirty();
    }

    public SiegePhase getSiegePhase() {
        return siegePhase;
    }

    public void setSiegePhase(SiegePhase siegePhase) {
        this.siegePhase = siegePhase;
        setDirty();
    }

    public boolean isDormant()  { return altarPhase == AltarEventPhase.DORMANT; }
    public boolean isAwakening(){ return altarPhase == AltarEventPhase.AWAKENING; }
    public boolean isSiege()    { return altarPhase == AltarEventPhase.SIEGE; }
    public boolean isBoss() { return altarPhase == AltarEventPhase.BOSS; }
    public boolean isDefeated() { return altarPhase == AltarEventPhase.DEFEATED; }
}
