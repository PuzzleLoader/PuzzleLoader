package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;
import com.github.puzzle.game.items.data.DataTagManifest;
import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;

public class DataTagManifestAttribute implements DataTag.DataTagAttribute<DataTagManifest> {

    DataTagManifest manifest;

    public DataTagManifestAttribute() {}

    public DataTagManifestAttribute(DataTagManifest manifest) {
        this.manifest = manifest;
    }

    @Override
    public void setValue(DataTagManifest value) {
        manifest = value;
    }

    @Override
    public DataTagManifest getValue() {
        return manifest;
    }

    @Override
    public DataTag.DataTagAttribute<DataTagManifest> copyAndSetValue(DataTagManifest value) {
        return new DataTagManifestAttribute(value);
    }

    @Override
    public String getFormattedString() {
        return manifest.toString();
    }

    @Override
    public String toString() {
        return getFormattedString();
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        manifest = new DataTagManifest();
        manifest.read(crBinDeserializer);
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        manifest.write(crBinSerializer);
    }
}
