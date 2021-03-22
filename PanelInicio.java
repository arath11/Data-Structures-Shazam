
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class PanelInicio {
    private SoundSimple2 micro = new SoundSimple2();
    private int posMax = 0;
    private BufferedReader songs;

    //aqui leemos las canciones del src
    public PanelInicio() {
        try {
            this.songs = new BufferedReader(new FileReader("src/dataSongs.txt"));
            this.process();
            this.songs.close();
        } catch (FileNotFoundException var2) {
            var2.printStackTrace();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public int getPosMax() {
        return this.posMax;
    }

    public void process() throws IOException {
        int current = 0;
        int temp = 0;

        String linea;
        for(int cont = 0; (linea = this.songs.readLine()) != null; ++cont) {
            StringTokenizer st = new StringTokenizer(linea, ",");
            Hashtable<Double, String> guardada = new Hashtable();

            for(int i = 0; i < 2500; ++i) {
                String keyStr = st.nextToken();
                Double keyNum = Double.parseDouble(keyStr);
                if (!guardada.containsKey(keyNum)) {
                    guardada.put(keyNum, keyStr);
                }
            }

             current = this.setHash(guardada);
            if (cont == 0) {
                temp = current;
                this.posMax = cont + 1;
            } else if (temp < current) {
                this.posMax = cont + 1;
                temp = current;
            }
        }

    }

    public int setHash(Hashtable guardados) {
        int buenas = 0;

        for(int i = 0; i < 20; ++i) {
            for(int j = 0; j < 20; ++j) {
                if (i != j && guardados.containsKey(this.micro.getDatos()[i][0] * 100000.0D + this.micro.getDatos()[j][0])) {
                    ++buenas;
                }
            }
        }

        return buenas;
    }
}
