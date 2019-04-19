package me.rayzr522.eventchecker.listeners;

import me.rayzr522.eventchecker.util.ReflectionUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.List;

public class PluginListener implements Listener {

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e) {
        ReflectionUtil.clearCacheFor(e.getPlugin().getClass().getClassLoader());
    }
}
