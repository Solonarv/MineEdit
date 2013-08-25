package com.solonarv.mods.mineedit.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IntVec3Subspace implements Iterable<IntVec3> {

    public final IntVec3 start, end;
    
    public IntVec3Subspace(IntVec3 start, IntVec3 end){
        this.start=start;
        this.end=end;
    }
    
    public class IntVec3SubspaceIterator implements Iterator<IntVec3> {
        
        public final IntVec3 start, end;
        private final int xDir, yDir, zDir;
        private IntVec3 current;
        
        public IntVec3SubspaceIterator(IntVec3Subspace subspace) {
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
        
        @Override
        public IntVec3 next() {
            if(this.hasNext()){
                this.current = this.current.clone();
            } else throw new NoSuchElementException();
            if(this.current.x != this.end.x){
                this.current.x+=this.xDir;
            } else if(this.current.y != this.end.y){
                this.current.y+=this.yDir;
            } else if(this.current.z != this.end.z){
                this.current.z+=this.zDir;
            } else throw new NoSuchElementException();
            return this.current;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException(); 
        }
        
    }

    @Override
    public Iterator<IntVec3> iterator() {
        return new IntVec3SubspaceIterator(this);
    }
    
}
