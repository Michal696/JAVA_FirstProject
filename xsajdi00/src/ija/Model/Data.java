
package ija.Model;

public class Data {

    protected double value;

    public Data(double value) {
        this.value = value;
    }

    public Data(String value) {
        this.value = Double.parseDouble(value);
    }
    public Data () {
        this.value = 0;
    }

    public void updateData(double value) {
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    public void printState() {
        System.out.println(" : " + value);
    }

}
