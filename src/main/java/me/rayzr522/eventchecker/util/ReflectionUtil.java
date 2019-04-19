package me.rayzr522.eventchecker.util;

import com.google.common.reflect.ClassPath;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectionUtil {
    private static final Map<ClassLoader, List<Class<Event>>> eventClassCache = new HashMap<>();

    public static List<Class<? extends Event>> getAllEventClasses() {
        return Arrays.stream(Bukkit.getServer().getPluginManager().getPlugins())
                .flatMap(plugin -> getEventClasses(plugin.getClass().getClassLoader()).stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    public static List<Class<Event>> getEventClasses(ClassLoader classLoader) {
        return eventClassCache.computeIfAbsent(classLoader, loader -> {
            try {
                return ClassPath.from(classLoader).getTopLevelClasses().stream()
                        .map(classInfo -> {
                            try {
                                return classInfo.load();
                            } catch (NoClassDefFoundError e) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .filter(Event.class::isAssignableFrom)
                        .filter(eventClass -> {
                            try {
                                eventClass.getDeclaredMethod("getHandlerList");
                                return true;
                            } catch (NoSuchMethodException e) {
                                return false;
                            }
                        })
                        .map(eventClass -> (Class<Event>) eventClass)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        });
    }

    public static void clearCache() {
        eventClassCache.clear();
    }

    public static void clearCacheFor(ClassLoader classLoader) {
        eventClassCache.remove(classLoader);
    }
}
