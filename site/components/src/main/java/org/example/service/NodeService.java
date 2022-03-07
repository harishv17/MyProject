package org.example.service;

import org.example.dto.PrintableNode;
import org.example.transformer.NodeTransformer;
import org.springframework.stereotype.Service;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NodeService {

    private final NodeTransformer nodeTransformer;

    public NodeService(NodeTransformer nodeTransformer) {
        this.nodeTransformer = nodeTransformer;
    }

    public List<String> getChildNamesRecursively(Node node) throws RepositoryException {
        List<String> names = new ArrayList<>();
        if (node != null && !node.isNodeType("hippofacnav:facetnavigation")) {
            NodeIterator nodeIterator = node.getNodes();
            while (nodeIterator.hasNext()) {
                Node childNode = nodeIterator.nextNode();
                names.add(childNode.getName());
                names.addAll(getChildNamesRecursively(childNode));
            }
        }
        return names;
    }

    public List<PrintableNode> getPrintableNodes(NodeIterator nodeIterator) {
        List<PrintableNode> printableNodes = new ArrayList<>();
        if (nodeIterator != null) {
            while (nodeIterator.hasNext()) {
                Optional.ofNullable(nodeIterator.nextNode()).map(nodeTransformer)
                        .ifPresent(printableNodes::add);
            }
        }
        return printableNodes;
    }
}
