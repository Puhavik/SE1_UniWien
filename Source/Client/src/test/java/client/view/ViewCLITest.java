package client.view;

import client.map.*;
import client.network.ActionState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ViewCLITest {

    private static class TestViewCLI extends ViewCLI {
        @Override
        public void displayValidationError(String errorType, String description, String validationMethod, String validationClass) {
            System.err.println("❌ Map Validation Error ❌");
            System.err.println("🔍 Error Type: " + errorType);
            System.err.println("📝 Description: " + description);
            System.err.println("🧪 Validation: " + validationClass + "." + validationMethod + "()");
        }
    }

    private ViewCLI viewCLI;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private ByteArrayOutputStream errorStream;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        originalErr = System.err;
        errorStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errorStream));

        viewCLI = new TestViewCLI();
    }

    @Test
    void displayGameEndState_withWonState_shouldDisplayWinMessage() {
        viewCLI.displayGameEndState(ActionState.WON);

        String output = outputStream.toString();
        assertTrue(output.contains("Congratulations"));
        assertTrue(output.contains("won"));
    }

    @Test
    void displayGameEndState_withLostState_shouldDisplayLoseMessage() {
        viewCLI.displayGameEndState(ActionState.LOST);

        String output = outputStream.toString();
        assertTrue(output.contains("Game Over"));
        assertTrue(output.contains("lost"));
    }

    @Test
    void displayValidationError_shouldDisplayFormattedError() {
        viewCLI.displayValidationError("TestError", "Test description", "testMethod", "TestClass");

        String error = errorStream.toString();
        assertTrue(error.contains("Map Validation Error"));
        assertTrue(error.contains("TestError"));
        assertTrue(error.contains("Test description"));
        assertTrue(error.contains("TestClass.testMethod()"));
    }

    @ParameterizedTest
    @CsvSource({
        "true, ✅",
        "false, ❌"
    })
    void displayTreasureStatus_shouldShowCorrectStatus(boolean treasureCollected, String expectedSymbol) {
        viewCLI.displayTreasureStatus(treasureCollected);

        String output = outputStream.toString();
        assertTrue(output.contains("💎: " + expectedSymbol));
    }

    @Test
    void drawMap_shouldRenderMapWithCorrectSymbols() {
        List<Field> fields = new ArrayList<>();

        Field grassField = new Field(new Position(0, 0), Terrain.GRASS);
        Field waterField = new Field(new Position(0, 1), Terrain.WATER);
        Field mountainField = new Field(new Position(1, 0), Terrain.MOUNTAIN);
        Field playerField = new Field(new Position(1, 1), Terrain.GRASS);
        playerField.setPlayerState(PlayerState.PLAYER);

        fields.add(grassField);
        fields.add(waterField);
        fields.add(mountainField);
        fields.add(playerField);

        viewCLI.drawMap(fields, 1, 1);

        String output = outputStream.toString();
        assertTrue(output.contains("🌿"));  // Grass
        assertTrue(output.contains("💧"));  // Water
        assertTrue(output.contains("⛰️"));  // Mountain
        assertTrue(output.contains("🧍"));  // Player
    }

    @Test
    void drawMap_withTreasureCollected_shouldShowTreasureStatus() {
        List<Field> fields = new ArrayList<>();
        fields.add(new Field(new Position(0, 0), Terrain.GRASS));

        viewCLI.drawMap(fields, 0, 0, true);

        String output = outputStream.toString();
        assertTrue(output.contains("💎: ✅"));
    }

    @Test
    void drawMap_withCastles_shouldRenderCastlesCorrectly() {
        List<Field> fields = new ArrayList<>();

        Field playerCastle = new Field(new Position(0, 0), Terrain.GRASS);
        playerCastle.setCastleState(CastleState.PLAYER_CASTLE);

        Field enemyCastle = new Field(new Position(1, 0), Terrain.GRASS);
        enemyCastle.setCastleState(CastleState.ENEMY_CASTLE);

        fields.add(playerCastle);
        fields.add(enemyCastle);

        viewCLI.drawMap(fields, 1, 0);

        String output = outputStream.toString();
        assertTrue(output.contains("🏰"));  // Player castle
        assertTrue(output.contains("🏯"));  // Enemy castle
    }

    @Test
    void drawMap_withBothPlayers_shouldRenderCorrectly() {
        List<Field> fields = new ArrayList<>();

        Field bothPlayersField = new Field(new Position(0, 0), Terrain.GRASS);
        bothPlayersField.setPlayerState(PlayerState.BOTH);

        fields.add(bothPlayersField);

        viewCLI.drawMap(fields, 0, 0);

        String output = outputStream.toString();
        assertTrue(output.contains("🍻"));  // Both players
    }

    @Test
    void drawMap_withEnemyPlayer_shouldRenderCorrectly() {
        List<Field> fields = new ArrayList<>();

        Field enemyField = new Field(new Position(0, 0), Terrain.GRASS);
        enemyField.setPlayerState(PlayerState.ENEMY);

        fields.add(enemyField);

        viewCLI.drawMap(fields, 0, 0);

        String output = outputStream.toString();
        assertTrue(output.contains("🦹"));  // Enemy player
    }

    @Test
    void drawMap_withDiscoveredTreasure_shouldRenderCorrectly() {
        List<Field> fields = new ArrayList<>();

        Field treasureField = new Field(new Position(0, 0), Terrain.GRASS);
        treasureField.setTreasureState(TreasureState.DISCOVERED);

        fields.add(treasureField);

        viewCLI.drawMap(fields, 0, 0);

        String output = outputStream.toString();
        assertTrue(output.contains("💎"));  // Discovered treasure
    }

    @Test
    void drawMap_withCollectedTreasure_shouldRenderCorrectly() {
        List<Field> fields = new ArrayList<>();

        Field treasureField = new Field(new Position(0, 0), Terrain.GRASS);
        treasureField.setTreasureState(TreasureState.COLLECTED);

        fields.add(treasureField);

        viewCLI.drawMap(fields, 0, 0);

        String output = outputStream.toString();
        assertTrue(output.contains("🧰"));  // Collected treasure
    }
}
