package us.blockbox.palette.api;

import org.bukkit.entity.Player;
import us.blockbox.uilib.api.View;

public interface ViewFactory{
	View get(Player p);
}
