package resultsMagager;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {
    public static void exportToCsv(List<TestResult> results, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ClassName,TestName,Status,Throwable\n");
            for (TestResult result : results) {
                writer.write(
                        result.getParent() + "," +
                            result.getTestName() + "," +
                            result.getStatus() + "," +
                            result.getThrowable() + "\n");
            }
        }
    }
}

