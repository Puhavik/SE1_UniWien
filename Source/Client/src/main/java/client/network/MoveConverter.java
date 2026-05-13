package client.network;

import client.move.Move;
import messagesbase.messagesfromclient.EMove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MoveConverter {
    private static final Logger logger = LoggerFactory.getLogger(MoveConverter.class);

    public EMove convertFromClient(Move move) {
        Objects.requireNonNull(move, "Move must not be null");

        EMove convertedMove = switch (move) {
            case UP -> EMove.Up;
            case DOWN -> EMove.Down;
            case LEFT -> EMove.Left;
            case RIGHT -> EMove.Right;
        };

        logger.trace("Converted Move [{}] to EMove [{}]", move, convertedMove);
        return convertedMove;
    }
}
