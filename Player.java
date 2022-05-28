/**
 * Responsibilities:
 * - Hold player status (model)
 */

public class Player extends Model{
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

    // Getter lambda functions
    public Getter<Integer> getRank = () -> rank;
    public Getter<Integer> getCredits = () -> credits;
    public Getter<Integer> getMoney = () -> money;
    public Getter<Integer> getScore = () -> score;
    public Getter<Integer> getId = () -> id;
    public Getter<String> getName = () -> name;
    public Getter<Boolean> getMoved = () -> hasMoved;
    public Getter<Integer> getChips = () -> rehearseChips;
    public Getter<Set> getLocation = () -> location;
    public Getter<Role> getRole = () -> currentRole;

    public Player(String name,int id) {
        this.credits = 0;
        this.money = 0;
        this.name = name;
        this.id = id;
    }

    public int calculateScore() {
        score = this.money + this.credits + (this.rank * 5);
        notify(getScore);
        return score;
    }

    public String getName() {
        return this.name;
    }

    public void addMoney(int m) {
        this.money += m;
        notify(getMoney);
    }

    public void subMoney(int m) {
        this.money -= m;
        notify(getMoney);
    }

    public int getMoney() {
        return this.money;
    }

    public void addCredits(int c) {
        this.credits += c;
        notify(getCredits);
    }

    public void subCredits(int c) {
        this.credits -= c;
        notify(getCredits);
    }

    public int getCredits() {
        return this.credits;
    }

    public int getChips() {
        return this.rehearseChips;
    }

    public void addChip() {
        this.rehearseChips++;
        notify(getChips);
    }

    public void resetChips() {
        this.rehearseChips = 0;
        notify(getChips);
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int r) {
        this.rank = r;
        notify(getRank);
    }

    public void increaseRank() {
        this.rank++;
        notify(getRank);
    }

    public void setLocation(Set loc) {
        this.location = loc;
        notify(getLocation);
    }

    public void moved() {
        this.hasMoved = true;
        notify(getMoved);
    }

    public void allowMove() {
        this.hasMoved = false;
        notify(getMoved);
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
        notify(getRole);
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
