package io.github.puzzle.cosmic.impl.mixin.item;

import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.util.Identifier;
import io.github.puzzle.cosmic.api.item.IPuzzleItem;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Item.class)
public interface ItemMixin extends IPuzzleItem {

    @Shadow String getID();

    @Override
    default IPuzzleIdentifier _getIdentifier() {
        return IPuzzleIdentifier.as(Identifier.of(getID()));
    }

}
