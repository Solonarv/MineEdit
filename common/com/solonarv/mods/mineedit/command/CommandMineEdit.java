package com.solonarv.mods.mineedit.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;

public abstract class CommandMineEdit extends CommandBase {
    
    public static final CommandSelectCorner corner1 = new CommandSelectCorner(false);
    public static final CommandSelectCorner corner2 = new CommandSelectCorner(true);
    public static final CommandSetBlocks set = new CommandSetBlocks();
    
    @Override
    public final String getCommandUsage(ICommandSender icommandsender) {
        return this.getL10nPrefix() + ".usage";
    }
    
    public final String getL10nPrefix(){
        return "command.mineedit."+this.getCommandName();
    }
    
    public static void registerCommands() {
        ServerCommandManager manager=(ServerCommandManager) (MinecraftServer.getServer().getCommandManager());
        manager.registerCommand(corner1);
        manager.registerCommand(corner2);
        manager.registerCommand(set);
    }
}
