package models.finder;

import com.avaje.ebean.Finder;
import models.Other;
import models.query.QOther;

public class OtherFinder extends Finder<Long,Other> {

  /**
   * Construct using the default EbeanServer.
   */
  public OtherFinder() {
    super(Other.class);
  }

  /**
   * Construct with a given EbeanServer.
   */
  public OtherFinder(String serverName) {
    super(Other.class, serverName);
  }

  /**
   * Start a new typed query.
   */
  public QOther where() {
     return new QOther(db());
  }
}
