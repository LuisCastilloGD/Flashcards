package flashcards;

import java.io.*;
import java.util.*;


public class Controller {

    private final Deck deck;
    private final FileLogger fileLogger;
    private boolean exportWhenExit;
    private File exportFile;

    Controller() {
        this.deck = new Deck();
        this.fileLogger = FileLogger.getInstance();
    }

    Controller(final boolean isImport, final boolean isExport, final File importFile, final File exportFile) {
        this.deck = isImport ? new Deck(importFile) : new Deck();
        this.exportWhenExit = isExport;
        this.exportFile = exportFile;
        this.fileLogger = FileLogger.getInstance();
    }

    protected void menu() {
        final Scanner scanner = new Scanner(System.in);
        boolean active = true;
        while (active) {
            fileLogger.log("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):\n");
            final String input = scanner.nextLine();
            fileLogger.logInput(input);
            switch (input) {
                case "add" -> deck.addNewCard();
                case "remove" -> deck.removeCard();
                case "import" -> deck.importCards();
                case "export" -> deck.exportCards();
                case "ask" -> ask();
                case "log" -> fileLogger.saveLog();
                case "hardest card" -> deck.hardestCard();
                case "reset stats" -> deck.resetStats();
                case "exit" -> {
                    exit();
                    active = false;
                }
                default -> {
                    fileLogger.log("Bad parameters\n");
                }
            }
        }

    }

    private void ask() {
        final Scanner scanner = new Scanner(System.in);
        fileLogger.log("How many times to ask?\n");
        final int numQuestions = scanner.nextInt();
        fileLogger.logInput(Integer.toString(numQuestions));
        scanner.nextLine();
        for (int i = 0; i < numQuestions; i++) {
            final FlashCard card = this.deck.getCards().get(this.deck.getAskIndex());
            fileLogger.log("Print the definition of \"" + card.getTerm() + "\":\n");
            final String def = scanner.nextLine();
            fileLogger.logInput(def);
            if (def.equals(card.getDefinition())) {
                fileLogger.log("Correct!\n");
            } else {
                if (!foundDefInOtherCard(def, card))
                    fileLogger.log("Wrong. The right answer is \"" + card.getDefinition() + "\".\n");
                card.gotMistaken();
            }
            this.deck.setNextAskIndex();
        }


    }

    private boolean foundDefInOtherCard(final String def, final FlashCard card) {
        for (FlashCard cardCompare : this.deck.getCards()) {
            if (def.equals(cardCompare.getDefinition()) && !card.equals(cardCompare)) {
                fileLogger.log("Wrong. The right answer is \"" + card.getDefinition() + "\", but your definition is correct for \"" + cardCompare.getTerm() + "\"  \n");
                card.gotMistaken();
                return true;
            }

        }
        return false;
    }

    private void exit() {
        if (this.exportWhenExit) {
            this.deck.exportCards(this.exportFile);
        }
        fileLogger.log("Bye bye!\n");
    }

}
