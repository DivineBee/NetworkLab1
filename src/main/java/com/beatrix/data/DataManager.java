package com.beatrix.data;
/**
 * This class manages and extracts all the data.
 * From getters by specific name, id to getting the commands
 * from user.
 * @author Beatrice V.
 * @created 26.09.2020 - 12:09
 * @project NetworkLab1
 */
import com.beatrix.data.link.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class DataManager {
    // not processed data, directly from server(unserialized)
    public static ArrayList<Route> dataFromServer = new ArrayList<>();

    // deserialized data from server and stored in local storage
    public static ArrayList<Data> localDataStorage = new ArrayList<>();

    // getters for the local and server storage
    public static ArrayList<Data> getLocalDataStorage() {
        return localDataStorage;
    }
    public static ArrayList<Route> getDataFromServer() { return dataFromServer; }

    private static Data getDataById(String id) {
        for (Data data : localDataStorage) {
            if(data.getId() != null && data.getId().equalsIgnoreCase(id)) {
                return data;
            }
        }
        System.out.println("Error: No such ID in system");
        return null;
    }

    private static Data getDataByFirstName(String first_name){
        for (Data data : localDataStorage) {
            if(data.getFirst_name() != null && data.getFirst_name().equalsIgnoreCase(first_name)) {
                return data;
            }
        }
        System.out.println("Error: No such first name in system");
        return null;
    }

    private static Data getDataByLastName(String last_name){
        for (Data data : localDataStorage) {
            if(data.getLast_name() != null && data.getLast_name().equalsIgnoreCase(last_name)) {
                return data;
            }
        }
        System.out.println("Error: No such last name in system");
        return null;
    }

    private static Data getDataByBitcoinAddress(String bitcoin_address){
        for (Data data : localDataStorage) {
            if(data.getBitcoin_address() != null && data.getBitcoin_address().equalsIgnoreCase(bitcoin_address)) {
                return data;
            }
        }
        System.out.println("Error: No such bitcoin address in system");
        return null;
    }

    private static Data getDataByEmail(String email){
        for (Data data : localDataStorage) {
            if(data.getEmail() != null && data.getEmail().equalsIgnoreCase(email)) {
                return data;
            }
        }
        System.out.println("Error: No such Email in system");
        return null;
    }

    private static Data getDataByIp(String ip_address) {
        for (Data data : localDataStorage) {
            if(data.getIp_address() != null && data.getIp_address().equalsIgnoreCase(ip_address)) {
                return data;
            }
        }
        System.out.println("Error: No such IP address in system");
        return null;
    }

    private static Data getDataByCardNumber(String card_number){
        for (Data data : localDataStorage) {
            if(data.getCard_number() != null && data.getCard_number().equalsIgnoreCase(card_number)) {
                return data;
            }
        }
        System.out.println("Error: No such card number in system");
        return null;
    }

    private static Data getDataByFullName(String full_name){
        for (Data data : localDataStorage) {
            if(data.getFull_name() != null && data.getFull_name().equalsIgnoreCase(full_name)) {
                return data;
            }
        }
        System.out.println("Error: No such full name in system");
        return null;
    }

    private static Data getDataByEmployeeId(String employee_id){
        for (Data data : localDataStorage) {
            if(data.getEmployee_id() != null && data.getEmployee_id().equalsIgnoreCase(employee_id)) {
                return data;
            }
        }
        System.out.println("Error: No such employee ID in system");
        return null;
    }

    private static Data getDataByUsername(String username){
        for (Data data : localDataStorage) {
            if(data.getUsername() != null && data.getUsername().equalsIgnoreCase(username)) {
                return data;
            }
        }
        System.out.println("Error: No such username in system");
        return null;
    }

    private static Data getDataByCreatedAccount(String accountData){
        for (Data data : localDataStorage) {
            if(data.getCreated_account_data() != null && data.getCreated_account_data().equalsIgnoreCase(accountData)) {
                return data;
            }
        }
        System.out.println("Error: No such account data in system");
        return null;
    }

    public static <T> List<T> getGlobCommand(String command) {
        List<T> listOfGlobs = new ArrayList<>();

        List<T> localList = (List<T>) getLocalDataStorage();;

        if (command.contains("*")) {
            String lastWord = command.replaceAll("^.*?(\\w+)\\W*$", "$1");

            String[] cutWord = command.split("[ ]+");
            String penultimateWord = cutWord[cutWord.length - 2];
            String asteriskGlob = lastWord.substring(0);

            for (T l : localList){
                if(command.contains(penultimateWord) && command.contains(asteriskGlob)){
                    //localList.getDataById(asteriskGlob);
                    listOfGlobs.add(l);
                }
            }
            System.out.println("globs- " + listOfGlobs.toString());
        }
        return listOfGlobs;
    }


    public static Data readClientCommand(String command) {
        //another way instead of regex - command.substring(command.lastIndexOf(" ")+1);
        String lastWord = command.replaceAll("^.*?(\\w+)\\W*$", "$1");
        //for globbing commands(in progress):
        /*String[] array = {"*", "{", "}", "[", "]", "-", "(", "^"};

        boolean foundAtLeastOne = false;
        for (String word : array) {
            if (command.indexOf(word) > 0) {
                foundAtLeastOne = true;
                System.out.println("Word: " + word + " is found");
                break;
            }
        }
        System.out.println("Found at least one : " + foundAtLeastOne);

        if (foundAtLeastOne){
            String glob = lastWord;
            Pattern pattern = Pattern.compile(glob);
            Matcher match = pattern.matcher(localDataStorage);

            List<String> matchingGlobs = new ArrayList<String>();
            while (match.find()) {
                System.out.println("Found value: " + match.group(0)); //get localdatastorage
                matchingGlobs.add(match.group(0));
            }

            for(Data data : localDataStorage){
                if(data.contains(matchingGlobs));
            }
            //System.out.println(data.substring(Integer.parseInt(command.replaceAll("^.*?(\\w+)\\W*$", "$1"))) + matchingGlobs);
        } else {*/
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

        else {
            System.out.println("Check your spelling.");
            return null;
        }
    }

    public static Data getRandomData() {
        return localDataStorage.get(new Random().nextInt(localDataStorage.size() - 1));
    }

    /**
     * Process all data and write it in local storage using DataConverter class' methods
     * @return Nothing.
     */
    public static void addDataFromServer() {
        if (dataFromServer != null && dataFromServer.size() > 0) {
            for (Route route : dataFromServer) {
                if (route.getMimeType() == null)
                    localDataStorage.addAll(Objects.requireNonNull(DataConverter.getDataFromJson(route)));
                else if (route.getMimeType().equals("application/xml"))
                    localDataStorage.addAll(Objects.requireNonNull(DataConverter.getDataFromXml(route)));
                else if (route.getMimeType().equals("application/x-yaml"))
                    localDataStorage.addAll(Objects.requireNonNull(DataConverter.getDataFromYaml(route)));
                else if (route.getMimeType().equals("text/csv"))
                    localDataStorage.addAll(DataConverter.getDataFromCsv(route));
                else
                    System.err.println("Unknown type of data: " + route.getMimeType());
            }
        }
    }
}

