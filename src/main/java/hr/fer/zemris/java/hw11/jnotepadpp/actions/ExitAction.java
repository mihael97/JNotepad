package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ExitAction extends AbstractAction {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.exit(1);
	}

}
