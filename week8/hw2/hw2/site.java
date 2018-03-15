package hw2;

public class site {
    int x;
    int y;
    boolean open;
    boolean full;
    int number = 0;
    int size;

    public site (){
        this.x = 0;
        this.y = 0;
        this.open = false;
        this.full = false;
    }

    public site (int x, int y, boolean open, boolean full){
        this.x = x;
        this.y = y;
        this.open = open;
        this.full = full;
    }

    public site (int x, int y, boolean open, boolean full, int N){
        this.x = x;
        this.y = y;
        this.open = open;
        this.full = full;
        this.number = x * N + y;
    }
}
