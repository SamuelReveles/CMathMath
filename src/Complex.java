import java.util.Objects;

public class Complex {
    public final double real;
    public final double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public static Complex fromReal(double real) {
        return new Complex(real, 0);
    }

    public static Complex fromImaginary(double imaginary) {
        return new Complex(0, imaginary);
    }

    public static Complex fromDegrees(double degrees) {
        return new Complex(Math.toRadians(degrees), 0);
    }

    public Complex add(Complex other) {
        return new Complex(this.real + other.real, this.imaginary + other.imaginary);
    }

    public Complex substract(Complex other) {
        return new Complex(this.real - other.real, this.imaginary - other.imaginary);
    }

    public Complex multiply(Complex other) {
        double r = this.real * other.real - this.imaginary * other.imaginary;
        double i = this.real * other.imaginary + this.imaginary * other.real;
        return new Complex(r, i);
    }

    public Complex multiply(double other) {
        double r = this.real * other;
        double i = this.imaginary * other;
        return new Complex(r, i);
    }

    public Complex divide(Complex other) {
        double denominator = other.real * other.real + other.imaginary * other.imaginary;
        double r = (this.real * other.real + this.imaginary * other.imaginary) / denominator;
        double i = (this.imaginary * other.real - this.real * other.imaginary) / denominator;
        return new Complex(r, i);
    }

    public boolean equals(Complex other) {
        return this.real == other.real && this.imaginary == other.imaginary;
    }

    public boolean isLessThan(Complex other) {
        return this.real < other.real;
    }

    public boolean isGreaterThan(Complex other) {
        return this.real > other.real;
    }

    public Complex sin() {
        double r = Math.sin(real) * Math.cosh(imaginary);
        double i = Math.cos(real) * Math.sinh(imaginary);
        return new Complex(r, i);
    }

    public Complex cos() {
        double r = Math.cos(real) * Math.cosh(imaginary);
        double i = -Math.sin(real) * Math.sinh(imaginary);
        return new Complex(r, i);
    }

    public Complex tan() {
        return this.sin().divide(this.cos());
    }

    public Complex sec() {
        return new Complex(1, 0).divide(this.cos());
    }

    public Complex csc() {
        return new Complex(1, 0).divide(this.sin());
    }

    public Complex cot() {
        return this.cos().divide(this.sin());
    }

    public Complex round() {
        return new Complex(Math.round(this.real * 100) / 100, Math.round(this.imaginary * 100) / 100);
    }

    public double magnitude() {
        return Math.sqrt(this.real * this.real + this.imaginary * this.imaginary);
    }

    public double argument() {
        return Math.atan2(this.imaginary, this.real);
    }

    public Complex pow(Complex exponent) {
        double r = this.magnitude();
        double theta = this.argument();

        // ln(z) = ln(r) + iθ
        Complex logZ = new Complex(Math.log(r), theta);

        // w * ln(z)
        Complex wLogZ = exponent.multiply(logZ);

        // e^(w * ln(z)) = exp(a + bi) = exp(a) * (cos(b) + i sin(b))
        double expReal = Math.exp(wLogZ.real);
        return new Complex(
                expReal * Math.cos(wLogZ.imaginary),
                expReal * Math.sin(wLogZ.imaginary));
    }

    @Override
    public String toString() {
        if (imaginary == 0)
            return "" + real;
        if (real == 0)
            return "" + imaginary + "i";
        if (imaginary > 0)
            return real + " + " + imaginary + "i";
        return real + " - " + (-imaginary) + "i";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || object.getClass() != this.getClass())
            return false;

        return this.equals((Complex) object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }
}
