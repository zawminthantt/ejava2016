package ejava2016.pt08.ca1.rest;

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class AppConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return super.getClasses(); //To change body of generated methods, choose Tools | Templates.
    }
	
}
