package util;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import models.fbtest.User;
import play.Logger;
import play.mvc.Http.Context;

/**
 * User: Vladimir Romanov
 * Date: 09.04.14
 * Time: 21:35
 */
public class FBHelper {
    private static final String appId = "531369976977751";       //client_id
    private static final String appSecret = "324497e65821722d2af291731994bb54";
    private static final String callbackURL = "http://localhost:9000/fbcallback";
    private static final String permissions = "publish_actions";
    //private static final String callbackToIndex = "http://localhost:9000/fbcallback";

    private static Facebook getSimpleFBInstance() {
        Facebook facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(appId, appSecret);
        //facebook.setOAuthPermissions(permissions);
        return facebook;
    }

    public static Facebook getFBInstance() {
        Facebook facebook = getSimpleFBInstance();
        //String fbToken=play.mvc.Http.Context.current().request().cookies().get("fbToken").value();
        try {
            facebook.setOAuthAccessToken(new AccessToken(Context.current().request().cookies().get("fbToken").value()));
        } catch (Exception e) {
        }
        return facebook;
    }

    public static String getAccessToken(String oauthCode, Facebook fb) throws FacebookException {
        return fb.getOAuthAccessToken(oauthCode, callbackURL).getToken();
    }

    public static String getAuthUrl() {
        Facebook facebook = getFBInstance();
        return facebook.getOAuthAuthorizationURL(callbackURL);
    }

    public static void logIn(String oauthCode) throws FacebookException {
        Facebook fb = getFBInstance();
        Context.current().response().setCookie("fbToken", FBHelper.getAccessToken(oauthCode, fb));
        facebook4j.User fbUser = fb.users().getMe();
        User user;
        try {
            Logger.debug("Looking for user in the DB");
            user = User.FIND.where().eq("profile.user_id", fbUser.getId()).findUnique();
            Logger.info("User is loaded from db: "+user.toString());
        } catch (Exception e){
            Logger.debug("User not found in DB. Creating new user");
            user = loadNewUser(fbUser);
            user.save();
            user= User.FIND.where().eq("profile.user_id", fbUser.getId()).findUnique();
            Logger.info("New user is created");
        }

        Context.current().response().setCookie("appUser", user.id.toString());
    }

    public static void logOut() {
        Context.current().response().discardCookie("fbToken");
        Context.current().response().discardCookie("appUser");
        Logger.info("Logged out successfully.");
    }

    public static User loadNewUser(facebook4j.User fbUser) throws FacebookException {
        User user = new User(fbUser.getId(), fbUser.getName(), fbUser.getUsername(), fbUser.getGender(), fbUser.getLink().toString());
        return user;
    }

    public static void loadFriends() {
        Facebook fb = getFBInstance();

    }

    public static String getAppStatus(){
        String s = "";
        Facebook fb = getFBInstance();
        User user;
        try {
            user = User.FIND.byId(Long.decode(Context.current().request().cookies().get("appUser").value()));
            s = "You are logged in as "+user.profile.name;
        }   catch (Exception e)                                                                           {
            return "You are not authenticated.";
        }
        return s;
    }

}
