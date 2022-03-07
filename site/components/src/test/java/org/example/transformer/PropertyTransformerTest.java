package org.example.transformer;

import org.apache.jackrabbit.value.StringValue;
import org.example.dto.PrintableProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyTransformerTest {
    private PropertyTransformer underTest;

    @BeforeEach
    public void setUp() {
        underTest = new PropertyTransformer();
    }

    @Test
    void testApply_WithNullProperty () {
        assertThat(underTest.apply(null)).isNull();
    }

    @Test
    void testApply_withSingleValueProperty () throws RepositoryException {
        Property singleValueProperty = Mockito.mock(Property.class);
        Mockito.when(singleValueProperty.isMultiple()).thenReturn(false);
        Mockito.when(singleValueProperty.getString()).thenReturn("TestValue");
        Mockito.when(singleValueProperty.getName()).thenReturn("TestName");

        PrintableProperty result = underTest.apply(singleValueProperty);

        assertThat(result).hasFieldOrPropertyWithValue("name", "TestName")
        .extracting("values").asList().hasSize(1).contains("TestValue");
    }

    @Test
    void testApply_withMultiValueProperty () throws RepositoryException {
        Property multiValueProperty = Mockito.mock(Property.class);
        Mockito.when(multiValueProperty.isMultiple()).thenReturn(true);
        Mockito.when(multiValueProperty.getValues())
                .thenReturn(new Value[] {new StringValue("Value1"), new StringValue("Value2")});
        Mockito.when(multiValueProperty.getName()).thenReturn("TestName1");

        PrintableProperty result = underTest.apply(multiValueProperty);

        assertThat(result).hasFieldOrPropertyWithValue("name", "TestName1")
                .extracting("values").asList().hasSize(2).contains("Value1", "Value2");

    }

    @Test
    void testApply_withRepositoryException () throws RepositoryException {
        Property multiValueProperty = Mockito.mock(Property.class);
        Mockito.when(multiValueProperty.isMultiple()).thenReturn(true);
        Mockito.when(multiValueProperty.getValues())
                .thenThrow(new RepositoryException());
        Mockito.when(multiValueProperty.getName()).thenReturn("TestName1");

        assertThat(underTest.apply(multiValueProperty)).isNull();
    }

}
