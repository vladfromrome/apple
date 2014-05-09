package controllers;

import play.mvc.*;

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

}
