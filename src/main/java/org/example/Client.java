package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static OutputStream out;
    private static BufferedReader in;
    private static Socket server;

    public static void main(String[] args) {
        try {
            server = new Socket(ServerSettings.getHost(), ServerSettings.getPort());
            setIO();

            MessageReceiver receiver = new MessageReceiver();
            MessageSender sender = new MessageSender();

            receiver.start();
            sender.start();

            receiver.join();
            sender.join();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void setIO() throws IOException {
        out = server.getOutputStream();
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
    }

    private static class MessageSender extends Thread {
        @Override
        public void run() {
            Scanner scan = new Scanner(System.in);
            String msg;
            while (true) {
                msg = scan.nextLine();
                try {
                    out.write((msg + "\n").getBytes());
                    out.flush();

                    if (msg.equals("/exit")) {
                        out.close();
                        in.close();
                        break;
                    }

                } catch (IOException ignored) { }
            }
        }
    }

    private static class MessageReceiver extends Thread{
        @Override
        public void run () {
            String resp;
            try {
                while ((resp = in.readLine()) != null) {
                    System.out.println(resp);
                }
            } catch (IOException ignore) {}
        }
    }
}
