package me.semx11.gravitypvp.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

public interface ICommandBase extends CommandExecutor, TabCompleter {

    String getCommandName();

}
