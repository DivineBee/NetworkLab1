package com.beatrix.data.link;

/**
 * @author Beatrice V.
 * @created 15.10.2020 - 10:48
 * @project NetworkLab1
 */
import com.beatrix.data.DataManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Any thread works either using Runnable interface or Thread class. Programmer can enhance behavior of thread by making
 * own class with required behavior by implementing interface Runnable or extending Thread. The only function that will
 * require implementation or inheritance is run().
 * In this class the only required change was to be able to append string to the thread.
 */
public class MyRunnable implements Runnable {
    //  route that will be processed by this thread
    private final String routeToHandle;

    //  constructor requires route that will be handled
    MyRunnable(String routeToHandle) {
        this.routeToHandle = routeToHandle;
    }

    @Override
    public void run() {
        // Create mapper for primary deserialization of json
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Perform get request to current route and performs first partial deserialization
            Route routeData = objectMapper.readValue(PageInformation.getPageContent(routeToHandle), Route.class);

            // Check if data from page contains links
            if (routeData.getLink() != null && routeData.getLink().size() > 0) {
                // Transform all links to arrayList
                List<String> innerList = new ArrayList<>(routeData.getLink().values());

                // Try to append found links to the queue of unhandled routes
                for (int j = 0; j < innerList.size(); j++) {
                    // Try appending until offer() returns true(successful appending)
                    String fullpath = PageInformation.url + innerList.get(j);
                    PageInformation.getConcurrentRoutes().offer(fullpath);
                }
            }

            // If it is not home address then add all data from page to database of json, xml and csv data.
            if (!(PageInformation.url + PageInformation.homeAddress).equals(routeToHandle)) {
                DataManager.getDataFromServer().add(routeData);
            }

            // Decrease counter of active threads by one finalizing work of thread
            synchronized (PageInformation.getActiveThreadsCount()) {
                PageInformation.getActiveThreadsCount().getAndDecrement();
            }
        } catch (IOException e) {
            System.err.println("Can't read the links.\n" + e);
        }
    }
}

