import java.io.File;

public class SongReader {
    String [] canciones= new String[1];

    public void conseguirArchivos(String ruta) {
        conseguirArchivosPrivado(ruta);
    }

    public void conseguirArchivos() {
        conseguirArchivosPrivado("src/Canciones");
    }

    private void conseguirArchivosPrivado(String ruta) {
        // String [] archivosRegreso=new String[0];
        File rutaArchivo = new File(ruta);
        if (rutaArchivo.exists()) {
            File[] archivos = rutaArchivo.listFiles();
            if (!(archivos == null)) {
                for (int i = 0; i < archivos.length; i++) {
                    if (archivos[i].isFile()) {
                        //System.out.println(archivos[i].getName());
                        agregarCanciones(archivos[i].getName());
                    } else if (archivos[i].isDirectory()) {
                        conseguirArchivosPrivado(archivos[i].getAbsolutePath());
                    }
                }
            } else {
                System.out.println("Esta vacia ");
            }
        } else {
            System.out.println("El directori o la ruta no existeixen.");
        }
    }

    private void agregarCanciones(String nuevaCancion) {
        //System.out.println("Entreee"+canciones.length);
        String[] tmp=new String[this.canciones.length+1];
            for(int i=0;i<this.canciones.length;i++){
                tmp[i]=canciones[i];
            }
            //String[] remover=nuevaCancion.split("\\.");
            //tmp[canciones.length]=remover[0];
            tmp[canciones.length]=nuevaCancion;
            canciones=tmp;
    }

    public void imprimir(){
        for(int i=1;i<this.canciones.length;i++){
            System.out.println(canciones[i]);
        }
    }

    public void removerNull() {
        String[] tmp=new String[canciones.length-1];
        for(int i=1;i<this.canciones.length;i++){
            tmp[i-1]=canciones[i];
        }
        canciones=tmp;
    }




    public String[] correr(){
        SongReader p =new SongReader();
        p.conseguirArchivos("src/Canciones");
        p.removerNull();
        p.imprimir();

        return this.canciones;
    }


    public static void main(String[] args) {
        SongReader pruebas =new SongReader();
        pruebas.correr();

        /*SongReader pruebas =new SongReader();
        //pruebas.conseguirArchivos("Insetar ruta aqui");
        pruebas.conseguirArchivos("src/Canciones");

         */
    }
}
