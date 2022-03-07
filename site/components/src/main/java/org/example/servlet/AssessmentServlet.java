package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.example.service.NodeService;
import org.hippoecm.hst.site.HstServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jcr.*;
import javax.jcr.query.Query;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AssessmentServlet extends HttpServlet {
    private static Logger log = LoggerFactory.getLogger(AssessmentServlet.class);
    @Autowired
    private NodeService nodeService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Repository repository =
                HstServices.getComponentManager().getComponent(Repository.class.getName());
        Session session = null;
        PrintWriter out = res.getWriter();
        try {
            session =
                    repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
            String name = req.getParameter("name");

            // You can get the root node of the default workspace from the repository.
            Node rootNode = session.getRootNode();
            if (StringUtils.isBlank(name)) {
                out.println(nodeService.getChildNamesRecursively(rootNode.getNode("content/documents")));
            } else {
                Query query = session.getWorkspace().getQueryManager().
                        createQuery(getQueryForNodeName(name), Query.XPATH);
                out.println(new ObjectMapper().writeValueAsString(nodeService.getPrintableNodes(query.execute().getNodes())));
            }
        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);
            out.println("Technical error!");
        } finally {
            if(session != null) {
                session.logout();
            }
        }
    }

    private String getQueryForNodeName(String nodeName) {
        return "//*[jcr:contains(.,'" + nodeName + " ')]";
    }
}