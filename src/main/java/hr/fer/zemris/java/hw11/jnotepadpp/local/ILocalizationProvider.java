package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Interface provides methods every {@link LocalizationProvider} must have
 * 
 * @author Mihael
 *
 */
public interface ILocalizationProvider {
	/**
	 * Method adds localization listener to list of listeners
	 * 
	 * @param listener
	 *            - listener we want to add
	 */
	void addLocalizationListner(ILocalizationListener listener);

	/**
	 * Method removes listener from list of listeners
	 * 
	 * @param listener
	 *            - listener we want to remove
	 */
	void removeLocalizationListener(ILocalizationListener listener);

	/**
	 * Method returns translated word(in current language) which is stored in pair
	 * with key
	 * 
	 * @param string
	 *            - key
	 * @return translated word
	 */
	String getString(String string);
}
