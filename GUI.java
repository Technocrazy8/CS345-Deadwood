/*

   Deadwood GUI helper file
   Author: Moushumi Sharmin
   Modified by: Evan Johnson
   This file shows how to create a simple GUI using Java Swing and Awt Library
   Classes Used: JFrame, JLabel, JButton, JLayeredPane

*/
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.event.*;
import javax.swing.JOptionPane;


public class GUI extends JFrame {

  private static GUI instance;
  int numPlayers =0;
  String[] colors = { "b", "c", "g", "o", "p", "r", "v",
          "y" };

  boardMouseListener mouseListener = new boardMouseListener();

  // JLabels
  JLabel boardlabel;
  JLabel cardlabel;
  JLabel playerlabel;
  JLabel mLabel;

  //PlayerLabels
  LinkedList<JLabel> playerIcons = new LinkedList<JLabel>();
  LinkedList<JLabel> boardTiles = new LinkedList<JLabel>();

  //JButtons
  JButton bAct;
  JButton bRehearse;
  JButton bMove;
  JButton bTurn;
  JButton bQuit;
  JButton bTake;
  JButton bUpgrade;
  LinkedList<JButton> bButtons= new LinkedList<JButton>();
  LinkedList<JButton> bRoleButtons = new LinkedList<JButton>();
  LinkedList<JButton> bSceneButtons = new LinkedList<JButton>();


  // JLayered Pane
  JLayeredPane bPane;

  //Text area
  static JTextArea area;
  JTextArea rulebox;
  JScrollPane scroll;
  JButton button;

  //Board image
  ImageIcon icon;

  //Deadwood model
  static Deadwood game;
  Board board;

  // Constructor

  public GUI() {

      super("Deadwood");

       // Set the exit option for the JFrame
       setDefaultCloseOperation(EXIT_ON_CLOSE);

       // Create the JLayeredPane to hold the display, cards, dice and buttons
       bPane = getLayeredPane();

       // Create the deadwood board
       boardlabel = new JLabel();
       icon =  new ImageIcon("Deadwood Needed Image Files/board.jpg");
       boardlabel.setIcon(icon);
       boardlabel.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());

       // Add the board to the lowest layer
       bPane.add(boardlabel, 0);

       // Set the size of the GUI
       setSize(icon.getIconWidth()+500,icon.getIconHeight()+100);

       // Add a scene card to this room
       cardlabel = new JLabel();
       ImageIcon cIcon =  new ImageIcon("Deadwood Needed Image Files/cards/01.png");
       cardlabel.setIcon(cIcon);
       cardlabel.setBounds(20,65,cIcon.getIconWidth()+2,cIcon.getIconHeight());
       cardlabel.setOpaque(true);
       cardlabel.setVisible(true);

       // Add the card to the lower layer
       bPane.add(cardlabel, 1);

       // Create the Menu for action buttons
       mLabel = new JLabel("MENU");
       mLabel.setBounds(icon.getIconWidth()+90,0,200,30);
       bPane.add(mLabel,2);

       bTake = new JButton("TAKE JOB");
       bTake.setBackground(Color.white);
       bTake.setBounds(icon.getIconWidth()+10, 30,200, 20);
       bTake.addMouseListener(mouseListener);

       // Create Action buttons
       bAct = new JButton("ACT");
       bAct.setBackground(Color.white);
       bAct.setBounds(icon.getIconWidth()+10, 60,200, 20);
       bAct.addMouseListener(mouseListener);

       bRehearse = new JButton("REHEARSE");
       bRehearse.setBackground(Color.white);
       bRehearse.setBounds(icon.getIconWidth()+10,90,200, 20);
       bRehearse.addMouseListener(mouseListener);

       bMove = new JButton("MOVE");
       bMove.setBackground(Color.white);
       bMove.setBounds(icon.getIconWidth()+10,120,200, 20);
       bMove.addMouseListener(mouseListener);

