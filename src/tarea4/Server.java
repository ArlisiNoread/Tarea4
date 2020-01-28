package tarea4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {

    private int puerto;

    public Server(int puerto) {
        this.puerto = puerto;
    }

    public void iniciarServidor() {
        ServerSocket server = null;
        OutputStream out = null;
        DataOutputStream dos = null;
        InputStream inputStream = null;
        DataInputStream in = null;
        try {
            server = new ServerSocket(this.puerto);
            System.out.println("Servidor inicializado en espera de peticiones");
            Socket socket = server.accept();

            //String mensaje = in.readUTF();
            System.out.println("Conexión recibida");
            System.out.println("");
            System.out.println(socket);

            inputStream = socket.getInputStream();
            in = new DataInputStream(inputStream);

            System.out.println("Cliente Get: ");
            Scanner s = new Scanner(socket.getInputStream());
            while (s.hasNextLine()) {
                String temp = s.nextLine();
                System.out.println(temp);
                if (temp.isEmpty()) {
                    break;
                }
            }
            System.out.println("Enviando mensaje de respuesta");

            out = socket.getOutputStream();
            dos = new DataOutputStream(out);

            File archivo = new File("./html/index.xhtml");
            FileInputStream archivoDeEntrada = new FileInputStream(archivo);
            int cantidadDeBytes = (int) archivo.length();
            byte[] archivoEnBytes = new byte[cantidadDeBytes];
            archivoDeEntrada.read(archivoEnBytes);

            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("Content-Length: " + cantidadDeBytes + "r\n");
            dos.writeBytes("Content-Type: text/html\r\n\r\n");
            dos.write(archivoEnBytes, 0, cantidadDeBytes);
            
            System.out.println("Mensaje enviado");
            archivoDeEntrada.close();
            dos.close();
            out.close();
            in.close();
            inputStream.close();
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println("Servidor finalizado");
        }

    }

}
