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

    public void addPlayers(LinkedList<Player> players) {
        // TODO
    }

    public void addSet(Set set){
        
    }

    public void addSets(LinkedList<Set> sets) {
        this.sets = sets;
    }

    // Takes 1 scene card for every set and assigns these cards accordingly
    public void distributeScenes(LinkedList<Scene> cards) {

        if (cards.size() != sets.size()) {
            System.out.println("error: number of cards does not match number of sets!!!");
        }

        for (int i = 0; i < cards.size(); i++) {
            sets.get(i).setScene(cards.get(i));
        }
    }
}
