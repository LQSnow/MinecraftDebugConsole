package top.lqsnow.minecraftplugintest;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class DebugToolWindowFactory implements ToolWindowFactory {
    private JPanel mainPanel;
    private JTextField serverJarFile;
    private JButton serverBrowseButton;
    private JTextField pluginJarFile;
    private JButton pluginBrowseButton;
    private JTextArea outTextArea;
    private JTextField commandField;
    private JButton startButton;
    private JButton stopButton;
    private JButton sendCommandButton;
    private JButton reloadPluginButton;
    private JTextField pluginName;
    private JButton pluginNameSetButton;
    private JTextField connectPort;
    private JButton portSetButton;
    private JButton reloadButton;
    private JButton rebootButton;
    private JButton forceStopButton;
    private JScrollPane scrollPane;
    private JTextField hostField;
    private JButton hostSetButton;

    private Project project;
    private PluginDataConfig config;
    private ServerProcessManager processManager;

    private static FileChooserDescriptor SingleJarFileDescriptor = new FileChooserDescriptor(true, false, true, false, false, false)
            .withFileFilter((file) -> Comparing.equal(file.getExtension(), "jar", SystemInfo.isFileSystemCaseSensitive));

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.project = project;
        this.config = PluginDataConfig.getInstance(project);
        if (this.config != null) this.config.init(project);
        processManager = new ServerProcessManager(this.config, line -> {
            if (line.equals("STX")) outTextArea.setText("");
            else outTextArea.append(line + "\n");
        });
        ((DefaultCaret) outTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        Content content = ContentFactory.getInstance().createContent(mainPanel, "", false);
        toolWindow.getContentManager().addContent(content);
        setValues();
        setListeners();
    }

    private void setValues() {
        serverJarFile.setText(config.getServerJarFile().getAbsolutePath());
        pluginJarFile.setText(config.getPluginJarFile().getAbsolutePath());
        pluginName.setText(config.getPluginName());
        hostField.setText(config.getHost());
        connectPort.setText(config.getPort().toString());
    }

    private void setListeners() {
        serverBrowseButton.addActionListener((e) -> {
            VirtualFile chooseFile = FileChooser.chooseFile(SingleJarFileDescriptor, this.project, this.project.getBaseDir());
            if (chooseFile != null) {
                config.setServerJarFile(new File(chooseFile.getPath()));
                this.serverJarFile.setText(config.getServerJarFile().getAbsolutePath());
            }
        });
        pluginBrowseButton.addActionListener((e) -> {
            VirtualFile chooseFile = FileChooser.chooseFile(SingleJarFileDescriptor, this.project, this.project.getBaseDir());
            if (chooseFile != null) {
                config.setPluginJarFile(new File(chooseFile.getPath()));
                this.pluginJarFile.setText(config.getPluginJarFile().getAbsolutePath());
            }
        });

        pluginNameSetButton.addActionListener((e) -> {
            config.setPluginName(pluginName.getText());
        });
        hostSetButton.addActionListener((e) -> {
            config.setHost(hostField.getText());
        });
        portSetButton.addActionListener((e) -> {
            config.setPort(Integer.valueOf(connectPort.getText()));
        });

        reloadPluginButton.addActionListener((e) -> {
            if (processManager.isRunning()) {
                new Thread(() -> {
                    try {
                        reloadPluginButton.setEnabled(false);
                        // Send "plugman unload plugin" command
                        processManager.writeCommand("plugman unload " + config.getPluginName() + "\n");

                        // Wait for 1 second
                        Thread.sleep(2000);

                        // Move plugin.jar to plugins folder
                        File pluginJar = config.getPluginJarFile();
                        File serverDir = config.getServerJarFile().getParentFile();
                        File pluginsDir = new File(serverDir, "plugins");
                        File destination = new File(pluginsDir, pluginJar.getName());

                        // Delete existing plugin.jar if it exists
                        if (destination.exists()) {
                            destination.delete();
                        }

                        // Copy the new plugin.jar to the plugins folder
                        Files.copy(pluginJar.toPath(), destination.toPath());

                        // Send "plugman load plugin" command
                        processManager.writeCommand("plugman load " + config.getPluginName() + "\n");
                        reloadPluginButton.setEnabled(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        });

        // ServerButtons
        startButton.addActionListener((e) -> {
            try {
                processManager.startServer();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        stopButton.addActionListener((e) -> {
            try {
                processManager.stopServer();
            } catch (InterruptedException | IOException e1) {
                e1.printStackTrace();
            }
        });
        reloadButton.addActionListener((e) -> {
            try {
                processManager.reload();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        rebootButton.addActionListener((e) -> {
            try {
                processManager.reboot();
            } catch (InterruptedException | IOException e1) {
                e1.printStackTrace();
            }
        });
        forceStopButton.addActionListener((e) -> {
            try {
                processManager.forceStop();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        commandField.addActionListener((e) -> {
            sendCommandButton.doClick();
        });
        sendCommandButton.addActionListener((e) -> {
            try {
                processManager.writeCommand(commandField.getText() + "\n");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

}