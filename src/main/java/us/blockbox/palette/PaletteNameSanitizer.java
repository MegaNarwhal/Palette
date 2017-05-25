package us.blockbox.palette;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PaletteNameSanitizer implements StringSanitizer{
	private static final Matcher spaceMatcher;

	static{
		final Pattern space = Pattern.compile(" ");
		spaceMatcher = space.matcher("");
	}

	@Override
	public String sanitize(String s){
		return ChatColor.stripColor(spaceMatcher.reset(s).replaceAll("").toLowerCase());
	}
}
