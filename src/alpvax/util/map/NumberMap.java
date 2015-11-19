package alpvax.util.map;

import java.util.HashMap;

/**
 * @author Alpvax
 *
 */
public class NumberMap extends HashMap<String, Number>
{
	private static final long serialVersionUID = 4698099594548087939L;

	public int getInt(String key, int _default)
	{
		return containsKey(key) ? get(key).intValue() : _default;
	}

	public float getFloat(String key, float _default)
	{
		return containsKey(key) ? get(key).floatValue() : _default;
	}

	public double getDouble(String key, double _default)
	{
		return containsKey(key) ? get(key).doubleValue() : _default;
	}
}
