import org.RectSky.NumberMatrix;
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
        out.println("Diagonal ->");
        out.println(Arrays.toString(numberMatrix.getDiagonal()));
    }
}
