import com.badlogic.gdx.graphics.Color;

public class Global {
    public static final int WORLD_WIDTH = 500;
    public static final int WORLD_HEIGHT = 1000;

    public static final int X = 1;
    public static final int Y = 0;

    public static final int FIELD_WIDTH = 400;
    public static final int FIELD_HEIGHT = 800;

    //Bottom Left Corner
    public static final int FIELD_X = 50;
    public static final int FIELD_Y = 100;

    public static final int ROWS = 23;

    public static final int VISIBLE_ROWS = 20;
    public static final int COLS = 10;
    public static final float SQUARE_SIZE = FIELD_WIDTH/(float)COLS - 2;

    public static final Color[] COLORS = new Color[] {
            Color.BLUE,
            Color.ORANGE,
            Color.GREEN,
            Color.RED,
            Color.PURPLE,
            Color.TEAL,
            Color.YELLOW
    };
}
