package Clases;

import java.math.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.sql.Timestamp;
import java.text.ParseException;

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




import javax.swing.JFrame;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.util.*;
import java.util.List;

import javax.swing.JPanel;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import java.awt.*;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.BorderLayout;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase VentanaPrincipal que representa la interfaz gráfica principal de la aplicación.
 * Esta clase hereda de JFrame y es la ventana principal de la aplicación.
 */
public class VentanaPrincipal extends JFrame implements ActionListener {
    
	private JPanel cardsPanel;
	private JPanel panelComboBoxesEvolucion = new JPanel();
	private CardLayout cardLayout;
	private JTable tablaConversionInicial;
	private JTable tablaEvolucionInicial;
	private JScrollPane scrollPaneConversionActual;
	private JScrollPane panelTablaConversion;
	private JScrollPane panelTablaEvolucion;

	private JButton botonEvolucionIndividual;
	private JButton botonEvolucionAbsoluta;
	private JButton botonEvolucionRelativa;
	private JComboBox<String> comboBoxOrigen = new JComboBox<>();
	private JComboBox<String> comboBoxDestino = new JComboBox<>();
	private JTextField textFieldMonto = new JTextField(10);
	private JComboBox<String> comboBoxDivisa = new JComboBox<>();
    private JTextField textFieldNuevoValor = new JTextField(10);
	
    private JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel();
    private JPanel panel3 = new JPanel();
    private JPanel graficoActual;
    private JTable tablaConversionActual;
    private JTable tablaEvolucionActual;
    private JComboBox<String> comboBoxHoraInicio = new JComboBox<>();
    private JComboBox<String> comboBoxHoraFin = new JComboBox<>();
    private JComboBox<String> comboBoxDivisaEvolucionIndividual = new JComboBox<>();
    private String isSelected= "Ninguno";
    //private static JProgressBar progressBar = new JProgressBar();
    String url = "jdbc:mysql://localhost:3306/moneda";
    String usuario = "root";
    String contrasenia = "123456";
	ConexionSQL database = new ConexionSQL(url, usuario, contrasenia);
    Graficos graficos= new Graficos();
    TestClases funciones = new TestClases();
    private static ArrayList<TodaOtraMoneda> listaTodaOtraMoneda = new ArrayList<TodaOtraMoneda>();
    private static ArrayList<String> divisasNombre = new ArrayList<>();
    private static ArrayList<String> divisasNombreTodaOtra = new ArrayList<>();
    private static ArrayList<String> divisasTabla = new ArrayList<>();
    static Dolar dolar= new Dolar("Dolar", "USA", new BigDecimal ("0.91629"));
	static Euro euro=new Euro("Euro", "Unión Europea", new BigDecimal ("1.09082"));
    TodaOtraMoneda pesoArgentino= new TodaOtraMoneda("Peso argentino", "Argentina", new BigDecimal ("0.00389"));
	TodaOtraMoneda yen= new TodaOtraMoneda("Yen", "Japón", new BigDecimal ("0.00691"));
	TodaOtraMoneda real= new TodaOtraMoneda("Real", "Brasil", new BigDecimal ("0.20864"));
	TodaOtraMoneda pesoMexicano= new TodaOtraMoneda("Peso mexicano", "México", new BigDecimal ("0.05849"));
	TodaOtraMoneda pesoChileno= new TodaOtraMoneda("Peso chileno", "Chile", new BigDecimal ("0.00125"));
	TodaOtraMoneda pesoUruguayo= new TodaOtraMoneda("Peso uruguayo", "Uruguay", new BigDecimal ("0.02635"));
	TodaOtraMoneda boliviano= new TodaOtraMoneda("Boliviano", "Bolivia", new BigDecimal ("0.14133"));
	TodaOtraMoneda rupia= new TodaOtraMoneda("Rupia", "India", new BigDecimal ("0.0122"));
	private static String horaString = "00:00:08";
	private JProgressBar progressBar;
	
	/**
     * Constructor de la clase VentanaPrincipal.
     * Configura la ventana y agrega los botones y paneles necesarios.
     */
	public VentanaPrincipal() {
        // Configurar la ventana
        configurarVentana();
        agregarBotones();
        configurarPaneles();
    
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setVisible(true);
    }
    
	/**
     * Método para configurar la ventana principal.
     * Se encarga de establecer el tamaño, título y diseño de la ventana.
     */
    private void configurarVentana() {
        // Configurar la ventana para ocupar toda la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, (screenSize.height-35));
        
        // Otras configuraciones de la ventana, si lo deseas
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Conversión y Estadística de Divisas");
        
        getContentPane().setLayout(new BorderLayout());
        
        database.eliminarRegistrosTabla("evolucion_moneda");
        
        ArrayList<String> columnas4 = new ArrayList<>();
		ArrayList<Object> valores4 = new ArrayList<>();
		columnas4.addAll(List.of("moneda","tasa_cambio","hora","porcentaje_evolucion","dolar","euro","peso_argentino","yen","reales","peso_mexicano","peso_chileno","peso_uruguayo","boliviano","rupia"));
		valores4.addAll(List.of("Dólar", 0.91629, "00:00:00", 0.0, 0.91629, 1.09082, 0.00389, 0.00691, 0.20864, 0.05849, 0.00125, 0.02635, 0.14133, 0.0122));
		
		database.insertarRegistroPreparado("evolucion_moneda", columnas4, valores4);
    }
    
    /**
     * Método para agregar los botones a la ventana principal.
     * Se encarga de crear los botones y establecer sus características.
     */
    private void agregarBotones() {
    	JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	Color colorPastelFondo = new Color(223, 210, 128);
    	panelBotones.setBackground(colorPastelFondo);
    	panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
    	
    	// Crear los botones
    	JButton boton1 = new JButton("Conversión");
        JButton boton2 = new JButton("Tasas de cambio");
        JButton boton3 = new JButton("Gráficos");
        JButton boton4 = new JButton("Inserción random");
        JButton boton5 = new JButton("?");
        
        
        boton1.setPreferredSize(new Dimension(200, 50));
        boton2.setPreferredSize(new Dimension(200, 50));
        boton3.setPreferredSize(new Dimension(200, 50));
        boton4.setPreferredSize(new Dimension(205, 50));
        boton5.setPreferredSize(new Dimension(50, 50));
        
        Font font = new Font("Palatino Linotype", Font.PLAIN, 19);
        
        // Establecer el color de fondo de los botones (color pastel)
        Color colorPastel = new Color(238, 221, 109); // Puedes ajustar los valores RGB
        boton1.setBackground(colorPastel);
        boton2.setBackground(colorPastel);
        boton3.setBackground(colorPastel);
        boton4.setBackground(colorPastel);
        
        boton1.setFont(font);
        boton1.setText("<html><div style='text-align: center;'>Conversión</div></html>");
        boton2.setFont(font);
        boton2.setText("<html><div style='text-align: center;'>Tasas de cambio</div></html>");
        boton3.setFont(font);
        boton3.setText("<html><div style='text-align: center;'>Gráficos</div></html>");
        boton4.setText("<html><div style='text-align: center;'>Inserción random</div></html>");
        Font fuentePersonalizada = new Font("Palatino Linotype", Font.PLAIN, 17);
        boton4.setFont(fuentePersonalizada);
        boton5.setBackground(colorPastel);
        Font fuentePersonalizada2 = new Font("Palatino Linotype", Font.PLAIN, 18);
        boton5.setFont(fuentePersonalizada2);
        boton5.setText("<html><div style='text-align: center;'>?</div></html>");
        
        
        
     // Agregar los botones al panel superior usando BorderLayout.NORTH
        panelBotones.add(boton1);
        panelBotones.add(boton2);
        panelBotones.add(boton3);
        panelBotones.add(boton4);
        panelBotones.add(boton5);
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/conversion2.png"));
        boton1.setIcon(icon);
        ImageIcon icon2 = new ImageIcon(getClass().getResource("/resources/tasadecambio.png"));
        boton2.setIcon(icon2);
        ImageIcon icon3 = new ImageIcon(getClass().getResource("/resources/estadistica.png"));
        boton3.setIcon(icon3);
        ImageIcon icon4 = new ImageIcon(getClass().getResource("/resources/random2.png"));
        boton4.setIcon(icon4);
        //boton3.setMargin(new Insets(10, 10, 0, 0));
        
        getContentPane().add(panelBotones, BorderLayout.NORTH);
        
        boton1.setActionCommand("Panel1");
        boton2.setActionCommand("Panel2");
        boton3.setActionCommand("Panel3");
        
        boton1.addActionListener(this);
        boton2.addActionListener(this);
        boton3.addActionListener(this);
        boton4.addActionListener(btnRandom);
        boton5.addActionListener(btnInformativoPrincipal);
        
        
    }
    
