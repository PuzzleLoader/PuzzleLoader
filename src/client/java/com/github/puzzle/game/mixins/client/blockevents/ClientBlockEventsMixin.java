package com.github.puzzle.game.mixins.client.blockevents;

import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.meta.EnvType;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import finalforeach.cosmicreach.ClientBlockEvents;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.blockevents.BlockEventArgs;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientBlockEvents.class)
public class ClientBlockEventsMixin {

    @ModifyExpressionValue(method = "breakBlock", at=@At(value = "NEW", target = "()Lfinalforeach/cosmicreach/blockevents/BlockEventArgs;"))
    private BlockEventArgs breakBlockHelper(BlockEventArgs original){
        if (Piece.getSide() != EnvType.SERVER) {
            original.setSrcIdentity(new NetworkIdentity(null) {
                @Override
                public void send(GamePacket gamePacket) {
                }

                @Override
                public Player getPlayer() {
                    return GameSingletons.clientSingletons.getLocalPlayer();
                }
            });
        }
        return original;
    }
    @ModifyExpressionValue(method = "placeBlock", at=@At(value = "NEW", target = "()Lfinalforeach/cosmicreach/blockevents/BlockEventArgs;"))
    private BlockEventArgs placeBlockHelper(BlockEventArgs original){
        if (Piece.getSide() != EnvType.SERVER) {
            original.setSrcIdentity(new NetworkIdentity(null) {
                @Override
                public void send(GamePacket gamePacket) {
                }

                @Override
                public Player getPlayer() {
                    return GameSingletons.clientSingletons.getLocalPlayer();
                }
            });
        }
        return original;
    }
    @ModifyExpressionValue(method = "interactWithBlock", at=@At(value = "NEW", target = "()Lfinalforeach/cosmicreach/blockevents/BlockEventArgs;"))
    private BlockEventArgs interactWithHelper(BlockEventArgs original){
        if (Piece.getSide() != EnvType.SERVER) {
            original.setSrcIdentity(new NetworkIdentity(null) {
                @Override
                public void send(GamePacket gamePacket) {
                }

                @Override
                public Player getPlayer() {
                    return GameSingletons.clientSingletons.getLocalPlayer();
                }
            });
        }
        return original;
    }
}
