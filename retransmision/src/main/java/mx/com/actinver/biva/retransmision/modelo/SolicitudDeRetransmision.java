package mx.com.actinver.biva.retransmision.modelo;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Solicitud")
public class SolicitudDeRetransmision{
	
	@XmlElement
	private String session;
	@XmlElement
	private Long numeroDeSecuenciaInicial;
	@XmlElement
	private Long numeroDeSecuenciaFinal;

	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(20);
		buffer.put(this.session.getBytes(StandardCharsets.UTF_8));
	    buffer.putLong(this.numeroDeSecuenciaInicial.longValue());
	    Long requestMessageCount=numeroDeSecuenciaFinal-numeroDeSecuenciaInicial;
	    buffer.putShort(requestMessageCount.shortValue());
	    return buffer.array();
	}
	
	
	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}


	public Long getNumeroDeSecuenciaInicial() {
		return numeroDeSecuenciaInicial;
	}


	public void setNumeroDeSecuenciaInicial(Long numeroDeSecuenciaInicial) {
		this.numeroDeSecuenciaInicial = numeroDeSecuenciaInicial;
	}


	public Long getNumeroDeSecuenciaFinal() {
		return numeroDeSecuenciaFinal;
	}


	public void setNumeroDeSecuenciaFinal(Long numeroDeSecuenciaFinal) {
		this.numeroDeSecuenciaFinal = numeroDeSecuenciaFinal;
	}

	

}