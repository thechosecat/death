package cat.death;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SqlTable {
    private Main plugin;
    public SqlTable(Main plugin){
        this.plugin = plugin;
    }
    public void CrateTable(){
        PreparedStatement ps;
        try{
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS deathgenie "
            + "(NAME VARCHAR(100),UUID VARCHAR(100), GP INT(100),TOTAL INT(100),ENABLE INT(100),PRIMARY KEY (NAME))");
            ps.executeUpdate();
            Bukkit.getLogger().warning("table 已自動創建成功");
        }catch (SQLException e){
            e.printStackTrace();


        }
    }


    public void createplayer(Player player) {
        try{
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM deathgenie WHERE UUID=?");
            ps.setString(1 , uuid.toString());
            ResultSet results = ps.executeQuery();
            results.next();
            if (!exists(uuid)){
                PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO deathgenie" + "(NAME,UUID,GP,TOTAL) VALUES (?,?,"+ plugin.getConfig().getString("DEF_time")+ ",0,0)");
                ps2.setString(1, player.getName());
                ps2.setString(2, uuid.toString());
                ps2.executeUpdate();

                return;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean exists(UUID uuid){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT *FROM deathgenie  WHERE UUID=?");
            ps.setString(1 , uuid.toString());
            ResultSet results = ps.executeQuery();
            if (results.next()){
                // player is found
                return true;
            }
            return  false;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public void adddeathgenie(UUID uuid, int gp) {
        try{
             PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE deathgenie SET GP=? WHERE UUID=?");
             ps.setInt(1, (getdeathgenie(uuid) + gp));
             ps.setString(2, uuid.toString());
             ps.executeUpdate();
        }catch (SQLException e){
        e.printStackTrace();
        }

    }

    public int getdeathgenie(UUID uuid) {
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT GP FROM deathgenie WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs =ps.executeQuery();
            int gp = 0;
            if (rs.next()){
                gp = rs.getInt("GP");
                return gp;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    public int getTOTAL(UUID uuid) {
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT TOTAL FROM deathgenie WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs =ps.executeQuery();
            int TOTAL = 0;
            if (rs.next()){
                TOTAL = rs.getInt("TOTAL");
                return TOTAL;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    public void setTotal(UUID uuid,int total) throws SQLException {
        PreparedStatement ps3 = plugin.SQL.getConnection().prepareStatement("UPDATE deathgenie SET TOTAL=? WHERE UUID=?");
        ps3.setInt(1, (getTOTAL(uuid) + total));
        ps3.setString(2, uuid.toString());
        ps3.executeUpdate();
    }

    public int getEnable(UUID uuid) {
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT ENABLE FROM deathgenie WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs =ps.executeQuery();
            int ENABLE = 0;
            if (rs.next()){
                ENABLE = rs.getInt("ENABLE");
                return ENABLE;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void setEnable(UUID uuid,int ENABLE) {
        new BukkitRunnable() {
            @Override
            public void run() {
                PreparedStatement ps3 = null;
                try {
                    ps3 = plugin.SQL.getConnection().prepareStatement("UPDATE deathgenie SET ENABLE=? WHERE UUID=?");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    ps3.setInt(1, (getEnable(uuid) + ENABLE));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    ps3.setString(2, uuid.toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    ps3.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }.runTaskAsynchronously(plugin);

    }

    }

