package controllers;

import models.Image;
import play.Logger;
import play.mvc.*;

import java.io.IOException;

/**
 * Author: Vladimir Romanov
 * Date: 03.05.14
 * Time: 10:29
 */
public class ImageController extends Controller {

    /**
     * @return An image from DB by id
     */
    public static Result getImage(Long id) {
        try {
            Logger.debug("fetching image");
            Image image = Image.FIND.byId(id);
            return ok(image.content).as(image.contentType);
        } catch (Exception e) {
            Logger.debug("Error while fetching image "+id+": "+e.toString());
            return ok("image not fetched");
        }
    }

}
