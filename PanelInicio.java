import javax.swing.*;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Hashtable;

public class PanelInicio extends JPanel implements Runnable {
    private Image fondo;
    private SoundSimple2 micro;
    private int[] match=new int[7];


    private FFTSV base;

    private Thread hilo;
    private int posMax=0;
    boolean bandera;



    public PanelInicio() {

        super();
        this.setPreferredSize(new Dimension(400, 400));
        this.fondo = new ImageIcon("C:\\Users\\J4\\IdeaProjects\\Shazam\\src\\Gabo.jpeg").getImage();
        //arrancarPrograma();


        //System.out.println("Ya hizo el micro ");
        this.micro = new SoundSimple2();
        this.micro.bandera = true;
        this.micro.run();
        //System.out.println("Haciendo hash");
        int current=0,
                temp=0;
        for (int k=0; k<10; k++){

            Hashtable guardada = new Hashtable();
            this.base = new FFTSV(k+1);
            //llena el hash con los dato sde la coompu
          //  System.out.println("Datos compu en hash ");
            for (int j = 0; j < 50; j++) {
                for (int i = 0; i < 50; i++) {
                    if (i != j) {
                        guardada.put(this.base.getDatos()[j][0] * 100000 + this.base.getDatos()[i][0], this.base.getPatnToFile());
         //               System.out.println(this.base.getDatos()[j][0] * 100000 + this.base.getDatos()[i][0]);

                    }

                }
            }
            current=setHash(guardada);
            System.out.println(current);
            System.out.println(temp+"temp");
            if (k==0){
                temp=current;
            }else if (temp<current){
                this.posMax=k+1;
                temp=current;
            }

            hilo = new Thread(this);
            bandera=false;
            System.out.println(k+1);

        }/*
        if (this.posMax==1){
            //System.out.println("Esta es la cancion..."+posMax);

        }else if (this.posMax==2){
            System.out.println("Sunflower");
            //System.out.println("Esta es la cancion..."+posMax);

        }else if (this.posMax==3){
            System.out.println("Bidi Bidi bon bom ");
            //System.out.println("Esta es la cancion..."+posMax);

        }else if (this.posMax==4){
            //System.out.println("Esta es la cancion..."+posMax);
            System.out.println("Firework");
        }else if (this.posMax==5){
            //System.out.println("Esta es la cancion..."+posMax);
            System.out.println("Uptown Funk");
        }else if (this.posMax==6){
            //System.out.println("Esta es la cancion..."+posMax);
            System.out.println("Secreto De Amor");
        }else if (this.posMax==7){
            //System.out.println("Esta es la cancion..."+posMax);
            System.out.println("You Better Run ");
        }else if (this.posMax==8){
            //System.out.println("Esta es la cancion..."+posMax);
            System.out.println("7 years");
        } else if (this.posMax==9) {
            //System.out.println("Esta es la cancion..."+posMax);
            System.out.println("Vivir Mi Vida");
        }else{
            //System.out.println("Esta es la cancion..."+posMax);
            System.out.println("Without You");
        }*/
    }


    public void hacerNuevoHilo(){
        hilo = new Thread(this);
        hilo.start();
    }
    /*public void arrancarPrograma(){
        this.micro=new SoundSimple2();
        correrMicrofono();

        micro.mergesortAmplitud();
        System.out.println("Comenzando Merge Amplitud en Sound Simple");
        System.out.println("Imprimiendo Datos, soundSimple");
        micro.imprimeDatos();




        Hashtable guardada=new Hashtable();
        this.base=new FFTSV(1);




        System.out.println("Datos compu a la hash ");

        for (int j=0; j<50; j++){
            for (int i=0; i<50; i++){
                System.out.println("qqqq");
                if (i!=j){

                    guardada.put(this.base.getDatos()[j][0]*10000+this.base.getDatos()[i][0],this.base.getPatnToFile());
                    System.out.println(1);
                    System.out.println(this.base.getDatos()[j][0]*10000+this.base.getDatos()[i][0]);
                }
            }
        }

        System.out.println("Tamaño del hash"+guardada.size());
        System.out.println("ya termine de guardar los datos en la hash");


        setHash(guardada);
    }*/

    public int setHash(Hashtable guardados){
       // System.out.println("Empezando set");
        String a="";
        int buenas=0;
        for (int i=0; i<50;i++){
            for (int j=0; j<50; j++){
                if (i!=j){
       //             System.out.println(this.micro.getDatos()[i][0]*100000+this.micro.getDatos()[j][0]);
                    if(guardados.containsKey(this.micro.getDatos()[i][0]*100000+this.micro.getDatos()[j][0])){
                        buenas++;
                    }
                   /* if (buenas==10){
                        return true;
                    }*/
                }
            }
        }
        return buenas;
    }

    public boolean comparar(Hashtable guardados){
        System.out.println("Arracando");
        this.base.imprimeDatos();
        int buenas=0;
        for (int i=0; i<10;i++){
            for (int j=0; j<10; j++){
                if(i!=j){
                    if (guardados.containsKey(this.base.getDatos()[i][0]*100000+this.base.getDatos()[j][0])){
                        buenas++;
                        System.out.println("bien");
                    }
                }

            }
        }
        System.out.println("Buenas"+buenas);
        if (buenas==10){
            return true;
        }else{
            return false;
        }

    }


    public void correrMicrofono(){
        this.micro.bandera=true;
        this.micro.run();
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(this.fondo, 0, 0, this.getWidth(), this.getHeight(), this);

        if (this.posMax==1){
            //System.out.println("Esta es la cancion..."+posMax);
            g.drawString("100 Hz",200,200);
        }else if (this.posMax==2){
            //System.out.println("Esta es la cancion..."+posMax);
            g.drawString("Sunflower",200,200);
        }else if (this.posMax==3){
            System.out.println("Bidi Bidi bon bom ");
            //System.out.println("Esta es la cancion..."+posMax);
            g.drawString("Selena Bidi Bidi Bon Bom",200,200);
        }else if (this.posMax==4){
            //System.out.println("Esta es la cancion..."+posMax);
            g.drawString("Fireworks ",200,200);
            System.out.println("Firework");
        }else if (this.posMax==5){
            //System.out.println("Esta es la cancion..."+posMax);
            g.drawString("Uptown Funk",200,200);

        }else if (this.posMax==6){
            //System.out.println("Esta es la cancion..."+posMax);
            g.drawString("Secreto De Amor",200,200);

        }else if (this.posMax==7){
            //System.out.println("Esta es la cancion..."+posMax);
            g.drawString("You Better Run",200,200);

        }else if (this.posMax==8){
            //System.out.println("Esta es la cancion..."+posMax);
            g.drawString("7 years",200,200);
        } else if (this.posMax==9) {
            //System.out.println("Esta es la cancion..."+posMax);
            g.drawString("Vivir Mi Vida",200,200);
            System.out.println();
        }else{
            //System.out.println("Esta es la cancion..."+posMax);
            g.drawString("Without You",200,200);

        }


    }

    @Override
    public void run() {
        float seg = 0;

        this.micro = new SoundSimple2();
      //  System.out.println("Haciendo el run, de soundsimple");
        this.micro.run();


        /*while(bandera){
            try {
                Thread.sleep(50);
                this.micro.run();
                System.out.println(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            seg+=100;


            if (seg==15000){
                bandera=false;
                System.out.println("Termine");

            }
        }*/

        hilo.stop();
    }
}
