package io.github.puzzle.cosmic.util;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeType {

    String value();
    boolean removeOnNotFound() default true;

}
