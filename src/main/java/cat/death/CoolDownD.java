package cat.death;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CoolDownD {
    public static HashMap<UUID, Double> cooldownD;

    public static void Cooldown(){
        cooldownD =new HashMap<>();
    }

    public static void setCooldown(Player player, int seconds){

        double delay = System.currentTimeMillis() + (seconds * 1000);
        cooldownD.put(player.getUniqueId(), delay);
    }

    public static int getCooldown(Player player){
        return Math.toIntExact(Math.round((cooldownD.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000));
    }

    public static boolean CheckCooldown(Player player){

        if(!cooldownD.containsKey(player.getUniqueId()) || cooldownD.get(player.getUniqueId()) <= System.currentTimeMillis()){
            return true;
        };

        return false;
    }

}
