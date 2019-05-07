import org.RectSky.NumberMatrix;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class LibraryCreation {
    private NumberMatrix numberMatrix;


    @Before
    public void startup(){
         numberMatrix = new NumberMatrix(2,2);
        numberMatrix.add(4).add(5.4)
                    .add(5).add(2.5);
    }

    @Before
    public void addWithRowColumn(){
        numberMatrix.add(42,1,1);
    }

    @Test
    public void print(){
        numberMatrix.print();
    }

    @Test
    public void getRow(){
        System.out.println("Row -> 0");
        System.out.println(Arrays.toString(numberMatrix.getRow(0)));
    }

    @Test
    public void getColumn(){
        System.out.println("Column -> 0");
        System.out.println(Arrays.toString(numberMatrix.getColumn(0)));
    }
}
