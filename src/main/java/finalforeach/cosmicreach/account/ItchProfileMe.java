package finalforeach.cosmicreach.account;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import finalforeach.cosmicreach.util.exceptions.YarHarrFiddleDeeDeeException;

public class ItchProfileMe implements Json.Serializable {
    static final int COSMIC_REACH_ITCH_GAME_ID = 2557309;
    public String username;
    public String display_name;
    public String cover_url;
    public String url;
    public long id;

    ItchProfileMe() {
    }

    public void write(Json json) {
        json.writeField(this, "username");
        json.writeField(this, "display_name");
        json.writeField(this, "cover_url");
        json.writeField(this, "url");
        json.writeField(this, "id");
    }

    public void read(Json json, JsonValue jsonData) {
        JsonValue user = jsonData;
        if (jsonData.has("user")) {
            JsonValue api_key = jsonData.get("api_key");
            JsonValue issuer = api_key.get("issuer");
            if (2557309 != issuer.getInt("game_id")) {
                throw new YarHarrFiddleDeeDeeException("Invalid game id!");
            }

            user = jsonData.get("user");
        }

        this.username = user.getString("username");
        this.display_name = user.getString("display_name", this.username);
        this.cover_url = user.getString("cover_url", (String)null);
        this.url = user.getString("url");
        this.id = user.getLong("id");
    }
}
