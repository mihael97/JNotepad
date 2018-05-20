package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

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
					LocalizationProvider.getInstance().getString("statistic1")
							+ documentModel.getCurrentDocument().getTextComponent().getText().length()
							+ LocalizationProvider.getInstance().getString("statistic2") + numberOfNonBlanks
							+ LocalizationProvider.getInstance().getString("statistic3")
							+ documentModel.getCurrentDocument().getTextComponent().getLineCount()
							+ LocalizationProvider.getInstance().getString("statistic3"),
					LocalizationProvider.getInstance().getString("statistic4"), JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(frame, LocalizationProvider.getInstance().getString("statistic5"),
					LocalizationProvider.getInstance().getString("statistic"), JOptionPane.INFORMATION_MESSAGE);
		}
	}

}
