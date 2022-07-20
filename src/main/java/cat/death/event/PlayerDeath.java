package cat.death.event;

import cat.death.CoolDownD;
import cat.death.Main;
import cat.death.SqlTable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class PlayerDeath implements Listener {
    private final Plugin plugin = Main.getMainPlugin();
    public SqlTable dataM;
    public boolean cat;
    public CoolDownD cd;
    public int times;
    public int ifenable;
    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        this.dataM = new SqlTable((Main) plugin);
        Player player = event.getEntity();
        if(cd.CheckCooldown(player)){

            times = dataM.getdeathgenie(player.getUniqueId());
            ifenable = dataM.getEnable(player.getUniqueId());
            if (ifenable == 1){
                if (times >= 1) {
                    //
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            dataM.adddeathgenie(player.getUniqueId(), -1);
                            try {
                                dataM.setTotal(player.getUniqueId(),1);
                            } catch (SQLException e) {e.printStackTrace();}}
                    }.runTaskAsynchronously(plugin);
                    //

                    event.setKeepInventory(true);
                    event.getDrops().clear();
                    event.setKeepLevel(true);
                    event.setDroppedExp(0);

                } else {
                    player.sendMessage( plugin.getConfig().getString("prefix") + plugin.getConfig().getString("GG"));
                }
            }else {
                player.sendMessage( plugin.getConfig().getString("prefix") + plugin.getConfig().getString("GG_not_enable"));
            }
            cd.setCooldown((player), 5);
        }else{
            player.sendMessage( plugin.getConfig().getString("prefix") +" §f我還在休息拉qwq 我還要 " + cd.getCooldown((player))+ " §f秒才能保護你");
        }

    }


}










