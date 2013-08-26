package com.solonarv.mods.mineedit.selection;

import java.util.Iterator;

import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.Pair;

import com.solonarv.mods.mineedit.util.IntVec3;
import com.solonarv.mods.mineedit.util.IntVec3Volume;

public class SelectionCuboid extends Selection {
    
    public final IntVec3Volume cuboid;
    
    public SelectionCuboid(World world, IntVec3 start, IntVec3 end){
        super(world);
        Pair<IntVec3, IntVec3> vectors=IntVec3.orderIntVec3Pair(start, end);
        this.cuboid=new IntVec3Volume(vectors.getLeft(), vectors.getRight());
    }
    
    @Override
    public Iterator<IntVec3> iterator() {
        return this.cuboid.iterator();
    }
    
    @Override
    public boolean isPositionContained(IntVec3 position) {
        return position.x >= this.cuboid.start.x && position.x <= this.cuboid.end.x
                && position.y >= this.cuboid.start.y && position.y <= this.cuboid.end.y
                && position.z >= this.cuboid.start.z && position.z <= this.cuboid.end.z;
    }
    
}
