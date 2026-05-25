package dev.csilman.modpackutils.util.altar.siege;

import java.util.List;

public record SiegeWave(
        String title,
        String subtitle,
        List<WaveEntry> entries
) {

    public record WaveEntry(
            String entityRegistryName,
            int count,
            String customName,
            float spreadMultiplier
    ) {}

}
