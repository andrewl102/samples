package models.query;

import com.avaje.ebean.EbeanServer;
import models.Other;
import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TQRootBean;
import org.avaje.ebean.typequery.TypeQueryBean;

/**
 * Query bean for Other.
 */
@TypeQueryBean
public class QOther extends TQRootBean<Other,QOther> {

  private static final QOther _alias = new QOther(true);

  /**
   * Return the shared 'Alias' instance used to provide properties to 
   * <code>select()</code> and <code>fetch()</code> 
   */
  public static QOther alias() {
    return _alias;
  }

  public PLong<QOther> id;
  public PString<QOther> name;


  /**
   * Construct with a given EbeanServer.
   */
  public QOther(EbeanServer server) {
    super(Other.class, server);
  }

  /**
   * Construct using the default EbeanServer.
   */
  public QOther() {
    super(Other.class);
  }

  /**
   * Construct for Alias.
   */
  private QOther(boolean dummy) {
    super(dummy);
  }
}
