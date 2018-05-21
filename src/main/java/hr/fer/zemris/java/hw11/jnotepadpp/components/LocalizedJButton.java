package hr.fer.zemris.java.hw11.jnotepadpp.components;

import javax.swing.Action;
import javax.swing.JButton;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Class represents localized {@link JButton}. After every language
 * change,button translates name to current language
 * 
 * @author Mihael
 *
 */
public class LocalizedJButton extends JButton {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Key for the stored translated word
	 */
	private String key;

	/**
	 * Default constructor
	 * 
	 * @param action
	 *            - button action
	 * @param provider
	 *            - provider
	 */
	public LocalizedJButton(Action action, ILocalizationProvider provider) {
		this.key = action.getValue(Action.NAME).toString();
		super.setAction(action);
		setText(provider.getString(key));

		provider.addLocalizationListner(() -> {
			setText(provider.getString(key));
			super.getAction().putValue(Action.SHORT_DESCRIPTION, provider.getString(key + "_desc"));
		});
	}
}
