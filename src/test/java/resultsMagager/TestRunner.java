package resultsMagager;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import java.io.IOException;

/*
全テストを実行し、結果をJsonとCSVに出力する
出力内容はテスト名と成功/失敗と失敗時の例外の型

出力情報を拡張したい
 */

public class TestRunner {
    public static void main(String[] args) throws IOException {
        CustomTestListener listener = new CustomTestListener();

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
                        selectClass(AllTests.class)
                )
                .build();

        Launcher launcher = LauncherFactory.create();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        // テスト結果の取得
        var results = listener.getResults();

        // JSONに出力
        JsonExporter.exportToJson(results, "test-results.json");

        // CSVに出力
        CsvExporter.exportToCsv(results, "test-results.csv");

        System.out.println("Results exported to JSON and CSV.");
    }
}
