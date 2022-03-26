import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Tetris extends ApplicationAdapter
{
    private OrthographicCamera camera; //the camera to our world
    private Viewport viewport; //maintains the ratios of your world
    private ShapeRenderer renderer; //used to draw textures and fonts 
    private BitmapFont font; //used to draw fonts (text)
    private SpriteBatch batch; //also needed to draw fonts (text)

    public static Color[][] board;
    private Tetromino piece;

    private ArrayList<Tetromino> tetrominos;
    private static ArrayList<Tetromino> tetrominoList;

    private int stickTimer;
    private int dropTimer;
    @Override//called once when we start the game
    public void create(){

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
        tetrominoList = new ArrayList<>(Arrays.asList(
                new Tetromino(Pieces.JPiece, new int[] {20,5}),
                new Tetromino(Pieces.LPiece, new int[] {20,5}),
                new Tetromino(Pieces.SPiece, new int[] {20,5}),
                new Tetromino(Pieces.ZPiece, new int[] {20,5}),
                new Tetromino(Pieces.TPiece, new int[] {20,5}),
                new Tetromino(Pieces.IPiece, new int[] {20,4}),
                new Tetromino(Pieces.OPiece, new int[] {20,4})
        ));
        piece = new Tetromino(Pieces.OPiece, new int[] {20,4});

    }

    @Override//called 60 times a second
    public void render(){
        preRender();

        updatePiece();
        control();

        if(!piece.canMoveDown()){
            stickTimer++;
            dropTimer = 0;
            if(stickTimer%90==0){
                piece = new Tetromino(Pieces.JPiece, new int[] {10,5});
                scanRows();
            }
        }else{
            stickTimer = 0;
            dropTimer++;
            if(dropTimer%50==0){
                piece.down();
            }
        }

        drawGrid();
    }
    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
    }

    @Override
    public void dispose(){
        renderer.dispose();
        batch.dispose();
    }

    private void preRender(){
        viewport.apply();

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float delta = Gdx.graphics.getDeltaTime();//1/60 

        //draw everything on the screen
        renderer.setProjectionMatrix(viewport.getCamera().combined);
    }

    private void control(){
        if(Gdx.input.isKeyJustPressed(Keys.Z)){
            piece.rotate(Rotate.counterClockwise);
        }
        if(Gdx.input.isKeyJustPressed(Keys.X)){
            piece.rotate(Rotate.clockwise);
        }
        if(Gdx.input.isKeyJustPressed(Keys.DOWN)){
            piece.down();
        }
        if(Gdx.input.isKeyJustPressed(Keys.UP)){
            piece.up();
        }
        if(Gdx.input.isKeyJustPressed(Keys.LEFT)){
            piece.left();
        }
        if(Gdx.input.isKeyJustPressed(Keys.RIGHT)){
            piece.right();
        }
    }

    private void drawGrid() {
        renderer.begin(ShapeType.Filled);

        for (int y = 0; y < Global.ROWS; y++) {
            for (int x = 0; x < Global.COLS; x++) {
                renderer.setColor(board[y][x]);
                renderer.rect(1 + x * (Global.SQUARE_SIZE + 2), 1 + y * (Global.SQUARE_SIZE + 2), Global.SQUARE_SIZE, Global.SQUARE_SIZE);
            }
        }
        renderer.end();
    }

    private void updatePiece(){
        piece.updateGrid(piece.getColor());
    }

    private void scanRows(){
        for(int row = 0; row<board.length; row++){
            boolean isFull = true;
            for(int col = 0; col<board[row].length; col++){
                if(board[row][col] == Color.WHITE){
                    isFull = false;
                    break;
                }
            }
            if(isFull){
                shiftDown(row);
            }
        }
    }

    private void shiftDown(int delRow){
        for(int row = delRow; row<board.length-1; row++){
            System.arraycopy(board[row + 1], 0, board[row], 0, board[row].length);
        }
        Arrays.fill(board[board.length - 1], Color.WHITE);
    }

    private void randomTetrominos(){

    }
}