package Clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.Timestamp;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.ApplicationFrame;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import javax.crypto.spec.OAEPParameterSpec;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.Color;
import java.awt.HeadlessException;
import java.math.*;

/**
 * Clase Graficos que contiene métodos para generar gráficos y tablas relacionados con la evolución de monedas.
 */
public class Graficos {
	
	/**
	 * Genera un gráfico de líneas con la evolución de una moneda entre dos horas especificadas.
	 *
	 * @param monedaQuery   El nombre de la moneda para la cual se generará el gráfico.
	 * @param horaInicio    La hora de inicio para la consulta de la evolución de la moneda (en formato "HH:mm:ss").
	 * @param horaFin       La hora de fin para la consulta de la evolución de la moneda (en formato "HH:mm:ss").
	 * @return Un objeto ChartPanel que contiene el gráfico de líneas generado.
	 * @throws SQLException Si ocurre un error durante la consulta a la base de datos.
	 */
	public ChartPanel generarGraficolineas(String monedaQuery, String horaInicio, String horaFin) throws SQLException {
		String url = "jdbc:mysql://localhost:3306/moneda";
	    String usuario = "root";
	    String contrasenia = "123456";
		ConexionSQL database = new ConexionSQL(url, usuario, contrasenia);
		
		ArrayList<Object> parametros = new ArrayList<>();
		parametros.add(horaInicio);
		parametros.add(horaFin);
		ResultSet consulta = database.ejecutarConsultaPreparada("SELECT hora, " + monedaQuery + " FROM evolucion_moneda where hora>=? and hora<=?", parametros);
		
		try {
			if (consulta.next()) {
				
			} else {
				JOptionPane.showMessageDialog(null, "No hay registros disponibles para la hora especificada.");
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		XYSeries series = new XYSeries("Evolución");
        
        Timestamp primerRegistro = null;
        Timestamp ultimoRegistro = null;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        DateAxis domainAxis = new DateAxis("Hora");
        domainAxis.setDateFormatOverride(dateFormat);
        
        while (consulta.next()) {
            try {
                String horaString = consulta.getString("hora");
                double valor = consulta.getDouble(monedaQuery);

                if (!consulta.wasNull()) {
                    java.util.Date hora = dateFormat.parse(horaString);
                    Timestamp timestamp = new Timestamp(hora.getTime());
                    
                    if (primerRegistro == null) {
                        primerRegistro = timestamp;
                    }
                    
                    ultimoRegistro = timestamp;
                    
                    series.add(hora.getTime(), valor);
                    }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } 
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        
        ArrayList<String> divisas2 = new ArrayList<>();
        divisas2.addAll(List.of("dolar","euro","peso_argentino","yen","reales","peso_mexicano","peso_chileno","peso_uruguayo","boliviano","rupia"));
        int indiceDiv = divisas2.indexOf(monedaQuery);
        ArrayList<String> divisas3 = new ArrayList<>();
    	divisas3.addAll(List.of("Dólar", "Euro", "Peso Argentino", "Yen", "Reales", "Peso Mexicano", "Peso Chileno", "Peso Uruguayo", "Boliviano", "Rupia"));
        String nombreDiv = divisas3.get(indiceDiv);
        String nombreDivFinal= "Grafico de " + nombreDiv;
        
     // Crear el gráfico de líneas
        JFreeChart chart = ChartFactory.createXYLineChart(
                nombreDivFinal, // Título del gráfico
                "Hora", // Etiqueta del eje x
                "Valor de cambio", // Etiqueta del eje y
                dataset // Dataset de los datos
        );
        
        domainAxis.setRange(primerRegistro, ultimoRegistro);
   
        XYPlot plot = chart.getXYPlot();
        plot.setDomainAxis(domainAxis);
        plot.getDomainAxis().setRange(primerRegistro.getTime(), ultimoRegistro.getTime());
        
        Color colorPastelFondo = new Color(223, 210, 128);
        chart.setBackgroundPaint(colorPastelFondo);
        chart.setBorderPaint(Color.BLACK);
        
        // Configurar el renderizador para mostrar solo una línea sin puntos
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        return chartPanel;
        
	}
	
	/**
	 * Genera una tabla a partir de un ResultSet con los datos de evolución o conversión de monedas.
	 *
	 * @param resultSet El conjunto de resultados (ResultSet) que contiene los datos para la tabla.
	 * @param tipo      El tipo de datos en el ResultSet ("evolucion" o "conversion").
	 * @return Un objeto JTable que representa la tabla generada con los datos del ResultSet.
	 */
	public JTable generarTabla(ResultSet resultSet, String tipo) {
	    String[] nombresColumnas;
	    
	    JTable jTable= new JTable();
	    ArrayList<Object[]> datosList = new ArrayList<>();
	    
	    int[] columnWidths;

	    if (tipo.equals("evolucion")) {
	        nombresColumnas = new String[]{"N°", "Moneda", "Tasa de cambio", "Hora", "Porcentaje de evolución", "Dólar",
	                "Euro", "Peso argentino", "Yen", "Reales", "Peso mexicano", "Peso chileno", "Peso uruguayo",
	                "Boliviano", "Rupia"};
	        columnWidths = new int[]{50, 100, 100, 100, 150, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100};
	    } else if (tipo.equals("conversion")) {
	        nombresColumnas = new String[]{"N°", "Moneda de origen", "Monto original", "Tasa de cambio de origen",
	                "Moneda de destino", "Monto convertido", "Tasa de cambio de destino", "Hora de la conversión"};
	        columnWidths = new int[]{50, 150, 100, 150, 150, 100, 150, 150};
	    } else {
	        // Manejar caso no válido
	        return (new JTable());
	    }
		

		try {
		    com.mysql.cj.jdbc.result.ResultSetMetaData metaData = (com.mysql.cj.jdbc.result.ResultSetMetaData) resultSet.getMetaData();
		    int columnCount = metaData.getColumnCount();

		    while (resultSet.next()) {
		        Object[] fila = new Object[columnCount];
		        for (int i = 1; i <= columnCount; i++) {
		            Object dato = resultSet.getObject(i);
		            fila[i - 1] = dato;
		        }
		        datosList.add(fila);
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}

		// Convertir el ArrayList en un arreglo bidimensional
		Object[][] datos = new Object[datosList.size()][nombresColumnas.length];
		datos = datosList.toArray(datos);
		
		jTable = new JTable(datos, nombresColumnas);
	    TableColumnModel columnModel = jTable.getColumnModel();
	    int columnCount2 = columnModel.getColumnCount();
		
		if (columnCount2 == columnWidths.length) {
	        for (int i = 0; i < columnCount2; i++) {
	            TableColumn column = columnModel.getColumn(i);
	            int width = columnWidths[i];
	            column.setPreferredWidth(width);
	        }
	    } else {
	        // Manejar el caso cuando la longitud del array no coincide con el número de columnas
	        System.out.println("La longitud del array columnWidths no coincide con el número de columnas.");
	    }
	    
	    return jTable;
	
	}
	
	/**
	 * Genera un gráfico de barras con la evolución relativa de todas las monedas entre dos horas especificadas.
	 *
	 * @param horaInicio La hora de inicio para la consulta de la evolución (en formato "HH:mm:ss").
	 * @param horaFin    La hora de fin para la consulta de la evolución (en formato "HH:mm:ss").
	 * @return Un objeto JFreeChart que representa el gráfico de barras generado.
	 */
	public JFreeChart generarGraficoBarrasRelativo(String horaInicio, String horaFin) {
		
		// Crear el conjunto de datos para el gráfico de barras
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		// Establecer la conexión a la base de datos
		String url = "jdbc:mysql://localhost:3306/moneda";
	    String usuario = "root";
	    String contrasenia = "123456";
		ConexionSQL database = new ConexionSQL(url, usuario, contrasenia);
		
		// Preparar los parámetros para la consulta SQL
		ArrayList<Object> parametros = new ArrayList<>();
		parametros.add(horaInicio);
		parametros.add(horaFin);
		
		// Ejecutar la consulta SQL para obtener los datos de evolución de monedas
		ResultSet consulta = database.ejecutarConsultaPreparada("SELECT dolar,euro,peso_argentino,yen,reales,peso_mexicano,peso_chileno,peso_uruguayo,boliviano,rupia FROM evolucion_moneda where hora>=? and hora<=?", parametros);
		
		try {
			if (consulta.next()) {
				
			} else {
				JOptionPane.showMessageDialog(null, "No hay registros disponibles para la hora especificada.");
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Variables para calcular la evolución relativa de cada moneda
		boolean primerRegistro = true;
		
		int columnCount = 10;
		ArrayList<BigDecimal> listaTodaOtra10 = new ArrayList<>();
		
		ArrayList<BigDecimal> listaPrimerRegistro = new ArrayList<>(Collections.nCopies(columnCount, BigDecimal.ZERO));
		BigDecimal cantidadResto = new BigDecimal(0.0);
		
		// Inicializar las variables para el cálculo de la evolución relativa
		BigDecimal dolar2= new BigDecimal("0.0"),euro2= new BigDecimal("0.0"),peso_argentino2= new BigDecimal("0.0"),yen2= new BigDecimal("0.0"),reales2= new BigDecimal("0.0"),peso_mexicano2= new BigDecimal("0.0"),peso_chileno2= new BigDecimal("0.0"),peso_uruguayo2= new BigDecimal("0.0"),boliviano2= new BigDecimal("0.0"),rupia2 = new BigDecimal("0.0");
		listaTodaOtra10.addAll(List.of(dolar2,euro2,peso_argentino2,yen2,reales2,peso_mexicano2,peso_chileno2,peso_uruguayo2,boliviano2,rupia2));
		
		try {
			// Obtener los metadatos de la consulta para obtener el número de columnas
			com.mysql.cj.jdbc.result.ResultSetMetaData metaData = (com.mysql.cj.jdbc.result.ResultSetMetaData) consulta.getMetaData();
			columnCount = metaData.getColumnCount();
			
			// Calcular la evolución relativa para cada moneda
			while (consulta.next()) {
				if (primerRegistro == true) {
					// Almacenar el primer registro para calcular la evolución
					for (int i=0; i<columnCount; i++) {
						BigDecimal valorObjeto = new BigDecimal(String.valueOf(consulta.getDouble(i+1)));
						listaPrimerRegistro.set(i, valorObjeto);
					}
				}
				// Calcular la suma de los valores de evolución para cada moneda
				if (primerRegistro == false) {
						
					for (int i=0; i<columnCount; i++) {
						BigDecimal valorObjeto = new BigDecimal(String.valueOf(consulta.getDouble(i+1)));
						listaTodaOtra10.set(i, (listaTodaOtra10.get(i).add(valorObjeto)));
					}
					cantidadResto=cantidadResto.add(new BigDecimal("1"));
				}
				
				primerRegistro = false;
			
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Calcular el valor promedio de evolución para cada moneda
		for (int i=0; i<listaTodaOtra10.size(); i++) {
			BigDecimal valorPromedio2 = new BigDecimal(String.valueOf(listaTodaOtra10.get(i).divide(cantidadResto, 3, RoundingMode.HALF_UP)));
			// Calcular la diferencia porcentual entre el valor promedio y el primer registro
				//BigDecimal valorFinal4 = valorPromedio2.subtract(listaPrimerRegistro.get(i)).divide(listaPrimerRegistro.get(i), 3, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
			BigDecimal valorFinal2 = new BigDecimal ((valorPromedio2.subtract(listaPrimerRegistro.get(i))).toString());
			BigDecimal valorFinal3= new BigDecimal((valorFinal2.divide(listaPrimerRegistro.get(i), 3, RoundingMode.HALF_UP)).toString());
			// Convertir la diferencia porcentual a porcentaje
			BigDecimal valorFinal4= new BigDecimal((valorFinal3.multiply(new BigDecimal("100"))).toString());
			// Actualizar el valor en la lista con el porcentaje de evolución
			listaTodaOtra10.set(i, valorFinal4);

		}
		
		// Etiquetas de las monedas para el gráfico
		ArrayList<String> nombresMonedas = new ArrayList<>();
		nombresMonedas.addAll(List.of("Dólar","Euro", "P.Arg.", "Yen", "Reales", "P.Mex.", "P.Chil.", 
				"P.Uru.", "Boliv.", "Rupia"));
		String info = "Registros entre la hora " + horaInicio + " y las " + horaFin + " hs.";
		for (int i=0; i<listaTodaOtra10.size(); i++) {
			dataset.addValue(listaTodaOtra10.get(i), info, nombresMonedas.get(i));
		}
		
		JFreeChart chart = ChartFactory.createBarChart(
                "Evolución Relativa",  // Título del gráfico
                "Divisas",         // Etiqueta del eje X
                "Diferencia Porcentual",            // Etiqueta del eje Y
                dataset);             // Conjunto de datos
		
		Color colorPastelFondo = new Color(223, 210, 128);
        chart.setBackgroundPaint(colorPastelFondo);
        chart.setBorderPaint(Color.BLACK);

        return chart;

	}
	
	/**
	 * Genera un gráfico de barras con la evolución absoluta de todas las monedas entre dos horas especificadas.
	 *
	 * @param horaInicio La hora de inicio para la consulta de la evolución (en formato "HH:mm:ss").
	 * @param horaFin    La hora de fin para la consulta de la evolución (en formato "HH:mm:ss").
	 * @return Un objeto JFreeChart que representa el gráfico de barras generado.
	 */
	public JFreeChart generarGraficoBarrasAbsoluto(String horaInicio, String horaFin) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		String url = "jdbc:mysql://localhost:3306/moneda";
	    String usuario = "root";
	    String contrasenia = "123456";
		ConexionSQL database = new ConexionSQL(url, usuario, contrasenia);
		
		ArrayList<Object> parametros = new ArrayList<>();
		parametros.add(horaInicio);
		parametros.add(horaFin);
		ResultSet consulta = database.ejecutarConsultaPreparada("SELECT dolar,euro,peso_argentino,yen,reales,peso_mexicano,peso_chileno,peso_uruguayo,boliviano,rupia FROM evolucion_moneda where hora>=? and hora<=?", parametros);
		
		try {
			if (consulta.next()) {
				
			} else {
				JOptionPane.showMessageDialog(null, "No hay registros disponibles para la hora especificada.");
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		boolean primerRegistro = true;
		
		int columnCount = 10;
		//ArrayList<BigDecimal> listaTodaOtra10 = new ArrayList<>(Collections.nCopies(columnCount, BigDecimal.ZERO));
		ArrayList<BigDecimal> listaTodaOtra10 = new ArrayList<>();
		
		ArrayList<BigDecimal> listaPrimerRegistro = new ArrayList<>(Collections.nCopies(columnCount, BigDecimal.ZERO));
		BigDecimal cantidadResto = new BigDecimal(0.0);
		
		BigDecimal dolar2= new BigDecimal("0.0"),euro2= new BigDecimal("0.0"),peso_argentino2= new BigDecimal("0.0"),yen2= new BigDecimal("0.0"),reales2= new BigDecimal("0.0"),peso_mexicano2= new BigDecimal("0.0"),peso_chileno2= new BigDecimal("0.0"),peso_uruguayo2= new BigDecimal("0.0"),boliviano2= new BigDecimal("0.0"),rupia2 = new BigDecimal("0.0");
		listaTodaOtra10.addAll(List.of(dolar2,euro2,peso_argentino2,yen2,reales2,peso_mexicano2,peso_chileno2,peso_uruguayo2,boliviano2,rupia2));
		
		try {
			com.mysql.cj.jdbc.result.ResultSetMetaData metaData = (com.mysql.cj.jdbc.result.ResultSetMetaData) consulta.getMetaData();
			columnCount = metaData.getColumnCount();
			
			while (consulta.next()) {
				if (primerRegistro == true) {
					
					for (int i=0; i<columnCount; i++) {
						BigDecimal valorObjeto = new BigDecimal(String.valueOf(consulta.getDouble(i+1)));
						listaPrimerRegistro.set(i, valorObjeto);
					}
				}
				
				if (primerRegistro == false) {
					System.out.println("listaTodaOtra Size: " + listaTodaOtra10.size());
						
					for (int i=0; i<columnCount; i++) {
						BigDecimal valorObjeto = new BigDecimal(String.valueOf(consulta.getDouble(i+1)));
						listaTodaOtra10.set(i, valorObjeto);
					}
					//cantidadResto=cantidadResto.add(new BigDecimal("1"));
				}
				
				primerRegistro = false;
			
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (int i=0; i<listaTodaOtra10.size(); i++) {
						
			BigDecimal valorFinal2 = new BigDecimal ((listaTodaOtra10.get(i).subtract(listaPrimerRegistro.get(i))).toString());
			BigDecimal valorFinal3= new BigDecimal((valorFinal2.divide(listaPrimerRegistro.get(i), 3, RoundingMode.HALF_UP)).toString());
			BigDecimal valorFinal4= new BigDecimal((valorFinal3.multiply(new BigDecimal("100"))).toString());
			listaTodaOtra10.set(i, valorFinal4);
		}
		
		ArrayList<String> nombresMonedas = new ArrayList<>();
		nombresMonedas.addAll(List.of("Dólar","Euro", "P.Arg.", "Yen", "Reales", "P.Mex.", "P.Chil.", 
				"P.Uru.", "Boliv.", "Rupia"));
		String info = "Registros entre la hora " + horaInicio + " y las " + horaFin + " hs.";
		for (int i=0; i<listaTodaOtra10.size(); i++) {
			dataset.addValue(listaTodaOtra10.get(i), info, nombresMonedas.get(i));
		}
		
		JFreeChart chart = ChartFactory.createBarChart(
                "Evolución Absoluta",  // Título del gráfico
                "Divisas",         // Etiqueta del eje X
                "Diferencia Porcentual",            // Etiqueta del eje Y
                dataset);             // Conjunto de datos
		
		Color colorPastelFondo = new Color(223, 210, 128);
        chart.setBackgroundPaint(colorPastelFondo);
        chart.setBorderPaint(Color.BLACK);
		return chart;
	}
	
	public void testResultSet(ResultSet consulta) {
	    try {
	    	com.mysql.cj.jdbc.result.ResultSetMetaData metaData = (com.mysql.cj.jdbc.result.ResultSetMetaData) consulta.getMetaData();
	        int columnCount = metaData.getColumnCount();

	        // Imprimir los nombres de las columnas
	        for (int i = 1; i <= columnCount; i++) {
	            String columnName = metaData.getColumnName(i);
	            System.out.print(columnName + "\t");
	        }
	        System.out.println();

	        // Imprimir los datos de cada fila
	        while (consulta.next()) {
	            for (int i = 1; i <= columnCount; i++) {
	                Object dato = consulta.getObject(i);
	                System.out.print(dato + "\t");
	            }
	            System.out.println();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	

}
