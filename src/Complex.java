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
}
