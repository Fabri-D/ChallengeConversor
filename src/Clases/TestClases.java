package Clases;

import java.math.BigDecimal;

import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

/**
 * Clase TestClases contiene métodos para realizar conversiones de tasas de cambio entre diferentes monedas,
 * actualizar valores de tasa de cambio, obtener porcentajes de evolución, y realizar sorteos.
 */
public class TestClases {
	
	protected static BigDecimal tasa_previa_dolar= new BigDecimal ("0.91629");
	public static BigDecimal getTasa_previa_dolar() {
		return tasa_previa_dolar;
	}

	public static void setTasa_previa_dolar(BigDecimal tasa_previa_dolar) {
		TestClases.tasa_previa_dolar = tasa_previa_dolar;
	}

	public static BigDecimal getTasa_previa_euro() {
		return tasa_previa_euro;
	}

	public static void setTasa_previa_euro(BigDecimal tasa_previa_euro) {
		TestClases.tasa_previa_euro = tasa_previa_euro;
	}

	protected static BigDecimal tasa_previa_euro= new BigDecimal ("1.09082");
	
	/**
	 * Actualiza la tasa de cambio del euro en función de una nueva tasa de cambio del dólar.
	 *
	 * @param euro          Objeto Euro que contiene la tasa de cambio actual del euro.
	 * @param dolar         Objeto Dolar que contiene la tasa de cambio actual del dólar.
	 * @param nueva_tasa_dolar Nueva tasa de cambio del dólar.
	 * @return La nueva tasa de cambio del euro actualizada.
	 */
	public static BigDecimal actualizarTasaEuro(Euro euro, Dolar dolar, BigDecimal nueva_tasa_dolar) {
		BigDecimal res_multiplicacion=(euro.getTasa_de_cambio_euro().multiply(TestClases.tasa_previa_dolar));
		BigDecimal res_final= res_multiplicacion.divide(nueva_tasa_dolar, 3, RoundingMode.HALF_UP);
		euro.setTasa_de_cambio_euro(res_final);
		return euro.getTasa_de_cambio_euro();
	}
	
	/**
	 * Actualiza la tasa de cambio del dólar en función de una nueva tasa de cambio del euro.
	 *
	 * @param dolar          Objeto Dolar que contiene la tasa de cambio actual del dólar.
	 * @param euro           Objeto Euro que contiene la tasa de cambio actual del euro.
	 * @param nueva_tasa_euro Nueva tasa de cambio del euro.
	 * @return La nueva tasa de cambio del dólar actualizada.
	 */
	public static BigDecimal actualizarTasaDolar(Dolar dolar, Euro euro, BigDecimal nueva_tasa_euro) {

		BigDecimal res_multiplicacion=(dolar.getTasa_de_cambio_dolar().multiply(TestClases.tasa_previa_euro));
		BigDecimal res_final= res_multiplicacion.divide(nueva_tasa_euro, 3, RoundingMode.HALF_UP);
		dolar.setTasa_de_cambio_dolar(res_final);
		return dolar.getTasa_de_cambio_dolar();
	}
	
	/**
	 * Actualiza la tasa de cambio de una moneda diferente a dólar y euro en función de una nueva tasa de cambio del dólar.
	 *
	 * @param moneda         Objeto TodaOtraMoneda que contiene la tasa de cambio referida actual de la moneda.
	 * @param dolar          Objeto Dolar que contiene la tasa de cambio actual del dólar.
	 * @param nueva_tasa_dolar Nueva tasa de cambio del dólar.
	 * @return La nueva tasa de cambio referida de la moneda actualizada.
	 */
	public static BigDecimal actualizarTasaTodaOtraMoneda(TodaOtraMoneda moneda, Dolar dolar, BigDecimal nueva_tasa_dolar) {
		
		BigDecimal res_multiplicacion=(moneda.getTasa_de_cambio_referida().multiply(tasa_previa_dolar));
		BigDecimal res_final= res_multiplicacion.divide(dolar.getTasa_de_cambio_dolar(), 3, RoundingMode.HALF_UP);
		moneda.setTasa_de_cambio_referida(res_final);
		return moneda.getTasa_de_cambio_referida();
	}
	
