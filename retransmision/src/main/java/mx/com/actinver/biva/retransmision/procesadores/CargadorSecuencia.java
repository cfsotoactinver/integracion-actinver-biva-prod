package mx.com.actinver.biva.retransmision.procesadores;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import mx.com.actinver.biva.retransmision.modelo.SolicitudDeRetransmision;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CargadorSecuencia implements Processor {
	private HashMap<String, Long> mocSecuencia;
	Logger LOGGER = LoggerFactory.getLogger(CargadorSecuencia.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		
		String secuencia =exchange.getIn().getBody(String.class);
		if(secuencia.equals("cargar")){
			FileReader fr = new FileReader("/opt/rh/jbossfusedatos/Secuencias.txt");
			BufferedReader bf = new BufferedReader(fr);
			String sCadena;
			while ((sCadena = bf.readLine())!=null) {
				   System.out.println(sCadena);
				}
			Long secrecuperada=Long.parseLong(sCadena);
			mocSecuencia.put("Secuencia",secrecuperada);	
		}else{
			if(mocSecuencia.isEmpty()){
			mocSecuencia.put("Secuencia",exchange.getIn().getBody(Long.class));
			}else {
				mocSecuencia.remove("Secuencia");
				mocSecuencia.put("Secuencia",exchange.getIn().getBody(Long.class));
			}
		}
		 
	}

	public HashMap<String, Long> getMocSecuencia() {
		return mocSecuencia;
	}

	public void setMocSecuencia(HashMap<String, Long> mocSecuencia) {
		this.mocSecuencia = mocSecuencia;
	}

}
