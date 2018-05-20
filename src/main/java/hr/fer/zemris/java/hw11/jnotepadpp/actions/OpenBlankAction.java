package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;

import hr.fer.zemris.java.hw11.jnotepadpp.interfaces.MultipleDocumentModel;

public class OpenBlankAction extends AbstractAction {

	private MultipleDocumentModel model;

	public OpenBlankAction(MultipleDocumentModel model) {
		this.model = Objects.requireNonNull(model);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		model.createNewDocument();
	}

}
