import java.util.*;

public class Character {
    private int playerTurn = 0;
    private int playerCount = 4;
    public int remainingAntMove = 0;
    public int remainingSpiderMove = 0;

    // Getters and setters for x and y coordinates
    public int getTurn() {
        return playerTurn;
    }

    public void changeTurn() {
        this.playerTurn = (playerTurn + 1) % 4;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public void rollAntDie() {
        Random rand = new Random();
        int numberOfMove = rand.nextInt(6);
        numberOfMove++;
        remainingAntMove = numberOfMove;
    }
    
    public void reduceAntMoveCount() {
        remainingAntMove--;
    }
    
    public void rollSpiderDie() {
        Random rand = new Random();
        int numberOfMove = rand.nextInt(6);
        numberOfMove++;
        remainingSpiderMove = numberOfMove;
    }

    public void reduceSpiderMoveCount() {
        remainingSpiderMove--;
    }
}

