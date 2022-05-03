
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
        boolean validsyntax = false;
        Board board = new Board();
        int numplayers = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Deadwood!");

        while (!validsyntax) {
            System.out.print("Please enter the number of players (2 to 8): ");
            String temp = scanner.nextLine();
            if (!isNumeric(temp)) {

            } else if (Integer.parseInt(temp) > 1 && Integer.parseInt(temp) < 9) {
                validsyntax = true;
                numplayers = Integer.parseInt(temp);
            }
            System.out.println("Please enter a valid number of players");
        }

        // System.out.print("Please enter the number of players (2 to 8): ");

        // int temp = scanner.nextInt();

        String input;
        int currplayerindex = 0;

        for (int i = 0; i < numplayers; i++) {
            String playername = "Player " + (i + 1);
            Player player = new Player(playername, i);
            board.addPlayer(player);
            // board
        }
        // System.out.println("WELCOME TO DEADWOOD!");
        setupGame();
        while (toggle) {

            System.out.print(board.players.get(currplayerindex).name + " Please enter your move: ");
            input = scanner.next();

            switch (input) {
                case "QUIT":
                    System.out.print("Are you sure? (Y) or (N): ");
                    String answer = scanner.next();
                    if (answer.equals("Y")) {
                        for (int i = 0; i < board.players.size(); i++) {
                            System.out.println(
                                    board.players.get(i).name + "'s score: " + board.players.get(i).calculateScore());
                        }
                        System.exit(0);
                    }
                    break;
                case "TURN":
                    currplayerindex++;
                    break;
                default:
                    break;
            }
            // currplayerindex++;
            if (currplayerindex == numplayers) {
                currplayerindex = 0;
            }
        }
        scanner.close();
    }

    public void setupGame() {

    }

    public void move(Player player, Set set, Board board) {

    }

    public void takeRole(Player player, Role role, Set set, Scene scene, Board board) {

    }

    public void resetBoard(Board board) {

    }

    public Scene[] shuffleScenes(Scene[] scenes) {
        return null;
    }

    public void move(Player player, Set set) {

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

    public boolean isNumeric(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!(Character.isDigit(s.charAt(i)))) {
                return false;
            }
        }
        return true;
    }
}
/**
 * Questions:
 * - How should we keep track of each player's position?
 * Possible Solutions:
 * - Board keeps track of where each player is
 * - Role keeps track of player and then each step in the hierarchy goes fetch
 * that
 * - Sets and scenes keep track of it
 * - Any kind of combinations of this.
 */