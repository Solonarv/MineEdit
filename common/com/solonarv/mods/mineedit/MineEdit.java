package com.solonarv.mods.mineedit;

import com.solonarv.mods.mineedit.lib.Reference;
import com.solonarv.mods.mineedit.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

/**
 * Main mod class
 * @author Solonarv
 *
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class MineEdit {
    @Instance(Reference.MOD_ID)
    public MineEdit instance;
    
    @SidedProxy(clientSide=Reference.CLIENT_PROXY, serverSide=Reference.SERVER_PROXY)
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        proxy.registerCommands();
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event){
        
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        
    }
    
}
