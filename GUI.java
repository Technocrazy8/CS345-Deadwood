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
  //public static final Deadwood game;// = new Deadwood(this);
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


  // JLayered Pane
  JLayeredPane bPane;

  //Text area
  static JTextArea area;
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

        //this.game = new Deadwood(this);
       // Set the title of the JFrame
       //super("Deadwood");
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

       //boardMouseListener mouseListener = new boardMouseListener();


       // Add a dice to represent a player.
       // Role for Crusty the prospector. The x and y co-ordiantes are taken from Board.xml file
       // playerlabel = new JLabel();
       // ImageIcon pIcon = new ImageIcon("Deadwood Needed Image Files/dice/b1.png");
       // playerlabel.setIcon(pIcon);
       // playerlabel.setBounds(icon.getIconWidth()-53,250,pIcon.getIconWidth(),pIcon.getIconHeight());
       // //playerlabel.setBounds(114,227,46,46);
       // //playerlabel.setVisible(true);
       // bPane.add(playerlabel,0);
       // playerlabel.setVisible(true);

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

       this.area = new JTextArea();
       area.setEditable(false);
       area.setLineWrap(true);
       area.setBackground(Color.white);
       area.setVisible(true);

       scroll = new JScrollPane(area);
       scroll.setBounds(icon.getIconWidth()+10,icon.getIconHeight()/2,200,icon.getIconHeight()-450);
       scroll.setBackground(Color.white);
       scroll.setVisible(true);
       bPane.add(scroll,3);
       area.append("Welcome to Deadwood!\n");
       //System.out.println("Text\n");
  }
  //WAS BREAKING MY SHIT
  // public static synchronized GUI getInstance() {
  //   if (instance == null&&game==null) {
  //     instance = new GUI();
  //     game=new Deadwood(instance,area);
  //   }
  //   return instance;
  // }

  public void addText(String text){
    area.append("\n"+text);
    area.setCaretPosition(area.getDocument().getLength());
  }

  public void displayCard(Set tile){// take the coords from the tile, take the image from the scene and mix/display the two

  }

  // // This class implements Mouse Events

  class boardMouseListener implements MouseListener{

      Player currentPlayer;
      int id;
      LinkedList<Set> neighbors = new LinkedList<Set>();
      boolean choseToMove=false;
      Set currlocation;

      public void setCurrPlayer(Player curr){
        this.currentPlayer = curr;
        this.id = currentPlayer.getId();
        this.currlocation = curr.getLocation();
      }

      // Code for the different button clicks
      public void mouseClicked(MouseEvent e) {

        //if(!choseToMove){
         if(e.getSource() ==bTake && !currentPlayer.checkInRole()){
           LinkedList<Role> totalRoles = board.getAvailableRoles(currentPlayer);
           if(currlocation.getName().equals("trailer") || currlocation.getName().equals("office")){
             addText("\nNo roles are offered at: " + currlocation.getName());
           }else if(totalRoles.size() == 0){
             addText("All roles are taken");
           }else{
           System.out.println("Take is selected");
           }
         }
         else if (e.getSource()== bAct && currentPlayer.checkInRole()){
            System.out.println("Acting is Selected\n");
            game.actingChoices(currentPlayer);

         }
         else if (e.getSource()== bRehearse && currentPlayer.checkInRole()){
            System.out.println("Rehearse is Selected\n");
            int budget = currlocation.getScene().getBudget();
            if(currentPlayer.getChips() == budget-1){
              addText("\nYou cannot rehearse anymore. Please act");
            }else{
              addText("\nYou chose to rehearse");
              currentPlayer.addChip();
              game.changePlayer();
              game.dailyRoutine();
            }
         }
         else if (e.getSource()== bMove && !currentPlayer.checkInRole() && currentPlayer.canMove()){
            System.out.println("Move is Selected\n");
            choseToMove = true;
            addText("\nWhere would you like \nto move to?");
         }
         else if(e.getSource()== bMove && !currentPlayer.canMove()){
           addText("You already moved this turn");
         }
         else if(e.getSource()==bTurn){
           System.out.println("Turn is Selected\n");
           game.changePlayer();
           game.dailyRoutine();
         }
         else if(e.getSource() == bQuit){
           System.out.println("Quit is Selected\n");
           game.quitGame();
           //return 6;
         }
         else if(e.getSource()== bUpgrade && currlocation.getName().equals("office")){
           System.out.println("Upgrade selected");
           int rank = currentPlayer.getRank();
           //int ret = game.upgrade(currentPlayer);
           if(rank!=6){
             int ret = game.upgrade(currentPlayer);
             if(ret == 1){
               updatePlayerIcon(currentPlayer);
             }
             //updatePlayerIcon(currentPlayer);
           }else{
             addText("\nYou are already max rank");
           }
         }
         else if(choseToMove == true){
           if(!neighbors.isEmpty()){
             neighbors.clear();
           }
           neighbors.add(currentPlayer.getLocation());
           neighbors.addAll(currentPlayer.getLocation().getNeighbors());

           for(int i=0;i<neighbors.size();i++){
             System.out.println("In for loop");
             Set curr = neighbors.get(i);
             JButton currbutton = curr.getButton();
             if(e.getSource()==currbutton){
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
             }
           }
         }
      //}else{
        // for(int i=0;i<bButtons.size();i++){
        //   if(e.getSource() == bButtons.get(i)){
        //     System.out.print("tile: "+i+" was hit ");
        //   }
        }


      public void mousePressed(MouseEvent e) {
      }
      public void mouseReleased(MouseEvent e) {
      }
      public void mouseEntered(MouseEvent e) {
      }
      public void mouseExited(MouseEvent e) {
      }

      public void movePlayer(){

      }
   }

   public GUI getlistener(){
     return this;
   }

   public void displayPlayer(int id){
     playerIcons.get(id).setVisible(true);
   }
   public void updatePlayerIcon(Player p){
     int pid = p.getId();
     int rank = p.getRank();
     ImageIcon nIcon = new ImageIcon("Deadwood Needed Image Files/dice/"+colors[pid]+rank+".png");
     JLabel curLabel = playerIcons.get(pid);
     curLabel.setIcon(nIcon);
   }

   public void hidePlayer(int id){
     playerIcons.get(id).setVisible(false);
   }

   public void changeLocation(int id, int x,int y,int h,int w){
     JLabel curr = playerIcons.get(id);
     curr.setBounds(x,y,h,w);
   }

   public void presentLocations(LinkedList<Set> neighbors){

   }


