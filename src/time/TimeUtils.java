package time;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TimeUtils {

    static final String TIME_SERVER = "time.apple.com";
    static NTPUDPClient timeClient = new NTPUDPClient();
    static InetAddress inetAddress;
    static TimeInfo timeInfo = null;
    static {
        try {
            inetAddress = InetAddress.getByName(TIME_SERVER);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static long getDefaultTime() {
        return getAppleTime();
    }
    public static long getAppleTime()
    {
        try {
            timeInfo = timeClient.getTime(inetAddress);
        } catch (IOException e) {
          return -1;
        }
        return timeInfo.getMessage().getTransmitTimeStamp().getTime();
    }
    public static long getSystemTime()
    {
        return System.currentTimeMillis();
    }
}
