package calculadora;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Calculadora2 {
	public static JFrame marco; // Lo creo publico y estatico para poder llamarlo desde cualquier clase
	public static void main(String[] args) {
		marco=new JFrame();
		marco.setResizable(false);
		marco.setVisible(true);
		marco.setTitle("Calculadora");
		marco.setBounds(500, 300, 310, 300); 
		
		// Si el marco no esta terminado antes de instanciar la lamina, entonces no existe y no
		//se puede usar en otra clase
		LaminaCalculadora milamina = new LaminaCalculadora();
		marco.add(milamina);
		
		/* Las siguientes tiene que estar antes de agregar la lamina para que la llamada al marco
		desde otra clase funcione. pero tiene que estar despues para que la ventana aparezca con todos
		sus componentes. Por eso duplico las instrucciones-*/
		marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		marco.setVisible(true);
	}
}

//-----------------CREANDO LA LAMINA PRINCIPAL----------------------------------
class LaminaCalculadora extends JPanel{
	public LaminaCalculadora() {
		
		//---------------IMPORTAR RECURSOS EXTERNOS----------------------
		// no es necesario importar los recusos externos.
		// Si se quiere usar habria que cambiar en los ImageIcon el texto por las siguientes variables
		/* URL ImagenBorrar = LaminaCalculadora.class.getResource("eliminar.png");*/
		setLayout(new BorderLayout(5,5));
		LSuperior = new JPanel();
		LMenu = new JPanel();
		LMenu.setLayout(new GridLayout(0, 2));
		
		//---------Crear menu superior------------------------------
		JMenuBar BarraMenu = new JMenuBar();
		
		//LMenu.add(BarraMenu);
				
		//-------------LaminaSuperior para menu----------------
		LSuperior.setLayout(new GridLayout(2,0));
		add(LSuperior, BorderLayout.NORTH);
		
		//---------------Creacion de la pantalla---------------------
		pantalla = new JLabel("0");
		pantalla.setPreferredSize(new Dimension(250,40));
		pantalla.setFont(fuente);
		pantalla.setForeground(Color.BLUE);
		pantalla.setHorizontalAlignment(SwingConstants.RIGHT);
		pantalla.setVerticalAlignment(SwingConstants.BOTTOM);
		pantalla.setBackground(Color.WHITE);
		
		//-----------Añadimos pantalla y menu a lamina superior----------------
		LSuperior.add(LMenu);
		LSuperior.add (pantalla);
		
		// Creamos Lamina para el teclado----------------------
		laminaTeclado = new JPanel();
		laminaTeclado.setLayout(new GridLayout(6,4,3,3));
		
		//-------------------ACCIONES------------------------------
		ActionListener inserta = new InsertaNumero();
		ActionListener orden = new Operaciones();
		
		//--------------------CREAR BOTONES TECLADO---------------------
		// Llamando al constructor para ahorrar codigo--------------
		ponerBoton("%", orden, 0);
		ponerBoton("√", orden, 0);
		ponerBoton("x²", orden, 0);
		ponerBoton("1/x", orden, 0);
		ponerBoton("CE", orden, 0);
		ponerBoton("C", orden, 0);
		
		// Como borrar lleva un icono lo ponemos fuera del constructor----------
		ImageIcon iconoborrar = new ImageIcon("src/eliminar.png");
		borrar = new JButton(iconoborrar);
		borrar.setAlignmentY(CENTER_ALIGNMENT);
		borrar.addActionListener(orden);
		laminaTeclado.add(borrar);
		
		//este KeyListener es para borrar copiar y pegar
		Calculadora2.marco.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			public void keyPressed(KeyEvent e) {
			}
			
			// Ignoramos los metodos que no necesitamos pero que hay que sobreescribir
			public void keyReleased(KeyEvent e) {
				// Escuchamos la tecla que ha sido presionada y la capturamos en la e
				if(e.getKeyCode() == 8) {
					//Le asignamos a cada boton, ya que estamos en la clase que construye los botones,
					// la tecla correspondiente usando doClick()
					borrar.doClick(8);
				} else if (e.isControlDown() && e.getKeyCode() == 67) {
					Calculadora2.marco.requestFocus();
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(pantalla.getText()), null);
				} else if (e.isControlDown() && e.getKeyCode() == 86) {
					Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
				    try {
						String copiado = (String) t.getTransferData(DataFlavor.stringFlavor);
						try {
							Double.parseDouble(copiado);
							pantalla.setText(copiado);
							
						} catch (NumberFormatException exception) {}
					} catch (UnsupportedFlavorException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				    Calculadora2.marco.requestFocus();
				}
				}
		});
		
		//-------------llamadas a poner boton-------------------------------------------
		ponerBoton("/",orden, 111);
		ponerBoton("7",inserta, 103);
		ponerBoton("8",inserta, 104);
		ponerBoton("9",inserta, 105);
		ponerBoton("x",orden, 106);
		ponerBoton("4",inserta, 100);
		ponerBoton("5",inserta, 101);
		ponerBoton("6",inserta, 102);
		ponerBoton("-",orden, 109);
		ponerBoton("1",inserta, 97);
		ponerBoton("2",inserta, 98);
		ponerBoton("3",inserta, 99);
		ponerBoton("+",orden, 107);
		ponerBoton("±",orden, 0);
		ponerBoton("0",inserta, 96);
		ponerBoton(".",inserta, 110);
		ponerBoton("=", orden, 10);
		
		//-Añadimos la lamina del teclado----------------------------------
		add(laminaTeclado, BorderLayout.CENTER);
	}
	
	//--------------BOTONES-----------------------------
	private void ponerBoton(String rotulo, ActionListener oyente, int tecla) {
		JButton boton = new JButton(rotulo);
		boton.setFont(fuente2);
		boton.setAlignmentY(CENTER_ALIGNMENT);
		
		// Funciona para usar el teclado pero hay que pulsar alt
		//boton.setMnemonic(tecla);	
		
		//-----Le añadimos la accion que le corresponde segun sea el boton-------------
		boton.addActionListener(oyente);
			
		//----Ponemos de oyente al marco para darle las acciones desde el teclado--------
		JFrame ventana = Calculadora2.marco;
		
		ventana.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			public void keyPressed(KeyEvent e) {
			}
			
			// Ignoramos los metodos que no necesitamos pero que hay que sobreescribir
			public void keyReleased(KeyEvent e) {
				// Escuchamos la tecla que ha sido presionada y la capturamos en la e
				if(e.getKeyCode() == tecla) {
					//Le asignamos a cada boton, ya que estamos en la clase que construye los botones,
					// la tecla correspondiente usando doClick()
					boton.doClick(tecla);
				}
			}
		});
		
		// Añadimos cada boton a su lamina-------------------
		laminaTeclado.add(boton);
	}
	
	//--------------------INSERTAR NUMEROS EN PANTALLA-------------------------
	private class InsertaNumero implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			
			// Ponemos en entrada al accion que hemos capturado en la e--------------
			String entrada = e.getActionCommand();
			if (pantalla.getText().equals("0") && entrada.equals(".")) {
				pantalla.setText(pantalla.getText() + entrada);
			}
			else if (pantalla.getText().equals("0") || condicion ) {
				pantalla.setText("");
				pantalla.setText(pantalla.getText() + entrada);
			}
			else {
				pantalla.setText(pantalla.getText() + entrada);
			}
			condicion = false;
			Calculadora2.marco.requestFocus();
		}	
	}
	
	//-----------------ACCIONES--------------------------------
	private class Operaciones implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			operacion = e.getActionCommand();
			BORRAR = (JButton)e.getSource();
			calcular(Double.parseDouble(pantalla.getText()));
		}	
	}
	
	public void  calcular(Double x) {
		if ( operacion.equals("CE")) {
			pantalla.setText("0");
		}
		else if (operacion.equals("C")) {
			resultado = 0;
			pantalla.setText("0");
		}
		
		else if (operacion.equals("±")) {
			resultado = x * -1;
			pantalla.setText(""+ QuitarCeros(Double.toString(resultado)));
		}
		else if (operacion.equals("√")) {
			resultado = Math.sqrt(x);
			pantalla.setText(""+ QuitarCeros(Double.toString(resultado)));
		}
		else if (operacion.equals("%")) {
				resultado = x/100;
				pantalla.setText(""+ QuitarCeros(Double.toString(resultado)));
		}
		else if (operacion.equals("x²")) {
			resultado = x*x;
			pantalla.setText(""+ QuitarCeros(Double.toString(resultado)));
		} 
		else if (operacion.equals("1/x")) {
			resultado = 1/x;
			pantalla.setText(""+ QuitarCeros(Double.toString(resultado)));
		}
		else if (UltimaOperacion.equals("")) {
			UltimaOperacion = operacion;
			resultado = x;
		}
		else if (UltimaOperacion.equals("+")) {
			resultado += x;
			pantalla.setText(""+ QuitarCeros(Double.toString(resultado)));
			UltimaOperacion = operacion;
		}
		else if (UltimaOperacion.equals("-")) {
			resultado -= x;
			pantalla.setText(""+ QuitarCeros(Double.toString(resultado)));
			UltimaOperacion = operacion;
		}
		else if (UltimaOperacion.equals("x")) {
			resultado *= x;
			pantalla.setText(""+ QuitarCeros(Double.toString(resultado)));
			UltimaOperacion = operacion;
		}
		else if (UltimaOperacion.equals("/")) {
			resultado /= x;
			pantalla.setText(""+ QuitarCeros(Double.toString(resultado)));
			UltimaOperacion = operacion;
		}
		if (operacion.equals("=")) {
			UltimaOperacion = "";
		} 
		condicion = true;
		if (BORRAR == borrar) {
			String textoNuevo = pantalla.getText();
			textoNuevo = textoNuevo.substring(0, textoNuevo.length() -1);
			if (textoNuevo == "") {
				pantalla.setText("0");
			}else {
				pantalla.setText(textoNuevo);
			}
			condicion = false;
			Calculadora2.marco.requestFocus();
		}
	}
	
	//----------------------ELIMINAR CEROS SOBRANTES--------------------
	private String QuitarCeros(String numero) {
		for (int i = numero.length()-1 ; i >=0 ;i--) {
			if (numero.charAt(i) == '.') {
				for (int z = numero.length()-1 ; z >=0 ;z--) {
					if (numero.charAt(z) == '0') {
						numero = numero.substring(0, numero.length()-1);
					}else if (!(numero.charAt(z) == '0') && !(numero.charAt(z) == '.')) {
						z = -1; i = -1;
					}else if (numero.charAt(z) == '.') {
						numero = numero.substring(0, numero.length()-1);
						z = -1; i = -1;
					}
				}
			}
		}
		return numero;
	}
	
//------------------------DECLARACION DE VARIABLES---------------------------
	private JPanel laminaTeclado;
	private JPanel LSuperior;
	private JPanel LMenu;
	private JLabel pantalla;
	private JButton borrar;
	private double resultado = 0; 
	private String UltimaOperacion = "";
	private boolean condicion = true;
	private String operacion;
	private JButton BORRAR;
	Font fuente = new Font("Calibri", 1, 30);
	Font fuente2 = new Font("Calibri", 1, 15);
}


