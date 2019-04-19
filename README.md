# EventChecker

> A little plugin to check what plugins are listening to each event type.

This minimalistic Bukkit plugin is designed to allow you to track down which plugins are responsible for doing naughty things on your server!

An example use case (this was a real problem!) was where items were being deleted from enderchests if you closed the enderchest too quickly after placing the item in it.

However, the client I was assisting had dozens upon dozens of complicated Bukkit plugins.

We needed to quickly locate which plugins were listening to `InventoryClickEvent`s and messing with the timings.

*Enter EventChecker!* A quick `/listeners org.bukkit.event.inventory.InventoryClickEvent` helped us find that only half a dozen or so plugins were even messing with `InventoryClickEvent`s.

From there, it only took us about 15 minutes to figure out which one was the culprit by trial and error.

## Installation

Grab the latest version from the [releases page](https://github.com/RayzrDev/EventChecker/releases) and drop it in your `plugins` folder.

The only requirements are **Vault** and a permissions plugin!

## Usage

Simply do `/listeners <full event class name>` to find all listeners for the given event class. This does support auto-complete for class names, and functions both with built in Bukkit event classes as well as custom plugin event classes.

*The permission to use this command is `EventChecker.use`.*

## Join Me

[![Discord Badge](https://github.com/Rayzr522/ProjectResources/raw/master/RayzrDev/badge-small.png)](https://rayzr.dev/join)
