import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

    private ArrayList<Pieces> tetrominos;
    private static final ArrayList<Pieces> tetrominoList = new ArrayList<>(Arrays.asList(
            Pieces.JPiece,
            Pieces.LPiece,
            Pieces.SPiece,
            Pieces.ZPiece,
            Pieces.TPiece,
            Pieces.IPiece,
            Pieces.OPiece
    ));

    private Pieces hold;

    private int stickTimer;
    private int dropTimer;

    private int rightTimer;
    private int leftTimer;
    private int downTimer;

    private float fieldYOffset;

    private boolean canHold;

    @Override//called once when we start the game
    public void create() {

        camera = new OrthographicCamera();
        viewport = new FitViewport(Final.WORLD_WIDTH, Final.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();
        font = new BitmapFont();
        batch = new SpriteBatch();//if you want to use images instead of using ShapeRenderer 

        board = new Color[Final.ROWS][Final.COLS];
        for (Color[] colors : board) {
            Arrays.fill(colors, Color.WHITE);
        }
        tetrominos = new ArrayList<>();

        fieldYOffset = 0;

        randomTetrominos();
        randomTetrominos();

        piece = new Tetromino(tetrominos.remove(0));
        canHold = true;
    }

    @Override//called 60 times a second
    public void render() {
        fieldYOffset = fieldYOffset < 1 ? 0 : fieldYOffset * 17 / 20;
        preRender();
        updatePiece();
        control();
        checkStick();
        drawGrid();
        drawShadow();
        drawNextAndHold();
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
            downTimer = Final.FIRST_MOVE_DELAY;
        } else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            if (downTimer % Final.MOVE_DELAY == 0 && downTimer >= 0) {
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
            leftTimer = Final.FIRST_MOVE_DELAY;
        } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            if (leftTimer % Final.MOVE_DELAY == 0 && leftTimer >= 0) {
                piece.left();
            }
            leftTimer++;
        }

        if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
            piece.right();
            rightTimer = Final.FIRST_MOVE_DELAY;
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            if (rightTimer % Final.MOVE_DELAY == 0 && rightTimer >= 0) {
                piece.right();

            }
            rightTimer++;
        }

        if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            piece.drop();
            newPiece();
        }

        if (Gdx.input.isKeyJustPressed(Keys.C)) {
            storePiece();
        }
    }

    private void drawNextAndHold() {

        for (int i = 0; i < Final.FUTURE_DEPTH; i++) {
            batch.begin();
            Pieces temp = tetrominos.get(i);
            Texture t = Final.TEXTURES[temp.ordinal()];
            batch.draw(
                    t,
                    Final.NEXT_CENTER - t.getWidth() * Final.TEXTURE_SCALE / 2f,
                    Final.NEXT_BEGIN_Y - (t.getHeight() * Final.TEXTURE_SCALE / 2f + (i + 1) * Final.NEXT_GAP) - fieldYOffset,
                    t.getWidth() * Final.TEXTURE_SCALE,
                    t.getHeight() * Final.TEXTURE_SCALE
            );
            batch.end();
        }

        if (hold == null) {
            return;
        }

        batch.begin();
        Texture t = Final.TEXTURES[hold.ordinal()];
        if(!canHold){
            batch.setColor(Color.GRAY);
        }
        batch.draw(
                t,
                Final.HOLD_CENTER_X - t.getWidth() * Final.TEXTURE_SCALE / 2f,
                Final.HOLD_CENTER_Y - t.getHeight() * Final.TEXTURE_SCALE / 2f - fieldYOffset,
                t.getWidth() * Final.TEXTURE_SCALE,
                t.getHeight() * Final.TEXTURE_SCALE
        );
        batch.setColor(Color.WHITE);
        batch.end();
    }

    private void drawGrid() {
        renderer.begin(ShapeType.Filled);
        renderer.setColor(Color.WHITE);
        //Field outline
        renderer.rect(Final.FIELD_X - Final.OUTLINE_SIZE,
                Final.FIELD_Y - fieldYOffset - Final.OUTLINE_SIZE,
                Final.FIELD_WIDTH + 2 * Final.OUTLINE_SIZE,
                Final.FIELD_HEIGHT + Final.OUTLINE_SIZE
        );
        //Upcoming blocks outline
        renderer.rect(Final.FIELD_X + Final.FIELD_WIDTH + Final.OUTLINE_SIZE,
                Final.FIELD_Y + Final.NEXT_HEIGHT_OFFSET - (fieldYOffset + Final.OUTLINE_SIZE),
                Final.NEXT_WIDTH + Final.OUTLINE_SIZE,
                Final.FIELD_HEIGHT - Final.NEXT_HEIGHT_OFFSET + Final.OUTLINE_SIZE
        );
        //Held block outline
        renderer.rect(Final.FIELD_X - (Final.OUTLINE_SIZE * 2 + Final.NEXT_WIDTH),
                Final.FIELD_Y + Final.HOLD_HEIGHT_OFFSET - (fieldYOffset + Final.OUTLINE_SIZE),
                Final.NEXT_WIDTH + Final.OUTLINE_SIZE,
                Final.FIELD_HEIGHT - Final.HOLD_HEIGHT_OFFSET + Final.OUTLINE_SIZE
        );
        renderer.setColor(Color.BLACK);
        //Field surface
        renderer.rect(Final.FIELD_X,
                Final.FIELD_Y - fieldYOffset,
                Final.FIELD_WIDTH,
                Final.FIELD_HEIGHT
        );
        //Next block surface
        renderer.rect(Final.FIELD_X + Final.FIELD_WIDTH + Final.OUTLINE_SIZE,
                Final.FIELD_Y + Final.NEXT_HEIGHT_OFFSET - fieldYOffset,
                Final.NEXT_WIDTH,
                Final.FIELD_HEIGHT - Final.NEXT_HEIGHT_OFFSET - Final.SIDE_TOP_BORDER
        );
        //Held block surface
        renderer.rect(Final.FIELD_X - (Final.NEXT_WIDTH + Final.OUTLINE_SIZE),
                Final.FIELD_Y + Final.HOLD_HEIGHT_OFFSET - fieldYOffset,
                Final.NEXT_WIDTH,
                Final.FIELD_HEIGHT - Final.HOLD_HEIGHT_OFFSET - Final.SIDE_TOP_BORDER
        );
        renderer.end();
        Gdx.gl.glLineWidth(1 / 3f);
        for (int y = 0; y < Final.VISIBLE_ROWS; y++) {
            for (int x = 0; x < Final.COLS; x++) {
                if (board[y][x] == Color.WHITE) {
                    renderer.begin(ShapeType.Line);
                    renderer.setColor(30 / 255f, 30 / 255f, 30 / 255f, 1);
                } else {
                    renderer.begin(ShapeType.Filled);
                    renderer.setColor(board[y][x]);
                }
                renderer.rect(
                        Final.FIELD_X + Final.GAP + x * (Final.SQUARE_SIZE + Final.GAP * 2),
                        Final.FIELD_Y + Final.GAP + y * (Final.SQUARE_SIZE + Final.GAP * 2) - fieldYOffset,
                        Final.SQUARE_SIZE,
                        Final.SQUARE_SIZE
                );
                renderer.end();
            }
        }
        renderer.begin(ShapeType.Filled);
        for (int y = Final.VISIBLE_ROWS; y < Final.ROWS; y++) {
            for (int x = 0; x < Final.COLS; x++) {
                if (board[y][x] != Color.WHITE) {
                    renderer.setColor(board[y][x]);
                    renderer.rect(
                            Final.FIELD_X + Final.GAP + x * (Final.SQUARE_SIZE + Final.GAP * 2),
                            Final.FIELD_Y + Final.GAP + y * (Final.SQUARE_SIZE + Final.GAP * 2) - fieldYOffset,
                            Final.SQUARE_SIZE,
                            Final.SQUARE_SIZE
                    );
                }
            }
        }
        renderer.end();
    }

    private void drawShadow() {
        Tetromino t = new Tetromino(piece);
        while (t.checkDown()) {
            t.setCenter(new int[]{t.getCenter()[0] - 1, t.getCenter()[1]});
        }

        Gdx.gl.glLineWidth(1.3f);

        renderer.begin(ShapeType.Line);
        renderer.setColor(t.getColor());
        for (int[] b : t.getBlocks()) {
            renderer.rect(
                    Final.FIELD_X + Final.GAP + (b[1] + t.getCenter()[1]) * ((Final.SQUARE_SIZE + Final.GAP * 2)),
                    Final.FIELD_Y + Final.GAP + (b[0] + t.getCenter()[0]) * ((Final.SQUARE_SIZE + Final.GAP * 2)) - fieldYOffset,
                    Final.SQUARE_SIZE,
                    Final.SQUARE_SIZE
            );
        }
        renderer.end();
    }

    private void checkStick() {
        if (!piece.canMoveDown()) {
            stickTimer++;
            dropTimer = 0;
            if (stickTimer % 40 == 0) {
                scanRows();
                newPiece();
            }
        } else {
            stickTimer = 0;
            dropTimer++;
            if (dropTimer % 60 == 0) {
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
            if (tetrominos.size() > 2 && tetrominos.get(tetrominos.size() - 1) == temp) {
                tetrominoListClone.add(temp);
                continue;
            }
            tetrominos.add(temp);
        }
    }

    private void newPiece() {
        int rows = scanRows();
        if (rows == 0) {
            fieldYOffset = 5;
        } else {
            fieldYOffset = 5 * (rows + 1);
        }

        piece = new Tetromino(tetrominos.remove(0));
        if (tetrominos.size() < 7) {
            randomTetrominos();
        }

        canHold = true;
    }

    private void storePiece() {
        if (canHold) {
            piece.updateGrid(Color.WHITE);
            if (hold == null) {
                hold = piece.getPiece();
                piece = new Tetromino(tetrominos.remove(0));
            } else {
                Pieces temp = hold;
                hold = piece.getPiece();
                piece = new Tetromino(temp);
            }
        }
        canHold = false;
    }
}