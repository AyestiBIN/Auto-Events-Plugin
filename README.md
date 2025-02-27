# EventPlugin

A Minecraft plugin for scheduling daily and weekly events. Built with Maven, IntelliJ IDEA, and JDK 17.

## Features
- Automatically schedules events at specific times.
- Supports random event selection (e.g., Daily Keyall, Daily Envoy, Daily KoTH).
- Countdown announcements with sound effects.
- Customizable configuration via `config.yml`.

## Installation
1. Download the latest `.jar` file from the [Releases](https://github.com/AyestiBIN/Auto-Event-Plugin/releases) page.
2. Place the `.jar` file in your server's `plugins` folder.
3. Start or restart your server.

## Configuration
Edit the `config.yml` file to customize event times, commands, and countdown settings.

```yaml
events:
  daily:
    times:
      - "10:00"  # 10:00 AM BST
      - "14:00"  # 2:00 PM BST
      - "18:00"  # 6:00 PM BST
      - "22:00"  # 10:00 PM BST
      - "02:00"  # 2:00 AM BST
      - "06:00"  # 6:00 AM BST
    possible_events:
      - command: "crates giveall common 1"
        name: "Daily Keyall"
      - command: "envoy start"
        name: "Daily Envoy"
      - command: "koth start SkyCloud_1"
        name: "Daily KoTH (SkyCloud 1)"
      - command: "koth start SkyCloud_2"
        name: "Daily KoTH (SkyCloud 2)"
    broadcast: "&6&l{event} &e&lstarting now!"
    countdown:
      enabled: true
      duration: 10
      messages:
        - "&aEvent starting in &e10 &aseconds!"
        - "&aEvent starting in &e5 &aseconds!"
        - "&aEvent starting in &e4 &aseconds!"
        - "&aEvent starting in &e3 &aseconds!"
        - "&aEvent starting in &e2 &aseconds!"
        - "&aEvent starting in &e1 &asecond!"
      sound: "BLOCK_NOTE_BLOCK_PLING"
