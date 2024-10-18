package com.github.puzzle.game.server_mixins.refactors.account;

import com.github.puzzle.game.util.AccountUtil;
import finalforeach.cosmicreach.accounts.Account;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Account.class)
public abstract class AccountUtilMixin implements AccountUtil {

    @Shadow private String username;
    @Unique
    private String puzzleLoader$rawUsername;
    @Shadow private String uniqueId;

    @Shadow public abstract String getPrefix();

    /**
     * @author Mr_Zombii
     * @reason getRawUsername
     */
    @Overwrite
    public final void setUsername(String username) {
        String var10001 = this.getPrefix();
        this.username = var10001 + ":" + username;
        this.puzzleLoader$rawUsername = username;
    }

    @Override
    public String getRawUserName() {
        return puzzleLoader$rawUsername;
    }

    @Override
    public String getRawID() {
        return uniqueId;
    }
}
