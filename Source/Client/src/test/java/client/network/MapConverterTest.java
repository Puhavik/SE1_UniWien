package client.network;

import client.map.ClientMap;
import client.map.Field;
import client.map.Position;
import client.map.Terrain;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromserver.FullMap;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MapConverterTest {

    @Test
    void convertFromClient_withNullFields_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> MapConverter.convertFromClient(null));
    }

    @Test
    void convertFromClient_withEmptyFields_shouldReturnEmptyList() {
        List<Field> fields = new ArrayList<>();
        
        List<PlayerHalfMapNode> result = MapConverter.convertFromClient(fields);
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    void convertFromClient_withOneField_shouldReturnOneNode() {
        List<Field> fields = new ArrayList<>();
        fields.add(new Field(new Position(3, 5), Terrain.GRASS));
        
        List<PlayerHalfMapNode> result = MapConverter.convertFromClient(fields);
        
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getX());
        assertEquals(5, result.get(0).getY());
    }
    
    @Test
    void convertFromServer_withNullFullMap_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> MapConverter.convertFromServer(null));
    }
}