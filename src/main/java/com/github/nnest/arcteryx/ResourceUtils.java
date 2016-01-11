/**
 * 
 */
package com.github.nnest.arcteryx;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;

/**
 * resource utilities
 * 
 * @author brad.wu
 */
public class ResourceUtils {
	private static MultiKeyMap<String, ILayer> LAYERS = new MultiKeyMap<String, ILayer>();

	/**
	 * redress qualified resource id, remove {@linkplain IResource#SEPARATOR} if
	 * at start or end
	 * 
	 * @param qualifiedId
	 * @return
	 */
	public static String redressQualifiedId(String qualifiedId) {
		if (StringUtils.isEmpty(qualifiedId)) {
			return qualifiedId;
		}
		return StringUtils.removeStart(StringUtils.removeEnd(qualifiedId, IResource.SEPARATOR), IResource.SEPARATOR);
	}

	/**
	 * register resource
	 * 
	 * @param parent
	 * @param child
	 */
	public static void registerResource(IContainer parent, IResource child) {
		IContainer originalParent = child.getContainer();
		if (originalParent == null) {
			child.setContainer(parent);
			parent.registerResource(child);
		} else if (originalParent != parent) {
			originalParent.unregisterResource(child.getId());
			child.setContainer(parent);
			parent.registerResource(child);
		} else {
			// not sure registered or not, do it again
			child.setContainer(parent);
			parent.registerResource(child);
		}
	}

	/**
	 * get layer
	 * 
	 * @param layerId
	 * @param parentLayerId
	 * @return
	 */
	public static ILayer getLayer(String layerId, String parentLayerId) {
		String redressedLayerId = StringUtils.isEmpty(layerId) ? "" : layerId;
		String redressedParentLayerId = StringUtils.isEmpty(parentLayerId) ? "" : parentLayerId;
		
		ILayer layer = LAYERS.get(redressedLayerId, redressedParentLayerId);
		if (layer == null) {
			synchronized (LAYERS) {
				layer = LAYERS.get(redressedLayerId, redressedParentLayerId);
				if (layer == null) {
					layer = new Layer(redressedLayerId, redressedParentLayerId);
					LAYERS.put(redressedLayerId, redressedParentLayerId, layer);
				}
			}
		}
		return layer;
	}
}
