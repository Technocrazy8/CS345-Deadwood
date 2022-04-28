/**
 * Responsibilities:
 * - Hold player status (model)
 */

public class Player {
    int rank;
    int credits;
    int money;
    String name;
    int id;
    int rehearseChips;
    int status;

    public Player(String name, int id) {
        this.rank = 0;
        this.credits = 0;
        this.money = 0;
        this.id = id;
        this.name = name;
        this.rehearseChips = 0;
    }

    public int calculateScore(Player player) {
        int score = 0;
        return score;
    }
}
