package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;

/**
 * Class represents action for removing same lines form selected text in
 * document
 * 
 * @author Mihael
 *
 */
public class UniqueAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to {@link DefaultMultipleDocumentModel} where current document is
	 * stored
	 */
	DefaultMultipleDocumentModel documentModel;

	/**
	 * Public constructor
	 * 
	 * @param documentModel
	 *            - document model
	 * 
	 * @throws NullPointerException
	 *             - if argument is <code>null</code>
	 */
	public UniqueAction(DefaultMultipleDocumentModel documentModel) {
		this.documentModel = Objects.requireNonNull(documentModel);
	}

	/**
	 * Method performs removing of same lines in selected text
	 * 
	 * @param arg0
	 *            - {@link ActionEvent}
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JTextArea area = documentModel.getCurrentDocument().getTextComponent();
		Document doc = area.getDocument();

		// selected text
		int offset = Math.min(area.getCaret().getDot(), area.getCaret().getMark());
		int length = Math.abs(area.getCaret().getDot() - area.getCaret().getMark());

		try {
			length = area.getLineEndOffset(area.getLineOfOffset(length + offset));
			offset = area.getLineStartOffset(area.getLineOfOffset(offset));
			String text = doc.getText(offset, length - offset);

			Set<String> lines = new LinkedHashSet<>();

			for (String str : Arrays.asList(text.split("\\r?\\n"))) {
				lines.add(str);
			}

			int index = area.getLineCount();
			doc.remove(offset, length - offset);

			for (String string : lines) {
				doc.insertString(offset, string, null);

				--index;
				if (index > 0) {
					doc.insertString(offset + string.length(), "\n", null);
				} else {
					doc.insertString(offset + string.length(), "", null);
				}

				offset += string.length() + 1;
			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
