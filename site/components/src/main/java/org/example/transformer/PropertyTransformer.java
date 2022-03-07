package org.example.transformer;

import org.apache.commons.lang3.StringUtils;
import org.example.dto.PrintableProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class PropertyTransformer implements Function<Property, PrintableProperty> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyTransformer.class);

    @Override
    public PrintableProperty apply(Property property) {
        List<String> values = new ArrayList<>();
        try {
            if (property == null) {
                return null;
            } else if (property.isMultiple()) {
                for (Value value : property.getValues()) {
                    values.add(value.getString().trim());
                }
            } else {
                values.add(StringUtils.trim(property.getString()));
            }
            return new PrintableProperty(property.getName(), values);
        } catch (RepositoryException e) {
            LOGGER.error("Error Transforming Property to PrintableProperty", e);
            return null;
        }
    }
}
