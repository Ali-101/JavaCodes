package utils;

import java.net.URL;
import javax.swing.ImageIcon;

public class Utils {

    public static ImageIcon createImage(String path) {
        URL url = System.class.getClass().getResource(path);
        if (url == null) {
            System.err.println("cannot load image");
        }
        ImageIcon icon = new ImageIcon(url);
        return icon;
    }
}
