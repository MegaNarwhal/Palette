package us.blockbox.palette;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.blockbox.palette.api.Palette;
import us.blockbox.palette.api.PaletteManager;
import us.blockbox.palette.api.ViewFactory;
import us.blockbox.uilib.api.Component;
import us.blockbox.uilib.api.View;
import us.blockbox.uilib.api.util.ItemBuilder;
import us.blockbox.uilib.view.InventoryView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class ViewFactoryImpl implements ViewFactory{
	private final PaletteManager paletteManager;
	private final boolean usePermissions;
	private final Cache<UUID,View> cache = CacheBuilder.newBuilder().expireAfterAccess(5,TimeUnit.SECONDS).concurrencyLevel(1).build();

	public ViewFactoryImpl(PaletteManager paletteManager,boolean usePermissions){
		this.paletteManager = paletteManager;
		this.usePermissions = usePermissions;
	}

	@Override
	public View get(final Player p){
		try{
			return cache.get(p.getUniqueId(),new Callable<View>(){
				@Override
				public View call() throws Exception{
					return createView(p);
				}
			});
		}catch(ExecutionException e){
			e.printStackTrace();
		}
		return null;
	}

	private View createView(Player p){
		final Collection<Palette> palettes = paletteManager.getPalettes();
		final List<Component> components = new ArrayList<>(palettes.size());
		final ItemStack iconDefault = new ItemStack(Material.BRICK);
		for(final Palette palette : palettes){
			if(!(usePermissions) || p.hasPermission(palette.getPermission())){
				final ItemStack icon = getIcon(iconDefault,palette);
				components.add(new PaletteChooser(palette.getName(),null,null,icon,palette));
			}
		}
		return InventoryView.createPaginated("Palette Chooser",components.toArray(new Component[components.size()]),5);
	}

	private static ItemStack getIcon(ItemStack iconDefault,Palette palette){
		ItemStack icon = iconDefault;
		for(final ItemStack itemStack : palette.getItemStacks()){
			if(itemStack != null){
				icon = itemStack.clone();
				break;
			}
		}
		icon = new ItemBuilder(icon).name(palette.getName()).build();
		return icon;
	}
}
