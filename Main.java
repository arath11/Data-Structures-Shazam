import javax.swing.*;

public class Main extends JFrame {

    public Main(int ventana) {
        super("Inicio");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        if(ventana==1) {
            PanelInicio pd = new PanelInicio();
            this.add(pd);
        }else if(ventana==2){

            Intro pd = new Intro(720);
            this.add(pd);
        }
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Main m=new Main(2);
    }
}
