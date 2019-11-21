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

public class FFT2 {

    private static int sample_rate = 48;//8 es mala
    //16 medio jala
    //32 es mas decente
    //44100 es la normal
    //48000 es la mejor
    //muchos datos
    private static AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private static AudioInputStream audioInputStream;
    private static AudioFormat format;
    final static int W = 1024*2;

    private Complex[] c;

    public FFT2(){
        //Entrada de archivo
        String PatnToFile = "C:\\Users\\J4\\IdeaProjects\\Shazam\\src\\TestMono.wav";
        File AudioFile = new File(PatnToFile);


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
        System.out.println("Fft2\n"+format.toString());
        TargetDataLine targetLine = null;

        try {

            targetLine = (TargetDataLine) AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, format));
            targetLine.open(format);

        } catch (LineUnavailableException ex) {
            System.out.println("Error");
        }catch (Exception ex){

        }

        //buffer=new Byte[targetLine.getBufferSize()];
        targetLine.start();


        byte[] data = new byte[W * format.getSampleSizeInBits() / sample_rate];
        System.out.println(W * format.getSampleSizeInBits() / sample_rate);

        double[] inbuf = new double[W];
        double[] fftbuf = new double[W];



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

        System.out.println("Data="+data.length);

        decode(data, inbuf);
        //System.out.println("Data="+data.length);
        //System.out.println("inbuf="+inbuf.length);


        for (int i = 0; i < 10000; i++) {
            System.out.println(data[i]);
        }




        fft(inbuf, fftbuf);
    }

    public void run(){

    }

    public static void main(String[] args) {
        FFT2 prueba=new FFT2();
        prueba.run();

    }


    public static void decode(byte[] data1, double[] output) {
        assert data1.length == 2 * output.length;
        for (int i = 0; i < output.length; i++) {
            //System.out.println(data1[i]);
//            System.out.println((((0xFF & data1[2 * i + 1]) << 8) | (0xFF & data1[2 * i])));
            //corta los ultimos y los remplaza con los primeros 8 bits
            output[i] = (short) (((0xFF & data1[2 * i + 1]) << 8) | (0xFF & data1[2 * i]));
            output[i] /= Short.MAX_VALUE;
       //     System.out.println(output[i]);
        }

        System.out.println("//////////////////////////////////////////////////");
        /*for (int i = 0; i < output.length; i++) {
            System.out.println(output[i]);
        }*/


    }

    public static void fft(final double[] inbuf2, double[] fftbuf2) {
        assert inbuf2.length == 2 * fftbuf2.length;
        int n = inbuf2.length;
        double ld = Math.log(n) / Math.log(2.0);

        if (((int) ld) - ld != 0) {
            System.out.println("The number of elements is not a power of 2.");
        }
        int nu = (int) ld;
        int n2 = n / 2;
        int nu1 = nu - 1;
        double[] xReal = new double[n];
        double[] xImag = new double[n];

        double tReal, tImag, p, arg, c, s;

        double constant;
        if (true){
            constant = -2 * Math.PI;
        }

        for (int i = 0; i < n; i++) {
            xReal[i] = inbuf2[i];
            xImag[i] = fftbuf2[i];
        }

        int k = 0;
        for (int l = 1; l <= nu; l++) {
            while (k < n) {
                for (int i = 1; i <= n2; i++) {
                    p = bitreverseReference(k >> nu1, nu);
                    arg = constant * p / n;
                    c = Math.cos(arg);
                    s = Math.sin(arg);
                    tReal = xReal[k + n2] * c + xImag[k + n2] * s;
                    tImag = xImag[k + n2] * c - xReal[k + n2] * s;
                    xReal[k + n2] = xReal[k] - tReal;
                    xImag[k + n2] = xImag[k] - tImag;
                    xReal[k] += tReal;
                    xImag[k] += tImag;
                    //                  System.out.println(p);
                    k++;
                }
                k += n2;
            }
            k = 0;
            nu1--;
            n2 /= 2;
        }

        k = 0;
        int r;
        while (k < n) {
            r = bitreverseReference(k, nu);
            if (r > k) {
                tReal = xReal[k];
                tImag = xImag[k];
                xReal[k] = xReal[r];
                xImag[k] = xImag[r];
                xReal[r] = tReal;
                xImag[r] = tImag;
            }
            k++;
            //System.out.println(k);
        }

        double[] newArray = new double[xReal.length * 2];
        double radice = 1 / Math.sqrt(n);
        for (int i = 0; i < newArray.length; i += 2) {
            int i2 = i / 2;
            newArray[i] = xReal[i2] * radice;
          //     System.out.println(xReal[i2] * radice);
            newArray[i + 1] = xImag[i2] * radice;
       //         System.out.println(xImag[i2] * radice);
        }

        for (int i = 0; i < newArray.length; i++) {
         //  System.out.println(newArray[i]);
        }

    }

    private static int bitreverseReference(int j, int nu) {
        int j2;
        int j1 = j;
        int k = 0;
        for (int i = 1; i <= nu; i++) {
            j2 = j1 / 2;
            k = 2 * k + j1 - 2 * j2;
            j1 = j2;
        }
        return k;
    }
}