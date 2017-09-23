package us.blockbox.palette;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.blockbox.palette.api.StringSanitizer;

import java.util.Locale;

public class CommandManage implements CommandExecutor{
	private final PalettePlugin palettePlugin;
	private final StringSanitizer stringSanitizer;

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
			sender.sendMessage(ChatColor.GRAY + "/palmg <add|remove> ...");
			return true;
		}
		final String sub = args[0].toLowerCase(Locale.US);
		if(sub.equals("add")){
			add(sender,args);
		}
		return true;
	}

	private void add(CommandSender sender,String[] args){
		if(args.length < 2){
			sender.sendMessage("Specify a name.");
			return;
		}
		final StringBuilder sb = new StringBuilder(args.length * 6);
		int i = 1;
		while(true){
			if(i < args.length){
				sb.append(args[i]);
				i++;
				if(i != args.length){
					sb.append(' ');
				}
			}else{
				break;
			}
		}
		if(sender instanceof Player){
			final ItemStack[] contents = ((Player)sender).getInventory().getContents();
			final ItemStack[] stacks = new ItemStack[PalettePlugin.HOTBAR_LENGTH];
			System.arraycopy(contents,0,stacks,0,PalettePlugin.HOTBAR_LENGTH);
			final String key = ChatColor.translateAlternateColorCodes('&',sb.toString());
			palettePlugin.addPaletteToConfig(key,new PaletteImpl(key,stringSanitizer.sanitize(key),stacks));
			sender.sendMessage(ChatColor.GREEN + "Added palette \"" + key + ChatColor.GREEN + "\" to config.");
		}else{
			sender.sendMessage("You must be a player.");
		}
	}
}
