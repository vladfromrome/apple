package util;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;

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
            facebook.setOAuthAccessToken(new AccessToken(play.mvc.Http.Context.current().request().cookies().get("fbToken").value()));
        } catch (Exception e) {}
        return facebook;
    }

    public static String getAccessToken(String oauthCode) throws FacebookException {
        return getSimpleFBInstance().getOAuthAccessToken(oauthCode,callbackURL).getToken();
    }

    public static String getAuthUrl() {
        Facebook facebook = getFBInstance();
        return facebook.getOAuthAuthorizationURL(callbackURL);
    }

}
