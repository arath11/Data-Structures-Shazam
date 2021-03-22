public class Complex {
    private final double real;
    private final double imaginario;

    public Complex(double real, double imaginario) {
        this.real = real;
        this.imaginario = imaginario;
    }

    public double abs() {
        return Math.hypot(this.real, this.imaginario);
    }

    public Complex suma(Complex c) {
        return new Complex(this.real + c.real, this.imaginario + c.imaginario);
    }

    public Complex resta(Complex c) {
        return new Complex(this.real - c.real, this.imaginario - c.imaginario);
    }

    public Complex multiplica(Complex c) {
        return new Complex(this.real * c.real - this.imaginario * c.imaginario, this.real * c.imaginario + this.imaginario * c.real);
    }

    public String toString() {
        return this.real + "\n" + this.imaginario;
    }
}
