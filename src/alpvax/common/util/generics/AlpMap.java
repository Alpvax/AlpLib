package alpvax.common.util.generics;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class AlpMap<T> extends HashMap<String, T>
{
	private Map<String, String> caseMap = new HashMap<String, String>();
	
	@Override
	public T put(String key, T value)
	{
		caseMap.put(key.toLowerCase(), key);
		return super.put(key, value);
	}
	
	@Override
	public T get(Object key)
	{
		return super.get(getKey(key));
	}

	@Override
	public boolean containsKey(Object key)
	{
		return super.containsKey(getKey(key));
	}

	@Override
	public T remove(Object key)
	{
		return super.remove(getKey(key));
	}
	
	private Object getKey(Object key)
	{
		if(key instanceof CaseInsensitiveString)
		{
			return caseMap.get(((CaseInsensitiveString)key).lcString());
		}
		return key;
	}
	
	public static class CaseInsensitiveString
	{
		private String value;
		
		public CaseInsensitiveString(String s)
		{
			value = s;
		}
		
		@Override
		public boolean equals(Object other)
		{
			if(other == null || !((other instanceof CaseInsensitiveString) || (other instanceof String)))
			{
				return false;
			}
			if(other instanceof String)
			{
				return value.equalsIgnoreCase((String)other);
			}
			else
			{
				return value.equalsIgnoreCase(((CaseInsensitiveString)other).value);
			}
		}
		
		@Override
		public int hashCode()
		{
			return value.toLowerCase().hashCode();
		}
		
		@Override
		public String toString()
		{
			return value;
		}
		
		public String lcString()
		{
			return value.toLowerCase();
		}
	}
}
