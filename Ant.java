public class Ant {
    private int x;
    private int y;
    private int player;
    private int[] initialPosition = { 619, 37, 686, 41, 615, 107, 684, 108 };

    public Ant(int x, int y, int player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    // Getters and setters for x and y coordinates
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void killed() {
        this.x = initialPosition[0 + player * 2];
        this.y = initialPosition[1 + player * 2];
    }
}
