package flashcards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
public class Deck {
    private List<FlashCard> cards;

    private int askIndex;

    public int getAskIndex() {
        return askIndex;
    }

    public void setAskIndex(int askIndex) {
        this.askIndex = askIndex;
    }

    public void setNextAskIndex(){
        if(this.getAskIndex() >= getCards().size()-1) {
            setAskIndex(0);
        }else{
            setAskIndex(this.getAskIndex() + 1);
        }
    }

    public List<FlashCard> getCards() {
        return  this.cards.stream().toList();
    }
    private void setCards(List<FlashCard> cards) {
        this.cards = cards;
    }

    Deck(){
        cards = new ArrayList<>();
        askIndex = 0;
    }

    protected void addNewCard(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("The card:");
        String term = scanner.nextLine();
        if(!isUniqueTerm(term)){
            System.out.println("The card \""+term+"\" already exists.");
            return;
        }
        System.out.println("The definition of the card:");
        String def = scanner.nextLine();
        if(!isUniqueDef(def)){
            System.out.println("The definition \""+def+"\" already exists.");
            return;
        }
        FlashCard card = new FlashCard(term,def);
        cards.add(card);
        System.out.println("The pair (\""+term+"\":\""+def+"\") has been added");
    }

    protected void removeCard(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which card?");
        String term = scanner.nextLine();
        for (FlashCard card:cards) {
            if(card.getTerm().equals(term)){
                cards.remove(card);
                System.out.println("The card has been removed");
                return;
            }
        }
        System.out.println("Can't remove \""+term+"\":there is no such card");
    }


    protected void importCards(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("File name:");
        String fileName = scanner.nextLine();
        File flashCardFile = new File(fileName);
        try (Scanner fileScanner = new Scanner(flashCardFile)){
            int numCards = 0;
            while(fileScanner.hasNextLine()){
                String term = fileScanner.nextLine();
                String def = fileScanner.nextLine();
                int mistakes = fileScanner.nextInt();
                fileScanner.nextLine();
                addImportedCard(term,def,mistakes);
                numCards++;
            }
            System.out.printf("%d cards have been loaded.\n",numCards);

        }catch (Exception e){
            System.out.println("File not found.");
        }

    }

    protected void exportCards(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("File name:");
        String fileName = scanner.nextLine();
        int numCards = cards.size();
        try {
            PrintWriter fileWriter = new PrintWriter(fileName);
            for(FlashCard card: cards){
                fileWriter.println(card.getTerm());
                fileWriter.println(card.getDefinition());
                fileWriter.println(card.getMistakes());
            }
            fileWriter.close();

            System.out.printf("%d cards have been saved.\n",numCards);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    protected void addImportedCard(String term, String def,int mistakes){
        FlashCard newCard = new FlashCard(term,def,mistakes);
        if(isUniqueTerm(term)){
            cards.add(newCard);
        }else{
            for(FlashCard card:cards){
                if(card.getTerm().equals(term)){
                    card.setDefinition(def);
                }
            }
        }
    }

    protected void hardestCard(){
        int mostMistakes = 0;
        List<FlashCard> hardestCards = new ArrayList<>();

        for (FlashCard card :getCards()) {
            if(card.getMistakes() > mostMistakes){
                mostMistakes = card.getMistakes();
                hardestCards.clear();
                hardestCards.add(card);
            } else if (card.getMistakes() == mostMistakes) {
                hardestCards.add(card);
            }
        }

        if(mostMistakes==0) {
            System.out.println("There are no cards with errors.");
        }
        else if(hardestCards.size()==1){
            System.out.println("The hardest card is \""+hardestCards.get(0).getTerm()+"\". You have "+ hardestCards.get(0).getMistakes()+" errors answering it");
        }else{
            System.out.print("The hardest cards are ");
            int cardCounter = 1;
            for(FlashCard card :hardestCards){
                System.out.printf("\"%s\", ",card.getTerm(),cardCounter);
                cardCounter++;
            }
            System.out.printf(". You have %d errors answering them",hardestCards.get(0).getMistakes());
            System.out.println();
        }


    }

    protected void resetStats(){
        for(FlashCard card: this.cards){
            card.setMistakes(0);
        }
        System.out.println("Card statistics have been reset");
    }

    protected boolean isUniqueTerm(String newTerm){
        for (FlashCard card: cards) {
            if(card.getTerm().equals(newTerm))
                return false;
        }
        return true;
    }

    protected boolean isUniqueDef(String newDef){
        for (FlashCard card: cards) {
            if(card.getDefinition().equals(newDef))
                return false;
        }
        return true;
    }

}
