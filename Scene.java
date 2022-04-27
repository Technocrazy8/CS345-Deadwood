public class Scene {
    String title;
    String description;
    int budget;
    Roles[] parts;
    int shotcounter;
    public Scene(String title,String description,int budgets, Roles[] parts,int shotcounter){
        this.title=title;
        this.description=description;
        this.budget=budgets;
        this.parts=parts;
        this.shotcounter = shotcounter;
    }
}
