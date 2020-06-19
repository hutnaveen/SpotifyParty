package osx;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class OSXUtils {

    public static String runAppleScript(String script) throws IOException {
        String as[] = new String[]{"osascript", "-e", script};
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(as);
        return processToString(process);
    }
    public static void runAppleCmd(String script) throws IOException {
        String as[] = new String[]{"osascript", "-e", script};
        Runtime.getRuntime().exec(as);
    }
    private static String processToString(Process process)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        return builder.toString();
    }

    public static void setWallpaper(File file) throws IOException {
        runAppleCmd("tell application \"System Events\" to set picture of (a reference to current desktop) to \""+file.getAbsolutePath()+"\"");
    }
    public static void sendNotification(String title, String sub)
    {
        try {
            runAppleCmd("display notification \"\" with title \"" + title + "\" subtitle \"" + sub + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
