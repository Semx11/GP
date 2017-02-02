package me.semx11.gravitypvp.scoreboard;

import me.semx11.gravitypvp.GravityPvp;
import me.semx11.gravitypvp.gravity.GravityConstant;
import me.semx11.gravitypvp.util.Wrapper;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardHandler {

    private final Objective objective;

    public ScoreboardHandler(Scoreboard sb) {
        this.objective = sb.registerNewObjective("main", "dummy");
        this.objective.setDisplayName(Wrapper.format("&e&lGRAVITY PVP"));
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.update();
    }

    public void update() {
        ScoreboardBuilder scoreboardBuilder = new ScoreboardBuilder(this.objective).line();

        switch (GravityPvp.getGameState()) {
            case WAITING:
                scoreboardBuilder
                        .add("Players: &a%d/%d", Wrapper.getOnlineCount(),
                                GravityPvp.getMaxPlayers())
                        .line()
                        .add("&fWaiting...");
                break;
            case STARTING:
                scoreboardBuilder
                        .add("&fPlayers: &a%d/%d", Wrapper.getOnlineCount(),
                                GravityPvp.getMaxPlayers())
                        .line()
                        .add("&fStarting in &a%ds", GravityPvp.getInstance().getCountdown());
                break;
            case RUNNING:
                scoreboardBuilder
                        .add("&eChanges each minute!")
                        .line()
                        .add("&eGravity:");

                GravityConstant.getCurrent().entrySet().forEach(
                        entry -> scoreboardBuilder.add("&f%s: &a%s",
                                entry.getKey().getSimpleName(),
                                entry.getValue().toString()));
                break;
            case FINISHED:
                scoreboardBuilder.add("&aThanks for playing!");
                break;
        }

        scoreboardBuilder.line()
                .add("&ewww.example.com")
                .buildAndSet();

    }

}
