package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Objects;

/**
 * Class represents provider which is between user and final
 * {@link LocalizationProvider}
 * 
 * @author Mihael
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/**
	 * Status of connection
	 */
	private boolean connected;
	/**
	 * List of listeners
	 */
	private ILocalizationListener listener;
	/**
	 * Reference to parent provider
	 */
	private ILocalizationProvider parent;

	/**
	 * Constructor for new bridge provider initialization
	 * 
	 * @param parent
	 *            - parent provider
	 * 
	 * @throws NullPointerException
	 *             - if argument is <code>null</code>
	 */
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = Objects.requireNonNull(parent);
		connected = false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider#getString(java
	 *      .lang.String)
	 */
	@Override
	public String getString(String string) {
		return parent.getString(string);
	}

	/**
	 * Method connects to parent provider
	 */
	public void connect() {
		if (!connected) {
			connected = true;
			listener = () -> {
				fire();
			};
			parent.addLocalizationListner(listener);
		}
	}

	/**
	 * Method disconnects from parent provider
	 */
	public void disconnect() {
		if (connected) {
			connected = false;
			parent.removeLocalizationListener(listener);
			listener = null;
		}
	}

}
