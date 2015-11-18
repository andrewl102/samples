package models;

import models.finder.OtherFinder;
import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Other extends Model {

  public static final OtherFinder find = new OtherFinder();
  @Id
  public Long id;

  public String name;
}
