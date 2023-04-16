package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger {
    private static FileLogger INSTANCE;
    private File logFile;
    private FileWriter fileWriter;

    public static FileLogger getInstance(){
        if(INSTANCE == null) {
            try {
                INSTANCE = new FileLogger("D:\\Projects\\ChatikWIFI\\src\\main\\resources\\file.log");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return INSTANCE;
    }

    private FileLogger(String path) throws IOException {
        logFile = new File(path);
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
