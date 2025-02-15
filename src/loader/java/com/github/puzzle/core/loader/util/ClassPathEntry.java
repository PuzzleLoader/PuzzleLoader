package com.github.puzzle.core.loader.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public record ClassPathEntry(
        File file,
        boolean isArchive,
        boolean isDirectory
) {

    public byte[] getContents() throws IOException {
        InputStream stream = null;
        byte[] bytes = new byte[0];

        if (file.canRead() && file.isFile()) stream = new FileInputStream(file);
        if (stream == null) throw new IOException("Could Not Read \"" + file.getName() + "\"");

        bytes = stream.readAllBytes();
        stream.close();
        return bytes;
    }

    public ClassPathFileEntry[] listAllFiles() throws IOException {
        List<ClassPathFileEntry> entries = new ArrayList<>();

        if (isArchive) {
            ZipFile zip = new ZipFile(file);
            Iterator<? extends ZipEntry> iterator = zip.entries().asIterator();
            while (iterator.hasNext()) {
                ZipEntry entry = iterator.next();

                entries.add(new ClassPathFileEntry(
                        true,
                        entry.getName(),
                        entry,
                        zip,
                        null
                ));
            }
        } else {
            if (!isDirectory) throw new IOException("Cannot get files from non-archive/directory");

            for (File file : ModLocator.getFilesRecursive(file)) {
                entries.add(new ClassPathFileEntry(
                        false,
                        file.getName(),
                        null,
                        null,
                        file
                ));
            }
        }

        return entries.toArray(new ClassPathFileEntry[0]);
    }

}
