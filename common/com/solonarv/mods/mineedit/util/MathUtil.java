package com.solonarv.mods.mineedit.util;

/**
 * Generic Mathematical utility class
 * @author Solonarv
 *
 */
public class MathUtil {
    
    public static final double TAU = 2*Math.PI;
    
    /**
     * @param angle an integer angle, in number of quarter turns
     * @return the cosine of the given angle
     */
    public static int cosineForIntAngle(int angle){
        return (angle & 1)!=0 ? 0 : (angle & 2) == 0 ? 1 : -1;
    }
    
    /**
     * @param angle an integer angle, in number of quarter turns
     * @return the sine of the given angle
     */
    public static int sineForIntAngle(int angle){
        switch(angle & 3){
            case 0: case 2:
                return 0;
            case 1:
                return 1;
            case 3:
                return -1;
            default: // Won't happen anyway since we're bitmasking angle
                return 0;
        }
    }
    
}
