package com.beatrix.client_server; /**
 * @author Beatrice V.
 * @created 04.10.2020 - 22:23
 * @project NetworkLab1
 */

import com.beatrix.data.Data;
import com.beatrix.data.DataManager;
import com.beatrix.data.link.PageInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static com.beatrix.data.DataManager.localDataStorage;

public class Server {
    // Thread pool for each Client if 6 clients then 6 threads which can run and so on.
    private static ExecutorService serverExecutor = Executors.newFixedThreadPool(6);
    public static void main(String[] args) {
        PageInfo.getToken();
        BlockingQueue<String> localConcurent = PageInfo.getConcurrentRoutes();
        localConcurent.offer(PageInfo.url + PageInfo.homeAddress);
        ArrayList<String> availableRoutes = new ArrayList<>();
        availableRoutes.add(PageInfo.url + PageInfo.homeAddress);
        PageInfo.findRoute(availableRoutes);

        while(Thread.activeCount() > 2) { }
        DataManager.addDataFromServer();

        for(Data currentData : localDataStorage) {
            System.out.println(currentData);
        }
        try {
            // launch socket for client communication
            ServerSocket serverSocket = new ServerSocket(2020);
            System.out.println("SERVER is starting up, run CLIENT.");

            while(!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                serverExecutor.execute(new ClientHandler(client));
                System.out.println("Connection accepted.");
            }
            // shutdown() will allow previously submitted tasks to execute before terminating
            serverExecutor.shutdown();
        } catch (IOException e) {
            System.out.println("Error in setting server: " + e);
        }
    }
}
