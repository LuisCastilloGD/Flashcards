package flashcards;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


public class Controller {

    public Deck deck;
    public PrintWriter printWriterLog;
    Controller(){
        deck = new Deck();
        try {
            startLog();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void menu(){

        final Scanner scanner = new Scanner(System.in);
        boolean active = true;
        while (active){
            System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String input = scanner.nextLine();
            switch (input) {
                case "add" -> deck.addNewCard();
                case "remove" -> deck.removeCard();
                case "import" -> deck.importCards();
                case "export" -> deck.exportCards();
                case "ask" -> ask();
                case "log" -> log();
                case "hardest card" -> deck.hardestCard();
                case "reset stats" -> deck.resetStats();
                case "exit" -> {System.out.println("Bye bye!"); active = false;}
                default -> {System.out.println("Bad parameters");}
            }
        }

    }

    protected  void startLog() throws IOException {
        printWriterLog = new PrintWriter(new FileWriter("defaultLog.txt"),true);
    }
    protected void log(){
        String log = printWriterLog.toString();
        printWriterLog.close();

        final Scanner scanner = new Scanner(System.in);
        System.out.println("File name:");
        String nameFile = scanner.nextLine();

        try {
            PrintWriter fileWriter = new PrintWriter(nameFile);
            fileWriter.println(log);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("The log has been saved.");
    }
    protected void ask(){
        final Scanner scanner = new Scanner(System.in);
        System.out.println("How many times to ask?");
        final int numQuestions = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < numQuestions; i++) {

            FlashCard card = deck.getCards().get(deck.getAskIndex());
            System.out.println("Print the definition of \""+card.getTerm()+"\":");
            String def = scanner.nextLine();
            if(def.equals(card.getDefinition())){
                System.out.println("Correct!");
            }else {
                if(!foundDefInOtherCard(def,card))
                    System.out.println("Wrong. The right answer is \""+card.getDefinition()+"\".");card.gotMistaken();
            }
            deck.setNextAskIndex();
        }



    }
    protected void askAllCards(){
        final Scanner scanner = new Scanner(System.in);
        for (FlashCard card: deck.getCards()) {
            System.out.println("Print the definition of \""+card.getTerm()+"\":");
            String def = scanner.nextLine();
            if(def.equals(card.getDefinition())){
                System.out.println("Correct!");
            }else {
                if(!foundDefInOtherCard(def,card))
                System.out.println("Wrong. The right answer is \""+card.getDefinition()+"\".");card.gotMistaken();
            }
        }

    }

    protected boolean foundDefInOtherCard(String def,FlashCard card){
        for (FlashCard cardCompare: deck.getCards()) {
            if(def.equals(cardCompare.getDefinition()) && !card.equals(cardCompare)){
                System.out.println("Wrong. The right answer is \""+card.getDefinition()+"\", but your definition is correct for \""+cardCompare.getTerm()+"\"  ");
                card.gotMistaken();
                return true;
            }

        }
        return false;
    }



}
