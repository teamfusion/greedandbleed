package com.github.teamfusion.greedandbleed.api;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A class with reflection helper methods relevant to Piglins
 * @author Thelnfamous1
 */
public class PiglinReflectionHelper {
    private static final String CAN_HUNT_METHOD_NAME = "func_234422_eK_";
    private static final String CAN_REPLACE_CURRENT_ITEM_METHOD_NAME = "func_208003_a";
    private static Method canHunt;
    private static Method canReplaceCurrentItem;

    /**
     * Equivalent to MobEntity#canReplaceCurrentItem
     * @param mob The MobEntity to invoke canReplaceCurrentItem on
     * @param replacementItem The ItemStack that is considered as a replacement
     * @param currentItem The ItemStack that is considered to be replaced
     * @return Whether or not replacementItem should replace currentItem
     */
    public static boolean reflectCanReplaceCurrentItem(MobEntity mob, ItemStack replacementItem, ItemStack currentItem) {
        if(canReplaceCurrentItem == null){
            canReplaceCurrentItem = ObfuscationReflectionHelper.findMethod(MobEntity.class, CAN_REPLACE_CURRENT_ITEM_METHOD_NAME, ItemStack.class, ItemStack.class);
        }
        try {
            return (boolean) canReplaceCurrentItem.invoke(mob, replacementItem, currentItem);
        } catch (IllegalAccessException | InvocationTargetException e) {
            GreedAndBleed.LOGGER.error("Reflection error for method canReplaceCurrentItem in MobEntity! Used method name {} on {}", CAN_REPLACE_CURRENT_ITEM_METHOD_NAME, mob.toString());
        }
        return false;
    }
}
