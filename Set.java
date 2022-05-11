
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
    private String name;
    private int actorcapacity; // the extras
    private LinkedList<Player> extras;
    private LinkedList<Role> roles;
    private LinkedList<Set> neighbors;
    private int shotstocompletion = -1;
    private int completedshots =0;
    private Scene scene;
    private String[][] upgradeList;

    // public Set(String name){
    //     this.name=name;
    // }

    public int calculatePayout(Player player) {
        int payout = 0;
        return payout;
    }

    public void setName(String name){
        this.name=name;
    }
    
    public String getName(){
        return this.name;
    }

    public void setActCapacity(int n){
        this.actorcapacity = n;
    }

    public int getActCapacity(){
        return this.actorcapacity;
    }

    public void setShotCapacity(int n){
        if(n>this.shotstocompletion){
            this.shotstocompletion = n;
        }
    }

    public void addPlayer(Player p){
        if(this.extras == null){
            this.extras = new LinkedList<Player>();
        }
        if(this.actorcapacity == this.extras.size()){
            System.out.println("Actor capacity aready reached");
            return;
        }
        this.extras.add(p);
    }

    public void addRole(Role r){
        if(this.roles == null){
            this.roles = new LinkedList<Role>();
        }
        this.roles.add(r);
    }

    public void noRoles(){
        this.roles = new LinkedList<Role>();
    }

    public LinkedList<Role> getRoles(){
        return this.roles;
    }

    public int getRoleCount(){
        return this.roles.size();
    }

    public void setScene(Scene scene){
        this.scene=scene;
    }

    public Scene getScene(){
        return this.scene;
    }

    public void addNeighbor(Set set){
        if (this.neighbors==null){
            this.neighbors = new LinkedList<Set>();
        }
        this.neighbors.add(set);
    }

    public LinkedList<Set> getNeighbors(){
        return this.neighbors;
    }

    public void completeShot(){
        this.completedshots++;
    }
    public void resetShots(){
        this.completedshots =0;
    }

    public void setUpList(String[][] list){
        this.upgradeList = list;
    }
}
