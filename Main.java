
import javax.swing.JFrame;

public class Main extends JFrame {
    public Main() {
        super("Inicio");
        this.setDefaultCloseOperation(3);
        this.add(new Intro(720));
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
