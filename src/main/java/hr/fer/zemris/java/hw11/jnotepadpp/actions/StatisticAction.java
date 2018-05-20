package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

/**
 * Class represents action for tracking document statistic
 * 
 * @author Mihael
 *
 */
public class StatisticAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link MultipleDocumentModel} where current file is stored
	 */
	private MultipleDocumentModel documentModel;
	/**
	 * Frame from where statistic action is called
	 */
	private JFrame frame;

	/**
	 * Reference on bridge with {@link LocalizationProvider}
	 */
	private LocalizationProviderBridge provider;

	/**
	 * Default constructor
	 * 
	 * @param model
	 *            - document model
	 * @param frame
	 *            - frame
	 * @param provider
	 *            - dictionary
	 * 
	 * @throws NullPointerException
	 *             - if any of arguments is null
	 */
	public StatisticAction(MultipleDocumentModel model, JFrame frame, LocalizationProviderBridge provider) {
		this.documentModel = Objects.requireNonNull(model);
		this.frame = Objects.requireNonNull(frame);
		this.provider = Objects.requireNonNull(provider);
	}

	/**
	 * Method implements action for calculating and printing statistic about current
	 * file. Statistic information are: <br>
	 * total size<br>
	 * line and caret position inside line<br>
	 * length of selected text
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (documentModel.getCurrentDocument() != null) {
			int numberOfNonBlanks = 0;

			for (Character c : documentModel.getCurrentDocument().getTextComponent().getText().toCharArray()) {
				if (!Character.isWhitespace(c)) {
					numberOfNonBlanks++;
				}
			}

			JOptionPane.showMessageDialog(frame,
					provider.getString("statistic1")
							+ documentModel.getCurrentDocument().getTextComponent().getText().length()
							+ provider.getString("statistic2") + numberOfNonBlanks + provider.getString("statistic3")
							+ documentModel.getCurrentDocument().getTextComponent().getLineCount()
							+ provider.getString("statistic3"),
					provider.getString("statistic4"), JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(frame, provider.getString("statistic5"), provider.getString("statistic"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

}
