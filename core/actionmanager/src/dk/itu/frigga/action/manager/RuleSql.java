/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.ConditionResult;
import dk.itu.frigga.action.Context;
import dk.itu.frigga.action.Replacement;
import dk.itu.frigga.action.Rule;
import dk.itu.frigga.action.RuleTemplate;
import dk.itu.frigga.action.Template;
import dk.itu.frigga.data.ConnectionPool;
import dk.itu.frigga.data.PreparedStatementProxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phylock
 */
public class RuleSql implements Rule {

  private ConnectionPool pool = null;
  private final String condition_template;
  private RuleTemplate template;
  private Context context;
  private ConditionChecker checker;

  public RuleSql(Context context, RuleTemplate template) {
    checker = new ConditionCheckerSql(template.getCondition());
    String ctemplate = "";//checker.getSelection();
    Template t = context.getTemplate();
    Map<String, String> r = context.getReplacements();
    Collection<Replacement> replacements = t.getReplacements().values();
    for (Replacement replace : replacements) {
      String name = replace.getName();
      if ("String".equals(replace.getType())) {
        ctemplate = ctemplate.replaceAll("!" + name, String.format("'%s'", r.get(name)));
      } else {
        ctemplate = ctemplate.replaceAll("!" + name, r.get(name));
      }
    }

    condition_template = ctemplate;
    this.template = template;
    this.context = context;
  }

  public void setDeviceDatabaseConnectionPool(ConnectionPool pool) {
    this.pool = pool;
  }

  public List<ConditionResult> check() {
    State state = State.Invalid;
    if (pool != null) {
      //TODO: replace script values, for now ignore and set to true
      String vreplaced = condition_template.replaceAll("\\$[0-9]*\\$", "1");
      PreparedStatementProxy statement = new PreparedStatementProxy("SELECT 1 WHERE " + vreplaced);
      //System.out.println(statement);
      Connection conn = null;
      try {
        conn = pool.getConnection();
        PreparedStatement stmt = statement.createPreparedStatement(conn);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
          if (rs.getLong(1) == 1) {
            state = State.Valid;
          }
        }
        rs.close();
      } catch (SQLException ex) {
        Logger.getLogger(RuleSql.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
        pool.releaseConnection(conn);
      }
    }
    return checker.check();
  }

  public String getID() {
    return template.getId();
  }
}
