package us.blockbox.palette;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.ItemBuilder;
import us.blockbox.uilib.ViewManager;
import us.blockbox.uilib.component.Component;
import us.blockbox.uilib.view.InventoryView;
import us.blockbox.uilib.view.View;

import java.util.Collection;

public class CommandPalette implements CommandExecutor{
	private final ViewManager viewManager;
	private PaletteManager paletteManager;
	private CachedObject<View> paletteView = new CachedObject<View>(){
		@Override
		protected void validate(){
			final View paletteView = getPaletteView();
			setValue(paletteView);
		}
	};

	CommandPalette(PaletteManager paletteManager,ViewManager viewManager){
		this.paletteManager = paletteManager;
		this.viewManager = viewManager;
		paletteView.getValue();
	}

	@Override
	public boolean onCommand(CommandSender sender,Command command,String label,String[] args){
		if(sender instanceof Player){
			final View v = getCachedPaletteView();
			viewManager.setView(((Player)sender),v);
		}
		return true;
	}

	private View getCachedPaletteView(){
		return paletteView.getValue();
	}

	private View getPaletteView(){
		final Collection<Palette> palettes = paletteManager.getPalettes();
		final Component[] components = new Component[palettes.size()];
		int i = 0;
		for(final Palette palette : palettes){
			ItemStack icon = new ItemStack(Material.BRICK);
			for(final ItemStack itemStack : palette.getItemStacks()){
				if(itemStack != null){
					icon = itemStack.clone();
					break;
				}
			}
			icon = new ItemBuilder(icon).name(palette.getName()).build();
			components[i] = new PaletteChooser(palette.getName(),null,null,icon,palette);
			i++;
		}
		return InventoryView.createPaginated("Palette Chooser",components,5);
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