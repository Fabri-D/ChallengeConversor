package Clases;
import java.math.*;

public class Dolar extends Moneda {
	
	private String nombre = "Dólar";
	private String pais = "USA";
	private BigDecimal tasa_de_cambio_dolar;
	
	private BigDecimal cotizacion_euro;
	
	
	public Dolar(String nombre, String pais, BigDecimal tasa_de_cambio_dolar) {
		super(nombre, pais);
		this.tasa_de_cambio_dolar= tasa_de_cambio_dolar;
		
	}
	

	public BigDecimal getTasa_de_cambio_dolar() {
		return tasa_de_cambio_dolar;
	}



	public void setTasa_de_cambio_dolar(BigDecimal tasa_de_cambio_dolar) {
		this.tasa_de_cambio_dolar = tasa_de_cambio_dolar;
	}



	

	

	public BigDecimal getCotizacion_euro() {
		return cotizacion_euro;
	}



	public void setCotizacion_euro(BigDecimal cotizacion_euro) {
		this.cotizacion_euro = cotizacion_euro;
	}



//	protected void actualizacionAutomaticaDeTasaDolar(double cotizacion_euro){
//		while(true) {
//			tasa_de_cambio_dolar= cotizacion_euro/this.cotizacion_dolar;
//		}
//		
//	}
	
	
	
	
	
	@Override
    public String toString() {
        return super.toString() + ", Tasa de cambio: " + this.tasa_de_cambio_dolar;
    }
	
	
	
}
