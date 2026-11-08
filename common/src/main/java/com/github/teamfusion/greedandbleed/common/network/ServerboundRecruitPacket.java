package com.github.teamfusion.greedandbleed.common.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.block.blockentity.PygmyStationBlockEntity;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.GBPigmy;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.github.teamfusion.greedandbleed.common.registry.MemoryRegistry;
import com.github.teamfusion.greedandbleed.common.registry.PoiRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;

public record ServerboundRecruitPacket(BlockPos origin) implements Packet<ServerboundRecruitPacket> {
    public static final ResourceLocation ID = new ResourceLocation(GreedAndBleed.MOD_ID, "recruit");
    public static final Handler HANDLER = new Handler();
    
    @Override
    public ResourceLocation getId() {
        return ID;
    }
    
    @Override
    public PacketHandler<ServerboundRecruitPacket> getHandler() {
        return HANDLER;
    }
    
    public static class Handler implements PacketHandler<ServerboundRecruitPacket> {
        @Override
        public void encode(ServerboundRecruitPacket packet, FriendlyByteBuf buf) {
            buf.writeBlockPos(packet.origin);
        }
        
        @Override
        public ServerboundRecruitPacket decode(FriendlyByteBuf buf) {
            return new ServerboundRecruitPacket(buf.readBlockPos());
        }
        
        @Override
        public PacketContext handle(ServerboundRecruitPacket packet) {
            return (player, level) -> {
                GBPigmy pygmy = level.getNearestEntity(
                    GBPigmy.class,
                    TargetingConditions.forNonCombat()
                        .range(10F)
                        .ignoreLineOfSight()
                        .ignoreInvisibilityTesting(),
                    player,
                    player.blockPosition().getX(),
                    player.blockPosition().getY(),
                    player.blockPosition().getZ(),
                    new AABB(player.blockPosition())
                        .inflate(10F)
                );
                
                if (pygmy != null && level instanceof ServerLevel server) {
                    Brain<?> brain = pygmy.getBrain();
                    BlockEntity blockEntity = server.getChunkAt(packet.origin).getBlockEntity(packet.origin, LevelChunk.EntityCreationType.IMMEDIATE);
                    
                    if (blockEntity instanceof PygmyStationBlockEntity station && !pygmy.getBrain().hasMemoryValue(MemoryModuleType.JOB_SITE)) {
                        ItemStack stack = station.getItem(0);
                        ItemStack stack2 = station.getItem(1);
                        
                        if (stack.getItem() == ItemRegistry.PIGLIN_BELT.get() && !stack2.isEmpty()) {
                            server.getPoiManager()
                                .take(holder -> holder.is(PoiRegistry.PYGMY_STATION), (holder, pos) -> pos.equals(packet.origin), packet.origin, 1)
                                .ifPresent(pos -> {
                                    brain.setMemory(MemoryModuleType.JOB_SITE, GlobalPos.of(server.dimension(), packet.origin));
                                    brain.setMemory(MemoryModuleType.LIKED_PLAYER, player.getUUID());
                                    brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                                    addWorkTime(pygmy, 24000 * stack.getCount());
                                    pygmy.setPatrolRange(Mth.clamp(2 * stack2.getCount(), 0, 40));
                                    station.setPygmyRange(Mth.clamp(2 * stack2.getCount(), 0, 40));
                                    stack.shrink(stack.getCount());
                                    pygmy.playSound(SoundEvents.ITEM_PICKUP, 0.7F, 1.25F);
                                    pygmy.swing(InteractionHand.MAIN_HAND);
                                    pygmy.setMode(GBPigmy.Mode.PATROL);
                                    pygmy.setPersistenceRequired();
                                    DebugPackets.sendPoiTicketCountPacket(server, packet.origin);
                                });
                        }
                    }
                }
            };
        }
        
        private static void addWorkTime(GBPigmy gbPigmy, int time) {
            if (gbPigmy.getBrain().hasMemoryValue(MemoryRegistry.WORK_TIME.get())) {
                gbPigmy.getBrain().setMemory(MemoryRegistry.WORK_TIME.get(), time + gbPigmy.getBrain().getMemory(MemoryRegistry.WORK_TIME.get()).get());
            } else {
                gbPigmy.getBrain().setMemory(MemoryRegistry.WORK_TIME.get(), time);
            }
            gbPigmy.getBrain().setActiveActivityIfPossible(Activity.WORK);
        }
    }
}