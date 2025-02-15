package com.github.puzzle.core.loader.meta;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Repeatable(SidedImpls.class)
@Target(ElementType.TYPE)
public @interface SidedImpl {

    EnvType value();
    Class<?> itf();

}
