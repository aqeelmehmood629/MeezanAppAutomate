package utils;

import java.io.*;
import java.util.*;

public class CSVUtils {

    private static final String DEFAULT_FILE_PATH = "src/test/resources/TestData.csv";

    // ✅ NEW METHOD (dynamic file path)
    public static List<Map<String, String>> getAllData(String filePath) {
        List<Map<String, String>> dataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String headerLine = br.readLine();
            String[] headers = headerLine.split(",");

            for (int i = 0; i < headers.length; i++) {
                headers[i] = headers[i].trim();
            }

            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty())
                    continue;

                String[] values = line.split(",", -1);
                Map<String, String> map = new HashMap<>();

                for (int i = 0; i < headers.length; i++) {
                    String value = i < values.length ? values[i].trim() : "";
                    map.put(headers[i], value);
                }

                System.out.println("✅ CSV Row: " + map);
                dataList.add(map);
            }

        } catch (Exception e) {
            throw new RuntimeException("❌ Error reading CSV file: " + filePath, e);
        }

        return dataList;
    }

    // ✅ OLD METHOD (login ke liye still usable)
    public static List<Map<String, String>> getAllData() {
        return getAllData(DEFAULT_FILE_PATH);
    }

    public static Map<String, String> getLoginData(int index) {
        List<Map<String, String>> allData = getAllData();

        if (index < 0 || index >= allData.size()) {
            throw new IndexOutOfBoundsException("CSV index out of range: " + index);
        }

        return allData.get(index);
    }
}