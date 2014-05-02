package models.ormtest;

import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * User: mgt
 * Date: 04.04.14
 * Time: 11:09
 */
//@Entity
public class Product extends Model{
    @Id
    public Long id;
    public static Finder<Long,Product> FIND = new Finder<>(Long.class,Product.class);

    public String name;
    @ManyToMany(mappedBy = "products",cascade = CascadeType.ALL)
    public List<Tag> tags;
    @ManyToMany (cascade = CascadeType.ALL)
    public List<ZOrder> orders;


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
