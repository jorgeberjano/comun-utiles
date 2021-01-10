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
public class DeserializadorFechaHora extends JsonDeserializer<FechaHora> {

    @Override
    public FechaHora deserialize(JsonParser jp, DeserializationContext dc)
            throws IOException, JsonProcessingException {
        return new FechaHora(dc.readValue(jp, String.class));
    }
}
