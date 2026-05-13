package client.network;

import client.map.ClientMap;
import client.map.Field;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromserver.FullMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MapConverter {
    private static final Logger logger = LoggerFactory.getLogger(MapConverter.class);

    private MapConverter() {
    }

    public static List<PlayerHalfMapNode> convertFromClient(List<Field> fields) {
        Objects.requireNonNull(fields, "Client map (fields) must not be null");

        logger.info("Converting client map to PlayerHalfMapNodes. Total fields: {}", fields.size());

        List<PlayerHalfMapNode> convertedNodes = fields.stream()
                .map(NodeConverter::convertFromClient)
                .collect(Collectors.toList());

        logger.debug("Successfully converted {} map nodes.", convertedNodes.size());
        return convertedNodes;
    }

    public static ClientMap convertFromServer(FullMap fullMap) {
        Objects.requireNonNull(fullMap, "Full map must not be null");

        logger.info("Converting FullMap to ClientMap.");

        List<Field> clientFields = fullMap.getMapNodes().stream()
                .map(NodeConverter::convertFromServer)
                .collect(Collectors.toList());

        logger.debug("Converted {} fields to client map.", clientFields.size());
        return new ClientMap(clientFields);
    }
}
