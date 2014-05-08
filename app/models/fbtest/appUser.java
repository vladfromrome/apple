package models.fbtest;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Author: Vladimir Romanov
 * Date: 24.04.14
 * Time: 12:28
 */
@Entity
@Table(name = "users")         //otherwise database gives ERROR: syntax error at or near "user" Position: 14
public class AppUser extends Model{
    @Id
    public Long id;
    public static Finder<Long,AppUser> FIND = new Finder<>(Long.class, AppUser.class);

    @OneToOne(cascade = CascadeType.REMOVE)
    public AppFriend profile;
    @OneToMany(mappedBy = "appUser")
    public List<AppFriend> friendEntities;

    /**
     * autosaves new user into DB
     */
    public AppUser(String user_id, String name, String nickname, String gender, String link, String profileImageLink) {
        this.profile = new AppFriend(this,user_id,name,nickname,gender,link,profileImageLink);
        this.profile.save();
        this.save();
    }

    public Long getPictureId(){
        return this.profile.picture.id;
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
