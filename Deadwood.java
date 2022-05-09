
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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class Deadwood {
    public static void main(String[] args) {
        Deadwood game = new Deadwood();
        game.run();
    }

    public void run() {
        boolean toggle = true;
        boolean validsyntax = false;
        Board board = Board.getBoard();
        int numplayers = 0;
        Scanner scanner = new Scanner(System.in);
        LinkedList<Set> cards = new LinkedList<Set>();
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
        int currplayerindex = 0;

        for (int i = 0; i < numplayers; i++) {
            String playername = "Player " + (i + 1);
            Player player = new Player(playername, i);
            board.addPlayer(player);
            // board
        }
        setupGame();
        String input;
        while (toggle) {

            System.out.print(board.players.get(currplayerindex).getName() + " Please enter your move: ");
            input = scanner.next();
            input = input.toUpperCase();

            switch (input) {
                case "QUIT":
                    while(true){
                        System.out.print("Are you sure? (Y) or (N): ");
                        String answer = scanner.next();
                        answer = answer.toUpperCase();
                        if (answer.equals("Y")) {
                            for (int i = 0; i < board.players.size(); i++) {
                                System.out.println(
                                        board.players.get(i).name + "'s score: " + board.players.get(i).calculateScore());
                            }
                            System.exit(0);
                        }else if(answer.equals("N")){
                            break;
                        }else{
                            System.out.println("Please enter Y or N");
                        }
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

        
        ParseXML parser = new ParseXML();
        String[] xfiles = {"board.xml", "cards.xml"};
        for(int i=0; i<2;i++){
            
                Document d;
                try {
                    d = parser.getDocFromFile(xfiles[i]);
                    if(i==0){
                        parser.readBoard(d);
                    }else{
                        parser.readCards(d);
                    }
                } catch (ParserConfigurationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
        }
        //Doc



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