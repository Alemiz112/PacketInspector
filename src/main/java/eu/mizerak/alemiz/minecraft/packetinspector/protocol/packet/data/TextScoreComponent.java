package eu.mizerak.alemiz.minecraft.packetinspector.protocol.packet.data;

import lombok.Data;

@Data
public class TextScoreComponent {

    private String name;
    private String objective;
    private String value;
}
