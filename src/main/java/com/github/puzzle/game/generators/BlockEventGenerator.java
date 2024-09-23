package com.github.puzzle.game.generators;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.puzzle.game.engine.blocks.BlockLoader;
import com.github.puzzle.game.factories.IGenerator;
import finalforeach.cosmicreach.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockEventGenerator implements IGenerator {

    public static String getEventName(Identifier blockId, String eventName) {
        return blockId.toString() + "_" + eventName + "_events";
    }

    public static class Trigger implements Json.Serializable {
        public Identifier actionId;
        public Map<String, Object> parameters;

        @Override
        public void write(Json json) {
            json.writeValue("actionId", actionId.toString());
            json.writeObjectStart("parameters");
            for(String key : parameters.keySet()) {
                json.writeValue(key, parameters.get(key));
            }
            json.writeObjectEnd();
        }

        @Override
        public void read(Json json, JsonValue jsonValue) {}
    }

    public Identifier blockId;
    public String eventName;
    public Map<String, List<Trigger>> triggers = new HashMap<>();

    public BlockEventGenerator(Identifier blockId, String eventName) {
        this.blockId = blockId;
        this.eventName = eventName;
    }

    public String getEventName() {
        return getEventName(blockId, eventName);
    }


    public Trigger createTrigger(String triggerName, Identifier actionId, Map<String, Object> parameters) {
        Trigger trigger = new Trigger();
        trigger.actionId = actionId;
        trigger.parameters = parameters;
        if(triggers.containsKey(triggerName)) {
            triggers.get(triggerName).add(trigger);
        } else {
            List<Trigger> triggerMap = new ArrayList<>();
            triggerMap.add(trigger);
            triggers.put(triggerName, triggerMap);
        }
        return trigger;
    }

    @Override
    public void register(BlockLoader loader) {}

    @Override
    public String generateJson() {
        Json json = new Json();
        json.setTypeName(null);
        OrderedMap<String, Trigger[]> triggers = new OrderedMap<>();
        for(String triggerName : this.triggers.keySet()) {
            Trigger[] triggerMap = this.triggers.get(triggerName).toArray(Trigger[]::new);
            triggers.put(triggerName, triggerMap);
        }
        String eventName = getEventName();
        String triggerJson = json.toJson(triggers);
        return """
               {"parent":"base:block_events_default","stringId":"%s","triggers":%s}
               """.formatted(eventName, triggerJson);
    }
}
