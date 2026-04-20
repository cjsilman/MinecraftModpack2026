package dev.csilman.modpackutils.event;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.command.ResetAltarStageCommand;
import dev.csilman.modpackutils.util.altar.AltarEventManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = ModpackUtilsMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        ServerLevel overworld = event.getServer().getLevel(Level.OVERWORLD);

        if (overworld != null) {
            AltarEventManager.tick(overworld);
        }
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        new ResetAltarStageCommand(event.getDispatcher());
    }

}
