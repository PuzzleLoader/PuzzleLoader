package io.github.puzzle.cosmic.api.item;

import io.github.puzzle.cosmic.api.item.container.IPuzzleSlotContainer;
import io.github.puzzle.cosmic.util.ApiDeclaration;

@ApiDeclaration(api = IPuzzleItemSlot.class, impl = "ItemSlot")
public interface IPuzzleItemSlot {

    void _set(IPuzzleItemSlot src);
    void _setItemStack(IPuzzleItemStack stack);

    boolean _mergeFrom(IPuzzleItemSlot slot);
    boolean _merge(IPuzzleItemStack stack);
    boolean _mergeInto(IPuzzleItemSlot slot);

    int _getSlotId();

    IPuzzleSlotContainer _getContainer();
    void _setContainer(IPuzzleSlotContainer container);

    void _addAmount(int amount);
    void _onItemSlotUpdate();

    void _setOutputOnly(boolean readOnly);
    boolean _isOutputOnly();

    boolean _isEmpty();

    boolean _hasRoomFor(IPuzzleItem item);
    boolean _hasRoomFor(IPuzzleItemStack stack);
    boolean _hasRoomFor(IPuzzleItem item, int amount);

    boolean _addItemStack(IPuzzleItem item, int amount);
    boolean _addItemStack(IPuzzleItemStack stack);

}
