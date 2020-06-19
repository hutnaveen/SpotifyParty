//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package upnp;

import java.net.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

abstract class GatewayFinder {
    private static final String[] SEARCH_MESSAGES;
    private LinkedList<GatewayListener> listeners = new LinkedList();

    public GatewayFinder() {
        Inet4Address[] var1 = getLocalIPs();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Inet4Address ip = var1[var3];
            String[] var5 = SEARCH_MESSAGES;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String req = var5[var7];
                GatewayFinder.GatewayListener l = new GatewayFinder.GatewayListener(ip, req);
                l.start();
                this.listeners.add(l);
            }
        }

    }

    public boolean isSearching() {
        Iterator var1 = this.listeners.iterator();

        GatewayFinder.GatewayListener l;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            l = (GatewayFinder.GatewayListener)var1.next();
        } while(!l.isAlive());

        return true;
    }

    public abstract void gatewayFound(Gateway var1);

    private static Inet4Address[] getLocalIPs() {
        LinkedList ret = new LinkedList();

        try {
            Enumeration ifaces = NetworkInterface.getNetworkInterfaces();

            while(ifaces.hasMoreElements()) {
                try {
                    NetworkInterface iface = (NetworkInterface)ifaces.nextElement();
                    if (iface.isUp() && !iface.isLoopback() && !iface.isVirtual() && !iface.isPointToPoint()) {
                        Enumeration<InetAddress> addrs = iface.getInetAddresses();
                        if (addrs != null) {
                            while(addrs.hasMoreElements()) {
                                InetAddress addr = (InetAddress)addrs.nextElement();
                                if (addr instanceof Inet4Address) {
                                    ret.add((Inet4Address)addr);
                                }
                            }
                        }
                    }
                } catch (Throwable var5) {
                }
            }
        } catch (Throwable var6) {
        }

        return (Inet4Address[])ret.toArray(new Inet4Address[0]);
    }

    static {
        LinkedList<String> m = new LinkedList();
        String[] var1 = new String[]{"urn:schemas-upnp-org:device:InternetGatewayDevice:1", "urn:schemas-upnp-org:service:WANIPConnection:1", "urn:schemas-upnp-org:service:WANPPPConnection:1"};
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String type = var1[var3];
            m.add("M-SEARCH * HTTP/1.1\r\nHOST: 239.255.255.250:1900\r\nST: " + type + "\r\nMAN: \"ssdp:discover\"\r\nMX: 2\r\n\r\n");
        }

        SEARCH_MESSAGES = (String[])m.toArray(new String[0]);
    }

    private class GatewayListener extends Thread {
        private Inet4Address ip;
        private String req;

        public GatewayListener(Inet4Address ip, String req) {
            this.setName("WaifUPnP - Gateway Listener");
            this.ip = ip;
            this.req = req;
        }

        public void run() {
            try {
                byte[] req = this.req.getBytes();
                DatagramSocket s = new DatagramSocket(new InetSocketAddress(this.ip, 0));
                s.send(new DatagramPacket(req, req.length, new InetSocketAddress("239.255.255.250", 1900)));
                s.setSoTimeout(3000);

                while(true) {
                    try {
                        DatagramPacket recv = new DatagramPacket(new byte[1536], 1536);
                        s.receive(recv);
                        Gateway gw = new Gateway(recv.getData(), this.ip);
                        GatewayFinder.this.gatewayFound(gw);
                    } catch (SocketTimeoutException var5) {
                        break;
                    } catch (Throwable var6) {
                    }
                }
            } catch (Throwable var7) {
            }

        }
    }
}
