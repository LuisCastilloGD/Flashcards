package flashcards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

public class Deck {
    private final HashMap<String,FlashCard> cards;
    private final FileLogger fileLogger;
    final protected HashMap<String,FlashCard> getCards() {
        return this.cards;
    }

    Deck() {
        this.cards = new HashMap<String,FlashCard>();
        this.fileLogger = FileLogger.getInstance();
    }

    Deck(File file) {
        this.cards = new HashMap<String,FlashCard>();
        this.fileLogger = FileLogger.getInstance();
        importCards(file);
    }

    final protected void addNewCard() {
        final Scanner scanner = new Scanner(System.in);
        fileLogger.log("The card:\n");
        String term = scanner.nextLine();
        fileLogger.logInput(term);
        if (!isUniqueTerm(term)) {
            fileLogger.log("The card \"" + term + "\" already exists.\n");
            return;
        }
        fileLogger.log("The definition of the card:\n");
        String def = scanner.nextLine();
        fileLogger.logInput(def);
        if (!isUniqueDef(def)) {
            fileLogger.log("The definition \"" + def + "\" already exists.\n");
            return;
        }
        FlashCard card = new FlashCard(term, def);
        this.cards.put(card.getTerm(), card);
        fileLogger.log("The pair (\"" + term + "\":\"" + def + "\") has been added\n");
    }

    final protected void removeCard() {
        final Scanner scanner = new Scanner(System.in);
        fileLogger.log("Which card?\n");
        final String term = scanner.nextLine();
        fileLogger.logInput(term);
        if(this.cards.get(term)==null){
            fileLogger.log("Can't remove \"" + term + "\":there is no such card\n");
            return;
        }
        this.cards.remove(term);
        fileLogger.log("The card has been removed\n");
    }


    final protected void importCards() {
        final Scanner scanner = new Scanner(System.in);
        fileLogger.log("File name:\n");
        final String fileName = scanner.nextLine();
        fileLogger.logInput(fileName);
        final File flashCardFile = new File(fileName);
        try (final Scanner fileScanner = new Scanner(flashCardFile)) {
            int numCards = 0;
            while (fileScanner.hasNextLine()) {
                final String term = fileScanner.nextLine();
                final String def = fileScanner.nextLine();
                final int mistakes = fileScanner.nextInt();
                fileScanner.nextLine();
                addImportedCard(term, def, mistakes);
                numCards++;
            }
            fileLogger.log(numCards + " cards have been loaded.\n");

        } catch (Exception e) {
            fileLogger.log("File not found.\n");
        }

    }

    final protected void importCards(final File file) {
        try (final Scanner fileScanner = new Scanner(file)) {
            int numCards = 0;
            while (fileScanner.hasNextLine()) {
                final String term = fileScanner.nextLine();
                final String def = fileScanner.nextLine();
                final int mistakes = fileScanner.nextInt();
                fileScanner.nextLine();
                addImportedCard(term, def, mistakes);
                numCards++;
            }
            fileLogger.log(numCards + " cards have been loaded.\n");
        } catch (Exception e) {
            fileLogger.log("File not found.\n");
        }
    }

    final protected void exportCards() {
        final Scanner scanner = new Scanner(System.in);
        fileLogger.log("File name:\n");
        final String fileName = scanner.nextLine();
        fileLogger.logInput(fileName);
        int numCards = cards.size();
        try {
            final PrintWriter fileWriter = new PrintWriter(fileName);
            this.cards.forEach((key, value) -> {
                fileWriter.println(value.getTerm());
                fileWriter.println(value.getDefinition());
                fileWriter.println(value.getMistakes());
            });
            fileWriter.close();
            fileLogger.log(numCards + " cards have been saved.\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    final protected void exportCards(final File file) {
        final int numCards = cards.size();
        try {
            final PrintWriter fileWriter = new PrintWriter(file);
            this.cards.forEach((key, value) -> {
                fileWriter.println(value.getTerm());
                fileWriter.println(value.getDefinition());
                fileWriter.println(value.getMistakes());
            });
            fileWriter.close();
            fileLogger.log(numCards + " cards have been saved.\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addImportedCard(final String term, final String def, final int mistakes) {
        FlashCard newCard = new FlashCard(term, def, mistakes);
        this.cards.put(term,newCard);
    }

    final protected void hardestCard() {
        int mostMistakes = 0;
        List<FlashCard> hardestCards = new ArrayList<>();
        for(HashMap.Entry<String, FlashCard> entry : cards.entrySet()) {
            FlashCard card = entry.getValue();
            if (card.getMistakes() > mostMistakes) {
                mostMistakes = card.getMistakes();
                hardestCards.clear();
                hardestCards.add(card);
            } else if (card.getMistakes() == mostMistakes) {
                hardestCards.add(card);
            }
        }

        if (mostMistakes == 0) {
            fileLogger.log("There are no cards with errors.\n");
        } else if (hardestCards.size() == 1) {
            fileLogger.log("The hardest card is \"" + hardestCards.get(0).getTerm() + "\". You have " + hardestCards.get(0).getMistakes() + " errors answering it\n");
        } else {
            fileLogger.log("The hardest cards are ");
            for (FlashCard card : hardestCards) {
                fileLogger.log("\"" + card.getTerm() + "\", ");
            }
            fileLogger.log(". You have " + hardestCards.get(0).getMistakes() + " errors answering them \n");
        }


    }

    final protected void resetStats() {
        this.cards.forEach((key, value) -> {
            value.setMistakes(0);
        });
        fileLogger.log("Card statistics have been reset\n");
    }

    final protected boolean isUniqueTerm(String newTerm) {
        if(cards.containsKey(newTerm))
            return false;
        return true;
    }

    final protected boolean isUniqueDef(String newDef) {
        for(HashMap.Entry<String, FlashCard> entry : cards.entrySet()) {
            FlashCard card = entry.getValue();
            if (card.getDefinition().equals(newDef))
                return false;
        }
        return true;
    }

}
