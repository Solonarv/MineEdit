package com.solonarv.mods.mineedit.util;

public class Matrix3x3Int implements Cloneable{
    public int[][] values=new int[3][3];
    
    public Matrix3x3Int(int[][] values){
        for(int x=0; x<3; x++)
            for(int y=0; y<3; y++){
                this.values[x][y]=values[x][y];
            }
    }
    
    public IntVec3 applyToIntVec3(IntVec3 vec){
        return new IntVec3(vec.x*this.values[0][0] + vec.y*this.values[0][1] + vec.z*this.values[0][2],
                vec.x*this.values[1][0] + vec.y*this.values[1][1] + vec.z*this.values[1][2],
                vec.x*this.values[2][0] + vec.y*this.values[2][1] + vec.z*this.values[2][2]);
    }
    
    @Override
    public Matrix3x3Int clone(){
        return new Matrix3x3Int(this.values);
    }
}
