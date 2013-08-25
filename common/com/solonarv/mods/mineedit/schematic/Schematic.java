package com.solonarv.mods.mineedit.schematic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.solonarv.mods.mineedit.util.IntVec3;
import com.solonarv.mods.mineedit.util.MathUtil;
import com.solonarv.mods.mineedit.util.Matrix3x3Int;

/**
 * Schematic class. Allows importing and exporting of stuff in the .schematic format
 * and manipulation of said schematics.
 * @author Solonarv
 *
 */
public class Schematic{
    
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
    public List<NBTTagCompound> tileentities;
    /**
     * The entities included in the schematic.
     */
    public List<NBTTagCompound> entities;
    /**
     * The volume of this schematic
     */
    public final int size;
    
    /**
     * Construct a schematic object from its NBT represantation
     * @param schematic An {@link NBTTagCompound} to read the schematic data from
     */
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
    
    /**
     * Deep-copy constructor
     * @param source A Schematic object to copy from
     */
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
    
    /**
     * Export constructor: This one is used to create a schematic from a
     * region in a world
     * @param world The {@link World} to export from
     * @param corner1 An {@link IntVec3} specifying one corner if the selection
     * @param corner2 An {@link IntVec3} specifying the other corner if the selection
     */
    public Schematic(World world, IntVec3 corner1, IntVec3 corner2){
        this.width=Math.abs(corner1.x - corner2.x);
        this.height=Math.abs(corner1.y - corner2.y);
        this.length=Math.abs(corner1.z - corner2.z);
        this.size = this.width * this.length * this.height;
        this.materials="Alpha";
        this.blocks=new byte[this.width][this.height][this.length];
        this.extraBlockData=new byte[this.width][this.height][this.length];
        Pair<IntVec3, IntVec3> orderedCorners = IntVec3.orderIntVec3Pair(corner1, corner2);
        this.tileentities = new LinkedList<NBTTagCompound>();
        this.entities = new LinkedList<NBTTagCompound>();
        for(IntVec3 pos : IntVec3.getVolume(orderedCorners.getLeft(), orderedCorners.getRight())){
            IntVec3 relativePos = pos.difference(orderedCorners.getLeft());
            int id = world.getBlockId(pos.x, pos.y, pos.z),
                    meta = world.getBlockMetadata(pos.x, pos.y, pos.z);
            this.blocks[relativePos.x][relativePos.y][relativePos.z] = (byte) id;
            this.extraBlockData[relativePos.x][relativePos.y][relativePos.z] = (byte) ((id >> 4) | meta);
            TileEntity te=world.getBlockTileEntity(pos.x, pos.y, pos.z);
            if(te!=null){
                NBTTagCompound nbt = new NBTTagCompound();
                te.writeToNBT(nbt);
                nbt.setInteger("x", nbt.getInteger("x") - orderedCorners.getLeft().x);
                nbt.setInteger("y", nbt.getInteger("y") - orderedCorners.getLeft().y);
                nbt.setInteger("z", nbt.getInteger("z") - orderedCorners.getLeft().z);
                this.tileentities.add(nbt);
            }
        }
        for(Object entityObj : world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getAABBPool().getAABB(orderedCorners.getLeft().x, orderedCorners.getLeft().y, orderedCorners.getLeft().z, orderedCorners.getRight().x, orderedCorners.getRight().y, orderedCorners.getRight().z))){
            Entity e = (Entity) entityObj;
            NBTTagCompound nbt = new NBTTagCompound();
            e.writeToNBT(nbt);
            NBTTagList pos=nbt.getTagList("Pos");
            NBTTagDouble tagX = (NBTTagDouble) pos.tagAt(0),
                    tagY = (NBTTagDouble) pos.tagAt(1),
                    tagZ = (NBTTagDouble) pos.tagAt(2);
            tagX.data -= orderedCorners.getLeft().x;
            tagY.data -= orderedCorners.getLeft().y;
            tagZ.data -= orderedCorners.getLeft().z;
            this.entities.add(nbt);
        }
    }
    
    /**
     * Write this schematic to NBT in the default .schematic format
     * @return An {@link NBTTagCompound} representing this schematic
     */
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
    
    /**
     * Import this schematic into a world.
     * This will place all included blocks, tile entities and entities in the correct positions.
     * <br><code>
     * (new Schematic(world, new IntVec3(x,y,z), new IntVec3(x+w,y+h,z+l).importIntoWorld(world,x,y,z,false))
     * </code><br>
     * should be a no-op, where w,h,l are positive.
     * @param world The {@link World} to import into
     * @param ox The x position to start at
     * @param oy The y position to start at
     * @param oz The z position to start at
     * @param causeBlockUpdates Whether or not to cause block updates.
     */
    public void importIntoWorld(World world, int ox, int oy, int oz, boolean causeBlockUpdates){
        // Import blocks
        int flag = causeBlockUpdates ? 3 : 2;
        for(int x=ox; x<this.width+ox; x++)
            for(int y=oy; y<this.height+oy; y++)
                for(int z=oz; z<this.length+oz; z++){
                    int id=this.blocks[x][y][z] + (this.extraBlockData[x][y][z] & 0xf0) << 4;
                    int meta=this.extraBlockData[x][y][z] & 0x0f;
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
    
    /**
     * Rotate this schematic around the specified axis by the specified amount of counterclockwise quarter turns
     * @param angle The angle to rotate by
     * @param axis The axis to rotate around
     * @param rotateEntityYaw Whether or not to rotate entities
     * @return
     */
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
    
    /**
     * Apply an automorphism in Z^3 to this schematic, specified by a 3x3 matrix with integer entries.
     * The matrix' determinant must be 1.
     * @param matrix
     * @return
     */
    public Schematic applyMatrixTForm(Matrix3x3Int matrix){
        if(Math.abs(matrix.det())!=1){
            throw new IllegalArgumentException("Transform matrix determinant must be +- 1!");
        }
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
    
    /**
     * Mirrors this schematic with respect to the plane orthogonal to the given axis
     * @param axis The axis to mirror over
     * @return
     */
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
