package models.query.assoc;

import models.Task;
import models.query.QTask;
import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TQAssocBean;
import org.avaje.ebean.typequery.TQProperty;
import org.avaje.ebean.typequery.TypeQueryBean;

/**
 * Association query bean for AssocTask.
 */
@TypeQueryBean
public class QAssocTask<R> extends TQAssocBean<Task,R> {

  public PLong<R> id;
  public PString<R> name;
  public QAssocOther<R> other;

  /**
   * Eagerly fetch this association loading the specified properties.
   */
  @SafeVarargs
  public final R fetch(TQProperty<QTask>... properties) {
    return fetchProperties(properties);
  }

  public QAssocTask(String name, R root) {
    super(name, root);
  }
}
