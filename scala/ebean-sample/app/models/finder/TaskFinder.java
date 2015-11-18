package models.finder;

import com.avaje.ebean.Finder;
import models.Task;
import models.query.QTask;

public class TaskFinder extends Finder<Long,Task> {

  /**
   * Construct using the default EbeanServer.
   */
  public TaskFinder() {
    super(Task.class);
  }

  /**
   * Construct with a given EbeanServer.
   */
  public TaskFinder(String serverName) {
    super(Task.class, serverName);
  }

  /**
   * Start a new typed query.
   */
  public QTask where() {
     return new QTask(db());
  }
}
