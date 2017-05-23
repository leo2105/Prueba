
import java.util.Scanner;

class Cliente50{
    TCPClient50 mTcpClient;
    Scanner sc;
    public static void main(String[] args)  {
        Cliente50 objcli = new Cliente50();
        objcli.iniciar();
    }
    void iniciar(){
       new Thread(
            new Runnable() {

                @Override
                public void run() {
                    mTcpClient = new TCPClient50("127.0.0.1",
                        new TCPClient50.OnMessageReceived(){
                            @Override
                            public void messageReceived(String message){
                                ClienteRecibe(message);
                            }
                        }
                    );
                    mTcpClient.run();                   
                }
            }
        ).start();
        //---------------------------
       
        String salir = "n";
        sc = new Scanner(System.in);
        System.out.println("Cliente bandera 01");
        while( !salir.equals("s")){
            salir = sc.nextLine();
            ClienteEnvia(salir);
        }
        System.out.println("Cliente bandera 02");
    
    }
    void ClienteRecibe(String llego){
        System.out.println("CLINTE50 El mensaje::" + llego);
            System.out.println("HOLA" + llego.trim());
            if ( llego.trim().contains("TRA")){
                String arrayString[] = llego.split("\\s+");
                double rpta = 0;
                if (arrayString[1].equals("TRA")){
                    System.out.println("TCPClient recibo trabajo:");
                    int dat = Integer.parseInt(arrayString[0]);
                    rpta = funcion(dat);
                    System.out.println("TCPClient resulado:" + rpta);                                                    
                }
                ClienteEnvia("La Respuesta es:" + rpta);
            }
    }
    void ClienteEnvia(String envia){
        if (mTcpClient != null) {
            mTcpClient.sendMessage(envia);
        }
    }
    double funcion(int fin){
        double sum = 0;
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        for(int j = 0; j<=fin;j++ ){
            sum = sum + Math.sin(j*Math.random());
        }
        initConcurrent();
        time_end = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion es: " + (time_end - time_start));
        return sum;
    }

    public class claVec{
        int tam;
        int[] dat;

        public claVec(int tam_){
            tam = tam_;
            dat = new int[tam_];
            for(int i = 0; i < tam; ++i)
                dat[i] = (int)(Math.random() * 101);
        }

        public void imprimirVec(){
            System.out.print("Vector es: ");
            for (int i = 0; i < tam; ++i)
                System.out.print(dat[i] + " ");
            System.out.println();
        }

        public int[] getVec(){
            return dat;
        }
    }

    public void initConcurrent(){
        System.out.println("Creamos el Hilo");
        int nHil = 4, nVec = 8;
        claVec[] DATA = new claVec[nVec];
        Thread[] hilos = new Thread[nHil];

        for (int i = 0; i < nVec; i++){
            DATA[i] = new claVec(8);
            DATA[i].imprimirVec();
        }

        for (int i = 0; i < nHil; i++){
            hilos[i] = new Thread (new hilouno(i, hilos, DATA[i*2], DATA[i*2 + 1]));
            hilos[i].start();
        }
    }

    public class hilouno implements Runnable{
        int nombre;
        Thread[] hermanos;
        claVec v1,v2;
        boolean listo;
        
        public hilouno(int _nombre, Thread[] _hermanos, claVec _v1, claVec _v2){
            nombre = _nombre;
            hermanos = _hermanos;
            v1 = _v1; v2 = _v2;
        }

        public claVec Oper(){
            int tam = v1.tam;
            claVec v = new claVec(tam);
            for(int i = 0; i < tam; ++i)
                v.dat[i] = v1.dat[i] + v2.dat[i];
            return v;
        }

        public void run(){
            int tam = v1.tam;
            claVec aux = Oper();
            aux.imprimirVec();
            System.out.println("En el HILO: " + nombre);
        }
    }
}
