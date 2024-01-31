import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.awt.GraphicsEnvironment;

public class CustomFontLoader {

    public static Font loadFont(String fontPath, float fontSize) {
        try {
            // Load the font file
            File fontFile = new File(fontPath);
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);

            // Register the font with the graphics environment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            // Set the font size
            customFont = customFont.deriveFont(fontSize);
            return customFont;

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            // Handle the exception
            return null;
        }
    }
}
