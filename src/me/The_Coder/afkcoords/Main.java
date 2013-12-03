package me.The_Coder.afkcoords;

import com.sun.jndi.url.corbaname.corbanameURLContextFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by The_Coder on 12/3/13.
 */
public class Main extends JavaPlugin {
    Logger logger = Logger.getLogger("Minecraft");


    private CoordsListener coordsListener = new CoordsListener(this);
    private UpdateChecker update;

    Map<String, Location> getallplayers = new HashMap<String, Location>();
    Map<String, Location> stillplayers = new HashMap<String, Location>();

    public void onDisable() {
        PluginDescriptionFile pdfFile = getDescription();
        logger.info(pdfFile.getName() + " is now disabled");
    }

    public void onEnable() {
        PluginDescriptionFile pdfFile = getDescription();
        getServer().getPluginManager().registerEvents(coordsListener , this);


        if(!getConfig().contains("AFK.Time")) {
            saveConfig();
            getConfig().set("AFK.Time", 300);
            getConfig().set("AFK.autoupdate", true);
            saveConfig();
        }

        if(getConfig().contains("AFK.autoupdate")) {
            if(getConfig().getBoolean("AFK.autoupdate")) {
                update = new UpdateChecker(this, "http://dev.bukkit.org/server-mods/afkcoords/files.rss");
                if(update.updateNeeded()) {
                    logger.info("A new version, " + update.getVerson() + ", of " + pdfFile.getName() + " is available at " + update.getLink());
                }
            }else {
                logger.info("You have removed the autoupdater field! Adding it back. If you don't what it then turn it to false");
                getConfig().set("AFK.autoupdate", false);
                saveConfig();
            }
        }else {
            logger.info("Auto Updating as been turned off.");
        }

        int task = getConfig().getInt("AFK.Time");
        if(task % 2 == 0) {
           if(Bukkit.getServer().getOnlinePlayers() != null) {
               getServer().getScheduler().runTaskTimer(this, new Runnable() {
                   @Override
                   public void run() {
                       Player[] arrayOfPlayer;
                       int j = (arrayOfPlayer = Bukkit.getServer().getOnlinePlayers()).length;
                       for (int i = 0; i < j; i++) {
                           Player player = arrayOfPlayer[i];
                           if (!getallplayers.containsKey(player.getName())) {
                               getallplayers.put(player.getName(), player.getLocation());
                               return;
                           }

                           if (getallplayers.containsKey(player.getName())) {
                               if (!stillplayers.containsKey(player.getName())) {
                                   if (!player.hasPermission("afk.notseen")) {
                                       Location loc = player.getLocation();
                                       Bukkit.getServer().broadcastMessage(ChatColor.GRAY + player.getDisplayName() + " has gone afk at: " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                                       stillplayers.put(player.getName(), loc);
                                   }
                               }
                           }
                       }
                   }
               }, task, task);
           }else {
               logger.warning("You have to enter a number divisible by 2");
           }
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;
            DateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date date = new Date();
            if (commandLabel.equalsIgnoreCase("afk")) {
                if (player.hasPermission("afk.command")) {
                    this.stillplayers.put(player.getName(), player.getLocation());
                    getServer().broadcastMessage(
                            ChatColor.GRAY + player.getDisplayName() + " has gone AFK");
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                }
            }

            if (commandLabel.equalsIgnoreCase("setplayerafk")) {
                if (player.hasPermission("afk.setplayerafk")) {
                    if (args.length == 1) {
                        Player setplayerafk = getServer().getPlayer(args[0]);
                        if (Bukkit.getServer().getPlayer(args[0]) != null) {
                            logToFile(player.getDisplayName() + " has set " +
                                    setplayerafk.getDisplayName() + " afk. " +
                                    dateformat.format(date));
                            this.stillplayers.put(setplayerafk.getName(),
                                    setplayerafk.getLocation());
                            getServer().broadcastMessage(
                                    ChatColor.GRAY +
                                            setplayerafk.getDisplayName() +
                                            " has gone afk at: " +
                                            setplayerafk.getLocation()
                                                    .getBlockX() +
                                            ", " +
                                            setplayerafk.getLocation()
                                                    .getBlockY() +
                                            ", " +
                                            setplayerafk.getLocation()
                                                    .getBlockZ());
                        } else {
                            player.sendMessage(ChatColor.DARK_RED + args[0] +
                                    " is offline! You can only afk online users.");
                        }
                    } else {
                        player.sendMessage("Sorry " + player.getDisplayName() +
                                " your args are wrong");
                    }

                } else if (args.length == 1) {
                    Player setplayerafk = getServer().getPlayer(args[0]);

                    logToFile(player.getDisplayName() +
                            " tried to use: /setplayerafk <" +
                            setplayerafk.getDisplayName() + "> " +
                            dateformat.format(date));
                    player.sendMessage( ChatColor.RED + "You don't have permission to use that command.");
                }

            }
        }
        return true;
    }

    public void logToFile(String message)
    {
        try
        {
            File dataFolder = getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }

            File saveTo = new File(getDataFolder(), "log.txt");
            if (!saveTo.exists()) {
                try {
                    saveTo.createNewFile();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
            FileWriter fw = new FileWriter(saveTo, true);

            PrintWriter pw = new PrintWriter(fw);

            pw.println(message);

            pw.flush();

            pw.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
