package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Class represents action for ascending or descending sort of selected lines in
 * current document
 * 
 * @author Mihael
 *
 */
public class SortAction extends AbstractAction {

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
	 * Flag indicates what type of sorting we want
	 */
	private boolean flag;

	/**
	 * Default constructor. If second argument is <code>true</code> lines will be
	 * sorted ascending,otherwise descending
	 * 
	 * @param documentModel
	 *            - document model
	 * @param flag
	 *            - indicator for way of sorting
	 */
	public SortAction(DefaultMultipleDocumentModel documentModel, boolean flag) {
		this.documentModel = Objects.requireNonNull(documentModel);
		this.flag = flag;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (documentModel.getCurrentDocument() != null) {
			JTextArea area = documentModel.getCurrentDocument().getTextComponent();
			Document doc = area.getDocument();

			// selected text
			int offset = Math.min(area.getCaret().getDot(), area.getCaret().getMark());
			int length = Math.abs(area.getCaret().getDot() - area.getCaret().getMark());

			try {
				offset = area.getLineStartOffset(area.getLineOfOffset(offset));
				length = area.getLineEndOffset(area.getLineOfOffset(length + offset));
				String selectedText = doc.getText(offset, length - offset);
				List<String> text = Arrays.asList(selectedText.split("\\r?\\n"));

				Collator collator = Collator.getInstance(new Locale(LocalizationProvider.getInstance().getLanguage()));

				Collections.sort(text, new Comparator<String>() {

					@Override
					public int compare(String first, String second) {
						if (flag == true) {
							return collator.compare(first, second);
						}

						return collator.compare(second, first);
					}
				});
				int lines = area.getLineCount();
				doc.remove(offset, length - offset);

				for (String string : text) {
					doc.insertString(offset, string, null);

					if ((--lines) > 0) {
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

}
