public class Nodo {
    String caracter;
    Integer numero;
    String coincidencia;


    public Nodo(String valor, int numero, String coincidencia){
        this.caracter=valor;
        this.numero=numero;
        this.coincidencia=coincidencia;
    }




    public String toString(){
        String regreso= (this.caracter+"..."+this.coincidencia);
        return regreso;
    }

    public int verificarUltimo(String valor){
        if(caracter.equals(valor)){
            return this.numero;
        }
        return -1;
    }

}
