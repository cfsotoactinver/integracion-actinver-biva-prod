package mx.com.actinver.biva.procesadores;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import mx.com.actinver.biva.memoria.Indice;

public class ProcesadorDeIndice implements Processor {

	private Indice indice;

	@Override
	public void process(Exchange exchange) throws Exception {
		indice.buildIndexWithString(exchange.getIn().getBody(String.class));
		exchange.getIn().setBody(indice.getRegistros());
	}

	public Indice getIndice() {
	    return indice;
	}

	public void setIndice(Indice indice) {
	    this.indice = indice;
	}
	
}