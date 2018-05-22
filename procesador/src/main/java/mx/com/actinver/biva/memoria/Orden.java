package mx.com.actinver.biva.memoria;

public class Orden {
	private String emisora;
	private String serie;
	private Double precio;
	private Integer carteraDePedidos;
	private Long numeroDeOrden;
	private String verboDeLaOrden;
	private Long cantidad;
	private String eliminaOrden;

	public String getEmisora() {
		return emisora;
	}

	public void setEmisora(String emisora) {
		this.emisora = emisora;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getCarteraDePedidos() {
		return carteraDePedidos;
	}

	public void setCarteraDePedidos(Integer carteraDePedidos) {
		this.carteraDePedidos = carteraDePedidos;
	}

	public Long getNumeroDeOrden() {
		return numeroDeOrden;
	}

	public void setNumeroDeOrden(Long numeroDeOrden) {
		this.numeroDeOrden = numeroDeOrden;
	}

	public String getVerboDeLaOrden() {
		return verboDeLaOrden;
	}

	public void setVerboDeLaOrden(String verboDeLaOrden) {
		this.verboDeLaOrden = verboDeLaOrden;
	}

	public Long getCantidad() {
		return cantidad;
	}

	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}

	public String getEliminaOrden() {
		return eliminaOrden;
	}

	public void setEliminaOrden(String eliminaOrden) {
		this.eliminaOrden = eliminaOrden;
	}
}
