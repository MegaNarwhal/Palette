package us.blockbox.palette;

import us.blockbox.palette.api.Palette;
import us.blockbox.palette.api.PaletteManager;
import us.blockbox.palette.api.StringSanitizer;

import java.util.*;

public class PaletteManagerImpl implements PaletteManager{
	private final Map<String,Palette> palettes;
	private final StringSanitizer stringSanitizer;

	public PaletteManagerImpl(StringSanitizer stringSanitizer){
		this.stringSanitizer = stringSanitizer;
		this.palettes = new HashMap<>();
	}

	public PaletteManagerImpl(Collection<Palette> palettes,StringSanitizer stringSanitizer){
		this.palettes = new HashMap<>(palettes.size());
		this.stringSanitizer = stringSanitizer;
		addAll(palettes);
	}

	@Override
	public void addAll(Collection<Palette> palettes){
		for(final Palette palette : palettes){
			add(palette);
		}
	}

	@Override
	public void add(Palette palette){
		this.palettes.put(stringSanitizer.sanitize(palette.getName()),palette);
	}

	@Override
	public boolean remove(String name){
		final String s = stringSanitizer.sanitize(name);
		final boolean removed = palettes.containsKey(s);
		if(removed){
			palettes.remove(s);
			return true;
		}
		return false;
	}

	@Override
	public void clear(){
		palettes.clear();
	}

	@Override
	public Set<String> getNames(){
		return Collections.unmodifiableSet(palettes.keySet());
	}

	@Override
	public Palette get(String name){
		return palettes.get(stringSanitizer.sanitize(name));
	}

	@Override
	public Collection<Palette> getPalettes(){
		return Collections.unmodifiableCollection(palettes.values());
	}
}
