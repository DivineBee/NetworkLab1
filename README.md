# Network Docker Lab1

## Table of contents

* [Technologies](#technologies)
* [How to run](#how-to-run)
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
3. Run Main()

## Code Examples
Show examples of usage:
`

    CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url + registerAddress);
    
            try {
                HttpResponse response = client.execute(request);
                ResponseHandler<String> handler = new BasicResponseHandler();
                String text = handler.handleResponse(response);
                token = text.split("\"")[3];
                System.out.println(token);
            } catch (IOException e) {
                System.out.println("Couldn't get token!" + e);
            }
`

## Objectives
List of features ready and TODOs for future development
#### Ready:
* Pull a docker container from the registry
* access the root route of the server and find a way to /register
* The accessed token from /register route must be put in a HTTP header under the key X-Access-Token

#### To-do list:
* Extract data from data key and get next links from link key traversing the API
* Access token has a timeout of 20 seconds, and you are not allowed to get another token every  
  time you access a different route. So, one register per program run
* Once you fetch all the data, convert it to a common representation  
* The final part of the lab is to make a concurrent TCP server, serving the fetched content,  
  that will respond to (mandatory) a column selector message, like `SelectColumn column_name`,  
  and (optional) `SelectFromColumn column_name glob_pattern`
  
#### Optional tasks:
* `SelectFromColumn column_name glob_pattern` message for TCP server
* Implementing own thread pools
* Dispatching data to a different process pool/dedicated processes for  
  conversion while the fetching is running (can use queue)
* Anything else, turn on your imagination

## Status
Project is: _in progress_
