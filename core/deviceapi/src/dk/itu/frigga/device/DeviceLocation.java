package dk.itu.frigga.device;

import java.util.Date;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-05
 */
public class DeviceLocation
{
    private double x;
    private double y;
    private double z;
    private double velX;
    private double velY;
    private double velZ;
    private Date lastUpdated;

    public DeviceLocation(final double x, final double y, final double z, final double velX, final double velY, final double velZ, final Date lastUpdated)
    {
        update(x, y, z, velX, velY, velZ, lastUpdated);
    }

    public void update(final double x, final double y, final double z, final double velX, final double velY, final double velZ, final Date lastUpdated)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
        this.lastUpdated = lastUpdated;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }

    public double getVelocityX()
    {
        return velX;
    }

    public double getVelocityY()
    {
        return velY;
    }

    public double getVelocityZ()
    {
        return velZ;
    }

    public double getVelocity()
    {
        return Math.sqrt(Math.pow(velX, 2.0) + Math.pow(velY, 2.0) + Math.pow(velZ, 2.0));
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    @Override
    public String toString()
    {
        return "DeviceLocation: (" + x + ", " + y + ", " + z + ") updated: " + lastUpdated;
    }
}
