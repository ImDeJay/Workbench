Workbench
===========

Hey, and welcome to the Workbench repository! Workbench is a resource used in
FutureDev's Bukkit plugins to make development easier and less repetitive. It offers
dozens of useful resources that allow for faster and easier development, such
as:

* **An annotation-based command system.** This resource allows us to create
methods which auto-generate basic documentation, but also allow built-in
control for permissions, flags, et al. See example:

```java
@Permission ("workbench.example")
@Command (aliases = { "example" }, simple = "Shows a message to the user.", console = true)
public void example (Session user, Arguments args,
                     @Default("0") @Flag('i') int value) throws CommandException {
    user.print(ChatColor.AQUA + "Hello!" +
        (args.hasFlag('i') ? " I see you put in" + value + "." : "" ));
}
```

* **A material database system.** Workbench can load items from a configuration file to match items.
This includes support for sub-items, such as `wool:red`.

* **A localisation and theming system.** Workbench offers localisation utilities which also support
themed colour codes (allowing users to instantly change the language and colours to their preferred flavour).

* **A 2D math API.** Workbench offers some very useful math APIs, such as the Levenshtein utility which finds
Levenshtein distance and the RevelantSearch class which attempts to accurately gauge how relevant one term is
to another.

* **A 3D math API.** Workbench has a few useful utilities for three-dimensional math, such as the VectorRegion
class, which represents an area in the world.

* **A pseudo-HTML to JSON converter.** This may be one of the most useful utilities available in Workbench.

* **A slew of processors.** Workbench offers a set of processors for various types of data, such as players and
times.

So go on, check it out. I'm sure you'll like what it has to offer!
