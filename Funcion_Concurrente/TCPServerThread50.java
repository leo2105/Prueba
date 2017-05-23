/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//import OnMessageReceived;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.net.Socket;


public class TCPServerThread50 extends Thread{
    
    private Socket client;
    private TCPServer50 tcpserver;
    private int clientID;                 
    private boolean running = false;
    public PrintWriter mOut;
    public BufferedReader in;
    TCPServer50.OnMessageReceived messageListener = null;
    private String message;
    TCPServerThread50[] cli_amigos;

    public TCPServerThread50(Socket client_, TCPServer50 tcpserver_, int clientID_,TCPServerThread50[] cli_ami_) {
        this.client = client_;
        this.tcpserver = tcpserver_;
        this.clientID = clientID_;
        this.cli_amigos = cli_ami_;
    }
    
     public void trabajen(int cli){      
         mOut.println("TRABAJAMOS ["+cli+"]...");
    }
    
    public void run() {
        running = true;
        try {
            try {               
                boolean soycontador = false;                
                mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                System.out.println("TCP Server"+ "C: Sent.");
                messageListener = tcpserver.getMessageListener();
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while (running) {
                    message = in.readLine();
                    
                    if (message != null && messageListener != null) {
                        messageListener.messageReceived(message);
                    }
                    
                    //eco
                    System.out.println("Eco de:" + clientID +" dice:" + message);
                    
                    //Verifica quien envia trabajo y quien es el trabajador
		    //Envia el trabajo el cliente 1 y 2
                    if (clientID == 1 && !soycontador){
                        String mitabla = "SoyClienteEnviaTrabajo";
                        mOut.println( mitabla + "soy el numero:" + clientID);
                        soycontador = true;
                    }else if ( !soycontador){                    
                        String mitabla = "SoyTrabajador";
                        mOut.println( mitabla + "soy el numero:" + clientID);
                        soycontador = true;

                    }
                    //El cliente 1 envia el comando "TRA"
                    if (clientID == 1 && message.trim().contains("TRA")){

                        tcpserver.sendMessageTCPServerWorker("100000 TRA BAJEN_SLAVE");
                        System.out.println("Envio el mensaje trabajo: 100000 TRA BAJEN_SLAVE");
                    }
                    message = null;
                }
                System.out.println("RESPONSE FROM CLIENT"+ "S: Received Message: '" + message + "'");
            } catch (Exception e) {
                System.out.println("TCP Server"+ "S: Error"+ e);
            } finally {
                client.close();
            }

        } catch (Exception e) {
            System.out.println("TCP Server"+ "C: Error"+ e);
        }
    }
    
    public void stopClient(){
        running = false;
    }
    
    public void sendMessage(String message){//funcion de trabajo
        if (mOut != null && !mOut.checkError()) {
            mOut.println( message);
            mOut.flush();
        }
    }
    
}
