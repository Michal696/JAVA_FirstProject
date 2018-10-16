
package ija.Model;

public final class Position {

    private double x, y;
    public Position(){
        this.x = 0;
        this.y = 0;
    }
    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) { this.y = y; }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
