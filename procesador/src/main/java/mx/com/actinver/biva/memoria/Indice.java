package mx.com.actinver.biva.memoria;import java.io.IOException;import java.io.StringWriter;import java.util.ArrayList;import java.util.Arrays;import java.util.List;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import com.fasterxml.jackson.core.JsonGenerationException;import com.fasterxml.jackson.core.JsonParseException;import com.fasterxml.jackson.databind.JsonMappingException;import com.fasterxml.jackson.databind.ObjectMapper;public class Indice {		private List<RegistroGrid> registros;	private ObjectMapper objectMapper = new ObjectMapper();	private StringWriter stringWriter;	private static final Logger LOGGER = LoggerFactory.getLogger(Indice.class);		public void inicializar() {		this.registros = new ArrayList<RegistroGrid>();	}	public List<RegistroGrid> getRegistros() {		return registros;	}	public void setRegistros(List<RegistroGrid> registros) {		this.registros = registros;	}		public void buildIndexWithString(String indice) throws JsonParseException, JsonMappingException, IOException {		if(indice == null) {			LOGGER.info("El indice esta vacio");		} else {			List<String> items = Arrays.asList(indice.split("#"));			for(String item : items) {				this.registros.add(this.objectMapper.readValue(item, RegistroGrid.class));			}		}	}		public String getIndexAsString() throws JsonGenerationException, JsonMappingException, IOException {		StringBuilder stringBuilder = new StringBuilder();		for(RegistroGrid registro : registros) {			stringWriter = new StringWriter();			objectMapper.writeValue(stringWriter, registro);			stringBuilder.append(stringWriter.toString());			stringBuilder.append("#");		}		stringBuilder.append("end");		return stringBuilder.toString().replace("#end", "");	}}