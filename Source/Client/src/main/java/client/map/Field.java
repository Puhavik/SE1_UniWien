package client.map;

import java.util.Objects;
import java.util.Optional;

public class Field implements Comparable<Field> {

    private final Position position;
    private final Terrain terrain;

    private CastleState castleState = CastleState.NO_CASTLE;
    private PlayerState playerState = PlayerState.NONE;
    private TreasureState treasureState = TreasureState.NO_TREASURE;

    private int gScore = Integer.MAX_VALUE;
    private int fScore = Integer.MAX_VALUE;
    private Optional<Field> cameFrom = Optional.empty();

    public Field(Position position, Terrain terrain) {
        this.position = position;
        this.terrain = terrain;
    }

    public Position getPosition() {
        return position;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public CastleState getCastleState() {
        return castleState;
    }

    public void setCastleState(CastleState castleState) {
        this.castleState = castleState;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public TreasureState getTreasureState() {
        return treasureState;
    }

    public void setTreasureState(TreasureState treasureState) {
        this.treasureState = treasureState;
    }

    public boolean isPlayerCastle() {
        return castleState == CastleState.PLAYER_CASTLE;
    }

    public int getGScore() {
        return gScore;
    }

    public void setGScore(int gScore) {
        this.gScore = gScore;
    }

    public void setFScore(int fScore) {
        this.fScore = fScore;
    }

    public Optional<Field> getCameFrom() {
        return cameFrom;
    }

    public void setCameFrom(Field cameFrom) {
        this.cameFrom = Optional.ofNullable(cameFrom);
    }

    public void resetPathfinding() {
        this.gScore = Integer.MAX_VALUE;
        this.fScore = Integer.MAX_VALUE;
        this.cameFrom = Optional.empty();
    }

    @Override
    public int compareTo(Field other) {
        return Integer.compare(this.fScore, other.fScore);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Field other)) return false;
        return Objects.equals(this.position, other.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}
