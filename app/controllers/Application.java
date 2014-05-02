package controllers;

import com.avaje.ebean.Ebean;
import models.TestBar;
import models.ormtest.Product;
import models.ormtest.Tag;
import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class Application extends Controller {

    public static Result index() {
        return ok(views.html.index.render("Your new application is ready."));
    }

    public static Result addBar(){
        TestBar bar = Form.form(TestBar.class).bindFromRequest().get();
        bar.save();
        return redirect("/");
    }

    public static Result status(){
        return ok(views.html.status.render());
    }

    public static Result revroute(){
        return redirect(controllers.routes.Application.index()); //reverse routing!
    }

}
