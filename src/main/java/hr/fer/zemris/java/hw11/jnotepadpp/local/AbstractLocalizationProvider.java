package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that provides {@link LocalizationProvider}'s methods and
 * implementations for operations with listeners
 * 
 * @author Mihael
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	/**
	 * List of all listeners
	 */
	List<ILocalizationListener> listeners = new ArrayList<>();

	/**
	 * Method adds listener to list
	 * 
	 * @param listener
	 *            - new listener
	 */
	@Override
	public void addLocalizationListner(ILocalizationListener listener) {
		listeners.add(listener);
	}

	/**
	 * Method removes listener from list
	 * 
	 * @param listener
	 *            - listener we want to remove
	 */
	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	/**
	 * Method informs every listener when localization change happened
	 */
	public void fire() {
		listeners.forEach(e -> e.localizationChanged());
	}

}
