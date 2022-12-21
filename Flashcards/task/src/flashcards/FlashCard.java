package flashcards;

public class FlashCard {

    private String term;
    private String definition;
    private int mistakes;

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    FlashCard(String term, String definition) {
        setTerm(term);
        setDefinition(definition);
    }

    FlashCard(String term, String definition, int mistakes) {
        setTerm(term);
        setDefinition(definition);
        setMistakes(mistakes);
    }

    public void gotMistaken() {
        setMistakes(getMistakes() + 1);
    }

    public void resetMistakes() {
        setMistakes(0);
    }

}
