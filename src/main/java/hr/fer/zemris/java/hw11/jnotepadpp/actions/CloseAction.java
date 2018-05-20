package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;

import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;

public class CloseAction extends AbstractAction {

	private MultipleDocumentModel documentModel;
	public CloseAction(MultipleDocumentModel documentModel) {
		this.documentModel=Objects.requireNonNull(documentModel);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		documentModel.closeDocument(documentModel.getCurrentDocument());
	}

}
