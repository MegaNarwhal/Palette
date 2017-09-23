package us.blockbox.palette.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Palette{
	String getName();

	ItemStack[] getItemStacks();

	void equip(Player player);

	String getPermission();
}
