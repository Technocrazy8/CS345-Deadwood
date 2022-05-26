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

  // JLayered Pane
  JLayeredPane bPane;

  static Deadwood game;

  // Constructor

  public BoardLayersListener() {

       // Set the title of the JFrame
       super("Deadwood");
       // Set the exit option for the JFrame
       setDefaultCloseOperation(EXIT_ON_CLOSE);

       // Create the JLayeredPane to hold the display, cards, dice and buttons
       bPane = getLayeredPane();

       // Create the deadwood board
       boardlabel = new JLabel();
       ImageIcon icon =  new ImageIcon("board.jpg");
       boardlabel.setIcon(icon);
       boardlabel.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());

       // Add the board to the lowest layer
       bPane.add(boardlabel, 0);

       // Set the size of the GUI
       setSize(icon.getIconWidth()+200,icon.getIconHeight());

       // Add a scene card to this room
       cardlabel = new JLabel();
       ImageIcon cIcon =  new ImageIcon("01.png");
       cardlabel.setIcon(cIcon);
       cardlabel.setBounds(20,65,cIcon.getIconWidth()+2,cIcon.getIconHeight());
       cardlabel.setOpaque(true);

       // Add the card to the lower layer
       bPane.add(cardlabel, 1);




       // Add a dice to represent a player.
       // Role for Crusty the prospector. The x and y co-ordiantes are taken from Board.xml file
       playerlabel = new JLabel();
       ImageIcon pIcon = new ImageIcon("r2.png");
       playerlabel.setIcon(pIcon);
       //playerlabel.setBounds(114,227,pIcon.getIconWidth(),pIcon.getIconHeight());
       playerlabel.setBounds(114,227,46,46);
       playerlabel.setVisible(false);
       bPane.add(playerlabel,3);

       // Create the Menu for action buttons
       mLabel = new JLabel("MENU");
       mLabel.setBounds(icon.getIconWidth()+40,0,100,20);
       bPane.add(mLabel,2);

       // Create Action buttons
       bAct = new JButton("ACT");
       bAct.setBackground(Color.white);
       bAct.setBounds(icon.getIconWidth()+10, 30,120, 20);
       bAct.addMouseListener(new boardMouseListener());

       bRehearse = new JButton("REHEARSE");
       bRehearse.setBackground(Color.white);
       bRehearse.setBounds(icon.getIconWidth()+10,60,120, 20);
       bRehearse.addMouseListener(new boardMouseListener());

       bMove = new JButton("MOVE");
       bMove.setBackground(Color.white);
       bMove.setBounds(icon.getIconWidth()+10,90,120, 20);
       bMove.addMouseListener(new boardMouseListener());

       bTurn = new JButton("END TURN");
       bTurn.setBackground(Color.white);
       bTurn.setBounds(icon.getIconWidth()+10,120,120, 20);
       bTurn.addMouseListener(new boardMouseListener());

       bQuit = new JButton("END GAME");
       bQuit.setBackground(Color.white);
       bQuit.setBounds(icon.getIconWidth()+10,150,120, 20);
       bQuit.addMouseListener(new boardMouseListener());

       // Place the action buttons in the top layer
       bPane.add(bAct, 2);
       bPane.add(bRehearse, 2);
       bPane.add(bMove,  2);
       bPane.add(bTurn,2);
       bPane.add(bQuit, 2);
  }

  // This class implements Mouse Events

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


  public static void main(String[] args) {

    BoardLayersListener board = new BoardLayersListener();
    board.setVisible(true);
    game = new Deadwood();
    //game.run();
    String playerCount;
      // Take input from the user about number of players
    playerCount = JOptionPane.showInputDialog(board, "How many players?");
    game.run(Integer.parseInt(playerCount));
    System.exit(0);
  }
}
