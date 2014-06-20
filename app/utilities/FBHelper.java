package utilities;

import com.avaje.ebean.Ebean;
import facebook4j.*;
import facebook4j.Friend;
import facebook4j.User;
import facebook4j.auth.AccessToken;
import models.AppFriend;
import models.AppUser;
import play.Logger;
import play.mvc.Http.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Vladimir Romanov
 * Date: 09.04.14
 * Time: 21:35
 */
public class FBHelper {

    //<editor-fold desc="Constants">
    private static final String appId = "531369976977751";       //client_id
    private static final String appSecret = "324497e65821722d2af291731994bb54";
    private static final String callbackURL = "http://localhost:9000/fbcallback";
    private static final String permissions = "";
    //private static final String permissions = "publish_actions";
    //private static final String callbackToIndex = "http://localhost:9000/fbcallback";
    //</editor-fold>

    //<editor-fold desc="FB object mngmnt, login/logout and appStatus">
    /**
     * @return a fresh instance of facebook4j.facebook object
     */
    private static Facebook getSimpleFBInstance() {
        Facebook facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(appId, appSecret);
        facebook.setOAuthPermissions(permissions);
        return facebook;
    }

    /**
     * @return an instance of facebook4j.facebook object fueled with an Access Token taken from cookies.
     */
    public static Facebook getFBInstance() {
        Facebook facebook = getSimpleFBInstance();
        //String fbToken=play.mvc.Http.Context.current().request().cookies().get("fbToken").value();
        try {
            facebook.setOAuthAccessToken(new AccessToken(Context.current().request().cookies().get("fbToken").value()));
        } catch (Exception e) {
        }
        return facebook;
    }



    /**
     * @return a url to redirect user to in order to login.
     */
    public static String getAuthUrl() {
        Facebook facebook = getFBInstance();
        return facebook.getOAuthAuthorizationURL(callbackURL);
    }

    /**
     * Given and oauthCode acquires an Access Token from facebook and loads or creates current user entity.
     * Saves the access token and user's id into cookies.
     *
     * @param oauthCode
     */
    public static void logIn(String oauthCode) throws FacebookException {
        Facebook fb = getFBInstance();
        Context.current().response().setCookie("fbToken", fb.getOAuthAccessToken(oauthCode, callbackURL).getToken());
        facebook4j.User fbUser = fb.users().getMe();
        AppUser user;
        try {
            Logger.debug("Looking for user in the DB");
            user = AppUser.FIND.where().eq("profile.user_id", fbUser.getId()).findUnique();
            Logger.info("User is loaded from db: " + user.toString());
        } catch (Exception e) {
            Logger.debug("User not found in DB. Creating new user");
            createNewUser(fbUser,fb);
            user = AppUser.FIND.where().eq("profile.user_id", fbUser.getId()).findUnique();
            Logger.info("New user is created");
        }
        Context.current().response().setCookie("appUser", user.id.toString());
    }

    /**
     * Discards current user data from cookies
     */
    public static void logOut() {
        Context.current().response().discardCookie("fbToken");
        Context.current().response().discardCookie("appUser");
        Logger.info("Logged out successfully.");
    }

    /**
     * @return a String describing application status.
     */
    public static String getAppStatus() {
        String s = "";
        Facebook fb = getFBInstance();
        AppUser appUser;
        try {
            appUser = AppUser.FIND.byId(Long.decode(Context.current().request().cookies().get("appUser").value()));
            s = "You are logged in as " + appUser.profile.name;
        } catch (Exception e) {
            s = "You are not authenticated.";
        }
        return s;
    }
    //</editor-fold>

    //<editor-fold desc="Users and Friends persistence">
    /**
     * Persists a facebook user entity into DB as application user.
     */
    public static AppUser createNewUser(User fbUser, Facebook fb) throws FacebookException {
        AppUser appUser = new AppUser(fbUser.getId(),
                fbUser.getName(), fbUser.getUsername(),
                fbUser.getGender(), fbUser.getLink().toString(),
                fb.users().getPictureURL(fbUser.getId(), PictureSize.square).toString(),
                fb.users().getPictureURL(fbUser.getId(), PictureSize.large).toString()
        );
        return appUser;
    }


