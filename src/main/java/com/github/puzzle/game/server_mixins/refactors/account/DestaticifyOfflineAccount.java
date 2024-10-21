package com.github.puzzle.game.server_mixins.refactors.account;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.puzzle.game.engine.account.FakeItchAccount;
import com.github.puzzle.game.util.AccountUtil;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.accounts.AccountOffline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AccountOffline.class)
public class DestaticifyOfflineAccount extends Account {

//    FakeItchAccount fakeItchAccount;

    @Unique
    AccountUtil puzzleLoader$self = AccountUtil.get(this);

    @Override
    public String getPrefix() {
        return "offline";
    }

    @Override
    public String getDisplayName() {
        return puzzleLoader$self.getRawUserName();
    }

//    @Inject(method = "<init>", at = @At("TAIL"))
//    private void init(CallbackInfo ci) {
//        fakeItchAccount = new FakeItchAccount();
//
//        fakeItchAccount.profile.username = "puzzle_user_#" + MathUtils.random(Long.MAX_VALUE);
//        fakeItchAccount.profile.display_name = "{ Puzzle } Human";
//        fakeItchAccount.profile.cover_url = "";
//        fakeItchAccount.profile.url = "";
//        fakeItchAccount.profile.id = MathUtils.random(Long.MAX_VALUE);
//
//        fakeItchAccount.setUID(fakeItchAccount.profile.id);
//    }
//
//    @Override
//    public void write(Json json) {
//        super.write(json);
//
//        fakeItchAccount.write(json);
//    }
//
//    @Override
//    public void read(Json json, JsonValue jsonData) {
//        super.read(json, jsonData);
//
//        fakeItchAccount.read(json, jsonData);
//    }
//
//    @Override
//    public String getPrefix() {
//        return "itch";
//    }
//
//    @Override
//    public String getDisplayName() {
//        return fakeItchAccount.getDisplayName();
//    }
//
//    @Override
//    public String getUniqueId() {
//        return fakeItchAccount.getUniqueId();
//    }
}