       bUpgrade = new JButton("UPGRADE");
       bUpgrade.setBackground(Color.white);
       bUpgrade.setBounds(icon.getIconWidth()+10, 150,200, 20);
       bUpgrade.addMouseListener(mouseListener);

       bTurn = new JButton("END TURN");
       bTurn.setBackground(Color.white);
       bTurn.setBounds(icon.getIconWidth()+10,180,200, 20);
       bTurn.addMouseListener(mouseListener);

       bQuit = new JButton("END GAME");
       bQuit.setBackground(Color.white);
       bQuit.setBounds(icon.getIconWidth()+10,240,200, 40);
       bQuit.addMouseListener(mouseListener);

       // Place the action buttons in the top layer
       bPane.add(bAct, 2);
       bPane.add(bRehearse, 2);
       bPane.add(bMove,  2);
       bPane.add(bTurn,2);
       bPane.add(bQuit, 2);
       bPane.add(bTake,2);
       bPane.add(bUpgrade,2);

       // The main text box
       this.area = new JTextArea();
       area.setEditable(false);
       area.setLineWrap(true);
       area.setBackground(Color.white);
       area.setVisible(true);

       scroll = new JScrollPane(area);
       scroll.setBounds(icon.getIconWidth()+10,icon.getIconHeight()/2,275,icon.getIconHeight()-450);
       scroll.setBackground(Color.white);
       scroll.setVisible(true);
       bPane.add(scroll,3);
       area.append("Welcome to Deadwood!\n");

