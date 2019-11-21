import javax.swing.*;
import java.awt.*;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Intro extends JPanel implements MouseListener {
    private Image fondo;
    private int num;
    private int ancho,
            alto;
    public Intro(int alto){
        super();
        this.num=0;
        this.alto = this.ancho = alto;
        this.setPreferredSize(new Dimension(this.ancho ,this.alto));
        this.fondo = new ImageIcon("C:\\Users\\J4\\IdeaProjects\\Shazam\\src\\Gabo.jpeg").getImage();
        addMouseListener(this);
        setFocusable(true);
    }

    public void pintaStatus(int i, Graphics g){
        if (i==0){
            g.drawString("Start ", (int) (this.ancho * .48), (int) (this.alto * .545));
        }else{
            g.drawString("Analizando ", (int) (this.ancho * .48), (int) (this.alto * .545));
        }
    }

    public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.GRAY);
        g.drawImage(this.fondo, 0, 0, this.getWidth(), this.getHeight(), this);
        g.drawRect((int) (this.ancho * .27), (int) (this.alto * .50),380,50);
        g.setFont(setFont(40));
        g.setColor(Color.white);
        g.drawString("Team G & J", 250,100);
        pintaStatus(this.num, g);
    }
    public Font setFont(int size){
        Font nuevoFont = new Font("Arial", Font.BOLD, size);
        FontMetrics metr = this.getFontMetrics(nuevoFont);
        return nuevoFont;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if((int)(this.ancho*.27)<e.getX() && e.getX()<((int)(this.ancho*.27)+380) && (int)(this.ancho*.50)<e.getY() && e.getY()<((int)(this.ancho*.50)+50)){
            this.num=1;
            this.repaint();
            Window w = SwingUtilities.getWindowAncestor(this);
            //w.setVisible(false);
            Main f = new Main(1);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
