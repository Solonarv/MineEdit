package com.solonarv.mods.mineedit.selection;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.command.ICommandSender;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.solonarv.mods.mineedit.util.IntVec3;

public abstract class Selection implements Iterable<IntVec3> {
    
    public static final Map<ICommandSender, Selection> selectionMap=new HashMap<ICommandSender, Selection>();
    public static final Pair<Map<ICommandSender, IntVec3>, Map<ICommandSender, IntVec3>>
      selectionMaps = new ImmutablePair<Map<ICommandSender, IntVec3>, Map<ICommandSender, IntVec3>>
        (new HashMap<ICommandSender, IntVec3>(), new HashMap<ICommandSender, IntVec3>());
    
    public final World worldObj;
    
    public Selection(World world){
        this.worldObj=world;
    }
    
    public abstract boolean isPositionContained(IntVec3 position);
}