public void run(){
    System.out.println("main");
    //initBoardTiles(game.getBoard().getSets());
    String playerCount;
    // Take input from the user about number of players
    while(true){
      playerCount = JOptionPane.showInputDialog(instance, "How many players? (2-8)");
      if(playerCount.length()!=0&&isNumber(playerCount)){
        int in = Integer.parseInt(playerCount);
        if(in>=2&&in<=8){
          numPlayers=in;
          break;
        }
      }
  }
  Deadwood game = new Deadwood(this,numPlayers);
  this.game = game;
  this.board = game.getBoard();
  initPlayerIcons();
  //initBoardTiles(game.getBoard().getSets());
  game.run();
  //game.run(Integer.parseInt(playerCount));
  System.exit(0);
  }

  public void initPlayerIcons(){
    for(int i=0;i<numPlayers;i++){
      JLabel playerlabel = new JLabel();
      ImageIcon pIcon = new ImageIcon("Deadwood Needed Image Files/dice/"+colors[i]+"1.png");
      playerlabel.setIcon(pIcon);
      playerlabel.setBounds(icon.getIconWidth()-53,250,pIcon.getIconWidth(),pIcon.getIconHeight());
      playerIcons.add(playerlabel);
      playerlabel.setVisible(false);
      bPane.add(playerlabel,0);
    }
    for(int i = numPlayers-1;i>=0;i--){
      playerIcons.get(i).setVisible(true);
    }
  }

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
        x = Integer.parseInt(coords.get(0));
        y = Integer.parseInt(coords.get(1));
        h = Integer.parseInt(coords.get(2))+90;
        w = Integer.parseInt(coords.get(3))-50;
      //}
      setButton.setBounds(x,y,h,w);
      //setButton.setVisible(false);
      setButton.setOpaque(false);
      setButton.setContentAreaFilled(false);
      setButton.setBorderPainted(false);
      bPane.add(setButton,2);
      bButtons.add(setButton);
      currSet.setButton(setButton);
    }
  }

  public void setBoardTile(Set set, int index){
    JLabel currlabel = boardTiles.get(index);
    Scene currScene = set.getScene();
    String image = "Deadwood Needed Image Files/cards/"+currScene.getImage();
    ImageIcon tIcon = new ImageIcon(image);
    LinkedList<String> coordinates = set.getCoords();
    currlabel.setIcon(tIcon);
    currlabel.setVisible(true);
  }

  public void flipCard(int index){
    JLabel currlabel = boardTiles.get(index);
    String image = "Deadwood Needed Image Files/CardBack-small.jpg";
    ImageIcon tIcon = new ImageIcon(image);
    currlabel.setIcon(tIcon);
    currlabel.setVisible(true);
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

  // Update method , part of the observer design
  // Depending on the getter, a function should be called that redraws the frames using getter's data
  public void update(int subjectID, Model.Getter getter) {
    // TODO:
    System.out.println("from " + subjectID + ": " + getter.fetch());
  }
}
