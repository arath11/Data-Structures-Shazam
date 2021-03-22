import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Grafo {
    private ArrayList<ArrayList<Nodo>> grafo;
    private Stack pila;
    public String[] dato;

    public  String entrada;

    public Grafo(int v){
        grafo = new ArrayList<ArrayList<Nodo> >(v);
        for (int i = 0; i < v; i++){
            grafo.add(new ArrayList<Nodo>());
        }
        pila=new Stack();
        pila.add("I");
    }


    public boolean probar(){
        for(int i=0; i<dato.length;i++){
            boolean tmp1=false;
            for(int j=0;j<grafo.get(0).size() && !tmp1;j++) {
                Nodo tmp = grafo.get(0).get(j);
                if(comprobar(tmp,dato[i])){
                    tmp1=true;
                }
            }
            if(tmp1==false){
                return false;
            }
        }

        if(pila.peek().equals("F")){
            return true;
        }else {return false;}

    }

    private boolean comprobar(Nodo comprobar,String dato) {
        //caso inicial
        if(dato.equals(comprobar.caracter) && pila.peek()=="I"){
            // System.out.println("1");
            pila.pop();
            pila.add("F");
            //System.out.println(dato);
            pila.add(dato);
            return true;
        }
        //caso encontrar par
        else if(dato.equals(comprobar.caracter) && pila.peek()==comprobar.caracter){
            //System.out.println("2");
            pila.pop();
            return true;
        }
        //caso agregar
        else if(dato.equals(comprobar.caracter) && (pila.peek()=="(" || pila.peek()=="[" ||pila.peek()=="{")){
            if(dato.equals("(") || dato.equals("[") || dato.equals("{")){
                //System.out.println("3");
                //  System.out.println(dato);
                pila.add(dato);
                return true;
            }else {
                if(dato.equals(")") && pila.peek()=="("){
                    //  System.out.println("2");
                    pila.pop();
                    return true;
                }else if(dato.equals("}") &&  pila.peek()=="{"){

                    //System.out.println("2");
                    pila.pop();
                    return true;
                }else if(dato.equals("]") &&  pila.peek()=="[") {

                    //System.out.println("2");
                    pila.pop();
                    return true;
                }
                else if((dato.equals(")") || (dato.equals("]") || (dato.equals("}")) && pila.peek()=="I"))){
                    return false;
                }
                return false;


            }

        }        //caso final
        else if(dato.equals(comprobar.caracter) && pila.peek()=="F"){
            //System.out.println("4");
            return true;
        }
        return false;

    }


    public void añadir(String dato1, String dato2) {
        //inicio, entra el dato y tiene coincidencia con vacio
        grafo.get(0).add(new Nodo(dato1, 0,"I"));
        //ecnotrar par entra del dato2 y si es
        grafo.get(0).add(new Nodo(dato2,0,dato1));
        //agregar
        grafo.get(0).add(new Nodo(dato1,0,dato1));
        //final
        grafo.get(0).add(new Nodo("",0,"F"));
    }



    public void imprimir(){
        for(int i=0;i<grafo.size();i++){
            System.out.println("Vertice:" + i);
            for (int j = 0; j < grafo.get(i).size(); j++) {
                System.out.println("  "+grafo.get(i).get(j).toString()+",");
            }
            System.out.println();
        }
    }
    public void entrada() {
        Scanner entradaScanner = new Scanner(System.in);
        System.out.println("Ingrese la expresion:\n");
        entrada = entradaScanner.nextLine();
        System.out.println(entrada);
        dato=separador(entrada);
        dato=agregarA(dato);
    }

    private String[] agregarA(String[] cadena) {
        String[] tmp = new String[cadena.length + 1];
        for (int i = 0; i < cadena.length; i++) {
            tmp[i] = cadena[i];
        }
        tmp[cadena.length] = "";
        return tmp;
    }
        private String[] separador(String a){
        String[] salida = a.split("");
        return salida;
    }


    public static void main(String[] args) {
        Grafo prueba=new Grafo(1);
        prueba.añadir("(",")");
        prueba.añadir("[","]");
        prueba.añadir("{","}");

        prueba.entrada();

        //prueba.dato= new String[]{")","[","]",")",""};
        for(int i=0;i<prueba.dato.length;i++){
            System.out.println(prueba.dato[i]);
        }
        //prueba.imprimir();
        if(prueba.probar()){
            System.out.println("Esta cadena es valida");
        }else{
            System.out.println("Esta cadena no es valida ");
        }


    }

}
