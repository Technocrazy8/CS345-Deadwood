import java.util.LinkedList;

/**
 * Responsibilities:
 * - Store board status (model)
 */
public class Board {

    private static Board board = null;

    private LinkedList<Set> sets; // the tiles
    private LinkedList<Player> players;
    private Set trailer;
    private Set office;

    private Board() {
        this.players = new LinkedList<Player>();
        this.sets = new LinkedList<Set>();
    }

    // lazy instance for the game board. Assure one is made
    public static Board getBoard() {
        if (board == null) {
            board = new Board();
        }
        return board;
    }
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    // method that gets all the available roles (from scene and set) of a players location
    // controller uses this method when allowing players to take role
    // created because gui has access to board. Cuts down some length in controller
    public LinkedList<Role> getAvailableRoles(Player player){
      LinkedList<Role> totalRoles = new LinkedList<Role>();
      Set playerLocation = player.getLocation();
      Scene scene = playerLocation.getScene();
      if (playerLocation.getName().equals("trailer") || playerLocation.getName().equals("office")) {
          System.out.println("\nNo roles are offered at: " + playerLocation.getName());
          return null;
      }
      LinkedList<Role> sceneRoles = playerLocation.getAvailableRoles(); // grab the nontaken scene roles
      LinkedList<Role> setRoles = scene.getAvailableRoles(); // grab the nontaken set roles
      totalRoles.addAll(sceneRoles);
      totalRoles.addAll(setRoles);
      return totalRoles;
    }

    // get player by name
    public Player getPlayer(String name) {
        int size = players.size();
        for (int i = 0; i < size; i++) {
            Player curr = players.get(i);
            if (curr.getName() == name) {
                return curr;
            }
        }
        return null;
    }
    // get player by specific index
    public Player getPlayer(int index) {
        if (index >= players.size()) {
            return null;
        }
        return players.get(index);
    }
    // returns player count
    public int playerCount() {
        return this.players.size();
    }

    // add sets to the board
    public void addSets(LinkedList<Set> sets) {
        this.sets = sets;
    }

    // get set by name
    public Set grabSet(String name) {
        for (int i = 0; i < sets.size(); i++) {
            Set currSet = sets.get(i);
            if (currSet.getName().equals(name)) {
                return currSet;
            }
        }
        return null;
    }
    // get A set by id
    public Set grabSet(int Id) {
        return sets.get(Id);
    }

    // get a sets ID
    public int getSetIndex(String name) {
        for (int i = 0; i < sets.size(); i++) {
            Set currSet = sets.get(i);
            if (currSet.getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    // Takes 1 scene card for every set and assigns these cards accordingly
    // cards should contain 10 scenes
    public void distributeScenes(LinkedList<Scene> cards, GUI pane) {
        for (int i = 0; i < cards.size(); i++) {
            sets.get(i).setScene(cards.get(i));
            pane.setBoardTile(sets.get(i),i);
        }
    }

    public void setTrailer(Set t) {
        this.trailer = t;
    }

    public Set getTrailer() {
        return this.trailer;
    }

    public void setOffice(Set o) {
        this.office = o;
    }

    public Set getOffice() {
        return this.office;
    }

    public int numSets() {
        return this.sets.size();
    }

    public LinkedList<Set> getSets() {
        return this.sets;
    }

    public void completeAll(GUI frame) { // USED FOR TESTING
        for (int i = 0; i < 9; i++) {
            board.sets.get(i).completeSet(frame,i);
        }
        for (int i = 0; i < playerCount(); i++) {
            Player currPlayer = board.players.get(i);
            if (currPlayer.getRole() != null) {
                currPlayer.setRole(null);
            }
        }
    }

    // resets the shot count and booleans for all sets
    public void resetTiles() {
        for (int i = 0; i < 10; i++) {
            Set currSet = board.sets.get(i);
            currSet.resetRoles();
            currSet.resetShots();
            currSet.imcomplete();
            currSet.hide();
        }
    }

    // gets all the extra roles from every tile
    public LinkedList<Role> getSetRoles(){
        LinkedList<Role> allRoles = new LinkedList<Role>();
        for(int i=0;i<10;i++){
          allRoles.addAll(sets.get(i).getRoles());
        }
        return allRoles;
    }

    // gets all the lead roles from every tile
    public LinkedList<Role> getSceneRoles(){
      LinkedList<Role> allRoles = new LinkedList<Role>();
      for(int i=0;i<10;i++){
        Scene currScene = sets.get(i).getScene();
        allRoles.addAll(currScene.getParts());
      }
      return allRoles;
    }

    public boolean dayEnd() { // Checker for end of day
        int count = 10;
        int notComplete = 0;
        for (int i = 0; i < count; i++) {
            if (!board.getSets().get(i).isComplete()) {
                notComplete++;
                if (notComplete > 1) {
                    return false;
                }
            }
        }
        return true;
    }
}
