package dev.crmodders.puzzle.core.eventbus;

import java.lang.reflect.Method;

public interface Event {

    void call(Object clazz, Method method, Event event);

}
