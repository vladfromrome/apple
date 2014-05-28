package controllers;

import play.mvc.*;
import views.html.ajaxtest;

public class Application extends Controller {

    public static Result index() {
        return ok(views.html.index.render("Your new application is ready."));
    }

    public static Result status(){
        return ok(views.html.status.render());
    }

    public static Result revroute(){
        return redirect(controllers.routes.Application.index()); //reverse routing!
    }


    //Testing ajax
    public static Result ajax()
    {
        for (int i = 0; i < 500000; i++) {
            System.out.println("blah");
        }
        return ok("Here's my server-side data");
    }

    public static Result testAjax()
    {

        return ok(ajaxtest.render());
    }




}
