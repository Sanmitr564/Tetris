import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Global {
    public static final int WORLD_WIDTH = 1920;
    public static final int WORLD_HEIGHT = 1080;

    public static final int X = 1;
    public static final int Y = 0;

    public static final int FIELD_WIDTH = 350;
    public static final int FIELD_HEIGHT = 2*FIELD_WIDTH;

    public static final int MOVE_DELAY = 3;
    public static final int FIRST_MOVE_DELAY = -15;

    //Bottom Left Corner
    public static final float FIELD_X = (WORLD_WIDTH-FIELD_WIDTH)/2f;
    public static final float FIELD_Y = (WORLD_HEIGHT - FIELD_HEIGHT)/2f;

    public static final float OUTLINE_SIZE = 4;

    public static final int ROWS = 23;

    public static final int VISIBLE_ROWS = 20;
    public static final int COLS = 10;

    public static final float GAP = 0;
    public static final float SQUARE_SIZE = FIELD_WIDTH/(float)COLS - GAP * 2;

    //<editor-fold desc="Color Array">
    public static final Color[] COLORS = new Color[] {
            Color.BLUE,
            Color.ORANGE,
            Color.GREEN,
            Color.RED,
            Color.PURPLE,
            Color.TEAL,
            Color.YELLOW
    };
    //</editor-fold>

    //<editor-fold desc="Texture Array">
    public static final Texture[] TEXTURES = new Texture[] {
            new Texture(Gdx.files.internal("PiecePics/JPiece.png")),
            new Texture(Gdx.files.internal("PiecePics/LPiece.png")),
            new Texture(Gdx.files.internal("PiecePics/SPiece.png")),
            new Texture(Gdx.files.internal("PiecePics/ZPiece.png")),
            new Texture(Gdx.files.internal("PiecePics/TPiece.png")),
            new Texture(Gdx.files.internal("PiecePics/IPiece.png")),
            new Texture(Gdx.files.internal("PiecePics/OPiece.png")),
    };
    //</editor-fold>
}