       // The box for the workings of the buttons
       this.rulebox = new JTextArea();
       rulebox.setEditable(false);
       rulebox.setLineWrap(true);
       rulebox.setBackground(Color.white);
       rulebox.setVisible(true);
       rulebox.setBounds(icon.getIconWidth()+10,300,200,100);
       bPane.add(rulebox,2);
       rulebox.append("After clicking move, click the\nlocations name to move there");
       rulebox.append("\n\nIf you didn't mean to \nclick take or move, \nsimply click it again to undo");
  }
  // add text to the main text box
  public void addText(String text){
    area.append(text+"\n");
    area.setCaretPosition(area.getDocument().getLength());
  }

  // // This class implements Mouse Events

  class boardMouseListener implements MouseListener{

      Player currentPlayer;
      int id;
      int newJob =0;
      LinkedList<Set> neighbors = new LinkedList<Set>();
      boolean choseToMove=false;
      boolean choseToTake = false;
      Set currlocation;

      // Sets currentPlayer and gets their location and id
      public void setCurrPlayer(Player curr){
        this.currentPlayer = curr;
        this.id = currentPlayer.getId();
        this.currlocation = curr.getLocation();
      }

      // Code for the different button clicks
      public void mouseClicked(MouseEvent e) {

        // if a person wants to take a role and arent already in one
         if(e.getSource() ==bTake && !currentPlayer.checkInRole()){
           LinkedList<Role> totalRoles = board.getAvailableRoles(currentPlayer);
           if(currlocation.getName().equals("trailer") || currlocation.getName().equals("office")){ // if person tries to take a role at the office or trailer
             addText("\nNo roles are offered at: " + currlocation.getName());
           }else if(totalRoles.size() == 0){ // if there are no spots left
             addText("All roles are taken");
           }else if(this.currlocation.isComplete()){ // if the tile is already completed
             addText("This scene has wrapped");
           }else if(choseToTake==true){ // player changed their mind and doesnt want to take
             choseToTake=false;
           }else{ // player wants to take a role
             System.out.println("Take is selected");
             choseToTake = true;
           }
         }
         else if (e.getSource()== bAct && currentPlayer.checkInRole()&& newJob==0){ // if a player wants to act and didnt just take the job
            System.out.println("Acting is Selected\n");
            game.act(currentPlayer, currentPlayer.getRole()); //act
            game.changePlayer();//change player
            game.dailyRoutine();//give new player their turn
         }
         else if (e.getSource()== bRehearse && currentPlayer.checkInRole()&& newJob==0){
            System.out.println("Rehearse is Selected\n");
            int budget = currlocation.getScene().getBudget(); // get budget of location
            if(currentPlayer.getChips() == budget-1){ // if the person has guaranteed success
              addText("\nYou cannot rehearse anymore.\n Please act");
            }else{
              addText("\nYou chose to rehearse");
              currentPlayer.addChip(); // give player their chip
              addText("\n Total chips: " + currentPlayer.getChips());
              game.changePlayer();//change player
              game.dailyRoutine();// new turn
            }
         }
         else if (e.getSource()== bMove && !currentPlayer.checkInRole() && currentPlayer.canMove()){ // player wants to move
            System.out.println("Move is Selected\n");
            if(choseToMove ==true){ // player changed their mind on moving
              choseToMove = false;
            }else{
              choseToMove = true; // set to true to allow them to pick their options of moving
              addText("\nWhere would you like to move to?");
            }
         }
         else if(e.getSource()== bMove && !currentPlayer.canMove()){ // player clicked move but already moved during their turn
           addText("You already moved this turn");
         }
         else if(e.getSource()==bTurn){ // player clicked end turn
           System.out.println("Turn is Selected\n");
           //reset variables and change player and give them their turn
           if(newJob==1){
             newJob =0;
           }
           choseToMove = false;
           choseToTake = false;
           game.changePlayer();
           game.dailyRoutine();
         }
         else if(e.getSource() == bQuit){ // end game was selected
           System.out.println("Quit is Selected\n");
           game.quitGame(); // send them to the game end method
         }
         else if(e.getSource()== bUpgrade && currlocation.getName().equals("office")){ // player clicked upgrade and is at the office
           System.out.println("Upgrade selected");
           int rank = currentPlayer.getRank();
           if(rank!=6){ // if player isnt max rank
             int ret = game.upgrade(currentPlayer); // upgrade their rank
             if(ret == 1){ // upgrade successful
               updatePlayerIcon(currentPlayer); // change player icon to new rank
             }
           }else{ // player is max rank already
             addText("\nYou are already max rank");
           }
         }

         else if(choseToMove == true){ // the person wants to move
           if(!neighbors.isEmpty()){
             neighbors.clear();
           }// clear previous options
           // fetch neighboring tiles
           neighbors.add(currentPlayer.getLocation());
           neighbors.addAll(currentPlayer.getLocation().getNeighbors());
           // wait for a neighbor to be clicked on
           for(int i=0;i<neighbors.size();i++){
             System.out.println("In for loop");
             Set curr = neighbors.get(i);
             JButton currbutton = curr.getButton();
             if(e.getSource()==currbutton){ // when a location button was clicked, update player location
               System.out.println(curr.getName() +" was hit");
               addText("\nYou moved to: "+ curr.getName());
               game.move(currentPlayer,curr);
               this.currlocation = curr;
               choseToMove=false;
               int x = Integer.parseInt(curr.getCoords().get(0));
               int y = Integer.parseInt(curr.getCoords().get(1))+30;
               int h = Integer.parseInt(curr.getCoords().get(2));
               int w = Integer.parseInt(curr.getCoords().get(3));
               changeLocation(id,x,y,h,w);
               // if the card hasnt been flipped over/discovered flip card
               if(!this.currlocation.isDiscovered() && !currlocation.getName().equals("office")&& !currlocation.getName().equals("trailer")){
                 this.currlocation.discover();
                 flipCard(this.currlocation);
               }
             }
           }
         }
         // person chose to take a job in a tile
         else if(choseToTake == true && choseToMove == false){
           addText("What job would you like to take?");
           Scene currScene = currlocation.getScene();
           // grab all available roles
           LinkedList<Role> setRoles = currlocation.getAvailableRoles();
           LinkedList<Role> sceneRoles = currScene.getAvailableRoles();
           LinkedList<Role> allOptions = new LinkedList<Role>();
           allOptions.addAll(setRoles);
           allOptions.addAll(sceneRoles);
           int sceneX = Integer.parseInt(currlocation.getCoords().get(0));
           int sceneY = Integer.parseInt(currlocation.getCoords().get(1));
           for(int i=0;i<allOptions.size();i++){ // wait until a role is selected
             System.out.println("in take loop");
             Role currRole = allOptions.get(i);
             JButton currbutton = currRole.getButton();
             // if player is high enough rank to take the role
             if(e.getSource() == currbutton && currRole.isAvailable()&&currentPlayer.getRank()>=currRole.getRank()){
               System.out.println(currRole.getTitle() + " was hit");
               int x = Integer.parseInt(currRole.getCoords().get(0));
               int y = Integer.parseInt(currRole.getCoords().get(1));
               if(!currRole.isExtra()){ // if the role is not an extra, so on the card
                 x += sceneX;
                 y += sceneY;
               }
               int h = 40;
               int w = 40;
               changeLocation(this.id,x,y,h,w);// update players location
               currentPlayer.setRole(currRole); // set player role
               currRole.fillRole(); // fill role
               choseToTake = false; // reset variable
               game.changePlayer(); // change player
               game.dailyRoutine(); // new turn
             }
           }
         }
        }
      // unused methods to satisfy the interface
      public void mousePressed(MouseEvent e) {
      }
      public void mouseReleased(MouseEvent e) {
      }
      public void mouseEntered(MouseEvent e) {
      }
      public void mouseExited(MouseEvent e) {
      }
   }
   // get method for the deadwood class
   public GUI getlistener(){
     return this;
   }
   // make a specific player visible
   public void displayPlayer(int id){
     playerIcons.get(id).setVisible(true);
   }
   // updates player icon to new rank
   public void updatePlayerIcon(Player p){
     int pid = p.getId();
     int rank = p.getRank();
     ImageIcon nIcon = new ImageIcon("Deadwood Needed Image Files/dice/"+colors[pid]+rank+".png");
     JLabel curLabel = playerIcons.get(pid);
     curLabel.setIcon(nIcon);
   }
   // make player invisible
   public void hidePlayer(int id){
     playerIcons.get(id).setVisible(false);
   }
   // changes player icon location to specific coordinates
   public void changeLocation(int id, int x,int y,int h,int w){
     JLabel curr = playerIcons.get(id);
     curr.setBounds(x,y,h,w);
   }
