package hr.fer.zemris.java.hw11.jnotepadpp.components;

import javax.swing.JMenu;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

public class LocalizedJMenu extends JMenu {
	public LocalizedJMenu(String key, ILocalizationProvider provider) {
		setText(provider.getString(key));

		provider.addLocalizationListner(() -> {
			setText(provider.getString(key));
		});
	}
}
