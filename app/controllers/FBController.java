package controllers;


import com.avaje.ebean.Ebean;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import models.fbtest.Friend;
import models.fbtest.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import util.FBHelper;

import javax.servlet.ServletException;
import java.util.List;

/**
 * Author: Vladimir Romanov
 * Date: 22.04.14
 * Time: 8:51
 */
public class FBController extends Controller {

    private static final String logOutUrl = "/status";
    private static final String logInUrl = "/status";

    public static Result cleanDB() {
        Ebean.delete(User.FIND.all());
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

    public static Result getFriendsList() {
        try {
            return ok(views.html.friendslist.render(FBHelper.getFBInstance().friends().getFriends()));
        } catch (Exception e) {
            Logger.info("fb error: " + e.getMessage());
            return ok(e.getMessage());
        }
    }

    public static Result logOut() {
        FBHelper.logOut();
        return redirect(logOutUrl);
    }

}
