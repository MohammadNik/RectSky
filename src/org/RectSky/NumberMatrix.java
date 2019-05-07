package org.RectSky;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NumberMatrix extends AbstractMatrix<Number> {

    public NumberMatrix(int rowSize, int columnSize) {
        super(rowSize, columnSize);
    }

    @Override
    public void print() {
        Arrays.stream(get(Number.class)).forEach(numbers -> {
            String builder = "[" + Stream.of(numbers).map(number -> number == null ? "Null" : number.toString()).collect(Collectors.joining(",")) + "]";
            System.out.println(builder);
        });
    }


}
