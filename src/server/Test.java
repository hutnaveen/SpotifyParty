package server;

import upnp.UPnP;

public class Test {
    public static void main(String[] args) {
        for (int i = 5000; i < 40000; i++) {
            System.out.println(i + " : " + UPnP.closePortUDP(i));
            UPnP.waitInit();
            //System.out.println(UPnP.openPortTCP(9002));
        }

    }
}
