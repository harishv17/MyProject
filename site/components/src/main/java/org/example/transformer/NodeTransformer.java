package org.example.transformer;

import org.example.dto.PrintableNode;
import org.example.dto.PrintableProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.jcr.Node;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class NodeTransformer implements Function<Node, PrintableNode> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeTransformer.class);
    private final PropertyTransformer propertyTransformer;

    public NodeTransformer(PropertyTransformer propertyTransformer) {
        this.propertyTransformer = propertyTransformer;
    }

    @Override
    public PrintableNode apply(Node node) {
        try {
            if (node == null) {
                return null;
            }
            PropertyIterator properties = node.getProperties();
            List<PrintableProperty> printableProperties = new ArrayList<>();
            while (properties.hasNext()) {
                Optional.ofNullable(properties.nextProperty()).map(propertyTransformer)
                        .ifPresent(printableProperties::add);
            }
            return new PrintableNode(node.getName(), printableProperties);
        } catch (RepositoryException e) {
            LOGGER.error("Error Transforming Node to PrintableNode", e);
            return null;
        }
    }
}
