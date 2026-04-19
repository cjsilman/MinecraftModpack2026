package dev.csilman.modpackutils.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.data.AltarSavedData;
import dev.csilman.modpackutils.util.AltarEventPhase;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;

public class ResetAltarStageCommand {

    public ResetAltarStageCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("modpackutils").then(Commands.literal("resetAltar").executes(this::execute)));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        ServerLevel overworld = context.getSource().getServer().getLevel(ServerLevel.OVERWORLD);
        AltarSavedData data = AltarSavedData.get(overworld);

        data.setPhase(AltarEventPhase.DORMANT);
        data.setSiegeWave(0);
        data.setTicksInPhase(0);

        ModpackUtilsMod.LOGGER.info("[ModpackUtils] Reset altar stage to DORMANT.");

        return 1;
    }

}
