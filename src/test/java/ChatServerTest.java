import org.example.ChatServer;
import org.example.ServerSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ChatServerTest {

    {
        new Thread(() -> ChatServer.main(null)).start();
    }

    @Test
    public void setPortTest(){
        int actual = ChatServer.getPort();
        int expected = 8080;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void sendMsgAndReceiveTest() throws IOException {
        Socket chat1 = new Socket("localhost", ChatServer.getPort());
        Socket chat2 = new Socket("localhost", ChatServer.getPort());

        OutputStream out1 = chat1.getOutputStream();
        BufferedReader in1 = new BufferedReader(new InputStreamReader(chat1.getInputStream()));

        OutputStream out2 = chat2.getOutputStream();
        BufferedReader in2 = new BufferedReader(new InputStreamReader(chat2.getInputStream()));

        Assertions.assertEquals("Enter user name: ", in1.readLine());
        Assertions.assertEquals("Enter user name: ", in2.readLine());

        out1.write("Sasha\n".getBytes());
        out1.flush();

        out2.write("Alex\n".getBytes());
        out2.flush();

        out1.write("Hello!\n".getBytes());

        Assertions.assertTrue(in2.readLine().endsWith("Sasha: Hello!"));
    }
}
