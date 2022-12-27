package flashcards;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileLogger {
    private static final FileLogger INSTANCE = new FileLogger();
    private static StringBuilder logger;

    private FileLogger() {
        this.logger = new StringBuilder("");
    }

    public static FileLogger getInstance() {
        return INSTANCE;
    }

    public void log(final String log) {
        System.out.print(log);
        logger.append(log);
    }
    public void logInput(final String log) {
        logger.append(log + "\n");
    }


    public String getLog() {
        return logger.toString();
    }

    public void saveLog() {
        final Scanner scanner = new Scanner(System.in);
        this.log("File name:\n");
        String fileName = scanner.nextLine();
        this.logInput(fileName);
        this.log("The log has been saved.\n");
        this.logInput("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
        try {
            FileWriter fileWriter = new FileWriter(fileName, false);
            fileWriter.write(this.getLog());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}