package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;

public class StatisticAction extends AbstractAction {

	private MultipleDocumentModel documentModel;
	private JFrame frame;

	public StatisticAction(MultipleDocumentModel model, JFrame frame) {
		this.documentModel = Objects.requireNonNull(model);
		this.frame = Objects.requireNonNull(frame);
	}

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
					"Your document has " + documentModel.getCurrentDocument().getTextComponent().getText().length()
							+ " characters where " + numberOfNonBlanks + " are not whitespaces. Also text has "
							+ documentModel.getCurrentDocument().getTextComponent().getLineCount() + " rows",
					"Statistic", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(frame, "Current document doesn't exist", "Statistic",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

}
