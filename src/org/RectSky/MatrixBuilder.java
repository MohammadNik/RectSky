package org.RectSky;

public class   MatrixBuilder {

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

}
