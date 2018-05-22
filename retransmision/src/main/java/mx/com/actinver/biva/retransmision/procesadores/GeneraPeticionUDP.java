package mx.com.actinver.biva.retransmision.procesadores;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import mx.com.actinver.biva.retransmision.modelo.SolicitudDeRetransmision;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneraPeticionUDP implements Processor {
	@Produce(uri = "direct:procesaRetransmision")
	private ProducerTemplate retransmision;
	@Produce(uri = "direct:generapeticion")
	private ProducerTemplate retransmision2;
	private String ipFeedA;
	private int puertoFeedA;
	private String ipFeedB;
	private int puertoFeedB;
	Logger LOGGER = LoggerFactory.getLogger(GeneraPeticionUDP.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		SolicitudDeRetransmision peticion = exchange.getIn().getBody(
				SolicitudDeRetransmision.class);
		Long fin = peticion.getNumeroDeSecuenciaFinal();
		Long inicio = peticion.getNumeroDeSecuenciaInicial();

		SolicitudDeRetransmision petDeRetransmision = new SolicitudDeRetransmision();
		petDeRetransmision.setSession(peticion.getSession());
		petDeRetransmision.setNumeroDeSecuenciaInicial(inicio);
		petDeRetransmision.setNumeroDeSecuenciaFinal(fin);
		solicitaMensajes(petDeRetransmision);

	}

	public void solicitaMensajes(SolicitudDeRetransmision peticion)
			throws Exception {
		try {
			LOGGER.info("IP: " + ipFeedA + " Port: " + puertoFeedA
					+ " Message: " + peticion.toByteArray() + " Longitud: "
					+ peticion.toByteArray().length);
			byte[] m = peticion.toByteArray();
			DatagramSocket socketUDP = new DatagramSocket();
			socketUDP.setSoTimeout(9000);
			InetAddress host = java.net.InetAddress.getByName(ipFeedA);
			DatagramPacket request = new DatagramPacket(m, m.length, host,
					puertoFeedA);
			socketUDP.send(request);
			byte[] bufer = new byte[1500];
			DatagramPacket respuesta = new DatagramPacket(bufer, 1500);
			LOGGER.info("Esperando Respuesta");
			socketUDP.receive(respuesta);
			byte[] bufer2 = respuesta.getData();
			retransmision.sendBody(bufer2);
			socketUDP.close();
		} catch (Exception e) {
			try {
				LOGGER.info("IP: " + ipFeedB + " Port: " + puertoFeedB
						+ " Message: " + peticion.toByteArray() + " Longitud: "
						+ peticion.toByteArray().length);
				byte[] m = peticion.toByteArray();
				DatagramSocket socketUDP = new DatagramSocket();
				InetAddress host = java.net.InetAddress.getByName(ipFeedB);
				DatagramPacket request = new DatagramPacket(m, m.length, host,
						puertoFeedB);
				socketUDP.send(request);
				socketUDP.setSoTimeout(9000);
				byte[] bufer = new byte[1500];
				DatagramPacket respuesta = new DatagramPacket(bufer, 1500);
				LOGGER.info("Esperando Respuesta");
				socketUDP.receive(respuesta);
				byte[] bufer2 = respuesta.getData();
				retransmision.sendBody(bufer2);
				socketUDP.close();
			} catch (Exception e1) {
				LOGGER.error("Se produjo un error Reintentando"
						+ e1.getMessage());
				retransmision2.sendBody(peticion);
			}
		}

	}

	public String getIpFeedA() {
		return ipFeedA;
	}

	public void setIpFeedA(String ipFeedA) {
		this.ipFeedA = ipFeedA;
	}

	public int getPuertoFeedA() {
		return puertoFeedA;
	}

	public void setPuertoFeedA(int puertoFeedA) {
		this.puertoFeedA = puertoFeedA;
	}

	public String getIpFeedB() {
		return ipFeedB;
	}

	public void setIpFeedB(String ipFeedB) {
		this.ipFeedB = ipFeedB;
	}

	public int getPuertoFeedB() {
		return puertoFeedB;
	}

	public void setPuertoFeedB(int puertoFeedB) {
		this.puertoFeedB = puertoFeedB;
	}

}
