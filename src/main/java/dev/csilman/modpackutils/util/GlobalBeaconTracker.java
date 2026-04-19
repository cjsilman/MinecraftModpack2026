package dev.csilman.modpackutils.util;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.block.custom.BossBeaconBlock;
import dev.csilman.modpackutils.data.AltarSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class GlobalBeaconTracker {
    public static int countActiveBeacons(ServerLevel overworld) {
        AltarSavedData data = AltarSavedData.get(overworld);
        int count = 0;

        for (BlockPos pedestalPos : data.getRegisteredPedestals()) {
            if (isBeaconActiveAt(overworld, pedestalPos)) {
                count++;
            }
        }

        ModpackUtilsMod.LOGGER.info(
                "[ModpackUtils] There are currently {}/{} beacon(s) active", count, data.getRegisteredPedestals().size()
        );

        return count;
    }

    public static int countRegisteredPedestals(ServerLevel overworld) {
        return AltarSavedData.get(overworld).getRegisteredPedestals().size();
    }

    public static boolean areAllBeaconsActive(ServerLevel overworld) {
        int total = countRegisteredPedestals(overworld);

        if (total == 0) return false;

        return (countActiveBeacons(overworld) >= total);
    }

    public static boolean isBeaconActiveAt(ServerLevel level, BlockPos pedestalPos) {
        BlockPos abovePos = pedestalPos.above();

        if (!level.isLoaded(abovePos)) return false;

        BlockState above = level.getBlockState(abovePos);

        if (!(above.getBlock() instanceof BossBeaconBlock)) return false;

        return above.getValue(BossBeaconBlock.ACTIVE);

    }

    public static void onBeaconStateChanged(ServerLevel overworld) {
        AltarSavedData data = AltarSavedData.get(overworld);

        ModpackUtilsMod.LOGGER.info(
                "[ModpackUtils] Current state is: {}. Checking if all beacons active.", data.getPhase()
        );

        if (!data.isDormant()) return;

        if (areAllBeaconsActive(overworld)) {
            ModpackUtilsMod.LOGGER.info(
                    "[ModpackUtils] All beacons are active! Shifting to AWAKENING."
            );

            data.setPhase(AltarEventPhase.AWAKENING);
        }

    }



}
