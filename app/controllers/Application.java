package controllers;


import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.PictureSize;
import models.AppFriend;
import models.GraphData;
import models.Image;
import play.Logger;
import play.Routes;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.FBHelper;
import views.html.ajaxtest;
import views.html.friendslistpretty;
import views.html.graph;

import java.net.URL;
import java.util.*;

/**
 * Author: Vladimir Romanov
 * Date: 22.04.14
 * Time: 8:51
 */
public class Application extends Controller {

    //<editor-fold desc="Constants">
    private static final String logOutUrl = "/";
    private static final String logInUrl = "/";
    //</editor-fold>

    //<editor-fold desc="Results">

    public static Result index() {
        return ok(views.html.index.render("Your new application is ready."));
    }

    public static Result status(){
        return ok(views.html.status.render());
    }

    public static Result jsRoutes() {
        response().setContentType("text/javascript");
        return ok(Routes.javascriptRouter("appRoutes", routes.javascript.Application.fbcommon(),
                routes.javascript.Application.getFriends(),
                routes.javascript.Application.getFriendsList(),
                routes.javascript.Application.graph()
        ));
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

    public static Result getFriends() {
        try {
            Logger.info("Loading friends of " + FBHelper.getAppUserName());
            FBHelper.loadFriends();
            return ok(views.html.friendslist.render(FBHelper.getAllFriends()));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(e.toString());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Utilities">

    /**
     * Forms a friendlist by given list of user_ids.
     *
     * @return Graph data for the friendlist.
     */
    public static GraphData getGraphData(List<String> friendIds) {
        List<AppFriend> friendlist = new ArrayList<>();
        try {
            AppFriend userProfile = FBHelper.getAppUser().profile;
            if (!friendIds.contains(userProfile.user_id)) {   //add user to the list if he is not there yet.
                friendlist.add(userProfile);
            }
        } catch (Exception e) {
            return null;
        }
        for (String userId : friendIds) {
            AppFriend f = FBHelper.getFriend(userId);
            if (f != null) {
                friendlist.add(f);
            } else {
                Logger.debug("Friend " + userId + " not found!");
            }
        }
        GraphData graphData = new GraphData(friendlist);
        return graphData;
    }

    public static Result graph(String ids) {
//        final List<AppFriend> allFriends = FBHelper.getAllFriends();
//        allFriends.add(FBHelper.getAppUser().profile);
//        List<String> q = new LinkedList<>();
//        q.add("1140600495");
//        q.add("500454221");
//        q.add("10217893");
//        final GraphData graphdata = new GraphData(allFriends);

        String[] selectedIDs = ids.split(",");
        // final GraphData graphdata = getGraphData(FBHelper.getCommonFriendIDs(selectedIDs));
        final GraphData graphdata = getGraphData(Arrays.asList(selectedIDs));
        Logger.debug(graphdata.toString());
        return ok(graph.render(graphdata.getFriendNodes()));
    }
    //</editor-fold>

    //<editor-fold desc="Test&Debug">
    public static Result checkGraph() {
        //GraphData graphData = new GraphData(FBHelper.getAllFriends());
        List<String> q = new ArrayList<>();
        q.add("526883024");
        q.add("629531241");
        GraphData graphData = getGraphData(q);
        Logger.debug(graphData.toString());
        return ok(graphData.toString());
    }


    //for testing purposes
    public static Result postMsg() {
        final String message = "Bazinga! fb api test.";
        Facebook facebook = FBHelper.getFBInstance();
        try {
            facebook.postStatusMessage(message);
        } catch (FacebookException e) {
            Logger.info("fb error: " + e.getErrorMessage());
            return ok("fb error: " + e.getErrorMessage());
        }
        return ok();
    }

    public static Result fbcommon(String ids) {
        try {
            String[] selectedIDs = ids.split(",");
            System.out.println("userIDs = " + Arrays.toString(selectedIDs));
            Map<AppFriend, List<AppFriend>> allCommonFriends = new HashMap<>();
            for (String userID : selectedIDs) {
                AppFriend friend = AppFriend.FIND.where().eq("user_id", userID).eq("appUser", FBHelper.getAppUser()).findUnique();
                System.out.println("friend = " + friend);
                List<AppFriend> commonFriends = FBHelper.getCommonFriendsWith(userID);
                System.out.println("commonFriends = " + commonFriends);
                allCommonFriends.put(friend, commonFriends);
            }

            //AppUser user = FBHelper.getAppUser().profile.friends=null;
            //return ok();
            //todo: optimize this method, don't search twice for the friend
//            AppFriend friend = AppFriend.FIND.where().eq("user_id", ids).eq("appUser", FBHelper.getAppUser()).findUnique();

            return ok(friendslistpretty.render(allCommonFriends));
        } catch (Exception e) {
            return ok("<div class='alert alert-error fade in'>No common friends or error:"+e.getMessage()+"</div>");
        }
    }

    //for testing purposes
    public static Result fbcommontest() {
        try {

            //AppUser user = FBHelper.getAppUser().profile.friends=null;
            //return ok();
            //return ok(views.html.friendslist.render(FBHelper.getCommonFriendsWith("1148117708")));
            return ok(views.html.friendslist.render(FBHelper.getCommonFriendsWith("1140600495")));
        } catch (Exception e) {
            return ok("<div class='alert alert-error fade in'> No common friends </div>");
        }
    }

    //for testing purposes
    public static Result fbtest() {
        Facebook fb = FBHelper.getFBInstance();
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
    public static String getPicLink() {
        Facebook fb = FBHelper.getFBInstance();
        try {
            URL url = fb.users().getPictureURL(FBHelper.getAppUser().profile.user_id, PictureSize.square);
            return url.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }

    //for testing purposes
    public static Result testPage() {
        List<String> ids = new ArrayList<>();
        ids.add("868290636");
        ids.add("100000288916361");
        ids.add("1140600495");
        return ok(getGraphData(ids).toString());
    }
    //</editor-fold>




}
