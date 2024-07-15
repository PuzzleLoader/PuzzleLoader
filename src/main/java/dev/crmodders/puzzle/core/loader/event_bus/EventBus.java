package dev.crmodders.puzzle.core.loader.event_bus;

import dev.crmodders.flux.tags.Identifier;
import dev.crmodders.puzzle.core.loader.event_bus.annotation.EVSub;
import dev.crmodders.puzzle.core.loader.registries.BasicDynamicRegistry;
import dev.crmodders.puzzle.core.loader.registries.IRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class EventBus {

    Identifier EventBusID;
    private static final Logger logger = LoggerFactory.getLogger("Puzzle | Eventbuses");

    public static Map<Identifier, EventBus> eventBusMap = new HashMap<>();
    public static IRegistry.IDynamic<EventBus> eventBusRegistry = new BasicDynamicRegistry<>(Identifier.fromString("puzzle:event_bus_registry"));

    public EventBus(Identifier id) {
        EventBusID = id;
        eventBusRegistry.store(EventBusID, this);
//            eventBusMap.put(EventBusID, this);
    }

    List<Object> subscriber = new ArrayList<>();

    public void register(Object object) {
        subscriber.add(object);
    }

    public void unregister(Object object) {
        subscriber.remove(object);
    }

    public void postEvent(Event event) {
        subscriber.forEach((s) -> {
            Arrays.stream(s.getClass().getMethods()).forEach(method -> {
                if (method.getAnnotation(EVSub.class) != null) {
                    if (method.getParameterTypes()[0].getName().equals(event.getClass().getName())) {
                        event.call(s, method, event);
                    }
                }
            });
        });
    }

}
