package eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.data;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class TextHoverEvent {

    public enum Action {
        @SerializedName("show_text")
        SHOW_TEXT,
        @SerializedName("show_item")
        SHOW_ITEM,
        @SerializedName("show_entity")
        SHOW_ENTITY;
    }

    private TextClickEvent.Action action;
    private String value;
}
