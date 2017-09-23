package us.blockbox.palette.api;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface ICommand{
	boolean execute(CommandSender sender,List<String> args);
}
