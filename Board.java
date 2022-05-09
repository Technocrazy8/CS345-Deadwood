import java.util.LinkedList;

/**
 * Responsibilities:
 * - Store board status (model)
 */
public class Board {

    private static Board board = null;

    String name;
    LinkedList<Set> sets; // the tiles
    LinkedList<Player> players;

    private Board() {
        this.players = new LinkedList<Player>();
        this.sets = new LinkedList<Set>();
    }

    public static Board getBoard() {

        if (board == null) {
            board = new Board();
        }

        return board;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
    public void addSet(Set set){
        
    }
}
