import org.RectSky.NumberMatrix;
import org.junit.Test;

public class LibraryCreation {


    @Test
    public void createNumberMatrix(){
        NumberMatrix numberMatrix = new NumberMatrix(1,2);
    }

    @Test
    public void addElementsToNumberMatrix(){
        NumberMatrix numberMatrix = new NumberMatrix(2,2);
        numberMatrix.add(4);
        numberMatrix.add(5.4);
        numberMatrix.add(5);
        numberMatrix.add(2.5);
        numberMatrix.add(3.2);
        System.out.println(numberMatrix.isMatrixFull());
        numberMatrix.print();
    }
}
