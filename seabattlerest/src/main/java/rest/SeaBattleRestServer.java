package rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

//import static sun.net.www.protocol.http.AuthCacheValue.Type.Server;

public class SeaBattleRestServer {
    public static void main(String[] args) throws Exception {

        ServletContextHandler context = new
                ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        Server jettyServer = new Server(8090);
        jettyServer.setHandler(context);
        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
                SeaBattleRESTService.class.getCanonicalName());

        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e){

            jettyServer.stop();
            jettyServer.destroy();
        }
    }
}
