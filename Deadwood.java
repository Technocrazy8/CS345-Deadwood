
/**
 * Evan Johnson and Clement Faisandier
 * Spring 2022
 * CS 345 - Object Oriented Design
 * Western Washington University
 * Professor: Dr. Sharmin
 * We (Evan and Clement) attest that this implementation
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
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
//public static final BoardLayersListener listener = getlistener();
public class Deadwood{
    private static final String[] PLAYER_NAMES = { "Blue", "Cyan", "Green", "Orange", "Pink", "Red", "Violet",
            "Yellow" };
    private static Board board;
    private static int numPlayers;
    private static JFrame frame = new JFrame("Deadwood");
    private static JPanel mainPanel = new JPanel();
    private static JLabel boardlabel = new JLabel();
    //private static BoardLayersListener listener = getlistener();
    //private static

    // public static void main(String[] args) {
    //     Deadwood game = new Deadwood();
    //     // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     //
    //     // boardlabel.setIcon(new ImageIcon("Deadwood Needed Image Files/board.jpg"));
    //     // Dimension size = boardlabel.getPreferredSize();
    //     // //label.setPreferredSize(new Dimension(175, 100));
    //     // boardlabel.setBounds(0,0,size.width,size.height);
    //     // frame.getContentPane().add(boardlabel, BorderLayout.CENTER);
    //     // Button quitbtn = new Button("Quit");
    //     // Button turnbtn = new Button("End turn");
    //     // //btn.setBounds(50, 50, 150, 150);
    //     // quitbtn.setPreferredSize(new Dimension(100,75));
    //     // mainPanel.setLayout(new GridLayout(3,2));
    //     // mainPanel.setBounds(5000,5000,5000,5000);
    //     // mainPanel.setLocation(15000,11500);
    //     // mainPanel.add(quitbtn);
    //     // mainPanel.add(turnbtn);
    //     // frame.add(mainPanel);
    //     // frame.setSize(150, 150);
    //     // frame.setTitle("DeadWood");
    //     // frame.setLayout(new FlowLayout());
    //     // frame.pack();
    //     // frame.setVisible(true);
    //
    //     System.out.println("Welcome to Deadwood!\n");
    //     int numPlayers = 0;
    //
    //     if (args.length != 1) {
    //         System.out.println("Invalid arguments:\njava Deadwood p\np = number of players: [2,8]");
    //         return;
    //     }
    //
    //     try {
    //         numPlayers = Integer.parseInt(args[0]);
    //         if (numPlayers < 2 || numPlayers > 8) {
    //             Exception e = new Exception();
    //             throw e;
    //         }
    //
    //     } catch (Exception e) {
    //         System.out.println("Invalid arguments:\njava Deadwood p\np = number of players: [2,8]");
    //         return;
    //     }
    //     game.run(numPlayers);
    // }

    public void run(int playerCount) {
        // BoardLayersListener viewer = new BoardLayersListener();
        // viewer.setVisible(true);
        // JOptionPane.showInputDialog(board, "How many players?");
        numPlayers = playerCount;
        Scanner scanner = new Scanner(System.in);
        LinkedList<Scene> cards = setupProcedure(numPlayers);
        int dayCount = 4;
        if (numPlayers == 2 || numPlayers == 3) {
            dayCount = 3;
        }
        // System.out.println("Welcome to Deadwood!");

        for (int i = 1; i <= dayCount; i++) { // this it the main day loop
            String day = "Day " + i +"\n";
            BoardLayersListener.addText(day);
            System.out.println("Day " + i);
            board.distributeScenes(retrieveDailyCards(cards)); // Assigns a scene to each set (10 a day)
            board.resetTiles(); // prepare the tiles

            for (int j = 0; j < numPlayers; j++) { // assert players start in trailer at beginning of each day
                Player temp = board.getPlayer(j);
                temp.setLocation(board.getTrailer()); // change back to trailer after testing upgrade
            }
            dailyRoutine(scanner); // run main game loop for the player
        }
        scanner.close();
        wrapUp(); // modified quit game that doesnt prompt for input
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
                        sets = parser.readBoard(d); // grab the board tiles
                        break;
                    case 1:
                        cards = parser.readCards(d); // grab the cards
                        break;
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // Use data to populate board

        board = Board.getBoard(); // set the board global variable
        board.addSets(sets); // add all the sets to the board
        board.setTrailer(sets.get(sets.size() - 2)); // add the trailer tile to the board
        board.setOffice(sets.get(sets.size() - 1)); // add the office tile to the board
        Collections.shuffle(cards); // shuffle so cards arent in order

        // Populate players and change their attributes depending on player count

        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(PLAYER_NAMES[i]);
            // player.addCredits(76);
            // player.addMoney(101);
            if (numPlayers == 5) {
                player.addCredits(2);
            } else if (numPlayers == 6) {
                player.addCredits(4);
            } else if (numPlayers == 7 || numPlayers == 8) {
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

    // main turn loop for each player
    private void dailyRoutine(Scanner scanner) {
        String input;
        // boolean toggle = true;
        int currplayerindex = 0;
        // int

        while (true) {

            Player currentPlayer = board.getPlayer(currplayerindex); // get the current player
            String playerName = currentPlayer.getName(); // get players name
            Set playerLocation = currentPlayer.getLocation(); // get players location
            int shotsRemaining = playerLocation.shotsRemaining(); // get the shots remaining for players' location
            System.out.println("\n" + playerName + "'s turn! \n You have: ($" + currentPlayer.getMoney() + ", "
                    + currentPlayer.getCredits() + " cr)\n Your location is: " + currentPlayer.getLocName());
            System.out.println(" Your rank is: " + currentPlayer.getRank()); // print out the players attributes
            int opt;
            // debugBoard(3);
            if ((shotsRemaining == 0 && currentPlayer.checkInRole())) { // check if the players current acting gig is
                                                                        // completed
                System.out.println("Congrats! Your scene was completed!");
                currentPlayer.setRole(null); // reset the players current role
                currentPlayer.resetChips(); // reset their chip count
                playerLocation.complete(); // say location is complete
            }

            else if ((playerLocation.isComplete() && currentPlayer.checkInRole())) { // check if players role is
                                                                                     // completed -- may be redundant
                System.out.println("Congrats! Your scene was completed!");
                currentPlayer.setRole(null);
                currentPlayer.resetChips();
            }

            else if (playerLocation.isComplete()) { // if a player moves to a completed scene
                System.out.println("\nThis scene has already been completed");
            }

            if (currentPlayer.checkInRole() && !playerLocation.isComplete()) { // check if player is in role and if
                                                                               // their scene isnt completed
                opt = actingChoices(currentPlayer); // give the player the choices for acting
                if (opt == 1) { // player finished their turn
                    currplayerindex++;
                }
            } else {
                opt = basicChoices(currentPlayer); // player completed their turn
                if (opt == 1) {
                    currplayerindex++;
                }
                if (opt == 2) { // for testing purposes
                    board.completeAll();
                }
            }

            if (board.dayEnd()) { // if 9/10 scenes are completed, end the day
                break;
            }

            if (currplayerindex == numPlayers) { // loop back around to starting player
                currplayerindex = 0;
                for (int i = 0; i < numPlayers; i++) { // assert players can move again after their turn has completed
                    Player temp = board.getPlayer(i); // doesnt matter if player is already in a role. Current set up
                                                      // doesnt allow them to move if they already did in the turn
                    if (!temp.canMove()) {
                        temp.allowMove();
                    }
                }
            }
        }
    }

    public void quitGame() { // exit method that prompts user if they want to quit
        Scanner scanner = new Scanner(System.in);
        while (true) {
            //System.out.print("Are you sure? (Y) or (N): ");
            String answer = JOptionPane.showInputDialog("Are You Sure? (Y) or (N)");
            answer = answer.toUpperCase();
            if (answer.equals("Y")) {
                System.out.println("\nGAME OVER\n");
                for (int i = 0; i < numPlayers; i++) { // print out each players end game score
                    System.out.println(
                            " " + board.getPlayer(i).getName() + "'s score: " + board.getPlayer(i).calculateScore());
                }
                scanner.close();
                System.out.println();
                System.exit(0);
            } else if (answer.equals("N")) { // return back to game loop
                return;
            } else {
                System.out.println("Please enter Y or N"); // invalid input was given
            }
        }
    }

    public void wrapUp() { // end game method that doesnt take input
        System.out.println("\nGAME OVER\n");
        System.out.println("Final scores:");
        for (int i = 0; i < numPlayers; i++) { // print players end game score
            System.out.println(" " + board.getPlayer(i).getName() + "'s score: " + board.getPlayer(i).calculateScore());
        }
        System.out.println();
        System.exit(0);
    }

    public int basicChoices(Player p) { // present the choices to the player who isnt acting
        Scanner scanner = new Scanner(System.in); // DO NOT CLOSE SCANNER -- WILL BREAK THINGS
        Set playerLocation = p.getLocation();
        String op;
        while (true) {
            System.out.println("\nPlease enter your move (Turn, Quit, Work, Move or Upgrade)");
            op = scanner.nextLine();
            op = op.toUpperCase();
            switch (op) {
                case "QUIT":
                    quitGame(); // end game
                    break;
                case "TURN":
                    return 1; // return 1 to change player
                case "WORK":
                    int ret = takeRole(p); // ask player what role to take
                    if (ret == 0) {
                        break;
                    } else {
                        return ret; // if a role was taken return a 1 signifying end of turn
                    }
                case "MOVE":
                    if (p.canMove()) { // if a player hasnt moved in their turn already
                        move(p); // allow them to move
                    } else {
                        System.out.println("\nYou have already moved this turn\n");
                    }
                    break;
                case "UPGRADE":
                    if (p.getRank() == 6) { // player cant upgrade past rank 6
                        System.out.println("\nYou are already max rank. Upgrade denied\n");
                        break;
                    }
                    if (playerLocation == board.getOffice()) { // assert player is at office before letting them upgrade
                        upgrade(p);
                        break;
                    } else {
                        System.out.println("\nPlease move to the office to upgrade...\n");
                        break;
                    }
                    // case "DAY": // DELETE THIS IN FINAL PRODUCT
                    // return 2;
                default:
                    System.out.println("Please enter a valid option\n");
                    break;
            }
            break;
        }
        return 0;
    }

    public int actingChoices(Player p) { // present the choices to the actor
        // System.out.println("\nWelcome actor!");
        Scanner pinput = new Scanner(System.in);
        String choice;
        Scene currScene = p.getLocation().getScene();
        Role currRole = p.getRole();
        int budget = currScene.getBudget();
        // System.out.println(" There are " + p.getLocation().shotsRemaining() + " shots
        // remaining");
        while (true) {
            System.out.println("\nWhat would you like to do? (Act, Rehearse or Quit)");
            System.out.println(" Your current role is: " + currRole.getTitle() + "\n Is an extra: " + currRole.isExtra()
                    + "\n Scene budget: " + budget);
            System.out.println(" Shots remaining: " + p.getLocation().shotsRemaining());
            System.out.println(" You currently have: " + p.getChips() + " chip(s)");
            choice = pinput.nextLine();
            choice = choice.toUpperCase();
            switch (choice) {
                case "ACT":
                    act(p, currRole); // let player act
                    return 1;
                case "REHEARSE":
                    if (p.getChips() == budget - 1) { // if player is at a point of guaranteed succes, dont let them
                                                      // rehearse again
                        System.out.println("\nYou cannot rehearse anymore. Please act\n");
                    } else {
                        System.out.println("\nYou chose to rehearse\n");
                        p.addChip(); // incread players chip count
                        return 1;
                    }
                    break;
                case "QUIT":
                    quitGame(); // quit game
                    break;
                default:
                    System.out.println("\nPlease make a valid choice\n");
            }
        }
    }

    public void move(Player player) { // show the locations a player can move to. prompt them to pick one
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWhere would you like to move to?");
        Set location = player.getLocation(); // get players current location
        LinkedList<Set> neighbors = location.getNeighbors(); // grab the neighbor sets
        int count = location.getNeighbors().size();
        for (int i = 0; i < count; i++) { // print neighboring locations
            Set currSet = neighbors.get(i);
            Scene currScene = currSet.getScene();
            if (currScene == null) { // if the neighbor is office or trailer
                System.out.println(" " + i + " - Location: " + currSet.getName());
            } else { // print attributes of neighboring tile
                System.out
                        .println(" " + i + " - Location: " + currSet.getName() + " -Budget: $" + currScene.getBudget());
            }
        }
        System.out.println(" " + count + " - GO BACK"); // give them a go back option
        while (true) { // prompt for input
            String input = scanner.nextLine();
            if (isNumeric(input)) {
                int pick = Integer.parseInt(input);
                if (pick > count || pick < 0) { // make sure player entered a valid choice
                    System.out.println("\nPlease enter a valid option (0-" + count + ")");
                } else if (pick == count) { // go back choice
                    return;
                } else {// move player to the tile
                    player.setLocation(neighbors.get(pick));
                    player.moved();
                    player.resetChips();
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
        if (playerLocation.getName().equals("trailer") || playerLocation.getName().equals("office")) {
            System.out.println("\nNo roles are offered at: " + playerLocation.getName());
            return 0;
        }
        LinkedList<Role> sceneRoles = playerLocation.getAvailableRoles(); // grab the nontaken scene roles
        LinkedList<Role> setRoles = scene.getAvailableRoles(); // grab the nontaken set roles
        totalRoles.addAll(sceneRoles);
        totalRoles.addAll(setRoles);// combine them to one list
        int size = totalRoles.size();
        if (size == 0 || playerLocation.isComplete()) {// if no roles are offered or tile is complete
            System.out.println("No available roles");
            return 0;
        } else {
            String choice;
            while (true) {
                System.out.println("\nPick a role: ");
                for (int i = 0; i < size; i++) {// display the role choices to the player and ask to pick one
                    Role currRole = totalRoles.get(i);
                    System.out.println(
                            " " + i + " - Role: " + currRole.getTitle() + " Minimum rank: " + currRole.getRank()
                                    + " Is an extra: " + currRole.isExtra());
                }
                System.out.println(" " + size + " - GO BACK");
                choice = pinput.nextLine();
                if (isNumeric(choice)) {// prompt user for choice
                    int pick = Integer.parseInt(choice);
                    if (pick < 0 || pick > size) { // if pick out of scope
                        System.out.println("Please pick a valid number\n");
                    } else if (pick == size) {// player picked go back option
                        return 0;
                    } else if (player.getRank() < totalRoles.get(pick).getRank()) { // player chose a role without the
                                                                                    // means
                        System.out.println("Not high enough rank, please choose a valid role\n");
                    } else {
                        Role chosen = totalRoles.get(pick); // give player the chosen role
                        System.out.println("Role: '" + chosen.getTitle() + "' taken");
                        System.out.println(" Line: " + chosen.getDescription());
                        player.setRole(chosen);
                        chosen.fillRole();
                        // scanner.close();
                        return 1; // end the turn
                    }
                } else {
                    System.out.println("Please pick a valid number\n");
                }
            }
        }
    }

    public void upgrade(Player player) {
        int rank = player.getRank();
        Scanner scanner = new Scanner(System.in);
        // grab the players resources
        int playerMoney = player.getMoney();
        int playerCredit = player.getCredits();
        Set office = player.getLocation(); // get curr location
        LinkedList<String[]> creditLegend = office.getCreditLegend();
        LinkedList<String[]> moneyLegend = office.getMoneyLegend(); // grab the two different payment method rubrics
        String[] moneyCost = moneyLegend.get(rank - 1);
        int mCost = Integer.parseInt(moneyCost[2]); // grab the money cost
        String[] creditCost = creditLegend.get(rank - 1);
        int cCost = Integer.parseInt(creditCost[2]); // grab the credit cost
        String choice;
        if (playerMoney >= mCost && playerCredit >= cCost) { // if the player has enough of both credits and money to
                                                             // upgrade
            System.out.println("\nYou have the choice of spending your money or credits");

            while (true) { // give player the choice of what they want to spend
                System.out.println("\nYou have $" + playerMoney + " and " + playerCredit + " credits");
                System.out.print(" What would you like to spend (M or C)? ");
                choice = scanner.nextLine();
                choice = choice.toUpperCase();
                switch (choice) {
                    case "M": // spend their money and increase rank
                        System.out.println("Rank increased");
                        player.subMoney(mCost);
                        player.increaseRank();
                        return;
                    case "C": // spend their credits and increase their rank
                        System.out.println("Rank increased");
                        player.subCredits(cCost);
                        player.increaseRank();
                        return;
                    default: // enter valid option
                        System.out.println("\nPlease enter a valid option (M or C)\n");
                        break;
                }
            }
        } else if (playerMoney >= mCost) { // player only has enough money to upgrade rank
            System.out.println("You spent money to increase your rank");
            player.subMoney(mCost);
            player.increaseRank();
            return;
        } else if (playerCredit >= cCost) { // player only has enough credits to upgrade rank
            System.out.println("You spent credits to increase your rank");
            player.subCredits(cCost);
            player.increaseRank();
            return;
        } else { // player doesnt have the means to increase their rank
            System.out.println("\nYou dont have the resources to increase your rank. Try again later.");
            return;
        }
    }

    public void act(Player player, Role currRole) { // player chose to act
        int budget = player.getLocation().getScene().getBudget();
        Set currSet = player.getLocation();
        int roll = (int) (Math.random() * (6 - 1 + 1) + 1); // role the dice
        System.out.println("You rolled a: " + roll);
        int total = roll + player.getChips(); // get their total roll
        if (total >= budget) { // if the roll is successful
            System.out.println("Act success! Your total was: " + total);
            currSet.completeShot(); // increase amount of shots completed
            if (currSet.shotsRemaining() == 0) { // if tile is done, determine if their is a bonus to pay out
                payout(currSet);
            }
            if (currRole.isExtra()) { // pay the player the amount determined by role
                System.out.println(" Payout: 1 dollar and 1 credit");
                player.addCredits(1);
                player.addMoney(1);
            } else {
                System.out.println(" Payout: 2 credits");
                player.addCredits(2);
            }
            return;
        } else {
            System.out.println("Act failure! Your total was: " + total);
            if (currRole.isExtra()) { // if the player was an extra, they still get paid
                System.out.println(" You're an extra though, you still get a dollar");
                player.addMoney(1);
            } else {
                System.out.println(" Sucks to not be an extra. You get nothing");
            }
            return;
        }
    }

    public void payout(Set currsSet) { // method that determines if a bonus should be rewared
        LinkedList<Role> leads = currsSet.getScene().getParts();
        LinkedList<Role> extras = currsSet.getRoles();
        int count = countLeads(leads);
        int budget = currsSet.getScene().getBudget();
        if (count != 0) {
            payExtras(extras);
            payLeads(leads, budget);
        } else {
            System.out.println("No lead actors were present\n No bonuses will be awarded");
        }
    }

    public int countLeads(LinkedList<Role> roleList) { // counts the amount of players in lead roles for a scene
        int count = 0;
        for (int i = 0; i < numPlayers; i++) {
            Player currPlayer = board.getPlayer(i);
            for (int j = 0; j < roleList.size(); j++) {
                Role currRole = roleList.get(j);
                if (currPlayer.getRole() == currRole) {
                    count++;
                }
            }
        }
        return count;
    }

    public ArrayList<Player> getSpots(LinkedList<Role> roleList, ArrayList<Player> playersInRole) { // returns an
                                                                                                    // arraylist of
                                                                                                    // people in their
                                                                                                    // role spots.
        // similar to how it would look like on the board
        for (int i = 0; i < numPlayers; i++) {
            Player currPlayer = board.getPlayer(i);
            Role playerRole = currPlayer.getRole();
            for (int j = 0; j < roleList.size(); j++) {
                if (playerRole == roleList.get(j)) {
                    playersInRole.set(j, currPlayer);
                }
            }
        }
        return playersInRole;
    }

    // method that pays the players who are in the extra spots of a scene
    public void payExtras(LinkedList<Role> roleList) {
        for (int i = 0; i < numPlayers; i++) {
            Player currPlayer = board.getPlayer(i);
            for (int j = 0; j < roleList.size(); j++) {
                Role currRole = roleList.get(j);
                if (currPlayer.getRole() == currRole) {
                    currPlayer.addMoney(currRole.getRank());
                }
            }
        }
    }

    // method that pays players who are in lead rolls
    public void payLeads(LinkedList<Role> roleList, int budget) {
        ArrayList<Integer> diceRolls = new ArrayList<Integer>();
        // ArrayList<Player> leadPlayers = new ArrayList<Player>();
        ArrayList<Player> playersInRole = new ArrayList<Player>();
        int rolecount = roleList.size();
        for (int i = 0; i < roleList.size(); i++) {
            playersInRole.add(null);
        }
        getSpots(roleList, playersInRole);

        //System.out.println(playersInRole.toString());

        for (int i = 0; i < budget; i++) {
            int roll = (int) (Math.random() * (6 - 1 + 1) + 1);
            diceRolls.add(roll);
        }
        Collections.sort(diceRolls, Collections.reverseOrder());
        // System.out.println()
        for (int i = 0; i < diceRolls.size(); i++) {
            System.out.println("dice roll: " + diceRolls.get(i));
        }
        int index = 0;
        for (int i = 0; i < budget; i++) {
            Player curPlayer = playersInRole.get(index);
            if (curPlayer != null) {
                curPlayer.addMoney(diceRolls.get(i));
            }
            index++;
            if (index == rolecount) {
                index = 0;
            }
        }
    }

    public boolean determineCompletion(Set set) { // method that determines if a scene is wrapped
        LinkedList<Role> occupiedRoles = set.getTakenRoles();
        occupiedRoles.addAll(set.getScene().getTakenRoles());
        int size = occupiedRoles.size();
        for (int i = 0; i < size; i++) {
            Role currRole = occupiedRoles.get(i);
            if (!currRole.isExtra()) {
                //System.out.println("non extra detected");
                set.complete();
                return true;
            }
        }
        return false;
    }

    public boolean isNumeric(String s) { // helper method for input handling
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

    public void debugBoard(int opt) { // method that helps with debugging the board
        // Board board = Board.getBoard();

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
