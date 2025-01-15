package resultsMagager;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {
    public static void exportToCsv(List<CustomTestListener.TestResult> results, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ClassName,TestName,Status\n");
            for (CustomTestListener.TestResult result : results) {
                writer.write( result.getParent() + "," + result.getTestName() + "," + result.getStatus() + "\n");
            }
        }
    }
}

