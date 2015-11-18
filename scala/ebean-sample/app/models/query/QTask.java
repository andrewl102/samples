package models.query;

import com.avaje.ebean.EbeanServer;
import models.Task;
import models.query.assoc.QAssocOther;
import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TQRootBean;
import org.avaje.ebean.typequery.TypeQueryBean;

/**
 * Query bean for Task.
 */
@TypeQueryBean
public class QTask extends TQRootBean<Task,QTask> {

  private static final QTask _alias = new QTask(true);

  /**
   * Return the shared 'Alias' instance used to provide properties to 
   * <code>select()</code> and <code>fetch()</code> 
   */
  public static QTask alias() {
    return _alias;
  }

  public PLong<QTask> id;
  public PString<QTask> name;
  public QAssocOther<QTask> other;


  /**
   * Construct with a given EbeanServer.
   */
  public QTask(EbeanServer server) {
    super(Task.class, server);
  }

  /**
   * Construct using the default EbeanServer.
   */
  public QTask() {
    super(Task.class);
  }

  /**
   * Construct for Alias.
   */
  private QTask(boolean dummy) {
    super(dummy);
  }
}
