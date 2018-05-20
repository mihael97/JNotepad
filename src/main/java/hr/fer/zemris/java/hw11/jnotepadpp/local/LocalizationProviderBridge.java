package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Objects;

public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	private boolean connected;
	private ILocalizationListener listener;
	private ILocalizationProvider parent;

	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = Objects.requireNonNull(parent);
		connected = false;
	}

	@Override
	public String getString(String string) {
		return parent.getString(string);
	}

	public void connect() {
		if (!connected) {
			connected = true;
			listener = () -> {
				fire();
			};
			parent.addLocalizationListner(listener);
		}
	}

	public void disconnect() {
		if (connected) {
			connected = false;
			parent.removeLocalizationListener(listener);
			listener = null;
		}
	}

}
