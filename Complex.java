public class Complex
{
    private final double real;
    private final double imaginario;

    public Complex(final double re, final double imaginario) {
        this.real = re;
        this.imaginario = imaginario;
    }

    public double abs() {
        return Math.hypot(this.real, this.imaginario);
    }

    public Complex plus(final Complex c) {
        return new Complex(this.real + c.real, this.imaginario + c.imaginario);
    }

    public Complex minus(final Complex c) {
        return new Complex(this.real - c.real, this.imaginario - c.imaginario);
    }

    public Complex times(final Complex c) {
        return new Complex(this.real * c.real - this.imaginario * c.imaginario, this.real * c.imaginario + this.imaginario * c.real);
    }
    public String toString(){
        return ("Real: "+real+", Imaginario: "+imaginario);
    }
}