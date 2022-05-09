
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

    private void run() {

        // Retrieve XML data

        LinkedList<Scene> cards; // card deck of 40 scenes for the 4 days of 10 sets
        LinkedList<Set> sets;

        ParseXML parser = new ParseXML();
        String[] xfiles = {"board.xml", "cards.xml"};

        try {
            Document d;
            for (int i = 0; i < 2; i++) {
                
                d = parser.getDocFromFile(xfiles[i]);
                switch (i) {
                    case 0:
                        sets = parser.readBoard(d); TODO: switch these once readBoard returns the list of sets
                        break;
                    case 1:
                        cards = parser.readCards(d);
                        break;
                }
            }
        }
        catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Use data to populate board

        Board board = Board.getBoard();
        board.addSets(sets);
        Collections.shuffle(cards);
        
        
        //board.distributeScenes(retrieveDailyCards(cards)); TODO: at the start of every day


        // Get number of players from user

        int numplayers = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Deadwood!");

        while (true) {
            System.out.print("Please enter the number of players (2 to 8): ");
            String input = scanner.nextLine();
            if (!isNumeric(input)) {
                System.out.println("Please enter a valid number of players");
            }
            else if (Integer.parseInt(input) > 1 && Integer.parseInt(input) < 9) {
                numplayers = Integer.parseInt(input);
                break;
            }
            
        }

        // Populate players

        for (int i = 0; i < numplayers; i++) {
            String playername = "Player " + (i + 1);
            Player player = new Player(playername, i);
            board.addPlayer(player);
        }

        // Game loop

        String input;
        boolean toggle = true;
        int currplayerindex = 0;


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

    // Gets the first 10 cards out of the deck (which should have been randomized)
    private LinkedList<Scene> retrieveDailyCards(LinkedList<Scene> cards) {

        LinkedList<Scene> dailyCards = new LinkedList<>();
        for(int i = 0; i < 10; i++) {
            dailyCards.add(cards.remove(0));
        }
        return dailyCards;
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