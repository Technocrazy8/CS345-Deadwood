/**
 * Responsibilities:
 * - Hold player status (model)
 */

public class Player{
    private int rank = 1;
    private int credits;
    private int money;
    private int score;
    private int id;
    private String name;
    private boolean hasMoved = false;
    private int rehearseChips = 0;
    private Set location;
    private Role currentRole;

    public Player(String name,int id) {
        this.credits = 0;
        this.money = 0;
        this.name = name;
        this.id = id;
    }

    public int calculateScore() {
        score = this.money + this.credits + (this.rank * 5);
        return score;
    }

    public String getName() {
        return this.name;
    }

    public int getId(){
      return this.id;
    }

    public void addMoney(int m) {
        this.money += m;
    }

    public void subMoney(int m) {
        this.money -= m;
    }

    public int getMoney() {
        return this.money;
    }

    public void addCredits(int c) {
        this.credits += c;
    }

    public void subCredits(int c) {
        this.credits -= c;
    }

    public int getCredits() {
        return this.credits;
    }

    public int getChips() {
        return this.rehearseChips;
    }

    public void addChip() {
        this.rehearseChips++;
    }

    public void resetChips() {
        this.rehearseChips = 0;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int r) {
        this.rank = r;
    }

    public void increaseRank() {
        this.rank++;
    }

    public void setLocation(Set loc) {
        this.location = loc;
    }

    public void moved() {
        this.hasMoved = true;
    }

    public void allowMove() {
        this.hasMoved = false;
    }

    public boolean canMove() {
        return !this.hasMoved;
    }

    public Set getLocation() {
        return this.location;
    }

    public String getLocName() {
        return this.location.getName();
    }

    public void setRole(Role r) {
        this.currentRole = r;
    }

    public Role getRole() {
        return this.currentRole;
    }

    public boolean checkInRole() {
        if (this.currentRole == null) {
            return false;
        }
        return true;
    }
}
