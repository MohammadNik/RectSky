import org.RectSky.AbstractMatrix;
import org.RectSky.NumberMatrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static java.lang.System.out;

public class NumberMatrixTest {
    private NumberMatrix numberMatrix;


    @Before
    public void startup(){
         numberMatrix = new NumberMatrix(2,2);
        numberMatrix.add(4).add(5.4)
                    .add(5).add(2.5);
    }

    @Test
    public void addWithRowColumn(){
        numberMatrix.add(42,1,1);
        out.print("Matrix[1][1] -> ");
        out.println(numberMatrix.get(1,1));
    }

    @Test
    public void print(){
        out.println("Matrix");
        numberMatrix.print();
    }

    @Test
    public void getRow(){
        out.print("Row -> 0 ");
        out.println(Arrays.toString(numberMatrix.getRow(0)));

    }

    @Test
    public void getColumn(){
        out.print("Column -> 0 ");
        out.println(Arrays.toString(numberMatrix.getColumn(0)));
    }

    @Test
    public void getDiagonal(){
        out.print("Diagonal ->");
        out.println(Arrays.toString(numberMatrix.getDiagonal()));
    }

    @Test
    public void testConsumer() {
        numberMatrix.forEachElementConsume(number -> out.println(number.doubleValue() * 2) );
    }

    @Test
    public void testConsumerColumn() {
        numberMatrix.forEachColumnConsume(number -> out.println(number.doubleValue() * 2),0 );
    }

    @Test
    public void testConsumerRow() {
        numberMatrix.forEachRowConsume(number -> out.println(number.doubleValue() * 2),0 );
    }

    @Test
    public void testConsumerDiagonal() {
        numberMatrix.forEachRowConsume(number -> out.println(number.doubleValue() * 2),0 );
    }

    @Test
    public void testFunction() {
        numberMatrix.print();
        numberMatrix.forEachElementFunction(number -> number.doubleValue()*2);
        numberMatrix.print();
    }

    @Test
    public void testFunctionColumn() {
        numberMatrix.print();
        numberMatrix.forEachColumnFunction(number -> number.doubleValue()*2,0);
        numberMatrix.print();
    }

    @Test
    public void testFunctionRow() {
        numberMatrix.print();
        numberMatrix.forEachRowFunction(number -> number.doubleValue()*2,0 );
        numberMatrix.print();
    }

    @Test
    public void testFunctionDiagonal() {
        numberMatrix.print();
        numberMatrix.forEachDiagonalFunction(number -> number.doubleValue()*2 );
        numberMatrix.print();
    }


    @Test
    public void testPredicate() {
        out.println(numberMatrix.forEachElementPredicate((number,row,column) -> number.doubleValue() > 0));
    }

    @Test
    public void testPredicateColumn() {
        out.println(numberMatrix.forEachColumnPredicate((number,row,column) -> number.doubleValue() > 0,1));
    }

    @Test
    public void testPredicateRow() {
        out.println(numberMatrix.forEachRowPredicate((number,row,column) -> number.doubleValue() > 0,1));
    }

    @Test
    public void testPredicateDiagonal() {
        out.println(numberMatrix.forEachDiagonalPredicate((number,row,column) -> number.doubleValue() > 0));
    }

    @Test
    public void createWithBuilder(){
        NumberMatrix numberMatrix = AbstractMatrix.TWO_TWO(1,2,3,4);
        numberMatrix.print();
    }

    @Test
    public void size(){
        out.println(numberMatrix.occupiedElementsSize());
    }



}
