package utils;

import java.io.*;
import com.opencsv.CSVReaderHeaderAware;
import java.util.*;

public class CSVUtils {

    // Default login CSV
    private static final String DEFAULT_FILE_PATH = "src/test/resources/TestData.csv";

    // Donation CSVs (Zakat & Sadqa)
    private static final String ZAKAT_CSV_PATH = "src/test/resources/Zakat.csv";
    private static final String SADQA_CSV_PATH = "src/test/resources/Sadqa.csv";

    // ✅ Generic method to read CSV dynamically
    public static List<Map<String, String>> getAllData(String filePath) {
        List<Map<String, String>> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) return dataList;

            String[] headers = headerLine.split(",");
            for (int i = 0; i < headers.length; i++) headers[i] = headers[i].trim();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

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

    // ✅ Login CSV (default)
    public static List<Map<String, String>> getAllData() {
        return getAllData(DEFAULT_FILE_PATH);
    }

    public static Map<String, String> getLoginData(int index) {
        List<Map<String, String>> allData = getAllData();
        if (index < 0 || index >= allData.size())
            throw new IndexOutOfBoundsException("CSV index out of range: " + index);
        return allData.get(index);
    }

    // ✅ Donation CSVs
    public static List<Map<String, String>> getDonationData(String donationType) {
        String filePath;
        if ("Zakat".equalsIgnoreCase(donationType)) filePath = ZAKAT_CSV_PATH;
        else if ("Sadqa".equalsIgnoreCase(donationType)) filePath = SADQA_CSV_PATH;
        else throw new IllegalArgumentException("Invalid donation type: " + donationType);

        List<Map<String, String>> dataList = new ArrayList<>();
        try (CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(filePath))) {
            Map<String, String> row;
            while ((row = reader.readMap()) != null) {
                dataList.add(row);
            }
        } catch (Exception e) {
            System.err.println("❌ Error reading donation CSV: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("✅ Donation CSV (" + donationType + ") rows loaded: " + dataList.size());
        return dataList;
    }
}