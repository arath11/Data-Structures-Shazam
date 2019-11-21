import java.awt.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


public class FFTSV {
    private static AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private static AudioInputStream audioInputStream;
    private static AudioFormat format;
    private int sR;
    private int sS;
    private int bw;

    private int sample_rate;
    private int sample_size;
    private int bandwidth;
    private byte[] buffer;
    private double[] wave;
    private Complex[] c;
    private double[] freq;
    private TargetDataLine targetLine;
    private String PatnToFile;
    private double[][] datos;
    private int posDatos;


    public FFTSV(int song) {

        this.sR = 44100;
        this.sR = 18;
        this.sS = 2048;
        this.bw = 20000;
        sample_rate=44100;
        sample_size=2048;
        //sample_size=1024;
        bandwidth=20000;
        //buffer = new byte[targetLine.getBufferSize()];
        //buffer = new byte[4096];
        buffer = new byte[sample_size*2];
        wave = new double[buffer.length / 2];
        c = new Complex[buffer.length / 2];
        freq = new double[c.length * this.bandwidth / this.sample_rate];
        //freq = new double[928];

        int rangoMenor=1, rangoMayor=sample_size*2;

        datos=new double[700][2];
        posDatos=0;

        //Entrada de archivo
        if (song==1){
            this.PatnToFile= "C:\\Users\\J4\\IdeaProjects\\Shazam\\src\\100.wav";
        }else if (song==2){
            this.PatnToFile= "C:\\Users\\J4\\IdeaProjects\\Shazam\\src\\PruebaSunflower.wav";
        }else if (song==3){
            this.PatnToFile= "C:\\Users\\J4\\IdeaProjects\\Shazam\\src\\Bidi.wav";
        }else if (song==4){
           this.PatnToFile= "C:\\Users\\J4\\IdeaProjects\\Shazam\\src\\Firework.wav";
        }else if (song==5){
            this.PatnToFile= "C:\\Users\\J4\\IdeaProjects\\Shazam\\src\\UptownFunk.wav";
        }else if (song==6){
           this.PatnToFile= "C:\\Users\\J4\\IdeaProjects\\Shazam\\src\\SecretoDeAmor.wav";
           //esta no jala
        }else if (song==7){
            this.PatnToFile= "C:\\Users\\J4\\IdeaProjects\\Shazam\\src\\YouBetterRun.wav";
        }else if (song==8){
            this.PatnToFile= "C:\\Users\\J4\\IdeaProjects\\Shazam\\src\\7Years.wav";
        }else if (song==9){
            this.PatnToFile= "C:\\Users\\J4\\IdeaProjects\\Shazam\\src\\VivirMiVida.wav";
            //esta jala en ocasiones
        }else{
            this.PatnToFile= "C:\\Users\\J4\\IdeaProjects\\Shazam\\src\\WithoutYou.wav";
            //esta tampoco jala
        }




        File AudioFile = new File(this.PatnToFile);


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream in;

        //entrada de audio
        try {
            audioInputStream = AudioSystem.getAudioInputStream(AudioFile);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        format = audioInputStream.getFormat();

        //System.out.println("Fftsv\n"+format.toString());

        TargetDataLine targetLine = null;

        try {

            targetLine = (TargetDataLine) AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, format));
            targetLine.open(format);

        } catch (LineUnavailableException ex) {
            //System.out.println("Error");
        }catch (Exception ex){

        }

        //buffer=new Byte[targetLine.getBufferSize()];
        targetLine.start();


        byte[] data = new byte[1024 * format.getSampleSizeInBits() / sR];
//        System.out.println(1024 * format.getSampleSizeInBits() / sR);




        try {
            in = new BufferedInputStream(new FileInputStream(AudioFile));
            int read;
            while ((read = in.read(data)) > 0) {
                out.write(data, 0, read);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        data = out.toByteArray();

//        System.out.println("Data="+data.length);

        //funcion que hace el for y lo mete en Buffer
        while(rangoMayor<data.length){
        //while(true){
            //System.out.println("Entro al while");


            for(int i=rangoMenor;i<rangoMayor;i++){
                buffer[rangoMayor-i]=data[i];

            }

            wave = convert(wave, buffer, sample_size*2);
            for (int j = 0; j < wave.length; ++j) {
                //System.out.println(wave[j]);
                c[j] = new Complex(wave[j], 0.0);
            }

            c = fft(c);
            /*for (int i = 0; i < c.length; i++) {
                System.out.println(c[i].toString());
            }*/

            for (int j = 1; j < freq.length; ++j) {
                freq[j] = c[j].abs() / 131072 / this.sample_size * 12;
                //freq[j] = c[j].abs() / 65536.0 * 2.0 / this.sample_size * (12);
            }


            //frecuencia pico
            //System.out.println(rangoMenor%2==1);
            if(rangoMenor%2==1){
               // System.out.println("Entro al if");
                this.peak(freq, 1.0, this.bandwidth, "Hz");
            }else{
                //System.out.println("Entro al else");
            }


            //this.w.update();
            final double[] signal = freq;
            final double amplitude = 1.0;

            final int bandwidth = this.bandwidth;
            //final float n = -this.w.alto / 4.0f;
            //this.w.getClass();





            //rangoMenor=rangoMenor+18097;
            rangoMenor=rangoMenor+36195;


            //rangoMayor=rangoMayor+18097;
            rangoMayor=rangoMayor+36195;
            //1,3,5,7,9,11,13,15,17,19,
            //rangoMenor=rangoMenor+sample_size*2;
            //rangoMayor=rangoMayor+sample_size*2;

        }
        //System.out.println("//////////");
    //    System.out.println("Haciendo merge, fftsv");
        mergesortAmplitud();
      //  System.out.println("Imprimiendo datos, fftsv");
        //imprimeDatos();
        //System.out.println("//////////");
        //mergesortHz();
//        imprimeDatos();

    }

    public double[][] getDatos(){
        return this.datos;
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

            //System.out.println(max+"                                        pmax: "+pmax+"                    PM:      "+puntoMaximo);
            //System.out.println(max+"+"+posDatos+"-----------");
            if(posDatos<datos.length) {
                datos[posDatos][0] = max-max%10;
                datos[posDatos][1] = puntoMaximo;//System.out.println("Max:" + datos[posDatos][0] + ", puntotMaximo:" + datos[posDatos][1]);
                //System.out.println(datos[posDatos][0]+"+"+posDatos);
                posDatos++;

            }else {

                resize();
                //System.out.println("Nuevo datos:"+datos.length);
                datos[posDatos][0] = max-max%10;
                datos[posDatos][1] = puntoMaximo;
                //System.out.println(datos[posDatos][0]+"+"+posDatos+"//////");
                //System.out.println("Max:" + datos[posDatos][0] + ", puntotMaximo:" + datos[posDatos][1]);
                posDatos++;
            }

            //w.drawLabel(Color.gray, Integer.toString(max), 0, 0);
        }
    }

    public void imprimeDatos(){
        for(int i=0;i<posDatos;i++){
            //System.out.println("Max:" + datos[i][0] + ", puntotMaximo:" + datos[i][1]);
            //System.out.println(datos[i][0]);
            System.out.println(datos[i][0]);
        }
    }

    public String getPatnToFile(){
        return this.PatnToFile;
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


    public static void main(String[] args) {
        FFTSV prueba=new FFTSV(1);

    }

}
