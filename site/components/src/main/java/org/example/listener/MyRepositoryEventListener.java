package org.example.listener;

import org.example.servlet.AssessmentServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import java.util.HashMap;
import java.util.Optional;

public class MyRepositoryEventListener implements EventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssessmentServlet.class);
    @Override
    public void onEvent(EventIterator eventIterator) {
        try {
            if (eventIterator==null) {
                LOGGER.error("Error reading events");
                return;
            }
            while (eventIterator.hasNext()) {
                Event event = eventIterator.nextEvent();
                LOGGER.info(event.getIdentifier());
                LOGGER.info(event.getPath());
                LOGGER.info(event.getUserData());
                LOGGER.info(event.getUserID());
                LOGGER.info(String.valueOf(event.getDate()));
                LOGGER.info(String.valueOf(event.getType()));
                Optional.ofNullable(event.getInfo()).orElse(new HashMap())
                        .forEach((k,v) -> LOGGER.info(((String) k) + ":" + ((String)v)));
            }
        } catch (RepositoryException e) {
            LOGGER.error("Error reading events", e);
        }
    }
}
