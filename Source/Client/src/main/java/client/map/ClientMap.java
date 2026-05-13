package client.map;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientMap {

    private static final int MAP_HEIGHT = 4;
    private static final int MAP_WIDTH = 9;

    private final List<Field> fields;
    private final Map<Position, Field> fieldMap;
    private final int maxX;
    private final int maxY;
    private final Position playerPosition;

    public ClientMap(List<Field> fields) {
        this.fields = fields;
        this.fieldMap = fields.stream()
                .collect(Collectors.toUnmodifiableMap(Field::getPosition, f -> f));
        this.maxX = calculateMaxX();
        this.maxY = calculateMaxY();
        this.playerPosition = calculatePlayerPosition();
    }

    public List<Field> getFields() {
        return fields;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public Position getPlayerPosition() {
        return playerPosition;
    }

    public Optional<Field> findField(Position position) {
        return Optional.ofNullable(fieldMap.get(position));
    }

    public MapHalf getMapHalf(Position position) {
        if (maxY == MAP_HEIGHT) {
            return position.x() <= 9 ? MapHalf.LEFT : MapHalf.RIGHT;
        } else if (maxY == MAP_WIDTH) {
            return position.y() <= 4 ? MapHalf.TOP : MapHalf.BOTTOM;
        } else {
            throw new IllegalStateException("Unsupported map configuration (maxY=" + maxY + ")");
        }
    }

    private int calculateMaxX() {
        return fields.stream()
                .mapToInt(field -> field.getPosition().x())
                .max()
                .orElse(0);
    }

    private int calculateMaxY() {
        return fields.stream()
                .mapToInt(field -> field.getPosition().y())
                .max()
                .orElse(0);
    }

    private Position calculatePlayerPosition() {
        return fields.stream()
                .filter(field -> field.getPlayerState() == PlayerState.PLAYER
                        || field.getPlayerState() == PlayerState.BOTH)
                .map(Field::getPosition)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("Player position not found in the map. Ensure PLAYER or BOTH state is present."));
    }
}
