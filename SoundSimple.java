import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import javax.sound.sampled.*;

public class SoundSimple {
    private Window w;
    private int sample_rate;
    private int sample_size;
    private int bandwidth;
    private int multiplier;
    private byte[] buffer;
    private double[] wave;
    private Complex[] c;
    private double[] freq;
    private TargetDataLine targetLine;
//    private  BufferedImage[] line ;
    private double[][] datos;
    private int posDatos;


    public SoundSimple() {
        /*
        this.sample_rate = 192000;
        this.sample_size = 2048;
        this.multiplier = 12;
        this.bandwidth = 20000;
        */
        this.sample_rate = 44100;
        this.sample_size = 2048;
        this.multiplier = 12;
        this.bandwidth = 20000;

        //aprox un min de datos
        datos=new double[10][2];
        posDatos=0;

        this.targetLine = null;
        this.w = new Window();
        this.w.setTitle("FFT");


    }

    public void run(){
        //formateo del autio
        AudioFormat format = new AudioFormat((float)this.sample_rate, 16, 1, true, false);
        System.out.println("SoundSimple\n Formato Audio: "+format.toString());
        //lectura del sonido, creamos una targetDataLine le metemos una dataline


        try {
            targetLine = (TargetDataLine)AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, format));
            targetLine.open(format, 2 * this.sample_size);
        }
        catch (LineUnavailableException ex) {
        }
        catch (Exception ex) {
        }

        targetLine.start();



        //readBytes, freq, c(complex), wave

        //creamos un buffer con el tamaño del bufferSize de la entradda de formato, o 2*2048
        buffer = new byte[targetLine.getBufferSize()];
        System.out.println("buffer="+buffer.length+"/////////////////////////");

        // es lo mismo que el buffer pero en double
        wave = new double[buffer.length / 2];
        System.out.println("wave="+wave.length+"/////////////////////////");

        //complejos pero en nullo
        c = new Complex[buffer.length / 2];


        //frequencia pero en nulo
        System.out.println(c.length * this.bandwidth / this.sample_rate+"///////");

        freq = new double[c.length * this.bandwidth / this.sample_rate];
        System.out.println("Arreglo de complex="+c.length);


        while (true) {
            //freq = new double[this.bandwidth * this.sample_size / this.sample_rate];

            //podemos hacer esta una variable global?

            int readBytes = targetLine.read(buffer, 0, targetLine.available());
            //readBytes=4096 aqui
//            System.out.println("readbytes"+readBytes+"///////");
            /*
            for (int i = 0; i < buffer.length; i++) {
                System.out.println(buffer[i]);
            }*/

            //System.out.println(readBytes);

            wave = convert(wave, buffer, readBytes);


            for (int j = 0; j < wave.length; ++j) {

              //  System.out.println(wave[j]);
                c[j] = new Complex(wave[j], 0.0);
            }

            c = fft(c);
            /*for (int i = 0; i < c.length; i++) {
                System.out.println(c[i].toString());
            }*/

            for (int j = 1; j < freq.length; ++j) {
                freq[j] = c[j].abs() / 131072 / this.sample_size * 12;

            }


            //frecuencia pico
            this.peak(freq, 1.0, this.bandwidth, "Hz");

            this.w.update();
            //final double[] signal = freq;
            //final double amplitude = 1.0;

            //final int bandwidth = this.bandwidth;
            final float n = -this.w.alto / 4.0f;
            this.w.getClass();

        }
    }

    private void peak(final double[] signal, final double amplitude, final int value, final String unit) {
        double pmax = 0.0;
        int nmax = 0;
        String a="";

        //recorremos el arreglo para encontrar el mas grande
        for (int i = 0; i < signal.length; ++i) {
            a += signal[i] + ", ";
            //System.out.println(a);
            if (signal[i] > pmax) {
                pmax = signal[i];
                nmax = i;
            }
        }
        //System.out.println("Pmax" + pmax+" nmax "+ nmax);

        Double puntoMaximo=pmax;
        if (pmax >= 0.02 * amplitude) {

            double s = signal[nmax] * nmax;
            double g = signal[nmax];

            for (int j = 1; nmax - j > -1 && nmax + j < signal.length && signal[nmax - j] > 0.1 * signal[nmax] && signal[nmax + j] > 0.1 * signal[nmax]; ++j) {
                s += signal[nmax - j] * (nmax - j) + signal[nmax + j] * (nmax + j);
                g += signal[nmax - j] + signal[nmax + j];
            }

            pmax = s / g;

            int max=(int)(value*pmax/signal.length);

            if(posDatos<datos.length){
                if(max!=21) {
                    datos[posDatos][0] = max-max%10;
                    datos[posDatos][1] = puntoMaximo;
                    System.out.println("Max:" + datos[posDatos][0] + ", puntotMaximo:" + datos[posDatos][1]);
                    //System.out.println( datos[posDatos][0]);
                    posDatos++;
                }
            }else{
                resize();
                //System.out.println("Nuevo datos:"+datos.length);
                if(max!=21) {
                    datos[posDatos][0] = max-max%10;
                        datos[posDatos][1] = puntoMaximo;
                    System.out.println("Max:" + datos[posDatos][0] + ", puntotMaximo:" + datos[posDatos][1]);
                    //System.out.println( datos[posDatos][0]);
                    posDatos++;
                }
            }

            w.drawLabel(Color.gray, Integer.toString(max), 0, 0);
        }
    }

    public void imprimeDatos(){
        for(int i=0;i<posDatos;i++){
            //System.out.println("Max:" + datos[i][0] + ", puntotMaximo:" + datos[i][1]);
            System.out.println(datos[i][0]);
        }
    }
    public void resize(){
        double[][] nuevo=new double[this.datos.length*2][2];
        for(int i=0; i<this.datos.length; i++){
            nuevo[i]=this.datos[i];
        }
        this.datos=nuevo;
    }
    private static double[] convert(final double[] wave, final byte[] buffer, final int readBytes) {
        /*System.out.println("ReadByres "+readBytes);
        System.out.println("Wave "+wave.length );
        System.out.println("Buffer "+buffer.length );*/

        final double[] r = new double[wave.length];
        //de 2048 a 0

        /*for (int i = wave.length - 1; i >= readBytes / 2; --i) {
            r[i] = wave[i - readBytes / 2];
            System.out.println(i);
            //System.out.println(wave[i - readBytes / 2]);
        }*/

        //de 0 a 2048

        for (int i = 0; i < readBytes / 2; ++i) {
            r[i] = (buffer[readBytes - 1 - 2 * i] << 8 | (buffer[readBytes - 2 - 2 * i] & 0xFF));
            //System.out.println(buffer[readBytes - 1 - 2 * i] << 8 | (buffer[readBytes - 2 - 2 * i] & 0xFF));
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

    public   void mergesortAmplitud() {
        mergesortAmplitud(this.datos,0,this.datos.length-1);
    }

    private  void mergesortAmplitud(double[][] datos, int ini, int fin) {
        if(ini<fin) {
            int mid=(ini+fin)/2;
            mergesortAmplitud(datos,ini,mid);
            mergesortAmplitud(datos,mid+1,fin);
            mergeAmplitud(datos,ini,fin);
        }

    }

    private  void mergeAmplitud(double[][] datos, int ini, int fin) {
        int mid=(ini+fin)/2;
        int indiceArray1=mid-ini+1;
        int indiceArray2=fin-mid;
        double[][] arrayIzquierda=new double[indiceArray1][2];
        double[][] arrayDerecha=new double[indiceArray2][2];
        for(int i =0;i<indiceArray1;i++) {
            arrayIzquierda[i][0]=datos[i+ini][0];
            arrayIzquierda[i][1]=datos[i+ini][1];
        }
        for(int j =0;j<indiceArray2;j++) {
            arrayDerecha[j][0]=datos[j+mid+1][0];
            arrayDerecha[j][1]=datos[j+mid+1][1];
        }
        int i,j,k;
        i=j=0;
        k=ini;
        while(i<indiceArray1 && j<indiceArray2) {
            if(arrayIzquierda[i][1]>arrayDerecha[j][1]) {
                datos[k++]=arrayIzquierda[i++];
            }else {
                datos[k++]=arrayDerecha[j++];
            }
        }
        while(i<indiceArray1) {
            datos[k++]=arrayIzquierda[i++];
        }
        while(j<indiceArray2) {
            datos[k++]=arrayDerecha[j++];
        }
    }


    public void mergesortHz() {
        mergesortHz(this.datos,0,this.datos.length-1);
    }


    private void mergesortHz(double[][] datos, int ini, int fin) {
        if(ini<fin) {
            int mid=(ini+fin)/2;
            mergesortHz(datos,ini,mid);
            mergesortHz(datos,mid+1,fin);
            mergeHz(datos,ini,fin);
        }

    }


    private void mergeHz(double[][] datos, int ini, int fin) {
        int mid=(ini+fin)/2;
        int indiceArray1=mid-ini+1;
        int indiceArray2=fin-mid;
        double[][] arrayIzquierda=new double[indiceArray1][2];
        double[][] arrayDerecha=new double[indiceArray2][2];
        for(int i =0;i<indiceArray1;i++) {
            arrayIzquierda[i][0]=datos[i+ini][0];
            arrayIzquierda[i][1]=datos[i+ini][1];
        }
        for(int j =0;j<indiceArray2;j++) {
            arrayDerecha[j][0]=datos[j+mid+1][0];
            arrayDerecha[j][1]=datos[j+mid+1][1];
        }
        int i,j,k;
        i=j=0;
        k=ini;
        while(i<indiceArray1 && j<indiceArray2) {
            if(arrayIzquierda[i][0]>arrayDerecha[j][0]) {
                datos[k++]=arrayIzquierda[i++];
            }else {
                datos[k++]=arrayDerecha[j++];
            }
        }
        while(i<indiceArray1) {
            datos[k++]=arrayIzquierda[i++];
        }
        while(j<indiceArray2) {
            datos[k++]=arrayDerecha[j++];
        }
    }


    public static void main(final String[] args) {
        SoundSimple prueba=new SoundSimple();
        prueba.run();
    }
}

