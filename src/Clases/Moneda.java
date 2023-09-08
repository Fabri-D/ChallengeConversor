package Clases;

public abstract class Moneda {
	
	private String nombre;
	private String pais;
	
	public Moneda(String nombre, String pais) {
		this.nombre=nombre;
		this.pais=pais;
	}
	
//	protected void actualizarTasaDeCambio (Double tasa_de_cambio) {
//		this.tasa_de_cambio= tasa_de_cambio;
//		// Almacenar registro
//	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	
	@Override
    public String toString() {
        return "Nombre: " + this.nombre + ", país: " + this.pais;
    }
//	public double getTasa_de_cambio() {
//		return tasa_de_cambio;
//	}
//	public void setTasa_de_cambio(double tasa_de_cambio) {
//		this.tasa_de_cambio = tasa_de_cambio;
//	}
	
	
}
