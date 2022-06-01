
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
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class Deadwood{
    private static final String[] PLAYER_NAMES = { "Blue", "Cyan", "Green", "Orange", "Pink", "Red", "Violet",
            "Yellow" };
    private static Board board;
    private static int numPlayers;
    private static GUI frame = null;

    private static GUI.boardMouseListener listener;
    private static JTextArea area = null;
    private static LinkedList<Scene> cards;
    private static int dayCount=4;
    private static int currplayerindex =0;

    public Deadwood(GUI frame, int playerCount){
      this.frame = frame;
      this.listener = frame.mouseListener;
      this.numPlayers = playerCount;
      this.cards = setupProcedure(playerCount);
      if (numPlayers == 2 || numPlayers == 3) {
          this.dayCount = 3;
      }
    }

    // method where the gui boots up the game logic
    public void run() {
        Scanner scanner = new Scanner(System.in);
        for (int i = 1; i <= dayCount; i++) { // this it the main day loop
            String day = "\nDay " + i;
            frame.addText(day);
            System.out.println("Day " + i);
            board.distributeScenes(retrieveDailyCards(cards),frame); // Assigns a scene to each set (10 a day)
            board.resetTiles(); // prepare the tiles
            frame.initSceneButtons(board.getSets());
            frame.resetPlayerIcons();
            currplayerindex = 0;
            for (int j = 0; j < numPlayers; j++) { // assert players start in trailer at beginning of each day
                Player temp = board.getPlayer(j);
                temp.setLocation(board.getTrailer()); // change back to trailer after testing upgrade
                temp.allowMove();
            }
            dailyRoutine(); // run main game loop for the player
            loopStop();
            frame.resetTakeMarkers(board.getSets());
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
        frame.initBoardTiles(sets);
        frame.initBoardButtons(sets);
        frame.initSetButtons(board.getSetRoles());
        frame.initTakeMarkers(sets);
        Collections.shuffle(cards); // shuffle so cards arent in order

        // Populate players and change their attributes depending on player count
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(PLAYER_NAMES[i],i);
            // player.addCredits(76);
            // player.addMoney(101); // DELETE THIS AFTER TESTING
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
    public void dailyRoutine(){
          Player currentPlayer = board.getPlayer(currplayerindex); // get the current player
          listener.setCurrPlayer(currentPlayer);
          String playerName = currentPlayer.getName(); // get players name
          Set playerLocation = currentPlayer.getLocation(); // get players location
          int shotsRemaining = playerLocation.shotsRemaining(); // get the shots remaining for players' location
          System.out.println("\n" + playerName + "'s turn! \n You have: ($" + currentPlayer.getMoney() + ", "
                  + currentPlayer.getCredits() + " cr)\n Your location is: " + currentPlayer.getLocName());

          String turnline = "\n" + playerName + "'s turn! \n You have: ($" + currentPlayer.getMoney() + ", "+ currentPlayer.getCredits() + " cr)\n Your location is: " + currentPlayer.getLocName() + "\n Your rank is: " + currentPlayer.getRank()+"\n Your chip count: " +currentPlayer.getChips();
          frame.addText(turnline);
          if ((shotsRemaining == 0 && currentPlayer.checkInRole())) { // check if the players current acting gig is
                                                                      // completed
              System.out.println("1Congrats! Your scene was completed!");
              String congrats = "1Congrats! Your scene was completed!";
              frame.addText(congrats);
              currentPlayer.setRole(null); // reset the players current role
              currentPlayer.resetChips(); // reset their chip count
              playerLocation.complete(frame,board.getSetIndex(playerLocation.getName())); // say location is complete
          }else if ((playerLocation.isComplete() && currentPlayer.checkInRole())) { // check if players role is
              String congrats = "2Congrats! Your scene was completed!";
              frame.addText(congrats);                                                                     // completed -- may be redundant
              System.out.println(congrats);
              currentPlayer.setRole(null);
              currentPlayer.resetChips();
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

    public void quitGame() { // exit method that prompts user if they want to quit
        while (true) {
            String answer = JOptionPane.showInputDialog("Are You Sure? (Y) or (N)");
            if(answer==null){
              return;
            }
            answer = answer.toUpperCase();
            if (answer.equals("Y")) {
                StringBuilder endgame = new StringBuilder();
                endgame.append("GAME OVER\nFinal scores:\n");
                frame.addText("GAME OVER\n");
                System.out.println("\nGAME OVER\n");
                for (int i = 0; i < numPlayers; i++) { // print out each players end game score
                    System.out.println(" " + board.getPlayer(i).getName() + "'s score: " + board.getPlayer(i).calculateScore());
                    frame.addText(" " + board.getPlayer(i).getName() + "'s score: " + board.getPlayer(i).calculateScore());
                    endgame.append(" " + board.getPlayer(i).getName() + "'s score: " + board.getPlayer(i).calculateScore()+"\n");
                }
                System.out.println();
                JOptionPane.showMessageDialog(null,endgame.toString());
                System.exit(0);
            } else if (answer.equals("N")) { // return back to game loop
                return;
            } else {
                System.out.println("Please enter Y or N"); // invalid input was given
            }
        }
    }

    public void loopStop(){
      Scanner scanner = new Scanner(System.in);
      while(true){
        String answer=null;//= scanner.nextLine();
        //answer=answer.toUpperCase();
        if("QUIT".equals(answer)){
          quitGame();
        }else if("DAY".equals(answer)){
          board.completeAll(frame);
        }
        if(board.dayEnd()){
          break;
        }
      }
    }

    public void wrapUp() { // end game method that doesnt take input
        System.out.println("\nGAME OVER\n");
        System.out.println("Final scores:");
        StringBuilder endgame = new StringBuilder();
        String[] endscores = new String[numPlayers];
        endgame.append("GAME OVER\nFinal scores:\n");
        for (int i = 0; i < numPlayers; i++) { // print players end game score
            endgame.append(" "+board.getPlayer(i).getName() + "'s score: " + board.getPlayer(i).calculateScore()+"\n");
        }
        System.out.println();
        JOptionPane.showMessageDialog(null,endgame.toString());
        System.exit(0);
    }

    public void move(Player player,Set location) { // show the locations a player can move to. prompt them to pick one
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWhere would you like to move to?");
        player.setLocation(location);
        player.moved();
        player.resetChips();
    }

    public void changePlayer(){
      currplayerindex++;
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
                    } else if (player.getRank() < totalRoles.get(pick).getRank()) { // player chose a role without the means
                        System.out.println("Not high enough rank, please choose a valid role\n");
                    } else {
                        Role chosen = totalRoles.get(pick); // give player the chosen role
                        System.out.println("Role: '" + chosen.getTitle() + "' taken");
                        System.out.println(" Line: " + chosen.getDescription());
                        player.setRole(chosen);
                        chosen.fillRole();
                        return 1; // end the turn
                    }
                } else {
                    System.out.println("Please pick a valid number\n");
                }
            }
        }
    }

    public int upgrade(Player player) {
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
                String[] options = {"Money","Credits"};
                choice = ""+JOptionPane.showOptionDialog(null, "You have the choice of spending money or credits","Pick method of payment",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                choice = choice.toUpperCase();
                switch (choice) {
                    case "0": // spend their money and increase rank
                        System.out.println("Rank increased");
                        player.subMoney(mCost);
                        player.increaseRank();
                        return 1;
                    case "1": // spend their credits and increase their rank
                        System.out.println("Rank increased");
                        player.subCredits(cCost);
                        player.increaseRank();
                        return 1;
                    default: // enter valid option
                        System.out.println("\nPlease enter a valid option (M or C)\n");
                        break;
                }
            }
        } else if (playerMoney >= mCost) { // player only has enough money to upgrade rank
            System.out.println("You spent money to increase \nyour rank");
            frame.addText("\nYou spent money to increase \nyour rank");
            player.subMoney(mCost);
            player.increaseRank();
            return 1;
        } else if (playerCredit >= cCost) { // player only has enough credits to upgrade rank
            System.out.println("You spent credits to increase your rank");
            frame.addText("\nYou spent credits to increase \nyour rank");
            player.subCredits(cCost);
            player.increaseRank();
            return 1;
        } else { // player doesnt have the means to increase their rank
            System.out.println("\nYou dont have the resources to increase your rank. Try again later.");
            frame.addText("\nYou dont have the resources to increase your rank.\n Try again later.");
            return 0;
        }
    }

    public void act(Player player, Role currRole) { // player chose to act
        int budget = player.getLocation().getScene().getBudget();
        Set currSet = player.getLocation();
        int roll = (int) (Math.random() * (6 - 1 + 1) + 1); // role the dice
        System.out.println("You rolled a: " + roll);
        int total = roll + player.getChips(); // get their total roll
        if (total >= budget) { // if the roll is successful
            System.out.println("\nAct success! Your total was: " + total);
            frame.addText("\nAct success!\n Your total was: " + total);
            frame.hideTakeMarker(currSet,currSet.getCurrShot());
            currSet.completeShot(); // increase amount of shots completed
            if (currSet.shotsRemaining() == 0) { // if tile is done, determine if their is a bonus to pay out
                payout(currSet);
                String congrats = "Congrats! Your scene was completed!";
                frame.addText(congrats);
                player.setRole(null); // reset the players current role
                player.resetChips(); // reset their chip count
                currSet.complete(frame,board.getSetIndex(currSet.getName())); // say location is complete
            }
            if (currRole.isExtra()) { // pay the player the amount determined by role
                System.out.println("Payout: 1 dollar and 1 credit");
                frame.addText(" Payout: 1 dollar and 1 credit");
                player.addCredits(1);
                player.addMoney(1);
            } else {
                System.out.println(" Payout: 2 credits");
                frame.addText(" Payout: 2 credits");
                player.addCredits(2);
            }
            return;
        } else {
            System.out.println("Act failure! Your total was: " + total);
            frame.addText("Act failure! Your total was: " + total);
            if (currRole.isExtra()) { // if the player was an extra, they still get paid
                System.out.println(" You're an extra though, you still get a dollar");
                frame.addText(" You're an extra though, you still get a dollar");
                player.addMoney(1);
            } else {
                System.out.println(" Sucks to not be an extra. You get nothing");
                frame.addText(" Sucks to not be an extra. You get nothing");
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
            System.out.println("No lead actors were present.\n No bonuses will be awarded");
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
    // returns an arraylist of people in their role spots.
    public ArrayList<Player> getSpots(LinkedList<Role> roleList, ArrayList<Player> playersInRole) {
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
        ArrayList<Player> playersInRole = new ArrayList<Player>();
        int rolecount = roleList.size();
        for (int i = 0; i < roleList.size(); i++) {
            playersInRole.add(null);
        }
        getSpots(roleList, playersInRole);
        for (int i = 0; i < budget; i++) {
            int roll = (int) (Math.random() * (6 - 1 + 1) + 1);
            diceRolls.add(roll);
        }
        Collections.sort(diceRolls, Collections.reverseOrder());
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
                set.complete(frame,-1);
                return true;
            }
        }
        return false;
    }

    public Board getBoard(){
      return this.board;
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
