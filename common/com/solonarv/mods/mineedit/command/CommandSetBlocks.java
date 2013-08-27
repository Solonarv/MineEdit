package com.solonarv.mods.mineedit.command;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;

import com.solonarv.mods.mineedit.math.module.IntVec3;
import com.solonarv.mods.mineedit.selection.Selection;
import com.solonarv.mods.mineedit.util.BlockNames;
import com.solonarv.mods.mineedit.util.Settings;

public class CommandSetBlocks extends CommandMineEdit {
    
    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {
        if(!Selection.selectionMap.containsKey(commandSender.getCommandSenderName())){
            throw new NoSelectionError();
        }
        if(args.length >= 1){
            int blockID=BlockNames.getBlockForIDString(args[0]);
            int meta=0;
            try{
                int temp=parseIntBounded(commandSender, args[1], 0, 16);
                meta=temp;
            } catch(NumberInvalidException e){}
            int flag=2;
            if(args.length >= 3){
                if(args[2].equals("true")){
                    flag = flag | 1;
                } else if(args[2].equals("false")){
                    flag = flag & ~1;
                }
            } else flag = Settings.getSettingsFor(commandSender.getCommandSenderName()).getBooleanSetting("causeBlockUpdates", false) ? 3 : 2;
            Selection sel = Selection.selectionMap.get(commandSender.getCommandSenderName());
            for(IntVec3 vec : sel){
                sel.worldObj.setBlock(vec.x, vec.y, vec.z, blockID, meta, flag);
            }
        } else throw new WrongUsageException(this.getCommandUsage(commandSender));
    }
    
    @Override
    public String getCommandName() {
        return "set";
    }
    
    @Override
    public List addTabCompletionOptions(ICommandSender commandSender, String[] args){
        return args.length == 1 ? BlockNames.getPartialMatches(args[0]) : null;
    }
    
}
