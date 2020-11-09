package su.nexmedia.engine.commands.list;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.NexPlugin;
import su.nexmedia.engine.commands.api.ISubCommand;
import su.nexmedia.engine.utils.ClickText;
import su.nexmedia.engine.utils.StringUT;

public class AboutCommand<P extends NexPlugin<P>> extends ISubCommand<P> {

	public AboutCommand(@NotNull P plugin) {
		super(plugin, new String[]{"about"}, plugin.getNameRaw() + ".user");
	}
	
	@Override
	@NotNull
	public String usage() {
		return "";
	}
	
	@Override
	@NotNull
	public String description() {
		return plugin.lang().Core_Command_About_Desc.getMsg();
	}

	@Override
	public boolean playersOnly() {
		return false;
	}

	@Override
	public void perform(@NotNull CommandSender sender, String label, @NotNull String[] args) {
		sender.sendMessage(StringUT.color("&6&m━━━━━━━━━━━━&6&l[ &e" + plugin.getName() + " - Info &6&l]&6&m━━━━━━━━━━━━"));
        sender.sendMessage(StringUT.color("&eAnother fine product of &6" + NexPlugin.TM + "&e."));
        sender.sendMessage(StringUT.color("&eType &6/" + plugin.getLabel() + " help&e to show commands."));
        sender.sendMessage("");
        sender.sendMessage(StringUT.color("&6» &eCreated by: &6" + plugin.getAuthor()));
        sender.sendMessage(StringUT.color("&6» &eVersion: &6" + plugin.getDescription().getVersion()));
        sender.sendMessage("");
        sender.sendMessage(StringUT.color("&c&lDO NOT FORK, MODIFY, (RE)SELL"));
        sender.sendMessage("");
        
        ClickText ct = new ClickText("%more% &8&l| %donate%");
        	ct.createPlaceholder("%more%", "&c&l[More Plugins]")
        	.hint("&bClick me to browse", "&bMore plugins from &d" + plugin.getAuthor())
        	.url("https://www.spigotmc.org/resources/authors/nightexpress.81588/");
        	
        	ct.createPlaceholder("%donate%", "&a&l[$ Donate $]")
        	.hint("&bEnjoy the plugin?", "&dClick to support development :)")
        	.url("https://www.paypal.me/nightexpress");
        ct.send(sender);
        
        sender.sendMessage("");
        sender.sendMessage(StringUT.color("&ePowered by &6Nex Plugin Engine&e, &c© 2019-2020 NEX Multimedia"));
        sender.sendMessage(StringUT.color("&2&oCheap solutions with a good quality."));
        sender.sendMessage(StringUT.color("&6&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
	}
}
