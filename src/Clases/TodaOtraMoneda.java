package Clases;
import java.math.*;
import java.util.ArrayList;

public class TodaOtraMoneda extends Moneda {
	
	private String nombre;
	private String pais;
	private BigDecimal tasa_de_cambio_referida;

	public TodaOtraMoneda(String nombre, String pais, BigDecimal tasa_de_cambio_referida) {
		super(nombre, pais);
		this.tasa_de_cambio_referida=tasa_de_cambio_referida;
		
	}

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

	public BigDecimal getTasa_de_cambio_referida() {
		return tasa_de_cambio_referida;
	}

	public void setTasa_de_cambio_referida(BigDecimal tasa_de_cambio_referida) {
		this.tasa_de_cambio_referida = tasa_de_cambio_referida;
	}
	
	public BigDecimal conversionADolar(BigDecimal montoTodaOtraMoneda) {
		BigDecimal monto_convertido_dolar=montoTodaOtraMoneda.multiply(this.tasa_de_cambio_referida);
		return monto_convertido_dolar;
	}
	
	
	
	
	
	@Override
    public String toString() {
        return super.toString() + ", Tasa de cambio: " + this.tasa_de_cambio_referida;
    }

}
