package com.solonarv.mods.mineedit.command;

import java.util.Map;

import net.minecraft.command.ICommandSender;

import com.solonarv.mods.mineedit.math.module.IntVec3;
import com.solonarv.mods.mineedit.selection.Selection;
import com.solonarv.mods.mineedit.selection.SelectionCuboid;

public class CommandSelectCorner extends CommandMineEdit {
    
    public final CommandSelectCorner corner1 = new CommandSelectCorner(false),
            corner2 = new CommandSelectCorner(true);
    
    public final boolean isSecondCorner;
    
    public CommandSelectCorner(boolean secondCorner){
        this.isSecondCorner = secondCorner;
    }
    
    public CommandSelectCorner(Boolean secondCorner){
        this(secondCorner.booleanValue());
    }
    
    @Override
    public String getCommandName() {
        return "selc" + (this.isSecondCorner ? "2" : "1");
    }
    
    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {
        IntVec3 thisCorner = IntVec3.get(parseInt(commandSender, args[0]), parseInt(commandSender, args[1]), parseInt(commandSender, args[2]));
        Map<String, IntVec3> selmap = this.getSelCornerMap(true);
        if(selmap.containsKey(commandSender)){
            IntVec3 otherCorner=selmap.get(commandSender);
            Selection.selectionMap.put(commandSender.getCommandSenderName(), new SelectionCuboid(commandSender.func_130014_f_(), thisCorner, otherCorner));
            selmap.remove(commandSender.getCommandSenderName());
            this.getSelCornerMap(false).remove(commandSender.getCommandSenderName());
        } else {
            this.getSelCornerMap(false).put(commandSender.getCommandSenderName(), thisCorner);
        }
    }
    
    public Map<String, IntVec3> getSelCornerMap(boolean other){
        return this.isSecondCorner ^ other ? Selection.selectionMaps.getRight() : Selection.selectionMaps.getLeft();
    }
    
}
