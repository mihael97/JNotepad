package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import hr.fer.zemris.java.hw11.jnotepadpp.actions.CaseChangeAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.CaseEnum;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.CloseAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.CopyCutAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.ExitAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.OpenBlankAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.OpenDocumentAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.PasteAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.SaveDocumentAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.StatisticAction;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentListener;
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
public class JNotepadPP extends JFrame implements MultipleDocumentListener, CaretListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to JTabedPane with all active documents
	 */
	private DefaultMultipleDocumentModel documentModel;

	/**
	 * Status bar which indicates current length of file and caret position
	 */
	private StatusBar statusBar;

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
	 * Private action for current document closing
	 */
	private Action closeDocument;

	/**
	 * Private action for statistic tracking
	 */
	private Action statistic;

	/**
	 * Private action for text cutting
	 */
	private Action cutAction;

	/**
	 * Private action for text copying
	 */
	private Action copyAction;

	/**
	 * Private action for text pasting
	 */
	private Action pasteAction;

	/**
	 * Private actions for setting characters to upper case
	 */
	private Action upperCaseChange;

	/**
	 * Private actions for setting characters to lower case
	 */
	private Action lowerCaseChange;

	/**
	 * Private actions for setting characters to invert case
	 */
	private Action invertCaseChange;

	/**
	 * Private action for saving file to other location
	 */
	private Action saveAsChangeAction;

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
	 * Public constructor
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				exitAction.actionPerformed(null);

			}

		});
		setSize(500, 500);
		initGUI();
	}

	/**
	 * Method initializes user interface
	 */
	private void initGUI() {
		setLayout(new BorderLayout());
		// documents = new LinkedHashSet<>();
		documentModel = new DefaultMultipleDocumentModel();
		documentModel.addMultipleDocumentListener(this);

		add((Component) documentModel, BorderLayout.CENTER); // we need to cast to component because we are adding
																// element in BorderLayout but
																// DefaultMultipleDocumentModel already extends
																// JTabedPane
		statusBar = new StatusBar();
		add(statusBar, BorderLayout.PAGE_END);
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
			saveDocumentAction = new SaveDocumentAction(this, documentModel, false);
			exitAction = new ExitAction(documentModel, this);
			openBlankAction = new OpenBlankAction(documentModel);
			closeDocument = new CloseAction(documentModel);
			statistic = new StatisticAction(documentModel, this);
			cutAction = new CopyCutAction(documentModel, true);
			copyAction = new CopyCutAction(documentModel, false);
			pasteAction = new PasteAction(documentModel);
			upperCaseChange = new CaseChangeAction(documentModel, CaseEnum.UPPER);
			lowerCaseChange = new CaseChangeAction(documentModel, CaseEnum.LOWER);
			invertCaseChange = new CaseChangeAction(documentModel, CaseEnum.INVERT);
			saveAsChangeAction = new SaveDocumentAction(this, documentModel, true);
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
		file.add(new JMenuItem(saveAsChangeAction));
		file.add(new JMenuItem(closeDocument));
		file.add(new JMenuItem(exitAction));

		// menu text
		JMenu text = new JMenu("Text");
		menuBar.add(text);

		text.add(new JMenuItem(cutAction));
		text.add(new JMenuItem(copyAction));
		text.add(new JMenuItem(pasteAction));
		text.add(new JMenuItem(statistic));

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

		caseMenu.add(new JMenuItem(upperCaseChange));
		caseMenu.add(new JMenuItem(lowerCaseChange));
		caseMenu.add(new JMenuItem(invertCaseChange));

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
		openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_0);
		openDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to open existing file from disk.");

		saveDocumentAction.putValue(Action.NAME, "Save");
		saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to save current file to disk.");

		saveAsChangeAction.putValue(Action.NAME, "Save As");
		saveAsChangeAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F12"));
		saveAsChangeAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F12);
		saveAsChangeAction.putValue(Action.SHORT_DESCRIPTION, "Used to save current file to different location.");

		exitAction.putValue(Action.NAME, "Exit");
		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control E"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		exitAction.putValue(Action.SHORT_DESCRIPTION, "Exit");

		openBlankAction.putValue(Action.NAME, "New");
		openBlankAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		openBlankAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		openBlankAction.putValue(Action.SHORT_DESCRIPTION, "New blank document");

		closeDocument.putValue(Action.NAME, "Close");
		closeDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
		closeDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		closeDocument.putValue(Action.SHORT_DESCRIPTION, "Current document closing");

		statistic.putValue(Action.NAME, "Statistic");
		statistic.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control T"));
		statistic.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
		statistic.putValue(Action.SHORT_DESCRIPTION, "Prints statistic for current file");

		cutAction.putValue(Action.NAME, "Cut");
		cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		cutAction.putValue(Action.SHORT_DESCRIPTION, "Deletes selected text from file");

		copyAction.putValue(Action.NAME, "Copy");
		copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		copyAction.putValue(Action.SHORT_DESCRIPTION, "Copies selected text from file");

		pasteAction.putValue(Action.NAME, "Paste");
		pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
		pasteAction.putValue(Action.SHORT_DESCRIPTION, "Pastes string from text");

		caseActions();

	}

	/**
	 * Method sets actions for every case(uppercase,lowercase,invert) action
	 */
	private void caseActions() {
		invertCaseChange.putValue(Action.NAME, "Invert");
		invertCaseChange.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		invertCaseChange.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
		invertCaseChange.putValue(Action.SHORT_DESCRIPTION, "Inverts every case");

		upperCaseChange.putValue(Action.NAME, "Uppercase");
		upperCaseChange.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
		upperCaseChange.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		upperCaseChange.putValue(Action.SHORT_DESCRIPTION, "Sets every character to uppercase");

		lowerCaseChange.putValue(Action.NAME, "Lowercase");
		lowerCaseChange.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L O"));
		lowerCaseChange.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L | KeyEvent.VK_O);
		lowerCaseChange.putValue(Action.SHORT_DESCRIPTION, "Sets every character to lowercase");
	}

	/**
	 * Method is called when current document is changed
	 * 
	 * @param previousModel
	 *            - previous version
	 * @param currentModel
	 *            - current version
	 */
	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {

		System.out.println("Current documment!");
		documentRemoved(previousModel);
		documentAdded(currentModel);

		if (previousModel == null && currentModel == null) {
			throw new IllegalArgumentException("Both arguments are null!");
		}

		if (currentModel.getFilePath() != null) {
			setTitle(currentModel.getFilePath().toString());
			documentModel.getSelectedComponent().setName(currentModel.getFilePath().getFileName().toString());
		} else {
			setTitle("");
			documentModel.getSelectedComponent().setName("new");
		}

		calculateBar(currentModel.getTextComponent());
	}

	/**
	 * Method is called when document is added to list of active documents
	 * 
	 * @param model
	 *            - document we added
	 */
	@Override
	public void documentAdded(SingleDocumentModel model) {
		System.out.println("New file");
		model.getTextComponent().addCaretListener(this);
	}

	/**
	 * Method is called when document is removed form list of active documents
	 * 
	 * @param model
	 *            - document we removed
	 */
	@Override
	public void documentRemoved(SingleDocumentModel model) {
		System.out.println("Unregister!");
		model.getTextComponent().removeCaretListener(this);
	}

	/**
	 * Method is called when caret position in document is changed
	 * 
	 * @param event
	 *            - informations about last caret change
	 */
	@Override
	public void caretUpdate(CaretEvent event) {
		System.out.println("TU!");
		calculateBar((JTextArea) event.getSource());
	}

	/**
	 * Method refreshes status bar with updated informations
	 * 
	 * @param source
	 *            - {@link JTextArea}
	 */
	private void calculateBar(JTextArea source) {
		try {
			int line = source.getLineOfOffset(source.getCaretPosition());
			statusBar.setLength(source.getDocument().getLength());
			statusBar.setLn(line + 1);
			statusBar.setCol(source.getCaretPosition() - source.getLineStartOffset(line) + 1);
			statusBar.setSel(Math.abs(source.getCaret().getDot() - source.getCaret().getMark()));

			if (statusBar.getSel() == 0) {
				invertCaseChange.setEnabled(false);
				upperCaseChange.setEnabled(false);
				lowerCaseChange.setEnabled(false);
			} else {
				invertCaseChange.setEnabled(true);
				upperCaseChange.setEnabled(true);
				lowerCaseChange.setEnabled(true);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error during status bar calculting");
		}
	}

}
