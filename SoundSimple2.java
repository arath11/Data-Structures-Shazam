
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine.Info;

//microfono de la computadora

public class SoundSimple2 {
    private int sampleRate = 44100;
    private int sampleSize = 2048;
    private int bandwidth = 20000;
    private byte[] buffer;
    private double[] onda;
    private Complex[] c;
    private double[] freq;
    private TargetDataLine targetLine;
    private double[][] datos = new double[20][2];
    private int posDatos = 0;
    boolean bandera = true;

    public SoundSimple2() {
        this.bandera = true;
        this.targetLine = null;
        this.run();
    }

    public void run() {
        AudioFormat format = new AudioFormat((float)this.sampleRate, 16, 1, true, false);

        try {
            this.targetLine = (TargetDataLine)AudioSystem.getLine(new Info(TargetDataLine.class, format));
            this.targetLine.open(format, 2 * this.sampleSize);
        } catch (LineUnavailableException var6) {
        } catch (Exception var7) {
        }

        this.targetLine.start();
        this.buffer = new byte[this.targetLine.getBufferSize()];
        this.onda = new double[this.buffer.length / 2];
        this.c = new Complex[this.buffer.length / 2];
        this.freq = new double[this.c.length * this.bandwidth / this.sampleRate];
        float seg = 0.0F;

        while(this.bandera) {
            try {
                Thread.sleep(50L);
            } catch (InterruptedException var5) {
                var5.printStackTrace();
            }

            seg += 100.0F;
            int readBytes = this.targetLine.read(this.buffer, 0, this.targetLine.available());
            this.onda = convert(this.onda, this.buffer, readBytes);

            int j;
            for(j = 0; j < this.onda.length; ++j) {
                this.c[j] = new Complex(this.onda[j], 0.0D);
            }

            this.c = fft(this.c);

            for(j = 1; j < this.freq.length; ++j) {
                this.freq[j] = this.c[j].abs() / 131072.0D / (double)this.sampleSize * 12.0D;
            }

            this.peak(this.freq, 1.0D, this.bandwidth);
            if (seg == 15000.0F) {
                this.bandera = false;
            }
        }

        this.mergesortAmplitud();
        this.targetLine.flush();
        this.targetLine.stop();

    }

    public double[][] getDatos() {
        return this.datos;
    }

    private void peak(double[] signal, double amplitude, int value) {
        double pico = 0.0D;
        int nmax = 0;

        for(int i = 0; i < signal.length; ++i) {
            if (signal[i] > pico) {
                pico = signal[i];
                nmax = i;
            }
        }

        Double puntoMaximo = pico;
        if (pico >= 0.02D * amplitude) {
            double s = signal[nmax] * (double)nmax;
            double g = signal[nmax];

            int max;
            for(max = 1; nmax - max > -1 && nmax + max < signal.length && signal[nmax - max] > 0.1D * signal[nmax] && signal[nmax + max] > 0.1D * signal[nmax]; ++max) {
                s += signal[nmax - max] * (double)(nmax - max) + signal[nmax + max] * (double)(nmax + max);
                g += signal[nmax - max] + signal[nmax + max];
                System.out.println(s + "   " + g);
            }

            pico = s / g;
            max = (int)((double)value * pico / (double)signal.length);
            if (this.posDatos < this.datos.length) {
                if (max != 21) {
                    this.datos[this.posDatos][0] = (double)(max - max % 10);
                    this.datos[this.posDatos][1] = puntoMaximo;
                    ++this.posDatos;
                }
            } else {
                this.resize();
                if (max != 21) {
                    this.datos[this.posDatos][0] = (double)(max - max % 10);
                    this.datos[this.posDatos][1] = puntoMaximo;
                    ++this.posDatos;
                }
            }
        }

    }

    public void imprimeDatos() {
        for(int i = 0; i < this.posDatos; ++i) {
            System.out.println(this.datos[i][0]);
        }

    }

    public void resize() {
        double[][] nuevo = new double[this.datos.length * 2][2];

        for(int i = 0; i < this.datos.length; ++i) {
            nuevo[i] = this.datos[i];
        }

        this.datos = nuevo;
    }

    private static double[] convert(double[] wave, byte[] buffer, int readBytes) {
        double[] r = new double[wave.length];

        for(int i = 0; i < readBytes / 2; ++i) {
            r[i] = (double)(buffer[readBytes - 1 - 2 * i] << 8 | buffer[readBytes - 2 - 2 * i] & 255);
        }

        return r;
    }

