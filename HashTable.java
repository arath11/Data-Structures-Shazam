import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class HashTable {
    private Hashtable<Integer, List<String>> anidada = new Hashtable<Integer, List<String>>();

    public void ingresar (int key, String dato) {
        List<String> tmp= new ArrayList<>();
        if(anidada.containsKey(key)){
            tmp= anidada.get(key);
            tmp.add(dato);
            anidada.put(key,tmp);
        }else {
            tmp.add(dato);
            anidada.put(key,tmp);
        }
    }

    public List<String> exits(Integer data) {
        List<String> tmp= new ArrayList<>();
        if(anidada.containsKey(data)){
            tmp=anidada.get(data);
           /* for(int i=0;i<tmp.size();i++){
                System.out.println("Key "+data+" info:"+tmp.get(i));
            }*/
        }
        return tmp;
    }

    public void imprimir(List<String> data){
        for(int i=0;i<data.size();i++){
            System.out.println(data.get(i));
        }
    }


    public static void main(String[] args) {
        HashTable prueba=new HashTable();
        prueba.ingresar(30,"Katy");
        prueba.ingresar(30,"no type");
        prueba.ingresar(30,"aaaaaa");
        prueba.ingresar(10,"bbbb");


        prueba.imprimir(prueba.exits(10));
        prueba.imprimir(prueba.exits(30));

    }

}
