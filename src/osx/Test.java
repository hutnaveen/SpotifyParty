package osx;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        try {
            Desktop.getDesktop().open(new File("ClinetJoined.app"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
