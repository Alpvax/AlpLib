package alpvax.util.map;

import java.util.List;

public interface IAliasedItem<T>
{	
	public String[] getAllAliases();
	
	public String getKey();
	
	public List<String> getAliases();
	
	public void removeAlias(String alias);
	
	public void addAlias(String alias);
	
	public T getItem();
}
/* Sample Implementation
public interface IAliasedItem
{
	private final T wrappedItem;
	/** The list of all valid aliases /
	private final String[] allAliases;
	/** The list of active aliases to avoid conflicts /
	private List<String> dynamicAliases = new ArrayList<>();
	
	public AliasedItem(T item, String... aliases)
	{
		wrappedItem = item;
		for(String alias : aliases)
		{
			if(alias != null && alias.length() > 0)
			{
				dynamicAliases.add(alias);
			}
		}
		allAliases = dynamicAliases.toArray(new String[dynamicAliases.size()]);
	}
	
	public String[] getAllAliases()
	{
		return allAliases;
	}
	
	public List<String> getAliases()
	{
		return dynamicAliases;
	}
	
	public void removeAlias(String alias)
	{
		dynamicAliases.remove(alias);
	}
	
	public void addAlias(String alias)
	{
		if(!dynamicAliases.contains(alias))
		{
			dynamicAliases.add(alias);
		}
	}
	
	public T getItem()
	{
		return wrappedItem;
	}
}
*/
