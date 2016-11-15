import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    static List<List<Double>> readCsv(String csvFile) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<List<Double>> data = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(classLoader.getResourceAsStream(csvFile)))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                List<Double> row = new ArrayList<>();
                for (String value : nextLine) {
//                    System.out.print(value);
                    row.add(Double.parseDouble(value));
                }
                data.add(row);
//                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
