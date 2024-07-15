package dev.crmodders.puzzle.core.loader.event_bus;

import java.lang.reflect.Method;

public interface Event {

    void call(Object clazz, Method method, Event event);

}
