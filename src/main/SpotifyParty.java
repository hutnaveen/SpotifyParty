package main;

import chatGUI.ChatPanel;
import chatGUI.SpotifyPartyPanelChat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utils.OSXUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;

import static utils.GUIUtils.resizeIcon;

/**
 * @author Naveen Govindaraju, Dhaunsh Ramkumar
 */
public class SpotifyParty {
    public static ChatPanel chatPanel = null;
    public static boolean darkMode = true;
    public static final String VERSION = "v0.2-alpha";
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        System.setProperty("apple.awt.UIElement", "true");

        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int arg0) throws IOException {

            }
        }));
        checkForUpdate();
        chatPanel = new ChatPanel();
            UIManager.setLookAndFeel("org.violetlib.aqua.AquaLookAndFeel");
        try {
            darkMode = OSXUtils.runAppleScript("tell application \"System Events\"\n" +
                    "\treturn dark mode of appearance preferences\n" +
                    "end tell").startsWith("tru");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SpotifyPartyPanelChat panel = new SpotifyPartyPanelChat();
    }
    private static void checkForUpdate()
    {
        try {
            Document doc = Jsoup.connect("https://github.com/naveengovind/SpotifyParty/releases").get();
            String txt = (doc.getElementsByClass("release-entry").get(0).text());
           int a =  txt.indexOf(' ') + 1;
           String newVersion = txt.substring(a,txt.indexOf(" ", a));
            if(!newVersion.contains(VERSION))
            {
                Object[] options = { "UPDATE", "CANCEL" };
                JFrame frame = new JFrame();
                frame.setAlwaysOnTop(true);
                int selectedValue = JOptionPane.showOptionDialog(frame, "Would you like to download the new version of SpotifyParty?", "Update Available",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        resizeIcon(new ImageIcon(ChatPanel.class.getResource("/images/logo.png")), 50, 50), options, options[0]);
                if(selectedValue == 0)
                {
                    String urlStr = "https://github.com/naveengovind/SpotifyParty/releases/download/"+newVersion.trim()+"/SpotifyParty.dmg";
                    URL url = new URL(urlStr);
                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(url.toURI());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

}
