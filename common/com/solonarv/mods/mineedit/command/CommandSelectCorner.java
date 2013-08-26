package com.solonarv.mods.mineedit.command;

import java.util.Map;

import net.minecraft.command.ICommandSender;

import com.solonarv.mods.mineedit.selection.Selection;
import com.solonarv.mods.mineedit.selection.SelectionCuboid;
import com.solonarv.mods.mineedit.util.IntVec3;

public class CommandSelectCorner extends CommandMineEdit {
    
    public final CommandSelectCorner corner1 = new CommandSelectCorner(false),
            corner2 = new CommandSelectCorner(true);
    
    public final boolean isSecondCorner;
    
    private CommandSelectCorner(boolean secondCorner){
        this.isSecondCorner = secondCorner;
    }
    
    @Override
    public String getName() {
        return "selc" + (this.isSecondCorner ? "2" : "1");
    }
    
    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {
        IntVec3 thisCorner = IntVec3.get(parseInt(commandSender, args[0]), parseInt(commandSender, args[1]), parseInt(commandSender, args[2]));
        Map<ICommandSender, IntVec3> selmap = this.getSelCornerMap(true);
        if(selmap.containsKey(commandSender)){
            IntVec3 otherCorner=selmap.get(commandSender);
            Selection.selectionMap.put(commandSender, new SelectionCuboid(getWorldForCommandSender(commandSender), thisCorner, otherCorner));
        } else {
            this.getSelCornerMap(false).put(commandSender, thisCorner);
        }
    }
    
    public Map<ICommandSender, IntVec3> getSelCornerMap(boolean other){
        return this.isSecondCorner ^ other ? Selection.selectionMaps.getRight() : Selection.selectionMaps.getLeft();
    }
    
}
