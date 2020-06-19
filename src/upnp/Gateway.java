package upnp;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

import javax.xml.parsers.DocumentBuilderFactory;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

public class Gateway {
    private Inet4Address iface;
    private String serviceType = null;
    private String controlURL = null;

    public Gateway(byte[] data, Inet4Address ip) throws Exception {
        this.iface = ip;
        String location = null;
        StringTokenizer st = new StringTokenizer(new String(data), "\n");

        while(st.hasMoreTokens()) {
            String s = st.nextToken().trim();
            if (!s.isEmpty() && !s.startsWith("HTTP/1.") && !s.startsWith("NOTIFY *")) {
                String name = s.substring(0, s.indexOf(58));
                String val = s.length() >= name.length() ? s.substring(name.length() + 1).trim() : null;
                if (name.equalsIgnoreCase("location")) {
                    location = val;
                }
            }
        }

        if (location == null) {
            throw new Exception("Unsupported Gateway");
        } else {
            Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(location);
            NodeList services = d.getElementsByTagName("service");

            int slash;
            for(slash = 0; slash < services.getLength(); ++slash) {
                Node service = services.item(slash);
                NodeList n = service.getChildNodes();
                String serviceType = null;
                String controlURL = null;

                for(int j = 0; j < n.getLength(); ++j) {
                    Node x = n.item(j);
                    if (x.getNodeName().trim().equalsIgnoreCase("serviceType")) {
                        serviceType = x.getFirstChild().getNodeValue();
                    } else if (x.getNodeName().trim().equalsIgnoreCase("controlURL")) {
                        controlURL = x.getFirstChild().getNodeValue();
                    }
                }

                if (serviceType != null && controlURL != null && (serviceType.trim().toLowerCase().contains(":wanipconnection:") || serviceType.trim().toLowerCase().contains(":wanpppconnection:"))) {
                    this.serviceType = serviceType.trim();
                    this.controlURL = controlURL.trim();
                }
            }

            if (this.controlURL == null) {
                throw new Exception("Unsupported Gateway");
            } else {
                slash = location.indexOf("/", 7);
                if (slash == -1) {
                    throw new Exception("Unsupported Gateway");
                } else {
                    location = location.substring(0, slash);
                    if (!this.controlURL.startsWith("/")) {
                        this.controlURL = "/" + this.controlURL;
                    }

                    this.controlURL = location + this.controlURL;
                }
            }
        }
    }

    private Map<String, String> command(String action, Map<String, String> params) throws Exception {
        Map<String, String> ret = new HashMap();
        String soap = "<?xml version=\"1.0\"?>\r\n<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><SOAP-ENV:Body><m:" + action + " xmlns:m=\"" + this.serviceType + "\">";
        Entry entry;
        if (params != null) {
            for(Iterator var5 = params.entrySet().iterator(); var5.hasNext(); soap = soap + "<" + (String)entry.getKey() + ">" + (String)entry.getValue() + "</" + (String)entry.getKey() + ">") {
                entry = (Entry)var5.next();
            }
        }

        soap = soap + "</m:" + action + "></SOAP-ENV:Body></SOAP-ENV:Envelope>";
        byte[] req = soap.getBytes();
        HttpURLConnection conn = (HttpURLConnection)(new URL(this.controlURL)).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "text/xml");
        conn.setRequestProperty("SOAPAction", "\"" + this.serviceType + "#" + action + "\"");
        conn.setRequestProperty("Connection", "Close");
        conn.setRequestProperty("Content-Length", "" + req.length);
        conn.getOutputStream().write(req);
        Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(conn.getInputStream());
        NodeIterator iter = ((DocumentTraversal)d).createNodeIterator(d.getDocumentElement(), 1, (NodeFilter)null, true);

        Node n;
        while((n = iter.nextNode()) != null) {
            try {
                if (n.getFirstChild().getNodeType() == 3) {
                    ret.put(n.getNodeName(), n.getTextContent());
                }
            } catch (Throwable var11) {
            }
        }

        conn.disconnect();
        return ret;
    }

    public String getLocalIP() {
        return this.iface.getHostAddress();
    }

    public String getExternalIP() {
        try {
            Map<String, String> r = this.command("GetExternalIPAddress", (Map)null);
            return (String)r.get("NewExternalIPAddress");
        } catch (Throwable var2) {
            return null;
        }
    }

    public boolean openPort(int port, boolean udp) {
        if (port >= 0 && port <= 65535) {
            Map<String, String> params = new HashMap();
            params.put("NewRemoteHost", "");
            params.put("NewProtocol", udp ? "UDP" : "TCP");
            params.put("NewInternalClient", this.iface.getHostAddress());
            params.put("NewExternalPort", "" + port);
            params.put("NewInternalPort", "" + port);
            params.put("NewEnabled", "1");
            params.put("NewPortMappingDescription", "WaifUPnP");
            params.put("NewLeaseDuration", "0");

            try {
                Map<String, String> r = this.command("AddPortMapping", params);
                return r.get("errorCode") == null;
            } catch (Exception var5) {
                return false;
            }
        } else {
            throw new IllegalArgumentException("Invalid port");
        }
    }

    public boolean closePort(int port, boolean udp) {
        if (port >= 0 && port <= 65535) {
            Map<String, String> params = new HashMap();
            params.put("NewRemoteHost", "");
            params.put("NewProtocol", udp ? "UDP" : "TCP");
            params.put("NewExternalPort", "" + port);

            try {
                this.command("DeletePortMapping", params);
                return true;
            } catch (Exception var5) {
                return false;
            }
        } else {
            throw new IllegalArgumentException("Invalid port");
        }
    }

    public boolean isMapped(int port, boolean udp) {
        if (port >= 0 && port <= 65535) {
            Map<String, String> params = new HashMap();
            params.put("NewRemoteHost", "");
            params.put("NewProtocol", udp ? "UDP" : "TCP");
            params.put("NewExternalPort", "" + port);

            try {
                Map<String, String> r = this.command("GetSpecificPortMappingEntry", params);
                if (r.get("errorCode") != null) {
                    throw new Exception();
                } else {
                    return r.get("NewInternalPort") != null;
                }
            } catch (Exception var5) {
                return false;
            }
        } else {
            throw new IllegalArgumentException("Invalid port");
        }
    }
}
