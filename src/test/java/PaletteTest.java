import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import us.blockbox.palette.*;
import us.blockbox.uilib.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.*;

public class PaletteTest{
	@Test
	public void testPalette(){
		final ItemStack[] stacks = new ItemStack[9];
		for(int i = 0; i < stacks.length; i++){
			stacks[i] = new ItemStack(Material.WOOL,1,((short)i));
		}
		final Palette palette = new PaletteImpl("Test",stacks);
		final Player p = new MockPlayer();
		palette.equip(p);
		final ItemStack[] contents = p.getInventory().getContents();
		for(int i = 0; i < stacks.length; i++){
			assertEquals(stacks[i],contents[i]);
		}
	}

	@Test
	public void testPaletteManager(){
		final Collection<Palette> palettes = new ArrayList<>();
		final PaletteImpl testPalette = new PaletteImpl("T E S T",new ItemStack[9]);
		palettes.add(testPalette);
		final PaletteNameSanitizer stringSanitizer = new PaletteNameSanitizer();
		final PaletteManager paletteManager = new PaletteManager(palettes,stringSanitizer);
		assertEquals(testPalette,paletteManager.get("tes t"));
		assertNull(paletteManager.get("nonexistent"));
		final Set<String> names = paletteManager.getNames();
		assertTrue(names.size() == 1 && names.contains("test"));
	}

	public void testPaletteView(){
		final Collection<Palette> palettes = new ArrayList<>();
		for(int i = 0;i < 9;i++){
			final ItemStack[] itemStacks = new ItemStack[9];
			itemStacks[0] = new ItemStack(Material.WOOL,1,(short)i);
			palettes.add(new PaletteImpl("Palette " + i,itemStacks));
		}
		final PaletteManager paletteManager = new PaletteManager(palettes,new PaletteNameSanitizer());
		final ViewFactory factory = new ViewFactory(paletteManager);
		final View view = factory.getCachedPaletteView();
		System.out.println(view.size());
	}
}
