package es.jbp.comun.utiles.tiempo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.stereotype.Component;

/**
 *
 * @author jorge
 */
@Component
public class SerializadorFecha extends JsonSerializer<Fecha> {

    @Override
    public void serialize(Fecha value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value.toString());
    }
}
