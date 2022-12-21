package flashcards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

public class Deck {
    private final List<FlashCard> cards;
    private int askIndex;
    private final FileLogger fileLogger;

    protected int getAskIndex() {
        return askIndex;
    }

    private void setAskIndex(final int askIndex) {
        this.askIndex = askIndex;
    }

    final protected void setNextAskIndex() {
        if (this.getAskIndex() >= getCards().size() - 1) {
            setAskIndex(0);
        } else {
            setAskIndex(this.getAskIndex() + 1);
        }
    }

    final protected List<FlashCard> getCards() {
        return this.cards.stream().toList();
    }

    Deck() {
        this.cards = new ArrayList<>();
        this.askIndex = 0;
        this.fileLogger = FileLogger.getInstance();
    }

    Deck(File file) {
        this.cards = new ArrayList<>();
        this.askIndex = 0;
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
        this.cards.add(card);
        fileLogger.log("The pair (\"" + term + "\":\"" + def + "\") has been added\n");
    }

    final protected void removeCard() {
        final Scanner scanner = new Scanner(System.in);
        fileLogger.log("Which card?\n");
        final String term = scanner.nextLine();
        fileLogger.logInput(term);
        for (FlashCard card : cards) {
            if (card.getTerm().equals(term)) {
                this.cards.remove(card);
                fileLogger.log("The card has been removed\n");
                return;
            }
        }
        fileLogger.log("Can't remove \"" + term + "\":there is no such card\n");
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
            for (FlashCard card : cards) {
                fileWriter.println(card.getTerm());
                fileWriter.println(card.getDefinition());
                fileWriter.println(card.getMistakes());
            }
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
            for (FlashCard card : cards) {
                fileWriter.println(card.getTerm());
                fileWriter.println(card.getDefinition());
                fileWriter.println(card.getMistakes());
            }
            fileWriter.close();

            fileLogger.log(numCards + " cards have been saved.\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void addImportedCard(final String term, final String def, final int mistakes) {
        FlashCard newCard = new FlashCard(term, def, mistakes);
        if (isUniqueTerm(term)) {
            this.cards.add(newCard);
        } else {
            for (FlashCard card : cards) {
                if (card.getTerm().equals(term)) {
                    card.setDefinition(def);
                }
            }
        }
    }

    final protected void hardestCard() {
        int mostMistakes = 0;
        List<FlashCard> hardestCards = new ArrayList<>();

        for (FlashCard card : getCards()) {
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
        for (FlashCard card : this.cards) {
            card.setMistakes(0);
        }
        fileLogger.log("Card statistics have been reset\n");
    }

    final protected boolean isUniqueTerm(String newTerm) {
        for (FlashCard card : cards) {
            if (card.getTerm().equals(newTerm))
                return false;
        }
        return true;
    }

    final protected boolean isUniqueDef(String newDef) {
        for (FlashCard card : cards) {
            if (card.getDefinition().equals(newDef))
                return false;
        }
        return true;
    }

}
