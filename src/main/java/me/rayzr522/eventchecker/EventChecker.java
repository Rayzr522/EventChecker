package me.rayzr522.eventchecker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import me.rayzr522.eventchecker.util.ReflectionUtil;

public class EventChecker extends JavaPlugin implements TabCompleter {
    @Override
    public void onEnable() {
        getCommand("listeners").setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return ReflectionUtil.getEventClasses(Bukkit.getServer().getClass().getClassLoader()).stream()
                .map(Class::getCanonicalName)
                .filter(name -> name.startsWith(args[0]))
                .collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        String eventTypeName = args[0];

        Class<? extends Event> eventClass;
        try {
            Class<?> rawClass = Class.forName(eventTypeName);

            if (!Event.class.isAssignableFrom(rawClass)) {
                sender.sendMessage(ChatColor.RED + "Only classes which extend the Event class are allowed!");
                return true;
            }

            eventClass = rawClass.asSubclass(Event.class);
        } catch (ClassNotFoundException e) {
            sender.sendMessage(ChatColor.RED + "That class could not be found!");
            return true;
        }

        Method getHandlerList;
        try {
            getHandlerList = eventClass.getDeclaredMethod("getHandlerList");
        } catch (NoSuchMethodException e) {
            sender.sendMessage(ChatColor.RED + "Missing getHandlerList method on event type!");
            return true;
        }

        HandlerList handlerList;
        try {
            handlerList = (HandlerList) getHandlerList.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            sender.sendMessage(ChatColor.RED + "Failed to get the handler list object!");
            return true;
        }

        sender.sendMessage(String.format(
                "%s%s               %s Listeners for %s%s %s%s               ",
                ChatColor.DARK_GRAY,
                ChatColor.STRIKETHROUGH,
                ChatColor.YELLOW,
                ChatColor.RED,
                eventClass.getSimpleName(),
                ChatColor.DARK_GRAY,
                ChatColor.STRIKETHROUGH
        ));

        Arrays.stream(handlerList.getRegisteredListeners())
                .sorted(Comparator.comparing(provider -> provider.getPlugin().getName()))
                .map(provider -> String.format(
                        "%s%s%s - %s%s%s (%s)",

                        ChatColor.GREEN,
                        provider.getPlugin().getName(),
                        ChatColor.YELLOW,

                        ChatColor.RED,
                        provider.getListener().getClass().getSimpleName(),
                        ChatColor.YELLOW,

                        provider.getPriority().name()
                ))
                .forEach(sender::sendMessage);

        return true;
    }
}