	/**
	 * Actualiza la tasa de cambio referida de una lista de monedas diferentes a dólar y euro,
	 * en función de una nueva tasa de cambio del dólar y la tasa de cambio previa del dólar.
	 *
	 * @param listaTodaOtraMoneda Lista de objetos TodaOtraMoneda que contienen las tasas de cambio referidas actuales de las monedas.
	 * @param dolar               Objeto Dolar que contiene la tasa de cambio actual del dólar.
	 * @param nueva_tasa_dolar    Nueva tasa de cambio del dólar.
	 * @param tasa_previa_dolar   Tasa de cambio previa del dólar.
	 */
	public static void actualizarListaTodaOtraMoneda(ArrayList<TodaOtraMoneda> listaTodaOtraMoneda, Dolar dolar, BigDecimal nueva_tasa_dolar, BigDecimal tasa_previa_dolar) {
		// Recorre la lista de monedas diferentes a dólar y euro y actualiza sus tasas de cambio referidas.
		for (TodaOtraMoneda moneda : listaTodaOtraMoneda) {
			// Utiliza el método actualizarTasaTodaOtraMoneda para actualizar la tasa de cambio referida de cada moneda.
	        // Se le pasa como argumento el objeto moneda actual, la nueva tasa de cambio del dólar y la tasa de cambio previa del dólar.
			actualizarTasaTodaOtraMoneda(moneda, dolar, nueva_tasa_dolar);
		}	
	}
	
	/**
	 * Obtiene una lista de cadenas de texto que representan las tasas de cambio referidas actuales de una lista de monedas diferentes a dólar y euro.
	 *
	 * @param listaTodaOtraMoneda Lista de objetos TodaOtraMoneda que contienen las tasas de cambio referidas actuales de las monedas.
	 * @return Lista de cadenas de texto que representan las tasas de cambio referidas actuales de las monedas.
	 */
	public static ArrayList<String> listaTasasTodaOtraMoneda(ArrayList<TodaOtraMoneda> listaTodaOtraMoneda) {
		ArrayList<String> listaTasasTodaOtra= new ArrayList<>();
		for (TodaOtraMoneda moneda : listaTodaOtraMoneda) {
			BigDecimal tasa_referida=moneda.getTasa_de_cambio_referida().setScale(5);
			listaTasasTodaOtra.add(tasa_referida.toString());
		}
		return listaTasasTodaOtra;		
	}
	
	/**
	 * Convierte un monto dado en una moneda diferente al dólar y el euro a su equivalente en dólares.
	 *
	 * @param monto_TodaOtraMoneda Monto a convertir en la moneda de origen (diferente a dólar).
	 * @param monedaOrigen Objeto TodaOtraMoneda que representa la moneda de origen con su tasa de cambio referida actual.
	 * @return Monto convertido a dólares.
	 */
	public static BigDecimal conversionGenericaADolares(BigDecimal monto_TodaOtraMoneda, TodaOtraMoneda monedaOrigen) {
		BigDecimal tasaTodaOtraMoneda= monedaOrigen.getTasa_de_cambio_referida();
		BigDecimal monto_convertido_dolares=monto_TodaOtraMoneda.multiply(tasaTodaOtraMoneda);
		return monto_convertido_dolares;
	}
	
	/**
	 * Convierte un monto dado en dólares a su equivalente en euros.
	 *
	 * @param monto_dolar Monto en dólares a convertir.
	 * @param dolar Objeto Dolar que representa la tasa de cambio actual del dólar.
	 * @return Monto convertido a euros.
	 */
	public static BigDecimal conversionDolarAEuro(BigDecimal monto_dolar, Dolar dolar) {
		BigDecimal monto_convertido_euro=monto_dolar.multiply(dolar.getTasa_de_cambio_dolar());
		return monto_convertido_euro;
	}
	
	/**
	 * Convierte un monto dado en euros a su equivalente en dólares.
	 *
	 * @param monto_euro Monto en euros a convertir.
	 * @param euro Objeto Euro que representa la tasa de cambio actual del euro.
	 * @return Monto convertido a dólares.
	 */
	public static BigDecimal conversionEuroADolar(BigDecimal monto_euro, Euro euro) {
		BigDecimal monto_convertido_dolar=monto_euro.multiply(euro.getTasa_de_cambio_euro());
		return monto_convertido_dolar;
	}
	
