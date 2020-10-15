# Network Docker Lab1
## Table of contents

* [Technologies](#technologies)
* [How to run](#how-to-run)
* [Description](#description)
* [Code examples](#code-examples)
* [Objectives](#objectives)
* [Status](#status)

## Technologies
Maven  
Docker   
Language: Java  
IDE: IntelliJ IDEA  

## How to run
1. Import project as maven project to your IDE
2. Select JRE(1.8)
3. Run com.beatrix.client_server.Server()
4. Run com.beatrix.ClientRunner()

## Description
I divided the laboratory work in 3 parts and inserted the packages accordingly to the class' focus.  
First step was retrieving the token from the register link -> see _PageInfo_ class - method _getToken()_.  
Second step was accessing the next link which required registration and to traverse all its links(routes)  
and retrieve data and if other links are available. For reading the data I used the Jackson library  
which permitted me to read the json contents from the page, I also used it to read the xml, yaml, csv  
data from page but on that later on... The reading of the page was simple - now the hardest part. How to  
retrieve the nested links? First I created a method which searched by regex (which was not an option) then  
I remodeled the method to create and see how the recursive one will be doing, still wasn't happy because  
that is not the best solution. After some long days of thinking and studying I made up an iterative approach  
which used the _BlockingQueue_ for storing the routes and has a thread per route traversal(thrown in some  
comments to be explicit), so my implementation of _run()_ using iterative method of finding routes(can be found  
in class _MyRunnable_ and used in method _findRoute()_ in class _PageInformation_:   
```java
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
```
After doing the hardest part, came into play the simpler part of retrieving the data from page and put it in  
some local storage. I created a package called com.beatrix.data used only for work with data, there I have the  
conversion using Jackson which used my classes' getters and setters with appropriate field and mapped them.  
Then stored every data I get, into an arrayList of _Data_ called localDataStorage. Then I created the _Server_  
which will transfer records from DB by request from the _Client_. After that I created a class which will  
handle client requests and communication between server and client. In the _Server_ I instantiated a ThreadPool  
which will hold a thread per client connection this offers the possibility to connect multiple Clients.

## Code Examples
Gets contents of the page using apache httpClient:
```java
public static String getPageContent(String address) {
        String pageContent = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(address);
        request.addHeader("X-Access-Token", token);

        try {
            HttpResponse response = httpClient.execute(request);
            ResponseHandler<String> handler = new BasicResponseHandler();
            pageContent = handler.handleResponse(response);
            System.out.println(pageContent);
            httpClient.close();
        } catch (IOException e){
            System.out.println("Can't get home page content.");
        }
        return pageContent;
    }
```
Reads the command from user takes last word and then returns data containing the passed word:
```java
if (command.contains("id")) return getDataById(lastWord);
        else if (command.contains("first_name")) return getDataByFirstName(lastWord);
        else if (command.contains("last_name")) return getDataByLastName(lastWord);
        else if (command.contains("bitcoin_address")) return getDataByBitcoinAddress(lastWord);
        else if (command.contains("email")) return getDataByEmail(lastWord);
        else if (command.contains("ip_address")) return getDataByIp(lastWord);
        else if (command.contains("card_number")) return getDataByCardNumber(lastWord);
        else if (command.contains("full_name")) return getDataByFullName(lastWord);
        else if (command.contains("employee_id")) return getDataByEmployeeId(lastWord);
        else if (command.contains("username")) return getDataByUsername(lastWord);
        else if (command.contains("created_account_data")) return getDataByCreatedAccount(lastWord);
```
Example of deserialization from json:
```java
static List<Data> getDataFromJson (Route data) {
        try{
            return new ObjectMapper().readValue(data.getData().replaceAll(",]", "]"), new TypeReference<ArrayList<Data>>(){});
        } catch (JsonProcessingException jpe) {
            System.err.println("Can't extract from JSON." + jpe);
        } return null;
    }
```
For more check the classes and inside methods -> the naming of the classes and packages speak for them self.

## Objectives
List of features ready and TODOs for future development
#### Ready:
* Pull a docker container from the registry
* access the root route of the server and find a way to /register
* The accessed token from /register route must be put in a HTTP header under the key X-Access-Token
* Extract data from data key and get next links from link key traversing the API
* Access token has a timeout of 20 seconds, and you are not allowed to get another token every  
  time you access a different route. So, one register per program run
* Once you fetch all the data, convert it to a common representation  
* The final part of the lab is to make a concurrent TCP server, serving the fetched content,  
  that will respond to (mandatory) a column selector message, like `SelectColumn column_name`

#### In progress:
* `SelectFromColumn column_name glob_pattern` message for TCP server

#### Optional tasks:
* Implementing own thread pools
* Dispatching data to a different process pool/dedicated processes for  
  conversion while the fetching is running (can use queue)
* Anything else, turn on your imagination

## Status
Project is: _in progress_
