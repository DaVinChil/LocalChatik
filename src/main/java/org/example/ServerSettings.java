package org.example;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerSettings {
    private static int PORT = -1;
    private static String HOST = null;
    private static final Path SETTINGS_PATH = Paths.get(".\\src\\main\\resources\\settings.txt");

    public static int getPort() {
        if (PORT == -1) {
            try (BufferedReader fr = new BufferedReader(new FileReader(SETTINGS_PATH.toFile()))) {
                PORT = fr.lines()
                        .filter(line -> line.contains("SERVER_PORT"))
                        .map(line -> line.substring(line.indexOf("=") + 2))
                        .map(port -> Integer.parseInt(port))
                        .findFirst()
                        .get();
            } catch (IOException ignored) {
            }
        }

        return PORT;
    }

    public static String getHost(){
        if (HOST == null){
            try (BufferedReader fr = new BufferedReader(new FileReader(SETTINGS_PATH.toFile()))) {
                HOST = fr.lines()
                        .filter(line -> line.contains("SERVER_HOST"))
                        .map(line -> line.substring(line.indexOf("=") + 2))
                        .findFirst()
                        .get();
            } catch (IOException ignored) {
            }
        }

        return HOST;
    }
}
