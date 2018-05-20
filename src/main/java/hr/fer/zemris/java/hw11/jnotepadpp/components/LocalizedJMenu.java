package hr.fer.zemris.java.hw11.jnotepadpp.components;

import javax.swing.JMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Class represents localized {@link JMenu} where menu name updates it's value
 * after every language change
 * 
 * @author Mihael
 *
 */
public class LocalizedJMenu extends JMenu {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 * 
	 * @param key
	 *            - key for catching menu name
	 * @param provider
	 *            - {@link LocalizationProvider}
	 */
	public LocalizedJMenu(String key, ILocalizationProvider provider) {
		setText(provider.getString(key));

		provider.addLocalizationListner(() -> {
			setText(provider.getString(key));
		});
	}
}
