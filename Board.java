import java.util.LinkedList;

/**
 * Responsibilities:
 * - Store board status (model)
 */
public class Board {
    String name;
    LinkedList<Set> sets; // the tiles
    LinkedList<Player> players;

    public Board() {
        this.players = new LinkedList<Player>();
        this.sets = new LinkedList<Set>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
    public void addSet(Set set){
        
    }
}
