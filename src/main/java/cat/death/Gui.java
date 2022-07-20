package cat.death;

import cat.death.event.GuiEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.ArrayList;


public class Gui implements Listener {
    private Inventory gui;
    private ItemStack lookUp, increase, enable, decrease;
    public SqlTable dataM;
    public CoolDown cd;

    Plugin plugin = Main.getMainPlugin();
    private int modifyLevel = plugin.getConfig().getInt("respawnXP");

    public void openGui(Player player) {
        initializeItems();

        player.openInventory(gui);
    }

    public void initializeItems() {
        
        // Create a Gui inventory menu and set title
        gui = Bukkit.createInventory(null, 9, plugin.getConfig().getString("GUItitle"));

        lookUp = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta lookUpMeta = lookUp.getItemMeta();

        enable = new ItemStack(Material.FEATHER);
        ItemMeta enableMeta = enable.getItemMeta();



        // Set DisplayName
        enableMeta.setDisplayName(plugin.getConfig().getString("GUIenable"));

        // Set Lore
        ArrayList<String> enableLoreArray = new ArrayList<>();
        enableLoreArray.add("§f防噴小精靈會在你死亡的時候§c保護你的背包後死亡");
        // setup

        enableMeta.setLore(enableLoreArray);
        enable.setItemMeta(enableMeta);

        gui.setItem(0, enable);

        // Set DisplayName
        lookUpMeta.setDisplayName(plugin.getConfig().getString("GUIlookup"));

        // Set Lore
        ArrayList<String> lookUpLoreArray = new ArrayList<>();
        lookUpLoreArray.add("§f查詢目前剩餘的精靈數量，§c購買/解雇§f所§a花費/返回§f之經驗");
        lookUpLoreArray.add("§f防噴小精靈會在你死亡的時候§c保護你的背包後死亡");
        // setup
        lookUpMeta.setLore(lookUpLoreArray);
        lookUp.setItemMeta(lookUpMeta);

        gui.setItem(2, lookUp);
//

//

        increase = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta increaseMeta = increase.getItemMeta();

        // Set DisplayName
        increaseMeta.setDisplayName(plugin.getConfig().getString("GUIbuy"));

        // Set Lore
        ArrayList<String> increaseLoreArray = new ArrayList<>();
        increaseLoreArray.add("");
        increaseLoreArray.add("§f花費自身等級購買防噴小精靈");
        increaseLoreArray.add("§f防噴小精靈會在你死亡的時候§c保護你的背包後死亡");



        // setup
        increaseMeta.setLore(increaseLoreArray);
        increase.setItemMeta(increaseMeta);

        gui.setItem(4, increase);


        decrease = new ItemStack(Material.WITHER_SKELETON_SKULL);
        ItemMeta decreaseMeta = decrease.getItemMeta();

        // Set DisplayName
        decreaseMeta.setDisplayName(ChatColor.RED + "解雇防噴小精靈");

        // Set Lore
        ArrayList<String> decreaseLoreArray = new ArrayList<>();
        decreaseLoreArray.add("");
        decreaseLoreArray.add("§f返回自身等級與防噴小精靈解除契約");
        decreaseLoreArray.add("§f你確定真的要這麼做嗎?");
        decreaseLoreArray.add("§c防噴小精靈他會傷心的...");

        //setup
        decreaseMeta.setLore(decreaseLoreArray);
        decrease.setItemMeta(decreaseMeta);

        gui.setItem(6, decrease);
    }

    @EventHandler
    public void openGuiEvent(GuiEvent event) {
        openGui(event.getPlayer());
    }

