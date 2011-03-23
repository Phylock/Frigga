/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.device;

import java.util.Date;

/**
 *
 * @author phylock
 */
public class DeviceData {
    protected final String name;
    protected final String symbolic;
    protected final Date last_updated;
    protected final boolean online;
    protected final String[] categories;

    public DeviceData(String name, String symbolic, Date last_updated, boolean online, String[] categories) {
        this.name = name;
        this.symbolic = symbolic;
        this.last_updated = last_updated;
        this.online = online;
        this.categories = categories;
    }

    public String[] getCategories() {
        return categories;
    }

    public Date getLast_updated() {
        return last_updated;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return online;
    }

    public String getSymbolic() {
        return symbolic;
    }
}
