
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
    private String image;
    private int budget;
    private LinkedList<Role> parts;
    private LinkedList<Player> actors;
    private int actorcapacity;

    public Scene(String title, String description, int budgets, LinkedList<Role> parts, int actcap, String image) {
        this.title = title;
        this.description = description;
        this.budget = budgets;
        this.parts = parts;
        this.actors = new LinkedList<Player>();
        this.actorcapacity = actcap;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getBudget() {
        return budget;
    }

    public LinkedList<Role> getParts() {
        return parts;
    }

    public int getRoleCount() {
        return this.parts.size();
    }

    public LinkedList<Player> getActors() {
        return actors;
    }

    public int getActorCapacity() {
        return actorcapacity;
    }

    public String getImage(){
      return this.image;
    }

    public LinkedList<Role> getAvailableRoles() { // gets roles that nobody is in
        LinkedList<Role> available = new LinkedList<Role>();
        for (int i = 0; i < this.parts.size(); i++) {
            if (this.parts.get(i).isAvailable()) {
                available.add(this.parts.get(i));
            }
        }
        return available;
    }

    public LinkedList<Role> getTakenRoles() { // gets roles that people are in
        LinkedList<Role> taken = new LinkedList<Role>();
        for (int i = 0; i < this.parts.size(); i++) {
            if (!this.parts.get(i).isAvailable()) {
                taken.add(this.parts.get(i));
            }
        }
        return taken;
    }

    public void resetRoles() { // reset the role -isTaken- boolean
        int count = this.parts.size();
        for (int i = 0; i < count; i++) {
            Role currRole = parts.get(i);
            currRole.vacateRole();
        }
    }
}
