import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private String method;
    private String path;
    private String version;
    private String message;
    public void start(int port) throws IOException {

        serverSocket = new ServerSocket(port);
        message = "Hello world";

        while (true) {
            clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            System.out.println("connected");

            String[] requestLine = in.readLine().split(" ",3);
            method = requestLine[0];
            path = requestLine[1];
            version = requestLine[2];
            System.out.println("yee: "+ method+path+version);

            String line;
            System.out.println("---- RAW REQUEST START ----");
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                System.out.println(line);
            }
            out.println(message);
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/plain");
            out.println("Content-Length:" + message.length());
            out.println();
            clientSocket.close();
        }
    }

}
