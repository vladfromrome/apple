package models.fbtest;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Author: Vladimir Romanov
 * Date: 24.04.14
 * Time: 12:28
 */
@Entity
@Table(name = "users")         //otherwise database gives ERROR: syntax error at or near "user" Position: 14
public class User extends Model{
    @Id
    public Long id;
    public static Finder<Long,User> FIND = new Finder<>(Long.class, User.class);

    @OneToOne(cascade = CascadeType.ALL)
    public Friend profile;

    public User(String user_id, String name, String nickname, String gender, String link) {
        this.profile = new Friend(user_id,name,nickname,gender,link);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + profile.name + '\'' +
                '}'
                ;
    }
}
