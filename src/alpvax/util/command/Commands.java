package alpvax.util.command;

import java.lang.reflect.Field;
import java.util.List;

import alpvax.util.map.AliasedMap;

public class Commands
{
	public static final Command list = new Command("list", "ls", "/?")
	{
		@Override
		public String[] handleCommand(CommandGroup current, String... arguments)
		{
			List<Command> list = current.getValidCommands();
			if(list.size() > 0)
			{
				System.out.println("Command:\tAliases:");
				for(Command cmd : list)
				{
					System.out.println(cmd);
				}
			}
			list = current.getInheritedCommands();
			if(list.size() > 0)
			{
				System.out.println("\nInherited commands:\nCommand:\tAliases:");
				for(Command cmd : list)
				{
					System.out.println(cmd);
				}
			}
			return consumeCommand(arguments);
		}
	};
	public static final Command debugAliasList = new Command("cmdMap")
	{
		@Override
		public String[] handleCommand(CommandGroup current, String... arguments)
		{
			try
			{
				Field map1 = AliasedMap.class.getDeclaredField("backingMap");
				map1.setAccessible(true);
				Field map2 = AliasedMap.class.getDeclaredField("aliasesMap");
				map2.setAccessible(true);
				Field commands = CommandGroup.class.getDeclaredField("commands");
				Field uCommands = CommandGroup.class.getDeclaredField("ungroupedCommands");
				commands.setAccessible(true);
				uCommands.setAccessible(true);
				Object instance = commands.get(current);
				Object uInstance = uCommands.get(current);
				System.out.printf("%s:%n", current.getKey());
				System.out.println(map1.get(instance));
				System.out.println(map2.get(instance));
				System.out.println("Ungrouped:");
				System.out.println(map1.get(uInstance));
				System.out.println(map2.get(uInstance));
				return consumeCommand(arguments);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return arguments;
			}
		}
	};
}
