package org.example.listener;

import org.hippoecm.hst.site.HstServices;
import org.hippoecm.repository.util.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.observation.Event;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyServletContextListener.class);
    private Session session;
    private MyRepositoryEventListener myRepositoryEventListener;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            LOGGER.info(MyServletContextListener.class+": init");
            myRepositoryEventListener = new MyRepositoryEventListener();
            Repository repository =
                    HstServices.getComponentManager().getComponent(Repository.class.getName());
            session =
                    repository.login(new SimpleCredentials("admin", "admin".toCharArray()));

            int eventTypes = Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED | Event.PROPERTY_REMOVED
                    | Event.NODE_ADDED | Event.NODE_REMOVED | Event.NODE_MOVED | Event.PERSIST;

            session.getWorkspace().getObservationManager()
                    .addEventListener(myRepositoryEventListener, eventTypes, "/content/", true,
                            null, null, true);

        } catch (Exception e) {
            LOGGER.error("Error in MyServletContextListener initialization", e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            LOGGER.info(MyServletContextListener.class+": destroy");
            session.getWorkspace().getObservationManager().removeEventListener(myRepositoryEventListener);
            session.logout();
        } catch (RepositoryException e) {
            LOGGER.error("Error in MyServletContextListener destroy", e);
        }

    }
}
