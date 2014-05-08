package models.fbtest;

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

    public String user_id;
    public String name;
    public String nickname;
    public String gender;
    public Long pictureId;

    public String dc;
    public String bc;
    public String cc;
    public String dc_n;
    public String bc_n;
    public String cc_n;

    public GraphNode(List<String> connections,String user_id, String name,String nickname, String gender, Long pictureId,Float dc, Float bc, Float cc, Float dc_n, Float bc_n, Float cc_n) {
        this.connections=connections;
        this.user_id=user_id;
        this.name=name;
        this.nickname = nickname;
        this.gender = gender;
        this.pictureId = pictureId;

        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.UK);
        formatter.setMaximumFractionDigits(2);

        this.dc = formatter.format(dc);
        this.bc = formatter.format(bc);
        this.cc = formatter.format(cc);
        this.dc_n = formatter.format(dc_n);
        this.bc_n = formatter.format(bc_n);
        this.cc_n = formatter.format(cc_n);
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
