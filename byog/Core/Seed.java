package byog.Core;

public class Seed implements java.io.Serializable {
    private String seed;
    private boolean withKeyboard;

    public Seed(String seed, boolean runWithKeyboard) {
        this.seed = seed;
        withKeyboard = runWithKeyboard;
    }

    public String getSeed() {
        return seed;
    }

    public void load(Game g) {
        g.seed = seed;
        g.withKeyboard = withKeyboard;
    }
}
