package com.solonarv.mods.mineedit.selection;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.solonarv.mods.mineedit.math.module.IntVec3;

public abstract class Selection implements Iterable<IntVec3> {
    
    public static final Map<String, Selection> selectionMap=new HashMap<String, Selection>();
    public static final Pair<Map<String, IntVec3>, Map<String, IntVec3>>
      selectionMaps = new ImmutablePair<Map<String, IntVec3>, Map<String, IntVec3>>
        (new HashMap<String, IntVec3>(), new HashMap<String, IntVec3>());
    
    public final World worldObj;
    
    public Selection(World world){
        this.worldObj=world;
    }
    
    public abstract boolean isPositionContained(IntVec3 position);
}
