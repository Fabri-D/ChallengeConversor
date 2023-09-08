package Clases;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.*;


public class ConexionSQL {
	
	// Declaración de variables para la conexión a la base de datos
    private Connection conexion;
    String url = "jdbc:mysql://localhost:3306/moneda";
    String usuario = "root";
    String contrasenia = "123456";
    
    // Constructor de la clase para establecer la conexión a la base de datos
    public ConexionSQL(String url, String usuario, String contrasenia) {
        try {
            conexion = DriverManager.getConnection(url, usuario, contrasenia);
        } catch (SQLException e) {
            // Manejar la excepción de conexión
            e.printStackTrace();
        }
    }
    
    // Método para ejecutar una consulta SQL y obtener un conjunto de resultados
    public ResultSet ejecutarConsulta(String consulta) {
        try {
            Statement statement = conexion.createStatement();
            return statement.executeQuery(consulta);
        } catch (SQLException e) {
            // Manejar la excepción de consulta
            e.printStackTrace();
            return null;
        } 
    }
    
    // Método para insertar un nuevo registro en una tabla de la base de datos
    public void insertarRegistro(String tabla, String columnas, String valores) {
        String consulta = "INSERT INTO " + tabla + " (" + columnas + ") VALUES (" + valores + ")";
        try {
            Statement statement = conexion.createStatement();
            statement.executeUpdate(consulta);
        } catch (SQLException e) {
            // Manejar la excepción de consulta
            e.printStackTrace();
        }
    }
    
    // Método para ejecutar una consulta SQL preparada y obtener un conjunto de resultados
    public ResultSet ejecutarConsultaPreparada(String consulta, ArrayList<Object> parametros) {
        try {
            PreparedStatement statement = conexion.prepareStatement(consulta);

            // Asignar valores a los parámetros de la consulta preparada
            for (int i = 0; i < parametros.size(); i++) {
                statement.setObject(i + 1, parametros.get(i));
            }
            
            // Ejecutar la consulta preparada y obtener el resultado
            return statement.executeQuery();
        } catch (SQLException e) {
            // Manejar la excepción de consulta
            e.printStackTrace();
            return null;
        }
    }
    
    // Método para insertar un nuevo registro en una tabla usando una consulta preparada
    public void insertarRegistroPreparado(String tabla, ArrayList<String> columnas, ArrayList<Object> valores) {
        try {
        	
        	// Construir la consulta preparada con los parámetros proporcionados
            StringBuilder consultaBuilder = new StringBuilder();
            consultaBuilder.append("INSERT INTO ").append(tabla);
            
            // Agregar lista de columnas a la consulta preparada
            consultaBuilder.append(" (");
            for (int i = 0; i < columnas.size(); i++) {
                consultaBuilder.append(columnas.get(i));
                
                if (i < columnas.size() - 1) {
                    consultaBuilder.append(", ");
                }
            }
            consultaBuilder.append(")");
            
            // Agregar lista de valores
            consultaBuilder.append(" VALUES (");
            for (int i = 0; i < valores.size(); i++) {
                consultaBuilder.append("?");
                
                if (i < valores.size() - 1) {
                    consultaBuilder.append(", ");
                }
            }
            consultaBuilder.append(")");
            
            // Crear un PreparedStatement y asignar valores a los parámetros
            PreparedStatement statement = conexion.prepareStatement(consultaBuilder.toString());
            
            // Asignar valores a los parámetros
            for (int i = 0; i < valores.size(); i++) {
                statement.setObject(i + 1, valores.get(i));
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            // Manejar la excepción de consulta
            e.printStackTrace();
        }
    }
    
    // Método para eliminar todos los registros de una tabla y resetear el contador de autoincremento
    public void eliminarRegistrosTabla(String tabla) {
    	// Construir las consultas para eliminar registros y resetear el contador de autoincremento
    	String consulta = "delete from " + tabla + ";";
    	String consulta2 = "ALTER TABLE " + tabla + " AUTO_INCREMENT = 1;";
        try {
            Statement statement = conexion.createStatement();
            statement.executeUpdate(consulta);
            statement.executeUpdate(consulta2);
        } catch (SQLException e) {
            // Manejar la excepción de consulta
            e.printStackTrace();
        }
    }
    
    // Método para cerrar la conexión a la base de datos
    public void cerrarConexion() {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException e) {
            // Manejar la excepción de cierre de conexión
            e.printStackTrace();
        }
    }
}		