    /**
     * Método para configurar los paneles de la ventana principal.
     * Se encarga de configurar y agregar los paneles a la ventana principal.
     */
    private void configurarPaneles() {
    	
    	database.eliminarRegistrosTabla("registro_conversion");
    	
    	divisasNombre.addAll(List.of("Dólar", "Euro", "Peso Argentino", "Yen", "Reales", "Peso Mexicano", "Peso Chileno", "Peso Uruguayo", "Boliviano", "Rupia"));
    	divisasNombreTodaOtra.addAll(List.of("Peso Argentino", "Yen", "Reales", "Peso Mexicano", "Peso Chileno", "Peso Uruguayo", "Boliviano", "Rupia"));
    	divisasTabla.addAll(List.of ("dolar","euro","peso_argentino","yen","reales","peso_mexicano","peso_chileno","peso_uruguayo","boliviano","rupia"));
    	listaTodaOtraMoneda.addAll(List.of(pesoArgentino, yen, real, pesoMexicano, pesoChileno, pesoUruguayo, boliviano, rupia));
        
    	// Crea el panel que contiene los tres paneles
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        
        
        // No vuelvas a instanciar los paneles panel1, panel2 y panel3
        Color colorPastelFondo = new Color(223, 210, 128);
        panel1.setBackground(colorPastelFondo);
        panel2.setBackground(Color.GREEN);
        panel3.setBackground(Color.BLUE);

        // Crea los componentes del panel1
        comboBoxOrigen = new JComboBox<>();
        comboBoxDestino = new JComboBox<>();
        textFieldMonto = new JTextField(10);
        JButton botonConvertir = new JButton("Convertir");
        botonConvertir.addActionListener(btnConvertir);
        ResultSet resultset2 = database.ejecutarConsulta("Select * from registro_conversion");
	
        botonConvertir.setPreferredSize(new Dimension(235,30));
        Font font = new Font("Palatino Linotype", Font.PLAIN, 19);
        Color colorPastel = new Color(238, 221, 109);
        botonConvertir.setBackground(colorPastel);
        botonConvertir.setFont(font);
        botonConvertir.setText("<html><div style='text-align: center;'>Convertir</div></html>");
        
        // Crea el panel que contiene los tres paneles
		cardLayout = new CardLayout();
		cardsPanel = new JPanel(cardLayout);
		cardsPanel.add(panel1, "Panel1");
		cardsPanel.add(panel2, "Panel2");
		cardsPanel.add(panel3, "Panel3");
		
		getContentPane().add(cardsPanel, BorderLayout.CENTER);
		   
		panel1.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		
		
		gbc.weightx = 0.5;
	    gbc.weighty = 0.0;
		 
		JLabel labelMonedaOrigen = new JLabel("Moneda de origen:");
		JLabel labelMonedaDestino = new JLabel("Moneda de destino:");
		JLabel labelMonto = new JLabel("Monto:");
		     
		// Establecer las restricciones para el JLabel "Moneda de origen"
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 10, 5, 10);
		panel1.add(labelMonedaOrigen, gbc);
		
