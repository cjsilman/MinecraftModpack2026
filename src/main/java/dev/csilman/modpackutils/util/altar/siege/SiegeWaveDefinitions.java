package dev.csilman.modpackutils.util.altar.siege;

import java.util.List;

public class SiegeWaveDefinitions {
    public static final List<SiegeWave> WAVES = List.of(
            new SiegeWave(
                    "1/4",
                    "§cConsequence",
                    List.of(
                           new SiegeWave.WaveEntry("fdbosses:fire_malkuth_warrior", 12, null, 1),
                           new SiegeWave.WaveEntry("fdbosses:ice_malkuth_warrior", 12, null, 1),
                           new SiegeWave.WaveEntry("fdbosses:judgement_bird", 24, null, 1.5f),
                           new SiegeWave.WaveEntry("cataclysm:ignis", 2, null, 1.5f)
                    )
            ),
            new SiegeWave(
                    "2/4",
                    "§bOverconfidence",
                    List.of(
                           new SiegeWave.WaveEntry("iceandfire:siren", 4, null, 1),
                           new SiegeWave.WaveEntry("iceandfire:cyclops", 8, null, 1)
                    )
            ),
            new SiegeWave(
                    "3/4",
                    "§4Undone",
                    List.of(
                            new SiegeWave.WaveEntry("cataclysm:scylla", 2, null, 1),
                            new SiegeWave.WaveEntry("cataclysm:cindaria", 8, null, 1)
                    )
            ),
            new SiegeWave(
                    "4/4",
                    "§6Judgement",
                    List.of(

                            new SiegeWave.WaveEntry("cataclysm:maledictus", 2, null, 1),
                            new SiegeWave.WaveEntry("cataclysm:aptrgangr", 8, null, 1)
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
