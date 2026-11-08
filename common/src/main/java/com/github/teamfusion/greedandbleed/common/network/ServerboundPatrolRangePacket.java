package com.github.teamfusion.greedandbleed.common.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.block.blockentity.PygmyStationBlockEntity;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.GBPigmy;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;

public record ServerboundPatrolRangePacket(BlockPos origin, int range) implements Packet<ServerboundPatrolRangePacket> {
    public static final ResourceLocation ID = new ResourceLocation(GreedAndBleed.MOD_ID, "patrol_range");
    public static final Handler HANDLER = new Handler();
    
    @Override
    public ResourceLocation getId() {
        return ID;
    }
    
    @Override
    public PacketHandler<ServerboundPatrolRangePacket> getHandler() {
        return HANDLER;
    }
    
    public static class Handler implements PacketHandler<ServerboundPatrolRangePacket> {
        @Override
        public void encode(ServerboundPatrolRangePacket packet, FriendlyByteBuf buf) {
            buf.writeBlockPos(packet.origin);
            buf.writeInt(packet.range);
        }
        
        @Override
        public ServerboundPatrolRangePacket decode(FriendlyByteBuf buf) {
            return new ServerboundPatrolRangePacket(buf.readBlockPos(), buf.readInt());
        }
        
        @Override
        public PacketContext handle(ServerboundPatrolRangePacket packet) {
            return (player, level) -> {
                GBPigmy pygmy = level.getNearestEntity(
                    GBPigmy.class,
                    TargetingConditions.forNonCombat()
                        .range(18F)
                        .ignoreLineOfSight()
                        .ignoreInvisibilityTesting().selector(livingEntity -> {
                            if (livingEntity instanceof GBPigmy pygmy1) {
                                return pygmy1.getBrain().hasMemoryValue(MemoryModuleType.JOB_SITE) && pygmy1.getBrain().getMemory(MemoryModuleType.JOB_SITE).get().pos().equals(packet.origin);
                            }
                            
                            return false;
                        }),
                    player,
                    player.blockPosition().getX(),
                    player.blockPosition().getY(),
                    player.blockPosition().getZ(),
                    new AABB(player.blockPosition())
                        .inflate(18F)
                );
                
                if (pygmy != null && level instanceof ServerLevel server) {
                    BlockEntity blockEntity = server.getChunkAt(packet.origin).getBlockEntity(packet.origin, LevelChunk.EntityCreationType.IMMEDIATE);
                    
                    if (blockEntity instanceof PygmyStationBlockEntity station) {
                        ItemStack stack = station.getItem(0);
                        ItemStack stack2 = station.getItem(1);
                        int i = Mth.clamp(packet.range + pygmy.getPatrolRange(), 0, Mth.clamp(stack2.getCount() * 2, 0, 40));
                        station.setPygmyRange(i);
                        pygmy.setPatrolRange(i);
                    }
                }
            };
        }
    }
}