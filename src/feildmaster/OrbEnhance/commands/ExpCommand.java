package feildmaster.OrbEnhance.commands;

import feildmaster.OrbEnhance.plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExpCommand implements CommandExecutor {
    private plugin Plugin;

    public ExpCommand(plugin p) {
        Plugin = p;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int size = args.length;
        if(size == 0) return yourLevel(sender, label);

        Player p = null;
        Integer exp = null;
        String format = null;

        if(!sender.hasPermission("orbEnhance.admin")) return noPermission(sender);

        if(sender instanceof Player) {
            if(size > 2) return invalidCommand(sender, label);
            if(size == 1) {
                p = (Player)sender;

                try {
                    exp = Integer.parseInt(args[0]);
                    if(args[0].contains("+")||args[0].contains("-")) {
                        exp = p.getTotalExperience()+exp;
                        format = String.format("You have been given %1$s experience", args[0]);
                    } else
                        format = String.format("Your experience set to %1$s", args[0]);
                } catch (NumberFormatException nfe) {
                    return notANumber(sender);
                }


            } else if (size == 2) {
                p = sender.getServer().getPlayer(args[0]);
                if(p==null) return playerNotFound(sender);

                try {
                    exp = Integer.parseInt(args[1]);
                    if(p!=null && args[1].contains("+")||args[1].contains("-")) {
                        exp = p.getTotalExperience()+exp;
                        format = String.format("You have been given %1$s experience", args[1]);
                    } else
                        format = String.format("Your experience set to %1$s", args[1]);
                } catch (NumberFormatException nfe) {
                    return notANumber(sender);
                }
            }
        } else {
            if(size != 2) return invalidCommand(sender, label);

            p = sender.getServer().getPlayer(args[0]);

            if(p==null) return playerNotFound(sender);

            try {
                exp = Integer.parseInt(args[1]);
            } catch (NumberFormatException nfe) {
                return notANumber(sender);
            }
        }
        int old_exp = p.getExperience();
        int old_lvl = p.getLevel();
        p.setLevel(0);
        p.setExperience(exp);
        if(!(sender instanceof Player) || !((Player)sender).equals(p)) // Sender console, or player is a different player
            p.sendMessage(Plugin.format(ChatColor.YELLOW,format));
        sender.sendMessage(Plugin.format("Player experience changed from "+old_lvl+"/"+old_exp+" to "+p.getLevel()+"/"+p.getExperience()));
        return true;
    }

    private boolean noPermission(CommandSender sender) {
        sender.sendMessage(Plugin.format("You do not have permission to do that!"));
        return true;
    }
    private boolean playerNotFound(CommandSender sender) {
        sender.sendMessage(Plugin.format("Player not found"));
        return true;
    }
    private boolean invalidCommand(CommandSender sender, String label) {
        sender.sendMessage(Plugin.format("Invalid syntax"));
        sender.sendMessage(Plugin.format("/"+label+" [player] [+|-]{exp}"));
        return true;
    }
    private boolean notANumber(CommandSender sender) {
        sender.sendMessage(Plugin.format("Not a number"));
        return true;
    }

    private boolean yourLevel(CommandSender sender, String label) {
        if(sender instanceof Player) {
            Player p = (Player)sender;
            int level = p.getLevel();

            sender.sendMessage(Plugin.format("Your level: "+level));
            sender.sendMessage(Plugin.format("Experience: "+p.getExperience()+"/"+(level*10+10)));
        }

        return true;
    }
}
