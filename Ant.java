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

    public void move(int newX, int newY, Function repaint) {
        long startTime = System.currentTimeMillis();
        Thread moveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int originalX = x;
                int originalY = y;
                double moveXEachFrame = (newX - originalX) / 60.0;
                double moveYEachFrame = (newY - originalY) / 60.0;
                long endTime = System.currentTimeMillis();
                try {
                    while ((endTime - startTime) < 2000) {
                        // roll dice
                        if ((originalX < newX && moveXEachFrame > 0)||(originalX > newX && moveXEachFrame < 0)) {
                            originalX += (int) Math.round(moveXEachFrame);
                            setX(originalX);
                        }
                        if((originalY < newY && moveYEachFrame > 0)||(originalY > newY && moveYEachFrame < 0)) {
                            originalY += (int) Math.round(moveYEachFrame);
                            setY(originalY);
                        }
                        repaint.apply();
                        // sleep thread
                        Thread.sleep(1000 / 60);

                        endTime = System.currentTimeMillis();
                    }

                } catch (InterruptedException ex) {
                    System.out.println("Threading Error: " + ex);
                }
            }
        });
        moveThread.start();

    }
}
