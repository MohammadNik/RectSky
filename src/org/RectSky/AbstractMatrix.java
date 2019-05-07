package org.RectSky;

import org.RectSky.Exceptions.MatrixFullException;
import org.RectSky.Exceptions.MatrixNotSquareException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractMatrix<T> {
    private int rowSize;
    private int columnSize;
    private Object[][] matrix;

    public AbstractMatrix(int rowSize, int columnSize){
        this.columnSize = columnSize;
        this.rowSize = rowSize;
        matrix = new Object[rowSize][columnSize];
    }

    public final AbstractMatrix<T> add(T value){
        checkNullity(value);
        if (isMatrixFull()) throw new MatrixFullException();

        for (int row = 0; row < getRowSize(); row++) for (int column = 0; column < getColumnSize(); column++) {
            if (matrix[row][column] == null) {
                matrix[row][column] = value;
                break;
            }
        }

        return this;
    }

    public final AbstractMatrix<T> add(T value, int row, int column){
        checkNullity(value);
        if (isMatrixFull()) throw new MatrixFullException();
        checkRow(row);
        checkColumn(column);
        if ( row < 0) throw new IllegalArgumentException("Row cannot be negative");
        if (column < 0 ) throw new IllegalArgumentException("Column cannot be negative");

        matrix[row][column] = value;

        return this;
    }

    public abstract void print();

    public final void forEachElement(Consumer<Object> consumer){
        for (int row = 0; row < getRowSize(); row++)
            for (int column = 0; column < getColumnSize(); column++)
                consumer.accept(get(row,column));
    }

    public final void forEachRow(Consumer<Object> consumer, int row){
        checkRow(row);
        for (int column = 0; column < getRowSize(); column++)
            consumer.accept(get(row,column));
    }

    public final void forEachColumn(Consumer<Object> consumer, int column){
        checkColumn(column);
        for (int row = 0; row < getRowSize(); row++)
            consumer.accept(get(row,column));
    }

    public final void forEachDiagonal(Consumer<Object> consumer){
        if (!isSquare())
            throw new MatrixNotSquareException();

        for (int index = 0; index < getRowSize(); index++)
            consumer.accept(get(index,index));
    }

    public final void forEachElementFunction(Function<Object,Object> function){
        for (int row = 0; row < getRowSize(); row++) for (int column = 0; column < getColumnSize(); column++) invokeFunction(function,row,column);
    }

    public final void forEachRowFunction(Function<Object,Object> function,int row){
        checkRow(row);
        for (int column = 0; column < getColumnSize(); column++) invokeFunction(function,row,column);
    }

    public final void forEachColumnFunction(Function<Object,Object> function, int column){
        checkColumn(column);
        for (int row = 0; row < getRowSize(); row++) invokeFunction(function,row,column);
    }

    public final void forEachDiagonalFunction(Function<Object,Object> function){
        if (!isSquare())
            throw new MatrixNotSquareException();

        for (int index = 0; index < getRowSize(); index++) invokeFunction(function,index,index);
    }

    private void invokeFunction(Function<Object,Object> function, int row, int column){
        Object temp = matrix[row][column];
        checkNullity(temp,row,column);
        matrix[row][column] = function.apply(temp);
    }

    public final boolean forEachElementPredicate(Predicate<Index> predicate) {
        return forEachElementPredicate(predicate,getRowSize(),getColumnSize());
    }

    private boolean forEachElementPredicate(Predicate<Index> predicate, int maxRow, int maxColumn){
        for (int row = 0; row < maxRow; row++) for (int column = 0; column < maxColumn; column++){
            if (!predicate.test(Index.builder(row,column))) return false;
        }

        return true;
    }

    public final boolean forEachRowPredicate(Predicate<Index> predicate, int row){
        for (int column = 0; column < getColumnSize(); column++) if (!predicate.test(Index.builder(row,column))) return false;
        return true;
    }

    public final boolean forEachColumnPredicate(Predicate<Index> predicate, int column){
        for (int row = 0; row < getRowSize(); row++) if (!predicate.test(Index.builder(row,column))) return false;
        return true;
    }

    public final boolean forEachDiagonalPredicate(Predicate<Index> predicate){
        if (!isSquare()) throw new MatrixNotSquareException();

        for (int index = 0; index < getRowSize(); index++) if (!predicate.test(Index.builder(index,index))) return false;

        return true;
    }

    @SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
    public final T[][] get(Class cls){
        T[][] array = (T[][]) Array.newInstance(cls,getRowSize(),getColumnSize());
        for (int i = 0; i < getRowSize(); i++) System.arraycopy(matrix[i],0,array[i],0,matrix[i].length);
        return array;
    }

    @SuppressWarnings("unchecked")
    public final T get(int row, int column) throws IllegalArgumentException{
        checkRow(row);
        checkColumn(column);
        Object temp = matrix[row][column];
        if (temp == null)
            throw new NullPointerException(String.format("matrix[%d][%d] is Null!",row,column));
        return (T) temp;
    }

    @SuppressWarnings("unchecked")
    public final T[] getRow(int row) throws IllegalArgumentException{
       checkRow(row);
        return (T[]) matrix[row];
    }

    @SuppressWarnings("unchecked")
    public final T[] getColumn(int column) throws IllegalArgumentException{
        checkColumn(column);

        Object[] temp = new Object[getRowSize()];
        for (int i = 0; i < getRowSize() ; i++) {
            temp[i] = matrix[i][column];
        }
        return (T[]) temp;
    }

      public final int getRowSize() {
        return rowSize;
    }

      public final int getColumnSize() {
        return columnSize;
    }

     final void checkRow(int row){
        if (row >= getRowSize()) throw new IllegalArgumentException("Entered Row is bigger than RowSize!");
    }

     final void checkColumn(int column){
        if(column >= getColumnSize()) throw new IllegalArgumentException("Entered Column is bigger than ColumnSize!");
    }

     final void checkNullity(Object object,int row,int column){
        if (object == null) throw new NullPointerException(String.format("Matrix[%d][%d] is null!",row,column));
    }

    final void checkNullity(Object object){
        if (object == null) throw new NullPointerException("Entered value is null!");
    }

    public final boolean isSquare(){
        return getRowSize() == getColumnSize();
    }

    public int occupiedElementsSize(){
        return (int) Stream.of(matrix).parallel().flatMap(Arrays::stream).filter(Objects::nonNull).count();
    }

    public int notOccupiedElementsSize(){
        return (int) Stream.of(matrix).parallel().flatMap(Arrays::stream).filter(Objects::isNull).count();
    }

    public int size(){
        return getRowSize()* getColumnSize();
    }

    public boolean isMatrixFull(){
        return size() == occupiedElementsSize();
    }

}
