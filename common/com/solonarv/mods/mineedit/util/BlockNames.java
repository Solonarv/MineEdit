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
        // This was a PAIN. I NEVER want to do this again.
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
        registerBlock("ore:gold", Block.oreGold);
        registerBlock("ore:iron", Block.oreIron);
        registerBlock("ore:coal", Block.oreCoal);
        registerBlock("log", Block.wood);
        registerBlock("leaves", Block.leaves);
        registerBlock("sponge", Block.sponge);
        registerBlock("glass", Block.glass);
        registerBlock("ore:lapis", Block.oreLapis);
        registerBlock("block_lapis", Block.blockLapis);
        registerBlock("dispenser", Block.dispenser);
        registerBlock("sandstone", Block.sandStone);
        registerBlock("noteblock", Block.music);
        registerBlock("bed", Block.bed);
        registerBlock("rail:powered", Block.railPowered);
        registerBlock("rail:detector", Block.railDetector);
        registerBlock("piston_base:sticky", Block.pistonStickyBase);
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
        registerBlock("block:gold", Block.blockGold);
        registerBlock("block:iron", Block.blockIron);
        registerBlock("slab_double", Block.stoneDoubleSlab);
        registerBlock("slab_single", Block.stoneSingleSlab);
        registerBlock("block:bricks", Block.brick);
        registerBlock("tnt", Block.tnt);
        registerBlock("bookshelf", Block.bookShelf);
        registerBlock("moss_stone", Block.cobblestoneMossy);
        registerBlock("obsidian", Block.obsidian);
        registerBlock("torch", Block.torchWood);
        registerBlock("fire", Block.fire);
        registerBlock("spawner", Block.mobSpawner);
        registerBlock("stairs:wood:oak", Block.stairsWoodOak);
        registerBlock("chest", Block.chest);
        registerBlock("redstone_wire", Block.redstoneWire);
        registerBlock("ore:diamond", Block.oreDiamond);
        registerBlock("block:diamond", Block.blockDiamond);
        registerBlock("carfting_table", Block.workbench);
        registerBlock("crop:wheat", Block.crops);
        registerBlock("farmland", Block.tilledField);
        registerBlock("furnace", Block.furnaceIdle);
        registerBlock("furnace:burning", Block.furnaceBurning);
        registerBlock("sign:post", Block.signPost);
        registerBlock("door:wood", Block.doorWood);
        registerBlock("ladder", Block.ladder);
        registerBlock("rail", Block.rail);
        registerBlock("stairs:cobblestone", Block.stairsCobblestone);
        registerBlock("sign:wall", Block.signWall);
        registerBlock("lever", Block.lever);
        registerBlock("pressure_plate:stone", Block.pressurePlateStone);
        registerBlock("door:iron", Block.doorIron);
        registerBlock("pressure_plate:wood", Block.pressurePlatePlanks);
        registerBlock("ore:redstone", Block.oreRedstone);
        registerBlock("ore:redstone_glowing", Block.oreRedstoneGlowing);
        registerBlock("redstone_torch:off", Block.torchRedstoneIdle);
        registerBlock("redstone_torch:on", Block.torchRedstoneActive);
        registerBlock("button:stone", Block.stoneButton);
        registerBlock("snow_layer", Block.snow);
        registerBlock("ice", Block.ice);
        registerBlock("block_snow", Block.blockSnow);
        registerBlock("cactus", Block.cactus);
        registerBlock("block_clay", Block.blockClay);
        registerBlock("sugarcane", Block.reed);
        registerBlock("jukebox", Block.jukebox);
        registerBlock("fence", Block.fence);
        registerBlock("pumpkin", Block.pumpkin);
        registerBlock("netherrack", Block.netherrack);
        registerBlock("soulsand", Block.slowSand);
        registerBlock("glowstone", Block.glowStone);
        registerBlock("portal:nether", Block.portal);
        registerBlock("pumpkin_lantern", Block.pumpkinLantern);
        registerBlock("cake", Block.cake);
        registerBlock("redstone_repeater:off", Block.redstoneRepeaterIdle);
        registerBlock("redstone_repeater:on", Block.redstoneRepeaterActive);
        registerBlock("locked_chest", Block.lockedChest);
        registerBlock("trapdoor", Block.trapdoor);
        registerBlock("silverfish_egg", Block.silverfish);
        registerBlock("stone_brick", Block.stoneBrick);
        registerBlock("mushroom_cap:brown", Block.mushroomCapBrown);
        registerBlock("mushromm_cap:red", Block.mushroomCapRed);
        registerBlock("iron_bars", Block.fenceIron);
        registerBlock("glass_pane", Block.thinGlass);
        registerBlock("block_melon", Block.melon);
        registerBlock("stem:pumpkin", Block.pumpkinStem);
        registerBlock("stem:melon", Block.melonStem);
        registerBlock("vine", Block.vine);
        registerBlock("fence_gate", Block.fenceGate);
        registerBlock("stairs:brick", Block.stairsBrick);
        registerBlock("stairs:stone_brick", Block.stairsStoneBrick);
        registerBlock("mycelium", Block.mycelium);
        registerBlock("lilypad", Block.waterlily);
        registerBlock("block:netherbrick", Block.netherBrick);
        registerBlock("fence:netherbrick", Block.netherFence);
        registerBlock("stairs:netherbrick", Block.stairsNetherBrick);
        registerBlock("nether_wart", Block.netherStalk);
        registerBlock("enchantment_table", Block.enchantmentTable);
        registerBlock("brewing_stand", Block.brewingStand);
        registerBlock("cauldron", Block.cauldron);
        registerBlock("portal:end", Block.endPortal);
        registerBlock("end_portal_frame", Block.endPortalFrame);
        registerBlock("end_stone", Block.whiteStone);
        registerBlock("dragon_egg", Block.dragonEgg);
        registerBlock("redstone_lamp:off", Block.redstoneLampIdle);
        registerBlock("redstone_block:on", Block.redstoneLampActive);
        registerBlock("slab_double:wood", Block.woodDoubleSlab);
        registerBlock("slab_signle:wood", Block.woodSingleSlab);
        registerBlock("cocoa_pod", Block.cocoaPlant);
        registerBlock("stairs:sandstone", Block.stairsSandStone);
        registerBlock("ore:emerald", Block.oreEmerald);
        registerBlock("chest:ender", Block.enderChest);
        registerBlock("tripwire_hook", Block.tripWireSource);
        registerBlock("tripwire", Block.tripWire);
        registerBlock("block:emerald", Block.blockEmerald);
        registerBlock("stairs:wood:spruce", Block.stairsWoodSpruce);
        registerBlock("stairs:wood:birch", Block.stairsWoodBirch);
        registerBlock("stairs:wood:jungle", Block.stairsWoodJungle);
        registerBlock("command_block", Block.commandBlock);
        registerBlock("beacon", Block.beacon);
        registerBlock("wall:cobblestone", Block.cobblestoneWall);
        registerBlock("flower_pot", Block.flowerPot);
        registerBlock("crop:carrot", Block.carrot);
        registerBlock("crop:potato", Block.potato);
        registerBlock("button:wood", Block.woodenButton);
        registerBlock("head", Block.skull);
        registerBlock("anvil", Block.anvil);
        registerBlock("chest:trapped", Block.chestTrapped);
        registerBlock("pressure_plate:gold", Block.pressurePlateGold);
        registerBlock("pressure_plate:iron", Block.pressurePlateIron);
        registerBlock("redstone_comparator:off", Block.redstoneComparatorIdle);
        registerBlock("redstone_comparator:on", Block.redstoneComparatorActive);
        registerBlock("daylight_sensor", Block.daylightSensor);
        registerBlock("block:redstone", Block.blockRedstone);
        registerBlock("ore:nether_quartz", Block.oreNetherQuartz);
        registerBlock("hopper", Block.hopperBlock);
        registerBlock("block:nether_quartz", Block.blockNetherQuartz);
        registerBlock("stairs:nether_quartz", Block.stairsNetherQuartz);
        registerBlock("rail:activator", Block.railActivator);
        registerBlock("dropper", Block.dropper);
        registerBlock("stained_clay", Block.field_111039_cA);
        registerBlock("block:hay", Block.field_111038_cB);
        registerBlock("carpet", Block.field_111031_cC);
        registerBlock("hardened_clay", Block.field_111032_cD);
        registerBlock("block:coal", Block.field_111034_cE);
        ;;
    }
}
