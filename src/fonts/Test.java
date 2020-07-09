package fonts;

import java.awt.*;

public class Test {
    public static void main(String[] args) {
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
       for(Font f: ge.getAllFonts())
       {
           System.out.println(f.getName());
       }
    }
}
