package com.solonarv.mods.mineedit.schematic;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.solonarv.mods.mineedit.util.IntVec3;
import com.solonarv.mods.mineedit.util.MathUtil;
import com.solonarv.mods.mineedit.util.Matrix3x3Int;

public class Schematic implements Cloneable{
    
    /**
     * Size along x axis
     */
    public int width;
    /**
     * Size along y axis
     */
    public int height;
    /**
     * Size along z axis
     */
    public int length;
    /**
     * The schematic's source format
     */
    public String materials;
    
    /**
     * The blockIDs defining the schematic.
     * Coordinates are ordered [x][y][z]
     */
    public byte[][][] blocks;
    /**
     * The extra data defining the schematic's blocks.
     * The lower 4 bits are the damage value.
     * The higher 4 bits are used for the extra data corresponding to the Forge 4096 BlockID fix.
     * Coordinates are ordered [x][y][z]
     */
    public byte[][][] extraBlockData;
    /**
     * The TileEntities included in the schematic.
     */
    public ArrayList<NBTTagCompound> tileentities;
    /**
     * The entities included in the schematic.
     */
    public ArrayList<NBTTagCompound> entities;
    public final int size;
    
    public Schematic(NBTTagCompound schematic){
        this.width = schematic.getInteger("Width");
        this.height = schematic.getInteger("Height");
        this.length = schematic.getInteger("Length");
        this.size = this.width * this.length * this.height;
        this.materials = schematic.getString("Materials");
        byte[] blocks=schematic.getByteArray("Blocks");
        byte[] data=schematic.getByteArray("Data");
        this.blocks=new byte[this.width][this.height][this.length];
        this.extraBlockData=new byte[this.width][this.height][this.length];
        int index=0;
        for(int y=0; y<this.height; y++, index++)
            for(int z=0; z<this.length; z++, index++)
                for(int x=0; x<this.width; x++, index++){
                    this.blocks[x][y][z]=blocks[index];
                    this.extraBlockData[x][y][z]=data[index];
                }
        NBTTagList tiles = schematic.getTagList("TileEntities");
        int tilesLength=tiles.tagCount();
        this.tileentities=new ArrayList<NBTTagCompound>(tilesLength);
        for(int i=0; i<tilesLength; i++){
            this.tileentities.add((NBTTagCompound) tiles.tagAt(i));
        }
        NBTTagList entities = schematic.getTagList("Entities");
        int entitiesLength=tiles.tagCount();
        this.entities=new ArrayList<NBTTagCompound>(entitiesLength);
        for(int i=0; i<tilesLength; i++){
            this.entities.add((NBTTagCompound) entities.tagAt(i));
        }
    }
    
    public Schematic(Schematic source) {
        this.width=source.width;
        this.length=source.length;
        this.height=source.height;
        this.size=source.size;
        this.materials=source.materials;
        this.blocks=source.blocks.clone();
        this.extraBlockData=source.extraBlockData.clone();
        this.tileentities=new ArrayList<NBTTagCompound>(source.tileentities.size());
        for (NBTTagCompound tile : source.tileentities) {
            this.tileentities.add((NBTTagCompound) tile.copy());
        }
        this.entities=new ArrayList<NBTTagCompound>(source.entities.size());
        for (NBTTagCompound entity : source.entities) {
            this.entities.add((NBTTagCompound) entity.copy());
        }
    }
    
    public Schematic(World world, IntVec3 corner1, IntVec3 corner2){
        this.width=Math.abs(corner1.x - corner2.x);
        this.height=Math.abs(corner1.y - corner2.y);
        this.length=Math.abs(corner1.z - corner2.z);
        this.size = this.width * this.length * this.height;
        this.materials="Alpha";
        this.blocks=new byte[this.width][this.height][this.length];
        this.extraBlockData=new byte[this.width][this.height][this.length];
    }

    public NBTTagCompound toNBT(){
        NBTTagCompound nbt=new NBTTagCompound();
        nbt.setInteger("Width", this.width);
        nbt.setInteger("Height", this.height);
        nbt.setInteger("Length", this.length);
        nbt.setString("Materials", this.materials);
        byte[] blocks=new byte[this.size], data=new byte[this.size];
        int index=0;
        for(int y=0; y<this.height; y++, index++)
            for(int z=0; z<this.length; z++, index++)
                for(int x=0; x<this.width; x++, index++){
                    blocks[index]=this.blocks[x][y][z];
                    data[index]=this.extraBlockData[x][y][z];
                }
        nbt.setByteArray("Blocks", blocks);
        nbt.setByteArray("Data", data);
        NBTTagList tiles=new NBTTagList(), entities=new NBTTagList();
        for(NBTTagCompound tile: this.tileentities){
            tiles.appendTag(tile);
        }
        for(NBTTagCompound entity: this.entities){
            entities.appendTag(entity);
        }
        nbt.setTag("Entities", entities);
        nbt.setTag("TileEntities", tiles);
        return nbt;
    }
    
