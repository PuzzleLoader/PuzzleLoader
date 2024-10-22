package com.github.puzzle.game.account;

import com.badlogic.gdx.math.MathUtils;
import finalforeach.cosmicreach.accounts.AccountOffline;

public class OfflineAccountPZ extends AccountOffline {

    String displayName;

    public OfflineAccountPZ(String displayName, String userName) {
        setUserName(userName);
        setDisplayName(displayName);
    }

    public OfflineAccountPZ() {
        this.setUsername("localPlayer");
        this.setUniqueId(MathUtils.random(Long.MAX_VALUE));
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public void setUserName(String name) {
        setUsername(name);
    }

    public String getPrefix() {
        return "offline";
    }

    public String getDisplayName() {
        return displayName;
    }

}
