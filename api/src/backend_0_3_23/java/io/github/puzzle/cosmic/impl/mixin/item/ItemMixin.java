package io.github.puzzle.cosmic.impl.mixin.item;

import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.util.Identifier;
import io.github.puzzle.cosmic.api.item.IPuzzleItem;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Item.class)
public abstract class ItemMixin implements IPuzzleItem {

    @Shadow public abstract String getID();

    @Override
    public IPuzzleIdentifier _getIdentifier() {
        return IPuzzleIdentifier.as(Identifier.of(getID()));
    }

}
