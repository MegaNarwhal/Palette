package us.blockbox.palette;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockbox.uilib.ViewManager;
import us.blockbox.uilib.view.View;

public class CommandPalette implements CommandExecutor{
	private final ViewManager viewManager;
	private ViewFactory viewFactory;

	CommandPalette(ViewManager viewManager,ViewFactory viewFactory){
		this.viewManager = viewManager;
		this.viewFactory = viewFactory;
	}

	@Override
	public boolean onCommand(CommandSender sender,Command command,String label,String[] args){
		if(sender instanceof Player){
			final View v = viewFactory.getCachedPaletteView();
			viewManager.setView(((Player)sender),v);
		}else{
			sender.sendMessage("You must be a player.");
		}
		return true;
	}

//		private View getRandomView(){
//		final Component[] components = new Component[4];
//		for(int i = 0;i < components.length;i++){
//			final String name = "Palette " + (i + 1);
//			final ItemStack icon = new ItemBuilder(new ItemStack(Material.GRASS)).name(name).build();
//			final ItemStack[] items = new ItemStack[9];
//			final Material[] mats = Material.values();
//			final Random r = new Random();
//			for(int x = 0;x < items.length;x++){
//				items[x] = new ItemStack(mats[r.nextInt(mats.length)]);
//			}
//			final Palette pal = new Palette(name,items);
//			components[i] = new PaletteChooser(name,"chooser",null,icon,pal);
//		}
//		return InventoryView.createCentered("Palettes",components);
//	}
}