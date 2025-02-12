package com.tungsten.fclcore.mod.curse;

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
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Install a downloaded CurseForge modpack.
 */
public final class CurseInstallTask extends Task<Void> {

    private final DefaultDependencyManager dependencyManager;
    private final DefaultGameRepository repository;
    private final File zipFile;
    private final Modpack modpack;
    private final CurseManifest manifest;
    private final String name;
    private final File run;
    private final ModpackConfiguration<CurseManifest> config;
    private final List<Task<?>> dependents = new ArrayList<>(4);
    private final List<Task<?>> dependencies = new ArrayList<>(1);

    /**
     * Constructor.
     *
     * @param dependencyManager the dependency manager.
     * @param zipFile the CurseForge modpack file.
     * @param manifest The manifest content of given CurseForge modpack.
     * @param name the new version name
     */
    public CurseInstallTask(DefaultDependencyManager dependencyManager, File zipFile, Modpack modpack, CurseManifest manifest, String name) {
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

        GameBuilder builder = dependencyManager.gameBuilder().name(name).gameVersion(manifest.getMinecraft().getGameVersion());
        for (CurseManifestModLoader modLoader : manifest.getMinecraft().getModLoaders()) {
            if (modLoader.getId().startsWith("forge-")) {
                builder.version("forge", modLoader.getId().substring("forge-".length()));
            } else if (modLoader.getId().startsWith("fabric-")) {
                builder.version("fabric", modLoader.getId().substring("fabric-".length()));
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

        ModpackConfiguration<CurseManifest> config = null;
        try {
            if (json.exists()) {
                config = JsonUtils.GSON.fromJson(FileUtils.readText(json), new TypeToken<ModpackConfiguration<CurseManifest>>() {
                }.getType());

                if (!CurseModpackProvider.INSTANCE.getName().equals(config.getType()))
                    throw new IllegalArgumentException("Version " + name + " is not a Curse modpack. Cannot update this version.");
            }
        } catch (JsonParseException | IOException ignore) {
        }
        this.config = config;
        dependents.add(new ModpackInstallTask<>(zipFile, run, modpack.getEncoding(), Collections.singletonList(manifest.getOverrides()), any -> true, config).withStage("hmcl.modpack"));
        dependents.add(new MinecraftInstanceTask<>(zipFile, modpack.getEncoding(), Collections.singletonList(manifest.getOverrides()), manifest, CurseModpackProvider.INSTANCE, manifest.getName(), manifest.getVersion(), repository.getModpackConfiguration(name)).withStage("hmcl.modpack"));

        dependencies.add(new CurseCompletionTask(dependencyManager, name, manifest));
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
            for (CurseManifestFile oldCurseManifestFile : config.getManifest().getFiles()) {
                if (StringUtils.isBlank(oldCurseManifestFile.getFileName())) continue;
                File oldFile = new File(run, "mods/" + oldCurseManifestFile.getFileName());
                if (!oldFile.exists()) continue;
                if (manifest.getFiles().stream().noneMatch(oldCurseManifestFile::equals))
                    if (!oldFile.delete())
                        throw new IOException("Unable to delete mod file " + oldFile);
            }
        }

        File root = repository.getVersionRoot(name);
        FileUtils.writeText(new File(root, "manifest.json"), JsonUtils.GSON.toJson(manifest));
    }
}
