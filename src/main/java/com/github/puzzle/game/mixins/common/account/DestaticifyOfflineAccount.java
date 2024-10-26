package com.github.puzzle.game.mixins.common.account;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.puzzle.core.Constants;
import com.github.puzzle.game.engine.account.FakeItchAccount;
import com.github.puzzle.game.util.AccountUtil;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.accounts.AccountOffline;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(AccountOffline.class)
public class DestaticifyOfflineAccount extends Account {

    FakeItchAccount fakeItchAccount;

    @Unique
    private static long GOD_ACCOUNT_ID = 8008135;
    private static boolean GOD_HAS_GENERATED = !Constants.getVersion().equals("69.69.69");

    boolean isFakeItch;

    @Override
    public boolean canSave() {
        return true;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if (!GOD_HAS_GENERATED) {
            AtomicReference<String> name = new AtomicReference<>("God Himself");
            JFrame frame = new JFrame("Set Name");
            JPanel panel = new JPanel();
            TextField field = new TextField(32);
            panel.add(field);
            Button submit = new Button("Submit");
            GOD_ACCOUNT_ID = RandomUtils.nextInt(1, 8008135);
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (Objects.equals(e.getActionCommand(), "Submit")) {
                        fakeItchAccount.profile.display_name = "《 Dev 》 " + field.getText();
                        frame.setVisible(false);
                    }
                }
            });
            panel.add(submit);
            frame.add(panel);
            frame.setSize(300, 100);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

            isFakeItch = true;
            fakeItchAccount = new FakeItchAccount();

            fakeItchAccount.profile.username = "puzzle_user_#" + MathUtils.random(Long.MAX_VALUE);
//            fakeItchAccount.profile.display_name = "MЯ ZФMБЇЇ"; //"《 Puzzle 》"
//            fakeItchAccount.profile.display_name = "Adolph Blaine Charles David Earl Frederick Gerald Hubert Irvin John Kenneth Lloyd Martin Nero Oliver Paul Quincy Randolph Sherman Thomas Uncas Victor William Xerxes Yancy Zeus Wolfeschlegelsteinhausenbergerdorffwelchevoralternwarengewissenhaftschaferswessenschafewarenwohlgepflegeundsorgfaltigkeitbeschutzenvonangreifendurchihrraubgierigfeindewelchevoralternzwolftausendjahresvorandieerscheinenvanderersteerdemenschderraumschiffgebrauchlichtalsseinursprungvonkraftgestartseinlangefahrthinzwischensternartigraumaufdersuchenachdiesternwelchegehabtbewohnbarplanetenkreisedrehensichundwohinderneurassevonverstandigmenschlichkeitkonntefortpflanzenundsicherfreuenanlebenslanglichfreudeundruhemitnichteinfurchtvorangreifenvonandererintelligentgeschopfsvonhinzwischensternartigraum\n";
            fakeItchAccount.profile.display_name = name.get();
            fakeItchAccount.profile.cover_url = "https://play-lh.googleusercontent.com/EicDCzuN6l-9g4sZ6uq0fkpB-1AcVzd6HeZ6urH3KIGgjw-wXrrtpUZapjPV2wgi5R4=w240-h480-rw";
            fakeItchAccount.profile.url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
            fakeItchAccount.profile.id = GOD_ACCOUNT_ID;

            fakeItchAccount.setUID(fakeItchAccount.profile.id);
            GOD_HAS_GENERATED = true;
        }
    }

    @Override
    public void write(Json json) {
        json.writeValue("identity", Constants.MOD_ID);
        if (isFakeItch) {
            fakeItchAccount.write(json);
            json.writeValue("isFakeItch", isFakeItch);
        } else {
            super.write(json);
        }
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);

        if (jsonData.has("isFakeItch")) {
            isFakeItch = true;
            fakeItchAccount = new FakeItchAccount();
            fakeItchAccount.read(json, jsonData);
            setUniqueId(fakeItchAccount.profile.id);
            setUsername(fakeItchAccount.profile.username);
        } else {
            this.setUsername("localPlayer");
            this.setUniqueId(MathUtils.random(Long.MAX_VALUE));
        }
    }

    @Override
    public String getPrefix() {
        return isFakeItch ? "itch" : "offline";
    }

    @Override
    public String getDisplayName() {
        return isFakeItch ? fakeItchAccount.getDisplayName() : "Player";
    }

    @Override
    public String getUniqueId() {
        return isFakeItch ? fakeItchAccount.getUniqueId() : super.getUniqueId();
    }
}