package models;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Author: Vladimir Romanov
 * Date: 08.05.14
 * Time: 12:52
 */
public class GraphNode {

    List<String> connections;

    private String user_id;
    private String name;
    private String nickname;
    private String gender;
    private Long pictureId;

    private String dc;
    private String bc;
    private String cc;
    private String dc_n;
    private String bc_n;
    private String cc_n;

    public GraphNode(List<String> connections,String user_id, String name,String nickname, String gender, Long pictureId,Float dc, Float bc, Float cc, Float dc_n, Float bc_n, Float cc_n) {
        this.connections=connections;
        this.user_id=user_id;
        this.name=name;
        this.nickname = nickname;
        this.gender = gender;
        this.pictureId = pictureId;

        final NumberFormat formatter = NumberFormat.getNumberInstance(Locale.UK);
        formatter.setMaximumFractionDigits(2);

        this.dc = formatter.format(dc);
        this.bc = formatter.format(bc);
        this.cc = formatter.format(cc);
        this.dc_n = formatter.format(dc_n);
        this.bc_n = formatter.format(bc_n);
        this.cc_n = formatter.format(cc_n);
    }

    public List<String> getConnections() {
        return connections;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getGender() {
        return gender;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public String getDc() {
        return dc;
    }

    public String getBc() {
        return bc;
    }

    public String getCc() {
        return cc;
    }

    public String getDc_n() {
        return dc_n;
    }

    public String getBc_n() {
        return bc_n;
    }

    public String getCc_n() {
        return cc_n;
    }

    @Override
    public String toString() {
        String s= "GraphNode{" +
                "user_id='" + user_id + '\'' +
                "name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", pictureId=" + pictureId +
                ", dc='" + dc + '\'' +
                ", bc='" + bc + '\'' +
                ", cc='" + cc + '\'' +
                ", dc_n='" + dc_n + '\'' +
                ", bc_n='" + bc_n + '\'' +
                ", cc_n='" + cc_n + '\'' +
                "\n"+
                "conndected to users: ";
        for (String c:connections){
            s+=c+" ";
        }
                s+='}';
        return s;
    }
}
