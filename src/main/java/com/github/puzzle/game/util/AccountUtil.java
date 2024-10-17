package com.github.puzzle.game.util;

import finalforeach.cosmicreach.accounts.Account;

public interface AccountUtil {

    static AccountUtil get(Account account) {
        return (AccountUtil) account;
    }

    String getRawUserName();
    String getRawID();

}
