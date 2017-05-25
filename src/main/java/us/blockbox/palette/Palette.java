package us.blockbox.palette;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Palette{
	String getName();

	ItemStack[] getItemStacks();

	@SuppressWarnings("deprecation")
	void equip(Player player);
}
