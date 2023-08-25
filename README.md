# MinecraftDebugConsole For Intellij IDEA

<!-- Plugin description -->

It allows you to quickly open the server and debug plugins within the IDE, and can apply the modified plugins to the server with just one click without the need for a reboot.

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
9. Click `Reboot` to reboot the server.
10. Click `ForceStop` to forcibly kill the process. If the process is stuck and cannot be shut down properly, click this button to proceed.
11. If you want to reload the plugin without restarting the server, please install the PlugManX plugin. When you modify your plugin, first compile it, then click `ReloadPlugin (Need PlugManX)`, and your updated plugin will immediately take effect on the server.
