import Constants.HttpResponses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;


public class Server {
    public Map<String, Handler> routes;
    private ServerSocket serverSocket;
    private ExecutorService pool;

    public void start(int port) throws IOException {
        pool = Executors.newFixedThreadPool(10);
        serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            Runnable runnable = () -> {
                try {
                    handleClient(socket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };
            pool.execute(runnable);
        }

    }


    private void handleClient(final Socket socket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        ) {
            System.out.println("connected");
            Map<String, String> headers = new HashMap<>();
            HttpResponses response = HttpResponses.OK;
            String[] requestLine = in.readLine().split(" ", 3);
            String method = requestLine[0];
            String message = "";
            String path = requestLine[1];
            String version = requestLine[2];
            System.out.println("yee: " + "method: " + method + "path: " + path + "version: " + version);

            String line;
            System.out.println("---- RAW REQUEST START ----");
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                String[] headerParts = line.split(":", 2);
                headers.put(headerParts[0], headerParts[1]);
                System.out.println(line);
            }
            if (path.equals("/")) {
                message = "Home";
            } else if (path.equals("/yee")) {
                message = "YEE";
            } else {
                response = HttpResponses.NOT_FOUND;
                message = "NOT FOUND";
            }

            sendResponse(out, response, message);
            socket.close();

        }
    }

    private void sendResponse(PrintWriter out, HttpResponses response, String message) {
        out.println("HTTP/1.1 " + response.getCode() + " " + response.getDescription());
        out.println("Content-Type: text/plain");
        out.println("Content-Length: " + message.length());
        out.println();
        out.println(message);
    }


}

