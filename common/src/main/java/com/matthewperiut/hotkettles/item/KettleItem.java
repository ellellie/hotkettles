package com.matthewperiut.hotkettles.item;

import com.matthewperiut.hotkettles.blockentity.KettleBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import static com.matthewperiut.hotkettles.block.KettleBlock.KETTLE_TYPE;
import static com.matthewperiut.hotkettles.components.HotKettleComponents.LIQUID_LEVEL_COMPONENT;

public class KettleItem extends BlockItem {
    public int kettle_type = 0;

    public KettleItem(Block block, Settings settings, int kettle_type) {
        super(block, settings.maxCount(1));
        this.kettle_type = kettle_type;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable("block." + Registries.ITEM.getId(this).toString().replace(":", "."));
    }

    public ActionResult place(ItemPlacementContext context) {
        ActionResult result = super.place(context);
        if (result == ActionResult.SUCCESS) {
            int liquidLevel = context.getStack().getComponents().getOrDefault(LIQUID_LEVEL_COMPONENT.get(), -1);
            if (liquidLevel != -1) {
                ((KettleBlockEntity) context.getWorld().getBlockEntity(context.getBlockPos())).setLiquidLevel(liquidLevel);
            } else {
                if (context.getWorld().getBlockState(context.getBlockPos()).get(KETTLE_TYPE) == 0) {
                    ((KettleBlockEntity) context.getWorld().getBlockEntity(context.getBlockPos())).setLiquidLevel(0);
                } else {
                    ((KettleBlockEntity) context.getWorld().getBlockEntity(context.getBlockPos())).setLiquidLevel(5);
                }
            }
        }

        return result;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        ComponentMap components = stack.getComponents();
        int liquidLevel = components.getOrDefault(LIQUID_LEVEL_COMPONENT.get(), 0);
        if (liquidLevel != 0) {
            return (int) (liquidLevel * 2.6f);
        }
        return 13;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x00FF00; // Replace with desired RGB color
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        ComponentMap components = stack.getComponents();
        int liquidLevel = components.getOrDefault(LIQUID_LEVEL_COMPONENT.get(), 0);
        return liquidLevel != 5;
    }
}
