package models.ormtest;

import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mgt
 * Date: 04.04.14
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 */
//@Entity
public class Customer extends Model{
    @Id
    public Long id;
    public static Finder<Long,Customer> FIND = new Finder<>(Long.class,Customer.class);

    public String name;
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    public List<ZOrder> orders;

    public Customer(String name) {
        this.name = name;
    }
}
