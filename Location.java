
/**
 * The {@code Location} class represents the index(i, j) of object in the 2D-Array maze grid.
 */

public class Location {
    private int i;
    private int j;

    public Location(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location other = (Location) obj;
            return this.i == other.getI() && this.j == other.getJ();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        return this.i * result + this.j;
    }

    @Override
    public String toString() {
        return "(" + this.i + ", " + this.j + ")";
    }
}

