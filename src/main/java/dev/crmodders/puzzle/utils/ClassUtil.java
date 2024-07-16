package dev.crmodders.puzzle.utils;

import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class ClassUtil {

    public static <T> T newInstance(Class<T> type) {
        try {
            return ClassReflection.newInstance(type);
        } catch (Exception var7) {
            Exception ex = var7;

            try {
                Constructor constructor = ClassReflection.getDeclaredConstructor(type, new Class[0]);
                constructor.setAccessible(true);
                return (T) constructor.newInstance(new Object[0]);
            } catch (SecurityException ignored) {
            } catch (ReflectionException var5) {
                if (type.isArray()) {
                    throw new SerializationException("Encountered object when expected array of type: " + type.getName(), ex);
                }

                if (ClassReflection.isMemberClass(type) && !ClassReflection.isStaticClass(type)) {
                    throw new SerializationException("Class cannot be created (non-static member class): " + type.getName(), ex);
                }

                throw new SerializationException("Class cannot be created (missing no-arg constructor): " + type.getName(), ex);
            } catch (Exception exception) {
                ex = exception;
            }

            throw new SerializationException("Error constructing instance of class: " + type.getName(), ex);
        }
    }

}
