package com.github.teamfusion.greedandbleed.common.item.slingshot;

import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SlingshotPouchItem extends Item {
    public static final String TAG_ITEMS = "Items";
    public static final int MAX_WEIGHT = 64 * 6;
    private static final int BUNDLE_IN_BUNDLE_WEIGHT = 4;
    private static final int BAR_COLOR = Mth.color(0.4f, 0.4f, 1.0f);

    public SlingshotPouchItem(Item.Properties properties) {
        super(properties);
    }

    public static float getFullnessDisplay(ItemStack itemStack) {
        return (float) SlingshotPouchItem.getContentWeight(itemStack) / 64 * 6.0f;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) {
        if (clickAction != ClickAction.SECONDARY) {
            return false;
        }
        ItemStack itemStack22 = slot.getItem();
        if (itemStack22.isEmpty()) {
            this.playRemoveOneSound(player);
            SlingshotPouchItem.removeOne(itemStack).ifPresent(itemStack2 -> SlingshotPouchItem.add(itemStack, slot.safeInsert((ItemStack) itemStack2)));
        } else if (itemStack22.getItem().canFitInsideContainerItems() && SlingshotItem.getAmmoBehavior(itemStack22.getItem()) != null) {
            int i = (64 * 6 - SlingshotPouchItem.getContentWeight(itemStack)) / SlingshotPouchItem.getWeight(itemStack22);
            int j = SlingshotPouchItem.add(itemStack, slot.safeTake(itemStack22.getCount(), i, player));
            if (j > 0) {
                this.playInsertSound(player);
            }
        }
        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack itemStack2, ItemStack itemStack22, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction != ClickAction.SECONDARY || !slot.allowModification(player)) {
            return false;
        }
        if (itemStack22.isEmpty()) {
            SlingshotPouchItem.removeOne(itemStack2).ifPresent(itemStack -> {
                this.playRemoveOneSound(player);
                slotAccess.set((ItemStack) itemStack);
            });
        } else if (SlingshotItem.getAmmoBehavior(itemStack22.getItem()) != null) {
            int i = SlingshotPouchItem.add(itemStack2, itemStack22);
            if (i > 0) {
                this.playInsertSound(player);
                itemStack22.shrink(i);
            }
        }
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        return SlingshotPouchItem.getContentWeight(itemStack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return Math.min(1 + 12 * SlingshotPouchItem.getContentWeight(itemStack) / (64 * 6), 13);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return BAR_COLOR;
    }

    public static int add(ItemStack itemStack, ItemStack itemStack2) {
        if (itemStack2.isEmpty() || !itemStack2.getItem().canFitInsideContainerItems()) {
            return 0;
        }
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (!compoundTag.contains(TAG_ITEMS)) {
            compoundTag.put(TAG_ITEMS, new ListTag());
        }
        int i = SlingshotPouchItem.getContentWeight(itemStack);
        int j = SlingshotPouchItem.getWeight(itemStack2);
        int k = Math.min(itemStack2.getCount(), (64 * 6 - i) / j);
        if (k == 0) {
            return 0;
        }
        ListTag listTag = compoundTag.getList(TAG_ITEMS, 10);
        Optional<CompoundTag> optional = SlingshotPouchItem.getMatchingItem(itemStack2, listTag);
        if (optional.isPresent() && itemStack2.getCount() < 64) {
            CompoundTag compoundTag2 = optional.get();
            CompoundTag compoundTag3 = compoundTag2.copy();
            ItemStack itemStack3 = ItemStack.of(compoundTag3);
            itemStack3.grow(k);
            listTag.remove(compoundTag2);
            itemStack3.save(compoundTag3);
            listTag.add(0, compoundTag3);

        } else {
            ItemStack itemStack4 = itemStack2.copyWithCount(k);
            CompoundTag compoundTag3 = new CompoundTag();
            itemStack4.save(compoundTag3);
            listTag.add(0, compoundTag3);
        }
        return k;
    }


    private static Optional<CompoundTag> getMatchingItem(ItemStack itemStack, ListTag listTag) {
        if (itemStack.is(ItemRegistry.SLINGSHOT_POUCH.get())) {
            return Optional.empty();
        }
        return listTag.stream().filter(CompoundTag.class::isInstance).map(CompoundTag.class::cast).filter(compoundTag -> ItemStack.isSameItemSameTags(ItemStack.of(compoundTag), itemStack) && itemStack.getCount() + ItemStack.of(compoundTag).getCount() < 64).findFirst();
    }

    private static int getWeight(ItemStack itemStack) {
        CompoundTag compoundTag;
        if (SlingshotItem.getAmmoBehavior(itemStack.getItem()) != null) {
            return 64 / itemStack.getMaxStackSize();
        }
        if (itemStack.hasTag() && (compoundTag = BlockItem.getBlockEntityData(itemStack)) != null && !compoundTag.getList("Bees", 10).isEmpty()) {
            return 64 * 10;
        }
        return 64 * 10;
    }

    private static int getContentWeight(ItemStack itemStack2) {
        return SlingshotPouchItem.getContents(itemStack2).mapToInt(itemStack -> SlingshotPouchItem.getWeight(itemStack) * itemStack.getCount()).sum();
    }


    public static Optional<ItemStack> removeOneCountItem(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (!compoundTag.contains(TAG_ITEMS)) {
            return Optional.empty();
        }
        ListTag listTag = compoundTag.getList(TAG_ITEMS, 10);
        if (listTag.isEmpty()) {
            return Optional.empty();
        }
        boolean i = false;
        CompoundTag compoundTag2 = listTag.getCompound(SlingshotPouchItem.getSelectedItem(itemStack));
        ItemStack itemStack2 = ItemStack.of(compoundTag2);
        itemStack2.shrink(1);

        if (itemStack2.isEmpty()) {
            listTag.remove(SlingshotPouchItem.getSelectedItem(itemStack));
            compoundTag.remove("ItemSelect");
        } else {
            listTag.set(SlingshotPouchItem.getSelectedItem(itemStack), itemStack2.save(new CompoundTag()));
        }
        if (listTag.isEmpty()) {
            itemStack.removeTagKey(TAG_ITEMS);
        }
        return Optional.of(itemStack2);
    }

    public static Optional<ItemStack> removeOne(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (!compoundTag.contains(TAG_ITEMS)) {
            return Optional.empty();
        }
        ListTag listTag = compoundTag.getList(TAG_ITEMS, 10);
        if (listTag.isEmpty()) {
            return Optional.empty();
        }
        boolean i = false;
        CompoundTag compoundTag2 = listTag.getCompound(0);
        ItemStack itemStack2 = ItemStack.of(compoundTag2);
        listTag.remove(0);
        if (listTag.isEmpty()) {
            itemStack.removeTagKey(TAG_ITEMS);
        }
        compoundTag.remove("ItemSelect");
        return Optional.of(itemStack2);
    }

    private static boolean dropContents(ItemStack itemStack, Player player) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (!compoundTag.contains(TAG_ITEMS)) {
            return false;
        }
        if (player instanceof ServerPlayer) {
            ListTag listTag = compoundTag.getList(TAG_ITEMS, 10);
            for (int i = 0; i < listTag.size(); ++i) {
                CompoundTag compoundTag2 = listTag.getCompound(i);
                ItemStack itemStack2 = ItemStack.of(compoundTag2);
                player.drop(itemStack2, true);
            }
        }
        itemStack.removeTagKey(TAG_ITEMS);
        return true;
    }

    public static Stream<ItemStack> getContents(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null) {
            return Stream.empty();
        }
        ListTag listTag = compoundTag.getList(TAG_ITEMS, 10);
        return listTag.stream().map(CompoundTag.class::cast).map(ItemStack::of);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        NonNullList<ItemStack> nonNullList = NonNullList.create();
        SlingshotPouchItem.getContents(itemStack).forEach(nonNullList::add);
        return Optional.of(new BundleTooltip(nonNullList, SlingshotPouchItem.getContentWeight(itemStack)));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("item.minecraft.bundle.fullness", SlingshotPouchItem.getContentWeight(itemStack), 64 * 6).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        ItemUtils.onContainerDestroyed(itemEntity, SlingshotPouchItem.getContents(itemEntity.getItem()));
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }

    public static int getSelectedItem(ItemStack p_40885_) {
        CompoundTag compoundtag = p_40885_.getOrCreateTag();
        if (compoundtag.contains("ItemSelect")) {
            return compoundtag.getInt("ItemSelect");
        }

        return 0;
    }

    public static void setSelectedItem(int select, ItemStack p_40885_) {
        CompoundTag compoundtag = p_40885_.getOrCreateTag();
        compoundtag.putInt("ItemSelect", select);
    }


    public static boolean cycle(ItemStack stack) {
        return cycle(1, stack);
    }

    public static boolean cycle(boolean clockWise, ItemStack stack) {
        return cycle(clockWise ? 1 : -1, stack);
    }

    public static boolean cycle(int slotsMoved, ItemStack stack) {
        int originalSlot = getSelectedItem(stack);
        var content = getContents(stack).toList();
        CompoundTag selected;
        if (slotsMoved == 0) {
            if (content.isEmpty()) return false;
        }
        int maxSlots = content.size();
        if (maxSlots <= 0) {
            return false;
        }
        slotsMoved = slotsMoved % maxSlots;
        setSelectedItem((maxSlots + (getSelectedItem(stack) + slotsMoved)) % maxSlots, stack);
        for (int i = 0; i < maxSlots; i++) {
            setSelectedItem((maxSlots + (getSelectedItem(stack) + (slotsMoved >= 0 ? 1 : -1))) % maxSlots, stack);
        }
        return originalSlot != getSelectedItem(stack);
    }
}
