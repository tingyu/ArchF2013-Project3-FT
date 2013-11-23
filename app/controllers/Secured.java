package controllers;

import play.mvc.*;
import play.mvc.Http.*;

import models.*;

public class Secured extends Security.Authenticator {
    
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("email");
    }
    
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.login());
    }

    // Access rights
    public static boolean isLoggedIn() {
    	if(Context.current().request().username().equals("admin@admin.com"))
    		return true;
    	else
    		return false;
    }
    
}