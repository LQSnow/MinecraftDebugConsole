package top.lqsnow.minecraftplugintest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.function.Consumer;

public class ServerProcessManager {
    private PluginDataConfig config;
    private Consumer<String> consumer;
    private Thread watchConsole;
    private Process process;

    public ServerProcessManager(PluginDataConfig config, Consumer<String> consumer) {
        this.config = config;
        this.consumer = consumer;
    }

    public boolean isRunning() {
        return process != null && process.isAlive();
    }

    public void startServer() throws IOException {
        if (!isRunning()) {
            // Move plugin.jar to the plugins folder
            File pluginJar = config.getPluginJarFile();
            File serverDir = config.getServerJarFile().getParentFile();
            File pluginsDir = new File(serverDir, "plugins");

            // Create plugins folder if it doesn't exist
            if (!pluginsDir.exists()) {
                pluginsDir.mkdirs();
            }

            File destination = new File(pluginsDir, pluginJar.getName());

            // Delete existing plugin.jar if it exists
            if (destination.exists()) {
                destination.delete();
            }

            // Copy the new plugin.jar to the plugins folder
            Files.copy(pluginJar.toPath(), destination.toPath());

            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", config.getServerJarFile().getName(), "nogui");
            processBuilder.directory(config.getServerJarFile().getParentFile());
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();
            if (watchConsole != null) watchConsole.interrupt();
            watchConsole = new Thread(() -> {
                this.consumer.accept("STX");
                try (InputStreamReader reader = new InputStreamReader(process.getInputStream());
                     BufferedReader bufferedReader = new BufferedReader(reader)) {
                    while (isRunning() && !Thread.interrupted()) {
                        try {
                            String line = bufferedReader.readLine();
                            if (line == null) continue;
                            this.consumer.accept(line);
                        } catch (Exception ignored) {}
                    }
                } catch (Exception ignored) {}
            });
            watchConsole.start();
        }
    }

    public void stopServer() throws InterruptedException, IOException {
        writeCommand("stop\n");
    }

    public void reload() throws IOException {
        writeCommand("reload confirm\n");
    }

    public void reboot() throws InterruptedException, IOException {
        stopServer();
        startServer();
    }

    public void forceStop() throws IOException {
        if (isRunning()) {
            process.getErrorStream().close();
            process.getInputStream().close();
            process.getOutputStream().close();
            process.destroy();
        }
    }

    public void writeCommand(String cmd) throws IOException {
        if (isRunning()) {
            OutputStream outputStream = process.getOutputStream();
            outputStream.write(cmd.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            consumer.accept("> " + cmd.substring(0, cmd.length() - 1));
        }
    }

}
