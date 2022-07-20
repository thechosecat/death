package cat.death;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import cat.death.command.OpenGuiCommand;
import cat.death.event.PlayerDeath;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class Main extends JavaPlugin implements Listener {

    public Mysql SQL;
    public SqlTable data;
    public CoolDown cd;
    public CoolDownD cd2;
    private static Plugin plugin;


    @Override
    public void onEnable() {
        FileConfiguration config = this.getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();
        this.SQL = new Mysql();
        this.cd = new CoolDown();
        this.cd2 = new CoolDownD();
        this.data = new SqlTable(this);

        try {
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().warning("資料庫連接失敗");
        }

        if (SQL.isConnect()){
            Bukkit.getLogger().info("資料庫連接成功");
            data.CrateTable();
            this.getServer().getPluginManager().registerEvents(this, this);
        }
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.GREEN +
                        "\n=========================" +
                        "DeathGenie is online" +
                        "=========================\n");
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.RED +
                        "\n=========================" +
                        "原作:江恩(絕對不是羅莉控!)，修改:笨貓" +
                        "=========================\n");
        plugin = this;

        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new Gui(), this);
        getServer().getPluginCommand("dg").setExecutor(new OpenGuiCommand());
        cd.Cooldown();
        cd2.Cooldown();

    }

    @Override
    public void onDisable() {
        //異部核心
        new BukkitRunnable() {
            @Override
            public void run() {
                SQL.disconnect();
            }

        }.runTaskAsynchronously(plugin);
        //異部核心
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //異部核心
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                data.createplayer(player);
            }

        }.runTaskAsynchronously(plugin);
        //異部核心
    }

    public static Plugin getMainPlugin() {
        return plugin;
    }
}