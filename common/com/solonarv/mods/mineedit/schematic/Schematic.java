package com.solonarv.mods.mineedit.schematic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
    public NBTTagCompound[] tileentities;
    /**
     * The entities included in the schematic.
     */
    public NBTTagCompound[] entities;
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
        this.tileentities=new NBTTagCompound[tilesLength];
        for(int i=0; i<tilesLength; i++){
            this.tileentities[i]=(NBTTagCompound) tiles.tagAt(i);
        }
        NBTTagList entities = schematic.getTagList("Entities");
        int entitiesLength=tiles.tagCount();
        this.entities=new NBTTagCompound[entitiesLength];
        for(int i=0; i<tilesLength; i++){
            this.entities[i]=(NBTTagCompound) entities.tagAt(i);
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
        this.tileentities=new NBTTagCompound[source.tileentities.length];
        for (int i = 0; i < source.tileentities.length; i++) {
            this.tileentities[i]=(NBTTagCompound) source.tileentities[i].copy();
        }
        this.entities=new NBTTagCompound[source.entities.length];
        for (int i = 0; i < source.entities.length; i++) {
            this.entities[i]=(NBTTagCompound) source.entities[i].copy();
        }
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
        Schematic s=new Schematic(this);
        return s;
    }
    
    public Schematic rotate(int angle){
        if((angle & 3)==0){ // Skip all these steps if there is no actual rotation to do
            if((angle & 2)!=0){
                byte[][][] oldBlocks=this.blocks,
                        oldData=this.extraBlockData;
                this.blocks=new byte[this.width][this.height][this.length];
                this.extraBlockData=new byte[this.width][this.height][this.length];
                for(int x=0; x<this.width; x++)
                    for(int y=0; y<this.height; y++)
                        for(int z=0; z<this.length; z++){
                            this.blocks[x][y][z]=oldBlocks[this.width - x - 1][y][this.length - z - 1];
                            this.extraBlockData[x][y][z]=oldData[this.width - x - 1][y][this.length - z - 1];
                        }
            }
            if((angle & 1)!=0){
                byte[][][] oldBlocks=this.blocks,
                        oldData=this.extraBlockData;
                int temp=this.length;
                this.length=this.width;
                this.width=temp;
                this.blocks=new byte[this.length][this.height][this.width];
                this.extraBlockData=new byte[this.length][this.height][this.width];
                for(int x=0; x<this.width; x++)
                    for(int y=0; y<this.height; y++)
                        for(int z=0; z<this.length; z++){
                            // TODO Blargh rotation matrices in minecraft. Brain=hurt.
                            //this.blocks[x][y][z]=oldBlocks
                        }
            }
        }
        return this;
    }
}
