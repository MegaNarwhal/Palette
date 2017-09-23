package us.blockbox.palette.api;

import java.util.Collection;
import java.util.Set;

public interface PaletteManager{
	void addAll(Collection<Palette> palettes);

	void add(Palette palette);

	boolean remove(String name);

	void clear();

	Set<String> getNames();

	Palette get(String name);

	Collection<Palette> getPalettes();
}
