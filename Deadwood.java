
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

public class Deadwood{

    private static final String[] PLAYER_NAMES = {"blue", "cyan", "green", "orange", "pink", "red", "violet", "yellow"};
    private static Board board;
    private static int numPlayers;

    public static void main(String[] args) {
        Deadwood game = new Deadwood();
        
        System.out.println("Welcome to Deadwood!\n");
        int numPlayers = 0;

        /*
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("Enter number of players: ");
            String input = scanner.nextLine();

            if(isNumeric(input)){
                numPlayers=Integer.parseInt(input);
                break;
            }else{
                System.out.println("\nPlease enter a valid number of players (2-8)\n");
            }
        }
        scanner.close();
        */
        if (args.length != 1) {
            System.out.println("Invalid arguments:\njava Deadwood p\np = number of players: [2,8]");
            return;
        }

        try {
            numPlayers = Integer.parseInt(args[0]);
            
        } catch (Exception e) {
            System.out.println("Invalid arguments:\njava Deadwood p\np = number of players: [2,8]");
            return;
        }

        game.run(numPlayers);
    }

    private void run(int playerCount) {
        numPlayers = playerCount;
        Scanner scanner = new Scanner(System.in);
        LinkedList<Scene> cards = setupProcedure(numPlayers);

        System.out.println("Welcome to Deadwood!");

        for (int i = 1; i < 5; i++) {

            System.out.println("Day " + i);
            board.distributeScenes(retrieveDailyCards(cards)); // Assigns a scene to each set (10 a day)

            dailyRoutine(scanner);
        }

        scanner.close();
    }

    // Gets xml data and populates board with sets and players
    // Returns the cards read from the xml in a randomized list
    private LinkedList<Scene> setupProcedure(int numPlayers) {

        // Retrieve XML data

        LinkedList<Scene> cards = null; // card deck of 40 scenes for the 4 days of 10 sets
        LinkedList<Set> sets = null;

        ParseXML parser = new ParseXML();
        String[] xfiles = {"board.xml", "cards.xml"};

        try {
            Document d;
            for (int i = 0; i < 2; i++) {
                
                d = parser.getDocFromFile(xfiles[i]);
                switch (i) {
                    case 0:
                        sets = parser.readBoard(d);
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

        board = Board.getBoard();
        board.addSets(sets);
        Collections.shuffle(cards);        

        // Populate players

        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(PLAYER_NAMES[i], i);
            board.addPlayer(player);
        }

        // Populate trailer with players
        Set trailer = board.grabSet("trailer");
        for(int i=0;i<numPlayers;i++){
            Player currPlayer = board.getPlayer(i);
            currPlayer.setRole(null);
            trailer.addPlayer(currPlayer);
            board.getPlayer(i).setLocation(trailer);
        }
        return cards;
    }

    // Gets the first 10 cards out of the deck (which should have been randomized)
    private LinkedList<Scene> retrieveDailyCards(LinkedList<Scene> cards) {

        LinkedList<Scene> dailyCards = new LinkedList<>();
        for(int i = 0; i < 10; i++) {
            dailyCards.add(cards.remove(0));
        }
        return dailyCards;
    }

    private void dailyRoutine(Scanner scanner) {
        String input;
        //boolean toggle = true;
        int currplayerindex = 0;
        //int 


        while (true) {

            Player currentPlayer = board.getPlayer(currplayerindex);
            String playerName = currentPlayer.getName();
            Set playerLocation = currentPlayer.getLocation();
            System.out.println("\n"+playerName+"'s turn! \n You have: ($"+currentPlayer.getMoney()+", " + currentPlayer.getCredits()+" cr)\n Your location is: " +currentPlayer.getLocName());
            System.out.println(" Your rank is: " + currentPlayer.getRank());
            System.out.print("Please enter your move: ");
            input = scanner.next();
            input = input.toUpperCase();

            switch (input) {
                case "QUIT":             
                    quitGame();               
                    break;
                case "MOVE":
                    if(currentPlayer.checkInRole()){
                        System.out.println("Unable to ");
                    }
                    break;

                case "TURN":
                    currplayerindex++;
                    break;
                default:
                
                    break;
            }
            // currplayerindex++;
            if (currplayerindex == numPlayers) {
                currplayerindex = 0;
            }
        }
    }

    public void quitGame(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("Are you sure? (Y) or (N): ");
            String answer = scanner.next();
            answer = answer.toUpperCase();
            if (answer.equals("Y")) {
                System.out.println("\nGAME OVER\n");
                for (int i = 0; i < numPlayers; i++) {
                    System.out.println(
                            board.getPlayer(i).getName() + "'s score: " + board.getPlayer(i).calculateScore());
                }
                scanner.close();
                System.exit(0);
            }else if(answer.equals("N")){
                break;
            }else{
                System.out.println("Please enter Y or N");
            }
        }
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

    // public boolean isNumeric(String s) {
    //     for (int i = 0; i < s.length(); i++) {
    //         if (!(Character.isDigit(s.charAt(i)))) {
    //             return false;
    //         }
    //     }
    //     return true;
    // }
    public static boolean isNumeric(String s) {
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