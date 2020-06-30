package allshop.allshop;

import org.bukkit.ChatColor;

public enum ColorUtils {
    BLACK("&0", ChatColor.BLACK.toString()),
    DARK_BLUE("&1", ChatColor.DARK_BLUE.toString()),
    DARK_GREEN("&2", ChatColor.DARK_GREEN.toString()),
    DARK_AQUA("&3", ChatColor.DARK_AQUA.toString()),
    DARK_RED("&4", ChatColor.DARK_RED.toString()),
    DARK_PURPLE("&5", ChatColor.DARK_PURPLE.toString()),
    GOLD("&6", ChatColor.GOLD.toString()),
    GRAY("&7", ChatColor.GRAY.toString()),
    DARK_GRAY("&8", ChatColor.DARK_GRAY.toString()),
    BLUE("&9", ChatColor.BLUE.toString()),
    GREEN("&a", ChatColor.GREEN.toString()),
    AQUA("&b", ChatColor.AQUA.toString()),
    RED("&c", ChatColor.RED.toString()),
    LIGHT_PURPLE("&d", ChatColor.LIGHT_PURPLE.toString()),
    YELLOW("&e", ChatColor.YELLOW.toString()),
    WHITE("&f", ChatColor.WHITE.toString()),
    MAGIC("&k", ChatColor.MAGIC.toString()),
    BOLD("&l", ChatColor.BOLD.toString()),
    STRIKETHROUGH("&m", ChatColor.STRIKETHROUGH.toString()),
    UNDERLINE("&n", ChatColor.UNDERLINE.toString()),
    ITALIC("&o", ChatColor.ITALIC.toString()),
    RESET("&r", ChatColor.RESET.toString());

    private final String input;
    private final String MinecraftColor;

    private ColorUtils(String input, String MinecraftColor) { this.input = input;
        this.MinecraftColor = MinecraftColor; }

    public String getMinecraftColor()
    {
        return this.MinecraftColor;
    }

    public String getInput() {
        return this.input;
    }
    public static String format(String message) {
        String msg = "";
        if(!message.contains("&")){
            msg = "&f"+message;
        } else{
            msg = message;
        }
        for (ColorUtils c : values()) {
            msg = msg.replace(c.getInput(), c.getMinecraftColor());
        }
        return msg;
    }
}