package models.fbtest;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * User: Vladimir Romanov
 * Date: 06.04.14
 * Time: 22:51
 */
@Entity
public class Friend extends Model{
    @Id
    public Long id;
    public static Finder<Long,Friend> FIND = new Finder<>(Long.class, Friend.class);

    //public boolean appUser=false;
    public String user_id;
    public String name;
    public String nickname;
    public String gender;
    public String link;
    public String profileImageLink;
    @ManyToMany//(cascade = CascadeType.ALL)
    @JoinTable( name = "friendindex",
            joinColumns = { @JoinColumn(name = "friend_id")},
            inverseJoinColumns={@JoinColumn(name="connectedfriend_id")})
    public List<Friend> friends;

//    public String firstName;
//    public String lastName;
    @Transient public CentralityMetrics cMetrics;

    public void getConnections(){}
    public void getCMetrics(){}
    public void getCommonFriends(Friend f){}
    public void getCommonFriends(Friend f1,Friend f2){}

    //to be continued...


    public Friend(String user_id, String name, String nickname, String gender, String link) {
        this.user_id = user_id;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.link = link;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
