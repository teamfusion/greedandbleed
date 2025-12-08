package com.github.teamfusion.greedandbleed.common.network;

import com.github.teamfusion.greedandbleed.common.block.blockentity.PygmyStationBlockEntity;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pigmy.GBPigmy;
import com.github.teamfusion.greedandbleed.common.item.slingshot.SlingshotPouchItem;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.github.teamfusion.greedandbleed.common.registry.MemoryRegistry;
import com.github.teamfusion.greedandbleed.common.registry.PoiRegistry;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;

public class GreedAndBleedServerNetwork implements GreedAndBleedNetwork {

    public static void registerReceivers() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SELECT_SYNC_PACKET, GreedAndBleedServerNetwork::onSelectSync);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, RECRUIT_PACKET, GreedAndBleedServerNetwork::onRecruit);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, PATROL_RANGE_PACKET, GreedAndBleedServerNetwork::onPatrol);
    }

    private static void onPatrol(FriendlyByteBuf friendlyByteBuf, NetworkManager.PacketContext packetContext) {
        Player player = packetContext.getPlayer();
        Level level = player.level();
        BlockPos origin = friendlyByteBuf.readBlockPos();
        int range = friendlyByteBuf.readInt();
        GBPigmy pygmy = level.getNearestEntity(
                GBPigmy.class,
                TargetingConditions.forNonCombat()
                        .range(18F)
                        .ignoreLineOfSight()
                        .ignoreInvisibilityTesting().selector(livingEntity -> {
                            if (livingEntity instanceof GBPigmy pygmy1) {
                                return pygmy1.getBrain().hasMemoryValue(MemoryModuleType.JOB_SITE) && pygmy1.getBrain().getMemory(MemoryModuleType.JOB_SITE).get().pos().equals(origin);
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
            BlockEntity blockEntity = server.getChunkAt(origin).getBlockEntity(origin, LevelChunk.EntityCreationType.IMMEDIATE);

            if (blockEntity instanceof PygmyStationBlockEntity station) {
                ItemStack stack = station.getItem(0);
                ItemStack stack2 = station.getItem(1);
                int i = Mth.clamp(range + pygmy.getPatrolRange(), 0, Mth.clamp(stack2.getCount() * 2, 0, 40));
                station.setPygmyRange(i);
                pygmy.setPatrolRange(i);
            }
        }
    }

    private static void onRecruit(FriendlyByteBuf friendlyByteBuf, NetworkManager.PacketContext packetContext) {
        Player player = packetContext.getPlayer();
        Level level = player.level();
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

        BlockPos origin = friendlyByteBuf.readBlockPos();
        if (pygmy != null && level instanceof ServerLevel server) {
            Brain<?> brain = pygmy.getBrain();
            BlockEntity blockEntity = server.getChunkAt(origin).getBlockEntity(origin, LevelChunk.EntityCreationType.IMMEDIATE);

            if (blockEntity instanceof PygmyStationBlockEntity station && !pygmy.getBrain().hasMemoryValue(MemoryModuleType.JOB_SITE)) {
                ItemStack stack = station.getItem(0);
                ItemStack stack2 = station.getItem(1);

                if (stack.getItem() == ItemRegistry.PIGLIN_BELT.get() && !stack2.isEmpty()) {
                    server.getPoiManager()
                            .take(holder -> holder.is(PoiRegistry.PYGMY_STATION), (holder, pos) -> pos.equals(origin), origin, 1)
                            .ifPresent(pos -> {
                                brain.setMemory(MemoryModuleType.JOB_SITE, GlobalPos.of(server.dimension(), origin));
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
                                DebugPackets.sendPoiTicketCountPacket(server, origin);
                            });
                }
            }
        }
    }

    private static void onSelectSync(FriendlyByteBuf friendlyByteBuf, NetworkManager.PacketContext packetContext) {
        int id = friendlyByteBuf.readInt();
        Player player = packetContext.getPlayer();
        ItemStack pouch = player.getMainHandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getMainHandItem() : player.getOffhandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getOffhandItem() : ItemStack.EMPTY;

        if (!pouch.isEmpty()) {
            SlingshotPouchItem.cycle(id, pouch);
        }
    }

    public static void addWorkTime(GBPigmy gbPigmy, int time) {
        if (gbPigmy.getBrain().hasMemoryValue(MemoryRegistry.WORK_TIME.get())) {
            gbPigmy.getBrain().setMemory(MemoryRegistry.WORK_TIME.get(), time + gbPigmy.getBrain().getMemory(MemoryRegistry.WORK_TIME.get()).get());
        } else {
            gbPigmy.getBrain().setMemory(MemoryRegistry.WORK_TIME.get(), time);
        }
        gbPigmy.getBrain().setActiveActivityIfPossible(Activity.WORK);
    }
}
