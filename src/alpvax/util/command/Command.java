package alpvax.util.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import alpvax.util.map.IAliasedItem;

public abstract class Command implements IAliasedItem<Command>
{
	private final String key;
	/** The list of all valid aliases */
	private final String[] allAliases;
	/** The list of active aliases to avoid conflicts */
	private List<String> dynamicAliases = new ArrayList<>();
	
	public Command(String command, String... aliases)
	{
		key = command;
		for(String alias : aliases)
		{
			if(alias != null && alias.length() > 0)
			{
				dynamicAliases.add(alias);
			}
		}
		allAliases = dynamicAliases.toArray(new String[dynamicAliases.size()]);
	}
	
	public abstract String[] handleCommand(CommandGroup currentGroup, String... arguments);
	
	@Override
	public String getKey()
	{
		return key;
	}

	@Override
	public String[] getAllAliases()
	{
		return allAliases;
	}

	@Override
	public List<String> getAliases()
	{
		return dynamicAliases;
	}

	@Override
	public void removeAlias(String alias)
	{
		dynamicAliases.remove(alias);
	}

	@Override
	public void addAlias(String alias)
	{
		if(!dynamicAliases.contains(alias))
		{
			dynamicAliases.add(alias);
		}
	}

	@Override
	public Command getItem()
	{
		return this;
	}

	public String[] consumeCommand(String... arguments)
	{
		return consumeCommands(1, arguments);
	}
	public String[] consumeCommands(int num, String... arguments)
	{
		return Arrays.copyOfRange(arguments, num, arguments.length);
	}
	
	@Override
	public String toString()
	{
		return new StringBuilder(getKey()).append("\t\t").append(aliasString(", ")).toString();
	}
	
	public String aliasString(CharSequence separator)
	{
		Iterator<String> i = getAliases().iterator();
		if(i.hasNext())
		{
			StringBuilder s = new StringBuilder(i.next());
			while(i.hasNext())
			{
				s.append(separator).append(i.next());
			}
			return s.toString();
		}
		return "";
	}
}
