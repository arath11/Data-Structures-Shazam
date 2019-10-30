import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat;

public class SoundSV {
    private Window w;
    private int sample_rate;
    private int sample_size;
    private int bandwidth;
    private int multiplier;
    private byte[] buffer;
    private double[] wave;
    private Complex[] c;
    private double[] freq;
    private  BufferedImage[] line ;


    public SoundSV() {
        this.sample_rate = 192000;
        this.sample_size = 2048;
        this.multiplier = 12;
        this.bandwidth = 20000;
        this.w = new Window();
        this.w.setTitle("FFT");



    }

    public void run(){
        //formateo del autio
        AudioFormat format = new AudioFormat((float)this.sample_rate, 16, 1, true, false);
        //lectura del sonido, creamos una targetDataLine le metemos una dataline
        TargetDataLine targetLine = null;
        try {
            targetLine = (TargetDataLine)AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, format));
            targetLine.open(format, 2 * this.sample_size);
        }
        catch (Exception ex) {
        }
        targetLine.start();
        //creamos un buffer gigantge
        buffer = new byte[targetLine.getBufferSize()];
        // es lo mismo que el buffer pero en double
        wave = new double[buffer.length / 2];
        //complejos pero en nullo
        c = new Complex[buffer.length / 2];
        //frequencia pero en nulo
        freq = new double[c.length * this.bandwidth / this.sample_rate];
        //arreglo nulos
        line = new BufferedImage[(int)this.w.alto];


        for (int i = 0; i < line.length; ++i) {
            //informacion de la altura=1 y anchura=1366 3 bandas
            line[i] = new BufferedImage((int)this.w.ancho, 1, 1);
        }

        //info para sacar
        int segundosGuardados = 0;
        //int frecuenciaMaxima = 0;

        //System.out.println("Frecuencias; "+bandwidth+"\nSample_rate; "+sample_rate+"\nSample_size; "+sample_size);
        while (true) {
            //poner un try sleep
            //mientras segundosGuardados sea diferentes a
            //if (segundosGuardados != this.w.segundosGuardados) {
            //System.out.println(w.segundosGuardados+" --------------------");
            /*if (this.w.segundosGuardados != 4) {
                this.w.segundosGuardados = 4;
                System.out.println("-----------------------------------");
                targetLine.close();
                format = new AudioFormat((float)this.sample_rate, 16, 1, true, false);
                try {
                    targetLine = (TargetDataLine)AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, format));
                    targetLine.open(format, 2 * this.sample_size);
                }
                catch (Exception e) {

                }
                targetLine.start();
                buffer = new byte[2 * this.sample_size];
                final double[] temp = copy(wave);
                wave = new double[this.sample_size];
                fill(wave, temp);
                c = new Complex[this.sample_size];
            }

             */
            freq = new double[this.bandwidth * this.sample_size / this.sample_rate];
            final int readBytes = targetLine.read(buffer, 0, targetLine.available());
            wave = convert(wave, buffer, readBytes);
            for (int j = 0; j < wave.length; ++j) {
                c[j] = new Complex(wave[j], 0.0);
            }
            c = fft(c);
            for (int j = 1; j < freq.length; ++j) {
                freq[j] = c[j].abs() / 65536.0 * 2.0 / this.sample_size * this.multiplier;
            }



            //frecuencia pico

            this.peak(freq, 1.0, this.bandwidth, "Hz");
            this.w.update();
/*
            final double[] signal = freq;
            final double amplitude = 1.0;
            final int bandwidth = this.bandwidth;
            final float n = -this.w.alto / 4.0f;
            this.w.getClass();

            //grafica la onda normal
            this.plot(signal, amplitude, bandwidth, "Hz", n + 11.0f + 1.0f, -this.w.alto / 4.0f);

*/

        }
    }

    private void peak(final double[] signal, final double amplitude, final int value, final String unit) {
        double pmax = 0.0;
        int nmax = 0;
        String a="";
        //recorremos el arreglo para encontrar el mas grande
        for (int i = 0; i < signal.length; ++i) {
            a += signal[i] + ", ";
            if (signal[i] > pmax) {
                pmax = signal[i];
                nmax = i;
            }
        }

        if (pmax >= 0.02 * amplitude) {
            double s = signal[nmax] * nmax;
            double g = signal[nmax];
            for (int j = 1; nmax - j > -1 && nmax + j < signal.length && signal[nmax - j] > 0.1 * signal[nmax] && signal[nmax + j] > 0.1 * signal[nmax]; ++j) {
                s += signal[nmax - j] * (nmax - j) + signal[nmax + j] * (nmax + j);
                g += signal[nmax - j] + signal[nmax + j];
            }
            pmax = s / g;

            int max=(int)(value*pmax/signal.length);
            //signal.lenght 213

            System.out.println(max+"   "+amplitude);

            w.drawLabel(Color.gray, Integer.toString(max), 0, 0);
        }
    }



    private static double[] convert(final double[] wave, final byte[] buffer, final int readBytes) {
        final double[] r = new double[wave.length];
        for (int i = wave.length - 1; i >= readBytes / 2; --i) {
            r[i] = wave[i - readBytes / 2];
        }
        for (int i = 0; i < readBytes / 2; ++i) {
            r[i] = (buffer[readBytes - 1 - 2 * i] << 8 | (buffer[readBytes - 2 - 2 * i] & 0xFF));
        }
        return r;
    }

    private static Complex[] fft(final Complex[] x) {
        final int n = x.length;
        if (n == 1) {
            return x;
        }
        Complex[] h = new Complex[n / 2];
        for (int k = 0; k < n / 2; ++k) {
            h[k] = x[2 * k];
        }
        final Complex[] e = fft(h);
        h = new Complex[n / 2];
        for (int i = 0; i < n / 2; ++i) {
            h[i] = x[2 * i + 1];
        }
        final Complex[] u = fft(h);
        h = new Complex[n];
        final double pin = -6.283185307179586 / n;
        for (int j = 0; j < n / 2; ++j) {
            final Complex exp = u[j].times(new Complex(StrictMath.cos(pin * j), StrictMath.sin(pin * j)));
            h[j] = e[j].plus(exp);
            h[j + n / 2] = e[j].minus(exp);
        }
        return h;
    }
    private static double[] copy(final double[] x) {
        final double[] y = new double[x.length];
        for (int i = 0; i < x.length; ++i) {
            y[i] = x[i];
        }
        return y;
    }
    private static void fill(final double[] x, final double[] y) {
        for (int i = 0; i < min(x.length, y.length); ++i) {
            x[i] = y[i];
        }
    }

    private static int min(final int x, final int y) {
        return (x < y) ? x : y;
    }

    private static double min(final double x, final double y) {
        return (x < y) ? x : y;
    }

    public static void main(final String[] args) {
        SoundSV prueba=new SoundSV();
        prueba.run();
    }

    private static double max(final double x, final double y) {
        return (x > y) ? x : y;
    }
    private void plot(final double[] signal, final double amplitude, final int value, final String unit, final float offset, final float height) {
        final double x3 = 10.0;
        final float ancho= this.w.ancho;
        final int n = 9;
        this.w.getClass();
        int mark = (int)min(x3, ancho / (n * 7 + 6));
        while (true) {
            final float n2 = this.w.ancho / mark;
            final int n3 = 18;
            this.w.getClass();
            if (n2 < n3 * 7 + 12) {
                break;
            }
            mark *= 2;
        }


        //dibujar frecuencias
        for (int i = 0; i < signal.length - 1; ++i) {
            final double x1 = this.w.ancho * i / (signal.length - 1) - this.w.ancho / 2.0f + 1.0f;
            final double x2 = this.w.ancho * (i + 1) / (signal.length - 1) - this.w.ancho / 2.0f + 1.0f;
            final float n24 = this.w.alto / 2.0f;
            final int n25 = 2;
            this.w.getClass();
            final double y1 = (n24 - n25 * 11 - 5.0f) * min((signal[i] + 1.0E-6) / amplitude, 1.0) + offset + height;
            final float n26 = this.w.alto / 2.0f;
            final int n27 = 2;
            this.w.getClass();
            final double y2 = (n26 - n27 * 11 - 5.0f) * min((signal[i + 1] + 1.0E-6) / amplitude, 1.0) + offset + height;
            this.w.drawLine(Color.WHITE, x1, y1, x2, y2);
        }
    }

    /*
    private static Color getColor(double h) {
        h = max(min(360.0 * (1.0 - h), 360.0), 0.0);
        double r = 0.0;
        double g = 0.0;
        double b = 0.0;
        if (h >= 0.0 && h < 60.0) {
            r = 255.0;
            g = 255.0;
            b = 255.0 - 255.0 * h / 60.0;
        }
        else if (h >= 60.0 && h < 180.0) {
            r = 255.0;
            g = 255.0 - 255.0 * (h - 60.0) / 120.0;
            b = 0.0;
        }
        else if (h >= 180.0 && h < 270.0) {
            r = 255.0 - 255.0 * (h - 180.0) / 180.0;
            g = 0.0;
            b = 255.0 * (h - 180.0) / 90.0;
        }
        else if (h >= 270.0 && h <= 360.0) {
            r = 255.0 - 255.0 * (h - 180.0) / 180.0;
            g = 0.0;
            b = 255.0 - 255.0 * (h - 270.0) / 90.0;
        }
        return new Color((int)r, (int)g, (int)b);
    }

    public void scale(final double[] freq) {
        double pmid = 0.0;
        for (int i = 1; i < freq.length; ++i) {
            pmid += freq[i];
        }
        pmid /= freq.length;
        if (pmid > 1.0E-4) {
            pmid = 0.02 / pmid;
            for (int i = 1; i < freq.length; ++i) {
                final int n = i;
                freq[n] *= pmid;
            }
        }
    }*/

}




