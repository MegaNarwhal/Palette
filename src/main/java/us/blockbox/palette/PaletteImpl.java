package us.blockbox.palette;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static us.blockbox.palette.PalettePlugin.HOTBAR_LENGTH;

public class PaletteImpl implements Palette{
	private final String name;
	private final ItemStack[] itemStacks;

	public PaletteImpl(String name,ItemStack[] itemStacks){
		Validate.isTrue(itemStacks.length == HOTBAR_LENGTH);
		this.name = name;
		this.itemStacks = itemStacks;
	}

	@Override
	public String getName(){
		return name;
	}

	@Override
	public ItemStack[] getItemStacks(){
		return itemStacks.clone();
	}

	@Override
	@SuppressWarnings("deprecation")
	public void equip(Player player){
		final PlayerInventory inventory = player.getInventory();
		final ItemStack[] contents = inventory.getContents();
		//hotbar is 0-8 or 36-44
		System.arraycopy(itemStacks,0,contents,0,9);
		inventory.setContents(contents);
//		player.updateInventory();
	}
}