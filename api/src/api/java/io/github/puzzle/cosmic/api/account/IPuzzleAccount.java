package io.github.puzzle.cosmic.api.account;

import io.github.puzzle.cosmic.api.entity.player.IPuzzlePlayer;
import io.github.puzzle.cosmic.util.ApiDeclaration;

@ApiDeclaration(api = IPuzzleAccount.class, impl = "Account")
public interface IPuzzleAccount {

    String getDisplayName();
    String getUsername();
    String getUniqueId();

    void setUsername(String username);
    void setUniqueId(String uniqueId);

    String getPrefix();
    String getDebugString();

    boolean canSave();
    boolean isAllowed();
    boolean isOperator();

    IPuzzlePlayer getPlayer();

}
