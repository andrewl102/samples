package models;


import models.query.QTask;

/**
 * Created by andrewlynch on 18/11/15.
 */
public class Access {
  public static String test() {
    QTask eq = Task.find.where().id.eq(1L);
    Task unique = eq.findUnique();
    if(unique == null) {
      return "No data";
    }
    else return unique.name;
  }
}
