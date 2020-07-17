package utils;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;

public class NetworkUtils {
    public static String getPublicIP()
    {
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String ip = null; //you get the IP as a String
        try {
            ip = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ip;
    }
    public static String getLocalIP()
    {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("google.com", 80));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ""+(socket.getPort());
    }
    public static String simpleEncode(String ip, int port, int size)
    {
        int cycNum = (int)(Math.random() * 999);
        char[] enc = (ip + ":" + port + ":" + size + ":").toCharArray();
        for(int i = 0; i < enc.length; i++)
        {
            if(Character.isDigit(enc[i]))
            {
                enc[i] = String.valueOf(cycle(enc[i] - 48, cycNum, false)).charAt(0);
            }

        }
        return String.valueOf(enc).replace(':', 'X').replace('.','M') + cycNum;
    }
    private static int cycle(int num, int cyc, boolean rev)
    {
        cyc = cyc % 10;
        if(!rev)
        {
            for(int i = 0; i < cyc; i++)
            {
                if(num == 9)
                    num = 0;
                else
                    num++;
            }
        }
        else
        {
            for(int i = 0; i < cyc; i++)
            {
                if(num == 0)
                    num = 9;
                else
                    num--;
            }
        }
        return num;
    }
    public static Object[] simpleDecode(String code)
    {
        String ip = "";
        Integer port = null;
        Integer cycNum = null;
        int clientIp = 0;
        code = code.replace('X', ':').replace('M', '.');
        try {
            cycNum = Integer.parseInt(code.substring(code.lastIndexOf(':') + 1).trim());
            code = code.substring(0, code.lastIndexOf(':'));
            clientIp = Integer.parseInt(code.substring(code.lastIndexOf(':') + 1).trim()) + 5002;
            code = code.substring(0, code.lastIndexOf(':') + 1);
            char[] enc = code.toCharArray();
            for(int i = 0; i < enc.length; i++)
            {
                if(Character.isDigit(enc[i]))
                {
                    enc[i] = String.valueOf(cycle(enc[i] - 48, cycNum, true)).charAt(0);
                }
            }
            code = String.valueOf(enc);
            ip = code.substring(0, code.indexOf(':'));
            port = Integer.parseInt(code.substring(code.indexOf(':') + 1, code.lastIndexOf(':')));
            return new Object[]{ip, port, clientIp};
        }catch (StringIndexOutOfBoundsException | NumberFormatException e)
        {
            return null;
        }
    }

}