    /**
     * Persists friends of the current user into DB
     *
     * @throws FacebookException
     */
    public static void loadFriends() throws FacebookException {
        if (getAppUser().friendEntities.size() > 0) {
            return;
        }

        Facebook fb = getFBInstance();
        //get user's friends into user's profile list
        AppUser appUser = getAppUser();
        ResponseList<Friend> fbFriends = fb.friends().getFriends();
        for (Friend f : fbFriends) {
            Logger.debug("Fetching friend: " + f.getId());
            User u = fb.users().getUser(f.getId());
            Logger.debug("User entity: " + u.toString());
            AppFriend appFriend = new AppFriend(
                    appUser,
                    u.getId(),
                    u.getName(),
                    u.getUsername(),
                    u.getGender(),
                    u.getLink().toString(),
                    fb.users().getPictureURL(u.getId(),PictureSize.square).toString());
            appFriend.save();
            appUser.profile.friends.add(appFriend);
        }
        appUser.profile.update();
        //for each user's friend:
        //add:user and mutual friends
        List<AppFriend> appUserFriends = appUser.profile.friends;

        for (AppFriend af : appUserFriends) {
            af.friends.add(appUser.profile);
            ResponseList<Friend> mutualFs = fb.getMutualFriends(af.user_id);
            for (Friend f : mutualFs) {
                af.friends.add(AppFriend.FIND.where().eq("appUser", appUser).eq("user_id", f.getId()).findUnique());
            }
            Logger.debug("Persisting friend: " + af.toString());
            af.save();
        }

    }

    //</editor-fold>



    //<editor-fold desc="Current user utils">

    /**
     * @return Current AppUser entity loaded from DB
     */
    public static AppUser getAppUser() throws NullPointerException {
        return AppUser.FIND.byId(Long.decode(Context.current().request().cookies().get("appUser").value()));
    }

    //Bool Get User
    public static boolean userLogged(){
        try {
            AppUser u = getAppUser();
            String s = u.profile.name;
           // Logger.debug("Current user is "+u.profile.name);
            return true;
        } catch (Exception e) {
            Logger.debug("User not logged in.");
            return false;
        }
    }

    /**
     * @return a String with name of the user, that is currently logged in
     */
    //TODO Store user name in cookies.
    public static String getAppUserName() throws NullPointerException {
        return getAppUser().profile.name;
    }
    //</editor-fold>



    //<editor-fold desc="Fetching friends from DB">
    /**
     * @return a list of friends of current user
     */
    public static List<AppFriend> getAllFriends() throws NullPointerException {
        return AppFriend.FIND.where().eq("appUser", getAppUser()).orderBy("name").findList();
    }


    /**
     * @return a friend of the current user given his facebook user_id
     * @param fb_user_id
     */
    public static AppFriend getFriend(String fb_user_id) throws NullPointerException{
        try{
            return AppFriend.FIND.where().eq("user_id",fb_user_id).eq("appUser",getAppUser()).findUnique();
        } catch (Exception e){
            Logger.debug("Error while retrieving friend "+fb_user_id+": "+e.toString());
            return null;
        }
    }

    /**
     * @param fb_user_id
     * @return a list of common friends of current user and friend with facebook Id = friendId
     */
    public static List<AppFriend> getCommonFriendsWith(String fb_user_id) throws NullPointerException {
        List<AppFriend> cf = AppFriend.FIND.where().eq("user_id", fb_user_id).eq("appUser", getAppUser()).findUnique().friends;
        String currentUserFbId = getAppUser().profile.user_id;
        for (int i=0;i<cf.size();i++){
            if (cf.get(i).user_id.equals(currentUserFbId)) cf.remove(i); //removing current user from the list of common friends
        }
        Collections.sort(cf);
        return cf;
    }

    public static List<AppFriend> getFriendsfromFbIds(List<String> ids){
        List<AppFriend> flist = new ArrayList<>();
        for (String fb_user_id: ids){
            flist.add(AppFriend.FIND.where().eq("user_id", fb_user_id).eq("appUser", getAppUser()).findUnique());
        }
        Collections.sort(flist);
        return flist;
    }

    public static List<String> getCommonFriendIDs(String[] selectedIDs) {
        List<String> commonFriendIDs = new LinkedList<>();
        for (String selectedID : selectedIDs) {
            List<AppFriend> commonFriends = getCommonFriendsWith(selectedID);
            for (AppFriend commonFriend : commonFriends) {
                String id = String.valueOf(commonFriend.user_id);
                if(!commonFriendIDs.contains(id)){
                    commonFriendIDs.add(id);
                }
            }
            if(!commonFriendIDs.contains(selectedID)){
                commonFriendIDs.add(selectedID);
            }
        }
        return commonFriendIDs;
    }
    //</editor-fold>


}
