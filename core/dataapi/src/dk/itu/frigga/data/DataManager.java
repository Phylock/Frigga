/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.data;

import java.sql.SQLException;

/**
 *
 * @author phylock
 */
public interface DataManager {

    /**
     * Creates a new data connection with a given group name. Use this method to
     * create connections to different databases. For instance if you want a
     * connection to a database in SQLite called log.db, which you will use for
     * logging, give it a group saying name (log could be an option) and add
     * the connection like this:
     * {@code
     * DataManager manager = DataManager.getInstance();
     * manager.addConnection("log",
     * new DataConnection("jdbc", "sqlite", "log.db", "org.sqlite.JDBC"));
     * boolean found = manager.hasConnection("log"); // this is now true.
     * }
     *
     * @param group      The name of the group this database connection belongs
     * to. This has to be unique, if a group with the given
     * name already exists, it will be overwritten with this
     * new one.
     * @param connection The connection to associate with the group. An instance
     * of the underlying JDBC connection will be returned when
     * a connection of this type is requested.
     */
    void addConnection(final String group, DataConnection connection);

    /**
     * Finds out whether a group with the given name has been registered in the
     * DatabaseManager.
     *
     * @param group The name of the group to retrieve.
     * @return Returns true if the connection is registered, false if not.
     */
    boolean hasConnection(final String group);

    /**
     * Requests a connection to a database for the given group, use this to
     * access the database, run queries and generally interact with the
     * database.
     *
     * @param group The name of the group, whose database connection to
     * retrieve.
     * @return Returns a Connection object, with an open connection to the
     * specified group datatbase.
     * @throws SQLException
     * @throws DataGroupNotFoundException
     * @throws UnknownDataDriverException
     */
    ConnectionPool requestConnection(final String group) throws SQLException, DataGroupNotFoundException, UnknownDataDriverException;

}
