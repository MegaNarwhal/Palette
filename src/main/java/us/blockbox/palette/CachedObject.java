package us.blockbox.palette;

public abstract class CachedObject<T>{
	private boolean valid;
	private T value;

	public CachedObject(){
		this.value = null;
		this.valid = false;
	}

	public CachedObject(T value){
		this.value = value;
		this.valid = true;
	}

	/**
	 * Sets the stored value. This should be called from inside {@link #validate()}.
	 * @param t
	 */
	protected void setValue(T t){
		this.value = t;
	}

	/**
	 * Gets the stored value of type {@link T} without checking its validity. This is useful for situations where it is desirable for the old value to influence the new value.
	 * @return The stored value of type {@link T}
	 */
	protected final T getDirtyValue(){
		return value;
	}

	/**
	 * Updates the stored value if necessary and returns it. This may be very costly depending on the implementation.
	 * This should never be called from inside {@link #validate()}.
	 * @return The stored value of type {@link T} if it is valid, otherwise the updated value.
	 */
	public final T getValue(){
		if(!valid){
			validate();
			this.valid = true;
		}
		return value;
	}

	/**
	 * Mark the object contained in this CachedObject as invalid. {@link #validate()} will be invoked next time {@link #getValue()} is called.
	 */
	public void invalidate(){
		this.valid = false;
	}

	/**
	 * Validates the value stored in this object. This is called by {@link #getValue()} and should not be called manually in most cases.
	 */
	protected abstract void validate();
}