package com.github.puzzle.game.engine.account;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

public class FakeItchProfileMe implements Json.Serializable {
    public String username;
    public String display_name;
    public String cover_url;
    public String url;
    public long id;

    public FakeItchProfileMe() {
    }

    public void write(Json json) {
        json.writeField(this, "username");
        json.writeField(this, "display_name");
        json.writeField(this, "cover_url");
        json.writeField(this, "url");
        json.writeField(this, "id");
    }

    public void read(Json json, JsonValue jsonData) {
        this.username = jsonData.getString("username");
        this.display_name = jsonData.getString("display_name", this.username);
        this.cover_url = jsonData.getString("cover_url", null);
        this.url = jsonData.getString("url");
        this.id = jsonData.getLong("id");
    }
}
