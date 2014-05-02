package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mgt
 * Date: 22.03.14
 * Time: 12:05
 * To change this template use File | Settings | File Templates.
 */
//@Entity
public class TestBar extends Model {

    @Id
    public Long id;


    public String name;


    public static String getBars(){
        List<TestBar> bars = new Model.Finder(Long.class, TestBar.class).all();
        String s="<ul>";
        for (TestBar b:bars){
           s+="<li>"+b.name+"</li>";
        }
        s+="</ul>";
        return s;
    }
}

