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
    private BufferedReader in;

    public void start(int port) throws IOException {

        serverSocket = new ServerSocket(port);
        while (true) {
            clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/plain");
            out.println("Content-Length: 11");
            out.println();
            out.println("Hello World");


            System.out.println("connected");
            String line;
            System.out.println("---- RAW REQUEST START ----");
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                System.out.println(line);
            }
            clientSocket.close();
        }
    }

}
