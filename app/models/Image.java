package models;

import play.Logger;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.*;
import java.net.*;

/**
 * Author: Vladimir Romanov
 * Date: 03.05.14
 * Time: 9:17
 */
@Entity
@Table(name = "images")
public class Image extends Model {
    @Id
    public Long id;
    public static Finder<Long, Image> FIND = new Finder<>(Long.class, Image.class);

    @Lob
    public byte[] content;
    public String contentType;



    public static Image saveNewImage(String link){
        final Image img = new Image(link);
        img.save();
        img.refresh();
        return img;
    }

    public static Long saveAndGetId(String link){
        final Image img = Image.saveNewImage(link);
        return img.id;
    }

    public Image(String link){
        try {
            final URL url = new URL(link);
            final HttpURLConnection connection = (HttpURLConnection)  url.openConnection();
            final InputStream inputStream =connection.getInputStream();
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            if (link.equals("")) {
                link = "http://dj.ru/user_music/covers/67/608667.jpg";
            }
            Logger.debug("downloading image: " + link);
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                bOut.write(data, 0, nRead);
            }
            bOut.flush();

            this.content = bOut.toByteArray();
            this.contentType = connection.getContentType();

        } catch (Exception e) {
            Logger.debug("ERROR while downloading image from "+link+" \n"+e.toString());
            this.content = null;
            this.contentType="nopicture";
        }
    }

    /*public Image(byte[] content, String contentType) {
        this.content = content;
        if (contentType.equals("")) {
            this.contentType = "image/jpeg";
        } else {
            this.contentType = contentType;
        }
    }

    public Image(byte[] content) {
        this.content = content;
        this.contentType = "image/jpeg";
    }*/

   /* public byte[] downloadImage(String address) throws IOException {
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        if (address.equals("")) {
            address = "http://dj.ru/user_music/covers/67/608667.jpg";
        }
        try {
            URL url = new URL(address);
            conn = url.openConnection();
            in = conn.getInputStream();
            Logger.debug("downloading image: " + address);
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = in.read(data, 0, data.length)) != -1) {
                bOut.write(data, 0, nRead);
            }
            bOut.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
            }
        }
        return bOut.toByteArray();
    }*/
}
