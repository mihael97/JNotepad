package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

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
 * statistics of current text(text length,number of non-space characters and
 * rows)<br>
 * ascending and descending sorting of lines<br>
 * 
 * etc.
 * 
 * @author Mihael
 *
 */
public class JNotepadPP extends JFrame {// implements MultipleDocumentListener, CaretListener {

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
	private LocalizedJMenu sortMenu;

	/**
	 * Localized {@link JMenu} with function for font case changing
	 */
	private LocalizedJMenu caseMenu;

	/**
	 * Bridge between user and {@link LocalizationProvider}
	 */
	private LocalizationProviderBridge provider;

	/**
	 * Universal caret listener which is set to every text area from documents for
	 * modification tracking
	 */
	private CaretListener listener = new CaretListener() {

		@Override
		public void caretUpdate(CaretEvent e) {
			calculateBar(((JTextArea) e.getSource()));
		}
	};

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
	 * Method initializes user interface. User interface is structured in three
	 * parts. First part,on the top of frame,is place where we store our localized
	 * menu bar and tool bar. In center part we set
	 * {@link DefaultMultipleDocumentModel} which contains and shows all active
	 * documents. On the bottom we have status bar where statistic of current active
	 * document is shown also like real-time clock
	 */
	private void initGUI() {
		this.setLayout(new BorderLayout());

		provider = new LocalizationProviderBridge(LocalizationProvider.getInstance());
		provider.connect();

		documentModel = new DefaultMultipleDocumentModel(this);
		documentModel.addMultipleDocumentListener(new MultipleDocumentListener() {
			@Override
			public void documentRemoved(SingleDocumentModel model) {
				model.getTextComponent().removeCaretListener(listener);
				calculateBar(model.getTextComponent());
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {
				model.getTextComponent().addCaretListener(listener);
				calculateBar(model.getTextComponent());
			}

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				if (previousModel == null && currentModel == null) {
					JOptionPane.showMessageDialog(JNotepadPP.this, provider.getString("nullDocuments"),
							provider.getString("error"), JOptionPane.ERROR_MESSAGE);
				}

				setFramePath(currentModel);

				if (previousModel != null) {
					previousModel.getTextComponent().removeCaretListener(listener);
				}

				if (currentModel != null) {
					currentModel.getTextComponent().addCaretListener(listener);
				} else {
					statusBar.setLength(0);
					statusBar.setCol(0);
					statusBar.setLn(0);
					return;
				}

				calculateBar(currentModel.getTextComponent());
			}
		});
		this.add((Component) documentModel, BorderLayout.CENTER); // we need to cast to component because we are adding
																	// element in BorderLayout but
																	// DefaultMultipleDocumentModel already extends
																	// JTabedPane

		statusBar = new StatusBar();
		this.add(statusBar, BorderLayout.PAGE_END);
		initializeConstants();

		documentActions();
		textAction();
		caseActions();
		sortActions();

		createMenuBar();

	}

	/**
	 * Method initializes actions constants.
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
			JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), provider.getString("error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Method creates universal menu bar with menus and tool bar wihh buttons
	 */
	private void createMenuBar() {
		// new menu bar
		JMenuBar menuBar = new JMenuBar();
		JToolBar toolBar = new JToolBar();

		JPanel panel = new JPanel(new GridLayout(2, 0));
		panel.add(menuBar);
		panel.add(toolBar);
		this.add(panel, BorderLayout.PAGE_START);

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
	 * Method creates tool menu and adds it to Menu bar and tool bar
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

		addItem(upperCaseChange, caseMenu, null);
		addItem(lowerCaseChange, caseMenu, null);
		addItem(invertCaseChange, caseMenu, null);

		// Sort submenu
		sortMenu = new LocalizedJMenu("sort", provider);
		sortMenu.setOpaque(true);
		tool.add(sortMenu);

		toolBar.addSeparator();

		addItem(ascendingAction, sortMenu, null);
		addItem(descendignAction, sortMenu, null);
		addItem(uniqueAction, sortMenu, null);
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
		if (toolBar != null) {
			toolBar.add(new LocalizedJButton(action, provider));
		}
	}

	/**
	 * Method initializes actions for opening,saving,closing and program terminating
	 */
	private void documentActions() {
		setAction(openDocumentAction, KeyStroke.getKeyStroke("control O"), Arrays.asList(KeyEvent.VK_O), "open",
				"open_desc");
		setAction(saveDocumentAction, KeyStroke.getKeyStroke("control S"), Arrays.asList(KeyEvent.VK_S), "save",
				"save_desc");
		setAction(saveAsChangeAction, KeyStroke.getKeyStroke("control shift S"),
				Arrays.asList(KeyEvent.VK_SHIFT, KeyEvent.VK_S), "saveAs", "saveAs_desc");
		setAction(exitAction, KeyStroke.getKeyStroke("control E"), Arrays.asList(KeyEvent.VK_E), "exit", "exit_desc");

		setAction(openBlankAction, KeyStroke.getKeyStroke("control N"), Arrays.asList(KeyEvent.VK_N), "new",
				"new_desc");

		setAction(closeDocument, KeyStroke.getKeyStroke("control shift C"),
				Arrays.asList(KeyEvent.VK_SHIFT, KeyEvent.VK_S), "close", "close_desc");
	}