//abajo del while true
//Mientras la frecuencia maxima no llegue a esta(20000hz) con -2 y no lleguen los segundos guardados a 4 o 10ms
//adaptable
//if (frecuenciaMaxima != -2 || segundosGuardados != 4) {
            /*if (frecuenciaMaxima != this.w.frecuenciaMaxima || segundosGuardados != this.w.segundosGuardados) {
                frecuenciaMaxima = this.w.frecuenciaMaxima;
                switch (this.w.segundosGuardados) {
                    //casos dependiendo del sample_rate
                    case -5: {
                        this.sample_rate = 2048;
                        this.sample_size = 16384;
                        this.multiplier = 200;
                        break;
                    }
                    case -4: {
                        this.sample_rate = 4096;
                        this.sample_size = 16384;
                        this.multiplier = 90;
                        break;
                    }
                    case -3: {
                        this.sample_rate = 8192;
                        this.sample_size = 16384;
                        this.multiplier = 60;
                        break;
                    }
                    case -2: {
                        this.sample_rate = 16384;
                        this.sample_size = 16384;
                        this.multiplier = 40;
                        break;
                    }
                    case -1: {
                        this.sample_rate = 48000;
                        this.sample_size = 16384;
                        this.multiplier = 20;
                        break;
                    }
                    case 0: {
                        this.sample_rate = 48000;
                        this.sample_size = 8192;
                        this.multiplier = 12;
                        break;
                    }
                    case 1: {
                        this.sample_rate = 96000;
                        this.sample_size = 8192;
                        this.multiplier = 12;
                        break;
                    }
                    case 2: {
                        this.sample_rate = 192000;
                        this.sample_size = 8192;
                        this.multiplier = 12;
                        break;
                    }
                    case 3: {
                        this.sample_rate = 192000;
                        this.sample_size = 4096;
                        this.multiplier = 12;
                        break;
                    }
                    case 4: {
                        //para guardar 10 ms esta es la que usamos
                        this.sample_rate = 192000;
                        this.sample_size = 2048;
                        this.multiplier = 12;
                        break;
                    }
                }
                switch (this.w.frecuenciaMaxima) {
                    case -3: {
                        this.bandwidth = this.sample_rate / 2;
                        break;
                    }
                    case -2: {
                        //usamos esta, la frecuencia maxima es de 20,000hz
                        this.bandwidth = 20000;
                        break;
                    }
                    case -1: {
                        this.bandwidth = 10000;
                        break;
                    }
                    case 0: {
                        this.bandwidth = 5000;
                        break;
                    }
                    case 1: {
                        this.bandwidth = 1000;
                        break;
                    }
                    case 2: {
                        this.bandwidth = 200;
                        break;
                    }
                }

                //this.bandwidth = min(this.bandwidth, this.sample_rate / 2);
                this.sample_rate = 192000;
                this.sample_size = 2048;
                this.multiplier = 12;

                this.bandwidth = 20000;
                System.out.println("Frecuencias; "+bandwidth+"\nSample_rate; "+sample_rate+"\nSample_size; "+sample_size);

                if (segundosGuardados != this.w.segundosGuardados) {
                    segundosGuardados = this.w.segundosGuardados;
                    targetLine.close();
                    format = new AudioFormat((float)this.sample_rate, 16, 1, true, false);
                    try {
                        targetLine = (TargetDataLine)AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, format));
                        targetLine.open(format, 2 * this.sample_size);
                    }
                    catch (Exception ex2) {}
                    targetLine.start();
                    buffer = new byte[2 * this.sample_size];
                    final double[] temp = copy(wave);
                    wave = new double[this.sample_size];
                    fill(wave, temp);
                    c = new Complex[this.sample_size];
                }
                freq = new double[this.bandwidth * this.sample_size / this.sample_rate];


            }*/

            