    @EventHandler
    public void onPlayerClickEvent(InventoryClickEvent event) throws SQLException, InterruptedException {

        if (!event.getInventory().equals(gui))
            return;
        event.setCancelled(true);
        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        Player player = (Player) event.getWhoClicked();
        //定義Mysql class
        this.dataM = new SqlTable((Main) plugin);

        //異部核心 避免IO卡頓
        new BukkitRunnable() {
            @Override
            public void run() {
                //設定各項變數
                int cat = dataM.getdeathgenie(player.getUniqueId());
                int xp1 = dataM.getTOTAL(player.getUniqueId());
                int ifenable = dataM.getEnable(player.getUniqueId());
                int total = xp1 * plugin.getConfig().getInt("time_Plus_XP") + modifyLevel;
                if (total > (plugin.getConfig().getInt("Max_Buy_XP"))){
                    total = plugin.getConfig().getInt("Max_Buy_XP");
                }
                //設定各項變數

                //menu設定
                switch (event.getSlot()) {
                    case 0:
                        if (!event.getCurrentItem().equals(enable))
                            return;

                        if(cd.CheckCooldown(player)){
                            if(ifenable == 1){
                                dataM.setEnable(player.getUniqueId(),-1);
                                player.sendMessage(plugin.getConfig().getString("prefix") + " §c已關閉防噴小精靈");
                            }

                            if(ifenable == 0){
                                dataM.setEnable(player.getUniqueId(),1);
                                player.sendMessage(plugin.getConfig().getString("prefix") + " §a已開啟防噴小精靈");
                            }
                            cd.setCooldown((player), 3);

                        }else{
                            player.sendMessage(plugin.getConfig().getString("prefix") + " §f此指令還在冷卻中，請等待" + cd.getCooldown((player))+ "§f秒");
                        }

                        break;
                    case 2:
                        if (!event.getCurrentItem().equals(lookUp))
                            return;

                        player.sendMessage(plugin.getConfig().getString("prefix")+ " §f您目前擁有 " + cat + " §f隻防噴小精靈");
                        player.sendMessage(plugin.getConfig().getString("prefix") + " §f目前購買一隻小精靈需要:" + total + "§a經驗等級");
                        player.sendMessage(plugin.getConfig().getString("prefix") + " §f目前解除一隻小精靈將會返回:" + (int)(total * 0.2) + "§a經驗等級");
                        break;
                    case 4:
                        if (!event.getCurrentItem().equals(increase))
                            return;

                        // decrease level
                        if (player.getLevel() >= total) {
                            if(cat >= 10){
                                player.sendMessage(plugin.getConfig().getString("prefix") + " §f您最多只能購買§c10§f隻小精靈");
                            }if(cat < 10){
                                player.setLevel(player.getLevel() - total);
                                player.sendMessage(plugin.getConfig().getString("prefix") + plugin.getConfig().getString("Buy_sues"));
                                dataM.adddeathgenie(player.getUniqueId(), 1);
                                int s1 = cat + 1;
                                player.sendMessage(plugin.getConfig().getString("prefix") + " §f您目前還剩餘" + s1 + "§f隻小精靈");
                            }
                        }
                        else {
                            player.sendMessage(plugin.getConfig().getString("prefix") + plugin.getConfig().getString("Buy_fail"));
                        }


                        break;
                    case 6:
                        if (!event.getCurrentItem().equals(decrease))
                            return;

                        // increase level
                        if (cat >= 1) {
                            player.setLevel(player.getLevel() + (int)(modifyLevel * 0.2));
                            player.sendMessage( plugin.getConfig().getString("prefix") + plugin.getConfig().getString("unbay_sues"));

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    dataM.adddeathgenie(player.getUniqueId(), -1);
                                }
                            }.runTaskAsynchronously(plugin);

                            int s2 = cat - 1;
                            player.sendMessage(plugin.getConfig().getString("prefix") + " §f您目前還剩餘" + s2 + "§f隻小精靈");
                        }
                        else {
                            player.sendMessage(plugin.getConfig().getString("prefix") + plugin.getConfig().getString("unbay_fail"));
                        }


                        break;
                }
                }

        }.runTaskAsynchronously(plugin);
        //異部核心 避免IO卡頓
    }
}
