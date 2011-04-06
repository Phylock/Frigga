package dk.itu.frigga.data.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

/**
 * Based on the example from:
 *  Java Persistence with hibernate
 *  isbn: 1-932394-88-5
 *
 * @author phylock
 */
public interface GenericDAO<T, ID extends Serializable> {
    void setConnection(Connection conn);

    T findById(ID id, boolean lock);

    List<T> findAll();

    List<T> findByExample(T exampleInstance);

    T makePersistent(T entity);

    void makeTransient(T entity);
}

