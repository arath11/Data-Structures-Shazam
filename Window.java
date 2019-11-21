import java.awt.image.ImageObserver;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.swing.JFrame;


public class Window extends JFrame
{
    protected final Graphics2D g;
    private final BufferedImage frame;
    protected final float ancho;
    protected final float alto;
    private final int fps = 10;

    private double st;
    //SOUND FPS
    private String sfps;



    //protected float opacity;
    protected int segundosGuardados;
    protected int frecuenciaMaxima;
    protected boolean adaptive;

    public Window() {
        this.ancho = (float)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        this.alto = (float)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.st = time();
        //this.opacity = 1.0f;
        //0 es 163
        //1 es 73
        //2 es 37
        //3 es 20
        //4 es 10
        this.segundosGuardados = 4;
        //-2 es 20000hz
        //-1 es 10000hz
        //0 es 5000hz
        //1 es 1000hz
        //2 es 200hz

        this.frecuenciaMaxima = -2;

        this.adaptive = false;
        this.setDefaultCloseOperation(3);
        this.setExtendedState(6);
        this.setUndecorated(true);

        this.getContentPane().setBackground(Color.BLACK);

        this.frame = new BufferedImage((int)this.ancho, (int)this.alto, 1);

        (this.g = (Graphics2D)this.frame.getGraphics()).setBackground(Color.BLACK);

        //this.g.setFont(Font.decode("Monospaced"));

        //ancho  de la lineas
        this.g.setStroke(new BasicStroke(1.0f));

        this.getContentPane().setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, 2), new Point(0, 0), ""));

        this.setVisible(true);

        this.addKeyListener(new KeyAdapter() {

            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == 27) {
                    //System.out.println("Cerrando el programa ");
                    System.exit(0);
                }
                /*if (e.getKeyCode() == 82) {
                    if (Window.this.opacity == 0.8f) {
                        final Window this$0 = Window.this;
                        this$0.opacity += 0.2f;
                    }
                    else if (Window.this.opacity <= 0.9f) {
                        final Window this$2 = Window.this;
                        this$2.opacity += 0.1f;
                    }
                    Window.this.setOpacity(Window.this.opacity);
                }
                if (e.getKeyCode() == 70) {
                    if (Window.this.opacity == 1.0f) {
                        final Window this$3 = Window.this;
                        this$3.opacity -= 0.2f;
                    }
                    else if (Window.this.opacity >= 0.2f) {
                        final Window this$4 = Window.this;
                        this$4.opacity -= 0.1f;
                    }
                    Window.this.setOpacity(Window.this.opacity);
                }*/

                //cantidad de milisegundos guarados

                /*if (e.getKeyCode() == 81) {
                    final Window this$5 = Window.this;
                    --this$5.segundosGuardados;
                }
                if (e.getKeyCode() == 65) {
                    final Window this$6 = Window.this;
                    ++this$6.segundosGuardados;
                }
                if (Window.this.segundosGuardados < -5) {
                    Window.this.segundosGuardados = -5;
                }
                else if (Window.this.segundosGuardados > 4) {
                    Window.this.segundosGuardados = 4;
                }
                //frecuencias
                if (e.getKeyCode() == 87) {
                    final Window this$7 = Window.this;
                    ++this$7.frecuenciaMaxima;
                }
                if (e.getKeyCode() == 83){

                    final Window this$8 = Window.this;
                    --this$8.frecuenciaMaxima;
                }
                if (Window.this.frecuenciaMaxima < -3) {
                    Window.this.frecuenciaMaxima = -3;
                }
                else if (Window.this.frecuenciaMaxima > 2) {

                    Window.this.frecuenciaMaxima = 2;
                }*/
                if (e.getKeyCode() == 69) {

                    Window.this.adaptive = !Window.this.adaptive;
                }
            }
        });
    }

    public void update() {
        this.sfps = "  " + (int)(1.0 / (time() - this.st));

        this.sfps = this.sfps.substring(this.sfps.length() - 3, this.sfps.length());

        //System.out.println(st+" "+sfps);
        this.drawLabel(Color.RED, this.sfps, this.ancho / 2.0f - 21.0f - 1.0f, -this.alto / 2.0f);
        this.getContentPane().getGraphics().drawImage(this.frame, 0, 0, null);
        this.g.clearRect(0, 0, (int)this.ancho, (int)this.alto);
        pause(0.016666666666666666 - time() + this.st);
        this.st = time();
    }

    private static double time() {
        return System.nanoTime() * 1.0E-9;
    }

    private static void pause(final double s) {
        if (s > 0.0) {
            try {
                //Thread.sleep((int)(s * 10000.0));
                Thread.sleep((int)(s * 20000.0));
            }
            catch (Exception ex) {}
        }
    }

    public void drawLine(final Color color, final double x1, final double y1, final double x2, final double y2) {
        this.g.setColor(color);
        this.g.drawLine((int)(this.ancho / 2.0f + x1), (int)(this.alto / 2.0f - y1), (int)(this.ancho / 2.0f + x2), (int)(this.alto / 2.0f - y2));
    }

    public void drawLabel(final Color color, final String s, final double x, final double y) {
        this.g.setColor(color);
        this.g.drawString(s, (int)(this.ancho / 2.0f + x), (int)(this.alto / 2.0f - y) - 2);
    }
}


//protected final int th = 11;
//protected final int tw = 7;