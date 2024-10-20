package com.github.puzzle.game.engine.account;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.puzzle.game.util.AccountUtil;
import finalforeach.cosmicreach.accounts.Account;

import java.time.Instant;

public class FakeItchAccount extends Account {
    public FakeItchProfileMe profile;
    public long expiresAtEpochSecond = Long.MAX_VALUE;

    public FakeItchAccount() {
        profile = new FakeItchProfileMe();
    }

    public void setUID(Long uid) {
        setUniqueId(uid);
    }

    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        json.readField(this, "profile", jsonData);
        json.readField(this, "expiresAtEpochSecond", jsonData);
    }

    public void write(Json json) {
        super.write(json);

        json.writeValue("profile", profile);
        json.writeValue("expiresAtEpochSecond", expiresAtEpochSecond);
//        json.writeField(json, "profile");
//        json.writeField(json, "expiresAtEpochSecond");
    }

    public FakeItchAccount(AccountUtil util) {
        profile = new FakeItchProfileMe();

        profile.username = util.getRawUserName();
        profile.display_name = util.getRawUserName();
        profile.cover_url = "";
        profile.url = "";
        profile.id = MathUtils.random(Long.MAX_VALUE);

        this.setUsername(util.getRawUserName());
        this.setUniqueId(util.getRawID() == null ? MathUtils.random(Long.MAX_VALUE) : util.getRawID());
    }

    public void setExpiresAt(String expiresAt) {
        if (expiresAt == null) {
            this.expiresAtEpochSecond = Long.MAX_VALUE;
        } else {
            this.expiresAtEpochSecond = Instant.parse(expiresAt).getEpochSecond();
        }
    }

    public String getPrefix() {
        return "itch";
    }

    public String getDisplayName() {
        return this.profile.display_name;
    }
}
