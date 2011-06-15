/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.core.clientapi;

import java.util.Collections;
import java.util.Set;

/**
 *
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 */
public class Access {
  private Set<Role> roles;
  private Set<Role> roles_readonly;

  public Access(Set<Role> roles) {
    this.roles = roles;
    roles_readonly = Collections.unmodifiableSet(roles);
  }

  public Set<Role> getRoles()
  {
    return roles_readonly;
  }
}
