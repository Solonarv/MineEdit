package com.solonarv.mods.mineedit.util;

import net.minecraft.util.Vec3;

import org.apache.commons.lang3.tuple.ImmutablePair;


public class IntVec3 implements Cloneable{
    
    public IntVec3 NULL=new IntVec3(0, 0, 0);
    public IntVec3 UNIT_X=new IntVec3(1, 0, 0);
    public IntVec3 UNIT_Y=new IntVec3(0, 1, 0);
    public IntVec3 UNIT_Z=new IntVec3(0, 0, 1);
    
    public int x, y, z;
    public IntVec3(int x, int y, int z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    
    @Override
    public IntVec3 clone(){
        return new IntVec3(this.x, this.y, this.z);
    }
    
    public byte accessArray(byte[][][] array){
        return array[this.x][this.y][this.z];
    }
    
    public byte accessArraySigned(byte[][][] array, boolean flipX, boolean flipY, boolean flipZ){
        int x = flipX ? array.length - this.x - 1 : this.x;
        int y = flipY ? array[0].length - this.y - 1 : this.y;
        int z = flipZ ? array[0][0].length - this.z - 1 : this.z;
        return array[x][y][z];
        
    }
    
    public IntVec3 crossProduct(IntVec3 other){
        return new IntVec3(this.y*other.z - this.z*other.y, this.z*other.x - this.x*other.z, this.x*other.y - this.y*other.x);
    }
    
    public int dotProduct(IntVec3 other){
        return this.x*other.x + this.y*other.y + this.z*other.z;
    }
    
    public IntVec3 scale(int scale){
        this.x*=scale;
        this.y*=scale;
        this.z*=scale;
        return this;
    }
    public IntVec3 getScaled(int scale){
        return this.clone().scale(scale);
    }

    public IntVec3 absoluteDims() {
        return new IntVec3(Math.abs(x), Math.abs(y), Math.abs(z));
    }
    
    public IntVec3 sum(IntVec3 other){
        return new IntVec3(this.x + other.x, this.y + other.y, this.z + other.z);
    }
    
    public IntVec3 difference(IntVec3 other){
        return new IntVec3(this.x - other.x, this.y - other.y, this.z - other.z);
    }
    
    public static IntVec3Subspace getSubspace(IntVec3 start, IntVec3 end){
        return new IntVec3Subspace(start, end);
    }
    
    @Override
    public boolean equals(Object other){
        if(other instanceof Vec3){
            Vec3 vec3=(Vec3) other;
            return this.x==vec3.xCoord && this.y==vec3.yCoord && this.z==vec3.zCoord;
        }else if(other instanceof IntVec3){
            IntVec3 intVec3=(IntVec3) other;
            return this.x==intVec3.x && this.y==intVec3.y && this.z==intVec3.z;
        }else{
            return false;
        }
    }
    
    /**
     * Create a pair of vectors that describes the same cuboid as the given one such that
     * vec2 - vec1 has only positive coords.
     * @param vec1 Some vector
     * @param vec2 Some vector
     * @return A pair of vectors as described above.
     */
    public static ImmutablePair<IntVec3, IntVec3> orderIntVec3Pair(IntVec3 vec1, IntVec3 vec2){
        return new ImmutablePair<IntVec3, IntVec3>(new IntVec3(Math.min(vec1.x, vec2.x), Math.min(vec1.y, vec2.y), Math.min(vec1.z, vec2.z)),
                new IntVec3(Math.max(vec1.x, vec2.x), Math.max(vec1.y, vec2.y), Math.max(vec1.z, vec2.z)));
    }
}
