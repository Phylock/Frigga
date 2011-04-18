package dk.itu.frigga.action.manager.block;

/**
 *
 * @author phylock
 */
public class Script extends BaseCondition{

  public enum Type
  {
    Once, Interval, Schedule
  }

  private final Type type;
  private final String call;
  private final long interval;
  private final String schedule;

  /**
   * The script is call with an interval in milliseconds
   * @param call
   * @param interval
   */
  public Script(String call, long interval) {
    this.call = call;
    this.interval = interval;
    this.schedule = null;
    type = Type.Interval;
  }

  /**
   * The script is called once when the rule is initialized
   * @param call
   */
  public Script(String call) {
    this.call = call;
    this.interval = -1;
    this.schedule = null;
    type = Type.Once;
  }

  /**
   * A scheduled job is inspired by how you describe a cron job(@see http://en.wikipedia.org/wiki/Cron)
   * a space separated string is used to define the interval
   * *    *    *    *    *    *    *  command to be executed
   * -    -    -    -    -    -    -
   * |    |    |    |    |    |
   * |    |    |    |    |    |
   * |    |    |    |    |    +----- day of week (0 - 7) (Sunday=0 or 7)
   * |    |    |    |    +---------- month (1 - 12)
   * |    |    |    +--------------- day of month (1 - 31)
   * |    |    +-------------------- hour (0 - 23)
   * |    +------------------------- minute (0 - 59)
   * +------------------------------ seconds (0 - 59)

   *
   * possible values:
   * * | anything
   * / | incremental range
   * , | a list of values
   * - | range of values
   * ? | ignore
   * examples:
   * 0 0 *    *   * * | Run once an hour, seconds and minute is 0
   * 0 0 7-18 1-5 * * | Run once an hour, all weekdays between 7 and 18
   * @param call
   * @param schedule
   */
  public Script(String call, String schedule) {
    this.call = call;
    this.interval = -1;
    this.schedule = schedule;
    this.type = Type.Schedule;
  }

  public String getCall() {
    return call;
  }

  public long getInterval() {
    return interval;
  }

  public String getSchedule() {
    return schedule;
  }

  public Type getType() {
    return type;
  }

}
