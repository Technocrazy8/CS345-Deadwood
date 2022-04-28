/**
 * Essentially the tiles of the game
 * 
 * Responsibilities:
 * Store Set status (model)
 */

import java.util.LinkedList;

public class Set {
    //the tiles on the board
    //have the extras
    String name;
    int actorcapacity; // the extras
    LinkedList<Player> extras;
    LinkedList<Roles> roles;
    int shotstocompletion;
    int completedshots;
}
