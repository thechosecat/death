package cat.death.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuiEvent extends Event {
    private static final HandlerList HANDLER = new HandlerList();

    Player player;

    public GuiEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER;
    }

    public static HandlerList getHandlerList() {
        return HANDLER;
    }
}