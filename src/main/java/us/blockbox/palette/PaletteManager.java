package us.blockbox.palette;

import java.util.*;

public class PaletteManager{
	private final Map<String,Palette> palettes;
	private final StringSanitizer stringSanitizer;

	PaletteManager(StringSanitizer stringSanitizer){
		this.stringSanitizer = stringSanitizer;
		this.palettes = new HashMap<>();
	}

	PaletteManager(Collection<Palette> palettes,StringSanitizer stringSanitizer){
		this.palettes = new HashMap<>(palettes.size());
		this.stringSanitizer = stringSanitizer;
		addAll(palettes);
	}

	void addAll(Collection<Palette> palettes){
		for(final Palette palette : palettes){
			add(palette);
		}
	}

	void add(Palette palette){
		this.palettes.put(stringSanitizer.sanitize(palette.getName()),palette);
	}

	boolean remove(String name){
		final String s = stringSanitizer.sanitize(name);
		final boolean removed = palettes.containsKey(s);
		if(removed){
			palettes.remove(s);
			return true;
		}
		return false;
	}

	void clear(){
		palettes.clear();
	}

	public Set<String> getNames(){
		return Collections.unmodifiableSet(palettes.keySet());
	}

	public Palette get(String name){
		return palettes.get(stringSanitizer.sanitize(name));
	}

	public Collection<Palette> getPalettes(){
		return Collections.unmodifiableCollection(palettes.values());
	}
}