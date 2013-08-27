package com.solonarv.mods.mineedit.math.module;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3Pool;

import org.apache.commons.lang3.tuple.ImmutablePair;


/**
 * Implementation of Z^3 as module over Z (basically 3D vectors with integer coordinates)
 * @author Solonarv
 *
 */
public class IntVec3 implements Cloneable{

    // Keep at most 64 vectors on hand
    private static final int MAX_CACHE_SIZE = 64;
    
    public final int x, y, z;
    
    private static final LinkedList<IntVec3> vectorCache = new LinkedList<IntVec3>();
    private static final Random rng=new Random();
    
    /**
     * Create a vector from its coordinates
     * @param x
     * @param y
     * @param z
     */
    private IntVec3(int x, int y, int z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    
    @Override
    public IntVec3 clone(){
        return get(this.x, this.y, this.z);
    }
    
    /**
     * Use the vector coordinates as indices to access an element in a 3-dimensional array
     * @param array
     * @return
     */
    public byte accessArray(byte[][][] array){
        return array[this.x][this.y][this.z];
    }
    
    /**
     * Same as accessArray, but reverses element order of subarrays if told to do so
     * @param array
     * @param flipX
     * @param flipY
     * @param flipZ
     * @return
     */
    public byte accessArraySigned(byte[][][] array, boolean flipX, boolean flipY, boolean flipZ){
        int x = flipX ? array.length - this.x - 1 : this.x;
        int y = flipY ? array[0].length - this.y - 1 : this.y;
        int z = flipZ ? array[0][0].length - this.z - 1 : this.z;
        return array[x][y][z];
        
    }
    
    /**
     * Compute the cross product of two vectors
     * @param other the vector to multiply by
     * @return the cross product this x other
     */
    public IntVec3 crossProduct(IntVec3 other){
        return get(this.y*other.z - this.z*other.y, this.z*other.x - this.x*other.z, this.x*other.y - this.y*other.x);
    }
    
    /**
     * Compute the dot product of two vectors
     * @param other the vector to multiply by
     * @return the dot product this . other
     */
    public int dotProduct(IntVec3 other){
        return this.x*other.x + this.y*other.y + this.z*other.z;
    }
    
    /**
     * Compute the scalar product of this vector and some integer
     * @param scale the integer to multiply by
     * @return the scalar product scale . other
     */
    public IntVec3 scale(int scale){
        return get(this.x*scale, this.y*scale, this.z*scale);
    }
    
    /**
     * Creates a clone of this vector whose coordinates are the absolute values of this vector's
     * @return
     */
    public IntVec3 absoluteDims() {
        return get(Math.abs(x), Math.abs(y), Math.abs(z));
    }
    
    /**
     * Compute the sum of two vectors
     * @param other the vector to add to this one
     * @return this + other
     */
    public IntVec3 sum(IntVec3 other){
        return get(this.x + other.x, this.y + other.y, this.z + other.z);
    }
    
    /**
     * Compute the difference of two vectors
     * @param other the vector to subtract from this one
     * @return this - other
     */
    public IntVec3 difference(IntVec3 other){
        return get(this.x - other.x, this.y - other.y, this.z - other.z);
    }
    
    /**
     * Create an object representing a cuboid volume in the Z^3 space
     * @param start one corner of the cuboid to create
     * @param end another corner of the cuboid to create
     * @return an axis-aligned cuboid with corners at start and end
     */
    public static IntVec3Volume getVolume(IntVec3 start, IntVec3 end){
        return getVolume(start, end);
    }
    
    /**
     * Equality check. Will return equality of coordinates if given an IntVec3 or a Vec3, false otherwise
     */
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
        return new ImmutablePair<IntVec3, IntVec3>(get(Math.min(vec1.x, vec2.x), Math.min(vec1.y, vec2.y), Math.min(vec1.z, vec2.z)),
                get(Math.max(vec1.x, vec2.x), Math.max(vec1.y, vec2.y), Math.max(vec1.z, vec2.z)));
    }

    public Vec3 getVec3() {
        return this.getVec3FromPool(Vec3.fakePool);
    }
    
    public Vec3 getVec3FromPool(Vec3Pool pool){
        return pool.getVecFromPool(this.x, this.y, this.z);
    }
    
    public double norm(){
        return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }
    
    public IntVec3 incrX(int dx){
        return get(this.x + dx, this.y, this.z);
    }
    
    public IntVec3 incrY(int dy){
        return get(this.x, this.y + dy, this.z);
    }
    
    public IntVec3 incrZ(int dz){
        return get(this.x, this.y, this.z + dz);
    }

    public static IntVec3 get(int x, int y, int z) {
        IntVec3 vec=null;
        Iterator<IntVec3> iterator=vectorCache.iterator();
        while(iterator.hasNext()){
            IntVec3 v=iterator.next();
            if(v.x==x && v.y==y && v.z==z){
                vec=v;
                iterator.remove();
                break;
            }
        }
        if(vec==null){
            vec = new IntVec3(x, y, z);
            while(vectorCache.size() >= MAX_CACHE_SIZE){
                vectorCache.removeLast();
            }
        }
        vectorCache.addFirst(vec);
        return vec;
    }
}
