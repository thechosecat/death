package cat.death;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CoolDown {
    public static HashMap<UUID, Double> cooldown;

    public static void Cooldown(){
        cooldown =new HashMap<>();
    }

    public static void setCooldown(Player player, int seconds){

        double delay = System.currentTimeMillis() + (seconds * 1000);
        cooldown.put(player.getUniqueId(), delay);
    }

    public static int getCooldown(Player player){
        return Math.toIntExact(Math.round((cooldown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000));
    }

    public static boolean CheckCooldown(Player player){

       if(!cooldown.containsKey(player.getUniqueId()) || cooldown.get(player.getUniqueId()) <= System.currentTimeMillis()){
           return true;
        };

        return false;
    }

}
