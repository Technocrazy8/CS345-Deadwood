/**
 * Responsibilities:
 * - Hold role's info (model)
 */
public class Role{
    String title;
    String description;
    int minRank;
    int rehearseBonus; // may be trivial
    int actor; // player id

    public void setTitle(String t){
        this.title =t;
    }
    public void setDesc(String desc){
        this.description = desc;
    }
    public void setRank(int r){
        this.minRank=r;
    }
    public void setBonus(int b){
        this.rehearseBonus = b;
    }
    public void setActorID(int i){
        this.actor = i;
    }
}