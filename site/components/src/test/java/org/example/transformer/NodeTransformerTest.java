package org.example.transformer;

import org.example.dto.PrintableNode;
import org.example.dto.PrintableProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class NodeTransformerTest {
    private NodeTransformer underTest;
    private PropertyTransformer propertyTransformer;

    @BeforeEach
    void init() {
        propertyTransformer = Mockito.mock(PropertyTransformer.class);
        underTest = new NodeTransformer(propertyTransformer);
    }

    @Test
    void testApply_NullNode() {
        assertThat(underTest.apply(null)).isNull();
    }

    @Test
    void testApply_RepositoryException () throws RepositoryException {
        Node node = Mockito.mock(Node.class);
        Mockito.when(node.getProperties()).thenThrow(new RepositoryException());

        assertThat(underTest.apply(node)).isNull();
    }

    @Test
    void testApply_NodeWithNoProperty () throws RepositoryException {
        Node node = Mockito.mock(Node.class);
        Property property = Mockito.mock(Property.class);
        PropertyIterator propertyIterator = Mockito.mock(PropertyIterator.class);
        PrintableProperty printableProperty = new PrintableProperty("Test", new ArrayList<>());
        Mockito.when(node.getName()).thenReturn("TestNode");
        Mockito.when(node.getProperties()).thenReturn(propertyIterator);
        Mockito.when(propertyIterator.hasNext()).thenReturn(true)
                .thenReturn(true).thenReturn(false);
        Mockito.when(propertyIterator.nextProperty()).thenReturn(property).thenReturn(null);

        Mockito.when(propertyTransformer.apply(property)).thenReturn(printableProperty);

        PrintableNode result = underTest.apply(node);

        assertThat(result).hasFieldOrPropertyWithValue("name", "TestNode")
        .extracting("printableProperty").asList().hasSize(1).contains(printableProperty);

    }

}
