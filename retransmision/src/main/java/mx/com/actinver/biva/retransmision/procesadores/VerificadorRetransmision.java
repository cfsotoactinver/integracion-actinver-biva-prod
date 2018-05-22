package mx.com.actinver.biva.retransmision.procesadores;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import mx.com.actinver.biva.retransmision.modelo.SolicitudDeRetransmision;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerificadorRetransmision implements Processor {
	@Produce(uri = "direct:generapeticion")
	private ProducerTemplate retransmision2;
	private HashMap<String, Long> mocSecuencia;
	Logger LOGGER = LoggerFactory.getLogger(VerificadorRetransmision.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		String mensage = exchange.getIn().getBody(String.class);
		JSONObject objfinders = new JSONObject(mensage);
		Long secuencia = objfinders.getLong("secuencia");
		String session = objfinders.getString("session");

		if (mocSecuencia.get("Secuencia") == null) {
			LOGGER.info("Cargando del Archivo");	
			try {
				FileReader fr = new FileReader(
						"/opt/rh/jbossfusedatos/Secuencias.txt");
				BufferedReader bf = new BufferedReader(fr);
				String sCadena="";
					sCadena = bf.readLine();
				
				Long secrecuperada = Long.parseLong(sCadena);
				mocSecuencia.put("Secuencia", secrecuperada);
			} catch (Exception e) {
				mocSecuencia.put("Secuencia", secuencia);
			}
		}
		LOGGER.info("SECUENCIA ANTERIOR: " + mocSecuencia.get("Secuencia")
				+ " SECUENCIA ACTUAL: " + secuencia);
		Long inicio = mocSecuencia.get("Secuencia") + 1l;
		Long fin = secuencia;
		Long diferencia = fin - inicio;

		if (secuencia - 1l == mocSecuencia.get("Secuencia")
				&& secuencia > mocSecuencia.get("Secuencia")) {
			LOGGER.debug("todo a tiempo");
		} else if (secuencia - 1l != mocSecuencia.get("Secuencia")
				&& secuencia > mocSecuencia.get("Secuencia")) {
		

			LOGGER.debug("Mensajes A pedir" + diferencia);
			SolicitudDeRetransmision retransmision = new SolicitudDeRetransmision();
			if (diferencia <= 10l) {
				retransmision.setSession(session);
				retransmision.setNumeroDeSecuenciaInicial(inicio);
				retransmision.setNumeroDeSecuenciaFinal(diferencia);
				retransmision2.sendBody(retransmision);
			} else {

				for (Long j = 10l; j >= 1l; j = j - 1l) {
					if (diferencia % j == 0) {
						for (Long i = inicio; i <= fin; i = i + j) {
							retransmision.setSession(session);
							retransmision.setNumeroDeSecuenciaInicial(i);
							retransmision.setNumeroDeSecuenciaFinal(j);
							retransmision2.sendBody(retransmision);
						}
						j=0l;
					}
				}

			}

		}
		try {
			mocSecuencia.replace("Secuencia", secuencia);
		} catch (Exception e) {
			mocSecuencia.put("Secuencia", secuencia);
		}

		exchange.getOut().setBody(secuencia.toString());

	}

	public HashMap<String, Long> getMocSecuencia() {
		return mocSecuencia;
	}

	public void setMocSecuencia(HashMap<String, Long> mocSecuencia) {
		this.mocSecuencia = mocSecuencia;
	}

}
