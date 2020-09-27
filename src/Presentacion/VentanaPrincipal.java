package Presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;


/**
 * Clase VentanaPrincipal. Solo contiene la interfaz, nada de logica.
 * No es importante comentarla.
 *  
 * @author Eduardo Garcia Aparicio
 *
 */
public class VentanaPrincipal {

	protected JFrame frame;
	protected JLabel lbl_total_poblacion;
	protected JPanel panel_poblacion;
	protected JButton btnEmpezar;
	protected JButton btnParar;
	protected JButton btn_masvel;
	protected JButton btn_menosvel;
	private JPanel panel_porcentajes;
	protected JLabel lblTiempotranscurrido;
	protected JTable tabla_estadisticas;
	protected JTextField txt_numeroFilas;
	protected JTextField txt_inmune;
	protected JTextField txt_igual;
	protected JTextField txt_morir;
	protected JTextField txt_contagiofc;
	protected JTextField txt_contagiodiag;
	protected JSpinner spinner_focos;

	public VentanaPrincipal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	protected void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1158, 769);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel_simulacion = new JPanel();
		panel_simulacion.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		frame.getContentPane().add(panel_simulacion, BorderLayout.WEST);
		panel_simulacion.setLayout(new MigLayout("", "[269.00px]", "[390.00px,grow][280px,grow][41px][41px]"));

		JPanel panel_configuracion = new JPanel();
		panel_simulacion.add(panel_configuracion, "cell 0 0,growx,aligny top");
		GridBagLayout gbl_panel_configuracion = new GridBagLayout();
		gbl_panel_configuracion.columnWidths = new int[] { 180, 56 };
		gbl_panel_configuracion.rowHeights = new int[] { 28, 0, 0, 0, 63, 200, 0 };
		gbl_panel_configuracion.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_configuracion.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE };
		panel_configuracion.setLayout(gbl_panel_configuracion);

		JLabel lblNumeroDeFilas = new JLabel("Numero de filas: ");
		lblNumeroDeFilas.setFont(new Font("Dialog", Font.BOLD, 18));
		GridBagConstraints gbc_lblNumeroDeFilas = new GridBagConstraints();
		gbc_lblNumeroDeFilas.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumeroDeFilas.gridx = 0;
		gbc_lblNumeroDeFilas.gridy = 0;
		panel_configuracion.add(lblNumeroDeFilas, gbc_lblNumeroDeFilas);

		txt_numeroFilas = new JTextField();
		txt_numeroFilas.setDisabledTextColor(Color.WHITE);
		txt_numeroFilas.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				{
			        try
			        {
			            KeyEvent ke = new KeyEvent(e.getComponent(), KeyEvent.KEY_PRESSED,
			                    System.currentTimeMillis(), InputEvent.CTRL_MASK,
			                    KeyEvent.VK_F1, KeyEvent.CHAR_UNDEFINED);
			            e.getComponent().dispatchEvent(ke);
			        }
			        catch (Throwable e1)
			        {e1.printStackTrace();}
			    }
			}
		});
		txt_numeroFilas.setToolTipText("<html><p>Mas de 90 filas puede</p> ralentizar el equipo.</html>");
		txt_numeroFilas.setMaximumSize(new Dimension(80, 2147483647));
		txt_numeroFilas.setFont(new Font("Dialog", Font.PLAIN, 18));
		GridBagConstraints gbc_txt_numeroFilas = new GridBagConstraints();
		gbc_txt_numeroFilas.insets = new Insets(0, 0, 5, 0);
		gbc_txt_numeroFilas.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_numeroFilas.gridx = 1;
		gbc_txt_numeroFilas.gridy = 0;
		panel_configuracion.add(txt_numeroFilas, gbc_txt_numeroFilas);
		txt_numeroFilas.setColumns(10);
		txt_numeroFilas.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') || ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					txt_numeroFilas.setEditable(true);
					btnEmpezar.setEnabled(true);
					btnParar.setEnabled(true);
				} else {
					txt_numeroFilas.setEditable(false);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				String value = txt_numeroFilas.getText();
				txt_numeroFilas.setEditable(true);
				if (value.length() > 0 && !value.equalsIgnoreCase("0")) {
					int numero = Integer.valueOf(value);
					lbl_total_poblacion.setText(String.valueOf(numero * numero));
					btnEmpezar.setEnabled(true);
					btnParar.setEnabled(true);
				} else {
					btnEmpezar.setEnabled(false);
					btnParar.setEnabled(false);
					lbl_total_poblacion.setText("0");
				}
			}
		});

		JLabel lblPoblacion = new JLabel("Poblacion: ");
		lblPoblacion.setFont(new Font("Dialog", Font.BOLD, 18));
		GridBagConstraints gbc_lblPoblacion = new GridBagConstraints();
		gbc_lblPoblacion.insets = new Insets(0, 0, 5, 5);
		gbc_lblPoblacion.gridx = 0;
		gbc_lblPoblacion.gridy = 2;
		panel_configuracion.add(lblPoblacion, gbc_lblPoblacion);
		
		JLabel lblFocos = new JLabel("Focos:");
		lblFocos.setFont(new Font("Dialog", Font.BOLD, 18));
		GridBagConstraints gbc_lblFocos = new GridBagConstraints();
		gbc_lblFocos.insets = new Insets(0, 0, 5, 0);
		gbc_lblFocos.gridx = 1;
		gbc_lblFocos.gridy = 2;
		panel_configuracion.add(lblFocos, gbc_lblFocos);

		lbl_total_poblacion = new JLabel("0");
		lbl_total_poblacion.setFont(new Font("Dialog", Font.BOLD, 18));
		GridBagConstraints gbc_lbl_total_poblacion = new GridBagConstraints();
		gbc_lbl_total_poblacion.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_total_poblacion.gridx = 0;
		gbc_lbl_total_poblacion.gridy = 3;
		panel_configuracion.add(lbl_total_poblacion, gbc_lbl_total_poblacion);
		
		spinner_focos = new JSpinner();
		spinner_focos.setModel(new SpinnerNumberModel(1, 0, 10, 1));
		GridBagConstraints gbc_spinner_focos = new GridBagConstraints();
		gbc_spinner_focos.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_focos.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_focos.gridx = 1;
		gbc_spinner_focos.gridy = 3;
		panel_configuracion.add(spinner_focos, gbc_spinner_focos);

		JPanel panel_tiempo = new JPanel();
		panel_tiempo.setBorder(
				new TitledBorder(null, "Tiempo Transcurrido", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_tiempo = new GridBagConstraints();
		gbc_panel_tiempo.gridwidth = 2;
		gbc_panel_tiempo.insets = new Insets(0, 0, 5, 0);
		gbc_panel_tiempo.fill = GridBagConstraints.BOTH;
		gbc_panel_tiempo.gridx = 0;
		gbc_panel_tiempo.gridy = 4;
		panel_configuracion.add(panel_tiempo, gbc_panel_tiempo);
		panel_tiempo.setLayout(new BorderLayout(0, 0));

		lblTiempotranscurrido = new JLabel("0 dias");
		lblTiempotranscurrido.setHorizontalAlignment(SwingConstants.CENTER);
		lblTiempotranscurrido.setFont(new Font("Dialog", Font.BOLD, 20));
		panel_tiempo.add(lblTiempotranscurrido, BorderLayout.CENTER);

		btn_menosvel = new JButton("- velocidad");
		btn_menosvel.setEnabled(false);
		panel_tiempo.add(btn_menosvel, BorderLayout.WEST);

		btn_masvel = new JButton("+ velocidad");
		btn_masvel.setEnabled(false);

		panel_tiempo.add(btn_masvel, BorderLayout.EAST);

		panel_porcentajes = new JPanel();
		panel_porcentajes.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Probabilidades Enfermo",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		GridBagConstraints gbc_panel_porcentajes = new GridBagConstraints();
		gbc_panel_porcentajes.gridwidth = 2;
		gbc_panel_porcentajes.fill = GridBagConstraints.BOTH;
		gbc_panel_porcentajes.gridx = 0;
		gbc_panel_porcentajes.gridy = 5;
		panel_configuracion.add(panel_porcentajes, gbc_panel_porcentajes);
		GridBagLayout gbl_panel_porcentajes = new GridBagLayout();
		gbl_panel_porcentajes.columnWidths = new int[] { 155, 0, 0, 0 };
		gbl_panel_porcentajes.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel_porcentajes.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel_porcentajes.rowWeights = new double[] { 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_porcentajes.setLayout(gbl_panel_porcentajes);

		JLabel lbl_morir = new JLabel("Morir:");
		lbl_morir.setFont(new Font("Noto Sans", Font.BOLD, 14));
		GridBagConstraints gbc_lbl_morir = new GridBagConstraints();
		gbc_lbl_morir.anchor = GridBagConstraints.EAST;
		gbc_lbl_morir.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_morir.gridx = 0;
		gbc_lbl_morir.gridy = 0;
		panel_porcentajes.add(lbl_morir, gbc_lbl_morir);

		txt_morir = new JTextField();
		txt_morir.setMaximumSize(new Dimension(80, 2147483647));
		txt_morir.setText("0,00");
		txt_morir.setHorizontalAlignment(SwingConstants.RIGHT);
		txt_morir.setFont(new Font("Dialog", Font.PLAIN, 18));
		txt_morir.setColumns(10);
		GridBagConstraints gbc_txt_morir = new GridBagConstraints();
		gbc_txt_morir.insets = new Insets(0, 0, 5, 5);
		gbc_txt_morir.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_morir.gridx = 1;
		gbc_txt_morir.gridy = 0;
		panel_porcentajes.add(txt_morir, gbc_txt_morir);

		JLabel label_1 = new JLabel("%");
		label_1.setFont(new Font("Noto Sans", Font.PLAIN, 19));
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 0);
		gbc_label_1.gridx = 2;
		gbc_label_1.gridy = 0;
		panel_porcentajes.add(label_1, gbc_label_1);

		JLabel lbl_inmune = new JLabel("Inmunizarse:");
		lbl_inmune.setFont(new Font("Noto Sans", Font.BOLD, 14));
		GridBagConstraints gbc_lbl_inmune = new GridBagConstraints();
		gbc_lbl_inmune.anchor = GridBagConstraints.EAST;
		gbc_lbl_inmune.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_inmune.gridx = 0;
		gbc_lbl_inmune.gridy = 1;
		panel_porcentajes.add(lbl_inmune, gbc_lbl_inmune);

		txt_inmune = new JTextField();
		txt_inmune.setMaximumSize(new Dimension(80, 2147483647));
		txt_inmune.setText("0,00");
		txt_inmune.setHorizontalAlignment(SwingConstants.RIGHT);
		txt_inmune.setFont(new Font("Dialog", Font.PLAIN, 18));
		txt_inmune.setColumns(10);
		GridBagConstraints gbc_txt_inmune = new GridBagConstraints();
		gbc_txt_inmune.insets = new Insets(0, 0, 5, 5);
		gbc_txt_inmune.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_inmune.gridx = 1;
		gbc_txt_inmune.gridy = 1;
		panel_porcentajes.add(txt_inmune, gbc_txt_inmune);

		JLabel label_2 = new JLabel("%");
		label_2.setFont(new Font("Noto Sans", Font.PLAIN, 19));
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.insets = new Insets(0, 0, 5, 0);
		gbc_label_2.gridx = 2;
		gbc_label_2.gridy = 1;
		panel_porcentajes.add(label_2, gbc_label_2);

		JLabel lblSeguirIgual = new JLabel("Seguir Igual:");
		lblSeguirIgual.setFont(new Font("Noto Sans", Font.BOLD, 14));
		GridBagConstraints gbc_lblSeguirIgual = new GridBagConstraints();
		gbc_lblSeguirIgual.anchor = GridBagConstraints.EAST;
		gbc_lblSeguirIgual.insets = new Insets(0, 0, 5, 5);
		gbc_lblSeguirIgual.gridx = 0;
		gbc_lblSeguirIgual.gridy = 2;
		panel_porcentajes.add(lblSeguirIgual, gbc_lblSeguirIgual);

		txt_igual = new JTextField();
		txt_igual.setMaximumSize(new Dimension(80, 2147483647));
		txt_igual.setText("0,00");
		txt_igual.setHorizontalAlignment(SwingConstants.RIGHT);
		txt_igual.setFont(new Font("Dialog", Font.PLAIN, 18));
		txt_igual.setColumns(10);
		GridBagConstraints gbc_txt_igual = new GridBagConstraints();
		gbc_txt_igual.insets = new Insets(0, 0, 5, 5);
		gbc_txt_igual.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_igual.gridx = 1;
		gbc_txt_igual.gridy = 2;
		panel_porcentajes.add(txt_igual, gbc_txt_igual);

		JLabel label_3 = new JLabel("%");
		label_3.setFont(new Font("Noto Sans", Font.PLAIN, 19));
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.insets = new Insets(0, 0, 5, 0);
		gbc_label_3.gridx = 2;
		gbc_label_3.gridy = 2;
		panel_porcentajes.add(label_3, gbc_label_3);

		JLabel lbl_contagio_diag = new JLabel("Contagio Vecino (f/c):");
		lbl_contagio_diag.setFont(new Font("Noto Sans", Font.BOLD, 14));
		GridBagConstraints gbc_lbl_contagio_diag = new GridBagConstraints();
		gbc_lbl_contagio_diag.anchor = GridBagConstraints.EAST;
		gbc_lbl_contagio_diag.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_contagio_diag.gridx = 0;
		gbc_lbl_contagio_diag.gridy = 3;
		panel_porcentajes.add(lbl_contagio_diag, gbc_lbl_contagio_diag);

		txt_contagiofc = new JTextField();
		txt_contagiofc.setMaximumSize(new Dimension(80, 2147483647));
		txt_contagiofc.setText("0,00");
		txt_contagiofc.setHorizontalAlignment(SwingConstants.RIGHT);
		txt_contagiofc.setFont(new Font("Dialog", Font.PLAIN, 18));
		txt_contagiofc.setColumns(10);
		GridBagConstraints gbc_txt_contagiofc = new GridBagConstraints();
		gbc_txt_contagiofc.insets = new Insets(0, 0, 5, 5);
		gbc_txt_contagiofc.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_contagiofc.gridx = 1;
		gbc_txt_contagiofc.gridy = 3;
		panel_porcentajes.add(txt_contagiofc, gbc_txt_contagiofc);

		JLabel label_4 = new JLabel("%");
		label_4.setFont(new Font("Noto Sans", Font.PLAIN, 19));
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.insets = new Insets(0, 0, 5, 0);
		gbc_label_4.gridx = 2;
		gbc_label_4.gridy = 3;
		panel_porcentajes.add(label_4, gbc_label_4);

		JLabel lblContagioVecinoDiagonal = new JLabel("Contagio Vecino (Diag):");
		lblContagioVecinoDiagonal.setFont(new Font("Noto Sans", Font.BOLD, 14));
		GridBagConstraints gbc_lblContagioVecinoDiagonal = new GridBagConstraints();
		gbc_lblContagioVecinoDiagonal.anchor = GridBagConstraints.EAST;
		gbc_lblContagioVecinoDiagonal.insets = new Insets(0, 0, 5, 5);
		gbc_lblContagioVecinoDiagonal.gridx = 0;
		gbc_lblContagioVecinoDiagonal.gridy = 4;
		panel_porcentajes.add(lblContagioVecinoDiagonal, gbc_lblContagioVecinoDiagonal);

		txt_contagiodiag = new JTextField();
		txt_contagiodiag.setMaximumSize(new Dimension(80, 2147483647));
		txt_contagiodiag.setText("0,00");
		txt_contagiodiag.setHorizontalAlignment(SwingConstants.RIGHT);
		txt_contagiodiag.setFont(new Font("Dialog", Font.PLAIN, 18));
		txt_contagiodiag.setColumns(10);
		GridBagConstraints gbc_txt_contagiodiag = new GridBagConstraints();
		gbc_txt_contagiodiag.insets = new Insets(0, 0, 5, 5);
		gbc_txt_contagiodiag.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_contagiodiag.gridx = 1;
		gbc_txt_contagiodiag.gridy = 4;
		panel_porcentajes.add(txt_contagiodiag, gbc_txt_contagiodiag);

		JLabel label_5 = new JLabel("%");
		label_5.setFont(new Font("Noto Sans", Font.PLAIN, 19));
		GridBagConstraints gbc_label_5 = new GridBagConstraints();
		gbc_label_5.insets = new Insets(0, 0, 5, 0);
		gbc_label_5.gridx = 2;
		gbc_label_5.gridy = 4;
		panel_porcentajes.add(label_5, gbc_label_5);

		JPanel panel_informacion = new JPanel();
		panel_informacion
				.setBorder(new TitledBorder(null, "Informacion", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_simulacion.add(panel_informacion, "cell 0 1,growx,aligny top");
		GridBagLayout gbl_panel_informacion = new GridBagLayout();
		gbl_panel_informacion.columnWidths = new int[] { 0, 62, 43, 0, 0 };
		gbl_panel_informacion.rowHeights = new int[] { 41, 47, 43, 47, 48, 0, 0 };
		gbl_panel_informacion.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_informacion.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_informacion.setLayout(gbl_panel_informacion);

		JLabel lblN = new JLabel("Nº");
		lblN.setFont(new Font("Dialog", Font.BOLD, 15));
		GridBagConstraints gbc_lblN = new GridBagConstraints();
		gbc_lblN.insets = new Insets(0, 0, 5, 5);
		gbc_lblN.gridx = 1;
		gbc_lblN.gridy = 0;
		panel_informacion.add(lblN, gbc_lblN);

		JLabel label = new JLabel("%");
		label.setFont(new Font("Dialog", Font.BOLD, 15));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 2;
		gbc_label.gridy = 0;
		panel_informacion.add(label, gbc_label);

		JLabel lblVivos = new JLabel("No Afectados:");
		lblVivos.setFont(new Font("Noto Sans", Font.BOLD, 17));
		GridBagConstraints gbc_lblVivos = new GridBagConstraints();
		gbc_lblVivos.anchor = GridBagConstraints.EAST;
		gbc_lblVivos.insets = new Insets(0, 0, 5, 5);
		gbc_lblVivos.gridx = 0;
		gbc_lblVivos.gridy = 1;
		panel_informacion.add(lblVivos, gbc_lblVivos);

		tabla_estadisticas = new JTable();
		tabla_estadisticas.setFont(new Font("Noto Sans", Font.PLAIN, 17));
		tabla_estadisticas.setModel(new DefaultTableModel(
				new Object[][] { { null, null }, { null, null }, { null, null }, { null, null }, },
				new String[] { "New column", "New column" }));
		tabla_estadisticas.setRowSelectionAllowed(false);
		tabla_estadisticas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GridBagConstraints gbc_tabla_estadisticas = new GridBagConstraints();
		gbc_tabla_estadisticas.gridheight = 4;
		gbc_tabla_estadisticas.gridwidth = 3;
		gbc_tabla_estadisticas.insets = new Insets(0, 0, 5, 0);
		gbc_tabla_estadisticas.fill = GridBagConstraints.BOTH;
		gbc_tabla_estadisticas.gridx = 1;
		gbc_tabla_estadisticas.gridy = 1;
		panel_informacion.add(tabla_estadisticas, gbc_tabla_estadisticas);

		JLabel lblMuertos = new JLabel("Enfermos:");
		lblMuertos.setFont(new Font("Noto Sans", Font.BOLD, 17));
		GridBagConstraints gbc_lblMuertos = new GridBagConstraints();
		gbc_lblMuertos.anchor = GridBagConstraints.EAST;
		gbc_lblMuertos.insets = new Insets(0, 0, 5, 5);
		gbc_lblMuertos.gridx = 0;
		gbc_lblMuertos.gridy = 2;
		panel_informacion.add(lblMuertos, gbc_lblMuertos);

		JLabel lblContagiados = new JLabel("Inmunes:");
		lblContagiados.setFont(new Font("Noto Sans", Font.BOLD, 17));
		GridBagConstraints gbc_lblContagiados = new GridBagConstraints();
		gbc_lblContagiados.anchor = GridBagConstraints.EAST;
		gbc_lblContagiados.insets = new Insets(0, 0, 5, 5);
		gbc_lblContagiados.gridx = 0;
		gbc_lblContagiados.gridy = 3;
		panel_informacion.add(lblContagiados, gbc_lblContagiados);

		JLabel lblMuertos_1 = new JLabel("Muertos");
		lblMuertos_1.setFont(new Font("Noto Sans", Font.BOLD, 17));
		GridBagConstraints gbc_lblMuertos_1 = new GridBagConstraints();
		gbc_lblMuertos_1.anchor = GridBagConstraints.EAST;
		gbc_lblMuertos_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblMuertos_1.gridx = 0;
		gbc_lblMuertos_1.gridy = 4;
		panel_informacion.add(lblMuertos_1, gbc_lblMuertos_1);

		btnEmpezar = new JButton("Empezar");
		btnEmpezar.setEnabled(false);

		btnEmpezar.setFont(new Font("Dialog", Font.BOLD, 22));
		panel_simulacion.add(btnEmpezar, "cell 0 2,growx,aligny center");

		btnParar = new JButton("Parar");
		btnParar.setEnabled(false);
		btnParar.setFont(new Font("Dialog", Font.BOLD, 22));
		panel_simulacion.add(btnParar, "cell 0 3,growx,aligny center");

		panel_poblacion = new JPanel();
		frame.getContentPane().add(panel_poblacion, BorderLayout.CENTER);
		GridBagLayout gbl_panel_poblacion = new GridBagLayout();
		gbl_panel_poblacion.columnWidths = new int[] { 0 };
		gbl_panel_poblacion.rowHeights = new int[] { 0 };
		gbl_panel_poblacion.columnWeights = new double[] { Double.MIN_VALUE };
		gbl_panel_poblacion.rowWeights = new double[] { Double.MIN_VALUE };
		panel_poblacion.setLayout(gbl_panel_poblacion);
	}
}
