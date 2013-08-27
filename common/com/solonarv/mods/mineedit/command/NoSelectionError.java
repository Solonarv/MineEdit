package com.solonarv.mods.mineedit.command;

import net.minecraft.command.CommandException;

public class NoSelectionError extends CommandException {

    public NoSelectionError(){
        this("commands.mineedit.exception.noselection", new Object[0]);
    }
    
    public NoSelectionError(String par1Str, Object[] par2ArrayOfObj) {
        super(par1Str, par2ArrayOfObj);
    }
    
}
