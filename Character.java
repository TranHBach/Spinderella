import java.util.*;

public class Character {
    private int playerTurn = 0;
    private int playerCount = 2;
    private Random rand = new Random();

    public int remainingAntMove = 0;
    public int remainingSpiderMove = 0;
    // objectMove = 1 means ant move, 2 means spider move, 3 means either can move.
    public int objectMove = 0;
    private RollButton rollButton;

    // Getters and setters for x and y coordinates
    public void addRollButton(RollButton rollButton) {
        this.rollButton = rollButton;
    }

    public int getTurn() {
        return playerTurn;
    }

    public void changeTurn() {
        this.playerTurn = (playerTurn + 1) % playerCount;
        rollButton.setEnabled(true);
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public void rollAntDie() {
        int numberOfMove = rand.nextInt(6) + 1;
        remainingAntMove = numberOfMove;
    }
    
    public void rollSpiderDie() {
        int numberOfMove = rand.nextInt(3) + 1;
        remainingSpiderMove = numberOfMove;
    }

    public void rollGeneralDie() {
        Random rand = new Random();
        // 1 is for ant, 2 is for spider, 3 is for either.
        int objectID = rand.nextInt(6) + 1;
        objectMove = objectID;
    }

    public void rollAllDie() {
        rollAntDie();
        rollSpiderDie();
        rollGeneralDie();
    }

    public void reduceAntMoveCount() {
        remainingAntMove--;
        remainingSpiderMove = 0;
    }
    
    public void reduceSpiderMoveCount() {
        remainingSpiderMove--;
        remainingAntMove = 0;
    }

    public void preventMoveBoth() {
        if (objectMove % 3 == 1) {
            remainingSpiderMove = 0;
        } else if (objectMove % 3 == 2) {
            remainingAntMove = 0;
        }
    }
}

