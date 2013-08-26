package com.solonarv.mods.mineedit.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An axis-aligned cuboid in the Z^3 module over the ring of integers.
 * @author Solonarv
 *
 */
public class IntVec3Volume implements Iterable<IntVec3> {
    
    public final IntVec3 start, end;
    
    /**
     * Regular Old Constructor (tm): Just sets field values to given arguments 
     * @param start
     * @param end
     */
    public IntVec3Volume(IntVec3 start, IntVec3 end){
        this.start=start;
        this.end=end;
    }
    
    /**
     * An iterator over a volume of this class
     * @author Solonarv
     *
     */
    public static class IntVec3SubspaceIterator implements Iterator<IntVec3> {
        
        public final IntVec3 start, end;
        // The direction in which the x, y and z coords of the current vector should vary
        private final int xDir, yDir, zDir;
        /** The vector we're currently working with */
        private IntVec3 current;
        
        /** Construct an iterator from a given subspace 
         * 
         * @param subspace
         */
        public IntVec3SubspaceIterator(IntVec3Volume subspace) {
            this.start = subspace.start;
            this.end = subspace.end;
            this.current=this.start.clone();
            int dx = this.end.x - this.start.x,
                    dy = this.end.y - this.start.y,
                    dz = this.end.z - this.start.z;
            this.xDir = dx==0 ? 0 : dx>0 ? 1 : -1;
            this.yDir = dy==0 ? 0 : dy>0 ? 1 : -1;
            this.zDir = dz==0 ? 0 : dz>0 ? 1 : -1;
            
        }

        @Override
        public boolean hasNext() {
            return !this.current.equals(this.end);
        }
        
        /**
         * Increment/reset coordinates of current vector and
         * return it as the next element in the sequence
         */
        @Override
        public IntVec3 next() {
            if(this.hasNext()){
                this.current = this.current.clone();
            } else throw new NoSuchElementException();
            if(this.current.x != this.end.x){
                this.current=this.current.incrX(this.xDir);
            } else if(this.current.y != this.end.y){
                this.current=IntVec3.get(0, this.current.y + this.yDir, this.current.z);
            } else if(this.current.z != this.end.z){
                this.current=IntVec3.get(0, 0, this.current.z + this.zDir);
            } else throw new NoSuchElementException();
            return this.current;
        }
        
        /**
         * We can't just remove a point from a cuboid!
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException(); 
        }
        
    }
    
    /**
     * Create an iterator over this object
     */
    @Override
    public IntVec3SubspaceIterator iterator() {
        return new IntVec3SubspaceIterator(this);
    }
    
}
