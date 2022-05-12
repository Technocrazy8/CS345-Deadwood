/**
 * Responsibilities:
 * - Hold role's info (model)
 */
public class Role {
    private String title;
    private String description;
    private boolean taken = false;
    private boolean isExtra = false;
    private int minRank;
    private int rehearseBonus = -1; // may be trivial
    private int actor = -1; // player id

    public void setTitle(String t) {
        this.title = t;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return this.description;
    }

    public void setRank(int r) {
        this.minRank = r;
    }

    public int getRank() {
        return this.minRank;
    }

    public void setBonus(int b) {
        this.rehearseBonus = b;
    }

    public void setActorID(int i) {
        this.actor = i;
    }

    public boolean isAvailable() {
        return this.taken;
    }

    public void fillRole() {
        this.taken = true;
    }

    public void vacateRole() {
        this.taken = false;
    }

    public void toggleExtra(){
        this.isExtra = true;
    }

    public boolean isExtra(){
        return this.isExtra;
    }
}