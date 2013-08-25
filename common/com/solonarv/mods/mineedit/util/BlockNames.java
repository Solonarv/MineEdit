package com.solonarv.mods.mineedit.util;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Store string names of all blocks in the game
 * @author Solonarv
 *
 */
public class BlockNames {
    
    private static final BiMap<String, Integer> mapping=HashBiMap.create(256);
    
    static{
        registerVanillaBlocks();
        tryRegisterModBlocks();
    }
    
    private BlockNames(){}

    public static void registerBlock(String name, Block block){
        registerBlock(name, block.blockID);
    }
    
    public static void registerBlock(String name, int id) {
        mapping.put(name, id);
    }
    
    public static Block getBlockForName(String name){
        return Block.blocksList[mapping.get(name)];
    }
    
    public static Set<String> getPartialMatches(String start){
        Set<String> result = new HashSet<String>();
        for(String key : mapping.keySet()){
            if(key.startsWith(start)){
                result.add(key);
            }
        }
        return result;
    }
    
    private static void tryRegisterModBlocks(){
        for(Block b : Block.blocksList){
            if(!mapping.containsValue(b.blockID)){
                String name = b.getUnlocalizedName();
                if(!name.equals("tile.null")){ //Don't auto-register unnamed blocks with their nonexisting name, duh
                    mapping.put(name, b.blockID);
                } else { // Generate some crappy default name instead
                    mapping.put("block_" + String.valueOf(b.blockID), b.blockID);
                }
            }
        }
    }
    
    private static void registerVanillaBlocks() {
        registerBlock("air", 0);
        registerBlock("stone", Block.stone);
        registerBlock("grass", Block.grass);
        registerBlock("dirt", Block.dirt);
        registerBlock("cobblestone", Block.cobblestone);
        registerBlock("planks", Block.planks);
        registerBlock("sapling", Block.sapling);
        registerBlock("bedrock", Block.bedrock);
        registerBlock("waterMoving", Block.waterMoving);
        registerBlock("waterStill", Block.waterStill);
        registerBlock("lavaMoving", Block.lavaMoving);
        registerBlock("lavaStill", Block.lavaStill);
        registerBlock("sand", Block.sand);
        registerBlock("gravel", Block.gravel);
        registerBlock("ore_gold", Block.oreGold);
        registerBlock("ore_iron", Block.oreIron);
        registerBlock("ore_coal", Block.oreCoal);
        registerBlock("log", Block.wood);
        registerBlock("leaves", Block.leaves);
        registerBlock("sponge", Block.sponge);
        registerBlock("glass", Block.glass);
        registerBlock("ore_lapis", Block.oreLapis);
        registerBlock("block_lapis", Block.blockLapis);
        registerBlock("dispenser", Block.dispenser);
        registerBlock("sandstone", Block.sandStone);
        registerBlock("noteblock", Block.music);
        registerBlock("bed", Block.bed);
        registerBlock("rail_powered", Block.railPowered);
        registerBlock("rail_detector", Block.railDetector);
        registerBlock("piston_base_sticky", Block.pistonStickyBase);
        registerBlock("cobweb", Block.web);
        registerBlock("tall_grass", Block.tallGrass);
        registerBlock("dead_bush", Block.deadBush);
        registerBlock("piston_base", Block.pistonBase);
        registerBlock("piston_arm", Block.pistonExtension);
        registerBlock("wool", Block.cloth);
        registerBlock("piston_moving", Block.pistonMoving);
        registerBlock("dandelion", Block.plantYellow);
        registerBlock("rose", Block.plantRed);
        registerBlock("mushroom_brown", Block.mushroomBrown);
        registerBlock("mushroom_red", Block.mushroomRed);
        registerBlock("block_gold", Block.blockGold);
        registerBlock("block_iron", Block.blockIron);
        ;;
    }
}
