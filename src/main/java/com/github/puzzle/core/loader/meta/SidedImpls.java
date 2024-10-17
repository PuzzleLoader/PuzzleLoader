package com.github.puzzle.core.loader.meta;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface SidedImpls {

    SidedImpl[] value();

}
