/**
 * 
 */
package com.github.nnest.arcteryx;

import org.apache.commons.lang3.StringUtils;

/**
 * resource utilities
 * 
 * @author brad.wu
 */
public final class ResourceUtils {
	/**
	 * redress qualified resource id, remove {@linkplain IResource#SEPARATOR} if
	 * at start or end
	 * 
	 * @param qualifiedId
	 * @return
	 */
	public static String redressQualifiedId(String qualifiedId) {
		if (qualifiedId == null || qualifiedId.length() == 0) {
			return qualifiedId;
		}
		return StringUtils.removeStart(StringUtils.removeEnd(qualifiedId, IResource.SEPARATOR), IResource.SEPARATOR);
	}
}
