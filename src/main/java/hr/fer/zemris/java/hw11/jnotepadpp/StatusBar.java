package hr.fer.zemris.java.hw11.jnotepadpp;

import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

public class StatusBar extends JToolBar {
	private JLabel length;
	private JLabel ln;
	private JLabel col;
	private JLabel sel;
	private int array[];

	public StatusBar() {
		length = new JLabel("Length: 0");
		ln = new JLabel("Ln: 0");
		col = new JLabel("col: 0");
		sel = new JLabel("Sel: 0");
		array = new int[] { 0, 0, 0, 0 };

		initGUI();
	}

	private void initGUI() {
		setFloatable(true);
		add(length);
		addSeparator();
		add(ln);
		addSeparator();
		add(col);
		addSeparator();
		add(sel);
		addSeparator();

	}

	public int getLength() {
		return array[0];
	}

	public void setLength(int i) {
		this.length.setText("Length : " + String.valueOf(i));
		array[0] = i;
	}

	public int getLn() {
		return array[1];
	}

	public void setLn(int ln) {
		this.ln.setText("Ln : " + String.valueOf(ln));
		array[1] = ln;
	}

	public int getCol() {
		return array[2];
	}

	public void setCol(int col) {
		this.col.setText("Col :" + String.valueOf(col));
		array[2] = col;
	}

	public int getSel() {
		return array[3];
	}

	public void setSel(int sel) {
		this.sel.setText("Sel : " + String.valueOf(sel));
		array[3] = sel;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new StatusBar().setVisible(true);
		});
	}

}
