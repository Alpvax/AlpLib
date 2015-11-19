package alpvax.util.command;

import java.util.ArrayList;
import java.util.List;

import alpvax.util.map.AliasedMap;

public class CommandGroup extends Command
{	
	private CommandGroup parent;
	private AliasedMap<Command> commands = new AliasedMap<>();
	private AliasedMap<Command> ungroupedCommands = new AliasedMap<>();
	private CommandGroup activeChild = null;
	
	public CommandGroup(String command, String... aliases)
	{
		this(null, command, aliases);
	}
	public CommandGroup(CommandGroup parent, String command, String... aliases)
	{
		super(command, aliases);
		this.parent = parent;
		if(parent == null)
		{
			addUngroupedCommands(Commands.list);//, Commands.debugAliasList);
		}
	}

	/*public CommandGroup getParent()
	{
		return parent;
	}*/
	
	public CommandGroup addCommands(Command... command)
	{
		for(Command c : command)
		{
			if(c instanceof CommandGroup)
			{
				((CommandGroup)c).parent = this;
			}
			commands.add(c);
		}
		return this;
	}

	public CommandGroup addUngroupedCommands(Command... command)
	{
		for(Command c : command)
		{
			ungroupedCommands.add(c);
		}
		return this;
	}

	public List<Command> getValidCommands()
	{
		List<Command> list = new ArrayList<>(commands.getAllItems());
		list.addAll(ungroupedCommands.getAllItems());
		return list;
	}

	public List<Command> getInheritedCommands()
	{
		List<Command> list = new ArrayList<>();
		if(parent != null)
		{
			list.addAll(parent.ungroupedCommands.getAllItems());
			list.addAll(parent.getInheritedCommands());
		}
		return list;
	}
	
	/*private List<String> getValidAliases()
	{
		List<String> list = new ArrayList<>(commands.getAllValidAliases());
		list.addAll(ungroupedCommands.getAllValidAliases());
		return list;
	}
	
	private List<String> getInheritedAliases()
	{
		List<String> list = new ArrayList<>();
		if(parent != null)
		{
			list.addAll(parent.ungroupedCommands.getAllValidAliases());
		}
		return list;
	}*/
	
	private Command getCommand(String alias)
	{
		if(commands.getAllValidAliases().contains(alias))
		{
			return commands.get(alias);
		}
		else if(ungroupedCommands.getAllValidAliases().contains(alias))
		{
			return ungroupedCommands.get(alias);
		}
		else
		{
			return getInheritedCommand(alias);
		}
	}
	
	private Command getInheritedCommand(String alias)
	{
		if(parent == null)
		{
			return null;
		}
		return parent.getCommand(alias);
	}
	
	private String prompt(String format)
	{
		return parent != null ? format.replaceAll("%p", parent.prompt(format)).replaceAll("%g", getKey()) : getKey();
	}
	/**
	 * Returns the complete prompt string as given by the format "%p/%g> "
	 */
	public String getPrompt()
	{
		return getPrompt("%p/%g", "%g> ");
	}
	
	/**
	 * Returns the complete prompt string as given by the format
	 * @param format the string to use to format the prompt. Use %p for the parent prompt (recursive) and %g for this group's prompt
	 * @param groupFormat the string to use to format the prompt. Use %g for this group's key
	 */
	public String getPrompt(String format, String groupFormat)
	{
		String group = groupFormat.replaceAll("%g", getKey());
		return parent != null ? format.replaceAll("%p", parent.prompt(format)).replaceAll("%g", group) : group;
	}
	
	public CommandGroup getCurrentGroup()
	{
		return activeChild == null ? this : activeChild.getCurrentGroup();
	}
	
	public void handleCommand(String... commandStrings)
	{
		if(commandStrings.length < 1)
		{
			return;
		}
		if(commandStrings == handleCommand(this, commandStrings))
		{
			System.err.printf("%sCommand \"%s\" not recognised. Use command \"%s\" to list all valid commands.%n", getPrompt(), commandStrings[0], Commands.list.getKey());
		}
	}
	@Override
	public String[] handleCommand(CommandGroup current, String... arguments)
	{
		if(arguments.length < 1 || current != this)
		{
			return null;
		}
		String command = arguments[0];
		String[] args = consumeCommand(arguments);
		if(command.equalsIgnoreCase("..") && parent != null)
		{
			parent.activeChild = null;
			current = parent;
			return parent.handleCommand(current, args);
		}
		Command c = getCommand(command);
		if(c == null)
		{
			return arguments;
		}
		if(c instanceof CommandGroup)
		{
			current = activeChild = (CommandGroup)c;
		}
		return c.handleCommand(current, args);
	}
}
