package alpvax.common.util.generics;

import java.util.HashMap;

/**
 * Will Automatically use any object's toString() method to find a key
 */
@SuppressWarnings("serial")
public class StringMap<T> extends HashMap<Object, T>
{
	@Override
	public T put(Object key, T value)
	{
		return super.put(key.toString(), value);
	}
	
	@Override
	public T get(Object key)
	{
		return super.get(key.toString());
	}

	@Override
	public boolean containsKey(Object key)
	{
		return super.containsKey(key.toString());
	}

	@Override
	public T remove(Object key)
	{
		return super.remove(key.toString());
	}
}
