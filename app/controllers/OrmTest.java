package controllers;

import com.avaje.ebean.Ebean;
import models.ormtest.Customer;
import models.ormtest.Product;
import models.ormtest.Tag;
import models.ormtest.ZOrder;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mgt
 * Date: 04.04.14
 * Time: 23:53
 * The relationship schema: Tag-*---*-Product-*---*-Order-1---*-Customer
 */
public class OrmTest extends Controller {
    public static Result populateOrmtest(){
        deleteAll();
        populate();



        return ok("ok");
    }

    private static void deleteAll(){

        List<ZOrder> orders=ZOrder.FIND.all();          //delete all orders
        Ebean.delete(orders);
        List<Product> products = Product.FIND.all();    //delete all products // you need to delete orders first because of the product-order cascade direction
        Ebean.delete(products);
        List<Tag> tags=Tag.FIND.all();          //delete all tags
        Ebean.delete(tags);
        List<Customer> customers=Customer.FIND.all();          //delete all customers
        Ebean.delete(customers);

    }
    private static void populate(){
        Product p = new Product();                        //insert new product
        p.name="prod1";
        List<Tag> tags = new ArrayList<Tag>();
        Tag t1=new Tag();
        t1.name="tag1";
        Tag t2=new Tag();
        Tag t3=new Tag();
        t2.name="tag2";
        t3.name="tag3";
        t1.save();                                                   //insert some tags
        t2.save();
        t3.save();
        tags.add(t1);
        tags.add(t2);
        tags.add(t3);
        //tags.add(Tag.FIND.where().eq("name","tag1").findUnique());
        p.tags=tags;
        p.save();
        Tag t4=new Tag();                                         //insert random tag
        t4.name="tag4";
        t4.save();

        Customer c=new Customer("Jack");                    //insert new customer
        c.save();
        ZOrder o=new ZOrder(c);                             //insert new order
        o.products.add(Product.FIND.where().eq("name","prod1").findList().get(0));
        o.quantity=223;
        o.save();
    }
}
//PREVIOUS QUERIES//

