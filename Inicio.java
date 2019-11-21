import javax.swing.JFrame;
import java.awt.*;

public class Inicio extends JFrame {
    public Inicio(){
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(new PanelInicio());
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Inicio ventana=new Inicio();
    }
}
