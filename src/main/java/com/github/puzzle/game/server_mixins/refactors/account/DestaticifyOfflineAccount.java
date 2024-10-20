package com.github.puzzle.game.server_mixins.refactors.account;

import com.github.puzzle.game.util.AccountUtil;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.accounts.AccountOffline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AccountOffline.class)
public class DestaticifyOfflineAccount extends Account {

//    AccountUtil util;
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
//        fakeItchAccount.profile.username = "test";
//        fakeItchAccount.profile.display_name = "test";
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
