/**
 * Responsibilities:
 * - Hold player status (model)
 */

public class Player {
    int rank=1;
    int credits;
    int money;
    String name;
    int id;
    int rehearseChips;
    int status;
    Set location;
    Role currentRole; 
    //int rank =1;

    public Player(String name, int id) {
        this.credits = 0;
        this.money = 0;
        this.id = id;
        this.name = name;
        this.rehearseChips = 0;
    }



    public int calculateScore() {
        int score = 0;
        score = this.money + this.credits + (this.rank * 5);
        return score;
    }

    public String getName() {
        return this.name;
    }

    public void addMoney(int m) {
        this.money += m;
    }

    public int getMoney() {
        return this.money;
    }

    public void addCredits(int c) {
        this.credits += c;
    }

    public int getCredits() {
        return this.credits;
    }

    public void addChip(int c) {
        this.rehearseChips += c;
    }

    public int getRank(){
        return this.rank;
    }

    public void setLocation(Set loc){
        this.location=loc;
    }

    public Set getLocation(){
        return this.location;
    }

    public String getLocName(){
        return this.location.getName();
    }

    public void setRole(Role r){
        this.currentRole=r;
    }

    public Role getRole(){
        return this.currentRole;
    }
}
