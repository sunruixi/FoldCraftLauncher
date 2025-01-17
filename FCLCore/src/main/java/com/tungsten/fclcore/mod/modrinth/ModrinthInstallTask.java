package com.tungsten.fclcore.mod.modrinth;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.GameBuilder;
import com.tungsten.fclcore.game.DefaultGameRepository;
import com.tungsten.fclcore.mod.MinecraftInstanceTask;
import com.tungsten.fclcore.mod.Modpack;
import com.tungsten.fclcore.mod.ModpackCompletionException;
import com.tungsten.fclcore.mod.ModpackConfiguration;
import com.tungsten.fclcore.mod.ModpackInstallTask;
import com.tungsten.fclcore.mod.curse.CurseManifest;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ModrinthInstallTask extends Task<Void> {

    private final DefaultDependencyManager dependencyManager;
    private final DefaultGameRepository repository;
    private final File zipFile;
    private final Modpack modpack;
    private final ModrinthManifest manifest;
    private final String name;
    private final File run;
    private final ModpackConfiguration<ModrinthManifest> config;
    private final List<Task<?>> dependents = new ArrayList<>(4);
    private final List<Task<?>> dependencies = new ArrayList<>(1);

    public ModrinthInstallTask(DefaultDependencyManager dependencyManager, File zipFile, Modpack modpack, ModrinthManifest manifest, String name) {
        this.dependencyManager = dependencyManager;
        this.zipFile = zipFile;
        this.modpack = modpack;
        this.manifest = manifest;
        this.name = name;
        this.repository = dependencyManager.getGameRepository();
        this.run = repository.getRunDirectory(name);

        File json = repository.getModpackConfiguration(name);
        if (repository.hasVersion(name) && !json.exists())
            throw new IllegalArgumentException("Version " + name + " already exists.");

        GameBuilder builder = dependencyManager.gameBuilder().name(name).gameVersion(manifest.getGameVersion());
        for (Map.Entry<String, String> modLoader : manifest.getDependencies().entrySet()) {
            switch (modLoader.getKey()) {
                case "minecraft":
                    break;
                case "forge":
                    builder.version("forge", modLoader.getValue());
                    break;
                case "fabric-loader":
                    builder.version("fabric", modLoader.getValue());
                    break;
                case "quilt-loader":
                    builder.version("quilt", modLoader.getValue());
                    break;
                default:
                    throw new IllegalStateException("Unsupported mod loader " + modLoader.getKey());
            }
        }
        dependents.add(builder.buildAsync());

        onDone().register(event -> {
            Exception ex = event.getTask().getException();
            if (event.isFailed()) {
                if (!(ex instanceof ModpackCompletionException)) {
                    repository.removeVersionFromDisk(name);
                }
            }
        });

        ModpackConfiguration<ModrinthManifest> config = null;
        try {
            if (json.exists()) {
                config = JsonUtils.GSON.fromJson(FileUtils.readText(json), new TypeToken<ModpackConfiguration<CurseManifest>>() {
                }.getType());

                if (!ModrinthModpackProvider.INSTANCE.getName().equals(config.getType()))
                    throw new IllegalArgumentException("Version " + name + " is not a Modrinth modpack. Cannot update this version.");
            }
        } catch (JsonParseException | IOException ignore) {
        }

        this.config = config;
        List<String> subDirectories = Arrays.asList("/client-overrides", "/overrides");
        dependents.add(new ModpackInstallTask<>(zipFile, run, modpack.getEncoding(), subDirectories, any -> true, config).withStage("hmcl.modpack"));
        dependents.add(new MinecraftInstanceTask<>(zipFile, modpack.getEncoding(), subDirectories, manifest, ModrinthModpackProvider.INSTANCE, manifest.getName(), manifest.getVersionId(), repository.getModpackConfiguration(name)).withStage("hmcl.modpack"));

        dependencies.add(new ModrinthCompletionTask(dependencyManager, name, manifest));
    }

    @Override
    public Collection<Task<?>> getDependents() {
        return dependents;
    }

    @Override
    public Collection<Task<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public void execute() throws Exception {
        if (config != null) {
            // For update, remove mods not listed in new manifest
            for (ModrinthManifest.File oldManifestFile : config.getManifest().getFiles()) {
                Path oldFile = run.toPath().resolve(oldManifestFile.getPath());
                if (!Files.exists(oldFile)) continue;
                if (manifest.getFiles().stream().noneMatch(oldManifestFile::equals)) {
                    Files.deleteIfExists(oldFile);
                }
            }
        }

        File root = repository.getVersionRoot(name);
        FileUtils.writeText(new File(root, "modrinth.index.json"), JsonUtils.GSON.toJson(manifest));
    }
}
