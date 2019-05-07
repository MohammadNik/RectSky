package org.RectSky;

public class Index {
    private int row;
    private int column;

    public static Index builder(int row, int column){
        return new Index(row,column);
    }

    private Index(int row, int column){
        this.row = row;
        this.column = column;
    }

    public void zeroColumn(){
        column = 0;
    }

    public void incrementColumn(){
        column++;
    }

    public void incrementRow(){
        row++;
    }


    public int column() {
        return column;
    }

    public int row() {
        return row;
    }
}
