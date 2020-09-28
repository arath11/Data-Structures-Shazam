
import java.io.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.DataLine.Info;

//computadora

public class FFTComputer {
    private static Type fileType;
    private static AudioInputStream audioInputStream;
    private static AudioFormat format;
    private int sR = 44100;
    private int sS;
    private int bw;
    private int sampleRate;
    private int sampleSize;
    private int bandwidth;
    private byte[] buffer;
    private double[] onda;
    private Complex[] c;
    private double[] freq;
    private TargetDataLine targetLine;
    private String direccion;
    private double[][] datos;
    private int posDatos;

    private boolean hasBeenRestarted=false;

    private SongReader lector = new SongReader();


    public FFTComputer() {
        this.sR = 18;
        this.sS = 2048;
        this.bw = 20000;
        this.sampleRate = 44100;
        this.sampleSize = 2048;
        this.bandwidth = 20000;
        this.buffer = new byte[this.sampleSize * 2];
        this.onda = new double[this.buffer.length / 2];
        this.c = new Complex[this.buffer.length / 2];
        this.freq = new double[this.c.length * this.bandwidth / this.sampleRate];
        int rangoMenor = 1;
        int rangoMayor = this.sampleSize * 2;
        this.datos = new double[700][2];


        this.posDatos = 0;
        this.lector.conseguirArchivos();
        this.lector.removerNull();
        //this.lector.imprimir();


        //todo poner el for

        if(hasBeenRestarted==false){
            reiniciarArchivo();
            hasBeenRestarted=true;
        }

        for (int a = 0; a < this.lector.canciones.length; a++) {
            this.direccion = ("src/Canciones/" + this.lector.canciones[a]);
            //System.out.println(this.direccion);

            File AudioFile = new File(this.direccion);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try {
                audioInputStream = AudioSystem.getAudioInputStream(AudioFile);
            } catch (UnsupportedAudioFileException var12) {
                var12.printStackTrace();
            } catch (IOException var13) {
                var13.printStackTrace();
            }

            format = audioInputStream.getFormat();
            TargetDataLine targetLine = null;

            try {
                targetLine = (TargetDataLine) AudioSystem.getLine(new Info(TargetDataLine.class, format));
                targetLine.open(format);
            } catch (LineUnavailableException var10) {
            } catch (Exception var11) {
            }

            targetLine.start();
            byte[] data = new byte[1024 * format.getSampleSizeInBits() / this.sR];

            int bandwidth;
            try {
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(AudioFile));

                while ((bandwidth = in.read(data)) > 0) {
                    out.write(data, 0, bandwidth);
                }
                out.flush();
            } catch (FileNotFoundException var14) {
                var14.printStackTrace();
            } catch (IOException var15) {
                var15.printStackTrace();
            }

            for (data = out.toByteArray(); rangoMayor < data.length; rangoMayor += 36195) {
                for (bandwidth = rangoMenor; bandwidth < rangoMayor; ++bandwidth) {
                    this.buffer[rangoMayor - bandwidth] = data[bandwidth];
                }
                this.onda = convert(this.onda, this.buffer, this.sampleSize * 2);
                for (bandwidth = 0; bandwidth < this.onda.length; ++bandwidth) {
                    this.c[bandwidth] = new Complex(this.onda[bandwidth], 0.0D);
                }
                this.c = fft(this.c);
                for (bandwidth = 1; bandwidth < this.freq.length; ++bandwidth) {
                    this.freq[bandwidth] = this.c[bandwidth].abs() / 131072.0D / (double) this.sampleSize * 12.0D;
                }
                if (rangoMenor % 2 == 1) {
                    this.peak(this.freq, 1.0D, this.bandwidth);
                }
                bandwidth = this.bandwidth;
                rangoMenor += 36195;
            }
            this.mergesortAmplitud();
            System.out.println(this.direccion);
            this.guardarDatos();
        }
    }

    //todo delete this old function
    public FFTComputer(int song) {
        this.sR = 18;
        this.sS = 2048;
        this.bw = 20000;
        this.sampleRate = 44100;
        this.sampleSize = 2048;
        this.bandwidth = 20000;
        this.buffer = new byte[this.sampleSize * 2];
        this.onda = new double[this.buffer.length / 2];
        this.c = new Complex[this.buffer.length / 2];
        this.freq = new double[this.c.length * this.bandwidth / this.sampleRate];
        int rangoMenor = 1;
        int rangoMayor = this.sampleSize * 2;
        this.datos = new double[700][2];


        this.posDatos = 0;

        if (song == 1) {
            this.direccion = "src/Sunflower.wav";
        } else if (song == 2) {
            this.direccion = "src/Bidi.wav";
        } else if (song == 3) {
            this.direccion = "src/Firework.wav";
        } else if (song == 4) {
            this.direccion = "src/AdiosAmor.wav";
        } else if (song == 5) {
            this.direccion = "src/SecretoDeAmor.wav";
        } else if (song == 6) {
            this.direccion = "src/YouBetterRun.wav";
        } else if (song == 7) {
            this.direccion = "src/SweetDreams.wav";
        } else {
            this.direccion = "src/VivirMiVida.wav";
        }

        //todo poner el for
        File AudioFile = new File(this.direccion);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            audioInputStream = AudioSystem.getAudioInputStream(AudioFile);
        } catch (UnsupportedAudioFileException var12) {
            var12.printStackTrace();
        } catch (IOException var13) {
            var13.printStackTrace();
        }

        format = audioInputStream.getFormat();
        TargetDataLine targetLine = null;

        try {
            targetLine = (TargetDataLine)AudioSystem.getLine(new Info(TargetDataLine.class, format));
            targetLine.open(format);
        } catch (LineUnavailableException var10) {
        } catch (Exception var11) {
        }

        targetLine.start();
        byte[] data = new byte[1024 * format.getSampleSizeInBits() / this.sR];

        int bandwidth;
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(AudioFile));

            while((bandwidth = in.read(data)) > 0) {
                out.write(data, 0, bandwidth);
            }
            out.flush();
        } catch (FileNotFoundException var14) {
            var14.printStackTrace();
        } catch (IOException var15) {
            var15.printStackTrace();
        }

        for(data = out.toByteArray(); rangoMayor < data.length; rangoMayor += 36195) {
            for(bandwidth = rangoMenor; bandwidth < rangoMayor; ++bandwidth) {
                this.buffer[rangoMayor - bandwidth] = data[bandwidth];
            }
            this.onda = convert(this.onda, this.buffer, this.sampleSize * 2);
            for(bandwidth = 0; bandwidth < this.onda.length; ++bandwidth) {
                this.c[bandwidth] = new Complex(this.onda[bandwidth], 0.0D);
            }
            this.c = fft(this.c);
            for(bandwidth = 1; bandwidth < this.freq.length; ++bandwidth) {
                this.freq[bandwidth] = this.c[bandwidth].abs() / 131072.0D / (double)this.sampleSize * 12.0D;
            }
            if (rangoMenor % 2 == 1) {
                this.peak(this.freq, 1.0D, this.bandwidth);
            }
            bandwidth = this.bandwidth;
            rangoMenor += 36195;
        }
        this.mergesortAmplitud();
    }

    public void reiniciarArchivo() {
        try{
            FileWriter fw=new FileWriter("src/filename.txt");
            fw.write("");
            fw.close();
        }catch (Exception e){
            System.out.println("El archivo no existe");
        }

    }

    public void guardarDatos(){
        try {
            BufferedWriter salida=new BufferedWriter(new FileWriter("src/filename.txt",true));
            salida.write(this.direccion+"\n");
            for(int j = 0; j < this.posDatos; ++j) {
                for (int i = 0; i < this.posDatos; ++i) {
                    if (i != j) {
                        double var10001 = this.datos[j][0] * 100000.0D;
                        double[] var10002 = this.datos[i];
                        salida.write(var10001 + var10002[0] + ",");
                    }
                }
            }
            salida.write("\n");
            salida.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public void imprimeDatos() {
        for(int j = 0; j < this.posDatos; ++j) {
            for (int i = 0; i < this.posDatos; ++i) {
                if (i != j) {
                    double var10001 = this.datos[j][0] * 100000.0D;
                    double[] var10002 = this.datos[i];
                    System.out.print(var10001 + var10002[0] + ",");
                }
            }
        }
    }

    public double[][] getDatos() {
        return this.datos;
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
            }

            pico = s / g;
            max = (int)((double)value * pico / (double)signal.length);
            if (this.posDatos < this.datos.length) {
                this.datos[this.posDatos][0] = (double)(max - max % 10);
                this.datos[this.posDatos][1] = puntoMaximo;
                ++this.posDatos;
            } else {
                this.resize();
                this.datos[this.posDatos][0] = (double)(max - max % 10);
                this.datos[this.posDatos][1] = puntoMaximo;
                ++this.posDatos;
            }
        }

    }

    public String getPatnToFile() {
        return this.direccion;
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
        FFTComputer prueba = new FFTComputer();
        /*
        FFTComputer prueba = new FFTComputer(8);
        prueba.imprimeDatos();

 */
    }

    static {
        fileType = Type.WAVE;
    }
}
