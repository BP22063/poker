import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("")

// 実行するテストクラスを指定したい場合は @SelectClasses を使用する

// 全てのテストを実行
public class AllTests {
}
