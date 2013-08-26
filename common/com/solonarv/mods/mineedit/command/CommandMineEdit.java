package com.solonarv.mods.mineedit.command;

import java.lang.reflect.Field;
import java.util.LinkedList;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class CommandMineEdit extends CommandBase {
    
    public abstract String getName();
    
    @Override
    public final String getCommandName() {
        return "/"+this.getName();
    }
    
    @Override
    public final String getCommandUsage(ICommandSender icommandsender) {
        return "command.mineedit" + this.getName() + ".usage";
    }
    
    public static World getWorldForCommandSender(ICommandSender commandSender){
        try{
            if(commandSender instanceof Entity){
                return ((Entity) commandSender).worldObj;
            } else if(commandSender instanceof TileEntity){
                return ((TileEntity) commandSender).getWorldObj();
            } else {
                Class class1 = commandSender.getClass();
                LinkedList<Field> typedFields = new LinkedList<Field>();
                for(Field f : class1.getDeclaredFields()){
                    if(World.class.isAssignableFrom(f.getType())){
                        typedFields.add(f);
                    }
                }
                for(Field f : typedFields){
                    String name = f.getName();
                    if(name.equals("worldObj") || name.equals("world")){
                        return (World) f.get(commandSender);
                    }
                }
                return (World) typedFields.getFirst().get(commandSender);
            }
        } catch(IllegalArgumentException e){
            e.printStackTrace();
            return null;
        } catch(IllegalAccessException e){
            e.printStackTrace();
            return null;
        }
    }
    
}
