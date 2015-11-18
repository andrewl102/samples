package models.query.assoc;

import models.Other;
import models.query.QOther;
import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TQAssocBean;
import org.avaje.ebean.typequery.TQProperty;
import org.avaje.ebean.typequery.TypeQueryBean;

/**
 * Association query bean for AssocOther.
 */
@TypeQueryBean
public class QAssocOther<R> extends TQAssocBean<Other,R> {

  public PLong<R> id;
  public PString<R> name;

  /**
   * Eagerly fetch this association loading the specified properties.
   */
  @SafeVarargs
  public final R fetch(TQProperty<QOther>... properties) {
    return fetchProperties(properties);
  }

  public QAssocOther(String name, R root) {
    super(name, root);
  }
}
