package schoolplanner.util;

public class Coordinate {
    private int valueX;
    private int valueY;

    public Coordinate(Coordinate coordinate) {
        this.valueX = coordinate.getValueX();
        this.valueY = coordinate.getValueY();
    }

    public Coordinate(int valueX, int valueY) {
        this.valueX = valueX;
        this.valueY = valueY;
    }

    /**
     * Getter for the X value;
     * @return X Value.
     */
    public int getValueX() {
        return valueX;
    }

    /**
     * Getter for the Y value;
     * @return Y Value.
     */
    public int getValueY() {
        return valueY;
    }

    /**
     * Setter for the X value.
     * @param valueX X value.
     */
    public void setValueX(int valueX) {
        this.valueX = valueX;
    }

    /**
     * Setter for the Y value.
     * @param valueY Y value.
     */
    public void setValueY(int valueY) {
        this.valueY = valueY;
    }

    /**
     * toString function for printing.
     * @return Value in string form.
     */
    @Override
    public String toString(){
        return "X: " + this.getValueX() + " Y: " + this.getValueY();
    }
}
