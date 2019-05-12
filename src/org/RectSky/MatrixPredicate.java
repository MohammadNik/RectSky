package org.RectSky;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface MatrixPredicate<T> {

    boolean test(T t,int row,int column);

    static <T> Predicate<T> isEqual(Object targetRef) {
        return (null == targetRef)
                ? Objects::isNull
                : targetRef::equals;
    }
}
