package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;

/**
 * Class represents action for changing of selected text to same case type.
 * Supported cases are: <br>
 * upper case <br>
 * lower case<br>
 * invert case
 * 
 * @author Mihael
 *
 */
public class CaseChangeAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to {@link DefaultMultipleDocumentModel} where current document is
	 * stored
	 */
	private DefaultMultipleDocumentModel documentModel;
	/**
	 * Flag represents which case change we want
	 */
	private CaseEnum flag;

	/**
	 * Constructor for new font case changer
	 * 
	 * @param documentModel
	 *            - {@link DefaultMultipleDocumentModel}
	 * @param b
	 *            - type of case we want
	 */
	public CaseChangeAction(DefaultMultipleDocumentModel documentModel, CaseEnum b) {
		this.documentModel = Objects.requireNonNull(documentModel);
		this.flag = Objects.requireNonNull(b);
	}

	/**
	 * Method represents action for case changing
	 * 
	 * @param event
	 *            - {@link ActionEvent}
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		JTextArea editor = documentModel.getCurrentDocument().getTextComponent();
		Document doc = editor.getDocument();

		int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());

		int offset = 0;

		if (len != 0) {
			offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
		} else {
			len = doc.getLength();
		}

		try {
			String text = doc.getText(offset, len);
			text = changeCase(text);

			doc.remove(offset, len);
			doc.insertString(offset, text, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method changes value to specified type
	 * 
	 * @param text
	 *            - previous text
	 * @return changed text
	 */
	private String changeCase(String text) {
		char[] chars = text.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];

			if (flag == CaseEnum.UPPER && Character.isLowerCase(c)) {
				chars[i] = Character.toUpperCase(c);
			} else if (flag == CaseEnum.LOWER && Character.isUpperCase(c)) {
				chars[i] = Character.toLowerCase(c);
			} else if (flag == CaseEnum.INVERT) {
				chars[i] = Character.isUpperCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c);
			}
		}

		return new String(chars);
	}
}
