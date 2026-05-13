package client.view;

import client.map.Field;
import client.network.ActionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class ViewCLI {
    private static final Logger logger = LoggerFactory.getLogger(ViewCLI.class);

    public void drawMap(List<Field> fields, int maxX, int maxY, boolean treasureCollected) {
        displayTreasureStatus(treasureCollected);

        logger.info("🗺️ Printing generated map ({}x{}):", maxX + 1, maxY + 1);

        Field[][] map = new Field[maxX + 1][maxY + 1];
        for (Field field : fields) {
            map[field.getPosition().x()][field.getPosition().y()] = field;
        }

        for (int y = 0; y <= maxY; y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x <= maxX; x++) {
                Field field = map[x][y];
                row.append(Optional.ofNullable(field).map(this::getEmoji).orElse("⛔ "));
            }
            printLine(row.toString());
        }
        System.out.println();
    }

    public void drawMap(List<Field> fields, int maxX, int maxY) {
        drawMap(fields, maxX, maxY, false);
    }

    public void displayGameEndState(ActionState state) {
        if (state == ActionState.WON) {
            printLine("🎉🏆 Congratulations! You have won the game! 🏆🎉");
        } else if (state == ActionState.LOST) {
            printLine("😢 Game Over! You have lost the game. 😢");
        }
    }

    public void displayValidationError(String errorType, String description, String validationMethod, String validationClass) {
        System.err.println("❌ Map Validation Error ❌");
        System.err.println("🔍 Error Type: " + errorType);
        System.err.println("📝 Description: " + description);
        System.err.println("🧪 Validation: " + validationClass + "." + validationMethod + "()");
    }

    public void displayTreasureStatus(boolean treasureCollected) {
        printLine("💎: " + (treasureCollected ? "✅" : "❌"));
    }

    private String getEmoji(Field field) {
        return switch (getEmojiPriorityKey(field)) {
            case "PLAYER_CASTLE" -> "🏰 ";
            case "ENEMY_CASTLE" -> "🏯 ";
            case "BOTH_PLAYERS" -> "🍻 ";
            case "TREASURE_DISCOVERED" -> "💎 ";
            case "TREASURE_COLLECTED" -> "🧰 ";
            case "PLAYER" -> "🧍 ";
            case "ENEMY" -> "🦹 ";
            case "GRASS" -> "🌿 ";
            case "WATER" -> "💧 ";
            case "MOUNTAIN" -> "⛰️ ";
            default -> "❓ ";
        };
    }

    private static String getEmojiPriorityKey(Field field) {
        return switch (field.getCastleState()) {
            case PLAYER_CASTLE -> "PLAYER_CASTLE";
            case ENEMY_CASTLE -> "ENEMY_CASTLE";
            case NO_CASTLE -> switch (field.getPlayerState()) {
                case BOTH -> "BOTH_PLAYERS";
                case PLAYER -> "PLAYER";
                case ENEMY -> "ENEMY";
                case NONE -> switch (field.getTreasureState()) {
                    case DISCOVERED -> "TREASURE_DISCOVERED";
                    case COLLECTED -> "TREASURE_COLLECTED";
                    case NO_TREASURE -> switch (field.getTerrain()) {
                        case GRASS -> "GRASS";
                        case WATER -> "WATER";
                        case MOUNTAIN -> "MOUNTAIN";
                    };
                };
            };
        };
    }

    protected void printLine(String line) {
        System.out.println(line);
    }
}
