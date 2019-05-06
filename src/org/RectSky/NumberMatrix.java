package org.RectSky;

public class NumberMatrix extends AbstractMatrix<Number> {

    public NumberMatrix(int rowSize, int columnSize) {
        super(rowSize, columnSize);
    }

    @Override
    public NumberMatrix add(Number value) {
        return this;
    }

    @Override
    public NumberMatrix add(Number number, int row, int column) {
        return this;
    }

    @Override
    public void print() {

    }


}
