/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.model;

import dk.itu.frigga.device.dao.CategoryDAO;
import dk.itu.frigga.device.dao.DeviceDAO;
import dk.itu.frigga.device.dao.VariableDao;

import java.io.Serializable;
import java.util.*;

/**
 * @author phylock
 */
//@Entity
//@Table(name = "device")
public class Device implements Serializable
{

    private static final long serialVersionUID = 1L;
    //@Id
    //@GeneratedValue
    private Long id;
    //@Column(name = "name", nullable = false)
    private String name;
    //@Column(name = "symbolic", nullable = false, unique = true)
    private String symbolic;
    //@Temporal(TemporalType.TIME)
    //@Column(name = "last_update", nullable = false)
    private Date last_update;
    //@Column(name = "online", nullable = false)
    private boolean online;

    private String driver;
    //@ManyToMany(cascade=CascadeType.ALL)
    //@JoinTable(name="device_category", joinColumns= { @JoinColumn(name = "device_id")}, inverseJoinColumns={@JoinColumn(name="category_id")} )
    private List<Category> categories = new ArrayList<Category>(0);

    private DeviceDAO deviceDao;
    private boolean hasCategories = false;
    private boolean hasVariables = false;

    private Location globalLocation;
    private final List<LocationLocal> localLocations = new LinkedList<LocationLocal>();
    private boolean hasGlobalLocation = false;
    private boolean hasLocalLocation = false;

    //@OneToMany(cascade=CascadeType.ALL)
    //@JoinColumn(name="device_id")
    private Set<Variable> variables = new HashSet<Variable>(0);

    public Device()
    {
        this.id = null;
        this.name = "";
        this.symbolic = "";
        this.last_update = null;
        this.online = false;
        this.driver = "";
    }

    public Device(Long id)
    {
        this.id = id;
        this.name = "";
        this.symbolic = "";
        this.last_update = null;
        this.online = false;
        this.driver = "";
    }

    public Device(String name, String symbolic, Date last_update, boolean online, String driver)
    {
        this.id = null;
        this.name = name;
        this.symbolic = symbolic;
        this.last_update = last_update;
        this.online = online;
        this.driver = driver;
    }

    public Device(Long id, String name, String symbolic, Date last_update, boolean online, String driver)
    {
        this.id = id;
        this.name = name;
        this.symbolic = symbolic;
        this.last_update = last_update;
        this.online = online;
        this.driver = driver;
    }

    public void setDeviceDao(DeviceDAO deviceDao)
    {
        this.deviceDao = deviceDao;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getLastUpdate()
    {
        return last_update;
    }

    public void setLastUpdate(Date last_update)
    {
        this.last_update = last_update;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isOnline()
    {
        return online;
    }

    public void setOnline(boolean online)
    {
        this.online = online;
    }

    public String getSymbolic()
    {
        return symbolic;
    }

    public void setSymbolic(String symbolic)
    {
        this.symbolic = symbolic;
    }

    public List<Category> getCategories()
    {
        if (!hasCategories)
        {
            synchronized (this)
            {
                if (!hasCategories)
                {
                    categories = deviceDao.getCategories(this);
                    hasCategories = true;
                }
            }
        }

        return categories;
    }

    public void setCategories(List<Category> categories)
    {
        synchronized (this)
        {
            this.categories = categories;
            hasCategories = true;
        }
    }

    public Set<Variable> getVariables()
    {
        if (!hasVariables)
        {
            synchronized (this)
            {
                if (!hasVariables)
                {
                    variables.addAll(deviceDao.getVariables(this));
                    hasVariables = true;
                }
            }
        }

        return variables;
    }

    public void setVariables(Set<Variable> variables)
    {
        synchronized (this)
        {
            this.variables = variables;
            hasVariables = true;
        }
    }

    public boolean isOfCategory(final String category)
    {
        return deviceDao.isOfCategory(this, new Category(category));
    }

    public String getDriver()
    {
        return driver;
    }

    public void setDriver(String driver)
    {
        this.driver = driver;
    }

    public Location getGlobalLocation()
    {
        if (!hasGlobalLocation)
        {
            synchronized (this)
            {
                if (!hasGlobalLocation)
                {
                    globalLocation = deviceDao.loadGlobalLocation(this);
                    hasGlobalLocation = true;
                }
            }
        }

        return globalLocation;
    }

    public List<LocationLocal> getLocalLocations()
    {
        if (!hasLocalLocation)
        {
            synchronized (this)
            {
                if (!hasLocalLocation)
                {
                    deviceDao.loadLocalLocations(this, localLocations);
                    hasLocalLocation = true;
                }
            }
        }

        return localLocations;
    }

    @Override
    public String toString()
    {
        return "Device{" + "id=" + id + ", name=" + name + ", symbolic=" + symbolic + ", last_update=" + last_update + ", online=" + online + '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        return symbolic.equals(device.symbolic);

    }

    @Override
    public int hashCode()
    {
        return symbolic.hashCode();
    }
}
