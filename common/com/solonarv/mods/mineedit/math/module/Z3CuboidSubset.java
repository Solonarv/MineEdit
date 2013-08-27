package com.solonarv.mods.mineedit.math.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;


public class Z3CuboidSubset implements Set<IntVec3> {
    
    public class Z3CuboidSubsetIterator implements Iterator<IntVec3> {
        
        private int x=0,y=0,z=0;
        
        private IntVec3 getNext(){
            IntVec3Volume planarCut=IntVec3.getVolume(IntVec3.get(x, y, 0), IntVec3.get(Z3CuboidSubset.this.width, Z3CuboidSubset.this.height, Z3CuboidSubset.this.length));
            for(IntVec3 vec : planarCut){
                if(Z3CuboidSubset.this.contains(vec)){
                    return vec;
                }
            }
            throw new NoSuchElementException();
            
        }
        
        @Override
        public boolean hasNext() {
            try{
                this.getNext();
            }catch(NoSuchElementException e){
                return false;
            }
            return true;
        }
        
        @Override
        public IntVec3 next() {
            IntVec3 next=this.getNext();
            this.x=next.x;
            this.y=next.y;
            this.z=next.z;
            return next.sum(Z3CuboidSubset.this.offset());
        }
        
        @Override
        public void remove() {
            Z3CuboidSubset.this.removeElem(x,y,z);
        }
        
    }

    private int width, height, length, ox, oy, oz;
    private boolean[][][] elements;
    private int size=0;
    private boolean mutable;
    
    public Z3CuboidSubset(int width, int height, int length, int ox, int oy, int oz){
        this.width=width;
        this.height=height;
        this.length=length;
        this.ox=ox;
        this.oy=oy;
        this.oz=oz;
        this.elements=new boolean[width][height][length];
    }
    
    public IntVec3 offset() {
        return IntVec3.get(this.ox, this.oy, this.oz);
    }

    public Z3CuboidSubset(int width, int height, int length){
        this(width, height, length, 0, 0, 0);
    }
    
    public void removeElem(int x, int y, int z) {
        if(this.isMutable()){
            this.elements[x][y][z]=false;
        }else throw new UnsupportedOperationException();
    }

    private boolean isMutable() {
        return this.mutable;
    }

    @Override
    public Z3CuboidSubsetIterator iterator() {
        return new Z3CuboidSubsetIterator();
    }
    
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean add(IntVec3 e) {
        if(this.isMutable()){
            if(e == null){
                throw new NullPointerException();
            }else if(this.canContain(e)){
                if(!this.elements[e.x - this.ox][e.y - this.oy][e.z - this.oz]){
                    this.elements[e.x - this.ox][e.y - this.oy][e.z - this.oz]=true;
                    return true;
                }else return false;
            } else throw new IllegalArgumentException("Coordinates of vector to add must be within bounding box specified in constructor.");
        }else throw new UnsupportedOperationException("Attempt to modify an immutable set");
    }

    public boolean canContain(IntVec3 e) {
        return e!=null && e.x >= this.ox && e.x <= this.ox+this.width
                && e.y >= this.oy && e.y <= this.oy+this.width
                && e.z >= this.oz && e.z <= this.oz+this.width;
    }
    
    @Override
    public boolean contains(Object e){
        if(e instanceof IntVec3){
            IntVec3 vec=(IntVec3) e;
            return this.canContain(vec) && this.elements[vec.x - this.ox][vec.y - this.oy][vec.z - this.oz];
        } else return false;
    }

    @Override
    public boolean addAll(Collection<? extends IntVec3> c) {
        boolean changed=false;
        for(IntVec3 v : c){
            if(!this.contains(v)){
                changed = changed || this.add(v);
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        for(int x=0; x<this.width; x++)
            for(int y=0; y<this.width; y++)
                for(int z=0; z<this.width; z++){
                    this.elements[x][y][z]=false;
                }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object o : c)
            if(!this.contains(o))
                return false;
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean remove(Object o) {
        if(this.contains(o)){
            IntVec3 vec=(IntVec3) o;
            this.elements[vec.x - this.ox][vec.y - this.oy][vec.z - this.oz] = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean success=false;
        for(Object o : c){
            success = success || this.remove(o);
        }
        return success;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        List<IntVec3> toRemove=new LinkedList<IntVec3>();
        for(IntVec3 vec : this){
            if(!c.contains(vec)){
                toRemove.add(vec);
            }
        }
        for(IntVec3 vec : toRemove){
            this.remove(vec);
        }
        return !toRemove.isEmpty();
    }

    @Override
    public Object[] toArray() {
        ArrayList<IntVec3> temp = new ArrayList<IntVec3>(this.size());
        for(IntVec3 vec : this){
            temp.add(vec);
        }
        return temp.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        ArrayList<IntVec3> temp = new ArrayList<IntVec3>(this.size());
        for(IntVec3 vec : this){
            temp.add(vec);
        }
        return temp.toArray(a);
    }
    
}