	/**
	 * Convierte un monto dado en dólares a su equivalente en una moneda diferente al euro y al dólar.
	 *
	 * @param monedaDestino Objeto TodaOtraMoneda que representa la tasa de cambio referida de la moneda destino.
	 * @param monto_dolar Monto en dólares a convertir.
	 * @return Monto convertido a la moneda destino.
	 */
	public static BigDecimal conversionDolarAGenerico(TodaOtraMoneda monedaDestino, BigDecimal monto_dolar) {
		BigDecimal monto_convertido_todaOtraMoneda=monto_dolar.divide(monedaDestino.getTasa_de_cambio_referida(), 3, RoundingMode.HALF_UP);
		return monto_convertido_todaOtraMoneda;
	}
	
	/**
	 * Convierte un monto de una moneda de origen (diferente al euro y al dólar) a su equivalente
	 * en una moneda de destino (también diferente al euro y al dólar).
	 *
	 * @param monedaOrigen Objeto TodaOtraMoneda que representa la tasa de cambio referida de la moneda de origen.
	 * @param monedaDestino Objeto TodaOtraMoneda que representa la tasa de cambio referida de la moneda de destino.
	 * @param montoTodaOtraMoneda Monto en la moneda de origen a convertir.
	 * @return Monto convertido a la moneda de destino.
	 */
	public static BigDecimal conversionGenerica(TodaOtraMoneda monedaOrigen, TodaOtraMoneda monedaDestino, BigDecimal montoTodaOtraMoneda) {
		BigDecimal monto_dolar= conversionGenericaADolares(montoTodaOtraMoneda, monedaOrigen);
		BigDecimal monto_convertido= conversionDolarAGenerico(monedaDestino, monto_dolar);
		return monto_convertido;
	}
	
	/**
	 * Convierte un monto de una moneda de origen (diferente al euro y al dólar) a su equivalente
	 * en Euros.
	 *
	 * @param monto_TodaOtraMoneda Monto en la moneda de origen a convertir.
	 * @param monedaOrigen Objeto TodaOtraMoneda que representa la tasa de cambio referida de la moneda de origen.
	 * @param dolar Objeto Dolar que representa la tasa de cambio del Dólar con respecto a Euros.
	 * @return Monto convertido a Euros.
	 */
	public static BigDecimal conversionGenericaAEuros(BigDecimal monto_TodaOtraMoneda, TodaOtraMoneda monedaOrigen, Dolar dolar) {
		BigDecimal monto_dolar= conversionGenericaADolares(monto_TodaOtraMoneda, monedaOrigen);
		BigDecimal monto_euros= conversionDolarAEuro(monto_dolar, dolar);
		return monto_euros;
	}
	
	/**
	 * Obtiene la hora actual del sistema en formato de cadena de caracteres (HH:mm:ss).
	 *
	 * @return Hora actual del sistema en formato de cadena de caracteres (HH:mm:ss).
	 */
	public static String horadesistema() {
		LocalDateTime now = LocalDateTime.now();
	    int hour = now.getHour();
	    int minute = now.getMinute();
        int second = now.getSecond();
        String hora=(hour+":"+minute+":"+second).toString();
	    return hora;
	}
	
	/**
	 * Obtiene el último ID registrado en la tabla "evolucion_moneda" de la base de datos.
	 *
	 * @return Último ID registrado en la tabla "evolucion_moneda" o 0 si no hay registros.
	 */
	public static int ultimo_id() {
		String url = "jdbc:mysql://localhost:3306/moneda";
	    String usuario = "root";
	    String contrasenia = "goldmountain";
		ConexionSQL database = new ConexionSQL(url, usuario, contrasenia);
		String consultaUltimoId = "Select MAX(id) from evolucion_moneda";
	    ResultSet porcentaje_evolucion = (database.ejecutarConsulta(consultaUltimoId));
	    try {
			while (porcentaje_evolucion.next()) {    
				int ultimoId = porcentaje_evolucion.getInt(1);
				return ultimoId;
				} 
	    } catch (SQLException e) {
		e.printStackTrace();
		} finally {
			database.cerrarConexion();
		}
	    return 0;
	}
	
