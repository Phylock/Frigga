package dk.itu.frigga.device.drivers.dog;

import it.polito.elite.domotics.dog2.dog2leash.Dog2JLeash;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xmlrpc.XmlRpcException;
import org.osgi.service.log.LogService;
import org.xml.sax.SAXException;

/**
 *
 * @author phylock
 */
public class Connection {

    private Dog2JLeash dogGateway;
    private String uri;
    private boolean connected;
    private long retry_delay;
    private String session;
    private LogService log = new NullLog();
    private DogParser parser;

    public Connection(DogDriver driver) {
        dogGateway = new Dog2JLeash();
        connected = false;
        retry_delay = 5 * 60000; // default 5 min
        parser = new DogParser(driver);
    }

    public boolean isConnected() {
        return connected;
    }

    public DogParser getParser() {
        return parser;
    }

    public long getRetryDelay() {
        return retry_delay;
    }

    public boolean connect(String uri) {
        //handle cases where connection alredy is established
        try {
            if (connected) {
                if (this.uri.equals(uri)) {
                    return true;
                } else {
                    dogGateway.disconnect();
                }
            }

            session = dogGateway.connect(uri);
            dogGateway.addDog2MessageListener(parser);
            connected = true;
            
        } catch (XmlRpcException ex) {
            log.log(LogService.LOG_ERROR, null, ex);
        } catch (IOException ex) {
            log.log(LogService.LOG_ERROR, null, ex);
        }

        if(connected)
        {
            //TODO: Process queue
        }

        return connected;
    }

    public void disconnect() {
        if (connected) {
            try {
                dogGateway.disconnect();
                connected = false;
            } catch (XmlRpcException ex) {
                log.log(LogService.LOG_ERROR, null, ex);
            } catch (IOException ex) {
                log.log(LogService.LOG_ERROR, null, ex);
            }
        }
    }

    public void send(DogMessage message) {
        send(message, 0);
    }

    public void send(DogMessage message, long timeout) {
        if (connected) {
            try {
                String command = message.generateXmlString(session);
                if (!Dog2JLeash.validate(command)) {
                    log.log(LogService.LOG_WARNING, "Aborting, message did not validate");
                    return;
                }
                if (!dogGateway.sendMessage(command)) {
                    log.log(LogService.LOG_WARNING, "Aborting, wrong session id ");
                } else {
                }
            } catch (XmlRpcException ex) {
                connected = false;
                //log.log(LogService.LOG_ERROR, null, ex);

            } catch (ParserConfigurationException ex) {
                //Should never happen here, would be caught earlier
                log.log(LogService.LOG_ERROR, null, ex);
            } catch (SAXException ex) {
                //Should never happen, we used the sax to build the xml
                log.log(LogService.LOG_ERROR, null, ex);
            } catch (IOException ex) {
                log.log(LogService.LOG_ERROR, "Aborting, unexpected closed stream", ex);
            }
        } else if (timeout > 0) {
            //TODO: Add to queue
        }
    }
}
