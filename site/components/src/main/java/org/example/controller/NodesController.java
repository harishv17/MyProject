package org.example.controller;

import org.apache.commons.lang3.StringUtils;
import org.example.dto.PrintableNode;
import org.example.service.NodeService;
import org.example.transformer.NodeTransformer;
import org.example.validator.UuidValidator;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("nodes")
public class NodesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodesController.class);
    private final NodeTransformer nodeTransformer;
    private final NodeService nodeService;

    public NodesController() {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/hst-assembly/overrides/beans.xml");
        this.nodeTransformer = context.getBean(NodeTransformer.class);
        this.nodeService = context.getBean(NodeService.class);
    }

    @GET
    @Path("{nodePath:.+}")
    public PrintableNode getNode(@Context HttpServletRequest servletRequest,
                                  @Context HttpServletResponse servletResponse,
                                  @PathParam("nodePath") String nodePath) {
        HstRequestContext requestContext = RequestContextProvider.get();
        try {
            if (UuidValidator.isUuid(nodePath)) {
                return nodeTransformer.apply(requestContext.getSession().getNodeByIdentifier(nodePath));
            } else {
                return nodeTransformer.apply(requestContext.getSession().getRootNode().getNode(PathUtils.normalizePath(nodePath)));
            }
        } catch (RepositoryException e) {
            LOGGER.error("Technical error", e);
            throw new InternalServerErrorException(e);
        }
    }

    @GET
    @Path("/")
    public List<String> listNodes(@QueryParam("query") String queryText) {
        HstRequestContext requestContext = RequestContextProvider.get();
        try {
            if (StringUtils.isNotBlank(queryText)) {
                Query query = requestContext.getSession().getWorkspace().getQueryManager().
                        createQuery(getQueryForNodeName(queryText), Query.XPATH);
                return Optional.of(query.execute().getNodes()).map(nodeService::getPrintableNodes)
                        .orElse(new ArrayList<>())
                        .stream().map(PrintableNode::getName).collect(Collectors.toList());
            }
            return nodeService.getChildNamesRecursively(requestContext.getSession().getRootNode().getNode("content/documents"));
        } catch (RepositoryException e) {
            LOGGER.error("Technical error", e);
            throw new InternalServerErrorException(e);
        }
    }

    private String getQueryForNodeName(String nodeName) {
        return "//*[jcr:contains(.,'" + nodeName + " ')]";
    }

}
