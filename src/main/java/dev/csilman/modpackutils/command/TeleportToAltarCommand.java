package dev.csilman.modpackutils.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.data.AltarSavedData;
import dev.csilman.modpackutils.util.altar.AltarEventPhase;
import dev.csilman.modpackutils.util.altar.AltarWeatherManager;
import dev.csilman.modpackutils.util.altar.siege.SiegePhase;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

public class TeleportToAltarCommand {

    public TeleportToAltarCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("modpackutils").then(Commands.literal("teleportToAltar").executes(this::execute)));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        ServerLevel overworld = context.getSource().getServer().getLevel(ServerLevel.OVERWORLD);

        AltarSavedData data = AltarSavedData.get(overworld);
        Player player = context.getSource().getPlayer();

        player.teleportTo(overworld,
                data.getAltarMidpoint().getX(), data.getAltarMidpoint().getY()+1, data.getAltarMidpoint().getZ(),
                Set.of(),
                player.getYRot(),
                player.getXRot()
        );


        player.displayClientMessage(
                Component.literal("Teleported to Altar Midpoint."),
                true
        );

        ModpackUtilsMod.LOGGER.info("[ModpackUtils] Teleported {} to AltarMidpoint.", player.getName());

        return 1;
    }

}
