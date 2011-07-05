package dk.itu.frigga.device;

import java.util.Date;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class DeviceLocalLocation extends DeviceLocation
{
    private String room;

    public DeviceLocalLocation(final String room, final double x, final double y, final double z, final double velX, final double velY, final double velZ, final Date lastUpdated)
    {
        super(x, y, z, velX, velY, velZ, lastUpdated);

        this.room = room;
    }

    public String getRoom()
    {
        return room;
    }
}
