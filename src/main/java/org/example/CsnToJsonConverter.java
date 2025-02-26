package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class CsnToJsonConverter {
    public static void converter() throws IOException {
        String csvFilePath = "export.csv";

        try{
            List<Product> products = readCsvToList(csvFilePath);
            String jsonOutput = convertListToJson(products);

            System.out.println("JSON Output : " + jsonOutput);

            writeJsonToFile("output.json", jsonOutput);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static List<Product> readCsvToList(String csvFilePath) throws IOException {
        List<Product> products = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
        String line;

        br.readLine();

        while((line = br.readLine()) != null) {
            String[] data = line.split(",", 2);
            if(data.length == 2) {
                products.add(new Product(data[0], data[1]));
            }
        }
        br.close();
        return products;
    }

    public static String convertListToJson(List<Product> products) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writeValueAsString(products);
    }

    public static void writeJsonToFile(String fileName, String json) throws IOException {
        FileWriter file = new FileWriter(fileName);
        file.write(json);
        file.close();
    }
}
