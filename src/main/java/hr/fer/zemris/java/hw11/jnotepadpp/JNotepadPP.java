package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
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
import hr.fer.zemris.java.hw11.jnotepadpp.actions.SortAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.StatisticAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.UniqueAction;
import hr.fer.zemris.java.hw11.jnotepadpp.components.LocalizedJButton;
import hr.fer.zemris.java.hw11.jnotepadpp.components.LocalizedJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.components.LocalizedJMenuItem;
import hr.fer.zemris.java.hw11.jnotepadpp.components.StatusBar;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

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
	 * Private action for removing same lines
	 */
	private Action uniqueAction;

	/**
	 * Private action for ascending sort of lines
	 */
	private Action ascendingAction;

	/**
	 * Private action for descending sort of lines
	 */
	private Action descendignAction;

	/**
	 * Localized {@link JMenu} with functions for line sorting
	 */
	private LocalizedJMenu sort;

	/**
	 * Localized {@link JMenu} with function for font case changing
	 */
	private LocalizedJMenu caseMenu;

	/**
	 * Bridge between user and {@link LocalizationProvider}
	 */
	private LocalizationProviderBridge provider;

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
		documentModel.addMultipleDocumentListener(this);

		provider = new LocalizationProviderBridge(LocalizationProvider.getInstance());
		provider.connect();

		add((Component) documentModel, BorderLayout.CENTER); // we need to cast to component because we are adding
																// element in BorderLayout but
																// DefaultMultipleDocumentModel already extends
																// JTabedPane
		statusBar = new StatusBar();
		add(statusBar, BorderLayout.PAGE_END);
		initializeConstants();

		documentActions();
		textAction();
		caseActions();
		sortActions();

		createMenuBar();
	}

	/**
	 * Method initializes actions constants
	 */
	private void initializeConstants() {

		try {
			openDocumentAction = new OpenDocumentAction(this, documentModel, provider);
			saveDocumentAction = new SaveDocumentAction(this, documentModel, false, provider);
			exitAction = new ExitAction(documentModel, this, provider);
			openBlankAction = new OpenBlankAction(documentModel);
			closeDocument = new CloseAction(documentModel, this, provider);
			statistic = new StatisticAction(documentModel, this, provider);
			cutAction = new CopyCutAction(documentModel, true);
			copyAction = new CopyCutAction(documentModel, false);
			pasteAction = new PasteAction(documentModel);
			upperCaseChange = new CaseChangeAction(documentModel, CaseEnum.UPPER);
			lowerCaseChange = new CaseChangeAction(documentModel, CaseEnum.LOWER);
			invertCaseChange = new CaseChangeAction(documentModel, CaseEnum.INVERT);
			saveAsChangeAction = new SaveDocumentAction(this, documentModel, true, provider);
			uniqueAction = new UniqueAction(documentModel);
			ascendingAction = new SortAction(documentModel, true);
			descendignAction = new SortAction(documentModel, false);
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
		JToolBar toolBar = new JToolBar();

		JPanel panel = new JPanel(new GridLayout(2, 0));
		panel.add(menuBar);
		panel.add(toolBar);
		add(panel, BorderLayout.PAGE_START);

		// menu file
		LocalizedJMenu file = new LocalizedJMenu("file", provider);
		menuBar.add(file);

		addItem(openBlankAction, file, toolBar);
		addItem(openDocumentAction, file, toolBar);
		addItem(saveDocumentAction, file, toolBar);
		addItem(saveAsChangeAction, file, toolBar);
		addItem(closeDocument, file, toolBar);
		addItem(exitAction, file, toolBar);

		toolBar.addSeparator();

		// menu text
		LocalizedJMenu text = new LocalizedJMenu("text", provider);
		menuBar.add(text);

		addItem(cutAction, text, toolBar);
		addItem(copyAction, text, toolBar);
		addItem(pasteAction, text, toolBar);
		addItem(statistic, text, toolBar);
		text.add(new LocalizedJMenuItem(statistic, provider));

		toolBar.addSeparator();

		// menu language
		LocalizedJMenu language = new LocalizedJMenu("language", provider);
		menuBar.add(language);

		JMenuItem en = new JMenuItem("en");
		en.addActionListener(e -> {
			LocalizationProvider.getInstance().setLanguage("en");
		});
		language.add(en);
		JMenuItem hr = new JMenuItem("hr");
		hr.addActionListener(e -> {
			LocalizationProvider.getInstance().setLanguage("hr");
		});
		language.add(hr);
		JMenuItem de = new JMenuItem("de");
		de.addActionListener(e -> {
			LocalizationProvider.getInstance().setLanguage("de");
		});
		language.add(de);

		toolsMenu(menuBar, toolBar);

	}

	/**
	 * Method creates tool menu and adds it to Menu bar
	 * 
	 * @param menuBar
	 *            - menu bar
	 * @param toolBar
	 *            - tool bar
	 */
	private void toolsMenu(JMenuBar menuBar, JToolBar toolBar) {
		// menu tools
		LocalizedJMenu tool = new LocalizedJMenu("tools", provider);

		menuBar.add(tool);

		// case submenu
		caseMenu = new LocalizedJMenu("case", provider);
		tool.add(caseMenu);

		addItem(upperCaseChange, caseMenu, toolBar);
		addItem(lowerCaseChange, caseMenu, toolBar);
		addItem(invertCaseChange, caseMenu, toolBar);

		// Sort submenu
		sort = new LocalizedJMenu("sort", provider);
		sort.setOpaque(true);
		tool.add(sort);

		toolBar.addSeparator();

		addItem(ascendingAction, sort, toolBar);
		addItem(descendignAction, sort, toolBar);
		addItem(uniqueAction, sort, toolBar);
	}

	/**
	 * Method adds element into menu and tool bar
	 * 
	 * @param action
	 *            - action
	 * @param menu
	 *            - menu
	 * @param toolBar
	 *            - tool bar
	 */
	private void addItem(Action action, LocalizedJMenu menu, JToolBar toolBar) {
		menu.add(new LocalizedJMenuItem(action, provider));
		toolBar.add(new LocalizedJButton(action, provider));
	}

	/**
	 * Method initializes actions for opening,saving,closing and program terminating
	 */
	private void documentActions() {
		openDocumentAction.putValue(Action.NAME, "open");
		openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_0);
		openDocumentAction.putValue(Action.SHORT_DESCRIPTION, provider.getString("open_desc"));

		saveDocumentAction.putValue(Action.NAME, "save");
		saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveDocumentAction.putValue(Action.SHORT_DESCRIPTION, provider.getString("save_desc"));

		saveAsChangeAction.putValue(Action.NAME, "saveAs");
		saveAsChangeAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F12"));
		saveAsChangeAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F12);
		saveAsChangeAction.putValue(Action.SHORT_DESCRIPTION, provider.getString("saveAs_desc"));

		exitAction.putValue(Action.NAME, "exit");
		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control E"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		exitAction.putValue(Action.SHORT_DESCRIPTION, provider.getString("exit_desc"));

		openBlankAction.putValue(Action.NAME, "new");
		openBlankAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		openBlankAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		openBlankAction.putValue(Action.SHORT_DESCRIPTION, provider.getString("new_desc"));

		closeDocument.putValue(Action.NAME, "close");
		closeDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift C"));
		closeDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		closeDocument.putValue(Action.SHORT_DESCRIPTION, provider.getString("close_desc"));
	}

	/**
	 * Method initializes actions for text editing
	 */
	private void textAction() {
		cutAction.putValue(Action.NAME, "cut");
		cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		cutAction.putValue(Action.SHORT_DESCRIPTION, provider.getString("cut_desc"));

		copyAction.putValue(Action.NAME, "copy");
		copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		copyAction.putValue(Action.SHORT_DESCRIPTION, provider.getString("copy_desc"));

		pasteAction.putValue(Action.NAME, "paste");
		pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
		pasteAction.putValue(Action.SHORT_DESCRIPTION, provider.getString("paste_desc"));

		statistic.putValue(Action.NAME, "statistic");
		statistic.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control T"));
		statistic.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
		statistic.putValue(Action.SHORT_DESCRIPTION, provider.getString("statistic_desc"));
	}

	/**
	 * Method initializes actions for line sorting
	 */
	private void sortActions() {
		uniqueAction.putValue(Action.NAME, "unique");
		uniqueAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift U"));
		uniqueAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_SHIFT | KeyEvent.VK_U);
		uniqueAction.putValue(Action.SHORT_DESCRIPTION, provider.getString("unique_desc"));

		ascendingAction.putValue(Action.NAME, "ascending");
		ascendingAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift A"));
		ascendingAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_SHIFT | KeyEvent.VK_A);
		ascendingAction.putValue(Action.SHORT_DESCRIPTION, provider.getString("ascending_desc"));

		descendignAction.putValue(Action.NAME, "descending");
		descendignAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift D"));
		descendignAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_SHIFT | KeyEvent.VK_D);
		descendignAction.putValue(Action.SHORT_DESCRIPTION, provider.getString("descending_desc"));
	}

	/**
	 * Method sets actions for every case(uppercase,lowercase,invert) action
	 */
	private void caseActions() {
		invertCaseChange.putValue(Action.NAME, "invert");
		invertCaseChange.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		invertCaseChange.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
		invertCaseChange.putValue(Action.SHORT_DESCRIPTION, provider.getString("invert_desc"));

		upperCaseChange.putValue(Action.NAME, "uppercase");
		upperCaseChange.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
		upperCaseChange.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		upperCaseChange.putValue(Action.SHORT_DESCRIPTION, provider.getString("uppercase_desc"));

		lowerCaseChange.putValue(Action.NAME, "lowercase");
		lowerCaseChange.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control M"));
		lowerCaseChange.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_M);
		lowerCaseChange.putValue(Action.SHORT_DESCRIPTION, provider.getString("lowercase_desc"));
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

		documentRemoved(previousModel);
		documentAdded(currentModel);

		if (previousModel == null && currentModel == null) {
			throw new IllegalArgumentException(provider.getString("nullarguments"));
		}

		if (currentModel.getFilePath() != null) {
			setTitle(currentModel.getFilePath().toString());
		} else {
			setTitle("");
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
		if (model != null)
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
		if (model != null)
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

			if (statusBar.getSel() == 0 || statusBar.getLength() == 0) {
				sort.setEnabled(false);
				caseMenu.setEnabled(false);
			} else {
				sort.setEnabled(true);
				caseMenu.setEnabled(true);

			}

		} catch (Exception e) {
		}
	}

}
