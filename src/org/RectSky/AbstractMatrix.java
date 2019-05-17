package org.RectSky;

import org.RectSky.Exceptions.MatrixFullException;
import org.RectSky.Exceptions.MatrixNotSquareException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @param <T> T can be Number String or anything that need to be processed in a matrix format
 */
public abstract class AbstractMatrix<T> {
    private int rowSize;
    private int columnSize;
    private Object[][] matrix;

    private Index index = Index.builder(0,0);

    private int occupiedElements = 0;

    /**
     * AbstractMatrix
     * @param rowSize number of matrix rows
     * @param columnSize number of matrix columns
     *
     * */
    public AbstractMatrix(int rowSize, int columnSize){
        this.columnSize = columnSize;
        this.rowSize = rowSize;
        matrix = new Object[rowSize][columnSize];
    }


    /**
     * @param len is Size of both row and column
     * @param elements are elements of a sqaure Matrix with size of len*len
     * @return lenXlen matrix of numbers
     */
    public static NumberMatrix squareMatrix(int len, Number... elements){
        NumberMatrix numberMatrix = new NumberMatrix(len,len);
        for (Number n : elements) numberMatrix.add(n);
        return numberMatrix;
    }

    /**
     * get four number and create a square 2x2 matrix
     * @return 2X2 Matrix of numbers
     */
    public static NumberMatrix TWO_TWO(Number n1, Number n2, Number n3, Number n4){
        return squareMatrix(2,n1,n2,n3,n4);
    }

    /**
     *   get nine number and create a sqaure 3x3 matrix
     * @return 3x3 matrix of numbers
     */
    public static NumberMatrix THREE_THREE(Number n1, Number n2, Number n3, Number n4,Number n5, Number n6, Number n7, Number n8,Number n9){
        return squareMatrix(2,n1,n2,n3,n4,n5,n6,n7,n8,n9);
    }

    /**
     * @return current matrix for chain adding
     * @throws MatrixFullException throw while matrix is full and trying to add new value
     * @throws NullPointerException throw if {T value} is null
     */
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

    /**
     *  move Index to next Null element in matrix
     */
    private void incrementIndex(){
        if ((index.column() + 1) == getColumnSize()){
            index.zeroColumn();
            index.incrementRow();
        }else index.incrementColumn();
    }


    /**
     *  same as {@link #add(Object)} in addition, this method get a row and column for adding or setting value to a specific index
     * @return current matrix for chain adding
     * @throws NullPointerException throw while matrix is full and trying to add new value
     * @throws IllegalArgumentException throw if {row || column} are ineligible -> negative or higher than dimen of matrix {row || column}
     */
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


    /**
     * structure for implementing printing matrix
     */
    public final void print(){
        Arrays.stream(get(Number.class)).forEach(objs -> {
            String builder = "[" + Stream.of(objs).map(obj -> obj == null ? "Null" : obj.toString()).collect(Collectors.joining(",")) + "]";
            System.out.println(builder);
        });
    }

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

    /**
     *  inner method for deleting duplicate code in forEachFunction methods
     */
    @SuppressWarnings("unchecked")
    private void invokeFunction(Function<T,T> function, int row, int column){
        Object temp = matrix[row][column];
        checkNullity(temp,row,column);
        matrix[row][column] = function.apply( (T) temp);
    }

    public final boolean forEachElementPredicate(MatrixPredicate<T> predicate) {
        return forEachElementPredicate(predicate,matrix);
    }

    /**
     * inner method for implementing forEachPredicate
     */
    @SuppressWarnings("unchecked")
    public final boolean forEachElementPredicate(MatrixPredicate<T> predicate,Object[][] matrix){
        int matrixRowSize = matrix.length;
        for (int row = 0; row < matrixRowSize; row++) for (int column = 0; column < matrix[row].length; column++){
            if (!predicate.test( (T) matrix[row][column],row,column)) return false;
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public final boolean forEachRowPredicate(MatrixPredicate<T> predicate, int row){
        for (int column = 0; column < getColumnSize(); column++) if (!predicate.test((T) matrix[row][column],row,column)) return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    public final boolean forEachColumnPredicate(MatrixPredicate<T> predicate, int column){
        for (int row = 0; row < getRowSize(); row++) if (!predicate.test((T) matrix[row][column],row,column)) return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    public final boolean forEachDiagonalPredicate(MatrixPredicate<T> predicate){
        checkIfMatrixSqaure();
        for (int index = 0; index < getRowSize(); index++) if (!predicate.test((T) matrix[index][index],index,index)) return false;

        return true;
    }

    /**
     * @param cls get class for creating a newInstance of 2D array
     * @return matrix of elements
     */
    @SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
    public final T[][] get(Class cls){
        T[][] array = (T[][]) Array.newInstance(cls,getRowSize(),getColumnSize());
        for (int i = 0; i < getRowSize(); i++) System.arraycopy(matrix[i],0,array[i],0,matrix[i].length);
        return array;
    }

    /**
     * @return specific element in the matrix
     */
    @SuppressWarnings("unchecked")
    public final T get(int row, int column){
        checkRow(row);
        checkColumn(column);
        Object temp = matrix[row][column];
        return (T) temp;
    }

    /**
     * @return get a specific row in matrix
     * @throws IllegalArgumentException throws if row is bigger than matrix rowSize
     */
    @SuppressWarnings("unchecked")
    public final T[] getRow(int row) throws IllegalArgumentException{
       checkRow(row);
        return (T[]) matrix[row];
    }

    /**
     * @return specific column in the matrix
     * @throws IllegalArgumentException throws if column is bigger than matrix columnSize
     */
    @SuppressWarnings("unchecked")
    public final T[] getColumn(int column) throws IllegalArgumentException{
        checkColumn(column);
        Object[] temp = new Object[getRowSize()];
        for (int i = 0; i < getRowSize() ; i++) temp[i] = matrix[i][column];

        return (T[]) temp;
    }

    /**
     * @return elements on the matrix diagonal
     * @throws MatrixNotSquareException throw if matrix is not square
     */
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

    /**
     * check if given row is valid or not
     */
     final void checkRow(int row){
        if (row >= getRowSize()) throw new IllegalArgumentException("Entered Row is bigger than RowSize!");
    }

    /**
     * check if given column is valid or not
     */
     final void checkColumn(int column){
        if(column >= getColumnSize()) throw new IllegalArgumentException("Entered Column is bigger than ColumnSize!");
    }

    /**
     * check if given element is null and if so throw a {@link NullPointerException}
     */
     final void checkNullity(Object object,int row,int column){
        if (object == null) throw new NullPointerException(String.format("Matrix[%d][%d] is null!",row,column));
    }

    /**
     * throw a {@link MatrixNotSquareException} if matrix is not square
     */
    final void checkIfMatrixSqaure(){
        if (!isSquare()) throw new MatrixNotSquareException();
    }

    public final boolean isSquare(){
        return getRowSize() == getColumnSize();
    }

    /**
     * @return number of nonNull elements in matrix
     */
    public int occupiedElementsSize(){
        return occupiedElements;
    }

    /**
     * @return number of Null elements in matrix
     */
    public int notOccupiedElementsSize(){
        return size() - occupiedElements;
    }

    public int size(){
        return getRowSize()* getColumnSize();
    }

    public boolean isMatrixFull(){
        return size() == occupiedElementsSize();
    }

    public final boolean isEqualSize(AbstractMatrix<T> abstractMatrix){
        return (getRowSize() == abstractMatrix.getRowSize()) && ( getColumnSize() == abstractMatrix.getColumnSize());
    }
}
