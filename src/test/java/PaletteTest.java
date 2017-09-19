import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import us.blockbox.palette.*;
import us.blockbox.uilib.view.View;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PaletteTest{
	@Test
	public void testPalette(){
		final ItemStack[] stacks = new ItemStack[9];
		for(int i = 0; i < stacks.length; i++){
			stacks[i] = new ItemStack(Material.WOOL,1,((short)i));
		}

		final Palette palette = new PaletteImpl("Test",stacks);
		final Player p = mockPlayerWithInventory("Dummy",UUID.randomUUID());

		final ItemStack[] contentsBefore = p.getInventory().getContents();
		for(int i = 0; i < stacks.length; i++){
			assertNotEquals(stacks[i],contentsBefore[i]);
		}

		palette.equip(p);

		final ItemStack[] contentsAfter = p.getInventory().getContents();
		for(int i = 0; i < stacks.length; i++){
			assertEquals(stacks[i],contentsAfter[i]);
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
		for(int i = 0; i < 9; i++){
			final ItemStack[] itemStacks = new ItemStack[9];
			itemStacks[0] = new ItemStack(Material.WOOL,1,(short)i);
			palettes.add(new PaletteImpl("Palette " + i,itemStacks));
		}
		final PaletteManager paletteManager = new PaletteManager(palettes,new PaletteNameSanitizer());
		final ViewFactory factory = new ViewFactory(paletteManager);
		final View view = factory.getCachedPaletteView();
		System.out.println(view.size());
	}

	private static Player mockPlayerWithInventory(String name,UUID uuid){
		final Player pMock = mock(Player.class);
		when(pMock.getName()).thenReturn(name);
		when(pMock.getUniqueId()).thenReturn(uuid);
		final PlayerInventory invMock = mockInventory();
		when(pMock.getInventory()).thenReturn(invMock);
		return pMock;
	}

	private static PlayerInventory mockInventory(){
		final int invSize = 41;
		final PlayerInventory invMocked = mock(PlayerInventory.class);
		final ItemStack[] contents = new ItemStack[invSize];
		when(invMocked.getContents()).thenReturn(contents.clone());
		doAnswer(new Answer<Void>(){
			@Override
			public Void answer(InvocationOnMock invocationOnMock) throws Throwable{
				final int i = invocationOnMock.getArgument(0);
				contents[i] = invocationOnMock.getArgument(1);
				return null;
			}
		}).when(invMocked).setItem(anyInt(),any(ItemStack.class));

		doAnswer(new Answer<Void>(){
			@Override
			public Void answer(InvocationOnMock invocationOnMock) throws Throwable{
				Arrays.fill(contents,null);
				final ItemStack[] items = invocationOnMock.getArgument(0);
				System.arraycopy(items,0,contents,0,items.length);
				System.out.println(items.length);
				return null;
			}
		}).when(invMocked).setContents(any(ItemStack[].class));
		return invMocked;
	}
}
