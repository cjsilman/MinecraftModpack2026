package dev.csilman.modpackutils.util.altar;

import net.minecraft.server.level.ServerLevel;

public class AltarWeatherManager {

    public static void startSiegeWeather(ServerLevel overworld) {
        overworld.setWeatherParameters(
                0,
                24000,
                true,
                true
        );
    }

    public static void resetWeather(ServerLevel overworld) {
        overworld.setWeatherParameters(
                6000,       // clearTime — start with a short clear period
                0,          // rainTime
                false,      // isRaining
                false       // isThundering
        );
    }

}
