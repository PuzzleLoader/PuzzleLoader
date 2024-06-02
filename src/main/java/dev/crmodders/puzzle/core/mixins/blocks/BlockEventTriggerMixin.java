package dev.crmodders.puzzle.core.mixins.blocks;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.crmodders.flux.FluxRegistries;
import dev.crmodders.flux.block.FluxBlockAction;
import dev.crmodders.flux.factories.IFactory;
import dev.crmodders.flux.registries.AccessableRegistry;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blockevents.BlockEventTrigger;
import finalforeach.cosmicreach.blockevents.BlockEvents;
import finalforeach.cosmicreach.blockevents.actions.IBlockAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockEventTrigger.class)
public abstract class BlockEventTriggerMixin {

    @Shadow
    private IBlockAction action;

    /**
     * @author nanobass
     * @reason adds flux block event actions
     */
    @Overwrite
    public void read(Json json, JsonValue jsonData) {
        Identifier actionId = Identifier.fromString(json.readValue(String.class, jsonData.get("actionId")));
        AccessableRegistry<IFactory<FluxBlockAction>> actions = FluxRegistries.BLOCK_EVENT_ACTION_FACTORIES.access();
        if(actions.contains(actionId)) {
            FluxBlockAction action = actions.get(actionId).generate();
            action.read(json, jsonData);
            this.action = action;
        } else {
            Class<? extends IBlockAction> actionClass = BlockEvents.ALL_ACTIONS.get(actionId.toString());
            if (actionClass == null) {
                throw new RuntimeException("Could not find action for id: " + actionId);
            } else {
                this.action = json.fromJson(actionClass, jsonData.toString());
            }
        }
    }

}