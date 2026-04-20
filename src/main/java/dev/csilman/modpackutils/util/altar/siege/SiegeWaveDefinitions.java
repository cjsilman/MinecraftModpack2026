package dev.csilman.modpackutils.util.altar.siege;

import java.util.List;

public class SiegeWaveDefinitions {
    public static final List<SiegeWave> WAVES = List.of(
            new SiegeWave(
                    "",
                    "Test1",
                    List.of(
                           new SiegeWave.WaveEntry("cataclysm:elite_draugr", 6, "TestName")
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
