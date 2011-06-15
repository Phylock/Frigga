/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.core.clientapi;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author phylock
 */
public class Session {
    private final UUID sessionId;
    private Date initialized;
    private Date last_action;

    public Session()
    {
        sessionId = UUID.randomUUID();
    }

    public Session(final UUID session)
    {
        sessionId = session;
    }

    public UUID getSessionId()
    {
        return sessionId;
    }
}
