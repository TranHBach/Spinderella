import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Spinderella {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        int frameWidth = 740;
        int frameHeight = 783;

        frame.setBounds(0, 0, frameWidth, frameHeight);

        MapPanel mapPanel = new MapPanel("Map/map.jpg",
                new String[] { "Ant/Ant-8-blue.png", "Ant/Ant-8-green.png", "Ant/Ant-8-pink.png", "Ant/Ant-8-red.png" },
                "Dices/");

        frame.add(mapPanel);
        frame.setLayout(null);
        mapPanel.setBounds(0, 0, 750, 750);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - 820) / 2;
        int y = (screenSize.height - 820) / 2;

        frame.setLocation(x, y);
        mapPanel.setMaximumSize(new Dimension(750, 750));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}