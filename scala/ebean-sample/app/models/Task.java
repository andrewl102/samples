package models;

import models.finder.TaskFinder;
import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Task extends Model {

  public static final TaskFinder find = new TaskFinder();
  @Id
  public Long id;

  public String name;

  @OneToOne(fetch = FetchType.LAZY)
  public Other other;
}
