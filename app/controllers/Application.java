package controllers;

import models.TestBar;
import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    public static Result addBar(){
        TestBar bar = Form.form(TestBar.class).bindFromRequest().get();
        bar.save();
        return redirect("/");
    }

}
