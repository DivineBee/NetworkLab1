package com.beatrix.client_server;
/**
 * @author Beatrice V.
 * @created 30.09.2020 - 22:48
 * @project NetworkLab1
 */

import com.beatrix.data.DataManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private static Socket clientDialog;

    public ClientHandler(Socket client) {
        ClientHandler.clientDialog = client;
    }

    /**
     * overrides run method from the interface Runnable where server can get
     * requests from him.
     * @exception IOException if nothing to read write
     * @exception InterruptedException which may occur if threads are interrupted
     */
    @Override
    public void run() {
        try {
            // channels for getting requests and sending data to the client:
            DataOutputStream output = new DataOutputStream(clientDialog.getOutputStream());
            DataInputStream input = new DataInputStream(clientDialog.getInputStream());
            // waiting for requests from client then responds
            while (!clientDialog.isClosed()) {
                String command = input.readUTF();
                System.out.println("CLIENT COMMAND --- " + command);

                if (command.equalsIgnoreCase("quit")) {
                    System.out.println("CLIENT requested closing ...");
                    output.writeUTF("SERVER reply --- " + command);
                    Thread.sleep(1000);
                    break;
                } else if(command.equalsIgnoreCase("select random")){
                    output.writeUTF("Request result = " + DataManager.getRandomData());
                    System.out.println("SUCCESS");
                } else if(command.contains("select")){
                    command = command.trim().replaceAll(" +", " ").toLowerCase();
                    //String input = command.replaceAll("^.*?(\\w+)\\W*$", "$1");;
                    output.writeUTF("SERVER reply - " + DataManager.readClientCommand(command));
                    System.out.println("SUCCESS");
                } else {
                    output.writeUTF("Request result = FAILED: check your command input!");
                    System.out.println("PASSED");
                }
                output.flush();
            }
            input.close();
            output.close();
            clientDialog.close();
            System.out.println("CLIENT disconnected.");
        } catch (IOException | InterruptedException e) {
            System.out.println("CLIENT disconnected.");
        }
    }
}

