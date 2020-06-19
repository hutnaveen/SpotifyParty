//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package upnp;


public class UPnP {
    private static Gateway defaultGW = null;
    private static final GatewayFinder finder = new GatewayFinder() {
        public void gatewayFound(Gateway g) {
            synchronized(UPnP.finder) {
                if (UPnP.defaultGW == null) {
                    UPnP.defaultGW = g;
                }

            }
        }
    };

    public UPnP() {
    }

    public static void waitInit() {
        while(finder.isSearching()) {
            try {
                Thread.sleep(1L);
            } catch (InterruptedException var1) {
            }
        }

    }

    public static boolean isUPnPAvailable() {
        waitInit();
        return defaultGW != null;
    }

    public static boolean openPortTCP(int port) {
        return !isUPnPAvailable() ? false : defaultGW.openPort(port, false);
    }

    public static boolean openPortUDP(int port) {
        return !isUPnPAvailable() ? false : defaultGW.openPort(port, true);
    }

    public static boolean closePortTCP(int port) {
        return !isUPnPAvailable() ? false : defaultGW.closePort(port, false);
    }

    public static boolean closePortUDP(int port) {
        return !isUPnPAvailable() ? false : defaultGW.closePort(port, true);
    }

    public static boolean isMappedTCP(int port) {
        return !isUPnPAvailable() ? false : defaultGW.isMapped(port, false);
    }

    public static boolean isMappedUDP(int port) {
        return !isUPnPAvailable() ? false : defaultGW.isMapped(port, false);
    }

    public static String getExternalIP() {
        return !isUPnPAvailable() ? null : defaultGW.getExternalIP();
    }

    public static String getLocalIP() {
        return !isUPnPAvailable() ? null : defaultGW.getLocalIP();
    }
}
