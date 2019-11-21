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

public class FFT {

    private static int BITS_IN_BYTE = 8;
    private static AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private static AudioInputStream audioInputStream;
    private static AudioFormat format;
    final static int W = 1024;

    public static void main(String[] args) {
        String PatnToFile = "C:\\Users\\J4\\IdeaProjects\\Shazam\\src\\Test.wav";
        File AudioFile = new File(PatnToFile);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream in;

        try {
            audioInputStream = AudioSystem.getAudioInputStream(AudioFile);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        format = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Error");
        }

        TargetDataLine line = null;
        try {

            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);

        } catch (LineUnavailableException ex) {
            System.out.println("Error");
        }

        line.start();
        byte[] data = new byte[W * format.getSampleSizeInBits() / BITS_IN_BYTE];
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




        decode(data, inbuf);
        System.out.println("Data="+data.length);
        System.out.println("Data="+data.length);
        System.out.println("inbuf="+inbuf.length);
      /*  for (int i = 0; i < data.length; i++) {
            System.out.println(data[i]);
        }*/


        fft(inbuf, fftbuf);

    }

    public static void decode(byte[] input, double[] output) {
        assert input.length == 2 * output.length;
        for (int i = 0; i < output.length; i++) {
            //System.out.println((0xFF & input[2 * i + 1]) << 8);
            output[i] = (short) (((0xFF & input[2 * i + 1]) << 8) | (0xFF & input[2 * i]));
            output[i] /= Short.MAX_VALUE;
           //System.out.println(output[i]);
        }
    }

    public static void fft(final double[] inputReal, double[] inputImag) {
        assert inputReal.length == 2 * inputImag.length;
        int n = inputReal.length;
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
            xReal[i] = inputReal[i];
            xImag[i] = inputImag[i];
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
         //   System.out.println(xReal[i2] * radice);
            newArray[i + 1] = xImag[i2] * radice;
        //    System.out.println(xImag[i2] * radice);
        }

        for (int i = 0; i < newArray.length; i++) {
          System.out.println(newArray[i]);
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