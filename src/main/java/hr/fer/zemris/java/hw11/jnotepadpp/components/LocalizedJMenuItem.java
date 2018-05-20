package hr.fer.zemris.java.hw11.jnotepadpp.components;

import javax.swing.Action;
import javax.swing.JMenuItem;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

public class LocalizedJMenuItem extends JMenuItem {
	private String key;

	public LocalizedJMenuItem(Action action, ILocalizationProvider provider) {
		this.key = action.getValue(Action.NAME).toString();
		super.setAction(action);
		System.out.println(key);
		setText(provider.getString(key));
		System.out.println(getText());

		provider.addLocalizationListner(() -> {
			setText(provider.getString(key));
			super.getAction().putValue(Action.SHORT_DESCRIPTION,
					LocalizationProvider.getInstance().getString(key+"_desc"));
		});
	}
}
