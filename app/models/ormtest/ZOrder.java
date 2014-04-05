package models.ormtest;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mgt
 * Date: 04.04.14
 * Time: 20:48
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class ZOrder extends Model{
    @Id
    public Long id;
    public static Finder<Long,ZOrder> FIND = new Finder<>(Long.class,ZOrder.class);

    public Integer quantity;
    @ManyToMany(cascade = CascadeType.ALL)
    public List<Product> products;
    @ManyToOne
    public Customer customer;

    public ZOrder(Customer customer) {
        this.customer = customer;
    }
}
