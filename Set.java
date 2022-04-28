
/**
 * Essentially the tiles of the game
 * 
 * Responsibilities:
 * Store Set status (model)
 */

import java.util.LinkedList;

public class Set {
    // the tiles on the board
    // have the extras
    String name;
    int actorcapacity; // the extras
    LinkedList<Player> extras;
    LinkedList<Role> roles;
    int shotstocompletion;
    int completedshots;

    public int calculatePayout(Player player) {
        int payout = 0;
        return payout;
    }

}
