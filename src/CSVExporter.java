import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CSVExporter {
    public void export(Map<String, String> dataMap, String fileName) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName, true)) { 
            StringBuilder sb = new StringBuilder();
            for (String key : dataMap.keySet()) {
                sb.append(dataMap.get(key)).append(",");
            }
            fileWriter.write(sb.substring(0, sb.length() - 1));
            fileWriter.write("\n");
        }
    }
}
