package handler;

import chatGUI.ChatPanel;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import gui.RequestTab;
import gui.Requests;
import model.UpdateData;
import utils.TimeUtils;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class MessageHandler implements HttpHandler {
=======
public class MessageHandler {

    public MessageHandler(String indicator, String message)
    {
        handle(indicator, message);
    }
    private boolean handle(String indicator, String message)
    {
        String playerData[] = message.split(" ");
        switch (indicator) {
            case "usr":
                //ChatPanel.addNames(playerData[1]);
                break;
            case "delete":
                Requests.redraw(playerData[0]);
                ChatPanel.chat.revalidate();
                break;
            case "request":
                Requests.addRequest(new RequestTab(playerData[0], playerData[1]));
                ChatPanel.chat.revalidate();
                //Chat.redraw("");
                break;
            case "addAll":
                ArrayList<RequestTab> tabs = new ArrayList<>();
                for (String rec : playerData[0].split(",")) {
                    String[] dat = rec.split(";");
                    tabs.add(new RequestTab(dat[0], dat[1]));
                }
                Requests.requestTabs = tabs;
                Requests.redraw("");
                break;
            case "chat":
                String name = message.substring(0, message.indexOf(' ') + 1);
                String temp = message.substring(message.indexOf(' ') + 1);
                ChatPanel.chat.addText(temp, name);
                break;
        }
        return true;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // System.out.println(code);
        String response = "You can open the SpotifyParty app now";
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        exchange.close();
    }
=======
>>>>>>> 3e9fbca0eb815f5375c63fe605c5be4cde41fd74
}
