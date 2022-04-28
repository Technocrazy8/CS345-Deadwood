
/**
 * Our game controller
 * 
 * Responsiblities:
 * - Loop through game routine
 * - Manage player's interaction with the game
 * - Use the player's input to update the game's model
 * - The controller
 */
import java.util.*;

public class Deadwood {
    public static void main(String[] args) {
        Deadwood game = new Deadwood();
        game.run();
    }

    public void run() {
        boolean toggle = true;
        Board board = new Board();
        // int num
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Deadwood!");
        System.out.println("Please enter the number of players: ");
        int numplayers = scanner.nextInt();

        for (int i = 0; i < numplayers; i++) {
            String playername = "Player" + i;
            Player player = new Player(playername, i);
            // board
        }

        while (toggle) {

        }
    }

    public void setupGame() {

    }

    public void move(Player player, Set set, Board board) {

    }

    public void takeRole(Player player, Role role, Set set, Scene scene, Board board) {

    }
    
    public void resetBoard(Board board) {

    }

    public Scene[] shuffleScenes(Scene[]) {

    }

    public void move(Player player, Set set){

    }

    public void takeRole(Player player, Role role) {

    }

    public void upgrade(Player player) {

    }

    public void act(Player player, Set set, Scene scene, Board board) {

    }

    public void rehearse(Player player, Set set, Scene scene) {

    }

    public void calculateScores(Board board) {

    }
}
/**
 * Questions:
 * - How should we keep track of each player's position?
 *      Possible Solutions:
 *      - Board keeps track of where each player is
 *      - Role keeps track of player and then each step in the hierarchy goes fetch that
 *      - Sets and scenes keep track of it
 *      - Any kind of combinations of this.
 */