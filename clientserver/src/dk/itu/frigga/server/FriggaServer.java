package dk.itu.frigga.server;

import com.sun.org.apache.bcel.internal.generic.NEW;

import javax.swing.*;
import java.awt.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-28
 */
public class FriggaServer
{
    public FriggaServer(final String[] args)
    {
        ServerForm.create("Frigga Debug Server");
    }

    public static void main(final String[] args)
    {
        new FriggaServer(args);
    }
}
