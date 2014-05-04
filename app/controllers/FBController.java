package controllers;


import com.avaje.ebean.Ebean;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.PictureSize;
import models.Image;
import models.fbtest.AppFriend;
import models.fbtest.AppUser;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import util.FBHelper;

import java.net.URL;

/**
 * Author: Vladimir Romanov
 * Date: 22.04.14
 * Time: 8:51
 */
public class FBController extends Controller {

    private static final String logOutUrl = "/status";
    private static final String logInUrl = "/status";

    //development in progress
    public static Result cleanDB() {
        //Ebean.delete(AppFriend.FIND.where().eq("appUser",FBHelper.getAppUser()).findList());
//        Ebean.delete(AppUser.FIND.all());
//        Ebean.delete(AppFriend.FIND.all());
        FBHelper.deleteFriends(FBHelper.getAppUser());
        return ok("DB is clean.");
    }

    public static Result logIn() {
        return redirect(FBHelper.getAuthUrl());
    }

    public static Result loginCallback(String oauthCode) {
        if (!oauthCode.equals("")) {
            try {
                FBHelper.logIn(oauthCode);
            } catch (FacebookException e) {
                Logger.info("fb error: " + e.getErrorMessage());
                return ok("fb error: " + e.getErrorMessage());
            }
            return redirect(logInUrl);
        }
        return redirect(logInUrl);
    }

    public static Result getFriendsList() {
        try {
            return ok(views.html.friendslist.render(FBHelper.getAllFriends()));
        } catch (Exception e) {
            Logger.info("no friends or error: " + e.getMessage());
            return ok("no friends or error: " + e.getMessage());
        }
    }

    public static Result logOut() {
        FBHelper.logOut();
        return redirect(logOutUrl);
    }

    public static Result loadFriends(){
        try {
            Logger.info("Loading friends of "+FBHelper.getAppUserName());
            FBHelper.loadFriends();
            return ok("friends are loaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ok(e.toString());
        }
    }

    //for testing purposes
    public static Result postMsg() {
        String message = "Bazinga! fb api test.";
        Facebook facebook = FBHelper.getFBInstance();
        try {
            facebook.postStatusMessage(message);
        } catch (FacebookException e) {
            Logger.info("fb error: " + e.getErrorMessage());
            return ok("fb error: " + e.getErrorMessage());
        }
        return ok();
    }

    //for testing purposes
    public static Result fbcommon(){
        try {

            //AppUser user = FBHelper.getAppUser().profile.friends=null;
            //return ok();
            return ok(views.html.friendslist.render(FBHelper.getCommonFriendsWith("1148117708")));
        } catch (Exception e){
            return ok("no common friends or error: " + e.getMessage());
        }
    }

    //for testing purposes
    public static  Result fbtest(){
        Facebook fb =  FBHelper.getFBInstance();
        try {
            URL url = fb.users().getPictureURL(FBHelper.getAppUser().profile.user_id, PictureSize.square);
            Image img = new Image(url.toString());
            img.save();
            return redirect(routes.ImageController.getImage(img.id));
            //return ok(url.toString());
        } catch (Exception e) {
            return ok(e.toString());
        }
    }

    //for testing purposes
    public static String getPicLink(){
        Facebook fb =  FBHelper.getFBInstance();
        try {
            URL url = fb.users().getPictureURL(FBHelper.getAppUser().profile.user_id, PictureSize.square);
            return url.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }

    //for testing purposes
    public static Result testPage(){
        return ok(views.html.fbtest.render());
    }

}
