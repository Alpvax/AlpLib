package alpvax.util.map;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AliasedMap<T>
{
	@SuppressWarnings("serial")
	public class StringList extends ArrayList<String>
	{		
		public StringList(String s)
		{
			add(s);
		}
		
		public String get()
		{
			return valid() ? get(0) : null;
		}
		
		public boolean valid()
		{
			return size() == 1;
		}
	}

	private Map<String, IAliasedItem<T>> backingMap = new HashMap<>();
	private Map<String, StringList> aliasesMap = new HashMap<>();
	
	public boolean add(IAliasedItem<T> item)
	{
		String key = item.getKey();
		if(backingMap.containsKey(key))
		{
			return false;
		}
		//IAliasedItem ai = new AliasedItem(item, aliases);
		backingMap.put(key, item);
		for(String alias : item.getAllAliases())
		{
			StringList list = aliasesMap.get(alias);
			if(list == null)
			{
				list = new StringList(key);
				aliasesMap.put(alias, list);
			}
			else
			{
				list.add(key);
				if(!list.valid())
				{
					for(String s : list)
					{
						backingMap.get(s).removeAlias(alias);
					}
				}
			}
		}
		return true;
	}
	
	/*TODO:Enablepublic void remove(String key)
	{
		IAliasedItem<T> item = backingMap.remove(key);
		for(String alias : item.getAllAliases())
		{
			StringList list = aliasesMap.get(alias);
			list.remove(key);
			if(list.valid())
			{
				for(String s : list)
				{
					backingMap.get(s).addAlias(alias);
				}
			}
		}
	}*/
	
	public T get(String alias)
	{
		if(!backingMap.containsKey(alias))
		{
			StringList list = aliasesMap.get(alias);
			if(list == null)
			{
				throw new NullPointerException("No such alias: \"" + alias + "\".");
			}
			else if(!list.valid())
			{
				throw new InvalidParameterException("Alias Conflict: \"" + alias + "\". Unable to determine correct item out of " + list.size() + " items");
			}
			else
			{
				alias = list.get();
			}
		}
		return backingMap.get(alias).getItem();
	}
	
	public List<String> getAllValidAliases()
	{
		List<String> list = new ArrayList<>();
		for(String alias : backingMap.keySet())
		{
			list.add(alias);
		}
		for(String alias : aliasesMap.keySet())
		{
			if(aliasesMap.get(alias).valid())
			{
				list.add(alias);
			}
		}
		return list;
	}
	
	public List<T> getAllItems()
	{
		List<T> list = new ArrayList<>();
		for(IAliasedItem<T> item : backingMap.values())
		{
			list.add(item.getItem());
		}
		return list;
	}
}
