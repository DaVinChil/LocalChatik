import org.example.ServerSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServerSettingsTest {

    @Test
    public void getPortTest(){
        int actual = ServerSettings.getPort();
        int expected = 8080;

        Assertions.assertEquals(expected, actual);
    }
}
