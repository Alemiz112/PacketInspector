package eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.data;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class TextClickEvent {

    public enum Action {
        @SerializedName("open_url")
        OPEN_URL,
        @SerializedName("run_command")
        RUN_COMMAND,
        @SerializedName("suggest_command")
        SUGGEST_COMMAND,
        @SerializedName("change_page")
        CHANGE_PAGE;
    }

    private Action action;
    private String value;
}
