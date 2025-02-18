package io.github.puzzle.cosmic.impl.mixin.util;

import finalforeach.cosmicreach.util.Identifier;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Identifier.class)
public class IdentifierMixin implements IPuzzleIdentifier {

    @Unique
    Identifier puzzleLoader$id = IPuzzleIdentifier.as(this);

    @Override
    public String _getNamespace() {
        return puzzleLoader$id.getNamespace();
    }

    @Override
    public String _getName() {
        return puzzleLoader$id.getName();
    }
}
