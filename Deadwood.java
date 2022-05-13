
/**
 * Clement Faisandier and Evan Johnson
 * Spring 2022
 * CS 345 - Object Oriented Design
 * Western Washington University
 * Professor: Dr. Sharmin
 * We (Clement and Evan) attest that this implementation
 * of Deadwood is a work of our own
 * 
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

    private static final String[] PLAYER_NAMES = { "Blue", "Cyan", "Green", "Orange", "Pink", "Red", "Violet",
            "Yellow" };
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
            if(numPlayers<2||numPlayers>8){
                Exception e = new Exception();
                throw e;
            }

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
        int dayCount = 4;
        if(numPlayers == 2 || numPlayers==3){
            dayCount = 3;
        }
        // System.out.println("Welcome to Deadwood!");

        for (int i = 1; i <= dayCount; i++) {

            System.out.println("Day " + i);
            board.distributeScenes(retrieveDailyCards(cards)); // Assigns a scene to each set (10 a day)
            board.resetTiles();
            for(int j=0;j<numPlayers;j++){ // assert players start in trailer at beginning of each day
                Player temp = board.getPlayer(j);
                temp.setLocation(board.getTrailer());
            }
            dailyRoutine(scanner);
        }
        scanner.close();
        wrapUp();
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
            e.printStackTrace();
        }

        // Use data to populate board

        board = Board.getBoard();
        board.addSets(sets);
        board.setTrailer(sets.get(sets.size() - 2));
        board.setOffice(sets.get(sets.size() - 1));
        Collections.shuffle(cards);

        // Populate players and change their attributes depending on player count

        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(PLAYER_NAMES[i], i);
            if(numPlayers == 5){
                player.addCredits(2);
            }else if(numPlayers == 6){
                player.addCredits(4);
            }else if(numPlayers ==7 || numPlayers ==8){
                player.setRank(2);
            }
            board.addPlayer(player);
        }

        // Populate trailer with players, initialize player role as null
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
            if(playerLocation.isComplete() && currentPlayer.checkInRole()){
                System.out.println("Congrats! Your scene was completed!");
                currentPlayer.setRole(null);

            }

            if(playerLocation.isComplete()){
                System.out.println("\nThis scene has already been completed");
            }

            if (currentPlayer.checkInRole()&&!playerLocation.isComplete()) {
                opt = actingChoices(currentPlayer);
                if (opt == 1) {
                    currplayerindex++;
                }
            } 
            else {
                opt = basicChoices(currentPlayer);
                if (opt == 1) {
                    currplayerindex++;
                }
                if(opt ==2){
                    board.completeAll();
                }
            }
            

            if(board.dayEnd()){
                break;
            }


            if (currplayerindex == numPlayers) {
                currplayerindex = 0;
                for(int i=0;i<numPlayers;i++){ // assert players can move again after their turn has completed 
                    Player temp = board.getPlayer(i); // doesnt matter if player is already in a role. Current set up doesnt allow them to move if they are
                    if(!temp.canMove()){
                        temp.allowMove();
                    }
                }
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
                    System.out.println(" "+board.getPlayer(i).getName() + "'s score: " + board.getPlayer(i).calculateScore());
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

    public void wrapUp(){
        System.out.println("\nGAME OVER\n");
        System.out.println("Final scores:");
        for (int i = 0; i < numPlayers; i++) {
            System.out.println(" "+board.getPlayer(i).getName() + "'s score: " + board.getPlayer(i).calculateScore());
        }
        System.out.println();
        System.exit(0);
    }

    public int basicChoices(Player p) {
        Scanner scanner = new Scanner(System.in); // DO NOT CLOSE SCANNER -- WILL BREAK THINGS
        // String playerName = p.getName();
        Set playerLocation = p.getLocation();
        Scene scene = playerLocation.getScene();
        // System.out.println()
        String op;
        while (true) {
            System.out.println("\nPlease enter your move (Turn, Quit, Work, Move or Upgrade)");
            op = scanner.nextLine();
            op = op.toUpperCase();
            switch (op) {
                case "QUIT":
                    quitGame();
                    break;
                case "TURN":
                    return 1;
                case "WORK":
                    int ret = takeRole(p);
                    if (ret == 0) {
                        break;
                    } else {
                        return ret;
                    }
                case "MOVE":
                    if(p.canMove()){
                        move(p);
                    }else{
                        System.out.println("\nYou have already moved this turn\n");
                    }
                    break;
                case "UPGRADE":
                    if(p.getRank() == 6){
                        System.out.println("\nYou are already max rank. Upgrade denied\n");
                        break;
                    }
                    if(playerLocation == board.getOffice()){
                        upgrade(p);
                        break;
                    }else{
                        System.out.println("\nPlease move to the office to upgrade...\n");
                        break;
                    }
                case "DAY": //DELETE THIS IN FINAL PRODUCT
                    return 2;
                default:
                    System.out.println("Please enter a valid option\n");
                    break;
            }
            break;
        }
        return 0;
    }

    public int actingChoices(Player p) {
        System.out.println("Welcome actor!");
        Scanner pinput = new Scanner(System.in);
        String choice;
        Scene currScene = p.getLocation().getScene();
        Role currRole = p.getRole();
        int budget = currScene.getBudget();
        while (true) {
            System.out.println("\nWhat would you like to do? (Act, Rehearse, Turn or Quit)");
            System.out.println(" Your current role is: "+currRole.getTitle()+"\n Is an extra: "+ currRole.isExtra());
            System.out.println(" You currently have: "+p.getChips()+" chip(s)");
            choice = pinput.nextLine();
            choice = choice.toUpperCase();
            switch(choice){
                case "ACT":

                    break;
                case "REHEARSE":
                    if(p.getChips() == budget){
                        System.out.println("\nYou cannot rehearse anymore. Please act\n");
                    }else{
                        System.out.println("\nYou chose to rehearse\n");
                        p.addChip();
                        return 1;
                    }
                    break;
                case "QUIT":
                    quitGame();
                    break;
                case "TURN":
                    return 1;
                default:
                    System.out.println("\nPlease make a valid choice\n");
            }
        }

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
        }System.out.println(" "+count +" - GO BACK");
        while (true) {
            //System.out.print("Select a location to move to (0-" + (count - 1) + "): ");
            String input = scanner.nextLine();
            if (isNumeric(input)) {
                int pick = Integer.parseInt(input);
                if (pick > count|| pick < 0) {
                    System.out.println("\nPlease enter a valid option (0-"+count+")");
                }else if(pick == count){
                    return;
                } 
                else {
                    player.setLocation(neighbors.get(pick));
                    player.moved();
                    player.resetChips();
                    // debugBoard(2);
                    return;
                }
            } else {
                System.out.println("\nPlease enter a valid option");
            }
        }
    }

    public int takeRole(Player player) { // return 1 for successful role fill, 0 for no roles offered or back
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
        if (size == 0 || playerLocation.isComplete()) {
            System.out.println("No available roles");
            return 0;
        } else {
            String choice;
            while (true) {
                System.out.println("\nPick a role: ");
                for (int i = 0; i < size; i++) {
                    Role currRole = totalRoles.get(i);
                    System.out.println(" "+i + " - Role: " + currRole.getTitle() + " Minimum rank: " + currRole.getRank()
                            + " Is an extra: " + currRole.isExtra());
                }
                System.out.println(" "+size +" - GO BACK");
                choice = pinput.nextLine();
                if (isNumeric(choice)) {
                    int pick = Integer.parseInt(choice);
                    // Role chosen = totalRoles.get(pick);
                    if (pick < 0 || pick > size) {
                        System.out.println("Please pick a valid number\n");
                    } else if(pick == size){
                        return 0;
                    }else if (player.getRank() < totalRoles.get(pick).getRank()) {
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

    public void upgrade(Player player) {
        int rank = player.getRank();
        
        int playerMoney= player.getMoney();
        int playerCredit=player.getCredits();
        Set office = player.getLocation();
        LinkedList<String[]> creditLegend =office.getCreditLegend();
        LinkedList<String[]> moneyLegend = office.getMoneyLegend();
        String[] moneyCost = moneyLegend.get(rank-1);
        String[] creditCost = creditLegend.get(rank-1);
        System.out.println(Arrays.toString(moneyCost));
        System.out.println(Arrays.toString(creditCost));

    }

    public void act(Player player, Set set, Scene scene, Board board) {

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
        //Board board = Board.getBoard();

        switch (opt) {
            case 5:
                for (int i = 0; i < board.numSets(); i++) {
                    System.out.println("Set: " + board.getSets().get(i).getName());
                }
            case 4: // FALL THROUGH ERROR IS ON PURPOSE PLEASE DONT TOUCH
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