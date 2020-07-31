package model;

import javax.naming.ldap.SortKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class ObjectIn implements Serializable {
    public InputStream s;

    {
        try {
            s = new Socket().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
