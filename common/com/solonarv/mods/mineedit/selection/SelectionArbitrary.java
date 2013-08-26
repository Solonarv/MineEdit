package com.solonarv.mods.mineedit.selection;

import java.util.Iterator;

import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.Pair;

import com.solonarv.mods.mineedit.util.IntVec3;
import com.solonarv.mods.mineedit.util.Z3CuboidSubset;

public class SelectionArbitrary extends Selection {
    
    public final Z3CuboidSubset includedPositions;
    
    public SelectionArbitrary(World world, IntVec3 corner1, IntVec3 corner2){
        super(world);
        Pair<IntVec3, IntVec3> orderedCorners = IntVec3.orderIntVec3Pair(corner1, corner2);
        IntVec3 delta=orderedCorners.getRight().difference(orderedCorners.getLeft());
        this.includedPositions=new Z3CuboidSubset(delta.x, delta.y, delta.z, orderedCorners.getLeft().x, orderedCorners.getLeft().y, orderedCorners.getLeft().z);
    }
    
    @Override
    public Iterator<IntVec3> iterator() {
        return includedPositions.iterator();
    }
    
    @Override
    public boolean isPositionContained(IntVec3 position) {
        return includedPositions.contains(position);
    }
    
}
