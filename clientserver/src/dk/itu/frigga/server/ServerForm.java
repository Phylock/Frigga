package dk.itu.frigga.server;

import dk.itu.frigga.protocol.NetworkAnnouncer;
import dk.itu.frigga.protocol.Peer;
import dk.itu.frigga.protocol.ServerInformationRequiredListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-04-28
 */
public class ServerForm implements ServerInformationRequiredListener
{
    private JTextPane lblServerPort;
    private JCheckBox chbServerActive;
    private JTextPane lblServerLog;
    private JPanel rootPane;
    private JButton regenerateIDButton;
    private JLabel lblServerID;
    private ServerSocket socket;
    private final NetworkAnnouncer announcer = new NetworkAnnouncer();
    private UUID serverId;

    public ServerForm()
    {
        announcer.addClientDiscoveredListener(this);

        serverId = UUID.randomUUID();
        lblServerID.setText("Server ID: " + serverId.toString());

        chbServerActive.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                boolean selected = e.getStateChange() == ItemEvent.SELECTED;

                if (selected)
                {
                    try
                    {
                        socket = new ServerSocket(0);
                        socket.setReuseAddress(true);
                        int port = socket.getLocalPort();
                        announcer.start();
                        lblServerPort.setText("Server port: " + port);
                    }
                    catch (IOException e1)
                    {
                        lblServerLog.setText(e1.getMessage());
                    }
                }
                else
                {
                    if (socket != null)
                    {
                        try
                        {
                            socket.close();
                        }
                        catch (IOException e1)
                        {
                            lblServerLog.setText(e1.getMessage());
                        }
                        socket = null;
                    }

                    announcer.stop();
                    lblServerPort.setText("Server port: <not active>");
                }
            }
        });
        regenerateIDButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                serverId = UUID.randomUUID();
                lblServerID.setText("Server ID: " + serverId.toString());
            }
        });
    }

    public static void create(final String title)
    {
        JFrame frame = new JFrame(title);
        frame.setContentPane(new ServerForm().rootPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public Peer requestServerInformation(String requesterIp, Peer suggestedServer)
    {
        lblServerLog.setText(lblServerLog.getText() + "Client found: " + requesterIp + "\n");
        int port = 0;

        if (socket != null)
        {
            port = socket.getLocalPort();
        }

        try
        {
            return new Peer(InetAddress.getLocalHost().getHostAddress(), port, "Frigga Test Server æøå\n\n" +
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam hendrerit est at lacus luctus " +
                    "sollicitudin. Cras ut ligula metus, a ultricies mauris. Quisque at eros eros, eu varius lorem. " +
                    "Aliquam rutrum tincidunt sapien, sed porta neque semper sed. Cras nec turpis ornare purus " +
                    "lobortis pellentesque quis ut nisi. Morbi semper odio sed sem ultrices vitae ultricies augue " +
                    "tristique. In at enim velit. Sed ligula dui, pellentesque sed vehicula eu, malesuada eget orci. " +
                    "In volutpat massa quis velit mollis porttitor sit amet sit amet erat. Praesent aliquam est " +
                    "malesuada arcu pellentesque condimentum. Morbi justo nisl, facilisis ut interdum ac, porta in " +
                    "diam. Ut sodales, quam rutrum congue venenatis, ipsum ipsum tempor massa, sit amet lobortis nisl " +
                    "est quis urna. Maecenas tempor turpis accumsan ligula eleifend porta. Cras euismod, nisi " +
                    "placerat varius volutpat, arcu sem auctor lorem, auctor ultrices eros nisi sit amet est.\n"
                    , serverId);
        }
        catch (UnknownHostException e)
        {
            return suggestedServer;
        }
    }
}
