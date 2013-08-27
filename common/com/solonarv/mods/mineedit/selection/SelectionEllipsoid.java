package com.solonarv.mods.mineedit.selection;

import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.solonarv.mods.mineedit.math.module.IntVec3;
import com.solonarv.mods.mineedit.math.module.IntVec3Volume;

public class SelectionEllipsoid extends SelectionArbitrary {

    public final IntVec3 center, dimensions;
    
    public SelectionEllipsoid(World world, IntVec3 center, IntVec3 dimensions){
        super(world, center.difference(dimensions), center.sum(dimensions));
        this.center=center.clone();
        this.dimensions=dimensions.clone();
        this.initializeIncludedPositions();
    }
    
    private void initializeIncludedPositions() {
        IntVec3Volume vol = IntVec3.getVolume(center.difference(dimensions), center.sum(dimensions));
        for(IntVec3 vec : vol){
            Vec3 delta=this.center.difference(vec).getVec3();
            delta.xCoord/=this.dimensions.x;
            delta.yCoord/=this.dimensions.y;
            delta.zCoord/=this.dimensions.z;
            if(delta.lengthVector() <= 1){
                this.includedPositions.add(vec);
            }
        }
    }

    @Override
    public boolean isPositionContained(IntVec3 position) {
        Vec3 delta = position.difference(center).getVec3();
        delta.zCoord/=this.dimensions.z;
        delta.yCoord/=this.dimensions.y;
        delta.zCoord/=this.dimensions.z;
        return delta.lengthVector() <= 1;
    }
    
}