    private static Complex[] fft(Complex[] entrada) {
        int n = entrada.length;
        if (n == 1) {
            return entrada;
        } else {
            Complex[] datosPartidos = new Complex[n / 2];

            for(int k = 0; k < n / 2; ++k) {
                datosPartidos[k] = entrada[2 * k];
            }

            Complex[] e = fft(datosPartidos);
            datosPartidos = new Complex[n / 2];

            for(int i = 0; i < n / 2; ++i) {
                datosPartidos[i] = entrada[2 * i + 1];
            }

            Complex[] u = fft(datosPartidos);
            datosPartidos = new Complex[n];
            double pi = -6.283185307179586D / (double)n;

            for(int j = 0; j < n / 2; ++j) {
                Complex exp = u[j].multiplica(new Complex(StrictMath.cos(pi * (double)j), StrictMath.sin(pi * (double)j)));
                datosPartidos[j] = e[j].suma(exp);
                datosPartidos[j + n / 2] = e[j].resta(exp);
            }

            return datosPartidos;
        }
    }

    public void mergesortAmplitud() {
        this.mergesortAmplitud(this.datos, 0, this.datos.length - 1);
    }

    private void mergesortAmplitud(double[][] datos, int ini, int fin) {
        if (ini < fin) {
            int mid = (ini + fin) / 2;
            this.mergesortAmplitud(datos, ini, mid);
            this.mergesortAmplitud(datos, mid + 1, fin);
            this.mergeAmplitud(datos, ini, fin);
        }

    }

    private void mergeAmplitud(double[][] datos, int ini, int fin) {
        int mid = (ini + fin) / 2;
        int indiceArray1 = mid - ini + 1;
        int indiceArray2 = fin - mid;
        double[][] arrayIzquierda = new double[indiceArray1][2];
        double[][] arrayDerecha = new double[indiceArray2][2];

        int i;
        for(i = 0; i < indiceArray1; ++i) {
            arrayIzquierda[i][0] = datos[i + ini][0];
            arrayIzquierda[i][1] = datos[i + ini][1];
        }

        for(i = 0; i < indiceArray2; ++i) {
            arrayDerecha[i][0] = datos[i + mid + 1][0];
            arrayDerecha[i][1] = datos[i + mid + 1][1];
        }

        int j = 0;
        i = 0;
        int var11 = ini;

        while(i < indiceArray1 && j < indiceArray2) {
            if (arrayIzquierda[i][1] > arrayDerecha[j][1]) {
                datos[var11++] = arrayIzquierda[i++];
            } else {
                datos[var11++] = arrayDerecha[j++];
            }
        }

        while(i < indiceArray1) {
            datos[var11++] = arrayIzquierda[i++];
        }

        while(j < indiceArray2) {
            datos[var11++] = arrayDerecha[j++];
        }

    }

    public void mergesortHz() {
        this.mergesortHz(this.datos, 0, this.datos.length - 1);
    }

    private void mergesortHz(double[][] datos, int ini, int fin) {
        if (ini < fin) {
            int mid = (ini + fin) / 2;
            this.mergesortHz(datos, ini, mid);
            this.mergesortHz(datos, mid + 1, fin);
            this.mergeHz(datos, ini, fin);
        }

    }

    private void mergeHz(double[][] datos, int ini, int fin) {
        int mid = (ini + fin) / 2;
        int indiceArray1 = mid - ini + 1;
        int indiceArray2 = fin - mid;
        double[][] arrayIzquierda = new double[indiceArray1][2];
        double[][] arrayDerecha = new double[indiceArray2][2];

        int i;
        for(i = 0; i < indiceArray1; ++i) {
            arrayIzquierda[i][0] = datos[i + ini][0];
            arrayIzquierda[i][1] = datos[i + ini][1];
        }

        for(i = 0; i < indiceArray2; ++i) {
            arrayDerecha[i][0] = datos[i + mid + 1][0];
            arrayDerecha[i][1] = datos[i + mid + 1][1];
        }

        int j = 0;
        i = 0;
        int var11 = ini;

        while(i < indiceArray1 && j < indiceArray2) {
            if (arrayIzquierda[i][0] > arrayDerecha[j][0]) {
                datos[var11++] = arrayIzquierda[i++];
            } else {
                datos[var11++] = arrayDerecha[j++];
            }
        }

        while(i < indiceArray1) {
            datos[var11++] = arrayIzquierda[i++];
        }

        while(j < indiceArray2) {
            datos[var11++] = arrayDerecha[j++];
        }

    }

    public static void main(String[] args) {
        SoundSimple2 prueba = new SoundSimple2();
        prueba.run();
    }
}
