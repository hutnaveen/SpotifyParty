package test;

import coroutines.KThreadRepKt;
import spotifyAPI.OSXSpotifyAPI;
import spotifyAPI.SpotifyWebAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class Test {
    static int i = 0;
    public static void main(String[] args) {
        SpotifyWebAPI api = new OSXSpotifyAPI();
        try {
            System.setOut(new PrintStream(new File("/Users/naveen/Documents/Idea/SpotifyParty/src/dumpData/data.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        long start = System.currentTimeMillis();
        System.out.println(start);
       for(int a = 0; a <= 999; a++)
       {
           KThreadRepKt.startInfCor(() -> {
               try {
                   api.getPlayerData();
               } catch (IOException e) {
                   e.printStackTrace();
               }
               i++;
               System.out.println(i + " calls in " + (System.currentTimeMillis() - start)/1000 + " secconds");
           });
           System.out.println(a + " Coroutines");
       }

    }
}

