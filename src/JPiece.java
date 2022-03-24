import com.badlogic.gdx.graphics.Color;

public class JPiece extends Tetromino {

    public JPiece(int[][] center, Color color) {
        super(center, new int[][]{{-1, -1}, {0, -1}, {0, 0}, {0, 1}}, color);
    }

    public void rotate(Rotate r){

    }
}
