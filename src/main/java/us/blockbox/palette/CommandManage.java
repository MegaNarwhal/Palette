package us.blockbox.palette;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.blockbox.palette.api.ICommand;
import us.blockbox.palette.api.StringSanitizer;

import java.util.*;

public final class CommandManage implements CommandExecutor{
	private final PalettePlugin palettePlugin;
	private final StringSanitizer stringSanitizer;
	private final Map<String,ICommand> commands = getCommandMap();
	private final ChatColor color = ChatColor.DARK_AQUA;

	CommandManage(PalettePlugin palettePlugin,StringSanitizer stringSanitizer){
		this.palettePlugin = palettePlugin;
		this.stringSanitizer = stringSanitizer;
	}

	@Override
	public boolean onCommand(CommandSender sender,Command command,String label,String[] args){
		if(!sender.hasPermission("palette.manage")){
			sender.sendMessage(ChatColor.GRAY + "You don't have permission.");
			return true;
		}
		if(args.length < 1){
			showHelp(sender);
			return true;
		}
		final String sub = args[0].toLowerCase(Locale.US);
		final ICommand iCommand = commands.get(sub);
		if(iCommand == null){
			showHelp(sender);
			return true;
		}else{
			final List<String> argList = Arrays.asList(args).subList(1,args.length);
			return iCommand.execute(sender,argList);
		}
	}

	private void showHelp(CommandSender sender){
		final Set<String> names = commands.keySet();
		final Iterator<String> iterator = names.iterator();
		if(iterator.hasNext()){
			sender.sendMessage(color + "Subcommands:");
			final StringBuilder sb = commaSeparatedList(new StringBuilder(names.size() * 4),iterator);
			sender.sendMessage(sb.toString());
		}
	}

	private void list(CommandSender sender){
		final List<String> names = Ordering.natural().immutableSortedCopy(palettePlugin.getPaletteManager().getNames());
		final Iterator<String> iterator = names.iterator();
		if(iterator.hasNext()){
			final int size = names.size();
			sender.sendMessage(color + "Showing all palettes (" + size + "):");
			final StringBuilder sb = commaSeparatedList(new StringBuilder(size * 8),iterator);
			sender.sendMessage(sb.toString());
		}else{
			sender.sendMessage(color + "No palettes found.");
		}
	}

	/**
	 * This assumes that the Iterator's {@link Iterator#hasNext()} method will return true at the time this method is
	 * called.
	 */
	private static StringBuilder commaSeparatedList(StringBuilder sb,Iterator<String> iterator){
		while(true){
			final String s = iterator.next();
			sb.append(s);
			if(iterator.hasNext()){
				sb.append(", ");
			}else{
				break;
			}
		}
		return sb;
	}

	private void remove(CommandSender sender,List<String> args){
		if(args.size() < 1){
			sender.sendMessage("Specify a name.");
			return;
		}
		final String name = args.get(0);
		if(palettePlugin.removeFromConfig(name)){
			sender.sendMessage("Removed palette " + name + '.');
		}else{
			sender.sendMessage("No palette found by that name.");
		}
	}

	private void add(CommandSender sender,List<String> args){
		if(args.size() < 1){
			sender.sendMessage("Specify a name.");
			return;
		}
		final StringBuilder sb = concatenateArgs(args);
		if(sender instanceof Player){
			final ItemStack[] contents = ((Player)sender).getInventory().getContents();
			final ItemStack[] stacks = new ItemStack[PalettePlugin.HOTBAR_LENGTH];
			System.arraycopy(contents,0,stacks,0,PalettePlugin.HOTBAR_LENGTH);
			final String key = ChatColor.translateAlternateColorCodes('&',sb.toString());
			palettePlugin.addToConfig(key,new PaletteImpl(key,stringSanitizer.sanitize(key),stacks));
			sender.sendMessage(ChatColor.GREEN + "Added palette \"" + key + ChatColor.GREEN + "\" to config.");
		}else{
			sender.sendMessage("You must be a player.");
		}
	}

	private static StringBuilder concatenateArgs(List<String> args){
		final int size = args.size();
		final StringBuilder sb = new StringBuilder(size * 6);
		int i = 0;
		while(true){
			if(i < size){
				sb.append(args.get(i));
				i++;
				if(i != size){
					sb.append(' ');
				}
			}else{
				break;
			}
		}
		return sb;
	}

	private Map<String,ICommand> getCommandMap(){
		final Map<String,ICommand> map = new LinkedHashMap<>();

		map.put("add",new ICommand(){
			@Override
			public boolean execute(CommandSender sender,List<String> args){
				add(sender,args);
				return true;
			}
		});

		map.put("remove",new ICommand(){
			@Override
			public boolean execute(CommandSender sender,List<String> args){
				remove(sender,args);
				return true;
			}
		});

		map.put("list",new ICommand(){
			@Override
			public boolean execute(CommandSender sender,List<String> args){
				list(sender);
				return true;
			}
		});

		return ImmutableMap.copyOf(map);
	}
}
