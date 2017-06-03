package us.blockbox.palette;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.ItemBuilder;
import us.blockbox.uilib.component.Component;
import us.blockbox.uilib.view.InventoryView;
import us.blockbox.uilib.view.View;

import java.util.Collection;

public class ViewFactory{
	private PaletteManager paletteManager;
	private CachedObject<View> paletteView = new CachedObject<View>(){
		@Override
		protected void validate(){
			final View paletteView = getPaletteView();
			setValue(paletteView);
		}
	};

	public ViewFactory(PaletteManager paletteManager){
		this.paletteManager = paletteManager;
	}

	public View getCachedPaletteView(){
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
}
