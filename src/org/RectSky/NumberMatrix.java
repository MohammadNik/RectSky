package org.RectSky;

import org.RectSky.Exceptions.MatricesNotSameSizeException;
import org.RectSky.Exceptions.MatrixNotInvertibleException;

import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NumberMatrix extends AbstractMatrix<Number> {

    public NumberMatrix(int rowSize, int columnSize) {
        super(rowSize, columnSize);
    }

    public void print(int precision){
        for (int row= 0 ; row < getRowSize() ; row++)
            System.out.println(Stream.of(getRow(row)).map( f-> String.format("%."+precision+"f",f.doubleValue()) ).collect(Collectors.joining(" ")));
    }

    public NumberMatrix transpose(){
        NumberMatrix tempMatrix = new NumberMatrix(getColumnSize(),getRowSize());

        for (int row = 0; row < getRowSize() ; row++) for (int column = 0; column < getColumnSize(); column++) {
            tempMatrix.add(get(row,column),column,row);
        }

        return tempMatrix;
    }

    public Number determinant(){
        return determinant(get(Number.class));
    }

    private Number determinant(Number[][] matrix){
        checkIfMatrixSqaure();

        int max = matrix.length;

        if (max == 1) return matrix[0][0];
        if (max == 2) return (matrix[0][0].doubleValue() * matrix[1][1].doubleValue()) - (matrix[1][0].doubleValue() * matrix[0][1].doubleValue());

        // Shortcuts for faster calculation of determinant
        // START:
        if (isDiagonal(matrix) || isUpperTriangular(matrix) || isLowerTriangular(matrix)){
            return diagonalMatrixDeterminant(matrix);
        }
        // END:

        Number det = 0;
        for (int column = 0 ; column < max ; column++){
            // row is zero because we calculate det on first row
            det = det.doubleValue() + (matrix[0][column].doubleValue() * cofactor(matrix,0,column).doubleValue());
        }

        return det;
    }

    private Number diagonalMatrixDeterminant(Number[][] matrix){
        Number result = 1;
        for (int row =0; row < matrix.length ; row++) for (int column = 0; column < matrix[0].length ; column++) if ( row == column){
            Number number = matrix[row][column];
            checkNullity(number,row,column);
            result = result.doubleValue() * number.doubleValue();
        }

        return result;
    }

    public NumberMatrix minor(int deletedRow, int deletedColumn){
        return minor(get(Number.class),deletedRow,deletedColumn);
    }

    private NumberMatrix minor(Number[][] matrix, int deletedRow, int deletedColumn){
        checkIfMatrixSqaure();

        int max = matrix.length;

        NumberMatrix tempMatrix = new NumberMatrix(max-1,max-1);

        for (int row = 0 ; row < max ; row++) for (int column = 0 ; column < max ; column++) {
                if ( row != deletedRow && column != deletedColumn) tempMatrix.add(matrix[row][column]);
            }

        return tempMatrix;
    }

    public NumberMatrix inverse(){
        if (isZero() || isIdentity()) return this;

        if (isSingular()) throw new MatrixNotInvertibleException();

        NumberMatrix tempMatrix = adjugate();
        tempMatrix.divideNumberToMatrix(determinant());

        return tempMatrix;
    }

    public NumberMatrix adjugate(){
        return cofactorMatrix().transpose();
    }

    public NumberMatrix cofactorMatrix(){
        NumberMatrix tempMatrix = new NumberMatrix(getRowSize(),getColumnSize());
        for (int row = 0 ; row < getRowSize() ; row++) for (int column = 0 ; column < getColumnSize() ; column++){
            tempMatrix.add(cofactor(row,column),row,column);
        }

        return tempMatrix;
    }

    public Number cofactor(int row, int column){
        return cofactor(get(Number.class),row,column);
    }

    private Number cofactor(Number[][] matrix, int row, int column){
        return ((row+column) % 2 == 0 ? 1 : -1) * determinant(minor(matrix,row,column).get(Number.class)).doubleValue();
    }

    public boolean isRowMatrix(){
        return getRowSize() == 1;
    }

    public boolean isColumnMatrix(){
        return getColumnSize() == 1;
    }


    public boolean isSymmetric(){
        if (!isSquare()) return false;

        return this.equals(transpose());
    }

    public boolean isAntiSymmetric(){
        if (!isSquare()) return false;

        return forEachElementPredicate((number,row,column) -> {
            checkNullity(number,row,column);

            // TODO: 2019-05-12 check condititon it may not working fine
            if (row != column) return number.doubleValue() == -get(column,row).doubleValue();

            return true;
        });
    }

    public boolean isSingular(){
        if (!isSquare()) return false;

        return determinant().doubleValue() == 0;
    }

    public boolean isDiagonal(){
        return isDiagonal(get(Number.class));
    }

    private boolean isDiagonal(Number[][] matrix){
        if (!isSquare(matrix)) return false;

        return forEachElementPredicate((number,row,column) -> {
            checkNullity(number,row,column);

            if (row != column) return number.doubleValue() == 0;

            return true;
        },matrix);
    }

    public boolean isIdentity(){
        return isIdentity(get(Number.class));
    }

    private boolean isIdentity(Number[][] matrix){
        if (!isSquare(matrix)) return false;

        return forEachElementPredicate((number,row,column) -> {
            checkNullity(number,row,column);

            if (row == column) return number.doubleValue() == 1;
            else return number.doubleValue() == 0;

        });
    }

    public boolean isUpperTriangular(){
        return isUpperTriangular(get(Number.class));
    }

    private boolean isUpperTriangular(Number[][] matrix){
        if (!isSquare(matrix)) return false;

        return forEachElementPredicate((number,row,column) -> {
            checkNullity(number,row,column);
            if (row > column) return number.doubleValue() == 0;

            return true;
        },matrix);
    }

    public boolean isLowerTriangular(){
        return isLowerTriangular(get(Number.class));
    }

    private boolean isLowerTriangular(Number[][] matrix){
        if (!isSquare(matrix)) return false;

        return forEachElementPredicate((number,row,column) -> {
            checkNullity(number,row,column);
            if (row < column) return number.doubleValue() == 0;

            return true;
        },matrix);
    }

    public boolean isZero(){
        return isZero(get(Number.class));
    }

    private boolean isZero(Number[][] matrix){
        return forEachElementPredicate((number, row, column) -> {
            checkNullity(number,row,column);
            // TODO: 2019-05-12 check condition it may not working fine
            return number.doubleValue() == 0;
        });
    }

    public boolean isScalar(){
        if (!isSquare()) return false;
        return forEachElementPredicate((number, row, column) ->{
            checkNullity(number,row,column);

            // TODO: 2019-05-12 check condition later they may not working fine
            if (getRowSize() == 0 && getColumnSize() == 0) return true;
            else if ( row == column) return number.equals(get(0,0));
            else return number.doubleValue() == 0;
        });
    }

    private boolean isSquare(Number[][] matrix){
        return matrix.length == matrix[0].length;
    }

    public NumberMatrix divideToEndOfMatrix(NumberMatrix matrix){
        if (getColumnSize() != matrix.getRowSize()) throw new IllegalArgumentException("You cannot divide these matrices. they are not compatible");

        if (this.isIdentity()) return matrix.inverse();
        if (matrix.isIdentity()) return this;

        return innerMultiPly(this,matrix.inverse());
    }

    public NumberMatrix multiplyToEndOfMatrix(NumberMatrix matrix){
        if (getColumnSize() != matrix.getRowSize()) throw new IllegalArgumentException("You cannot multiply these matrices. they are not compatible");

        if (this.isIdentity()) return matrix;
        if (matrix.isIdentity()) return this;

        return innerMultiPly(this,matrix);
    }

    private NumberMatrix innerMultiPly(NumberMatrix first, NumberMatrix second){

        NumberMatrix tempMatrix = new NumberMatrix(first.getRowSize(),second.getColumnSize());

        for (int row = 0; row < first.getRowSize() ; row++) for (int column = 0; column < second.getColumnSize(); column++) {
                Number value = 0;

                for (int i = 0; i < first.getColumnSize() ; i++){
                    Number fV = first.getRow(row)[i];
                    Number sV = second.getColumn(column)[i];
                    checkNullity(fV,row,i);
                    checkNullity(sV,column,i);
                    value =  value.doubleValue() + (fV.doubleValue() * sV.doubleValue());
                }

                tempMatrix.add(value,row,column);
            }

        return tempMatrix;
    }

    public void plusMatrixToMatrix(NumberMatrix matrix){
        binaryForEach(matrix,(n1, n2) -> n1.doubleValue() + n2.doubleValue());
    }

    public void subtractMatrixFromMatrix(NumberMatrix matrix){
        binaryForEach(matrix,(n1, n2) -> n1.doubleValue() - n2.doubleValue());
    }

    private void binaryForEach(NumberMatrix matrix, BinaryOperator<Number> operator){
        if (!isEqualSize(matrix)) throw new MatricesNotSameSizeException();

        for (int row = 0; row < getRowSize() ; row++) for (int column = 0; column < getColumnSize(); column++) {
            Number number = get(row,column);
            checkNullity(number,row,column);
            add(operator.apply(number,matrix.get(row,column)), row, column);
        }
    }

    public void plusNumberToMatrix(Number number){
        forEachElementFunction(x-> x.doubleValue()+number.doubleValue());
    }

    public void divideNumberToMatrix(Number number){
        forEachElementFunction(x-> x.doubleValue()/number.doubleValue());
    }

    public void multiplyNumberToMatrix(Number number){
        forEachElementFunction(x-> x.doubleValue()*number.doubleValue());
    }

    @Override
    public void print() {
        Arrays.stream(get(Number.class)).forEach(numbers -> {
            String builder = "[" + Stream.of(numbers).map(number -> number == null ? "Null" : number.toString()).collect(Collectors.joining(",")) + "]";
            System.out.println(builder);
        });
    }

    public final Number sumOfColumn(int column){
        return Stream.of(getColumn(column)).parallel().reduce(0f,(x,y)->x.doubleValue()+y.doubleValue());
    }

    public final Number sumOfRow(int row){
        return Stream.of(getRow(row)).parallel().reduce(0f,(x,y)->x.doubleValue()+y.doubleValue());
    }

    public Number sumOfElements(){
        return Stream.of(get(Number.class)).parallel().flatMap(Arrays::stream).reduce(0f,(x,y)->x.doubleValue()+y.doubleValue());
    }




}
