package controllers;


import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.auth.AccessToken;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import util.FBHelper;

import javax.servlet.ServletException;

/**
 * Author: Vladimir Romanov
 * Date: 22.04.14
 * Time: 8:51
 */
public class FBController extends Controller {

    public static Result signIn(){
        return redirect(FBHelper.getAuthUrl());
    }

    public static Result callback(String oauthCode){
        if (!oauthCode.equals("")){
        try {
            response().setCookie("fbToken",FBHelper.getAccessToken(oauthCode));
        } catch (FacebookException e) {
            Logger.info("fb error: "+e.getErrorMessage());
            return ok("fb error: "+e.getErrorMessage());
        }
        return ok("fb: You are authorized!");
        }

        return redirect("/");
    }

    public static Result postMsg(){
        String message = "Bazinga! fb api test.";
        Facebook facebook = FBHelper.getFBInstance();
        try {
            facebook.postStatusMessage(message);
        } catch (FacebookException e) {
            Logger.info("fb error: "+e.getErrorMessage());
            return ok("fb error: "+e.getErrorMessage());
        }
        return ok();
    }

}