	/**
	 * Method initializes actions for text editing
	 */
	private void textAction() {

		setAction(cutAction, KeyStroke.getKeyStroke("control X"), Arrays.asList(KeyEvent.VK_X), "cut", "cut_desc");

		setAction(copyAction, KeyStroke.getKeyStroke("control C"), Arrays.asList(KeyEvent.VK_C), "copy", "copy_desc");

		setAction(pasteAction, KeyStroke.getKeyStroke("control V"), Arrays.asList(KeyEvent.VK_V), "paste",
				"paste_desc");

		setAction(statistic, KeyStroke.getKeyStroke("control T"), Arrays.asList(KeyEvent.VK_T), "statistic",
				"statistic_desc");
	}

	/**
	 * Method initializes actions for line sorting
	 */
	private void sortActions() {

		setAction(uniqueAction, KeyStroke.getKeyStroke("control shift U"),
				Arrays.asList(KeyEvent.VK_SHIFT, KeyEvent.VK_U), "unique", "unique_desc");

		setAction(ascendingAction, KeyStroke.getKeyStroke("control shift A"),
				Arrays.asList(KeyEvent.VK_SHIFT, KeyEvent.VK_A), "ascending", "ascending_desc");

		setAction(descendignAction, KeyStroke.getKeyStroke("control shift D"),
				Arrays.asList(KeyEvent.VK_SHIFT, KeyEvent.VK_I), "descending", "descending_desc");
	}

	/**
	 * Method sets actions for every case(uppercase,lowercase,invert) action
	 */
	private void caseActions() {

		setAction(invertCaseChange, KeyStroke.getKeyStroke("control I"), Arrays.asList(KeyEvent.VK_I), "invert",
				"invert_desc");

		setAction(upperCaseChange, KeyStroke.getKeyStroke("control U"), Arrays.asList(KeyEvent.VK_U), "uppercase",
				"uppercase_desc");

		setAction(lowerCaseChange, KeyStroke.getKeyStroke("control M"), Arrays.asList(KeyEvent.VK_M), "lowercase",
				"lowercase_desc");
	}

	/**
	 * Method sets preferences for given action
	 * 
	 * @param action
	 *            - action to which we want add preferences
	 * @param keystroke
	 *            - keystroke combination
	 * @param list
	 *            - list of keys which are use in mnemonic key
	 * @param name
	 *            - key for catching action name
	 * @param descName
	 *            - key for catching action description name
	 */
	private void setAction(Action action, KeyStroke keystroke, List<Integer> list, String name, String descName) {
		action.putValue(Action.NAME, name);
		action.putValue(Action.ACCELERATOR_KEY, keystroke);

		if (list.size() == 1) {
			action.putValue(Action.MNEMONIC_KEY, list.get(0));
		} else if (list.size() == 2) {
			action.putValue(Action.MNEMONIC_KEY, list.get(0) | list.get(1));
		} else {
			throw new IllegalArgumentException("Too many arguments!");
		}
		action.putValue(Action.SHORT_DESCRIPTION, provider.getString(descName));
	}

	/**
	 * Method refreshes status bar with updated informations. Informations are
	 * updated after every caret position change in current active document
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

			updateMenus();
		} catch (Exception e) {
			System.out.println("error!");
		}
	}

	/**
	 * Method sets visibility of sort and case menu. If selected text doesn't
	 * exist,menus are <code>disabled</code>. Otherwise they are
	 * <code>enabled</code>
	 */
	private void updateMenus() {

		if (statusBar.getSel() == 0) {
			sortMenu.setEnabled(false);
			caseMenu.setEnabled(false);
		} else {
			sortMenu.setEnabled(true);
			caseMenu.setEnabled(true);
		}

	}

	/**
	 * Method sets frame title. Frame title is path of current active document. If
	 * document is not stored(document doesn't have path) new title fill be empty(
	 * <code>""</code>)
	 * 
	 * @param model
	 *            - current active document
	 */
	private void setFramePath(SingleDocumentModel model) {
		if (model != null) {
			if (model.getFilePath() != null) {
				this.setTitle(model.getFilePath().toString());
			} else {
				this.setTitle("");
			}
		}
	}

}