    public void importIntoWorld(World world, int ox, int oy, int oz, boolean causeBlockUpdates){
        // Import blocks
        int flag = causeBlockUpdates ? 3 : 2;
        for(int x=ox; x<this.width+ox; x++)
            for(int y=oy; y<this.height+oy; y++)
                for(int z=oz; z<this.length+oz; z++){
                    int id=this.blocks[x][y][z] + (this.extraBlockData[x][y][z] >> 4);
                    int meta=this.extraBlockData[x][y][z] % 16;
                    world.setBlock(x, y, z, id, meta, flag);
                }
        // Import TEs
        for (NBTTagCompound currentTETag : this.tileentities) {
            TileEntity currentTE=TileEntity.createAndLoadEntity(currentTETag);
            currentTE.xCoord+=ox;
            currentTE.yCoord+=oy;
            currentTE.zCoord+=oz;
            world.setBlockTileEntity(currentTE.xCoord, currentTE.yCoord, currentTE.zCoord, currentTE);
        }
        // Import entities
        for(NBTTagCompound currentEntityTag : this.entities){
            Entity entity=(EntityList.createEntityFromNBT(currentEntityTag, world));
            entity.posX+=ox;
            entity.posY+=oy;
            entity.posZ+=oz;
            world.spawnEntityInWorld(entity);
        }
    }
    
    @Override
    public Schematic clone(){
        return new Schematic(this);
    }
    
    public Schematic rotate(int angle, ForgeDirection axis, boolean rotateEntityYaw){
        if((angle & 3)!=0){ // Skip all these steps if there is no actual rotation to do
            if((axis == ForgeDirection.WEST || axis == ForgeDirection.DOWN || axis == ForgeDirection.NORTH) && (angle & 1) !=0){
                angle = 4 - angle;
            }
            Matrix3x3Int matrix = null;
            switch(axis){
                case WEST:
                case EAST: // Rotation about x axis
                    matrix = new Matrix3x3Int(new int[][]{{1, 0, 0},
                            {0, MathUtil.cosineForIntAngle(angle), -MathUtil.sineForIntAngle(angle)},
                            {0, MathUtil.sineForIntAngle(angle), MathUtil.cosineForIntAngle(angle)}});
                    break;
                case DOWN:
                case UP: // Rotation about y axis
                    matrix = new Matrix3x3Int(new int[][]{{MathUtil.cosineForIntAngle(angle), 0, MathUtil.sineForIntAngle(angle)},
                            {0, 1, 0},
                            {-MathUtil.sineForIntAngle(angle), 0, MathUtil.cosineForIntAngle(angle)}});
                    break;
                case NORTH:
                case SOUTH: // Rotation about z axis
                    matrix = new Matrix3x3Int(new int[][]{{MathUtil.cosineForIntAngle(angle), -MathUtil.sineForIntAngle(angle), 0},
                            {MathUtil.sineForIntAngle(angle), MathUtil.cosineForIntAngle(angle), 0},
                            {0, 0, 1}});
                    break;
                default:
                    return this;
            }
            if(rotateEntityYaw) for(NBTTagCompound e: this.entities){
                NBTTagFloat yaw=(NBTTagFloat) e.getTagList("Rotation").tagAt(0);
                yaw.data=(yaw.data + 90F*(angle & 3)) % 360F;
            }
            return this.applyMatrixTForm(matrix);
        }
        return this;
    }
    
    public Schematic applyMatrixTForm(Matrix3x3Int matrix){
        // Rotate blocks, this is the hardest part
        byte[][][] oldBlocks=this.blocks,
                oldData=this.extraBlockData;
        IntVec3 oldDimensions = new IntVec3(this.width, this.height, this.length);
        IntVec3 newDimensionsRaw = matrix.applyToIntVec3(oldDimensions);
        IntVec3 newDimensions = newDimensionsRaw.absoluteDims();
        boolean flipX=newDimensions.x<0,
                flipY=newDimensions.y<0,
                flipZ=newDimensions.z<0;
        this.blocks = new byte[newDimensions.x][newDimensions.y][newDimensions.z];
        this.extraBlockData = new byte[newDimensions.x][newDimensions.y][newDimensions.z];
        for(int x=0; x<newDimensions.x; x++)
            for(int y=0; y<newDimensions.y; y++)
                for(int z=0; z<newDimensions.z; z++){
                    IntVec3 vec=new IntVec3(x, y, z);
                    this.blocks[x][y][z] = vec.accessArraySigned(oldBlocks, flipX, flipY, flipZ);
                    this.extraBlockData[x][y][z] = vec.accessArraySigned(oldData, flipX, flipY, flipZ);
                }
        // Rotate tile entity positions
        for(NBTTagCompound e : this.tileentities){
            IntVec3 newPosition = matrix.applyToIntVec3(new IntVec3(e.getInteger("x"), e.getInteger("y"), e.getInteger("z")));
            e.setInteger("x", newPosition.x);
            e.setInteger("y", newPosition.y);
            e.setInteger("z", newPosition.z);
        }
        // Rotate entity positions
        for(NBTTagCompound e : this.entities){
            IntVec3 newPosition = matrix.applyToIntVec3(new IntVec3(e.getInteger("x"), e.getInteger("y"), e.getInteger("z")));
            e.setInteger("x", newPosition.x);
            e.setInteger("y", newPosition.y);
            e.setInteger("z", newPosition.z);
        }
        return this;
    }
    
    public Schematic mirror(ForgeDirection axis){
        Matrix3x3Int matrix = null;
        switch(axis){
            case WEST:
            case EAST:
                matrix = new Matrix3x3Int(new int[][]{{-1,0,0},{0,1,0},{0,0,1}});
                break;
            case DOWN:
            case UP:
                matrix = new Matrix3x3Int(new int[][]{{1,0,0},{0,-1,0},{0,0,1}});
                break;
            case NORTH:
            case SOUTH:
                matrix = new Matrix3x3Int(new int[][]{{1,0,0},{0,1,0},{0,0,-1}});
                break;
            default:
                return this;
        }
        this.applyMatrixTForm(matrix);
        return this;
    }
}
