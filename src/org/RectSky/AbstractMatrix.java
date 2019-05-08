package org.RectSky;

import org.RectSky.Exceptions.MatrixFullException;
import org.RectSky.Exceptions.MatrixNotSquareException;

import java.lang.reflect.Array;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractMatrix<T> {
    private int rowSize;
    private int columnSize;
    private Object[][] matrix;

    private Index index = Index.builder(0,0);

    private int occupiedElements = 0 ;

    public AbstractMatrix(int rowSize, int columnSize){
        this.columnSize = columnSize;
        this.rowSize = rowSize;
        matrix = new Object[rowSize][columnSize];
    }


    public static NumberMatrix squareMatrix(int len, Number... elements){
        NumberMatrix numberMatrix = new NumberMatrix(len,len);
        for (Number n : elements) numberMatrix.add(n);
        return numberMatrix;
    }

    public static NumberMatrix TWO_TWO(Number n1, Number n2, Number n3, Number n4){
        return squareMatrix(2,n1,n2,n3,n4);
    }

    public static NumberMatrix THREE_THREE(Number n1, Number n2, Number n3, Number n4,Number n5, Number n6, Number n7, Number n8,Number n9){
        return squareMatrix(2,n1,n2,n3,n4,n5,n6,n7,n8,n9);
    }

    public final AbstractMatrix<T> add(T value) throws MatrixFullException,NullPointerException{
        if (value == null) return this;
        if (isMatrixFull()) throw new MatrixFullException();

//        outer: for (int row = 0; row < getRowSize(); row++) for (int column = 0; column < getColumnSize(); column++) {
//            if (matrix[row][column] == null) {
//                matrix[row][column] = value;
//                // add to size
//                occupiedElements++;
//                break outer;
//            }
//        }

        matrix[index.row()][index.column()] = value;
        incrementIndex();


        return this;
    }

    private void incrementIndex(){
        if ((index.column() + 1) == getColumnSize()){
            index.zeroColumn();
            index.incrementRow();
        }else index.incrementColumn();
    }

    public final AbstractMatrix<T> add(T value, int row, int column) throws NullPointerException,IllegalArgumentException{
        if (value == null) return this;
        checkRow(row);
        checkColumn(column);
        if ( row < 0) throw new IllegalArgumentException("Row cannot be negative");
        if (column < 0 ) throw new IllegalArgumentException("Column cannot be negative");

        // check if Matrix[row][column] has value then it's overriding and don't need to ++occupiedElements
        if ( matrix[row][column] == null) occupiedElements++;
        matrix[row][column] = value;

        return this;
    }

    public abstract void print();

    public final void forEachElementConsume(Consumer<T> consumer){
        for (int row = 0; row < getRowSize(); row++)
            for (int column = 0; column < getColumnSize(); column++)
                consumer.accept(get(row,column));
    }

    public final void forEachRowConsume(Consumer<T> consumer, int row){
        checkRow(row);
        for (int column = 0; column < getRowSize(); column++)
            consumer.accept(get(row,column));
    }

    public final void forEachColumnConsume(Consumer<T> consumer, int column){
        checkColumn(column);
        for (int row = 0; row < getRowSize(); row++)
            consumer.accept(get(row,column));
    }

    public final void forEachDiagonalConsume(Consumer<T> consumer){
        checkIfMatrixSqaure();

        for (int index = 0; index < getRowSize(); index++)
            consumer.accept(get(index,index));
    }

    public final void forEachElementFunction(Function<T,T> function){
        for (int row = 0; row < getRowSize(); row++) for (int column = 0; column < getColumnSize(); column++) invokeFunction(function,row,column);
    }

    public final void forEachRowFunction(Function<T,T> function, int row){
        checkRow(row);
        for (int column = 0; column < getColumnSize(); column++) invokeFunction(function,row,column);
    }

    public final void forEachColumnFunction(Function<T,T> function, int column){
        checkColumn(column);
        for (int row = 0; row < getRowSize(); row++) invokeFunction(function,row,column);
    }

    public final void forEachDiagonalFunction(Function<T,T> function){
        checkIfMatrixSqaure();

        for (int index = 0; index < getRowSize(); index++) invokeFunction(function,index,index);
    }

    @SuppressWarnings("unchecked")
    private void invokeFunction(Function<T,T> function, int row, int column){
        Object temp = matrix[row][column];
        checkNullity(temp,row,column);
        matrix[row][column] = function.apply( (T) temp);
    }

    public final boolean forEachElementPredicate(Predicate<T> predicate) {
        return forEachElementPredicate(predicate,getRowSize(),getColumnSize());
    }

    @SuppressWarnings("unchecked")
    private boolean forEachElementPredicate(Predicate<T> predicate, int maxRow, int maxColumn){
        for (int row = 0; row < maxRow; row++) for (int column = 0; column < maxColumn; column++){
            if (!predicate.test( (T) matrix[row][column])) return false;
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public final boolean forEachRowPredicate(Predicate<T> predicate, int row){
        for (int column = 0; column < getColumnSize(); column++) if (!predicate.test((T) matrix[row][column])) return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    public final boolean forEachColumnPredicate(Predicate<T> predicate, int column){
        for (int row = 0; row < getRowSize(); row++) if (!predicate.test((T) matrix[row][column])) return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    public final boolean forEachDiagonalPredicate(Predicate<T> predicate){
        checkIfMatrixSqaure();
        for (int index = 0; index < getRowSize(); index++) if (!predicate.test((T) matrix[index][index])) return false;

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
        for (int i = 0; i < getRowSize() ; i++) temp[i] = matrix[i][column];

        return (T[]) temp;
    }

    @SuppressWarnings("unchecked")
    public final T[] getDiagonal() throws MatrixNotSquareException{
        if (!isSquare()) throw new MatrixNotSquareException();
        Object[] temp = new Object[getRowSize()];
        for (int i = 0; i < getRowSize(); i++) temp[i] = matrix[i][i];
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

    final void checkIfMatrixSqaure(){
        if (!isSquare()) throw new MatrixNotSquareException();
    }

    public final boolean isSquare(){
        return getRowSize() == getColumnSize();
    }

    public int occupiedElementsSize(){
        return occupiedElements;
    }

    public int notOccupiedElementsSize(){
        return size() - occupiedElements;
    }

    public int size(){
        return getRowSize()* getColumnSize();
    }

    public boolean isMatrixFull(){
        return size() == occupiedElementsSize();
    }

}