	/**
	 * Obtiene el valor anterior de una moneda específica en la tabla "evolucion_moneda"
	 * de la base de datos.
	 *
	 * @param monedaQuery El nombre de la columna que representa la moneda de interés
	 *                    en la tabla "evolucion_moneda". Debe ser uno de los nombres
	 *                    de las columnas válidas, por ejemplo, "dolar", "euro", etc.
	 * @return BigDecimal El valor anterior de la moneda en formato BigDecimal. Si ocurre
	 *                    algún error o no se encuentra el valor anterior, devuelve un
	 *                    BigDecimal con valor 0.0.
	 */
	public static BigDecimal valorAnterior(String monedaQuery) {
		String url = "jdbc:mysql://localhost:3306/moneda";
	    String usuario = "root";
	    String contrasenia = "goldmountain";
		ConexionSQL database = new ConexionSQL(url, usuario, contrasenia);
		String lastId = String.valueOf(ultimo_id());
	    String StrValorAnterior = "Select " + monedaQuery + " from evolucion_moneda where id=" + lastId + ";";
	    ResultSet consultaValorAnterior = database.ejecutarConsulta(StrValorAnterior);
	    
	    try {
			while (consultaValorAnterior.next()) {    
				BigDecimal valorAnterior = new BigDecimal(consultaValorAnterior.getDouble(monedaQuery));
				//System.out.println("ultimo id: " + ultimoId);
				return valorAnterior;
				}
	    } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} finally {
	    	database.cerrarConexion();
	    }
	    return new BigDecimal("0.0");
	}
	
	/**
	 * Calcula el porcentaje de evolución de una moneda específica en relación con su valor anterior.
	 *
	 * @param monedaQuery      El nombre de la moneda para la cual se calculará el porcentaje
	 *                         de evolución. Debe ser uno de los nombres válidos de monedas,
	 *                         por ejemplo, "dolar", "euro", "peso_argentino", etc.
	 * @param dolar            Objeto de la clase Dolar que contiene la tasa de cambio del dólar.
	 * @param euro             Objeto de la clase Euro que contiene la tasa de cambio del euro.
	 * @param listaTodaOtraMoneda  ArrayList que contiene objetos de la clase TodaOtraMoneda,
	 *                         cada uno con su respectiva tasa de cambio referida.
	 * @return BigDecimal      El porcentaje de evolución de la moneda, en formato BigDecimal.
	 *                         Si ocurre algún error o no se encuentra el valor anterior,
	 *                         devuelve un BigDecimal con valor 0.0.
	 */
	public static BigDecimal porcentajeEvolucion(String monedaQuery, Dolar dolar, Euro euro, ArrayList<TodaOtraMoneda> listaTodaOtraMoneda) {
		ArrayList<String> listaTasasSql = new ArrayList<>();
	    listaTasasSql.addAll(List.of("peso_argentino","yen","reales","peso_mexicano","peso_chileno","peso_uruguayo","boliviano","rupia"));
		int indiceTodaOtra = 0;
	    if (listaTasasSql.indexOf(monedaQuery)!=-1) {
			indiceTodaOtra= listaTasasSql.indexOf(monedaQuery);
		} else {
			indiceTodaOtra= 0;
		}
	    
		BigDecimal nuevo_valor = new BigDecimal("0.0");
		if (monedaQuery == "dolar") {
			nuevo_valor = new BigDecimal(dolar.getTasa_de_cambio_dolar().toString());
		} else if (monedaQuery=="euro") {
			nuevo_valor = new BigDecimal(euro.getTasa_de_cambio_euro().toString());
		} else {
			nuevo_valor = new BigDecimal(listaTodaOtraMoneda.get(indiceTodaOtra).getTasa_de_cambio_referida().toString());
		}
		BigDecimal valor_anterior = valorAnterior(monedaQuery);
		
		//moneda.Porcentaje de cambio = ((moneda.Nuevo valor - moneda.Valor anterior) / moneda.Valor anterior) * 100
		BigDecimal porcentaje_evo = ((nuevo_valor.subtract(valor_anterior)).divide(valor_anterior, 3, RoundingMode.HALF_UP)).multiply(new BigDecimal("100.0"));
		
		return porcentaje_evo;
	}
	
	/**
	 * Realiza un sorteo booleano aleatorio y devuelve el resultado como un valor booleano.
	 *
	 * @return boolean  Resultado del sorteo.
	 */
	public static boolean sorteoBooleano() {
		Random random = new Random();
		int randomico = random.nextInt(2);
		if (randomico==0) {
			return true;
		} else {
			return false;
		}
	}
	
//	public static double randomInRange(double min, double max) {
//	    Random random = new Random();
//	    return min + (max - min) * random.nextDouble();
//	}

}
