package us.blockbox.palette;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockbox.palette.api.Palette;
import us.blockbox.uilib.api.AbstractItem;

public class PaletteChooser extends AbstractItem{
	private final Palette palette;

	PaletteChooser(String name,String id,String description,ItemStack stack,Palette palette){
		super(name,id,description,stack);
		this.palette = palette;
	}

	@Override
	public boolean select(Player player,ClickType clickType){
		palette.equip(player);
		player.playSound(player.getLocation(),Sound.ITEM_ARMOR_EQUIP_LEATHER,2F,1.5F);
//		player.sendMessage(ChatColor.GRAY + "You selected " + palette.getName() + ChatColor.GRAY + ".");
		return true;
	}
}
