package com.tungsten.fclcore.game;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LaunchOptions implements Serializable {

    private File gameDir;
    private JavaVersion java;
    private String versionName;
    private String versionType;
    private String profileName;
    private List<String> gameArguments = new ArrayList<>();
    private List<String> javaArguments = new ArrayList<>();
    private List<String> javaAgents = new ArrayList<>(0);
    private Integer minMemory;
    private Integer maxMemory;
    private Integer metaspace;
    private Integer width;
    private Integer height;
    private boolean fullscreen;
    private String serverIp;
    private String wrapper;
    private Proxy proxy;
    private String proxyUser;
    private String proxyPass;
    private boolean noGeneratedJVMArgs;
    private String preLaunchCommand;
    private String postExitCommand;
    private ProcessPriority processPriority = ProcessPriority.NORMAL;
    private boolean daemon;

    /**
     * The game directory
     */
    public File getGameDir() {
        return gameDir;
    }

    /**
     * The Java Environment that Minecraft runs on.
     */
    public JavaVersion getJava() {
        return java;
    }

    /**
     * Will shown in the left bottom corner of the main menu of Minecraft.
     * null if use the id of launch version.
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * Will shown in the left bottom corner of the main menu of Minecraft.
     * null if use Version.versionType.
     */
    public String getVersionType() {
        return versionType;
    }

    /**
     * Don't know what the hell this is.
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * User custom additional minecraft command line arguments.
     */
    @NotNull
    public List<String> getGameArguments() {
        return Collections.unmodifiableList(gameArguments);
    }

    /**
     * User custom additional java virtual machine command line arguments.
     */
    @NotNull
    public List<String> getJavaArguments() {
        return Collections.unmodifiableList(javaArguments);
    }

    @NotNull
    public List<String> getJavaAgents() {
        return Collections.unmodifiableList(javaAgents);
    }

    /**
     * The minimum memory that the JVM can allocate.
     */
    public Integer getMinMemory() {
        return minMemory;
    }

    /**
     * The maximum memory that the JVM can allocate.
     */
    public Integer getMaxMemory() {
        return maxMemory;
    }

    /**
     * The maximum metaspace memory that the JVM can allocate.
     * For Java 7 -XX:PermSize and Java 8 -XX:MetaspaceSize
     * Containing class instances.
     */
    public Integer getMetaspace() {
        return metaspace;
    }

    /**
     * The initial game window width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * The initial game window height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Is inital game window fullscreen.
     */
    public boolean isFullscreen() {
        return fullscreen;
    }

    /**
     * The server ip that will connect to when enter game main menu.
     */
    public String getServerIp() {
        return serverIp;
    }

    /**
     * i.e. optirun
     */
    public String getWrapper() {
        return wrapper;
    }

    /**
     * Proxy settings
     */
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * The user name of the proxy, optional.
     */
    public String getProxyUser() {
        return proxyUser;
    }

    /**
     * The password of the proxy, optional
     */
    public String getProxyPass() {
        return proxyPass;
    }

    /**
     * Prevent game launcher from generating default JVM arguments like max memory.
     */
    public boolean isNoGeneratedJVMArgs() {
        return noGeneratedJVMArgs;
    }

    /**
     * Command called before game launches.
     */
    public String getPreLaunchCommand() {
        return preLaunchCommand;
    }

    /**
     * Command called after game exits.
     */
    public String getPostExitCommand() {
        return postExitCommand;
    }

    /**
     * Process priority
     */
    public ProcessPriority getProcessPriority() {
        return processPriority;
    }

    /**
     * Will launcher keeps alive after game launched or not.
     */
    public boolean isDaemon() {
        return daemon;
    }

    public static class Builder {

        private final LaunchOptions options = new LaunchOptions();

        public LaunchOptions create() {
            return options;
        }

        /**
         * The game directory
         */
        public File getGameDir() {
            return options.gameDir;
        }

        /**
         * The Java Environment that Minecraft runs on.
         */
        public JavaVersion getJava() {
            return options.java;
        }

        /**
         * Will shown in the left bottom corner of the main menu of Minecraft.
         * null if use the id of launch version.
         */
        public String getVersionName() {
            return options.versionName;
        }

        /**
         * Will shown in the left bottom corner of the main menu of Minecraft.
         * null if use Version.versionType.
         */
        public String getVersionType() {
            return options.versionType;
        }

        /**
         * Don't know what the hell this is.
         */
        public String getProfileName() {
            return options.profileName;
        }

        /**
         * User custom additional minecraft command line arguments.
         */
        public List<String> getGameArguments() {
            return options.gameArguments;
        }

        /**
         * User custom additional java virtual machine command line arguments.
         */
        public List<String> getJavaArguments() {
            return options.javaArguments;
        }

        public List<String> getJavaAgents() {
            return options.javaAgents;
        }

        /**
         * The minimum memory that the JVM can allocate.
         */
        public Integer getMinMemory() {
            return options.minMemory;
        }

        /**
         * The maximum memory that the JVM can allocate.
         */
        public Integer getMaxMemory() {
            return options.maxMemory;
        }

        /**
         * The maximum metaspace memory that the JVM can allocate.
         * For Java 7 -XX:PermSize and Java 8 -XX:MetaspaceSize
         * Containing class instances.
         */
        public Integer getMetaspace() {
            return options.metaspace;
        }

        /**
         * The initial game window width
         */
        public Integer getWidth() {
            return options.width;
        }

        /**
         * The initial game window height
         */
        public Integer getHeight() {
            return options.height;
        }

        /**
         * Is inital game window fullscreen.
         */
        public boolean isFullscreen() {
            return options.fullscreen;
        }

        /**
         * The server ip that will connect to when enter game main menu.
         */
        public String getServerIp() {
            return options.serverIp;
        }

        /**
         * i.e. optirun
         */
        public String getWrapper() {
            return options.wrapper;
        }

        /**
         * Proxy settings
         */
        public Proxy getProxy() {
            return options.proxy;
        }

        /**
         * The user name of the proxy, optional.
         */
        public String getProxyUser() {
            return options.proxyUser;
        }

        /**
         * The password of the proxy, optional
         */
        public String getProxyPass() {
            return options.proxyPass;
        }

        /**
         * Prevent game launcher from generating default JVM arguments like max memory.
         */
        public boolean isNoGeneratedJVMArgs() {
            return options.noGeneratedJVMArgs;
        }

        /**
         * Called command line before launching the game.
         */
        public String getPreLaunchCommand() {
            return options.preLaunchCommand;
        }

        public boolean isDaemon() {
            return options.daemon;
        }

        public Builder setGameDir(File gameDir) {
            options.gameDir = gameDir;
            return this;
        }

        public Builder setJava(JavaVersion java) {
            options.java = java;
            return this;
        }

        public Builder setVersionName(String versionName) {
            options.versionName = versionName;
            return this;
        }

        public Builder setVersionType(String versionType) {
            options.versionType = versionType;
            return this;
        }

        public Builder setProfileName(String profileName) {
            options.profileName = profileName;
            return this;
        }

        public Builder setGameArguments(List<String> gameArguments) {
            options.gameArguments.clear();
            options.gameArguments.addAll(gameArguments);
            return this;
        }

        public Builder setJavaArguments(List<String> javaArguments) {
            options.javaArguments.clear();
            options.javaArguments.addAll(javaArguments);
            return this;
        }

        public Builder setJavaAgents(List<String> javaAgents) {
            options.javaAgents.clear();
            options.javaAgents.addAll(javaAgents);
            return this;
        }

        public Builder setMinMemory(Integer minMemory) {
            options.minMemory = minMemory;
            return this;
        }

        public Builder setMaxMemory(Integer maxMemory) {
            options.maxMemory = maxMemory;
            return this;
        }

        public Builder setMetaspace(Integer metaspace) {
            options.metaspace = metaspace;
            return this;
        }

        public Builder setWidth(Integer width) {
            options.width = width;
            return this;
        }

        public Builder setHeight(Integer height) {
            options.height = height;
            return this;
        }

        public Builder setFullscreen(boolean fullscreen) {
            options.fullscreen = fullscreen;
            return this;
        }

        public Builder setServerIp(String serverIp) {
            options.serverIp = serverIp;
            return this;
        }

        public Builder setWrapper(String wrapper) {
            options.wrapper = wrapper;
            return this;
        }

        public Builder setProxy(Proxy proxy) {
            options.proxy = proxy;
            return this;
        }

        public Builder setProxyUser(String proxyUser) {
            options.proxyUser = proxyUser;
            return this;
        }

        public Builder setProxyPass(String proxyPass) {
            options.proxyPass = proxyPass;
            return this;
        }

        public Builder setNoGeneratedJVMArgs(boolean noGeneratedJVMArgs) {
            options.noGeneratedJVMArgs = noGeneratedJVMArgs;
            return this;
        }

        public Builder setPreLaunchCommand(String preLaunchCommand) {
            options.preLaunchCommand = preLaunchCommand;
            return this;
        }

        public Builder setPostExitCommand(String postExitCommand) {
            options.postExitCommand = postExitCommand;
            return this;
        }

        public Builder setProcessPriority(@NotNull ProcessPriority processPriority) {
            options.processPriority = processPriority;
            return this;
        }

        public Builder setDaemon(boolean daemon) {
            options.daemon = daemon;
            return this;
        }

    }
}