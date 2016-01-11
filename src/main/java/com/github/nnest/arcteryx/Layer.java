/**
 * 
 */
package com.github.nnest.arcteryx;

/**
 * Layer
 * 
 * @author brad.wu
 */
public class Layer implements ILayer {
	private String id = null;
	private String parentId = null;

	public Layer(String id) {
		this.id = id;
	}

	public Layer(String id, String parentId) {
		this.id = id;
		this.parentId = parentId;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.ILayer#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.github.nnest.arcteryx.ILayer#getParentId()
	 */
	@Override
	public String getParentId() {
		return this.parentId;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Layer other = (Layer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (parentId == null) {
			if (other.parentId != null)
				return false;
		} else if (!parentId.equals(other.parentId))
			return false;
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Layer [id=" + id + ", parentId=" + parentId + "]";
	}
}
