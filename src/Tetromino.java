import com.badlogic.gdx.graphics.Color;

public abstract class Tetromino {
    private Rotation rotation;
    private int[][] blocks;
    private int[] center;
    private Color color;
    private Pieces piece;

    public Tetromino(int[] center, Pieces piece, Color color) {
        this.center = center;
        this.piece = piece;
        rotation = Rotation.up;
        this.color = color;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public int[][] getBlocks() {
        return blocks;
    }

    public int[] getCenter() {
        return center;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public void setBlocks(int[][] blocks) {
        this.blocks = blocks;
    }

    public void setCenter(int[] center) {
        this.center = center;
    }

    public void up() {
        center[0]++;
    }

    public void down() {
        center[0]--;
    }

    public void left() {
        center[1]--;
    }

    public void right() {
        center[1]++;
    }

    public void rotate(Rotate r) {
        if (r == Rotate.clockwise) {
            switch (rotation) {
                case up:
                    //method for 0->1
                    break;
                case right:
                    //method for 1->2
                    break;
                case down:
                    //method for 2->3
                    break;
                case left:
                    //method for 3->0
                    break;
            }
        } else {
            switch (rotation) {
                case up:
                    //method for 0->3
                    break;
                case right:
                    //method for 1->0;
                    break;
                case down:
                    //method for 2->1
                    break;
                case left:
                    //method for 3->2
                    break;
            }
        }
    }

    private void upClockWise() {

    }

    private void tryRotateOffset() {

    }

    private void rotateOffset(int xOffset, int yOffset) {

    }
}

class RotationData {
    //Coordinates are listed as {rowOffset, colOffset}/{yOffset, xOffset}
    //All values are positions relative to the center of rotation of the tetromino
    //Arrays are called as [pieceType][facingDirection][blockNumber][row/colOffset]
    //For blocks that rotate around a corner, (0,0) is the block to the bottom left of the corner

    public static final int[][][][] rotationValues = new int[][][][]
            {
                    //jValues
                    {
                            {
                                    {1, -1},
                                    {0, -1},
                                    {0, 0},
                                    {0, 1}
                            },
                            {
                                    {1, 1},
                                    {1, 0},
                                    {0, 0},
                                    {-1, 0}
                            },
                            {
                                    {-1, 1},
                                    {0, 1},
                                    {0, 0},
                                    {0, -1}
                            },
                            {
                                    {-1, -1},
                                    {-1, 0},
                                    {0, 0},
                                    {1, 0}
                            }
                    },
                    //lValues
                    {
                            {
                                    {0, -1},
                                    {0, 0},
                                    {0, 1},
                                    {1, 1}
                            },
                            {
                                    {1, 0},
                                    {0, 0},
                                    {-1, 0},
                                    {-1, 1}
                            },
                            {
                                    {0, 1},
                                    {0, 0},
                                    {0, -1},
                                    {-1, -1}
                            },
                            {
                                    {1, -1},
                                    {1, 0},
                                    {0, 0},
                                    {-1, 0}
                            }
                    }
            };

    //[clockwise/counterclockwise][currentRotation][testNumber][rowOffset/colOffset]
    public static final int[][][][] kickData = new int[][][][]{
            //Clockwise
            {
                    //Current rotation
                    {

                            //testNumber
                            //{rowOffset, colOffset}
                            {0,0},
                            {0, -1},
                            {1, -1},
                            {-2, 0},
                            {-2, -1}

                    },
                    {
                            {0,0},
                            {0, 1},
                            {-1, 1},
                            {2, 0},
                            {2, 1}
                    },
                    {
                            {0,0},
                            {0, 1},
                            {1, 1},
                            {-2, 0},
                            {-2, 1}
                    },
                    {
                            {0,0},
                            {0, -1},
                            {-1, -1},
                            {2, 0},
                            {2, -1}
                    }
            },
            //counterClockwise
            {
                    //Current rotation
                    {
                            //testNumber
                            //{rowOffset, colOffset}
                            {0,0},
                            {0,1},
                            {1,1},
                            {-2,0},
                            {-2,1}
                    },
                    {
                            {0,0},
                            {0,1},
                            {-1,1},
                            {2,0},
                            {2,1}
                    },
                    {
                            {0,0},
                            {0,-1},
                            {1,-1},
                            {-2,1},
                            {-2,-1}
                    },
                    {
                            {0,0},
                            {0,-1},
                            {-1,-1},
                            {2,0},
                            {2,-1}
                    }
            }
    };
}