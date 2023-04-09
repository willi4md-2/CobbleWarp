package net.lordst3ak.cobblewarp.command;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
public class ExampleCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("savecoords")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            BlockPos pos = player.blockPosition();
                            CompoundTag playerData = player.getPersistentData();
                            CompoundTag modData = playerData.getCompound(ModConstants.MOD_ID);
                            modData.putInt("x", pos.getX());
                            modData.putInt("y", pos.getY());
                            modData.putInt("z", pos.getZ());
                            playerData.put(ModConstants.MOD_ID, modData);
                            player.displayClientMessage(Component.literal("message"), true);
                            player.teleportTo(pos.getX() + 1, pos.getY(), pos.getZ() + 1);
                            return 1;
                        })
        );
        dispatcher.register(
                Commands.literal("retrievecoords")
                        .executes(context -> retrieveCoords(context))
        );
    }

    public static int retrieveCoords(CommandContext<CommandSourceStack> context) {
        ServerPlayer player;
        try {
            player = context.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            return 0;
        }
        CompoundTag playerData = player.getPersistentData();
        CompoundTag modData = playerData.getCompound(ModConstants.MOD_ID);
        int x = modData.getInt("x");
        int y = modData.getInt("y");
        int z = modData.getInt("z");
        player.displayClientMessage(Component.literal("Stored coordinates: x=" + x + ", y=" + y + ", z=" + z), true);
        return 1;
    }
}