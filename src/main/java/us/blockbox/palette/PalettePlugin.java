package us.blockbox.palette;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import us.blockbox.palette.api.Palette;
import us.blockbox.palette.api.PaletteManager;
import us.blockbox.palette.api.StringSanitizer;
import us.blockbox.palette.api.ViewFactory;
import us.blockbox.uilib.viewmanager.ViewManagerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public final class PalettePlugin extends JavaPlugin{

	private static PalettePlugin instance;
	private final StringSanitizer stringSanitizer = new PaletteNameSanitizer();
	private PaletteManagerImpl paletteManager;
	public static final int HOTBAR_LENGTH = 9;
	private final Pattern colon = Pattern.compile(":");
	private ViewFactory viewFactory;

	@Override
	public void onEnable(){
		instance = this;
		saveDefaultConfig();
		paletteManager = new PaletteManagerImpl(loadPalettes(),stringSanitizer);
		final boolean usePermissions = getConfig().getBoolean("usepermissions");
		viewFactory = new ViewFactoryImpl(paletteManager,usePermissions);
		getCommand("palette").setExecutor(new CommandPalette(ViewManagerFactory.getInstance(),viewFactory));
		getCommand("palmg").setExecutor(new CommandManage(instance,stringSanitizer));
	}

	@Override
	public void onDisable(){

	}

	public static PalettePlugin getInstance(){
		return instance;
	}

	public PaletteManager getPaletteManager(){
		return paletteManager;
	}

	private Collection<Palette> loadPalettes(){
		final Logger log = getLogger();
		final FileConfiguration config = getConfig();
		final ConfigurationSection section = config.getConfigurationSection("palettes");
		final Set<String> keys = section.getKeys(false);
		final Collection<Palette> palettes = new ArrayList<>(keys.size());
		log.info("Loading " + keys.size() + " palettes...");
		for(final String key : keys){
			final ConfigurationSection palSection = section.getConfigurationSection(key);
			final String name = ChatColor.translateAlternateColorCodes('&',palSection.getString("name"));
			final ConfigurationSection items = palSection.getConfigurationSection("items");
			final ItemStack[] stacks = parseStacks(log,items);
			palettes.add(new PaletteImpl(name,stringSanitizer.sanitize(name),stacks));
		}
		return palettes;
	}

	private ItemStack[] parseStacks(Logger log,ConfigurationSection items){
		final ItemStack[] stacks = new ItemStack[HOTBAR_LENGTH];
		for(int i = 0; i < HOTBAR_LENGTH; i++){
			final Object item = items.get(String.valueOf(i));
			if(item == null) continue;
			final ItemStack stack;
			if(item instanceof String){
				stack = parseItemString(log,(String)item);
				if(stack == null) continue;
			}else if(item instanceof ItemStack){
				stack = ((ItemStack)item);
			}else{
				log.warning("Item " + i + " is not string or itemstack, skipping.");
				continue;
			}
//				System.out.println(stack);
			stacks[i] = stack;
		}
		return stacks;
	}

	private ItemStack parseItemString(Logger log,String item){
		final ItemStack stack;
		final String[] split = colon.split(item);
		final short data;
		if(split.length > 1){
			data = Short.valueOf(split[1]);
		}else{
			data = 0;
		}
		final Material material = Material.matchMaterial(split[0]);
		if(material == null){
			log.warning("Invalid material " + split[0]);
			return null;
		}
		stack = new ItemStack(material,1,data);
		return stack;
	}

	public boolean addToConfig(String key,Palette palette){
		final String palettesKey = "palettes";
		ConfigurationSection palettes = getConfig().getConfigurationSection(palettesKey);
		if(palettes == null){
			palettes = getConfig().createSection(palettesKey);
		}
		final ConfigurationSection section = palettes.createSection(stringSanitizer.sanitize(key));
		section.set("name",palette.getName());
		final ConfigurationSection items = section.createSection("items");
		final ItemStack[] itemStacks = palette.getItemStacks();
		for(int i = 0; i < itemStacks.length; i++){
			final ItemStack itemStack = itemStacks[i];
			if(itemStack != null){
				if(itemStack.hasItemMeta()){
					section.set(String.valueOf(i),itemStack);
				}else{
					final StringBuilder name = new StringBuilder(itemStack.getType().name());
					final short durability = itemStack.getDurability();
					if(durability != 0){
						name.append(':').append(durability);
					}
					items.set(String.valueOf(i),name.toString());
				}
			}
		}
		saveConfig();
		paletteManager.add(palette);
		return true;
	}

	public boolean removeFromConfig(String key){
		final String palettesKey = "palettes";
		ConfigurationSection palettes = getConfig().getConfigurationSection(palettesKey);
		if(palettes == null){
			palettes = getConfig().createSection(palettesKey);
		}
		if(palettes.isSet(key) && palettes.isConfigurationSection(key)){
			palettes.set(key,null);
			saveConfig();
			paletteManager.remove(key);
			return true;
		}
		return false;
	}
}