		// Establecer las restricciones para el JComboBox de moneda de origen
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 5, 10);
		panel1.add(comboBoxOrigen, gbc);
		
		// Establecer las restricciones para el JLabel "Moneda de destino"
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 10, 5, 10);
		panel1.add(labelMonedaDestino, gbc);
		
		// Establecer las restricciones para el JComboBox de moneda de destino
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 10, 5, 10);
		panel1.add(comboBoxDestino, gbc);
		
		// Establecer las restricciones para el JLabel "Monto"
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 10, 5, 10);
		panel1.add(labelMonto, gbc);
		
		// Establecer las restricciones para el JTextField de monto
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.ipadx = 120;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 18, 5, 10);
		panel1.add(textFieldMonto, gbc);
		
		// Establecer las restricciones para el botón "Convertir"
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 10, 10);
		panel1.add(botonConvertir, gbc);
		
		// Establece las restricciones para el quinto componente (tablaConversiones)
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 8; // Ocupa cuatro celdas horizontales
		gbc.gridheight = 1; // Ocupa una celda vertical
		gbc.weighty = 1.0; // Permite que la tabla ocupe espacio vertical adicional
		gbc.fill = GridBagConstraints.BOTH; // Rellena el espacio en ambas direcciones
		gbc.insets = new Insets(0, 10, 10, 10); // Márgenes
		JTable tablaConversionInicial = graficos.generarTabla(resultset2, "conversion");
		tablaConversionActual = tablaConversionInicial;
		panelTablaConversion = new JScrollPane(tablaConversionInicial);
		panel1.add(panelTablaConversion, gbc);
		
		try {
			resultset2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		 
		String[] monedas = { "Dólar","Euro", "Peso Argentino", "Yen", "Reales", "Peso Mexicano", "Peso Chileno", 
				"Peso Uruguayo", "Boliviano", "Rupia" };
		
		// Agregar elementos al comboBoxOrigen
		for (String moneda : monedas) {
		    comboBoxOrigen.addItem(moneda);
		}
		
		// Agregar elementos al comboBoxDestino (pueden ser los mismos o diferentes)
		for (String moneda : monedas) {
		    comboBoxDestino.addItem(moneda);
		}

		panel2.setLayout(new GridBagLayout());
		GridBagConstraints gbc2 = new GridBagConstraints();
		
		comboBoxDivisa = new JComboBox<>();
	    textFieldNuevoValor = new JTextField(10);
	    JButton botonActualizar = new JButton("Actualizar");
	    botonActualizar.addActionListener(btnActualizar);
	    JTable tablaRegistros = new JTable();
	    JLabel labelDivisa = new JLabel("Divisa:");
		JLabel labelNuevoValor = new JLabel("Nuevo Valor:");
		
		gbc2.weightx = 0.5;
	    gbc2.weighty = 0.0;
	    
		
		gbc2.gridx = 0;
		gbc2.gridy = 0;
		gbc2.anchor = GridBagConstraints.CENTER;
		gbc2.insets = new Insets(10, 10, 5, 10);
		panel2.add(labelDivisa, gbc2);
		
		gbc2.gridx = 0;
		gbc2.gridy = 2;
		gbc2.fill = GridBagConstraints.NONE;
		gbc2.insets = new Insets(10, 10, 5, 10);
		panel2.add(labelNuevoValor, gbc2);
		
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.fill = GridBagConstraints.NONE;
		gbc2.insets = new Insets(10, 10, 5, 10);
		panel2.add(comboBoxDivisa, gbc2);
		
		gbc2.gridx = 0;
		gbc2.gridy = 3;
		gbc2.fill = GridBagConstraints.NONE;
		gbc2.insets = new Insets(10, 10, 5, 10);
		panel2.add(textFieldNuevoValor, gbc2);
		
		gbc2.gridx = 0;
		gbc2.gridy = 4;
		gbc2.gridwidth = 2;
		gbc2.fill = GridBagConstraints.NONE;
		gbc2.insets = new Insets(10, 10, 10, 10);
		panel2.add(botonActualizar, gbc2);
		
		botonActualizar.setPreferredSize(new Dimension(206,30));
		botonActualizar.setBackground(colorPastel);
		botonActualizar.setFont(font);
		botonActualizar.setText("<html><div style='text-align: center;'>Actualizar</div></html>");
		
		
	    gbc2.gridx = 0;
	    gbc2.gridy = 5;
	    gbc2.gridwidth = 2;
	    gbc2.weighty = 1.0;
	    gbc2.fill = GridBagConstraints.BOTH;
	    gbc2.insets = new Insets(0, 10, 10, 10);
	    ResultSet resultset3 = database.ejecutarConsulta("Select * from evolucion_moneda");
	    JTable tablaEvolucionInicial = graficos.generarTabla(resultset3, "evolucion");
		tablaEvolucionActual = tablaEvolucionInicial;
		panelTablaEvolucion = new JScrollPane(tablaEvolucionInicial);
		panel2.add(panelTablaEvolucion, gbc2);
	
		// Configurar la apariencia del segundo panel
		panel2.setBackground(colorPastelFondo);

	    // Agregar elementos al comboBoxDivisa (pueden ser los mismos o diferentes)
	    String[] divisas = { "Dólar", "Euro", "Peso Argentino", "Yen", "Reales", "Peso Mexicano", "Peso Chileno", 
	            "Peso Uruguayo", "Boliviano", "Rupia" };
	    for (String divisa : divisas) {
	        comboBoxDivisa.addItem(divisa);
	    }
	    
	    panel3.setLayout(new BorderLayout());
	    panel3.setBackground(colorPastelFondo);
	    
	    

	    // Crear los botones
	    botonEvolucionIndividual = new JButton("Evolución Individual");
	    botonEvolucionAbsoluta = new JButton("Evolución Absoluta");
	    botonEvolucionRelativa = new JButton("Evolución Relativa");
	    JButton botonInformativo = new JButton("?");
        
        botonEvolucionIndividual = new JButton("Evolución Individual");
        botonEvolucionIndividual.setPreferredSize(new Dimension(200, 50));
        botonEvolucionIndividual.setBackground(colorPastel);
        Font fuentePersonalizada = new Font("Palatino Linotype", Font.PLAIN, 18);
        botonEvolucionIndividual.setFont(fuentePersonalizada);
        botonEvolucionIndividual.setText("<html><div style='text-align: center;'>Evolución Individual</div></html>");
        panel3.add(botonEvolucionIndividual);
        
        botonEvolucionAbsoluta = new JButton("Evolución Absoluta");
        botonEvolucionAbsoluta.setPreferredSize(new Dimension(200, 50));
        botonEvolucionAbsoluta.setBackground(colorPastel);
        botonEvolucionAbsoluta.setFont(font);
        botonEvolucionAbsoluta.setText("<html><div style='text-align: center;'>Evolución Absoluta</div></html>");
        panel3.add(botonEvolucionAbsoluta);

        // Crear el botón botonEvolucionRelativa y establecer sus propiedades
        botonEvolucionRelativa = new JButton("Evolución Relativa");
        botonEvolucionRelativa.setPreferredSize(new Dimension(200, 50));
        botonEvolucionRelativa.setBackground(colorPastel);
        botonEvolucionRelativa.setFont(font);
        botonEvolucionRelativa.setText("<html><div style='text-align: center;'>Evolución Relativa</div></html>");
        panel3.add(botonEvolucionRelativa);
        
        botonInformativo = new JButton("Evolución Individual");
        botonInformativo.setPreferredSize(new Dimension(50, 50));
        botonInformativo.setBackground(colorPastel);
        botonInformativo.setFont(fuentePersonalizada);
        botonInformativo.setText("<html><div style='text-align: center;'>?</div></html>");
        botonInformativo.addActionListener(btnInformativo);
        panel3.add(botonInformativo);
        
        
        botonEvolucionIndividual.addActionListener(new ActionListener() {
        	/**
             * Método actionPerformed para manejar eventos de botón.
             * Este método se llama automáticamente cuando ocurre un evento de botón.
             */
        	@Override
            public void actionPerformed(ActionEvent e) {
                // Habilitar el ComboBox de divisa cuando se selecciona "Evolución Individual"
                comboBoxDivisaEvolucionIndividual.setEnabled(true);
                isSelected = "Evolución Individual";
                if (graficoActual != null) {
                    panelComboBoxesEvolucion.remove(graficoActual);
                    panelComboBoxesEvolucion.revalidate();
                    panelComboBoxesEvolucion.repaint();
                    graficoActual = null;
                }
            }
            
        });

        // Agregar ActionListener para los otros botones (Absoluta y Relativa)
        botonEvolucionAbsoluta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Deshabilitar el ComboBox de divisa cuando se selecciona "Evolución Absoluta"
                comboBoxDivisaEvolucionIndividual.setEnabled(false);
                isSelected = "Evolución Absoluta";
                if (graficoActual != null) {
                    panelComboBoxesEvolucion.remove(graficoActual);
                    panelComboBoxesEvolucion.revalidate();
                    panelComboBoxesEvolucion.repaint();
                    graficoActual = null;
                }
            }
        });

        botonEvolucionRelativa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Deshabilitar el ComboBox de divisa cuando se selecciona "Evolución Relativa"
                comboBoxDivisaEvolucionIndividual.setEnabled(false);
                isSelected = "Evolución Relativa";
                if (graficoActual != null) {
                    panelComboBoxesEvolucion.remove(graficoActual);
                    panelComboBoxesEvolucion.revalidate();
                    panelComboBoxesEvolucion.repaint();
                    graficoActual = null;
                }
            }
        });

	    // Crear los ComboBoxes
	    comboBoxHoraInicio = new JComboBox<>();
	    comboBoxHoraInicio.setEditable(true);
	    comboBoxHoraFin = new JComboBox<>();
	    comboBoxHoraFin.setEditable(true);
	    comboBoxDivisaEvolucionIndividual = new JComboBox<>();

	    // Agregar elementos a los ComboBoxes (pueden ser los mismos o diferentes)
	    String[] horas = { "00:00:00", "00:30:00", "01:00:00", "01:30:00", "02:00:00", "02:30:00", "03:00:00", "03:30:00", "04:00:00", "04:30:00", "05:00:00", "05:30:00", "06:00:00", "06:30:00", "07:00:00", "07:30:00", "08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00", "10:30:00", "11:00:00", "12:00:00", "13:00:00", "14:00:00", "15:00:00","16:00:00", "17:00:00","18:00:00", "19:00:00", "20:00:00", "21:00:00", "22:00:00", "23:00:00" };
	    String[] divisas2 = { "Dólar", "Euro", "Peso Argentino", "Yen", "Reales", "Peso Mexicano", "Peso Chileno", "Peso Uruguayo", "Boliviano", "Rupia" };

	    for (String hora : horas) {
	        comboBoxHoraInicio.addItem(hora);
	        comboBoxHoraFin.addItem(hora);
	    }

	    for (String divisa : divisas2) {
	        comboBoxDivisaEvolucionIndividual.addItem(divisa);
	    }

	    // Crear un JPanel para los botones y otro JPanel para los ComboBoxes
	    JPanel panelBotonesEvolucion = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
	    panelComboBoxesEvolucion.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
	    panelBotonesEvolucion.setBackground(colorPastelFondo);
	    panelComboBoxesEvolucion.setBackground(colorPastelFondo);
	    
	    
	    
	    JButton botonAplicar = new JButton("Aplicar");
	    botonAplicar.setPreferredSize(new Dimension(100, 30));
	    botonAplicar.setBackground(colorPastel);
	    botonAplicar.setFont(font);
	    botonAplicar.setText("<html><div style='text-align: center;'>Aplicar</div></html>");
	    botonAplicar.addActionListener(aplicarListener);
	    
	    // Agregar los botones al panelBotonesEvolucion
	    panelBotonesEvolucion.add(botonEvolucionIndividual);
	    panelBotonesEvolucion.add(botonEvolucionAbsoluta);
	    panelBotonesEvolucion.add(botonEvolucionRelativa);
	    panelBotonesEvolucion.add(botonInformativo);
	    

	    // Agregar los ComboBoxes al panelComboBoxesEvolucion
	    panelComboBoxesEvolucion.add(new JLabel("Hora Inicio:"));
	    panelComboBoxesEvolucion.add(comboBoxHoraInicio);
	    panelComboBoxesEvolucion.add(new JLabel("Hora Fin:"));
	    panelComboBoxesEvolucion.add(comboBoxHoraFin);
	    panelComboBoxesEvolucion.add(new JLabel("Divisa:"));
	    panelComboBoxesEvolucion.add(comboBoxDivisaEvolucionIndividual);
	    panelComboBoxesEvolucion.add(botonAplicar);

	    // Agregar los paneles al panel3
	    panel3.add(panelBotonesEvolucion, BorderLayout.NORTH);
	    panel3.add(panelComboBoxesEvolucion, BorderLayout.CENTER);
	    
	    
    }
    
    /**
     * Método actionPerformed para manejar eventos de botón "Aplicar".
     * Se llama automáticamente cuando el botón "Aplicar" es presionado.
     * Realiza las siguientes acciones:
     * - Valida y obtiene las horas seleccionadas en los JComboBox.
     * - Verifica que las horas tengan un formato válido (HH:mm:ss).
     * - Muestra mensajes de error si el formato de hora es inválido.
     * - Si el tipo de gráfico seleccionado es "Ninguno", muestra un mensaje indicando que se debe seleccionar un tipo de gráfico.
     * - Si el tipo de gráfico seleccionado es "Evolución Individual":
     *     - Obtiene la divisa seleccionada en el JComboBox correspondiente.
     *     - Genera el gráfico de líneas para la divisa seleccionada y las horas proporcionadas.
     *     - Actualiza el panel con el gráfico generado.
     * - Si el tipo de gráfico seleccionado es "Evolución Absoluta":
     *     - Genera el gráfico de barras absolutas utilizando las horas proporcionadas.
     *     - Actualiza el panel con el gráfico generado.
     * - Si el tipo de gráfico seleccionado es "Evolución Relativa":
     *     - Genera el gráfico de barras de evolución relativa utilizando las horas proporcionadas.
     *     - Actualiza el panel con el gráfico generado. 
     */
    ActionListener aplicarListener = new ActionListener() {
    	@Override
        public void actionPerformed(ActionEvent e) {
            
        	String formatoHora = "HH:mm:ss";
        	// Obtener los valores seleccionados en los JComboBox
        	String horaInicio = "", horaFin = "";
        	String horaInicioPrueba = (String) comboBoxHoraInicio.getSelectedItem();
            try {
            	SimpleDateFormat sdf = new SimpleDateFormat(formatoHora);
            	sdf.setLenient(false); // No permitir valores inválidos
                sdf.parse(horaInicioPrueba);
                horaInicio = (String) comboBoxHoraInicio.getSelectedItem();
            } catch (ParseException ex) {
                // Capturar la excepción de formato inválido
                JOptionPane.showMessageDialog(null, "El formato de hora debe ser HH:mm:ss");
                return;
            }
            
            String horaFinPrueba = (String) comboBoxHoraFin.getSelectedItem();
            
            try {
            	SimpleDateFormat sdf = new SimpleDateFormat(formatoHora);
            	sdf.setLenient(false); // No permitir valores inválidos
                sdf.parse(horaFinPrueba);
                horaFin = (String) comboBoxHoraFin.getSelectedItem();
            } catch (ParseException ex) {
                // Capturar la excepción de formato inválido
                JOptionPane.showMessageDialog(null, "El formato de hora debe ser HH:mm:ss");
                return;
            }
            
            if (isSelected.equals("Ninguno")) {
            	JOptionPane.showMessageDialog(null, "Primero debe seleccionar el tipo de gráfico.");
            }
            
            if (isSelected.equals("Evolución Individual")){
            	String divisa = (String) comboBoxDivisaEvolucionIndividual.getSelectedItem();
            	int indiceDivisa = divisasNombre.indexOf(divisa);
            	String divisaFinal = String.valueOf(divisasTabla.get(indiceDivisa));
            	
            	if (graficoActual != null) {
                    panelComboBoxesEvolucion.remove(graficoActual);
                }
            	
            	try {
                    JPanel graficoEvolucionIndividual = graficos.generarGraficolineas(divisaFinal, horaInicio, horaFin);
                    graficoActual = graficoEvolucionIndividual; // Actualizar la referencia del gráfico actual
                    graficoEvolucionIndividual.setAlignmentY(BOTTOM_ALIGNMENT);
                    panelComboBoxesEvolucion.add(graficoEvolucionIndividual);
                    panelComboBoxesEvolucion.revalidate();
                    panelComboBoxesEvolucion.repaint();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            
            if (isSelected.equals("Evolución Absoluta")){
            	if (graficoActual != null) {
                    panelComboBoxesEvolucion.remove(graficoActual);
                }
            	
            	JFreeChart chart = graficos.generarGraficoBarrasAbsoluto(horaInicio, horaFin);
        		ChartPanel chartPanel = new ChartPanel(chart);
        		
        		JPanel graficoEvolucionAbsoluta = chartPanel;
                graficoActual = graficoEvolucionAbsoluta; // Actualizar la referencia del gráfico actual
                graficoEvolucionAbsoluta.setAlignmentY(BOTTOM_ALIGNMENT);
                panelComboBoxesEvolucion.add(graficoEvolucionAbsoluta);
                panelComboBoxesEvolucion.revalidate();
                panelComboBoxesEvolucion.repaint();
            	
            }
            
            if (isSelected.equals("Evolución Relativa")){
            	if (graficoActual != null) {
                    panelComboBoxesEvolucion.remove(graficoActual);
                }
            	
            	JFreeChart chart = graficos.generarGraficoBarrasRelativo(horaInicio, horaFin);
        		ChartPanel chartPanel = new ChartPanel(chart);
            	
            	JPanel graficoEvolucionRelativa = chartPanel;
                graficoActual = graficoEvolucionRelativa; // Actualizar la referencia del gráfico actual
                graficoEvolucionRelativa.setAlignmentY(BOTTOM_ALIGNMENT);
                panelComboBoxesEvolucion.add(graficoEvolucionRelativa);
                panelComboBoxesEvolucion.revalidate();
                panelComboBoxesEvolucion.repaint();
            	
            }
            
        }
	
    };
    
    /**
     * ActionListener para manejar el evento del botón "Convertir".
     * Se llama automáticamente cuando el botón "Convertir" es presionado.
     * Realiza las siguientes acciones:
     * - Valida y obtiene el monto ingresado en el textFieldMonto.
     * - Verifica que el monto sea un número válido.
     * - Muestra un mensaje de error si el monto ingresado no es válido.
     * - Verifica que el monto no sea negativo.
     * - Muestra un mensaje de error si el monto es negativo.
     * - Realiza la conversión del monto entre divisas seleccionadas en los JComboBox (comboBoxOrigen y comboBoxDestino).
     * - Obtiene las tasas de cambio para las divisas seleccionadas.
     * - Realiza los cálculos de conversión según las tasas de cambio y las divisas seleccionadas.
     * - Muestra un mensaje con el monto convertido.
     * - Registra la conversión en la base de datos "registro_conversion" con la moneda de origen, el monto original, la tasa de cambio origen,
     *   la moneda de destino, el monto convertido, la tasa de cambio destino y la hora de la conversión.
     * - Genera una tabla actualizada con los registros de las conversiones realizadas y la muestra en el panel1.
     * 
     * @param e El evento de botón que ha ocurrido.
     */
    ActionListener btnConvertir = new ActionListener() {
    	@Override
        public void actionPerformed(ActionEvent e) {
    		//comboBoxOrigen, comboBoxDestino y textFieldMonto
    		BigDecimal monto = new BigDecimal("0.0");
    		try {
    			monto = new BigDecimal(textFieldMonto.getText().toString());
    		} catch (NumberFormatException ne) {
    			JOptionPane.showMessageDialog(null, "El valor ingresado es incorrecto.");
    			return;
    		}
    		int comparacion = monto.compareTo(new BigDecimal("0.0"));
    		if (comparacion < 0) {
                try {
                    throw new NumeroNegativoException();
                } catch (NumeroNegativoException nne) {
                    JOptionPane.showMessageDialog(null, "El valor no debe ser negativo.");
                }
                return; // Salir del ActionListener si se lanza la excepción
            }
    		
    		BigDecimal montoDolar = new BigDecimal("0.0");
    		BigDecimal montoFinal = new BigDecimal("0.0");
    		String cboOrigen = comboBoxOrigen.getSelectedItem().toString();
    		String cboDestino = comboBoxDestino.getSelectedItem().toString();
    		String hora= TestClases.horadesistema();
    		BigDecimal tasaCambioOrigen= new BigDecimal("0.0");
    		BigDecimal tasaCambioDestino= new BigDecimal("0.0");
    		
    		ArrayList<String> divisasListaTodaOtra = new ArrayList<>();
        	divisasListaTodaOtra.addAll(List.of("Peso Argentino", "Yen", "Reales", "Peso Mexicano", "Peso Chileno", "Peso Uruguayo", "Boliviano", "Rupia"));
    		if (cboOrigen.equals("Dólar")) {
    			montoDolar = monto;
    			tasaCambioOrigen= dolar.getTasa_de_cambio_dolar();
    		}
    		if (cboOrigen.equals("Euro")) {
    			montoDolar = TestClases.conversionEuroADolar(monto, euro);
    			tasaCambioOrigen= euro.getTasa_de_cambio_euro();
    		} if (!cboOrigen.equals("Dólar") && !cboOrigen.equals("Euro")) {
    			int indiceDivisa = divisasListaTodaOtra.indexOf(cboOrigen);
    			TodaOtraMoneda divisaFinal = listaTodaOtraMoneda.get(indiceDivisa);
            	montoDolar = TestClases.conversionGenericaADolares(monto, divisaFinal);
            	tasaCambioOrigen= divisaFinal.getTasa_de_cambio_referida();
    			
    		}
    		if (cboDestino.equals("Dólar")) {
    			montoFinal = montoDolar;
    			System.out.println("monto final: " + montoFinal);
    			tasaCambioDestino= dolar.getTasa_de_cambio_dolar();
    		}
    		if (cboDestino.equals("Euro")) {
    			montoFinal = TestClases.conversionDolarAEuro(montoDolar, dolar);
    			tasaCambioDestino= euro.getTasa_de_cambio_euro();
    		} 
    		if (!cboDestino.equals("Dólar") && !cboDestino.equals("Euro")) {
    			int indiceDivisa2 = divisasListaTodaOtra.indexOf(cboDestino);
            	TodaOtraMoneda divisaFinal2 = listaTodaOtraMoneda.get(indiceDivisa2);
    			montoFinal = TestClases.conversionDolarAGenerico(divisaFinal2, montoDolar);
    			tasaCambioDestino= divisaFinal2.getTasa_de_cambio_referida();
    		}
    		
    		montoFinal=montoFinal.setScale(2,RoundingMode.HALF_UP);
  
    		JOptionPane.showMessageDialog(null, "El monto convertido de " + comboBoxOrigen.getSelectedItem() + " a " + comboBoxDestino.getSelectedItem() + " es de " + montoFinal);
    		
    		String monedaOrigen = divisasNombre.get(divisasNombre.indexOf(cboOrigen));
    		String monedaDestino = divisasNombre.get(divisasNombre.indexOf(cboDestino));
    		
    		ArrayList<String> columnas = new ArrayList<>();
    		ArrayList<Object> valores = new ArrayList<>();
    		columnas.addAll(List.of("moneda_origen", "monto_original", "tasa_cambio_origen", "moneda_destino", "monto_convertido", "tasa_cambio_destino", "hora_conversion"));
    		valores.addAll(List.of(monedaOrigen, monto, tasaCambioOrigen, monedaDestino, montoFinal, tasaCambioDestino, hora));
    		
    		database.insertarRegistroPreparado("registro_conversion", columnas, valores);
    		
    		ResultSet resultset2 = database.ejecutarConsulta("Select * from registro_conversion");
            JTable nuevaTablaConversion = graficos.generarTabla(resultset2, "conversion");
            
            if (panelTablaConversion != null) {
                panel1.remove(panelTablaConversion);
            }
    	
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 8;
            gbc.gridwidth = 4;
            gbc.gridheight = 1;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(0, 10, 10, 10);
            panelTablaConversion = new JScrollPane(nuevaTablaConversion);
            panel1.add(panelTablaConversion, gbc);

            // Actualizar la interfaz gráfica
            panel1.revalidate();
            panel1.repaint();
    	}
    };
    
    /**
     * ActionListener para manejar el evento del botón "Actualizar".
     * Se llama automáticamente cuando el botón "Actualizar" es presionado.
     * Realiza las siguientes acciones:
     * - Obtiene la divisa seleccionada en el comboBoxDivisa.
     * - Valida y obtiene el nuevo valor ingresado en el textFieldNuevoValor.
     * - Verifica que el nuevo valor sea un número válido.
     * - Muestra un mensaje de error si el nuevo valor ingresado no es válido.
     * - Verifica que el nuevo valor no sea negativo.
     * - Muestra un mensaje de error si el nuevo valor es negativo.
     * - Actualiza la tasa de cambio de la divisa seleccionada con el nuevo valor.
     * - Realiza ajustes en las tasas de cambio relacionadas con la divisa seleccionada.
     * - Registra la evolución de la divisa en la base de datos "evolucion_moneda" con la moneda,
     *   tasa de cambio, hora, porcentaje de evolución, tasas de cambio relacionadas y la hora de actualización.
     * - Genera una tabla actualizada con los registros de la evolución de las divisas y la muestra en el panel2.
     *
     */
    ActionListener btnActualizar = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			String cboDivisa = comboBoxDivisa.getSelectedItem().toString();
			BigDecimal nuevoValor = new BigDecimal("0.0");
			try {
				nuevoValor = new BigDecimal(textFieldNuevoValor.getText().toString());
    		} catch (NumberFormatException ne) {
    			JOptionPane.showMessageDialog(null, "El valor ingresado es incorrecto.");
    			return;
    		}
    		int comparacion = nuevoValor.compareTo(new BigDecimal("0.0"));
    		if (comparacion < 0) {
                try {
                    throw new NumeroNegativoException();
                } catch (NumeroNegativoException nne) {
                    JOptionPane.showMessageDialog(null, "El valor no debe ser negativo.");
                }
                return; // Salir del ActionListener si se lanza la excepción
            }
			
			if (cboDivisa.equals("Dólar")) {
				dolar.setTasa_de_cambio_dolar(nuevoValor);
				TestClases.actualizarTasaEuro(euro, dolar, nuevoValor);
				TestClases.actualizarListaTodaOtraMoneda(listaTodaOtraMoneda, dolar, nuevoValor, TestClases.tasa_previa_dolar);
				TestClases.setTasa_previa_dolar(dolar.getTasa_de_cambio_dolar());
				TestClases.setTasa_previa_euro(euro.getTasa_de_cambio_euro());
			}
			if (cboDivisa.equals("Euro")) {
				euro.setTasa_de_cambio_euro(nuevoValor);
				TestClases.actualizarTasaDolar(dolar, euro, nuevoValor);
				TestClases.actualizarListaTodaOtraMoneda(listaTodaOtraMoneda, dolar, dolar.getTasa_de_cambio_dolar(), TestClases.tasa_previa_dolar);
				TestClases.setTasa_previa_dolar(dolar.getTasa_de_cambio_dolar());
				TestClases.setTasa_previa_euro(euro.getTasa_de_cambio_euro());;
			}
			
			if ((!cboDivisa.equals("Dólar")) && (!cboDivisa.equals("Euro"))) {
				int indiceDivisa3 = divisasNombre.indexOf(cboDivisa);
            	TodaOtraMoneda divisaFinal3 = listaTodaOtraMoneda.get(indiceDivisa3-2);
				divisaFinal3.setTasa_de_cambio_referida(nuevoValor);
			}
			
			//Para registro:
			String tasadolar= dolar.getTasa_de_cambio_dolar().setScale(5, RoundingMode.UNNECESSARY).toString();
			String tasaeuro= euro.getTasa_de_cambio_euro().setScale(5, RoundingMode.UNNECESSARY).toString();
			
			ArrayList<String> listaTasasTodaOtra = new ArrayList<>();
			listaTasasTodaOtra= TestClases.listaTasasTodaOtraMoneda(listaTodaOtraMoneda);
			
			ArrayList<String> listaVariablesTasas = new ArrayList<>();
			
			String tasapesoar="", tasayen="", tasareales="", tasapesome="", tasapesoch="", tasapesour="", tasaboliviano="", tasarupia="";
			
			listaVariablesTasas.addAll(List.of(tasapesoar, tasayen, tasareales, tasapesome, tasapesoch, tasapesour, tasaboliviano, tasarupia));
			
			for (int i=0; i<listaVariablesTasas.size();i++) {
				listaVariablesTasas.set(i, listaTasasTodaOtra.get(i));
			}
			
			String hora=TestClases.horadesistema();
			
			// el siguiente chorro de código se trató de simplificar casi desde un principio,
			// lo que ocurrió fue que se intentó hacer un for de mil maneras y no se pudo. Estuve
			// consultando el código con ChatGPT una y otra vez y no le encontramos la vuelta.
			// A lo último directamente le pedí que me lo haga al ChatGPT y el resultado era prácticamente
			// igual a parte de todo lo que yo había intentado. Igual no funcionó.
		    BigDecimal tasapesoar2 = new BigDecimal(listaVariablesTasas.get(0));
			BigDecimal tasapesoarRedondeado = tasapesoar2.setScale(5, RoundingMode.UNNECESSARY);
			tasapesoar = tasapesoarRedondeado.toString();
		    BigDecimal tasayen2 = new BigDecimal(listaVariablesTasas.get(1));
			BigDecimal tasayenRedondeado = tasayen2.setScale(5, RoundingMode.UNNECESSARY);
			tasayen = tasayenRedondeado.toString();
		    BigDecimal tasareales2 = new BigDecimal(listaVariablesTasas.get(2));
			BigDecimal tasarealesRedondeado = tasareales2.setScale(5, RoundingMode.UNNECESSARY);
			tasareales = tasarealesRedondeado.toString();
		    BigDecimal tasapesome2 = new BigDecimal(listaVariablesTasas.get(3));
			BigDecimal tasapesomeRedondeado = tasapesome2.setScale(5, RoundingMode.UNNECESSARY);
			tasapesome = tasapesomeRedondeado.toString();
		    BigDecimal tasapesoch2 = new BigDecimal(listaVariablesTasas.get(4));
			BigDecimal tasapesochRedondeado = tasapesoch2.setScale(5, RoundingMode.UNNECESSARY);
			tasapesoch = tasapesochRedondeado.toString();
		    BigDecimal tasapesour2 = new BigDecimal(listaVariablesTasas.get(5));
			BigDecimal tasapesourRedondeado = tasapesour2.setScale(5, RoundingMode.UNNECESSARY);
			tasapesour = tasapesourRedondeado.toString();
		    BigDecimal tasaboliviano2 = new BigDecimal(listaVariablesTasas.get(6));
			BigDecimal tasabolivianoRedondeado = tasaboliviano2.setScale(5, RoundingMode.UNNECESSARY);
			tasaboliviano = tasabolivianoRedondeado.toString();
		    BigDecimal tasarupia2 = new BigDecimal(listaVariablesTasas.get(7));
			BigDecimal tasarupiaRedondeado = tasarupia2.setScale(5, RoundingMode.UNNECESSARY);
			tasarupia = tasarupiaRedondeado.toString();
		    
			String monedaQuery2= divisasTabla.get(divisasNombre.indexOf(cboDivisa));
			String monedaQuery3= divisasNombre.get(divisasTabla.indexOf(monedaQuery2));
			double porcentaje_evolucion = Double.valueOf(TestClases.porcentajeEvolucion(monedaQuery2, dolar, euro, listaTodaOtraMoneda).toString()) ;
			double tasaCambioTabla = 0.0;
			if (cboDivisa.equals("Dólar")) {
				tasaCambioTabla = Double.valueOf(dolar.getTasa_de_cambio_dolar().toString());
			}
			if (cboDivisa.equals("Euro")) {
				tasaCambioTabla = Double.valueOf(euro.getTasa_de_cambio_euro().toString());
			}
			if ((!cboDivisa.equals("Dólar")) && (!cboDivisa.equals("Euro"))) {
				int indiceDivisa4 = divisasNombre.indexOf(cboDivisa);
            	TodaOtraMoneda divisaFinal4 = listaTodaOtraMoneda.get(indiceDivisa4-2);
				tasaCambioTabla = Double.valueOf(divisaFinal4.getTasa_de_cambio_referida().toString());
			}
						
			ArrayList<String> columnas2 = new ArrayList<>();
			ArrayList<Object> valores2 = new ArrayList<>();
			columnas2.addAll(List.of("moneda","tasa_cambio","hora","porcentaje_evolucion","dolar","euro","peso_argentino","yen","reales","peso_mexicano","peso_chileno","peso_uruguayo","boliviano","rupia"));
			valores2.addAll(List.of(monedaQuery3, tasaCambioTabla, hora, porcentaje_evolucion, tasadolar, tasaeuro, tasapesoar, tasayen, tasareales, tasapesome, tasapesoch, tasapesour, tasaboliviano, tasarupia));

			database.insertarRegistroPreparado("evolucion_moneda", columnas2, valores2);
			
			//para actualizar tabla
			ResultSet resultset3 = database.ejecutarConsulta("Select * from evolucion_moneda");
            JTable nuevaTablaEvolucion = graficos.generarTabla(resultset3, "evolucion");
    		
            if (panelTablaEvolucion != null) {
                panel2.remove(panelTablaEvolucion);
            }
    		
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = 0;
    	    gbc2.gridy = 5;
    	    gbc2.gridwidth = 2;
    	    gbc2.weighty = 1.0;
    	    gbc2.fill = GridBagConstraints.BOTH;
    	    gbc2.insets = new Insets(0, 10, 10, 10);

            panelTablaEvolucion = new JScrollPane(nuevaTablaEvolucion);
            panel2.add(panelTablaEvolucion, gbc2);

            panel2.revalidate();
            panel2.repaint();
			
		}
	};
	
	/**
	 * ActionListener para manejar el evento del botón "Informativo".
	 * Se llama automáticamente cuando el botón informativo es presionado.
	 * Muestra un cuadro de diálogo informativo con detalles sobre el gráfico de Evolución Relativa.
	 *
	 */
	ActionListener btnInformativo = new ActionListener() {
		@Override
        public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "En que consiste:\nEs la evolución de la moneda en el tiempo que toma en cuenta todos "
					+ "los valores intermedios\n(además del final) en función del valor inicial. Es decir, compara el primer "
					+ "registro\n(según la hora ingresada) con el promedio de los posteriores.\nPara que sirve:\n- Tendencias o patrones consistentes a lo largo del tiempo.\r\n"
					+ "- Desempeño relativo: comparación del comportamiento de las divisas en un período de tiempo.\r\n"
					+ "- Medición de volatilidad, estabilidad y fortaleza de las divisas.", "Sobre el gráfico de Evolución Relativa", JOptionPane.INFORMATION_MESSAGE);
		}
	};
	
	/**
	 * ActionListener para manejar el evento del botón "Informativo principal".
	 * Se llama automáticamente cuando el botón informativo principal es presionado.
	 * Muestra un cuadro de diálogo informativo con detalles sobre el programa.
	 *
	 */
	ActionListener btnInformativoPrincipal = new ActionListener() {
		@Override
        public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "Sobre el comportamiento del euro y el dólar en el programa:\r\n"
					+ "En el programa, hemos asignado al dólar y al euro el papel de divisas de referencia, lo que significa que sus valores actúan como puntos de referencia para las demás divisas.\nEsta decisión tiene una base fundamentada en la realidad económica, donde el dólar y el euro son dos de las principales monedas internacionales, ampliamente utilizadas\nen transacciones comerciales y financieras a nivel mundial. \r"
					+ "El euro y el dólar, como monedas de referencia, tienen una relación de co-dependencia inversa en el programa.\nEsto se basa en el hecho de que, en los mercados financieros, el valor del euro y el dólar tienden a influirse mutuamente. Cuando uno de ellos experimenta una apreciación\n(aumento en su valor), el otro tiende a depreciarse (disminución en su valor). Por ejemplo, si el euro baja, el dólar tiende a subir, y viceversa. Sin embargo, es importante\ndestacar que esta relación de co-dependencia negativa implementada en el programa es una simplificación y no pretende reflejar completamente la complejidad y\ndinamismo de los mercados financieros reales.\r\n"
					+ "\nSobre la función de inserción random:\r\n"
					+ "La función de inserción automática genera 10000 registros que representan la evolución de las tasas de cambio para distintas divisas con respecto a sus divisas de referencia.\r\n"
					+ "\nObservación de comportamientos en los gráficos:\r\n"
					+ "Esta función tiene un gran potencial para permitir a los usuarios observar y analizar diferentes comportamientos de las divisas a través de los gráficos generados. Los gráficos\npueden mostrar la evolución de las tasas de cambio en relación con su respectiva divisa de referencia en intervalos específicos o períodos de tiempo más amplios. Por ejemplo,\npueden examinar qué sucedió en una franja de tiempo determinada, como la primera mitad del período total, incluyendo una caída del dólar, y cómo reaccionó la moneda\nboliviana frente a esa evolución sin dejar de lado las fluctuaciones propias del boliviano que ocurren aparte de las variaciones en el precio del dólar, así como también ver en\ngráficos de evolución absoluta o relativa cuales fueron las tendencias generalizadas, teniendo en cuenta que al igual que en la vida real, los escenarios reflejados no son\nsiempre los esperables o especulables.\r\n"
					+ "En resumen, la función de inserción aleatoria permite a los usuarios explorar y analizar la evolución de las tasas de cambio de manera más dinámica y eficiente.\nEsto ahorra tiempo y esfuerzo en comparación con la inserción manual de registros.\r\n"
					+ "\nNota sobre la precisión en las operaciones de actualización de tasas de cambio:\r\n"
					+ "Es importante señalar que, durante el proceso de actualización de las tasas de cambio, se ha enfrentado un desafío relacionado con la precisión de los cálculos de división con\nvalores decimales. Al utilizar la clase BigDecimal de la librería math para realizar estas operaciones, se han presentado pequeñas imprecisiones en los resultados debido a la\nrepresentación interna de los números en punto flotante.\r\n"
					+ "Para abordar esta situación, se ha implementado un mecanismo de redondeo para las divisiones de punto flotante realizadas con BigDecimal. Si bien esta solución asegura\nun nivel aceptable de precisión para la mayoría de los casos, es importante tener en cuenta que, en contextos donde se requiere una precisión extrema, es posible explorar\nalternativas más avanzadas, como la utilización de la librería JScience y su API, o incluso considerar el uso de otros lenguajes de programación.\r\n"
					+ "", "Sobre la Lógica y Funcionalidad del programa", JOptionPane.INFORMATION_MESSAGE);
		}
	};
	
	/**
	 * ActionListener para manejar los eventos de los botones en la interfaz gráfica.
	 * Se llama automáticamente cuando se presiona uno de los botones.
	 * Realiza las siguientes acciones:
	 * - Obtiene el comando del botón presionado.
	 * - Muestra el panel correspondiente según el botón presionado, utilizando un CardLayout.
	 *
	 * @param e El evento de botón que ha ocurrido.
	 */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Obtén el comando del botón presionado
        String comando = e.getActionCommand();

        // Muestra el panel correspondiente según el botón presionado
        cardLayout.show(cardsPanel, comando);
        
    }   
        
    /**
     * Punto de entrada principal del programa.
     * Este método se encarga de iniciar la interfaz gráfica.
     * Crea una instancia de VentanaPrincipal y la hace visible.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
            ventanaPrincipal.setVisible(true);
        });
    }
    
    /**
     * Clase personalizada para representar una excepción de número negativo.
     * Se utiliza cuando se detecta que un valor numérico es negativo, donde no debería serlo.
     */
    public class NumeroNegativoException extends Exception {
    	public NumeroNegativoException() {
            super("El valor no debe ser un número negativo"); // Llama al constructor de la clase base (Exception) con el mensaje proporcionado
        }
    	
    }
    
    /**
     * Método para insertar un nuevo registro en la tabla "evolucion_moneda" de la base de datos.
     * Este método se encarga de actualizar los valores de tasas de cambio y generar un registro en la tabla "evolucion_moneda"
     * con los datos proporcionados.
     *
     * @param divisa La divisa para la cual se actualizará la tasa de cambio y se generará el registro.
     * @param tasaCambio La nueva tasa de cambio para la divisa.
     */
    public static void insertarRegistroEnActualizacion(String divisa, BigDecimal tasaCambio) {
    	
    	String url = "jdbc:mysql://localhost:3306/moneda";
	    String usuario = "root";
	    String contrasenia = "123456";
		ConexionSQL database = new ConexionSQL(url, usuario, contrasenia);
		
		if (divisa.equals("Dólar")) {
			dolar.setTasa_de_cambio_dolar(tasaCambio);
			TestClases.actualizarTasaEuro(euro, dolar, tasaCambio);
			TestClases.actualizarListaTodaOtraMoneda(listaTodaOtraMoneda, dolar, tasaCambio, TestClases.tasa_previa_dolar);
			TestClases.setTasa_previa_dolar(dolar.getTasa_de_cambio_dolar());
			TestClases.setTasa_previa_euro(euro.getTasa_de_cambio_euro());
			
		}
		if (divisa.equals("Euro")) {
			euro.setTasa_de_cambio_euro(tasaCambio);
			TestClases.actualizarTasaDolar(dolar, euro, tasaCambio);
			TestClases.actualizarListaTodaOtraMoneda(listaTodaOtraMoneda, dolar, dolar.getTasa_de_cambio_dolar(), TestClases.tasa_previa_dolar);
			TestClases.setTasa_previa_dolar(dolar.getTasa_de_cambio_dolar());
			TestClases.setTasa_previa_euro(euro.getTasa_de_cambio_euro());
		}
		if ((!divisa.equals("Dólar")) && (!divisa.equals("Euro"))) {
			int indiceDivisa3 = divisasNombre.indexOf(divisa);
			TodaOtraMoneda divisaFinal3 = listaTodaOtraMoneda.get(indiceDivisa3-2);
			divisaFinal3.setTasa_de_cambio_referida(tasaCambio);
		}
    	
    	String tasadolar= dolar.getTasa_de_cambio_dolar().setScale(5, RoundingMode.UNNECESSARY).toString();
		String tasaeuro= euro.getTasa_de_cambio_euro().setScale(5, RoundingMode.UNNECESSARY).toString();
		
		ArrayList<String> listaTasasTodaOtra = new ArrayList<>();
		listaTasasTodaOtra= TestClases.listaTasasTodaOtraMoneda(listaTodaOtraMoneda);
		
		ArrayList<String> listaVariablesTasas = new ArrayList<>();
		
		String tasapesoar="", tasayen="", tasareales="", tasapesome="", tasapesoch="", tasapesour="", tasaboliviano="", tasarupia="";
		
		listaVariablesTasas.addAll(List.of(tasapesoar, tasayen, tasareales, tasapesome, tasapesoch, tasapesour, tasaboliviano, tasarupia));
		
		for (int i=0; i<listaVariablesTasas.size();i++) {
			listaVariablesTasas.set(i, listaTasasTodaOtra.get(i));
		}
		
        // Crear un objeto LocalTime a partir del string
        LocalTime hora = LocalTime.parse(horaString, DateTimeFormatter.ofPattern("HH:mm:ss"));
        
        // Sumarle 8 segundos a la hora
        LocalTime horaMas4Segundos = hora.plusSeconds(4);
        
        // Convertir el resultado nuevamente a un string en el formato HH:mm:ss
        horaString = horaMas4Segundos.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		
		// el siguiente chorro de código se trató de simplificar casi desde un principio,
		// lo que ocurrió fue que se intentó hacer un for de mil maneras y no se pudo. Estuve
		// consultando el código con ChatGPT una y otra vez y no le encontramos la vuelta.
		// A lo último directamente le pedí que me lo haga al ChatGPT y el resultado era prácticamente
		// igual a parte de todo lo que yo había intentado. Igual no funcionó.
	    BigDecimal tasapesoar2 = new BigDecimal(listaVariablesTasas.get(0));
		BigDecimal tasapesoarRedondeado = tasapesoar2.setScale(5, RoundingMode.UNNECESSARY);
		tasapesoar = tasapesoarRedondeado.toString();
	    BigDecimal tasayen2 = new BigDecimal(listaVariablesTasas.get(1));
		BigDecimal tasayenRedondeado = tasayen2.setScale(5, RoundingMode.UNNECESSARY);
		tasayen = tasayenRedondeado.toString();
	    BigDecimal tasareales2 = new BigDecimal(listaVariablesTasas.get(2));
		BigDecimal tasarealesRedondeado = tasareales2.setScale(5, RoundingMode.UNNECESSARY);
		tasareales = tasarealesRedondeado.toString();
	    BigDecimal tasapesome2 = new BigDecimal(listaVariablesTasas.get(3));
		BigDecimal tasapesomeRedondeado = tasapesome2.setScale(5, RoundingMode.UNNECESSARY);
		tasapesome = tasapesomeRedondeado.toString();
	    BigDecimal tasapesoch2 = new BigDecimal(listaVariablesTasas.get(4));
		BigDecimal tasapesochRedondeado = tasapesoch2.setScale(5, RoundingMode.UNNECESSARY);
		tasapesoch = tasapesochRedondeado.toString();
	    BigDecimal tasapesour2 = new BigDecimal(listaVariablesTasas.get(5));
		BigDecimal tasapesourRedondeado = tasapesour2.setScale(5, RoundingMode.UNNECESSARY);
		tasapesour = tasapesourRedondeado.toString();
	    BigDecimal tasaboliviano2 = new BigDecimal(listaVariablesTasas.get(6));
		BigDecimal tasabolivianoRedondeado = tasaboliviano2.setScale(5, RoundingMode.UNNECESSARY);
		tasaboliviano = tasabolivianoRedondeado.toString();
	    BigDecimal tasarupia2 = new BigDecimal(listaVariablesTasas.get(7));
		BigDecimal tasarupiaRedondeado = tasarupia2.setScale(5, RoundingMode.UNNECESSARY);
		tasarupia = tasarupiaRedondeado.toString();
		
		String divisaTabla= divisasTabla.get(divisasNombre.indexOf(divisa));
		double porcentaje_evolucion = Double.valueOf(TestClases.porcentajeEvolucion(divisaTabla, dolar, euro, listaTodaOtraMoneda).toString()) ;
		
		ArrayList<String> columnas3 = new ArrayList<>();
		ArrayList<Object> valores3 = new ArrayList<>();
		columnas3.addAll(List.of("moneda","tasa_cambio","hora","porcentaje_evolucion","dolar","euro","peso_argentino","yen","reales","peso_mexicano","peso_chileno","peso_uruguayo","boliviano","rupia"));
		valores3.addAll(List.of(divisa, tasaCambio, horaString, porcentaje_evolucion, tasadolar, tasaeuro, tasapesoar, tasayen, tasareales, tasapesome, tasapesoch, tasapesour, tasaboliviano, tasarupia));
		
		database.insertarRegistroPreparado("evolucion_moneda", columnas3, valores3);
		database.cerrarConexion();
    }
    
    /**
     * Método para insertar 10000 registros en la tabla "evolucion_moneda" de la base de datos con un prototipo de datos.
     * Este método genera registros de manera aleatoria a partir de las tasas de cambio existentes en el sistema.
     */
    public static void insertar10000Prototipo() {
		
		int registrosTotales = 0;
		int numCuenta = 0;
		int numCuenta2 = 0;
		
		// Inicializar las tasas de cambio con valor 0.0
		BigDecimal tasaDeCambio1 = new BigDecimal ("0.0");
		BigDecimal tasaDeCambio2 = new BigDecimal ("0.0");
		BigDecimal tasaDeCambio3 = new BigDecimal ("0.0");
		BigDecimal tasaDeCambio4 = new BigDecimal ("0.0");
		BigDecimal tasaDeCambio5 = new BigDecimal ("0.0");
		
		// Crear una lista para almacenar las tasas de cambio
		ArrayList<BigDecimal> listaTasasInsercion = new ArrayList<>();
		listaTasasInsercion.addAll(List.of(tasaDeCambio1,tasaDeCambio2,tasaDeCambio3,tasaDeCambio4,tasaDeCambio5));
		
		// Variable temporal para almacenar las tasas de cambio durante el proceso de generación de registros
		BigDecimal tasa = new BigDecimal("0.0");	
		
		// Generar 10000 registros aleatorios
		while (registrosTotales<10000) {
			// Actualizar las tasas de cambio de la lista
			listaTasasInsercion.set(0, dolar.getTasa_de_cambio_dolar());
			listaTasasInsercion.set(1, euro.getTasa_de_cambio_euro());
			listaTasasInsercion.set(2, listaTodaOtraMoneda.get(numCuenta2).getTasa_de_cambio_referida());
			numCuenta2++;
			if (numCuenta2==8) { numCuenta2=0; } ;
			listaTasasInsercion.set(3, listaTodaOtraMoneda.get(numCuenta2).getTasa_de_cambio_referida());
			numCuenta2++;
			if (numCuenta2==8) { numCuenta2=0; } ;
			listaTasasInsercion.set(4, listaTodaOtraMoneda.get(numCuenta2).getTasa_de_cambio_referida());
			numCuenta2++;
			if (numCuenta2==8) { numCuenta2=0; } ;
			
			// Realizar el proceso de alteración de las tasas de cambio
			for (int i=0; i<listaTasasInsercion.size(); i++) {
				boolean positivoONegativo = TestClases.sorteoBooleano();
				tasa = listaTasasInsercion.get(i);
				BigDecimal alteracion = new BigDecimal ("0.025");
				BigDecimal pasoANegativo = new BigDecimal ("-1");
				
				// Realizar la alteración de la tasa de cambio según el sorteo
				if (positivoONegativo==true) {
					tasa = tasa.add (tasa.multiply(alteracion));
				}
				if (positivoONegativo==false) {
					tasa = tasa.add ((tasa.multiply(alteracion)).multiply(pasoANegativo));
				}
				
				// Actualizar la tasa de cambio en la lista
				listaTasasInsercion.set(i, tasa);
				
			}
			
			// Insertar los registros en la base de datos
			BigDecimal e = new BigDecimal("0.0");	
			e= listaTasasInsercion.get(0).setScale(5, RoundingMode.HALF_UP);
			insertarRegistroEnActualizacion("Dólar", e);
			registrosTotales++;
			BigDecimal g = new BigDecimal("0.0");	
			g= listaTasasInsercion.get(1).setScale(5, RoundingMode.HALF_UP);
			insertarRegistroEnActualizacion("Euro", g);
			registrosTotales++;
			
			// Insertar registros para las demás divisas
			insertarRegistroEnActualizacion(divisasNombreTodaOtra.get(numCuenta), listaTasasInsercion.get(2).setScale(5, RoundingMode.HALF_UP));
			numCuenta++;
			if (numCuenta==8) { numCuenta=0; } ;
			registrosTotales++;
			insertarRegistroEnActualizacion(divisasNombreTodaOtra.get(numCuenta), listaTasasInsercion.get(3).setScale(5, RoundingMode.HALF_UP));
			numCuenta++;
			if (numCuenta==8) { numCuenta=0; } ;
			registrosTotales++;
			insertarRegistroEnActualizacion(divisasNombreTodaOtra.get(numCuenta), listaTasasInsercion.get(4).setScale(5, RoundingMode.HALF_UP));
			numCuenta++;
			if (numCuenta==8) { numCuenta=0; } ;
			registrosTotales++;
			System.out.println("Registros totales: " + registrosTotales);
			
		}
			
			
	}
    
    /**
     * ActionListener para el botón "Inserción random" que realiza una inserción aleatoria de
     * registros en la tabla "evolucion_moneda".
     */
    ActionListener btnRandom = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				dolar.setTasa_de_cambio_dolar(new BigDecimal ("0.91629"));
				euro.setTasa_de_cambio_euro(new BigDecimal ("1.09082"));
			    pesoArgentino.setTasa_de_cambio_referida(new BigDecimal ("0.00389"));
				yen.setTasa_de_cambio_referida(new BigDecimal ("0.00691"));
				real.setTasa_de_cambio_referida(new BigDecimal ("0.20864"));
				pesoMexicano.setTasa_de_cambio_referida(new BigDecimal ("0.05849"));
				pesoChileno.setTasa_de_cambio_referida(new BigDecimal ("0.00125"));
				pesoUruguayo.setTasa_de_cambio_referida(new BigDecimal ("0.02635"));
				boliviano.setTasa_de_cambio_referida(new BigDecimal ("0.14133"));
				rupia.setTasa_de_cambio_referida(new BigDecimal ("0.0122"));
				
				TestClases.tasa_previa_dolar= new BigDecimal ("0.91629");
				TestClases.tasa_previa_euro= new BigDecimal ("1.09082");
				
		        String url = "jdbc:mysql://localhost:3306/moneda";
			    String usuario = "root";
			    String contrasenia = "123456";
				ConexionSQL database = new ConexionSQL(url, usuario, contrasenia);
				
				database.eliminarRegistrosTabla("evolucion_moneda");
				
				ArrayList<String> columnas4 = new ArrayList<>();
				ArrayList<Object> valores4 = new ArrayList<>();
				columnas4.addAll(List.of("moneda","tasa_cambio","hora","porcentaje_evolucion","dolar","euro","peso_argentino","yen","reales","peso_mexicano","peso_chileno","peso_uruguayo","boliviano","rupia"));
				valores4.addAll(List.of("Dólar", 0.91629, "00:00:00", 0.0, 0.91629, 1.09082, 0.00389, 0.00691, 0.20864, 0.05849, 0.00125, 0.02635, 0.14133, 0.0122));
				
				database.insertarRegistroPreparado("evolucion_moneda", columnas4, valores4);
				
				boolean curvaAprobada = false;
				
				horaString = "00:00:08";
				
				while (curvaAprobada==false) {
					
					System.out.println("COMIENZO");
					insertar10000Prototipo();
					System.out.println("=========================================");
					
					curvaAprobada=true;
					
					ResultSet resultset6 = database.ejecutarConsulta("Select * from evolucion_moneda");
		            JTable nuevaTablaEvolucion = graficos.generarTabla(resultset6, "evolucion");
		    		
		            if (panelTablaEvolucion != null) {
		                panel2.remove(panelTablaEvolucion);
		            }
		    		
		            GridBagConstraints gbc2 = new GridBagConstraints();
		            gbc2.gridx = 0;
		    	    gbc2.gridy = 5;
		    	    gbc2.gridwidth = 2;
		    	    gbc2.weighty = 1.0;
		    	    gbc2.fill = GridBagConstraints.BOTH;
		    	    gbc2.insets = new Insets(0, 10, 10, 10);

		            panelTablaEvolucion = new JScrollPane(nuevaTablaEvolucion);
		            panel2.add(panelTablaEvolucion, gbc2);

		            panel2.revalidate();
		            panel2.repaint();
								
		            JOptionPane.showMessageDialog(null, "Se ha generado la inserción aleatoria de manera exitosa.");
		            database.cerrarConexion();
				}
			}
				
	
		};
		
		
}  


