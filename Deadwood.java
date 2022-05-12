
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

    private static final String[] PLAYER_NAMES = { "blue", "cyan", "green", "orange", "pink", "red", "violet",
            "yellow" };
    private static Board board;
    private static int numPlayers;

    public static void main(String[] args) {
        Deadwood game = new Deadwood();

        System.out.println("Welcome to Deadwood!\n");
        int numPlayers = 0;

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

        // System.out.println("Welcome to Deadwood!");

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
        String[] xfiles = { "board.xml", "cards.xml" };

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
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Use data to populate board

        board = Board.getBoard();
        board.addSets(sets);
        board.setTrailer(sets.get(sets.size() - 2));
        board.setOffice(sets.get(sets.size() - 1));
        Collections.shuffle(cards);

        // Populate players

        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(PLAYER_NAMES[i], i);
            board.addPlayer(player);
        }

        // Populate trailer with players
        Set trailer = board.grabSet("trailer");
        for (int i = 0; i < numPlayers; i++) {
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
        for (int i = 0; i < 10; i++) {
            dailyCards.add(cards.remove(0));
        }
        return dailyCards;
    }

    private void dailyRoutine(Scanner scanner) {
        String input;
        // boolean toggle = true;
        int currplayerindex = 0;
        // int

        while (true) {

            Player currentPlayer = board.getPlayer(currplayerindex);
            String playerName = currentPlayer.getName();
            Set playerLocation = currentPlayer.getLocation();
            System.out.println("\n" + playerName + "'s turn! \n You have: ($" + currentPlayer.getMoney() + ", "
                    + currentPlayer.getCredits() + " cr)\n Your location is: " + currentPlayer.getLocName());
            System.out.println(" Your rank is: " + currentPlayer.getRank());
            int opt;
            // debugBoard(3);
            if (currentPlayer.checkInRole()) {
                actingChoices(currentPlayer, currplayerindex);
            } else {
                opt = basicChoices(currentPlayer);
                if (opt == 1) {
                    currplayerindex++;
                }

            }
            // System.out.print("Please enter your move: ");
            // input = scanner.next();
            // input = input.toUpperCase();

            // switch (input) {
            // case "QUIT":
            // quitGame();
            // break;
            // case "MOVE":
            // if(currentPlayer.checkInRole()){
            // System.out.println("Unable to ");
            // }
            // break;

            // case "TURN":
            // currplayerindex++;
            // break;
            // default:

            // break;
            // }
            // currplayerindex++;
            if (currplayerindex == numPlayers) {
                currplayerindex = 0;
            }
        }
    }

    public void quitGame() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
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
                System.out.println();
                System.exit(0);
            } else if (answer.equals("N")) {
                break;
            } else {
                System.out.println("Please enter Y or N");
            }
        }
    }

    public int basicChoices(Player p) {
        Scanner scanner = new Scanner(System.in); // DO NOT CLOSE SCANNER -- WILL BREAK THINGS
        // String playerName = p.getName();
        Set playerLocation = p.getLocation();
        Scene scene = playerLocation.getScene();
        LinkedList<Role> totalRoles = new LinkedList<Role>();
        // System.out.println()
        String op;
        while (true) {
            System.out.println("\nPlease enter your move (Turn, Quit, Take, Move or Upgrade)");
            op = scanner.nextLine();
            op = op.toUpperCase();
            switch (op) {
                case "QUIT":
                    quitGame();
                    break;
                case "TURN":
                    return 1;
                case "TAKE":
                    int ret = takeRole(p);
                    if (ret == 0) {
                        break;
                    } else {
                        return ret;
                    }
                case "MOVE":
                    move(p);
                    break;
                case "UPGRADE":

                    break;
                default:
                    System.out.println("Please enter a valid option\n");
                    break;
            }
            break;
        }
        return 0;
    }

    public void actingChoices(Player p, int pindex) {

    }

    public void move(Player player) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWhere would you like to move to?");
        Set location = player.getLocation();
        LinkedList<Set> neighbors = location.getNeighbors();
        int count = location.getNeighbors().size();
        for (int i = 0; i < count; i++) {
            Set currSet = neighbors.get(i);
            System.out.println(" " + i + " - Location: " + currSet.getName());
        }
        while (true) {
            System.out.print("Select a location to move to (0-" + (count - 1) + "): ");
            String input = scanner.nextLine();
            if (isNumeric(input)) {
                int pick = Integer.parseInt(input);
                if (pick > count - 1 || pick < 0) {
                    System.out.println("\nPlease enter a valid option");
                } else {
                    player.setLocation(neighbors.get(pick));
                    // debugBoard(2);
                    return;
                }
            } else {
                System.out.println("\nPlease enter a valid option");
            }
        }
    }

    public int takeRole(Player player) { // return 1 for successful role fill, 0 for no roles offered
        Scanner pinput = new Scanner(System.in);
        LinkedList<Role> totalRoles = new LinkedList<Role>();
        Set playerLocation = player.getLocation();
        Scene scene = playerLocation.getScene();
        // debugBoard(3);
        if (playerLocation.getName().equals("trailer") || playerLocation.getName().equals("office")) {
            System.out.println("\nNo roles are offered at: " + playerLocation.getName());
            return 0;
        }
        LinkedList<Role> sceneRoles = playerLocation.getAvailableRoles();
        // System.out.println("num scene roles: " + sceneRoles.size());
        LinkedList<Role> setRoles = scene.getAvailableRoles();
        // System.out.println("num set roles: " + setRoles.size());
        totalRoles.addAll(sceneRoles);
        totalRoles.addAll(setRoles);
        int size = totalRoles.size();
        if (size == 0) {
            System.out.println("No available roles");
            return 0;
        } else {
            String choice;
            while (true) {
                System.out.println("Pick a role: ");
                for (int i = 0; i < size; i++) {
                    Role currRole = totalRoles.get(i);
                    System.out.println(i + " - Role: " + currRole.getTitle() + " minimum rank: " + currRole.getRank()
                            + " is an extra: " + currRole.isExtra());
                }
                choice = pinput.nextLine();
                if (isNumeric(choice)) {
                    int pick = Integer.parseInt(choice);
                    // Role chosen = totalRoles.get(pick);
                    if (pick < 0 || pick > size - 1) {
                        System.out.println("Please pick a valid number\n");
                    } else if (player.getRank() < totalRoles.get(pick).getRank()) {
                        System.out.println("Not high enough rank, please choose a valid role\n");
                    } else {
                        Role chosen = totalRoles.get(pick);
                        System.out.println("Role: '" + chosen.getTitle() + "' taken");
                        System.out.println(" Line: " + chosen.getDescription());
                        player.setRole(chosen);
                        chosen.fillRole();
                        // scanner.close();
                        return 1;
                    }
                } else {
                    System.out.println("Please pick a valid number\n");
                }
            }
        }
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

    public void upgrade() {

    }

    // public boolean isNumeric(String s) {
    // for (int i = 0; i < s.length(); i++) {
    // if (!(Character.isDigit(s.charAt(i)))) {
    // return false;
    // }
    // }
    // return true;
    // }
    public boolean isNumeric(String s) {
        int i = 0;
        if (s.charAt(0) == '-') {
            i = 1;
        }
        for (; i < s.length(); i++) {
            if (!(Character.isDigit(s.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public void debugBoard(int opt) {

        switch (opt) {
            case 5:
                for (int i = 0; i < board.numSets(); i++) {
                    System.out.println("Set: " + board.getSets().get(i).getName());
                }
            case 4:
                for (int i = 0; i < board.numSets(); i++) {
                    Set currset = board.getSets().get(i);
                    int numNbrs = currset.getNeighbors().size();
                    System.out.println(" Neighbors:");
                    for (int j = 0; j < numNbrs; j++) {
                        Set nbr = currset.getNeighbors().get(j);
                        System.out.println(" -" + nbr.getName());
                    }
                }
                break;
            case 3:
                for (int i = 0; i < board.numSets(); i++) {
                    Set currset = board.getSets().get(i);
                    System.out.println("Set: " + currset.getName());
                    int numNbrs = currset.getNeighbors().size();
                    int nroles = currset.getRoleCount();
                    LinkedList<Role> roles = currset.getRoles();
                    System.out.println(" Roles:");
                    for (int j = 0; j < nroles; j++) {
                        System.out.println(" -" + roles.get(j).getTitle());
                    }
                    System.out.println(" Neighbors:");
                    for (int j = 0; j < numNbrs; j++) {
                        Set nbr = currset.getNeighbors().get(j);
                        System.out.println(" -" + nbr.getName());
                    }
                }
                break;
            case 2:
                for (int i = 0; i < board.numSets(); i++) {
                    Set currset = board.getSets().get(i);
                    System.out.println("Set: " + currset.getName());
                    int numNbrs = currset.getNeighbors().size();
                    int nroles = currset.getRoleCount();
                    // LinkedList<Role> roles = currset.getRoles();
                    // System.out.println(" Roles:");
                    // for (int j = 0; j < nroles; j++) {
                    // System.out.println(" -" + roles.get(j).getTitle());
                    // }
                    System.out.println(" Neighbors:");
                    for (int j = 0; j < numNbrs; j++) {
                        Set nbr = currset.getNeighbors().get(j);
                        System.out.println(" -" + nbr.getName());
                        LinkedList<Role> roles = currset.getRoles();
                        System.out.println("  nRoles:");
                        for (int p = 0; p < nroles; p++) {
                            System.out.println("  -" + roles.get(p).getTitle());
                        }
                    }
                }
                break;
        }
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