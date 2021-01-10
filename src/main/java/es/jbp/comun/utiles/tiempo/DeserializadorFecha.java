package es.jbp.comun.utiles.tiempo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.springframework.stereotype.Component;

/**
 *
 * @author jorge
 */
@Component
public class DeserializadorFecha extends JsonDeserializer<Fecha> {

    @Override
    public Fecha deserialize(JsonParser jp, DeserializationContext dc)
            throws IOException, JsonProcessingException {
        return new Fecha(dc.readValue(jp, String.class));
    }
}
