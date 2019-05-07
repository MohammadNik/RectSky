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
            StringBuilder builder = new StringBuilder();
            builder.append("[")
                    .append(Stream.of(numbers).map(Number::toString).collect(Collectors.joining(",")))
                    .append("]");
            System.out.println(builder.toString());
        });
    }


}
