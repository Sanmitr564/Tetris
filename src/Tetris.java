import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.Input.Keys;

import java.util.ArrayList;
import java.util.Arrays;

public class Tetris extends ApplicationAdapter {
    private OrthographicCamera camera; //the camera to our world
    private Viewport viewport; //maintains the ratios of your world
    private ShapeRenderer renderer; //used to draw textures and fonts 
    private BitmapFont font; //used to draw fonts (text)
    private SpriteBatch batch; //also needed to draw fonts (text)

    public static Color[][] board;
    private Tetromino piece;

    private ArrayList<Tetromino> tetrominos;
    private static final ArrayList<Pieces> tetrominoList = new ArrayList<>(Arrays.asList(
            Pieces.JPiece,
            Pieces.LPiece,
            Pieces.SPiece,
            Pieces.ZPiece,
            Pieces.TPiece,
            Pieces.IPiece,
            Pieces.OPiece
    ));

    private int stickTimer;
    private int dropTimer;

    private int rightTimer;
    private int leftTimer;
    private int downTimer;

    private float fieldYOffset;

    @Override//called once when we start the game
    public void create() {

        camera = new OrthographicCamera();
        viewport = new FitViewport(Global.WORLD_WIDTH, Global.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();
        font = new BitmapFont();
        batch = new SpriteBatch();//if you want to use images instead of using ShapeRenderer 

        board = new Color[Global.ROWS][Global.COLS];
        for (Color[] colors : board) {
            Arrays.fill(colors, Color.WHITE);
        }
        tetrominos = new ArrayList<>();

        fieldYOffset = 0;

        randomTetrominos();
        piece = tetrominos.remove(0);

    }

    @Override//called 60 times a second
    public void render() {
        fieldYOffset = fieldYOffset < 1 ? 0 : fieldYOffset * 17 / 20;
        System.out.println();
        preRender();
        updatePiece();
        control();
        checkStick();
        drawGrid();
        drawOutlines();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
    }

    private void preRender() {
        viewport.apply();

        Gdx.gl.glClearColor(87 / 255f, 25 / 255f, 148 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float delta = Gdx.graphics.getDeltaTime();//1/60 

        //draw everything on the screen
        renderer.setProjectionMatrix(viewport.getCamera().combined);
    }

    private void control() {
        if (Gdx.input.isKeyJustPressed(Keys.Z)) {
            piece.rotate(Rotate.counterClockwise);
            stickTimer = 0;
        }

        if (Gdx.input.isKeyJustPressed(Keys.X)) {
            piece.rotate(Rotate.clockwise);
            stickTimer = 0;
        }

        if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
            piece.down();
            dropTimer = 0;
            downTimer = Global.FIRST_MOVE_DELAY;
        } else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            if (downTimer % Global.MOVE_DELAY == 0 && downTimer >= 0) {
                piece.down();
                dropTimer = 0;
            }
            downTimer++;
        }

        if (Gdx.input.isKeyJustPressed(Keys.UP)) {
            piece.up();
        }

        if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
            piece.left();
            leftTimer = Global.FIRST_MOVE_DELAY;
        } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            if (leftTimer % Global.MOVE_DELAY == 0 && leftTimer >= 0) {
                piece.left();
            }
            leftTimer++;
        }

        if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
            piece.right();
            rightTimer = Global.FIRST_MOVE_DELAY;
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            if (rightTimer % Global.MOVE_DELAY == 0 && rightTimer >= 0) {
                piece.right();

            }
            rightTimer++;
        }

        if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            piece.drop();
            newPiece();
        }
    }

    private void drawNext() {

    }

    private void drawGrid() {

        renderer.begin(ShapeType.Filled);
        renderer.setColor(Color.WHITE);
        renderer.rect(Global.FIELD_X - Global.OUTLINE_SIZE,
                Global.FIELD_Y - fieldYOffset - Global.OUTLINE_SIZE,
                Global.FIELD_WIDTH + 2 * Global.OUTLINE_SIZE,
                Global.FIELD_HEIGHT + Global.OUTLINE_SIZE
        );
        renderer.rect(Global.FIELD_X + Global.FIELD_WIDTH + Global.OUTLINE_SIZE,
                Global.FIELD_Y + Global.NEXT_HEIGHT_OFFSET - (fieldYOffset + Global.OUTLINE_SIZE),
                Global.NEXT_WIDTH + 2*Global.OUTLINE_SIZE,
                Global.FIELD_HEIGHT - Global.NEXT_HEIGHT_OFFSET + Global.OUTLINE_SIZE
        );
        renderer.rect(Global.FIELD_X - (Global.OUTLINE_SIZE * 2 + Global.NEXT_WIDTH),
                Global.FIELD_Y + Global.HOLD_HEIGHT_OFFSET - (fieldYOffset + Global.OUTLINE_SIZE),
                Global.NEXT_WIDTH + 2*Global.OUTLINE_SIZE,
                Global.FIELD_HEIGHT - Global.HOLD_HEIGHT_OFFSET + Global.OUTLINE_SIZE
        );
        renderer.setColor(Color.BLACK);
        renderer.rect(Global.FIELD_X,
                Global.FIELD_Y - fieldYOffset,
                Global.FIELD_WIDTH,
                Global.FIELD_HEIGHT
        );
        renderer.end();
        Gdx.gl.glLineWidth(1 / 3f);
        for (int y = 0; y < Global.VISIBLE_ROWS; y++) {
            for (int x = 0; x < Global.COLS; x++) {
                if (board[y][x] == Color.WHITE) {
                    renderer.begin(ShapeType.Line);
                    renderer.setColor(30 / 255f, 30 / 255f, 30 / 255f, 1);
                } else {
                    renderer.begin(ShapeType.Filled);
                    renderer.setColor(board[y][x]);
                }
                renderer.rect(
                        Global.FIELD_X + Global.GAP + x * (Global.SQUARE_SIZE + Global.GAP * 2),
                        Global.FIELD_Y + Global.GAP + y * (Global.SQUARE_SIZE + Global.GAP * 2) - fieldYOffset,
                        Global.SQUARE_SIZE,
                        Global.SQUARE_SIZE
                );
                renderer.end();
            }
        }
        renderer.begin(ShapeType.Filled);
        for (int y = Global.VISIBLE_ROWS; y < Global.ROWS; y++) {
            for (int x = 0; x < Global.COLS; x++) {
                if (board[y][x] != Color.WHITE) {
                    renderer.setColor(board[y][x]);
                    renderer.rect(
                            Global.FIELD_X + Global.GAP + x * (Global.SQUARE_SIZE + Global.GAP * 2),
                            Global.FIELD_Y + Global.GAP + y * (Global.SQUARE_SIZE + Global.GAP * 2) - fieldYOffset,
                            Global.SQUARE_SIZE,
                            Global.SQUARE_SIZE
                    );
                }
            }
        }
        renderer.end();
    }

    private void drawOutlines() {
        Tetromino t = new Tetromino(piece);
        while (t.checkDown()) {
            t.setCenter(new int[]{t.getCenter()[0] - 1, t.getCenter()[1]});
        }

        Gdx.gl.glLineWidth(1.3f);

        renderer.begin(ShapeType.Line);
        renderer.setColor(t.getColor());
        for (int[] b : t.getBlocks()) {
            renderer.rect(
                    Global.FIELD_X + Global.GAP + (b[1] + t.getCenter()[1]) * ((Global.SQUARE_SIZE + Global.GAP * 2)),
                    Global.FIELD_Y + Global.GAP + (b[0] + t.getCenter()[0]) * ((Global.SQUARE_SIZE + Global.GAP * 2)) - fieldYOffset,
                    Global.SQUARE_SIZE,
                    Global.SQUARE_SIZE
            );
        }
        renderer.end();
    }

    private void checkStick() {
        if (!piece.canMoveDown()) {
            stickTimer++;
            dropTimer = 0;
            if (stickTimer % 30 == 0) {
                scanRows();
                newPiece();
            }
        } else {
            stickTimer = 0;
            dropTimer++;
            if (dropTimer % 40 == 0) {
                piece.down();
            }
        }
    }

    private void updatePiece() {
        piece.updateGrid(piece.getColor());
    }

    private int scanRows() {
        int rowsShifted = 0;
        for (int row = board.length - 1; row >= 0; row--) {
            boolean isFull = true;
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == Color.WHITE) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                shiftDown(row);
                rowsShifted++;
            }
        }
        return rowsShifted;
    }

    private void shiftDown(int delRow) {
        for (int row = delRow; row < board.length - 1; row++) {
            System.arraycopy(board[row + 1], 0, board[row], 0, board[row].length);
        }
        Arrays.fill(board[board.length - 1], Color.WHITE);
    }

    private void randomTetrominos() {
        ArrayList<Pieces> tetrominoListClone = new ArrayList<>(tetrominoList);
        while (tetrominoListClone.size() > 0) {
            Pieces temp = tetrominoListClone.remove((int) (Math.random() * tetrominoListClone.size()));
            tetrominos.add(new Tetromino(temp));
        }
    }

    private void newPiece() {
        int rows = scanRows();
        if (rows == 0) {
            fieldYOffset = 5;
        } else {
            fieldYOffset = 5 * (rows + 1);
        }

        piece = tetrominos.remove(0);
        if (tetrominos.size() < 7) {
            randomTetrominos();
        }
    }
}