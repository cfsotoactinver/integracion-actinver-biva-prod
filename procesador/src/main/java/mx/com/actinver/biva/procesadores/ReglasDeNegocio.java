package mx.com.actinver.biva.procesadores;

import mx.com.biva.itch.modelo.*;

public interface ReglasDeNegocio {
	
	public Boolean procesaMensajeA(A mensajeA) throws Exception;
	public Boolean procesaMensajeD(D mensajeD) throws Exception;
	public Boolean procesaMensajeU(U mensajeU) throws Exception;
	public Boolean procesaMensajeE(E mensajeE) throws Exception;
	public Boolean procesaMensajeC(C mensajeC) throws Exception;
	public Boolean procesaMensajeP(P mensajeP) throws Exception;
	public Boolean procesaMensajeR(R mensajeR) throws Exception;
	public Boolean procesaMensajeH(H mensajeH) throws Exception;
 	public Boolean procesaMensajeL(L mensajeL) throws Exception;
	public Boolean procesaMensajeM(M mensajeM) throws Exception;
	public Boolean procesaMensajeX(X mensajeX) throws Exception;
	public Boolean limpiadorNotificaciones() throws Exception;
	
}