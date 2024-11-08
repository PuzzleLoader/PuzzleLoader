package com.github.puzzle.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.zip.ZipFile;

public class ZipPackLoader {

    int SELECTED_PACK_INDEX = 0;
    final Logger LOGGER;
    final File DIR;
    final List<ZipPack> PACKS;
    final Function<ZipFile, Boolean> PACK_VALIDATOR;

    public ZipPackLoader(Logger logger, File dir) {
        this(logger, dir, LoadingStyle.RECURSIVE);
    }

    public ZipPackLoader(Logger logger, File dir, @NonNull LoadingStyle style) {
        this(logger, dir, style, ZipPack::isValidZipPack);
    }

    public ZipPackLoader(Logger logger, File dir, @NonNull LoadingStyle style, Function<ZipFile, Boolean> zipPackValidator) {
        this.DIR = dir;
        LOGGER = logger;

        this.PACKS = new ArrayList<>();
        Set<ZipPack> packSet = new HashSet<>();

        if (style == LoadingStyle.TOP_LEVEL) {
            getAllZipsTopLevel(packSet);
        } else {
            getAllZipsRecursive(packSet, dir);
        }

        PACKS.addAll(packSet);

        PACK_VALIDATOR = zipPackValidator;
    }

    public int setSelectedPack(ZipPack pack) {
        return PACKS.indexOf(pack);
    }

    public void setSelectedPackIndex(int index) {
        SELECTED_PACK_INDEX = index;
    }

    public @Nullable ZipPack getSelectedPack() {
        return PACKS.get(SELECTED_PACK_INDEX);
    }

    public int getSelectedPackIndex() {
        return SELECTED_PACK_INDEX;
    }

    public List<ZipPack> getPacks() {
        return PACKS;
    }

    Set<ZipPack> getAllZipsRecursive(Set<ZipPack> parentSet, File folder) {
        if (folder.getName().toLowerCase().endsWith(".zip") && folder.isFile())
            try {
                parentSet.add(new ZipPack(this, new ZipFile(folder)));
            } catch (IOException e) {
                LOGGER.warn("Invalid zip \"{}\" found at path \"{}\"", folder.getName(), folder.getAbsolutePath());
            }
        if (folder.isDirectory())
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                getAllZipsRecursive(parentSet, file);
            }
        return parentSet;
    }

    Set<ZipPack> getAllZipsTopLevel(Set<ZipPack> set) {
        for (File file : Objects.requireNonNull(DIR.listFiles())) {
            try {
                set.add(new ZipPack(this, new ZipFile(file)));
            } catch (IOException e) {
                LOGGER.warn("Invalid zip \"{}\" found at path \"{}\"", file.getName(), file.getAbsolutePath());
            }
        }

        return set;
    }

    public enum LoadingStyle {
        RECURSIVE,
        TOP_LEVEL
    }

}
