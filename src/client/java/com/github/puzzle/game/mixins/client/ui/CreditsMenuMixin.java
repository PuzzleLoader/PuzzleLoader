package com.github.puzzle.game.mixins.client.ui;

import com.github.puzzle.game.ui.credits.PuzzleCreditsMenu;
import com.github.puzzle.game.ui.credits.categories.ListCredit;
import finalforeach.cosmicreach.gamestates.CreditsMenu;
import finalforeach.cosmicreach.lang.Lang;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(CreditsMenu.class)
public class CreditsMenuMixin {

    @Inject(method = "addCredit", at = @At("HEAD"))
    private void addCredit(String categoryLangKey, String[] names, CallbackInfo ci) {
        if (!Objects.equals(categoryLangKey, "creditsLocalization")) {
            ListCredit credit = new ListCredit(puzzleLoader$fixCase(Lang.get(categoryLangKey).replace(":", "")));
            credit.addNames(names);
            PuzzleCreditsMenu.addCategory(credit);
        }
    }

    @Unique
    private String puzzleLoader$fixCase(String str) {
        StringBuilder builder = new StringBuilder();

        String[] words = str.split(" ");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            String withoutFirst = word.replaceFirst("[^℃]", "");
            String firstChar = word.replace(withoutFirst, "");
            builder.append(firstChar.toUpperCase());
            builder.append(withoutFirst.toLowerCase());

            if (i != words.length - 1) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }

}
