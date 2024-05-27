package test_server_project;

import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_ADDRESS = "192.168.100.2"; // Change this to the server's IP address if needed
    private static final int SERVER_PORT = 555;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             OutputStream output = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true);
             InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input));
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            new Thread(new MessageReader(reader)).start();

            String userInput;
            System.out.println("Connected to the chat server. Type messages:");

            while ((userInput = consoleReader.readLine()) != null) {
                writer.println(userInput);
            }
        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }

    private static class MessageReader implements Runnable {
        private BufferedReader reader;

        public MessageReader(BufferedReader reader) {
            this.reader = reader;
        }

        public void run() {
            String serverMessage;
            try {
                while ((serverMessage = reader.readLine()) != null) {
                    System.out.println(serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
