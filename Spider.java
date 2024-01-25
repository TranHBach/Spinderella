import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Spider {
    private Image spiderImage;
    private int x;
    private int y;
    private int currentX;
    private int currentY;
    private boolean isDragged = false;
    public int index = 0;

    public Spider(int initialX, int initialY, String imagePath) {
        try {
            spiderImage = ImageIO.read(new File(imagePath));
            spiderImage = spiderImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        x = initialX;
        y = initialY;
        currentX = x;
        currentY = y;
    }

    public int getCurrentX(){
        return currentX;
    }

    public int getCurrentY(){
        return currentY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getSpiderImage() {
        return spiderImage;
    }

    public void moveTo(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public void failedmove(){
        x = currentX;
        y = currentY;
    }
    public void setCurrentPosition(int x, int y){
        currentX = x;
        currentY = y;
    }

    public void dragging(boolean isDragged){
        this.isDragged = isDragged;
    }

    public boolean isDragged(){
        return isDragged;
    }
}
