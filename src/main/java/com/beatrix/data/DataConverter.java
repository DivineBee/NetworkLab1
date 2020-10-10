package com.beatrix.data;
/**
 * @author Beatrice V.
 * @created 23.09.2020 - 19:20
 * @project NetworkLab1
 * This class is responsible for extracting and converting XML, YAML, CSV, JSON contents.
 */

import com.beatrix.data.link.Route;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataConverter {
    /**
     * @param data from route which will be converted
     * @return list of data from JSON
     * @exception JsonProcessingException encountered when processing JSON content that are not pure I/O problems
     * @see JsonProcessingException
     * @see ObjectMapper
     */
    static List<Data> getDataFromJson (Route data) {
        try{
            return new ObjectMapper().readValue(data.getData().replaceAll(",]", "]"), new TypeReference<ArrayList<Data>>(){});
        } catch (JsonProcessingException jpe) {
            System.err.println("Can't extract from JSON." + jpe);
        } return null;
    }

    /**
     * @param data from route which will be converted
     * @return list of data from YAML
     * @exception JsonProcessingException encountered when processing JSON content that are not pure I/O problems
     * @see JsonProcessingException
     */
    static List<Data> getDataFromYaml (Route data) {
        try{
            return new YAMLMapper().readValue(data.getData(), new TypeReference<ArrayList<Data>>(){});
        } catch (JsonProcessingException jpe) {
            System.err.println("Can't extract from YAML." + jpe);
        }
        return null;
    }

    /**
     * @param data from route which will be converted
     * @return list of data from XML
     * @exception JsonProcessingException encountered when processing JSON content that are not pure I/O problems
     * @see JsonProcessingException
     */
    static List<Data> getDataFromXml (Route data) {
        try{
            return new XmlMapper().readValue(data.getData(), new TypeReference<ArrayList<Data>>(){});
        } catch (JsonProcessingException jpe) {
            System.err.println("Can't extract from XML." + jpe);
        } return null;
    }

    /**
     * Because CsvMapper didn't fit in here another solution was found -
     * csv file contained specific fields related to username
     * @param data from route which will be converted
     * @return list of data from CSV
     */
    static List<Data> getDataFromCsv (Route data) {
        List<String> content = Arrays.asList(data.getData().split("\n"));
        String allContent = data.getData();
        int dataCapacity = content.size() - 1;
        List<Data> dataList = new ArrayList<>();

        if (allContent.contains("username")) {
            for (int i = 0; i < dataCapacity; i++) {
                String[] currentData = content.get(i + 1).split(",");
                dataList.add(new Data());
                dataList.get(i).setId(currentData[0]);
                dataList.get(i).setUsername(currentData[1]);
                dataList.get(i).setEmail(currentData[2]);
                dataList.get(i).setCreated_account_data(currentData[3]);
            }
        }
        else {
            for(int i = 0; i < dataCapacity; i++) {
                String[] currentData = content.get(i+1).split(",");
                dataList.add(new Data());
                dataList.get(i).setId(currentData[0]);
                dataList.get(i).setFirst_name(currentData[1]);
                dataList.get(i).setLast_name(currentData[2]);
                dataList.get(i).setEmail(currentData[3]);
                dataList.get(i).setGender(currentData[4]);
                dataList.get(i).setIp_address(currentData[5]);
            }
        }
        return dataList;
    }
}
