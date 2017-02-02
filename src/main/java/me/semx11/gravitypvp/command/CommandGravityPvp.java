package me.semx11.gravitypvp.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.semx11.gravitypvp.GravityPvp;
import me.semx11.gravitypvp.util.GameState;
import me.semx11.gravitypvp.util.Wrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandGravityPvp implements ICommandBase {

    private static final CommandGravityPvp INSTANCE = new CommandGravityPvp();

    private CommandGravityPvp() {
    }

    public static CommandGravityPvp getInstance() {
        return INSTANCE;
    }

    @Override
    public String getCommandName() {
        return "gravitypvp";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("gravitypvp.command")) {
            sender.sendMessage(Wrapper.format("&cYou don't have permission!"));
            return true;
        }
        if (args.length < 1) {
            return false;
        }
        switch (args[0]) {
            case "init":
                sender.sendMessage(Wrapper.format("&7[&cGP&7]: &eWaiting for players.."));
                GravityPvp.setGameState(GameState.WAITING);
                break;
            case "start":
                sender.sendMessage(Wrapper.format("&7[&cGP&7]: &eStarting game.."));
                GravityPvp.setGameState(GameState.RUNNING);
                break;
            case "stop":
                sender.sendMessage(Wrapper.format("&7[&cGP&7]: &eStopping game.."));
                GravityPvp.setGameState(GameState.FINISHED);
                break;
            case "players":
                if (args.length < 2) {
                    return false;
                }
                try {
                    GravityPvp.setMaxPlayers(Integer.parseInt(args[1]));
                } catch (NumberFormatException e) {
                    sender.sendMessage(Wrapper.format("&cInvalid number."));
                }
                if (Wrapper.getOnlineCount() >= GravityPvp.getMaxPlayers()) {
                    GravityPvp.setGameState(GameState.STARTING);
                }
                break;
            default:
                sender.sendMessage(Wrapper.format("&cUnknown command."));
                return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
            String[] args) {
        return args.length == 1
                ? Arrays.asList("init", "start", "stop", "players")
                : Collections.emptyList();
    }
}
