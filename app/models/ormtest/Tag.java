package models.ormtest;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mgt
 * Date: 04.04.14
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
//@Entity
public class Tag extends Model{
    @Id
    public Long id;
    public static Finder<Long,Tag> FIND = new Finder<>(Long.class,Tag.class);

    public String name;
    @ManyToMany
    public List<Product> products;


}