// accessed by the main class
public void run(){
    System.out.println("main");
    String playerCount;
    // Take input from the user about number of players
    while(true){
      playerCount = JOptionPane.showInputDialog(instance, "How many players? (2-8)");
      if(playerCount != null&&playerCount.length()!=0&&isNumber(playerCount)){
        int in = Integer.parseInt(playerCount);
        if(in>=2&&in<=8){
          numPlayers=in;
          break;
        }
      }
    }
    // initialize the game
    Deadwood game = new Deadwood(this,numPlayers);
    // fetch game and board
    this.game = game;
    this.board = game.getBoard();
    // create the appropriate amount of player icons
    initPlayerIcons();
    // start main game
    game.run();
    System.exit(0);
  }
  // moves icons back to trailer
  public void resetPlayerIcons(){
    for(int i=0;i<playerIcons.size();i++){
      JLabel curr = playerIcons.get(i);
      curr.setBounds(1147,250,40,40);
    }
  }
  // creates the amount of player icons neccessary
  public void initPlayerIcons(){
    for(int i=0;i<numPlayers;i++){
      JLabel playerlabel = new JLabel();
      ImageIcon pIcon = new ImageIcon("Deadwood Needed Image Files/dice/"+colors[i]+"1.png");
      playerlabel.setIcon(pIcon);
      playerlabel.setBounds(icon.getIconWidth()-53,250,pIcon.getIconWidth(),pIcon.getIconHeight());
      playerIcons.add(playerlabel);
      playerlabel.setVisible(true);
      bPane.add(playerlabel,0);
    }
  }
  // initializes the board tile labels so cards can be distrubuted
  public void initBoardTiles(LinkedList<Set> sets){
      for(int i=0;i<10;i++){
        JLabel setLabel = new JLabel();
        Set currSet = sets.get(i);
        LinkedList<String> coords = currSet.getCoords();
        int x = Integer.parseInt(coords.get(0));
        int y = Integer.parseInt(coords.get(1))-45;
        int h = Integer.parseInt(coords.get(2))+90;
        int w = Integer.parseInt(coords.get(3));
        setLabel.setBounds(x,y,h,w);
        bPane.add(setLabel,0);
        boardTiles.add(setLabel);
      }
  }
  // initialize the board buttons. Used for moving the player
  public void initBoardButtons(LinkedList<Set> sets){
    for(int i=0;i<sets.size();i++){
      JButton setButton = new JButton("");
      setButton.addMouseListener(mouseListener);
      Set currSet = sets.get(i);
      LinkedList<String> coords = currSet.getCoords();
      int x;
      int y;
      int h;
      int w;
      // move the button depending on which tile it is
      if(i==10||i==11){
        x = Integer.parseInt(coords.get(0));
        y = Integer.parseInt(coords.get(1))-40;
        h = Integer.parseInt(coords.get(2))+90;
        w = Integer.parseInt(coords.get(3))-20;
      }else if(i==7){
        x = Integer.parseInt(coords.get(0));
        y = Integer.parseInt(coords.get(1))+120;
        h = Integer.parseInt(coords.get(2))+40;
        w = Integer.parseInt(coords.get(3))-150;
      }else if(i==0){
        x = Integer.parseInt(coords.get(0));
        y = Integer.parseInt(coords.get(1))+120;
        h = Integer.parseInt(coords.get(2))+90;
        w = Integer.parseInt(coords.get(3))-168;
      }else{
      x = Integer.parseInt(coords.get(0));
      y = Integer.parseInt(coords.get(1))+120;
      h = Integer.parseInt(coords.get(2))+90;
      w = Integer.parseInt(coords.get(3))-150;
      }
      // set bounds, hide the buttons
      setButton.setBounds(x,y,h,w);
      setButton.setOpaque(false);
      setButton.setContentAreaFilled(false);
      setButton.setBorderPainted(false);
      bPane.add(setButton,2);
      bButtons.add(setButton);
      currSet.setButton(setButton); // tie the button to the set
    }
  }
  // init the role buttons for the extra roles
  public void initSetButtons(LinkedList<Role> roles){
    for(int i=0;i<roles.size();i++){
      JButton roleButton = new JButton("");
      roleButton.addMouseListener(mouseListener);
      LinkedList<String> coords = roles.get(i).getCoords();
      int x = Integer.parseInt(coords.get(0));
      int y = Integer.parseInt(coords.get(1));
      int h = Integer.parseInt(coords.get(2));
      int w = Integer.parseInt(coords.get(3));
      roleButton.setBounds(x,y,h,w);
      roleButton.setOpaque(false);
      roleButton.setContentAreaFilled(false);
      roleButton.setBorderPainted(false);
      bPane.add(roleButton,2);
      bRoleButtons.add(roleButton);
      roles.get(i).setButton(roleButton);
    }
  }
  // init the button for the non extra roles that are on the cards
  public void initSceneButtons(LinkedList<Set> sets){
    int index = bSceneButtons.size()-1;
    if(index != -1){ // day end clear all previous butons and add new ones for the new cards
      while(index!=0){
        JButton curr = bSceneButtons.get(index);
        bPane.remove(curr);
        index--;
      }bSceneButtons.clear();
    }
    // distribute the buttons for all the cards
    for(int j =0;j<10;j++){
      Set currSet = sets.get(j);
      Scene currScene = currSet.getScene();
      LinkedList<Role> roles= currScene.getParts();
      LinkedList<String> setCoords = currSet.getCoords();
      int sceneX =  Integer.parseInt(setCoords.get(0));
      int sceneY =  Integer.parseInt(setCoords.get(1));

      for(int i=0;i<roles.size();i++){
        JButton roleButton = new JButton("");
        roleButton.addMouseListener(mouseListener);
        LinkedList<String> coords = roles.get(i).getCoords();
        int x = Integer.parseInt(coords.get(0))+sceneX;
        int y = Integer.parseInt(coords.get(1))+sceneY;
        int h = Integer.parseInt(coords.get(2));
        int w = Integer.parseInt(coords.get(3));
        roleButton.setBounds(x,y,h,w);
        roleButton.setOpaque(false);
        roleButton.setContentAreaFilled(false);
        roleButton.setBorderPainted(false);
        bPane.add(roleButton,2);
        bSceneButtons.add(roleButton);
        roles.get(i).setButton(roleButton);
      }
    }
  }
  // initialize the shot count markers
  public void initTakeMarkers(LinkedList<Set> sets){
    for(int i=0;i<10;i++){
      Set currSet = sets.get(i);
      LinkedList<String[]> takecoords = currSet.getTakeCoords();
      for(int j=0;j<takecoords.size();j++){
        String[] coords = takecoords.get(j);
        JLabel shotTile = new JLabel("");
        String image = "Deadwood Needed Image Files/shot.png";
        ImageIcon sIcon = new ImageIcon(image);
        int x =Integer.parseInt(coords[0]);
        int y =Integer.parseInt(coords[1]);
        int h =Integer.parseInt(coords[2]);
        int w =Integer.parseInt(coords[3]);
        shotTile.setBounds(x,y,h,w);
        shotTile.setIcon(sIcon);
        shotTile.setVisible(true);
        bPane.add(shotTile,3);
        currSet.addShotLabel(shotTile);
      }
    }
  }
  // makes all the markers visible again
  public void resetTakeMarkers(LinkedList<Set> sets){
    for(int i=0;i<10;i++){
      Set currSet = sets.get(i);
      LinkedList<JLabel> shotmarks = currSet.getShotLabels();
      for(int j=0;j<shotmarks.size();j++){
        shotmarks.get(j).setVisible(true);
      }
    }
  }
  // hides a certain marker after successful act
  public void hideTakeMarker(Set set,int take){
    LinkedList<JLabel> shotmarks = set.getShotLabels();
    shotmarks.get(take).setVisible(false);
  }
  // sets the tiles with the backs of the cards
  public void setBoardTile(Set set, int index){
    JLabel currlabel = boardTiles.get(index);
    Scene currScene = set.getScene();
    String image = "Deadwood Needed Image Files/CardBack-small.jpg";
    ImageIcon tIcon = new ImageIcon(image);
    LinkedList<String> coordinates = set.getCoords();
    currlabel.setIcon(tIcon);
    currlabel.setVisible(true);
  }
  // presents the scene card when scene is discovered
  public void flipCard(int index){
    JLabel currlabel = boardTiles.get(index);
    Set currSet = board.grabSet(index);
    Scene currScene = currSet.getScene();
    String image = "Deadwood Needed Image Files/cards/"+currScene.getImage();
    ImageIcon tIcon = new ImageIcon(image);
    currlabel.setIcon(tIcon);
    currlabel.setVisible(true);
  }
  // presents the scene card when scene is discovered (overloaded method that takes in the set not its index)
  public void flipCard(Set currSet){
    JLabel currlabel = boardTiles.get(currSet.getId());
    Scene currScene = currSet.getScene();
    String image = "Deadwood Needed Image Files/cards/"+currScene.getImage();
    ImageIcon tIcon = new ImageIcon(image);
    currlabel.setIcon(tIcon);
    currlabel.setVisible(true);
  }
  // hide the card when scene is complete
  public void hideCard(int index){
    JLabel currlabel = boardTiles.get(index);
    currlabel.setVisible(false);
  }

  public static boolean isNumber(String s) { // helper method for input handling
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
}
