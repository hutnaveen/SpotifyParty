package server;

import chatGUI.ChatPanel;
import gui.RequestTab;
import gui.Requests;

import java.io.DataInputStream;
import java.util.Arrays;

public class ClientListener implements Runnable
{
    DataInputStream in;
    Thread t = new Thread(this);
    public ClientListener(DataInputStream id)
    {
        in = id;
        t.start();
    }

    @Override
    public void run() {
        while (true)
        {
            try {
                String org = in.readUTF().trim();
                String[] str = org.split(" ");
                System.out.println(Arrays.toString(str));
                switch (str[1])
                {
                    case "usr":
                        TCPServer.sendToClients("usr " + str[2].trim(),in);
                        break;
                    case "request":
                        Requests.addRequest(new RequestTab(str[2].trim(), str[3].trim()));
                        TCPServer.sendToClients("request " + str[2].trim() + " " + str[3].trim(), in);
                        break;
                    case "chat":
                        org = org.substring(org.indexOf(' ')+1);
                        org = org.substring(org.indexOf(' ')+1);
                        String name = org.substring(0, org.indexOf(' '));
                        String message = org.substring(org.indexOf(' ')+1);
                        ChatPanel.chat.addText(message, name);
                        TCPServer.sendToClients("chat " + name+ " " + message, in);
                        break;
                }
            } catch (Exception e) {
                t.stop();
            }
        }
    }
}
