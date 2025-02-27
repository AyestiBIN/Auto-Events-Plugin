package com.ayesti.eventplugin;

import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

public class EventPlugin extends JavaPlugin {

    private final Random random = new Random();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        scheduleEvents();
        getCommand("events").setExecutor(new EventCommand(this));
        getLogger().info("EventPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("EventPlugin has been disabled!");
    }

    private void scheduleEvents() {
        for (String eventTime : getConfig().getStringList("events.daily.times")) {
            LocalTime bstTime = LocalTime.parse(eventTime, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime utcTime = bstTime.minusHours(1); // BST is UTC+1

            long delay = calculateDelay(utcTime);
            if (delay >= 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Get the list of possible events
                        List<?> possibleEvents = getConfig().getMapList("events.daily.possible_events");

                        // Pick a random event
                        int randomIndex = random.nextInt(possibleEvents.size());
                        var event = possibleEvents.get(randomIndex);
                        String command = ((java.util.Map<?, ?>) event).get("command").toString();
                        String eventName = ((java.util.Map<?, ?>) event).get("name").toString();

                        // Start the countdown
                        startCountdown(eventName, command);
                    }
                }.runTaskTimer(this, delay, 20 * 60 * 60 * 24); // Repeat every 24 hours
            }
        }
    }

    private void startCountdown(String eventName, String command) {
        // Check if countdown is enabled
        if (getConfig().getBoolean("events.daily.countdown.enabled")) {
            int duration = getConfig().getInt("events.daily.countdown.duration");
            List<String> countdownMessages = getConfig().getStringList("events.daily.countdown.messages");
            String soundName = getConfig().getString("events.daily.countdown.sound");

            // Schedule the countdown task
            new BukkitRunnable() {
                int count = duration;

                @Override
                public void run() {
                    if (count > 0) {
                        // Broadcast countdown message
                        String message = countdownMessages.get(duration - count)
                                .replace("{event}", eventName);
                        getServer().broadcastMessage(message);

                        // Play sound effect
                        Sound sound = Sound.valueOf(soundName);
                        getServer().getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), sound, 1.0f, 1.0f));

                        count--;
                    } else {
                        // Run the event command
                        getServer().dispatchCommand(getServer().getConsoleSender(), command);

                        // Broadcast the event start message
                        String broadcastMessage = getConfig().getString("events.daily.broadcast")
                                .replace("{event}", eventName);
                        getServer().broadcastMessage(broadcastMessage);

                        // Cancel the task
                        cancel();
                    }
                }
            }.runTaskTimer(this, 0, 20); // Run every second (20 ticks)
        } else {
            // If countdown is disabled, just run the event
            getServer().dispatchCommand(getServer().getConsoleSender(), command);
            String broadcastMessage = getConfig().getString("events.daily.broadcast")
                    .replace("{event}", eventName);
            getServer().broadcastMessage(broadcastMessage);
        }
    }

    private long calculateDelay(LocalTime eventTime) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime nextEvent = now.with(eventTime);
        if (now.isAfter(nextEvent)) {
            nextEvent = nextEvent.plusDays(1);
        }
        return ChronoUnit.SECONDS.between(now, nextEvent) * 20;
    }
}
