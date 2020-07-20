package exception;

import interfaces.SpotifyPlayerAPI;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SpotifyException extends Exception{
    public SpotifyException(String msg)
    {
        super(msg);
        log(msg);
    }
    private void log(String msg)
    {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(new File("log.txt"), true));
                writer.append("Time: ").append(String.valueOf(System.currentTimeMillis())).append(" Msg: ").append(msg);
                writer.newLine();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    // TODO: 4/15/20 create fixes
    public void fix(int num, SpotifyPlayerAPI api)
    {
        switch (num)
        {

        }
    }
    // TODO: 4/15/20 create fixes
    public void fix(int num)
    {
        switch (num)
        {

        }
    }

}
