package hr.fer.zemris.java.hw11.jnotepadpp.components;

import javax.swing.JLabel;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

public class LocalizedJLabel extends JLabel {
	public LocalizedJLabel(String key, ILocalizationProvider provider) {
		setText(provider.getString(key));

		provider.addLocalizationListner(() -> {
			setText(provider.getString(key));
		});
	}
}
