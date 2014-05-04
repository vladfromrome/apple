package models.fbtest;

import models.Image;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * User: Vladimir Romanov
 * Date: 06.04.14
 * Time: 22:51
 */
@Entity
@Table(name = "friends")
public class AppFriend extends Model{
    @Id
    public Long id;
    public static Finder<Long,AppFriend> FIND = new Finder<>(Long.class, AppFriend.class);

    public String user_id;
    public String name;
    public String nickname;
    public String gender;
    public String link;
    public String profileImageLink;
    @OneToOne(cascade = CascadeType.ALL)
    public Image picture;
    @ManyToMany(cascade = {CascadeType.REMOVE,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable( name = "connections",
            joinColumns = { @JoinColumn(name = "friend_id")},
            inverseJoinColumns={@JoinColumn(name="connectedfriend_id")})
    public List<AppFriend> friends;
    @ManyToOne
    public AppUser appUser;   //todo.maybe move the field into a separate table?

    @Transient public CentralityMetrics cMetrics;

    public void getConnections(){}
    public void getCMetrics(){}
    public void getCommonFriends(AppFriend f){}
    public void getCommonFriends(AppFriend f1,AppFriend f2){}

    //to be continued...

    /**
     * autosaves new Friend entity into DB
     */
    public AppFriend(AppUser appUser,String user_id, String name, String nickname, String gender, String link, String profileImageLink) {
        this.appUser=appUser;
        this.user_id = user_id;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.link = link;
        this.profileImageLink = profileImageLink;
        this.picture=new Image(profileImageLink);
        this.save();
    }

    public Long picId(){
        return this.picture.id;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", has "+friends.size()+" friends"+
                '}';
    }
}
