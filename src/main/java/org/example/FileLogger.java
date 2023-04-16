package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileLogger {
    private static FileLogger INSTANCE;
    private File logFile;
    private FileWriter fileWriter;
    private static final Path FILE_LOG_PATH = Paths.get("./src/main/resources/file.log");

    public static FileLogger getInstance(){
        if(INSTANCE == null) {
            try {
                INSTANCE = new FileLogger();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return INSTANCE;
    }

    private FileLogger() throws IOException {
        logFile = new File(FILE_LOG_PATH.toUri());
        fileWriter = new FileWriter(logFile, true);
    }

    public void log(String str){
        try {
            fileWriter.write(str + "\n");
            fileWriter.close();
            fileWriter = new FileWriter(logFile, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
