
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
    String title;
    String description;
    int budget;
    LinkedList<Roles> parts;
    LinkedList<Player> actors;
    int shotcounter;
    int actorcapacity;

    public Scene(String title, String description, int budgets, LinkedList<Roles> parts, int shotcounter, int actcap) {
        this.title = title;
        this.description = description;
        this.budget = budgets;
        this.parts = parts;
        this.shotcounter = shotcounter;
        this.actors = new LinkedList<Player>();
        this.actorcapacity = actcap;
    }
}
