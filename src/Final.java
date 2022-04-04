import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Final {
    public static final int WORLD_WIDTH = 1920;
    public static final int WORLD_HEIGHT = 1080;

    public static final int X = 1;
    public static final int Y = 0;

    public static final int FIELD_WIDTH = 350;
    public static final int FIELD_HEIGHT = 2 * FIELD_WIDTH;

    public static final int MOVE_DELAY = 3;
    public static final int FIRST_MOVE_DELAY = -15;

    //Bottom Left Corner
    public static final float FIELD_X = (WORLD_WIDTH - FIELD_WIDTH) / 2f;
    public static final float FIELD_Y = (WORLD_HEIGHT - FIELD_HEIGHT) / 2f;

    public static final float OUTLINE_SIZE = 4;

    public static final int ROWS = 23;

    public static final int VISIBLE_ROWS = 20;
    public static final int COLS = 10;

    public static final float GAP = 0;
    public static final float SQUARE_SIZE = FIELD_WIDTH / (float) COLS - GAP * 2;

    public static final int FUTURE_DEPTH = 5;

    public static final float NEXT_WIDTH = 150;
    public static final float NEXT_HEIGHT_OFFSET = 200;

    public static final float HOLD_HEIGHT_OFFSET = 550;

    public static final float SIDE_TOP_BORDER = 35;

    public static final float NEXT_CENTER = FIELD_X + FIELD_WIDTH + OUTLINE_SIZE + NEXT_WIDTH / 2;
    public static final float NEXT_BEGIN_Y = (Final.FIELD_Y + Final.NEXT_HEIGHT_OFFSET) + (Final.FIELD_HEIGHT - Final.NEXT_HEIGHT_OFFSET - Final.SIDE_TOP_BORDER);
    public static final float NEXT_GAP = (Final.FIELD_HEIGHT - Final.NEXT_HEIGHT_OFFSET - Final.SIDE_TOP_BORDER) / (FUTURE_DEPTH + 1);

    public static final float HOLD_CENTER_X = FIELD_X - (OUTLINE_SIZE + NEXT_WIDTH / 2);
    public static final float HOLD_CENTER_Y = Final.FIELD_Y + Final.HOLD_HEIGHT_OFFSET + (Final.FIELD_HEIGHT - Final.HOLD_HEIGHT_OFFSET - Final.SIDE_TOP_BORDER) / 2;

    public static final float TEXTURE_SCALE = .3f;

    //<editor-fold desc="Color Array">
    public static final Color[] COLORS = new Color[]{
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
    public static final Texture[] TEXTURES = new Texture[]{
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
