package me.semx11.gravitypvp;

import java.util.Arrays;
import java.util.Objects;
import me.semx11.gravitypvp.command.CommandGravityPvp;
import me.semx11.gravitypvp.command.ICommandBase;
import me.semx11.gravitypvp.event.EventFallDamage;
import me.semx11.gravitypvp.event.EventPlayerDeath;
import me.semx11.gravitypvp.event.EventPlayerJoin;
import me.semx11.gravitypvp.event.EventPlayerQuit;
import me.semx11.gravitypvp.gravity.GravityConstant;
import me.semx11.gravitypvp.gravity.GravityHandler;
import me.semx11.gravitypvp.scoreboard.ScoreboardHandler;
import me.semx11.gravitypvp.util.GameState;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class GravityPvp extends JavaPlugin {

    private static GravityPvp INSTANCE;

    private GameState gameState = GameState.WAITING;
    private ScoreboardHandler scoreboardHandler;
    private BukkitTask gravityTask;
    private BukkitTask randomizeTask;
    private BukkitTask endGameTask;
    private BukkitTask countdownTask;
    private int countdown = 5;
    private int maxPlayers = 4;

    public static GravityPvp getInstance() {
        return INSTANCE;
    }

    public static ScoreboardHandler getScoreboard() {
        return INSTANCE.scoreboardHandler;
    }

    public static GameState getGameState() {
        return INSTANCE.gameState;
    }

    public static void setGameState(GameState state) {
        Objects.requireNonNull(state);
        switch (state) {
            case WAITING:
                if (INSTANCE.gameState.equals(GameState.RUNNING)
                        || INSTANCE.gameState.equals(GameState.STARTING)) {
                    INSTANCE.stopGame();
                }
                break;
            case STARTING:
                INSTANCE.startCountdown();
                break;
            case RUNNING:
                INSTANCE.startGame();
                break;
            case FINISHED:
                INSTANCE.stopGame();
                break;
        }
        INSTANCE.gameState = state;
        getScoreboard().update();
    }

    public static int getMaxPlayers() {
        return INSTANCE.maxPlayers;
    }

    public static void setMaxPlayers(int players) {
        INSTANCE.maxPlayers = players;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.scoreboardHandler = new ScoreboardHandler(
                Bukkit.getScoreboardManager().getNewScoreboard());

        this.registerCommands(
                CommandGravityPvp.getInstance());

        this.registerEvents(
                EventPlayerJoin.getInstance(),
                EventPlayerQuit.getInstance(),
                EventPlayerDeath.getInstance(),
                EventFallDamage.getInstance());

        this.getLogger().info("GravityPvP loaded.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("GravityPvP unloaded.");
    }

    private void registerCommands(ICommandBase... commands) {
        Arrays.stream(commands).forEach(command -> {
            this.getCommand(command.getCommandName()).setExecutor(command);
            this.getCommand(command.getCommandName()).setTabCompleter(command);
        });
    }

    private void registerEvents(Listener... events) {
        PluginManager pm = this.getServer().getPluginManager();
        Arrays.stream(events).forEach(event -> pm.registerEvents(event, this));
    }

    private void startCountdown() {
        this.countdown = 5;
        getScoreboard().update();
        this.countdownTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            if (this.countdown > 1) {
                this.countdown--;
                getScoreboard().update();
            } else {
                setGameState(GameState.RUNNING);
                getScoreboard().update();
                this.countdownTask.cancel();
            }
        }, 20, 20);
    }

    private void startGame() {
        this.randomizeTask = Bukkit.getScheduler()
                .runTaskTimerAsynchronously(this, GravityConstant::randomize, 0, 1200);
        this.gravityTask = Bukkit.getScheduler()
                .runTaskTimerAsynchronously(this, new GravityHandler(), 0, 1);
        this.endGameTask = Bukkit.getScheduler()
                .runTaskLaterAsynchronously(this, () -> setGameState(GameState.FINISHED), 6000);
    }

    private void stopGame() {
        if (this.gameState.equals(GameState.RUNNING)) {
            this.randomizeTask.cancel();
            this.gravityTask.cancel();
            this.endGameTask.cancel();
        }
        if (this.gameState.equals(GameState.STARTING)) {
            this.countdownTask.cancel();
        }
    }

    public int getCountdown() {
        return this.countdown;
    }

}
