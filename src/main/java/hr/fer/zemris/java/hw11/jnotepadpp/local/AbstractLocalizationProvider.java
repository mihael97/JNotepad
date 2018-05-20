package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	List<ILocalizationListener> listeners = new ArrayList<>();
	

	@Override
	public void addLocalizationListner(ILocalizationListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	public void fire() {
		listeners.forEach(e -> e.localizationChanged());
	}

}
