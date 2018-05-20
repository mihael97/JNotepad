package hr.fer.zemris.java.hw11.jnotepadpp.components;

import javax.swing.Action;
import javax.swing.JMenuItem;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Class represents localized {@link JMenuItem} where text updates it's value
 * after every language change
 * 
 * @author Mihael
 *
 */
public class LocalizedJMenuItem extends JMenuItem {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Key for translate catching
	 */
	private String key;

	/**
	 * Public constructor
	 * 
	 * @param action
	 *            - action
	 * @param provider
	 *            - {@link LocalizationProvider}
	 */
	public LocalizedJMenuItem(Action action, ILocalizationProvider provider) {
		this.key = action.getValue(Action.NAME).toString();
		super.setAction(action);
		setText(provider.getString(key));

		provider.addLocalizationListner(() -> {
			setText(provider.getString(key));
			super.getAction().putValue(Action.SHORT_DESCRIPTION, provider.getString(key + "_desc"));
		});
	}
}
