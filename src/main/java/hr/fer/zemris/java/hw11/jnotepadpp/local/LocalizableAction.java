package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class LocalizableAction extends AbstractAction {

	String key;
	ILocalizationProvider lp;

	public LocalizableAction(String key, ILocalizationProvider lp) {
		super();
		this.key = key;
		this.lp = lp;

		putValue(NAME, lp.getString(key));
		lp.addLocalizationListner(() -> {
			putValue(NAME, lp.getString(key));
		});
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
