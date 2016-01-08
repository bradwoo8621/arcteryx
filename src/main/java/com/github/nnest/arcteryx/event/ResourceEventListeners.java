/**
 * 
 */
package com.github.nnest.arcteryx.event;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Default resource event support
 * 
 * @author brad.wu
 */
public class ResourceEventListeners implements IResourceEventListeners {
	private final static EventListener[] NULL_ARRAY = {};
	private EventListener[] listeners = NULL_ARRAY;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceEventListeners#addListener(com.github.nnest.arcteryx.event.IResourceEventListener,
	 *      java.lang.Class)
	 */
	public synchronized <T extends IResourceEventListener> void addListener(T listener, Class<T> listenerClass) {
		if (listener == null) {
			return;
		}

		if (!listenerClass.isInstance(listener)) {
			throw new IllegalArgumentException(
					"Listener[" + listener + "] is not one of type [" + listenerClass + "].");
		}

		boolean found = false;
		for (EventListener eventListener : this.listeners) {
			if (eventListener.getListenerClass() == listenerClass && eventListener.getListener() == listener) {
				// already added
				// do nothing
				found = true;
				break;
			}
		}

		if (!found) {
			if (this.listeners == NULL_ARRAY) {
				this.listeners = new EventListener[] { new EventListener(listener, listenerClass) };
			} else {
				EventListener[] newListeners = new EventListener[this.listeners.length + 1];
				System.arraycopy(this.listeners, 0, newListeners, 0, this.listeners.length);
				newListeners[newListeners.length - 1] = new EventListener(listener, listenerClass);
				this.listeners = newListeners;
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceEventListeners#removeListener(com.github.nnest.arcteryx.event.IResourceEventListener,
	 *      java.lang.Class)
	 */
	public synchronized <T extends IResourceEventListener> boolean removeListener(T listener, Class<T> listenerClass) {
		if (listener == null) {
			return false;
		}

		if (!listenerClass.isInstance(listener)) {
			throw new IllegalArgumentException(
					"Listener[" + listener + "] is not one of type [" + listenerClass + "].");
		}

		for (int index = 0, count = this.listeners.length; index < count; index++) {
			EventListener eventListener = this.listeners[index];
			if (eventListener.getListenerClass() == listenerClass && eventListener.getListener() == listener) {
				// found, remove it
				EventListener[] newListeners = new EventListener[this.listeners.length - 1];
				if (index == 0) {
					// the first removed
					System.arraycopy(this.listeners, 1, newListeners, 0, newListeners.length);
				} else if (index == count - 1) {
					// the last removed
					System.arraycopy(this.listeners, 0, newListeners, 0, newListeners.length);
				} else {
					System.arraycopy(this.listeners, 0, newListeners, 0, index);
					if (index < count - 1) {
						System.arraycopy(this.listeners, index + 1, newListeners, index, count - index - 1);
					}
				}
				this.listeners = newListeners;
				return true;
			}
		}
		// not found, return false
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.event.IResourceEventListeners#getListeners(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T extends IResourceEventListener> T[] getListeners(Class<T> listenerClass) {
		EventListener[] listeners = this.listeners;

		List<T> list = new ArrayList<T>();
		for (EventListener listener : listeners) {
			if (listener.getListenerClass() == listenerClass) {
				list.add((T) listener.getListener());
			}
		}
		return list.toArray((T[]) Array.newInstance(listenerClass, list.size()));
	}

	/**
	 * Event listener pair
	 * 
	 * @author brad.wu
	 */
	public static class EventListener {
		private IResourceEventListener listener = null;
		private Class<? extends IResourceEventListener> listenerClass = null;

		public EventListener(IResourceEventListener listener, Class<? extends IResourceEventListener> listenerClass) {
			this.listener = listener;
			this.listenerClass = listenerClass;
		}

		/**
		 * @return the listener
		 */
		public IResourceEventListener getListener() {
			return listener;
		}

		/**
		 * @return the listenerClass
		 */
		public Class<? extends IResourceEventListener> getListenerClass() {
			return listenerClass;
		}
	}
}
