
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Intro extends JPanel implements MouseListener {
    private Image fondo;
    private int num = 0;
    private int tamaño;
    private int x;
    private int estado = 0;
    private int ancho;
    private boolean stop = false;
    private String nombre;

    public Intro(int alto) {
        this.ancho = alto;
        this.setPreferredSize(new Dimension(this.ancho, this.ancho));
        this.fondo = (new ImageIcon("src/istockphoto-527879048-640x640.jpg")).getImage();
        this.addMouseListener(this);
        this.setFocusable(true);
    }

    public void pintaStatus(Graphics g) {
        g.setColor(Color.WHITE);
        if (this.estado == 0) {
            g.drawString("Start", (int)((double)this.ancho * 0.45D), (int)((double)this.ancho * 0.555D));
        } else if (this.estado == 1) {
            g.drawString("Analizando", 310, 366);
        } else {
            g.drawString(this.nombre, 270, 366);
        }

    }

    public void pintaCirculo(Graphics g, int ancho) {
        g.setColor(Color.BLUE);
        g.setFont(this.setFont(20));
        g.fillOval(this.x, this.x, ancho, ancho);
        this.pintaStatus(g);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.fondo, 0, 0, this.getWidth(), this.getHeight(), this);
        g.setFont(this.setFont(40));
        g.setColor(Color.white);
        g.drawString("Team G & J", 253, 100);
        if (this.estado == 0) {
            this.pintaBoton(g, 0.5D);
        } else if (this.estado == 1) {
            this.pintaCirculo(g, this.tamaño);
        } else {
            this.pintaStatus(g);
        }

    }

    public void pintaBoton(Graphics g, double y) {
        g.setColor(Color.GRAY);
        g.drawRect((int)((double)this.ancho * 0.25D), (int)((double)this.ancho * y), 380, 50);
        this.pintaStatus(g);
    }

    public Font setFont(int size) {
        Font nuevoFont = new Font("Arial", 1, size);
        this.getFontMetrics(nuevoFont);
        return nuevoFont;
    }

    public void mouseClicked(MouseEvent e) {
        if ((int)((double)this.ancho * 0.25D) < e.getX() && e.getX() < (int)((double)this.ancho * 0.25D) + 380 && (int)((double)this.ancho * 0.5D) < e.getY() && e.getY() < (int)((double)this.ancho * 0.5D) + 50) {
            Thread hilo = new Thread() {
                public void run() {
                    Intro.this.estado = 1;
                    boolean flag = true;
                    float var2 = 0.0F;

                    while(!Intro.this.stop) {
                        try {
                            Intro.this.tamaño = 150;
                            Intro.this.x = 285;
                            Intro.this.repaint();
                            Thread.sleep(200L);
                            Intro.this.x = 260;
                            Intro.this.tamaño = 200;
                            Intro.this.repaint();
                            Thread.sleep(200L);
                            Intro.this.x = 235;
                            Intro.this.tamaño = 250;
                            Intro.this.repaint();
                            Thread.sleep(200L);
                            Intro.this.x = 260;
                            Intro.this.tamaño = 200;
                            Intro.this.repaint();
                            Thread.sleep(200L);
                        } catch (InterruptedException var4) {
                            System.out.println("No pude despertar!!");
                        }
                    }

                }
            };
            Thread paralelo = new Thread() {
                public void run() {
                    PanelInicio pi = new PanelInicio();
                    //todo cambuiar respuesta
                    if (pi.getPosMax() == 1) {
                        Intro.this.nombre = "Sunflower";
                    } else if (pi.getPosMax() == 2) {
                        Intro.this.nombre = "Bidi bidi bom bom";
                    } else if (pi.getPosMax() == 3) {
                        Intro.this.nombre = "Firework";
                    } else if (pi.getPosMax() == 4) {
                        Intro.this.nombre = "Adios amor/secreto de amor";
                    } else if (pi.getPosMax() == 5) {
                        Intro.this.nombre = "Secreto de amor";
                    } else if (pi.getPosMax() == 6) {
                        Intro.this.nombre = "You better run";
                    } else if (pi.getPosMax() == 7) {
                        Intro.this.nombre = "Sweat dreams";
                    } else {
                        Intro.this.nombre = "Vivir mi vida";
                    }

                    Intro.this.stop = true;
                    Intro.this.estado = 2;
                    Intro.this.repaint();
                }
            };
            hilo.start();
            paralelo.start();
        } else if ((int)((double)this.ancho * 0.25D) < e.getX() && e.getX() < (int)((double)this.ancho * 0.25D) + 380 && (int)((double)this.ancho * 0.65D) < e.getY() && e.getY() < (int)((double)this.ancho * 0.65D) + 50) {
            this.estado = 0;
            this.stop = false;
            System.gc();
            this.repaint();
        }

    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
