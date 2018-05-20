package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Class implements provider which provides us ability for easily language
 * changing inside application
 * 
 * @author Mihael
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
	/**
	 * Current language
	 */
	private String language;
	/**
	 * Dictionary with translated words
	 */
	private ResourceBundle bundle;
	/**
	 * LocalizationProvider with contains dictionary with translated words
	 */
	private static LocalizationProvider provider = null;
	/**
	 * Path to package with dictionaries
	 */
	private final String PACKAGE = "hr\\fer\\zemris\\java\\hw11\\local\\prijevodi";

	/**
	 * Private constructor. Sets language to english
	 */
	private LocalizationProvider() {
		setLanguage("en");
	}

	/**
	 * Method returns stored word in paire with key
	 * 
	 * @param string
	 *            - key
	 * @return translated word
	 */
	@Override
	public String getString(String string) {
		return bundle.getString(Objects.requireNonNull(string));
	}

	/**
	 * Static method return reference to {@link LocalizationProvider}
	 * 
	 * @return reference to {@link LocalizationProvider}
	 */
	public static LocalizationProvider getInstance() {
		if (provider == null) {
			provider = new LocalizationProvider();
		}
		return provider;
	}

	/**
	 * Method sets new language
	 * 
	 * @param language
	 *            - future current language
	 */
	public void setLanguage(String language) {
		if (this.language == null || !this.language.equals(language)) {
			this.language = Objects.requireNonNull(language);
			bundle = ResourceBundle.getBundle(PACKAGE, Locale.forLanguageTag(this.language));
			fire();
		}
	}

	/**
	 * Method returns current language
	 * 
	 * @return current language
	 */
	public String getLanguage() {
		return language;
	}

}
