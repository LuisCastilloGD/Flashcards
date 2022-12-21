package flashcards;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        if (args.length == 0) {
            final Controller controller = new Controller();
            controller.menu();
        } else {
            try {
                File importFile = null ;
                File exportFile = null;
                boolean isImport = false;
                boolean isExport = false;
                boolean nextIsImport = false;
                boolean nextIsExport = false;
                for (String s : args) {
                    if (s.equals("-import")) {
                        isImport = true;
                        nextIsImport = true;
                        continue;
                    }
                    if (s.equals("-export")) {
                        isExport = true;
                        nextIsExport = true;
                        continue;
                    }
                    if (nextIsImport) {
                        importFile = new File(s);
                        nextIsImport = false;
                    }
                    if (nextIsExport) {
                        exportFile = new File(s);
                        nextIsExport = false;
                    }
                }
                final Controller controller = new Controller(isImport, isExport, importFile, exportFile);
                controller.menu();
            } catch (Exception e) {
                System.out.println("Bad parameters");
            }
        }
    }
}
