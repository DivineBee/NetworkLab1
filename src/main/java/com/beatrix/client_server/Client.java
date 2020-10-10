package com.beatrix.client_server;
/**
 * @author Beatrice V.
 * @created 30.09.2020 - 17:34
 * @project NetworkLab1
 */
import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            new Client();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Client() throws InterruptedException {
        try(Socket socket = new Socket("localhost", 2020);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());) {
            System.out.println("CLIENT is active.\nWaiting for requests: ");

            while (!socket.isOutputShutdown()) {
                if (reader.ready()) {
                    String clientCommand = reader.readLine();
                    output.writeUTF(clientCommand);
                    // clear buffer and redirect data to destination
                    output.flush();
                    System.out.println("CLIENT sent request ---> " + clientCommand);
                    Thread.sleep(1000);

                    if (clientCommand.equalsIgnoreCase("quit")) {
                        System.out.println("CLIENT DISCONNECT.");
                        Thread.sleep(2000);
                        if (input.available() != 0) {
                            System.out.println("READING...");
                            String in = input.readUTF();
                            System.out.println(in);
                        }
                        break;
                    }
                    System.out.println("CLIENT is waiting for data from server...");
                    Thread.sleep(1000);

                    if (input.available() != 0) {
                        System.out.println("READING...");
                        String in = input.readUTF();
                        System.out.println(in);
                    }
                }
            }
            System.out.println("Closing connections and channels.");
        } catch (IOException e) {
            System.out.println("Interrupted");
            e.printStackTrace();
        }
    }
}
