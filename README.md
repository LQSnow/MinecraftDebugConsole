# MinecraftDebugConsole

<!-- Plugin description -->

It is for easy debugging Minecraft Plugin.

This project is adapted from "https://github.com/syuchan1005/MCPluginDebuggerforIDEA". I fixed its bugs and optimized its functionality.
<!-- Plugin description end -->

## Installation

Download the [latest release](https://github.com/LQSnow/MinecraftDebugConsole/releases/latest) and install it manually using<kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


## Tutorial

1. Create a new folder to store the server files for testing, and place the server jar files inside.
2. Set `ServerJarFile` to the location of the server jar file.
3. Compile your plugin into a Jar file, find its location, and set `PluginJarFile` to the location of the plugin. Ensure that the file location and file name remain unchanged after each compilation of the project.
4. Set the `Host` and `Port`, and if there are no special needs, keep them as default.
5. Modify `PluginName` to your plugin name.
6. Click `Start` to start the server.
7. Click `Stop` to shut down the server.
8. Click `Reload` to reload (not recommended, may cause bugs, recommended to use Reboot instead)
9. Click `Reboot` to restart the server.
10. Click `ForceStop` to forcibly kill the process. If the process is stuck and cannot be shut down properly, click this button to proceed.
11. If you want to reload the plugin without restarting the server, please install the PlugMan plugin. When you modify your plugin, first compile it, then click `ReloadPlugin (Need PlugMan)`, and your updated plugin will immediately take effect on the server. 
12. If the plugin has not been replaced after using `ReloadPlugin (Need PlugMan)`, and the `ReloadPlugin (Need PlugMan)` button is disabled, it cannot be clicked, simply click the `Start` or `Stop` button to restore the disabled button to its original state. This situation may be due to a bug in the version of the PlugMan plugin you are using, and when the PlugMan plugin is unloaded your plugin, the original plugin cannot be deleted. Try changing your PlugMan version or changing your server (such as switching from Paper to Spigot), or wait for a new version of the PlugMan plugin.
