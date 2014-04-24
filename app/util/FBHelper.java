package util;

import facebook4j.*;
import facebook4j.Friend;
import facebook4j.User;
import facebook4j.auth.AccessToken;
import models.fbtest.*;
import play.Logger;
import play.mvc.Http.Context;

import java.util.List;

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
        AppUser user;
        try {
            Logger.debug("Looking for user in the DB");
            user = AppUser.FIND.where().eq("profile.user_id", fbUser.getId()).findUnique();
            Logger.info("User is loaded from db: "+user.toString());
        } catch (Exception e){
            Logger.debug("User not found in DB. Creating new user");
            user = loadNewUser(fbUser);
            user.save();
            user= AppUser.FIND.where().eq("profile.user_id", fbUser.getId()).findUnique();
            Logger.info("New user is created");
        }

        Context.current().response().setCookie("appUser", user.id.toString());
    }

    public static void logOut() {
        Context.current().response().discardCookie("fbToken");
        Context.current().response().discardCookie("appUser");
        Logger.info("Logged out successfully.");
    }

    public static AppUser loadNewUser(User fbUser) throws FacebookException {
        AppUser appUser = new AppUser(fbUser.getId(), fbUser.getName(), fbUser.getUsername(), fbUser.getGender(), fbUser.getLink().toString());
        return appUser;
    }

    public static void loadFriends() throws FacebookException {
        Facebook fb = getFBInstance();
        //get user's friends into user's profile list
        //for each user's friend:
        //add:user and common friends
        AppUser appUser = getCurrentUser();
        ResponseList<Friend> fbFriends =  fb.friends().getFriends();
        for (Friend f:fbFriends){
            AppFriend appFriend = new AppFriend(appUser,f.getId(),f.getName(),f.getUsername(),f.getGender(),f.getLink().toString());
            appUser.profile.friends.add(appFriend);

        }
        appUser.save();
        appUser.update();
        List<AppFriend> appFriends = appUser.profile.friends;
        for (AppFriend af:appFriends){
            ResponseList<Friend> mutualFs = fb.getMutualFriends(af.user_id);
            for (Friend f:mutualFs){
                af.friends.add(AppFriend.FIND.where().eq("appUser",appUser).eq("user_id",f.getId()).findUnique());
                //af.friends.add(appUser.profile);
            }
            af.save();
        }


    }

    public static String getAppStatus(){
        String s = "";
        Facebook fb = getFBInstance();
        AppUser appUser;
        try {
            appUser = AppUser.FIND.byId(Long.decode(Context.current().request().cookies().get("appUser").value()));
            s = "You are logged in as "+appUser.profile.name;
        }   catch (Exception e)                                                                           {
            return "You are not authenticated.";
        }
        return s;
    }

    public static AppUser getCurrentUser(){
        return AppUser.FIND.byId(Long.decode(Context.current().request().cookies().get("appUser").value()));
    }

}
