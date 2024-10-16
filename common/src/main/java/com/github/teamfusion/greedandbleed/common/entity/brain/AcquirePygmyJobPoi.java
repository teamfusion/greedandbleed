package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.block.blockentity.PygmyStationBlockEntity;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.GBPygmy;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.github.teamfusion.greedandbleed.common.registry.MemoryRegistry;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.pathfinder.Path;
import org.apache.commons.lang3.mutable.MutableLong;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AcquirePygmyJobPoi {
    public static final int SCAN_RANGE = 48;

    public AcquirePygmyJobPoi() {
    }

    public static BehaviorControl<GBPygmy> create(Predicate<Holder<PoiType>> predicate, MemoryModuleType<GlobalPos> memoryModuleType, boolean bl, Optional<Byte> optional) {
        MutableLong mutableLong = new MutableLong(0L);
        Long2ObjectMap<AcquirePygmyJobPoi.JitteredLinearRetry> long2ObjectMap = new Long2ObjectOpenHashMap();
        OneShot<GBPygmy> oneShot = BehaviorBuilder.create((instance) -> {
            return instance.group(instance.absent(memoryModuleType)).apply(instance, (memoryAccessor) -> {
                return (serverLevel, pathfinderMob, l) -> {
                    if (bl && pathfinderMob.isBaby()) {
                        return false;
                    } else if (mutableLong.getValue() == 0L) {
                        mutableLong.setValue(serverLevel.getGameTime() + (long) serverLevel.random.nextInt(20));
                        return false;
                    } else if (serverLevel.getGameTime() < mutableLong.getValue()) {
                        return false;
                    } else {
                        mutableLong.setValue(l + 20L + (long) serverLevel.getRandom().nextInt(20));
                        PoiManager poiManager = serverLevel.getPoiManager();
                        long2ObjectMap.long2ObjectEntrySet().removeIf((entry) -> {
                            return !entry.getValue().isStillValid(l);
                        });
                        Predicate<BlockPos> predicate2 = (blockPosx) -> {
                            AcquirePygmyJobPoi.JitteredLinearRetry jitteredLinearRetry = long2ObjectMap.get(blockPosx.asLong());
                            if (jitteredLinearRetry == null) {
                                return true;
                            } else if (!jitteredLinearRetry.shouldRetry(l)) {
                                return false;
                            } else {
                                jitteredLinearRetry.markAttempt(l);
                                return true;
                            }
                        };
                        Set<Pair<Holder<PoiType>, BlockPos>> set = poiManager.findAllClosestFirstWithType(predicate, predicate2, pathfinderMob.blockPosition(), 16, PoiManager.Occupancy.HAS_SPACE).limit(5L).collect(Collectors.toSet());
                        Path path = findPathToPois(pathfinderMob, set);
                        if (path != null && path.canReach()) {
                            BlockPos blockPos = path.getTarget();
                            BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
                            if (blockEntity instanceof PygmyStationBlockEntity pygmyStationBlock) {
                                ItemStack stack = pygmyStationBlock.getItem(0);
                                if (stack.getItem() == ItemRegistry.PIGLIN_BELT.get()) {
                                    poiManager.getType(blockPos).ifPresent((holder) -> {
                                        poiManager.take(predicate, (holderx, blockPos2) -> {
                                            return blockPos2.equals(blockPos);
                                        }, blockPos, 1);
                                        memoryAccessor.set(GlobalPos.of(serverLevel.dimension(), blockPos));
                                        optional.ifPresent((byte_) -> {
                                            serverLevel.broadcastEntityEvent(pathfinderMob, byte_);
                                        });
                                        long2ObjectMap.clear();
                                        stack.shrink(1);
                                        addWorkTime(pathfinderMob, 24000);
                                        pathfinderMob.playSound(SoundEvents.ITEM_PICKUP, 0.7F, 1.25F);
                                        pathfinderMob.swing(InteractionHand.MAIN_HAND);
                                        DebugPackets.sendPoiTicketCountPacket(serverLevel, blockPos);
                                    });
                                }
                            }
                        } else {
                            Iterator var14 = set.iterator();

                            while (var14.hasNext()) {
                                Pair<Holder<PoiType>, BlockPos> pair = (Pair) var14.next();
                                long2ObjectMap.computeIfAbsent(pair.getSecond().asLong(), (m) -> {
                                    return new AcquirePygmyJobPoi.JitteredLinearRetry(serverLevel.random, l);
                                });
                            }
                        }

                        return true;
                    }
                };
            });
        });
        return oneShot;
    }

    public static void addWorkTime(GBPygmy gbPygmy, int time) {
        if (gbPygmy.getBrain().hasMemoryValue(MemoryRegistry.WORK_TIME.get())) {
            gbPygmy.getBrain().setMemory(MemoryRegistry.WORK_TIME.get(), time + gbPygmy.getBrain().getMemory(MemoryRegistry.WORK_TIME.get()).get());
        } else {
            gbPygmy.getBrain().setMemory(MemoryRegistry.WORK_TIME.get(), time);
        }
        gbPygmy.getBrain().setActiveActivityIfPossible(Activity.WORK);
    }

    @Nullable
    public static Path findPathToPois(Mob mob, Set<Pair<Holder<PoiType>, BlockPos>> set) {
        if (set.isEmpty()) {
            return null;
        } else {
            Set<BlockPos> set2 = new HashSet();
            int i = 1;
            Iterator var4 = set.iterator();

            while (var4.hasNext()) {
                Pair<Holder<PoiType>, BlockPos> pair = (Pair) var4.next();
                i = Math.max(i, ((PoiType) ((Holder) pair.getFirst()).value()).validRange());
                set2.add(pair.getSecond());
            }

            return mob.getNavigation().createPath(set2, i);
        }
    }

    static class JitteredLinearRetry {
        private static final int MIN_INTERVAL_INCREASE = 40;
        private static final int MAX_INTERVAL_INCREASE = 80;
        private static final int MAX_RETRY_PATHFINDING_INTERVAL = 400;
        private final RandomSource random;
        private long previousAttemptTimestamp;
        private long nextScheduledAttemptTimestamp;
        private int currentDelay;

        JitteredLinearRetry(RandomSource randomSource, long l) {
            this.random = randomSource;
            this.markAttempt(l);
        }

        public void markAttempt(long l) {
            this.previousAttemptTimestamp = l;
            int i = this.currentDelay + this.random.nextInt(40) + 40;
            this.currentDelay = Math.min(i, 400);
            this.nextScheduledAttemptTimestamp = l + (long) this.currentDelay;
        }

        public boolean isStillValid(long l) {
            return l - this.previousAttemptTimestamp < 400L;
        }

        public boolean shouldRetry(long l) {
            return l >= this.nextScheduledAttemptTimestamp;
        }

        public String toString() {
            return "RetryMarker{, previousAttemptAt=" + this.previousAttemptTimestamp + ", nextScheduledAttemptAt=" + this.nextScheduledAttemptTimestamp + ", currentDelay=" + this.currentDelay + "}";
        }
    }
}
