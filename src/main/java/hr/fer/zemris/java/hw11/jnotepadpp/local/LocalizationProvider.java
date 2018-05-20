package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {
	private String language;
	private ResourceBundle bundle;
	private static LocalizationProvider provider = null;
	private final String PACKAGE = "hr\\fer\\zemris\\java\\hw11\\local\\prijevodi";

	private LocalizationProvider() {
		setLanguage("en");
	}

	@Override
	public String getString(String string) {
		return bundle.getString(Objects.requireNonNull(string));
	}

	public static LocalizationProvider getInstance() {
		if (provider == null) {
			provider = new LocalizationProvider();
		}
		return provider;
	}

	public void setLanguage(String language) {
		if (this.language == null || !this.language.equals(language)) {
			System.out.println("TU!");
			System.out.print("Old language " + this.language);
			this.language = Objects.requireNonNull(language);
			System.out.println(",new one " + this.language);
			bundle = ResourceBundle.getBundle(PACKAGE, Locale.forLanguageTag(this.language));
			fire();
		}
	}

	public String getLanguage() {
		return language;
	}

}
