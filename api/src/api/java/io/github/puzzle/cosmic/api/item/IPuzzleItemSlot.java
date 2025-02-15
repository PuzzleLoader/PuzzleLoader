package io.github.puzzle.cosmic.api.item;

import io.github.puzzle.cosmic.api.item.container.IPuzzleSlotContainer;
import io.github.puzzle.cosmic.util.ChangeType;

public interface IPuzzleItemSlot {

    void set(IPuzzleItemSlot src);
    void setItemStack(IPuzzleItemStack stack);

    boolean mergeFrom(IPuzzleItemSlot slot);
    boolean merge(IPuzzleItemStack stack);
    boolean mergeInto(IPuzzleItemSlot slot);

    int getSlotId();

    IPuzzleSlotContainer getContainer();
    void ISlotContainer();

    void addAmount(int amount);
    void onItemSlotUpdate();

    void setOutputOnly(boolean readOnly);
    boolean isOutputOnly();

    boolean isEmpty();

    boolean hasRoomFor(IPuzzleItem item);
    boolean hasRoomFor(IPuzzleItemStack stack);
    boolean hasRoomFor(IPuzzleItem item, int amount);

    boolean addItemStack(IPuzzleItem item, int amount);
    boolean addItemStack(IPuzzleItemStack stack);

    @ChangeType("ItemSlot")
    Object as();

}
