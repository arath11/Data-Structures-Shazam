public class Merge {
    private double datos[][];
    public Merge(){
        datos=new double[10][2];
        datos[0][1]=10;
        datos[1][1]=9;
        datos[2][1]=8;
        datos[3][1]=7;
        datos[4][1]=6;
        datos[5][1]=5;
        datos[6][1]=4;
        datos[7][1]=3;
        datos[8][1]=2;
        datos[9][1]=1;



        datos[0][0]=1;
        datos[1][0]=5;
        datos[2][0]=6;
        datos[3][0]=10;
        datos[4][0]=8;
        datos[5][0]=7;
        datos[6][0]=4;
        datos[7][0]=9;
        datos[8][0]=2;
        datos[9][0]=3;
    }

    public  void imprimeArreglo() {
        for(int i=0;i<this.datos.length;i++) {
            System.out.println(this.datos[i][0]+","+this.datos[i][1]);
        }
    }

    public   void mergesortAmplitud() {
        mergesortAmplitud(this.datos,0,this.datos.length-1);
    }


    private  void mergesortAmplitud(double[][] datos, int ini, int fin) {
        if(ini<fin) {
            int mid=(ini+fin)/2;
            mergesortAmplitud(datos,ini,mid);
            mergesortAmplitud(datos,mid+1,fin);
            mergeAmplitud(datos,ini,fin);
        }

    }


    private  void mergeAmplitud(double[][] datos, int ini, int fin) {
        int mid=(ini+fin)/2;
        int indiceArray1=mid-ini+1;
        int indiceArray2=fin-mid;
        double[][] arrayIzquierda=new double[indiceArray1][2];
        double[][] arrayDerecha=new double[indiceArray2][2];
        for(int i =0;i<indiceArray1;i++) {
            arrayIzquierda[i][0]=datos[i+ini][0];
            arrayIzquierda[i][1]=datos[i+ini][1];
        }
        for(int j =0;j<indiceArray2;j++) {
            arrayDerecha[j][0]=datos[j+mid+1][0];
            arrayDerecha[j][1]=datos[j+mid+1][1];
        }
        int i,j,k;
        i=j=0;
        k=ini;
        while(i<indiceArray1 && j<indiceArray2) {
            if(arrayIzquierda[i][1]>arrayDerecha[j][1]) {
                datos[k++]=arrayIzquierda[i++];
            }else {
                datos[k++]=arrayDerecha[j++];
            }
        }
        while(i<indiceArray1) {
            datos[k++]=arrayIzquierda[i++];
        }
        while(j<indiceArray2) {
            datos[k++]=arrayDerecha[j++];
        }
    }


    public void mergesortHz() {
        mergesortHz(this.datos,0,this.datos.length-1);
    }


    private void mergesortHz(double[][] datos, int ini, int fin) {
        if(ini<fin) {
            int mid=(ini+fin)/2;
            mergesortHz(datos,ini,mid);
            mergesortHz(datos,mid+1,fin);
            mergeHz(datos,ini,fin);
        }

    }


    private void mergeHz(double[][] datos, int ini, int fin) {
        int mid=(ini+fin)/2;
        int indiceArray1=mid-ini+1;
        int indiceArray2=fin-mid;
        double[][] arrayIzquierda=new double[indiceArray1][2];
        double[][] arrayDerecha=new double[indiceArray2][2];
        for(int i =0;i<indiceArray1;i++) {
            arrayIzquierda[i][0]=datos[i+ini][0];
            arrayIzquierda[i][1]=datos[i+ini][1];
        }
        for(int j =0;j<indiceArray2;j++) {
            arrayDerecha[j][0]=datos[j+mid+1][0];
            arrayDerecha[j][1]=datos[j+mid+1][1];
        }
        int i,j,k;
        i=j=0;
        k=ini;
        while(i<indiceArray1 && j<indiceArray2) {
            if(arrayIzquierda[i][0]>arrayDerecha[j][0]) {
                datos[k++]=arrayIzquierda[i++];
            }else {
                datos[k++]=arrayDerecha[j++];
            }
        }
        while(i<indiceArray1) {
            datos[k++]=arrayIzquierda[i++];
        }
        while(j<indiceArray2) {
            datos[k++]=arrayDerecha[j++];
        }
    }




    public static void main(String[] args){
        Merge prueba = new Merge();
        prueba.imprimeArreglo();
        System.out.println();
        prueba.mergesortHz();
        prueba.imprimeArreglo();
        System.out.println();
        prueba.mergesortAmplitud();

        prueba.imprimeArreglo();
    }

}
