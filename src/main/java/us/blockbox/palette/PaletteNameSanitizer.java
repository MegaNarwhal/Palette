package us.blockbox.palette;

import org.bukkit.ChatColor;
import us.blockbox.palette.api.StringSanitizer;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaletteNameSanitizer implements StringSanitizer{
	private static final Matcher spaceMatcher;

	static{
		final Pattern space = Pattern.compile(" ");
		spaceMatcher = space.matcher("");
	}

	@Override
	public String sanitize(String s){
		return ChatColor.stripColor(spaceMatcher.reset(s).replaceAll("").toLowerCase(Locale.US));
	}
}
