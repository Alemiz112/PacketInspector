package eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.data;

import com.google.gson.Gson;
import lombok.Data;

import java.util.List;

/**
 * Holds basic components of server to client message
 */
@Data
public class TextComponent {
    
    private static final Gson GSON = new Gson();

    private String color;
    private String font;
    private String insertion;

    // Text container
    private String text;

    // Translate container
    private String translate;
    private List<TextComponent> with;

    // Score component
    private TextScoreComponent score;

    // Entity names component
    private String selector;
    private TextComponent separator;

    // Keybind
    private String keybind;

    // TODO: support NBT component

    private boolean italic;
    private boolean bold;
    private boolean underlined;
    private boolean obfuscated;
    private boolean strikethrough;

    private TextClickEvent clickEvent;
    private TextHoverEvent hoverEvent;
    private List<TextComponent> extra;

    public static TextComponent deserialize(String json) {
        return GSON.fromJson(json, TextComponent.class);
    }

    public String serialize() {
        return GSON.toJson(this);
    }
}
