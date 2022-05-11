import java.util.LinkedList;

/**
 * Responsibilities:
 * - Store board status (model)
 */
public class Board{

    private static Board board = null;

    private String name;
    private LinkedList<Set> sets; // the tiles
    private LinkedList<Player> players;
    

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
        this.players.add(player);
    }

    public void addPlayers(LinkedList<Player> players) {
        // TODO
    }

    public Player getPlayer(String name){
        int size = players.size();
        for(int i=0;i<size;i++){
            Player curr = players.get(i);
            if(curr.getName() == name){
                return curr;
            }
        }
        return null;
    }
    public Player getPlayer(int index){
        int size = players.size();
        if(index > players.size()){
            return null;
        }
        return players.get(index);
    }

    public Set locationOfPlayer(Player p){
        return p.getLocation();
    }

    public int playerCount(){
        return this.players.size();
    }

    public void addSet(Set set){
        this.sets.add(set);
    }

    public void addSets(LinkedList<Set> sets) {
        this.sets = sets;
    }

    public Set grabSet(String name){
        for(int i=0;i<sets.size();i++){
            Set currSet =sets.get(i);
            if(currSet.getName().equals(name)){
                return currSet;
            }
        }
        return null;
    }
    
    // Takes 1 scene card for every set and assigns these cards accordingly
    // cards should contain 10 scenes
    public void distributeScenes(LinkedList<Scene> cards) {
        for (int i = 0; i < cards.size(); i++) {
            if (sets.get(i).get)
            sets.get(i).setScene(cards.get(i));
        }
    }
}
