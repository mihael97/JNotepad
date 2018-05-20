package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;

public class CopyCutAction extends AbstractAction {

	private MultipleDocumentModel documentModel;
	private boolean flag;

	public CopyCutAction(MultipleDocumentModel model, boolean flag) {
		this.documentModel = Objects.requireNonNull(model);
		this.flag = flag;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (documentModel.getCurrentDocument() != null) {
			JTextArea area = documentModel.getCurrentDocument().getTextComponent();
			Document doc = area.getDocument();

			int len = Math.abs(area.getCaret().getDot() - area.getCaret().getMark());

			if (len == 0)
				return;

			int offset = Math.min(area.getCaret().getDot(), area.getCaret().getMark());

			try {
				if (flag == true) {
					doc.remove(offset, len);
					return;
				}

				StringSelection selection = new StringSelection(doc.getText(offset, len));
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

}
