import javax.swing.*;
import java.awt.*;

public class VictoriousLabel extends JLabel {
    public VictoriousLabel(String playerName) {
        super(playerName + " is victorious!");
        Font gameFont = new Font("Comic Sans MS", Font.BOLD, 48); // You can adjust the size as needed
        Color textColor = new Color(255, 255, 255);
        this.setFont(gameFont);
        this.setForeground(textColor);
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
        setBounds((995 - 600) / 2, 575, 600, 100);
        setBackground(new Color(0, 0, 0, 0)); // Set the background color to white
    }
}
