package com.solonarv.mods.mineedit.proxy;

import com.solonarv.mods.mineedit.command.CommandMineEdit;

public class ServerProxy extends CommonProxy {
    @Override
    public void registerCommands(){
        CommandMineEdit.registerCommands();
    }
}
