package hr.fer.zemris.java.hw11.jnotepadpp.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.Timer;

import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Class represents status bar for tracking informations about data stored in
 * document
 * 
 * @author Mihael
 *
 */
public class StatusBar extends JToolBar {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Lenght label
	 */
	private JLabel length;
	/**
	 * Line label
	 */
	private JLabel ln;
	/**
	 * Column label
	 */
	private JLabel col;
	/**
	 * Selection label
	 */
	private JLabel sel;
	/**
	 * Integer array with values
	 */
	private int array[];

	/**
	 * Constructor for initializing
	 */
	public StatusBar() {
		length = new JLabel(LocalizationProvider.getInstance().getString("length") + ": 0");
		ln = new JLabel("Ln: 0");
		col = new JLabel("col: 0");
		sel = new JLabel("Sel: 0");
		array = new int[] { 0, 0, 0, 0 };

		initGUI();
	}

	/**
	 * Method sets components to their places
	 */
	private void initGUI() {
		setLayout(new BorderLayout());
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		setBorder(BorderFactory.createEtchedBorder());
		setFloatable(true);
		JPanel panel=new JPanel();
		add(panel,BorderLayout.LINE_START);
		panel.add(length);
		addSeparator();
		panel.add(ln);
		addSeparator();
		panel.add(col);
		addSeparator();
		panel.add(sel);
		addSeparator();

		JLabel clock = new JLabel(format.format(new Date()));
		add(clock, BorderLayout.LINE_END);

		Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clock.setText(format.format(new Date()));
			}
		});
		timer.start();
	}

	/**
	 * Method returns length
	 * 
	 * @return length
	 */
	public int getLength() {
		return array[0];
	}

	/**
	 * Method sets value in length label
	 * 
	 * @param i
	 *            - new value
	 */
	public void setLength(int i) {
		this.length.setText(LocalizationProvider.getInstance().getString("length") + ":" + String.valueOf(i));
		array[0] = i;
	}

	/**
	 * Method returns line
	 * 
	 * @return length
	 */
	public int getLn() {
		return array[1];
	}

	/**
	 * Method sets value in line label
	 * 
	 * @param ln
	 *            - new value
	 */
	public void setLn(int ln) {
		this.ln.setText("Ln : " + String.valueOf(ln));
		array[1] = ln;
	}

	/**
	 * Method returns column
	 * 
	 * @return length
	 */
	public int getCol() {
		return array[2];
	}

	/**
	 * Method sets value in column label
	 * 
	 * @param col
	 *            - new value
	 */
	public void setCol(int col) {
		this.col.setText("Col :" + String.valueOf(col));
		array[2] = col;
	}

	/**
	 * Method returns selection
	 * 
	 * @return length
	 */
	public int getSel() {
		return array[3];
	}

	/**
	 * Method sets value in selection label
	 * 
	 * @param sel
	 *            - new value
	 */
	public void setSel(int sel) {
		this.sel.setText("Sel : " + String.valueOf(sel));
		array[3] = sel;
	}
}
