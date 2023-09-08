package Clases;
import java.math.*;

public class Euro extends Moneda {

	private String nombre = "Euro";
	private String pais = "Unión Europea";
	private BigDecimal tasa_de_cambio_euro;
	private BigDecimal cotizacion_dolar;
	
	
	public Euro(String nombre, String pais, BigDecimal tasa_de_cambio_euro) {
		super(nombre, pais);
		this.tasa_de_cambio_euro= tasa_de_cambio_euro;
		
	}
	
	
	
	public BigDecimal getTasa_de_cambio_euro() {
		return tasa_de_cambio_euro;
	}



	public void setTasa_de_cambio_euro(BigDecimal tasa_de_cambio_euro) {
		this.tasa_de_cambio_euro = tasa_de_cambio_euro;
	}

	
	public BigDecimal getCotizacion_dolar() {
		return cotizacion_dolar;
	}



	public void setCotizacion_dolar(BigDecimal cotizacion_dolar) {
		this.cotizacion_dolar = cotizacion_dolar;
	}



//	protected void actualizacionAutomaticaDeTasaEuro(){
//		while(true) {
//			tasa_de_cambio_euro= getCotizacion_dolar()/this.cotizacion_euro;
//		}
//		
//	}
	
	
	
	
	@Override
    public String toString() {
        return super.toString() + ", Tasa de cambio: " + this.tasa_de_cambio_euro;
    }
	
	

}
