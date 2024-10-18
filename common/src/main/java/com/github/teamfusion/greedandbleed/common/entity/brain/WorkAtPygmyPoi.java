package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.block.blockentity.PygmyArmorStandBlockEntity;
import com.github.teamfusion.greedandbleed.common.block.blockentity.PygmyStationBlockEntity;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.GBPygmy;
import com.github.teamfusion.greedandbleed.common.item.ClubItem;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.github.teamfusion.greedandbleed.common.registry.MemoryRegistry;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class WorkAtPygmyPoi extends Behavior<GBPygmy> {
    private static final int CHECK_COOLDOWN = 300;
    private static final double DISTANCE = 1.73;
    private long lastCheck;

    public WorkAtPygmyPoi() {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, GBPygmy pygmy) {
        if (serverLevel.getGameTime() - this.lastCheck < 30L) {
            return false;
        } else {
            this.lastCheck = serverLevel.getGameTime();
            GlobalPos globalPos = (GlobalPos) pygmy.getBrain().getMemory(MemoryModuleType.JOB_SITE).get();
            return globalPos.dimension() == serverLevel.dimension() && globalPos.pos().closerToCenterThan(pygmy.position(), 10);
        }
    }

    @Override
    protected void start(ServerLevel serverLevel, GBPygmy pygmy, long l) {
        Brain<?> brain = pygmy.getBrain();
        brain.getMemory(MemoryModuleType.JOB_SITE).ifPresent((globalPos) -> {
            brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(globalPos.pos()));
            this.useWorkstation(serverLevel, globalPos, pygmy);
        });
    }

    protected void useWorkstation(ServerLevel serverLevel, GlobalPos globalPos, GBPygmy pygmy) {
        BlockEntity armorStand = serverLevel.getBlockEntity(globalPos.pos().below());
        BlockEntity blockEntity = serverLevel.getBlockEntity(globalPos.pos());
        if (blockEntity instanceof PygmyStationBlockEntity pygmyStationBlock) {

            if (getWorkTime(pygmy) <= 600) {
                    ItemStack stack2 = findBelt(pygmyStationBlock);
                    if (!stack2.isEmpty()) {
                        stack2.shrink(1);
                        addWorkTime(pygmy, 24000);
                    } else {
                        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                            if (pygmy.getEquipmentDropChance(equipmentSlot) >= 2.0F) {
                                ItemStack stack = pygmy.getItemBySlot(equipmentSlot);
                                if (!stack.isEmpty()) {
                                    if (pygmyStationBlock.getItems().add(stack.copy())) {
                                        stack.shrink(1);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (armorStand instanceof PygmyArmorStandBlockEntity armorStandBlockEntity) {

                        ItemStack stack = findArmor(armorStandBlockEntity);
                        if (!stack.isEmpty()) {
                            ItemStack stack2 = pygmy.equipItemIfPossible(stack.copy());
                            if (!stack2.isEmpty()) {
                                stack.shrink(1);
                            }
                        }
                    }
                }
        }
    }


    public void addWorkTime(GBPygmy gbPygmy, int time) {
        if (gbPygmy.getBrain().hasMemoryValue(MemoryRegistry.WORK_TIME.get())) {
            gbPygmy.getBrain().setMemory(MemoryRegistry.WORK_TIME.get(), time + gbPygmy.getBrain().getMemory(MemoryRegistry.WORK_TIME.get()).get());
        } else {
            gbPygmy.getBrain().setMemory(MemoryRegistry.WORK_TIME.get(), time);
        }
        gbPygmy.getBrain().setActiveActivityIfPossible(Activity.WORK);
    }

    public int getWorkTime(GBPygmy gbPygmy) {
        if (gbPygmy.getBrain().hasMemoryValue(MemoryRegistry.WORK_TIME.get())) {
            return gbPygmy.getBrain().getMemory(MemoryRegistry.WORK_TIME.get()).get();
        } else {
            return 0;
        }
    }

    private ItemStack findArmor(PygmyArmorStandBlockEntity pygmyStationBlock) {
        for (int i = 0; i < pygmyStationBlock.getItems().size(); ++i) {
            ItemStack itemstack = pygmyStationBlock.getItems().get(i);
            if (!itemstack.isEmpty() && (itemstack.getItem() instanceof ArmorItem) || itemstack.getItem() instanceof SwordItem || itemstack.getItem() instanceof ClubItem) {
                return itemstack.split(1);
            }
        }
        return ItemStack.EMPTY;
    }

    private ItemStack findBelt(PygmyStationBlockEntity pygmyStationBlock) {
        for (int i = 0; i < pygmyStationBlock.getItems().size(); ++i) {
            ItemStack itemstack = pygmyStationBlock.getItems().get(i);
            if (!itemstack.isEmpty() && itemstack.is(ItemRegistry.PIGLIN_BELT.get())) {
                return itemstack.copy();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, GBPygmy pygmy, long l) {
        Optional<GlobalPos> optional = pygmy.getBrain().getMemory(MemoryModuleType.JOB_SITE);
        if (!optional.isPresent()) {
            return false;
        } else {
            GlobalPos globalPos = (GlobalPos) optional.get();
            return globalPos.dimension() == serverLevel.dimension() && globalPos.pos().closerToCenterThan(pygmy.position(), 1.73);
        }
    }
}
