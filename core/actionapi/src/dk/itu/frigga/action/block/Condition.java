/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.block;

/**
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 * @author Tommy Andersen (toan@itu.dk)
 */
public interface Condition
{

    public void addChild(Condition child);

    public boolean hasChildren();

    public void removeChild(Condition child);

    public boolean hasReplacement();

    public void addReplacement(String replacement);

    public long getUniqueId();

    public boolean hasId();

    public String getId();
}
