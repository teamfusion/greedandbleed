package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.api.PygmyTaskManager;
import com.github.teamfusion.greedandbleed.common.block.PygmyStationBlockEntity;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.Pygmy;
import com.github.teamfusion.greedandbleed.common.item.ClubItem;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class WorkAtPygmyPoi extends Behavior<Pygmy> {
    private static final int CHECK_COOLDOWN = 300;
    private static final double DISTANCE = 1.73;
    private long lastCheck;

    public WorkAtPygmyPoi() {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel, Pygmy pygmy) {
        if (serverLevel.getGameTime() - this.lastCheck < 300L) {
            return false;
        } else if (serverLevel.random.nextInt(2) != 0) {
            return false;
        } else {
            this.lastCheck = serverLevel.getGameTime();
            GlobalPos globalPos = (GlobalPos) pygmy.getBrain().getMemory(MemoryModuleType.JOB_SITE).get();
            return globalPos.dimension() == serverLevel.dimension() && globalPos.pos().closerToCenterThan(pygmy.position(), 1.73);
        }
    }

    protected void start(ServerLevel serverLevel, Pygmy pygmy, long l) {
        Brain<Pygmy> brain = pygmy.getBrain();
        brain.getMemory(MemoryModuleType.JOB_SITE).ifPresent((globalPos) -> {
            brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(globalPos.pos()));
            this.useWorkstation(serverLevel, globalPos, pygmy);
        });
    }

    protected void useWorkstation(ServerLevel serverLevel, GlobalPos globalPos, Pygmy pygmy) {
        BlockEntity blockEntity = serverLevel.getBlockEntity(globalPos.pos());
        if (blockEntity instanceof PygmyStationBlockEntity pygmyStationBlock) {

            if (pygmy.getTaskManager() instanceof PygmyTaskManager pygmyTaskManager) {
                if (pygmyTaskManager.getWorkTime() < 600) {
                    ItemStack stack2 = findBelt(pygmyStationBlock);
                    if (!stack2.isEmpty()) {
                        stack2.shrink(1);
                        pygmyTaskManager.addWorkTime(24000);
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
                    ItemStack stack = findArmor(pygmyStationBlock);
                    if (!stack.isEmpty()) {
                        ItemStack stack2 = pygmy.equipItemIfPossible(stack);
                        if (!stack2.isEmpty()) {
                            stack.shrink(1);
                        }
                    }
                }
            }
        }
    }

    private ItemStack findArmor(PygmyStationBlockEntity pygmyStationBlock) {
        for (int i = 0; i < pygmyStationBlock.getItems().size(); ++i) {
            ItemStack itemstack = pygmyStationBlock.getItems().get(i);
            if (!itemstack.isEmpty() && (itemstack.getItem() instanceof ArmorItem) || itemstack.getItem() instanceof SwordItem || itemstack.getItem() instanceof ClubItem) {
                return itemstack.copy();
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

    protected boolean canStillUse(ServerLevel serverLevel, Pygmy pygmy, long l) {
        Optional<GlobalPos> optional = pygmy.getBrain().getMemory(MemoryModuleType.JOB_SITE);
        if (!optional.isPresent()) {
            return false;
        } else {
            GlobalPos globalPos = (GlobalPos) optional.get();
            return globalPos.dimension() == serverLevel.dimension() && globalPos.pos().closerToCenterThan(pygmy.position(), 1.73);
        }
    }
}
