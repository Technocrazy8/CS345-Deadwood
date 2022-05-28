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


public class BoardLayersListener extends JFrame {

  private static BoardLayersListener instance;
  //public static final Deadwood game;// = new Deadwood(this);

  // JLabels
  JLabel boardlabel;
  JLabel cardlabel;
  JLabel playerlabel;
  JLabel mLabel;

  //JButtons
  JButton bAct;
  JButton bRehearse;
  JButton bMove;
  JButton bTurn;
  JButton bQuit;
  JButton bWork;
  JButton bUpgrade;


  // JLayered Pane
  JLayeredPane bPane;

  //Text area
  static JTextArea area;
  JScrollPane scroll;
  JButton button;

  //Deadwood model
  static Deadwood game;

  // Constructor

  private BoardLayersListener() {
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
       ImageIcon icon =  new ImageIcon("Deadwood Needed Image Files/board.jpg");
       boardlabel.setIcon(icon);
       boardlabel.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());

       // Add the board to the lowest layer
       bPane.add(boardlabel, 0);

       // Set the size of the GUI
       setSize(icon.getIconWidth()+500,icon.getIconHeight()+100);

       // Add a scene card to this room
       cardlabel = new JLabel();
       ImageIcon cIcon =  new ImageIcon("01.png");
       cardlabel.setIcon(cIcon);
       cardlabel.setBounds(20,65,cIcon.getIconWidth()+2,cIcon.getIconHeight());
       cardlabel.setOpaque(true);

       // Add the card to the lower layer
       bPane.add(cardlabel, 1);

       boardMouseListener mouseListener = new boardMouseListener();


       // Add a dice to represent a player.
       // Role for Crusty the prospector. The x and y co-ordiantes are taken from Board.xml file
       playerlabel = new JLabel();
       ImageIcon pIcon = new ImageIcon("Deadwood Needed Image Files/r2.png");
       playerlabel.setIcon(pIcon);
       //playerlabel.setBounds(114,227,pIcon.getIconWidth(),pIcon.getIconHeight());
       playerlabel.setBounds(114,227,46,46);
       playerlabel.setVisible(false);
       bPane.add(playerlabel,3);

       // Create the Menu for action buttons
       mLabel = new JLabel("MENU");
       mLabel.setBounds(icon.getIconWidth()+90,0,200,20);
       bPane.add(mLabel,2);

       // Create Action buttons
       bAct = new JButton("ACT");
       bAct.setBackground(Color.white);
       bAct.setBounds(icon.getIconWidth()+10, 30,200, 20);
       //bAct.addMouseListener(new boardMouseListener());
       bAct.addMouseListener(mouseListener);

       bRehearse = new JButton("REHEARSE");
       bRehearse.setBackground(Color.white);
       bRehearse.setBounds(icon.getIconWidth()+10,60,200, 20);
       //bRehearse.addMouseListener(new boardMouseListener());
       bRehearse.addMouseListener(mouseListener);

       bMove = new JButton("MOVE");
       bMove.setBackground(Color.white);
       bMove.setBounds(icon.getIconWidth()+10,90,200, 20);
       //bMove.addMouseListener(new boardMouseListener());
       bMove.addMouseListener(mouseListener);

       bTurn = new JButton("END TURN");
       bTurn.setBackground(Color.white);
       bTurn.setBounds(icon.getIconWidth()+10,120,200, 20);
       //bTurn.addMouseListener(new boardMouseListener());
       bTurn.addMouseListener(mouseListener);

       bQuit = new JButton("END GAME");
       bQuit.setBackground(Color.white);
       bQuit.setBounds(icon.getIconWidth()+10,150,200, 20);
       //bQuit.addMouseListener(new boardMouseListener());
       bQuit.addMouseListener(mouseListener);

       // Place the action buttons in the top layer
       bPane.add(bAct, 2);
       bPane.add(bRehearse, 2);
       bPane.add(bMove,  2);
       bPane.add(bTurn,2);
       bPane.add(bQuit, 2);

       this.area = new JTextArea();
       area.setEditable(false);
       area.setLineWrap(true);
       area.setBackground(Color.white);
       area.setVisible(true);
       scroll = new JScrollPane(area);
       scroll.setBounds(icon.getIconWidth()+10,icon.getIconHeight()/2,200,icon.getIconHeight()-450);
       //area.setBounds(icon.getIconWidth()+10,icon.getIconHeight()/2,400,icon.getIconHeight()-450);
       scroll.setBackground(Color.white);
       scroll.setVisible(true);
       //bPane.add(area,2);
       bPane.add(scroll,3);
       //bPane.add(area,3);
       area.append("Welcome to Deadwood!\n");
       System.out.println("Text\n");

       //game = new Deadwood();
  }
  public static synchronized BoardLayersListener getInstance() {
    if (instance == null&&game==null) {
      instance = new BoardLayersListener();
      game=new Deadwood(instance);
    }
    return instance;
  }

  // // This class implements Mouse Events

  class boardMouseListener implements MouseListener{

      // Code for the different button clicks
      public void mouseClicked(MouseEvent e) {

         if (e.getSource()== bAct){
            playerlabel.setVisible(true);
            System.out.println("Acting is Selected\n");
         }
         else if (e.getSource()== bRehearse){
            System.out.println("Rehearse is Selected\n");
         }
         else if (e.getSource()== bMove){
            System.out.println("Move is Selected\n");
         }
         else if(e.getSource()==bTurn){
           System.out.println("Turn is Selected\n");
         }
         else if(e.getSource() == bQuit){
           System.out.println("Quit is Selected\n");
           game.quitGame();
           //return 6;
         }
      }
      public void mousePressed(MouseEvent e) {
      }
      public void mouseReleased(MouseEvent e) {
      }
      public void mouseEntered(MouseEvent e) {
      }
      public void mouseExited(MouseEvent e) {
      }
   }
   public static void addText(String text){
     getInstance().area.append(text);
   }

   public BoardLayersListener getlistener(){
     return this;
   }


  public static void main(String[] args) {

    instance = getInstance();
    instance.setVisible(true);
    //board.run();
    //game = new Deadwood(board);
    //game.run();
    String playerCount;
      // Take input from the user about number of players
    while(true){
      playerCount = JOptionPane.showInputDialog(instance, "How many players? (2-8)");
      if(playerCount.length()!=0&&isNumeric(playerCount)){
        int in = Integer.parseInt(playerCount);
        if(in>=2&&in<=8){
          break;
        }
      }
    }

    game.run(Integer.parseInt(playerCount));
    System.exit(0);
  }
  // public void run(){
  //   //Deadwood game = new Deadwood();
  //   String playerCount;
  //     // Take input from the user about number of players
  //   while(true){
  //     playerCount = JOptionPane.showInputDialog(this, "How many players? (2-8)");
  //     if(playerCount.length()!=0&&game.isNumeric(playerCount)){
  //       int in = Integer.parseInt(playerCount);
  //       if(in>=2&&in<=8){
  //         break;
  //       }
  //     }
  //   }
  //
  //   game.run(Integer.parseInt(playerCount));
  //   System.exit(0);
  // }

  public static boolean isNumeric(String s) { // helper method for input handling
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
