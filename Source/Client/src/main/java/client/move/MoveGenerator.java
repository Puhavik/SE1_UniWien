package client.move;

import client.exceptions.InvalidMoveStrategyException;
import client.map.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class MoveGenerator {
    private static final Logger logger = LoggerFactory.getLogger(MoveGenerator.class);

    private static final int COST_GRASS_TO_GRASS = 2;
    private static final int COST_GRASS_TO_MOUNTAIN = 3;
    private static final int COST_MOUNTAIN_TO_MOUNTAIN = 4;
    private static final int COST_REVISITED_PENALTY = 5;

    private ClientMap clientMap;
    private List<Move> plannedMoves = new ArrayList<>();
    private final Set<Position> visitedPositions = new HashSet<>();
    private int moveIndex = 0;
    private MovePhase currentPhase = MovePhase.SEARCHING;

    public Optional<Move> getNextMove(ClientMap map, boolean hasTreasure) {
        if (plannedMoves.isEmpty() || moveIndex == 0) {
            startNewPath(map, hasTreasure);
        }

        if (hasTreasure && currentPhase == MovePhase.SEARCHING) {
            startNewPath(map, true);
            currentPhase = MovePhase.WITH_TREASURE;
        }

        if (moveIndex < plannedMoves.size()) {
            return Optional.of(plannedMoves.get(moveIndex++));
        }

        logger.warn("No more moves available.");
        return Optional.empty();
    }

    private void startNewPath(ClientMap map, boolean hasTreasure) {
        this.clientMap = map;
        this.visitedPositions.clear();
        this.moveIndex = 0;

        MapHalf targetHalf = hasTreasure
                ? reverseHalf(clientMap.getMapHalf(map.getPlayerPosition()))
                : clientMap.getMapHalf(map.getPlayerPosition());

        List<Position> targetGoals = calculateTargetPositions(targetHalf);
        this.plannedMoves = buildPathToGoals(map.getPlayerPosition(), targetGoals);
        logger.info("Generated {} moves for phase: {}", plannedMoves.size(), currentPhase);
    }

    private List<Position> calculateTargetPositions(MapHalf targetHalf) {
        return clientMap.getFields().stream()
                .filter(field -> field.getTerrain() == Terrain.GRASS)
                .map(Field::getPosition)
                .filter(position -> clientMap.getMapHalf(position) == targetHalf)
                .collect(Collectors.toList());
    }

    private List<Move> buildPathToGoals(Position startPosition, List<Position> goals) {
        List<Move> totalPath = new ArrayList<>();
        Position currentPosition = startPosition;

        while (!goals.isEmpty()) {
            Position nextGoal = findClosestGoal(currentPosition, goals);
            List<Move> pathSegment = runAStar(currentPosition, nextGoal);

            Position tempPosition = currentPosition;
            for (Move direction : pathSegment) {
                tempPosition = applyMove(tempPosition, direction);
                visitedPositions.add(tempPosition);
            }

            totalPath.addAll(pathSegment);
            goals.remove(nextGoal);
            currentPosition = nextGoal;
        }

        return totalPath;
    }

    private Position findClosestGoal(Position from, List<Position> goals) {
        return goals.stream()
                .min(Comparator.comparingInt(goal -> manhattanDistance(from, goal)))
                .orElseThrow(() -> new InvalidMoveStrategyException("No goals available for pathfinding."));
    }

    private List<Move> runAStar(Position start, Position goal) {
        clientMap.getFields().forEach(Field::resetPathfinding);
        Field startField = getField(start);
        Field goalField = getField(goal);

        PriorityQueue<Field> openSet = new PriorityQueue<>();
        Set<Field> closedSet = new HashSet<>();

        startField.setGScore(0);
        startField.setFScore(manhattanDistance(start, goal));
        openSet.add(startField);

        while (!openSet.isEmpty()) {
            Field currentField = openSet.poll();
            if (currentField.equals(goalField)) {
                return reconstructPath(goalField);
            }

            closedSet.add(currentField);

            for (Move direction : Move.values()) {
                Position neighborPosition = applyMove(currentField.getPosition(), direction);
                Optional<Field> neighborOptional = clientMap.findField(neighborPosition);

                if (neighborOptional.isEmpty()
                        || neighborOptional.get().getTerrain() == Terrain.WATER
                        || closedSet.contains(neighborOptional.get())) {
                    continue;
                }

                Field neighbor = neighborOptional.get();
                int moveCost = terrainCost(currentField.getPosition(), neighborPosition)
                        + (visitedPositions.contains(neighborPosition) ? COST_REVISITED_PENALTY : 0);
                int tentativeGScore = currentField.getGScore() + moveCost;

                if (tentativeGScore < neighbor.getGScore()) {
                    neighbor.setCameFrom(currentField);
                    neighbor.setGScore(tentativeGScore);
                    neighbor.setFScore(tentativeGScore + manhattanDistance(neighborPosition, goal));
                    openSet.remove(neighbor);
                    openSet.add(neighbor);
                }
            }
        }

        logger.error("Pathfinding failed: no path from {} to {}", start, goal);
        throw new InvalidMoveStrategyException("No path from " + start + " to " + goal);
    }

    private List<Move> reconstructPath(Field goalField) {
        LinkedList<Move> reconstructedPath = new LinkedList<>();
        Field currentField = goalField;

        while (currentField.getCameFrom().isPresent()) {
            Field previousField = currentField.getCameFrom().get();
            List<Move> moveSteps = new ArrayList<>(positionToMoves(previousField.getPosition(), currentField.getPosition()));
            Collections.reverse(moveSteps);
            reconstructedPath.addAll(0, moveSteps);
            currentField = previousField;
        }

        return reconstructedPath;
    }


    private int terrainCost(Position fromPosition, Position toPosition) {
        Terrain fromTerrain = getField(fromPosition).getTerrain();
        Terrain toTerrain = getField(toPosition).getTerrain();

        if (fromTerrain == Terrain.GRASS && toTerrain == Terrain.GRASS) return COST_GRASS_TO_GRASS;
        if ((fromTerrain == Terrain.GRASS && toTerrain == Terrain.MOUNTAIN)
                || (fromTerrain == Terrain.MOUNTAIN && toTerrain == Terrain.GRASS)) {
            return COST_GRASS_TO_MOUNTAIN;
        }
        if (fromTerrain == Terrain.MOUNTAIN && toTerrain == Terrain.MOUNTAIN) return COST_MOUNTAIN_TO_MOUNTAIN;

        return Integer.MAX_VALUE;
    }

    private int manhattanDistance(Position a, Position b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }

    private List<Move> positionToMoves(Position fromPosition, Position toPosition) {
        int deltaX = toPosition.x() - fromPosition.x();
        int deltaY = toPosition.y() - fromPosition.y();

        Move direction = switch (deltaX) {
            case 1 -> Move.RIGHT;
            case -1 -> Move.LEFT;
            default -> switch (deltaY) {
                case 1 -> Move.DOWN;
                case -1 -> Move.UP;
                default -> throw new InvalidMoveStrategyException("Invalid movement from " + fromPosition + " to " + toPosition);
            };
        };

        int cost = terrainCost(fromPosition, toPosition);
        return Collections.nCopies(cost, direction);
    }

    private Position applyMove(Position position, Move direction) {
        return switch (direction) {
            case UP -> new Position(position.x(), position.y() - 1);
            case DOWN -> new Position(position.x(), position.y() + 1);
            case LEFT -> new Position(position.x() - 1, position.y());
            case RIGHT -> new Position(position.x() + 1, position.y());
        };
    }

    private Field getField(Position position) {
        return clientMap.findField(position)
                .orElseThrow(() -> new InvalidMoveStrategyException("Field not found at position: " + position));
    }

    private MapHalf reverseHalf(MapHalf half) {
        return switch (half) {
            case LEFT -> MapHalf.RIGHT;
            case RIGHT -> MapHalf.LEFT;
            case TOP -> MapHalf.BOTTOM;
            case BOTTOM -> MapHalf.TOP;
        };
    }
}
