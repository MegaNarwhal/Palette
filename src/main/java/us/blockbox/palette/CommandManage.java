package us.blockbox.palette;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static us.blockbox.palette.PalettePlugin.HOTBAR_LENGTH;

public class CommandManage implements CommandExecutor{
	private PalettePlugin palettePlugin;

	CommandManage(PalettePlugin palettePlugin){
		this.palettePlugin = palettePlugin;
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
		final String sub = args[0].toLowerCase();
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
		final StringBuilder sb = new StringBuilder();
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
			final ItemStack[] stacks = new ItemStack[HOTBAR_LENGTH];
			System.arraycopy(contents,0,stacks,0,HOTBAR_LENGTH);
			final String key = ChatColor.translateAlternateColorCodes('&',sb.toString());
			palettePlugin.addPaletteToConfig(key,new PaletteImpl(key,stacks));
			sender.sendMessage(ChatColor.GREEN + "Added palette \"" + key + ChatColor.GREEN + "\" to config.");
		}else{
			sender.sendMessage("You must be a player.");
		}
	}
}
