/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.ConditionResult;
import dk.itu.frigga.action.Rule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phylock
 */
public class ActionWorker {
  private final Worker worker;

  private final List<Rule> rules;
  //private final Map<Rule, Rule.Validation> validations;
  //private final Map<Rule, Map<String, Rule.State>> states;
  private final BlockingQueue<Rule> queue;
  
  public ActionWorker() {
    this.queue = new LinkedBlockingQueue<Rule>();
    this.rules = new ArrayList<Rule>();
    //this.states = new HashMap<Rule, Map<String,Rule.State>>();
    //this.validations = new HashMap<Rule, Rule.Validation>();
    this.worker = new Worker();
  }

  public void start()
  {
    worker.start();
  }

  public void stop()
  {
    worker.kill();
  }

  public void addRule(Rule r) {
    synchronized (rules) {
      //states.put(r, new HashMap<String, Rule.State>());
      rules.add(r);
      rules.notifyAll();
    }
  }

    public void removeRule(Rule r) {
    synchronized (rules) {
      //states.remove(r);
      rules.remove(r);
      rules.notifyAll();
    }
  }

  private class Worker extends Thread {
    private final static int delay = 5000;
    private int current = 0;
    private volatile boolean running;

    public Worker() {
      this.setName("ActionWorker");
    }

    public void kill() {
      running = false;
      this.interrupt();
    }

    @Override
    public void run() {
      running = true;

      while (running) {
        Rule rule = null;
        synchronized (rules) {
          while (rules.isEmpty()) {
            try {
              rules.wait();
            } catch (InterruptedException ex) {
            }
          }
          if (current >= rules.size()) {
            current = 0;
          }
          rule = rules.get(current++);
        }
        //List<ConditionResult> results = rule.check();
        /*for(ConditionResult result : results)
        {
          String id = "";
          final int length = result.getScopeIdCount();
          for(int i = 0; i < length; i++)
          {
            if(i != 0)
            {
              id += ",";
            }
            //id += i + ":" + result.getScopeId(i);
          }
        }*/
        /*if(current_state != states.get(rule))
        {
          states.put(rule, current_state);
          System.out.println("state change: " + rule.getID() + " is now " + current_state);
          //TODO: notify listeners
        }*/
      }
      try {
        Thread.sleep(delay);
      } catch (InterruptedException ex) {
        Logger.getLogger(ActionWorker.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}
