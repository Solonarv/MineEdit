package com.solonarv.mods.mineedit.util;

import java.util.Iterator;

public class IntVec3Subspace implements Iterable<IntVec3> {

    public final IntVec3 start, end;
    
    public IntVec3Subspace(IntVec3 start, IntVec3 end){
        this.start=start;
        this.end=end;
    }
    
    public class IntVec3SubspaceIterator implements Iterator<IntVec3> {
        
        public final IntVec3 start, end;
        private IntVec3 current;
        
        public IntVec3SubspaceIterator(IntVec3Subspace subspace) {
            this.start = subspace.start;
            this.end = subspace.end;
            this.current=this.start.clone();
            
        }

        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            return false;
        }
        
        @Override
        public IntVec3 next() {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public void remove() {
            // TODO Auto-generated method stub
            
        }
        
    }

    @Override
    public Iterator<IntVec3> iterator() {
        return new IntVec3SubspaceIterator(this);
    }
    
}
