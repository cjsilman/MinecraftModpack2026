package dev.csilman.modpackutils.util.altar;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.block.custom.BeaconPedestalBlock;
import dev.csilman.modpackutils.data.AltarSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class AltarStructuresScanner {

    private static final int SCAN_RADIUS = 150;
    private static final int SCAN_MIN_Y = 40;
    private static final int SCAN_MAX_Y = 200;

    public static void scanIfNeeded(ServerLevel overworld) {
        AltarSavedData data = AltarSavedData.get(overworld);

        if (data.isPedestalsScanned()) {
            return;
        }

        int found = 0;

        for(int x = -SCAN_RADIUS; x <= SCAN_RADIUS; x++) {
            for (int z = -SCAN_RADIUS; z <= SCAN_RADIUS; z++) {
                for (int y = SCAN_MIN_Y; y <= SCAN_MAX_Y; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = overworld.getBlockState(pos);

                    if (state.getBlock() instanceof BeaconPedestalBlock pedestal) {
                        ModpackUtilsMod.LOGGER.info(
                                "[ModpackUtils] Found BeaconPedestalBlock x: {}, y: {}, z: {}", x, y, z
                        );
                        data.registerPedestal(pos);
                        found++;
                    }
                };
            }
        }

        if (found > 0) {
            data.setPedestalsScanned(true);

            BlockPos altarMidpoint = triangulateAltarMidpoint(data);
            data.setAltarMidpoint(altarMidpoint);

            ModpackUtilsMod.LOGGER.info(
                    "[ModpackUtils] Altar scan complete. Found {} pedestals", found
            );
        } else {
            ModpackUtilsMod.LOGGER.info(
                    "[ModpackUtils] Altar scan found 0 pedestals, will retry on next server startup.", found
            );
        }

    }

    private static BlockPos triangulateAltarMidpoint(AltarSavedData data) {
        Set<BlockPos> allPedestalPositions = data.getRegisteredPedestals();
        int sumOfXCoords = 0;
        int sumOfYCoords = 0;
        int sumOfZCoords = 0;
        int numberOfPedestals = allPedestalPositions.size();

        for (BlockPos pos : allPedestalPositions) {
            sumOfXCoords += pos.getX();
            sumOfYCoords += pos.getY();
            sumOfZCoords += pos.getZ();
        }

        BlockPos altarMidpoint = new BlockPos(
                sumOfXCoords/numberOfPedestals,
                sumOfYCoords/numberOfPedestals,
                sumOfZCoords/numberOfPedestals
        );

        ModpackUtilsMod.LOGGER.info(
                "[ModpackUtils] Found altar midpoint at x: {} y: {} z: {}",
                altarMidpoint.getX(),
                altarMidpoint.getY(),
                altarMidpoint.getZ()
        );

        return altarMidpoint;

    }

}
