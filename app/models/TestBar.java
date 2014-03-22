package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created with IntelliJ IDEA.
 * User: mgt
 * Date: 22.03.14
 * Time: 12:05
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class TestBar extends Model {

    @Id
    public String id;

    public String name;

}

