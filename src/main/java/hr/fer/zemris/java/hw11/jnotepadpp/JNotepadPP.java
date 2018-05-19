package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.hw11.jnotepadpp.actions.ExitAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.OpenDocumentAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.SaveDocumentAction;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.SingleDocumentModel;

/**
 * Public class implements text editor with many features like: <br>
 * saving,copying and text cutting<br>
 * statistics of current text(text length,number of non-space characters
 * etc)<br>
 * 
 * @author Mihael
 *
 */
public class JNotepadPP extends JFrame implements MultipleDocumentListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to JTabedPane with all active documents
	 */
	private MultipleDocumentModel documentModel;

	// ACTION CONSTANTS

	/**
	 * Private action that presents opening program action
	 */
	private Action openDocumentAction;

	/**
	 * Private Action that presents saving program action
	 */
	private Action saveDocumentAction;

	/**
	 * Private action that presents action for program exit
	 */
	private Action exitAction;

	/**
	 * Private action that presents action for new blank document opening
	 */
	private Action openBlankAction;

	/**
	 * Public constructor
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(500, 500);
		initGUI();
	}

	/**
	 * Main program
	 * 
	 * @param args
	 *            - not in use
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JNotepadPP().setVisible(true);
		});
	}

	/**
	 * Method initializes user interface
	 */
	private void initGUI() {
		setLayout(new BorderLayout());
		documentModel = new DefaultMultipleDocumentModel();

		add((Component) documentModel, BorderLayout.CENTER); // we need to cast to component because we are adding
																// element in BorderLayout but
																// DefaultMultipleDocumentModel already extends
																// JTabedPane

		initializeConstants();
		createActions();
		createMenuBar();
	}

	/**
	 * Method initializes actions constants
	 */
	private void initializeConstants() {

		try {
			openDocumentAction = new OpenDocumentAction(this, documentModel);

			saveDocumentAction = new SaveDocumentAction();

			exitAction = new ExitAction();

			openBlankAction = new AbstractAction() {

				/**
				 * serialVersionUID
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					documentModel.createNewDocument();
				}
			};
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Method creates universal menu bar with menus
	 */
	private void createMenuBar() {
		// new menu bar
		JMenuBar menuBar = new JMenuBar();
		add(menuBar, BorderLayout.PAGE_START);

		// menu file
		JMenu file = new JMenu("File");
		menuBar.add(file);
		// JMenuItem openNew = new JMenuItem("New");
		file.add(new JMenuItem(openBlankAction));
		file.add(new JMenuItem(openDocumentAction));
		file.add(new JMenuItem(saveDocumentAction));
		JMenuItem saveAs = new JMenuItem("Save as");
		file.add(saveAs);
		JMenuItem close = new JMenuItem("Close");
		file.add(close);
		file.add(new JMenuItem(exitAction));

		// menu text
		JMenu text = new JMenu("Text");
		menuBar.add(text);

		text.add(new JMenuItem("Cut"));
		JMenuItem copy = new JMenuItem("Copy");
		text.add(copy);
		JMenuItem paste = new JMenuItem("Paste");
		text.add(paste);
		JMenuItem statistic = new JMenuItem("Statistical info");
		text.add(statistic);

		// menu language
		JMenu language = new JMenu("Language");
		menuBar.add(language);

		language.add("en");
		language.add("hr");
		language.add("de");

		toolsMenu(menuBar);

	}

	/**
	 * Method creates tool menu and adds it to Menu bar
	 * 
	 * @param menuBar
	 *            - menu bar
	 */
	private void toolsMenu(JMenuBar menuBar) {
		// menu tools
		JMenu tool = new JMenu("Tools");
		menuBar.add(tool);

		// case submenu
		JMenu caseMenu = new JMenu("Change case");
		tool.add(caseMenu);

		JMenuItem uppercase = new JMenuItem("Uppercase");
		JMenuItem downcase = new JMenuItem("Downcase");
		JMenuItem invert = new JMenuItem("Invert");

		caseMenu.add(uppercase);
		caseMenu.add(downcase);
		caseMenu.add(invert);

		// Sort submenu
		JMenu sort = new JMenu("Sort");
		tool.add(sort);

		JMenuItem ascending = new JMenuItem("Ascending");
		JMenuItem descending = new JMenuItem("Descending");

		sort.add(ascending);
		sort.add(descending);
	}

	/**
	 * Method initializes every actions and sets right properties to them
	 */
	private void createActions() {
		openDocumentAction.putValue(Action.NAME, "Open");
		openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control 0"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_0);
		openDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to open existing file from disk.");

		saveDocumentAction.putValue(Action.NAME, "Save");
		saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to save current file to disk.");

		// deleteSelectedPartAction.putValue(Action.NAME, "Deleted selected text");
		// deleteSelectedPartAction.putValue(Action.ACCELERATOR_KEY,
		// KeyStroke.getKeyStroke("control F2"));
		// deleteSelectedPartAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
		// deleteSelectedPartAction.putValue(Action.SHORT_DESCRIPTION, "Used to delete
		// the selected part of text");
		//
		// toggleCaseAction.putValue(Action.NAME, "Toggle case");
		// toggleCaseAction.putValue(Action.ACCELERATOR_KEY,
		// KeyStroke.getKeyStroke("control F3"));
		// toggleCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
		// toggleCaseAction.putValue(Action.SHORT_DESCRIPTION,
		// "Used to toggle character case in selected part of text in entire
		// document!");

		exitAction.putValue(Action.NAME, "Exit");
		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		exitAction.putValue(Action.SHORT_DESCRIPTION, "Exit");

		openBlankAction.putValue(Action.NAME, "New");
		openBlankAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		openBlankAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		openBlankAction.putValue(Action.SHORT_DESCRIPTION, "New blank document");
	}

	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void documentAdded(SingleDocumentModel model) {

	}

	@Override
	public void documentRemoved(SingleDocumentModel model) {
		// TODO Auto-generated method stub

	}
}
