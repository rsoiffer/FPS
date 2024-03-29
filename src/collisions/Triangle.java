package collisions;

public class Triangle {

    public Vector p1, p2, p3;

    public Triangle(Vector p1, Vector p2, Vector p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Triangle(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
        p1 = new Vector(x1, y1, z1);
        p2 = new Vector(x2, y2, z2);
        p3 = new Vector(x3, y3, z3);
    }

    @Override
    public String toString() {
        return "p1: " + p1 + "\np2: " + p2 + "\np3: " + p3;
    }

}
