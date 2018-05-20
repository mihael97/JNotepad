package hr.fer.zemris.java.hw11.jnotepadpp.components;

import javax.swing.JLabel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Class represents localized {@link JLabel} where label updates it's text after
 * every language change
 * 
 * @author Mihael
 *
 */
public class LocalizedJLabel extends JLabel {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 * 
	 * @param key
	 *            - key for translate catching
	 * @param provider
	 *            - {@link LocalizationProvider}
	 */
	public LocalizedJLabel(String key, ILocalizationProvider provider) {
		setText(provider.getString(key));

		provider.addLocalizationListner(() -> {
			setText(provider.getString(key));
		});
	}
}
