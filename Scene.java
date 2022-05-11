
/**
 * The cards on the tiles of the game
 * 
 * Responsibilities:
 * - Store scene status (model)
 */
import java.util.LinkedList;

public class Scene {
    // the cards on the tiles(sets) on the board
    // have the nonextras
    private String title;
    private String description;
    private int budget;
    private LinkedList<Role> parts;
    private LinkedList<Player> actors;
    //int shotcounter;
    private int actorcapacity;

    public Scene(String title, String description, int budgets, LinkedList<Role> parts, int actcap) {
        this.title = title;
        this.description = description;
        this.budget = budgets;
        this.parts = parts;
        this.actors = new LinkedList<Player>();
        this.actorcapacity = actcap;
    }
}
