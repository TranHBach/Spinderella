import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Spinderella {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        // 740
        int frameWidth = 995;
        int frameHeight = 783;
        frame.setBounds(0, 0, frameWidth, frameHeight);

        MapPanel mapPanel = new MapPanel(new String[] {"Menu/PixelJungleBackground.png", "Map/diceBackground.png"},
                new String[] { "Ant/antblue.png", "Ant/antgreen.png", "Ant/antyellow.png", "Ant/antred.png" },
                new String[] { "Dices/AntDices/", "Dices/SpiderDices/", "Dices/GeneralDices/" },
                new String[] { "Spider/Spider.png", "Spider/Spider2.png" });

        frame.add(mapPanel);
        frame.setLayout(null);
        mapPanel.setBounds(0, 0, 995, 750);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frameWidth) / 2;
        int y = (screenSize.height - 820) / 2;

        frame.setLocation(x, y);
        mapPanel.setMaximumSize(new Dimension(750, 750));

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Not needed for arrow keys
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (mapPanel.mapVisibility == 2 || mapPanel.mapVisibility == 3) {
                    if (keyCode == KeyEvent.VK_RIGHT) {
                        // Call method to update map with new background and ant visibility
                        mapPanel.swapMap("Map/map2.jpg", 3);
                    } else if (keyCode == KeyEvent.VK_LEFT) {
                        // Call method to update map with original background and ant visibility
                        mapPanel.swapMap("Map/map.jpg", 2);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Not needed for arrow keys
            }
        });

        // Set focusable to true to receive key events
        frame.setFocusable(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}