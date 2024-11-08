package com.github.puzzle.util;

import com.google.gson.Gson;
import org.hjson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipPack {

    ZipPackLoader parentLoader;
    ZipFile zipFile;

    ZipEntry metadataZipEntry;

    public static Gson GSON = new Gson();

    public ZipPack(ZipPackLoader parentLoader, ZipFile zipFile) {
        this.parentLoader = parentLoader;
        this.zipFile = zipFile;

        metadataZipEntry = zipFile.getEntry("pack.json");
    }

    public boolean hasEntry(String entryName) {
        ZipEntry entry = zipFile.getEntry(entryName);
        return entry != null;
    }

    public String readEntryToString(String entryName) throws IOException {
        return new String(readEntryToBytes(entryName));
    }

    public byte[] readEntryToBytes(String entryName) throws IOException {
        ZipEntry entry = zipFile.getEntry(entryName);
        InputStream stream = zipFile.getInputStream(entry);
        return stream.readAllBytes();
    }

    public JsonObject readEntryToJson(String entryName) throws IOException {
        return JsonObject.readHjson(readEntryToString(entryName)).asObject();
    }

    public <T> T readEntryToClass(String entryName, Class<T> clazz) throws IOException {
        return GSON.fromJson(entryName, clazz);
    }

    public Enumeration<? extends ZipEntry> getEntries() {
        return zipFile.entries();
    }

    public ZipFile getZipFile() {
        return zipFile;
    }

    public ZipPackLoader getParentLoader() {
        return parentLoader;
    }

    public ZipEntry getMetadataZipEntry() {
        return metadataZipEntry;
    }

    public int setAsSelected() {
        return parentLoader.setSelectedPack(this);
    }

    public static boolean isValidZipPack(ZipFile file) {
        ZipEntry metaEntry = file.getEntry("pack.json");
        return metaEntry != null;
    }

}
