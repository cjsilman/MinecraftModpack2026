package dev.csilman.modpackutils.util.altar.siege;

import java.util.List;

public class SiegeWaveDefinitions {
    public static final List<SiegeWave> WAVES = List.of(
            new SiegeWave(
                    "",
                    "§4Consequence",
                    List.of(
                           new SiegeWave.WaveEntry("cataclysm:clawdian", 12, null)

                    )
            ),
            new SiegeWave(
                    "",
                    "§6Opulence",
                    List.of(
                           new SiegeWave.WaveEntry("royalvariations:royal_skeleton", 12, null),
                           new SiegeWave.WaveEntry("minecraft:skeleton", 24, null),
                           new SiegeWave.WaveEntry("royalvariations:royal_zombie", 12, null)
                    )
            ),
            new SiegeWave(
                    "",
                    "Test2",
                    List.of(
                            new SiegeWave.WaveEntry("minecraft:skeleton", 2, null)
                    )
            )
    );

    public static int totalWaves() {
        return WAVES.size();
    }

    public static SiegeWave getWave(int index) {
        return WAVES.get(index);
    }
}
