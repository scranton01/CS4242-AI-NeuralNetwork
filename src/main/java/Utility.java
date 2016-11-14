import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    static List<List<Integer>> readCsv(String csvFile) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<List<Integer>> data = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(classLoader.getResourceAsStream(csvFile)))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                List<Integer> row = new ArrayList<>();
                for (String value : nextLine) {
//                    System.out.print(value);
                    row.add(Integer.parseInt(value));
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
