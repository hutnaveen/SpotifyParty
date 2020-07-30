package main;

import chatGUI.ChatPanel;
import chatGUI.SpotifyPartyPanelChat;
import interfaces.SpotifyPlayerAPI;
import model.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import spotifyAPI.OSXSpotifyAPI;
import spotifyAPI.SpotifyAppleScriptWrapper;
import spotifyAPI.SpotifyWebAPI;
import spotifyAPI.WinSpotifyAPI;
import utils.OSXUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Arrays;

import static utils.GUIUtils.resizeIcon;

/**
 * @author Naveen Govindaraju, Dhaunsh Ramkumar
 */
public class SpotifyParty {
    public static ChatPanel chatPanel = null;
    public static boolean darkMode = true;
    public static final String VERSION = "v0.2-alpha";
    public static SpotifyWebAPI api;
    public static String defFont = "";
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        //System.setProperty("apple.awt.UIElement", "true");
        if(Taskbar.isTaskbarSupported()) {
            try {
                Taskbar.getTaskbar().setIconImage(ImageIO.read(SpotifyParty.class.getResource("/images/logo.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Taskbar.getTaskbar().setWindowIconBadge(this, image);
            //trayIcon.displayMessage("hi", "hi", TrayIcon.MessageType.NONE);
        }
        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font f = Font.createFont(Font.TRUETYPE_FONT, new File(SpotifyParty.class.getResource("/fonts/NotoColorEmoji.ttf").getFile()));
             ge.registerFont(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(new Font(defFont, Font.PLAIN, 1).getFontName());
        if(System.getProperty("os.name").contains("Windows")) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            api = new WinSpotifyAPI();
        }
        else {
            api = new OSXSpotifyAPI();
        }
        /*System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int arg0) throws IOException {

            }
        }));*/
        checkForUpdate();
        chatPanel = new ChatPanel();
        if(System.getProperty("os.name").contains("Mac"))
            UIManager.setLookAndFeel("org.violetlib.aqua.AquaLookAndFeel");
        try {
            /*darkMode = OSXUtils.runAppleScript("tell application \"System Events\"\n" +
                    "\treturn dark mode of appearance preferences\n" +
                    "end tell").startsWith("tru");*/
        } catch (Exception e) {
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
