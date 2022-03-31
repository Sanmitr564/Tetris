import com.badlogic.gdx.graphics.Color;

public class Tetromino {
    private Rotation rotation;
    //private int[][] blocks;
    private int[] center;
    private final Color color;
    private final Pieces piece;

    public Tetromino(Pieces piece, int[] center) {
        this.center = center;
        this.piece = piece;
        rotation = Rotation.up;
        this.color = Final.COLORS[piece.ordinal()];
    }

    public Tetromino(Tetromino t) {
        center = new int[2];
        this.center[0] = t.getCenter()[0];
        this.center[1] = t.getCenter()[1];
        this.piece = t.getPiece();
        this.color = Final.COLORS[piece.ordinal()];
        rotation = t.getRotation();
    }

    public Tetromino(Pieces piece) {
        this.piece = piece;
        if (piece == Pieces.IPiece || piece == Pieces.OPiece) {
            this.center = new int[]{20, 4};
        } else {
            this.center = new int[]{20, 5};
        }
        this.color = Final.COLORS[piece.ordinal()];
        rotation = Rotation.up;
    }

    private Pieces getPiece() {
        return piece;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public int[][] getBlocks() {
        return RotationData.rotationValues[piece.ordinal()][rotation.ordinal()];
    }

    public int[] getCenter() {
        return center;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public void setCenter(int[] center) {
        this.center = center;
    }

    public Color getColor() {
        return color;
    }

    public void up() {
        if (canMove(rotation, new int[]{1, 0})) {
            center[0]++;
        }
        updateGrid(color);
    }

    public void down() {
        if (canMove(rotation, new int[]{-1, 0})) {
            center[0]--;
        }
        updateGrid(color);
    }

    public void left() {
        if (canMove(rotation, new int[]{0, -1})) {
            center[1]--;
        }
        updateGrid(color);
    }

    public void right() {
        if (canMove(rotation, new int[]{0, 1})) {
            center[1]++;
        }
        updateGrid(color);
    }

    public void drop() {
        while (canMoveDown()) {
            down();
        }
    }

    public void updateGrid(Color c) {
        for (int i = 0; i < 4; i++) {
            int[] centerOffset = RotationData.rotationValues
                    [piece.ordinal()]
                    [rotation.ordinal()]
                    [i];
            Tetris.board[center[0] + centerOffset[0]][center[1] + centerOffset[1]] = c;
        }

    }

    public void rotate(Rotate r) {
        if (piece == Pieces.OPiece)
            return;
        Rotation temp;
        int num = 0;
        switch (r) {
            case clockwise -> num = rotation.ordinal() + 1;
            case counterClockwise -> num = rotation.ordinal() - 1;
        }
        if (num > 3) {
            num -= 4;
        } else if (num < 0) {
            num += 4;
        }
        temp = Rotation.values()[num];
        int[][][][] kickData;
        if (piece == Pieces.IPiece) {
            kickData = RotationData.iKickData;
        } else {
            kickData = RotationData.standardKickData;
        }
        for (int[] arr : kickData[r.ordinal()][rotation.ordinal()]) {
            if (canMove(temp, arr)) {
                rotation = temp;
                center[0] += arr[0];
                center[1] += arr[1];
                break;
            }
        }
        updateGrid(color);
    }

    private boolean canMove(Rotation r, int[] offset) {
        updateGrid(Color.WHITE);
        boolean canMove = true;
        try {
            for (int i = 0; i < 4; i++) {
                int[] centerOffset = RotationData.rotationValues
                        [piece.ordinal()]
                        [r.ordinal()]
                        [i];
                if (Tetris.board[center[0] + centerOffset[0] + offset[0]][center[1] + centerOffset[1] + offset[1]] != Color.WHITE) {
                    return false;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return canMove;
    }

    public boolean canMoveDown() {
        boolean b = canMove(rotation, new int[]{-1, 0});
        updateGrid(color);
        return b;
    }

    public boolean checkDown() {
        return canMove(rotation, new int[]{-1, 0});
    }
}

class RotationData {
    //Coordinates are listed as {rowOffset, colOffset}/{yOffset, xOffset}
    //All values are positions relative to the center of rotation of the tetromino
    //Arrays are called as [pieceType][rotation][blockNumber][row/colOffset]
    //For blocks that rotate around a corner, (0,0) is the block to the bottom left of the corner
    //<editor-fold desc="Piece Rotation Values">
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
                    },
                    //sValues
                    {
                            {
                                    {0, -1},
                                    {0, 0},
                                    {1, 0},
                                    {1, 1}
                            },
                            {
                                    {1, 0},
                                    {0, 0},
                                    {0, 1},
                                    {-1, 1}
                            },
                            {
                                    {0, 1},
                                    {0, 0},
                                    {-1, 0},
                                    {-1, -1}
                            },
                            {
                                    {-1, 0},
                                    {0, 0},
                                    {0, -1},
                                    {1, -1}
                            }
                    },
                    //zValues
                    {
                            {
                                    {1, -1},
                                    {1, 0},
                                    {0, 0},
                                    {0, 1}
                            },
                            {
                                    {1, 1},
                                    {0, 1},
                                    {0, 0},
                                    {-1, 0}
                            },
                            {
                                    {-1, 1},
                                    {-1, 0},
                                    {0, 0},
                                    {0, -1}
                            },
                            {
                                    {-1, -1},
                                    {0, -1},
                                    {0, 0},
                                    {1, 0}
                            }
                    },
                    //tValues
                    {
                            {
                                    {0, -1},
                                    {0, 0},
                                    {1, 0},
                                    {0, 1}
                            },
                            {
                                    {1, 0},
                                    {0, 0},
                                    {0, 1},
                                    {-1, 0}
                            },
                            {
                                    {0, -1},
                                    {0, 0},
                                    {-1, 0},
                                    {0, 1}
                            },
                            {
                                    {-1, 0},
                                    {0, 0},
                                    {0, -1},
                                    {1, 0}
                            }
                    },
                    //iValues
                    {
                            {
                                    {1, -1},
                                    {1, 0},
                                    {1, 1},
                                    {1, 2},
                            },
                            {
                                    {2, 1},
                                    {1, 1},
                                    {0, 1},
                                    {-1, 1}
                            },
                            {
                                    {0, -1},
                                    {0, 0},
                                    {0, 1},
                                    {0, 2},
                            },
                            {
                                    {2, 0},
                                    {1, 0},
                                    {0, 0},
                                    {-1, 0},
                            }
                    },
                    //oValues
                    {
                            {
                                    {0, 0},
                                    {1, 0},
                                    {1, 1},
                                    {0, 1}
                            },
                            {
                                    {0, 0},
                                    {1, 0},
                                    {1, 1},
                                    {0, 1}
                            },
                            {
                                    {0, 0},
                                    {1, 0},
                                    {1, 1},
                                    {0, 1}
                            },
                            {
                                    {0, 0},
                                    {1, 0},
                                    {1, 1},
                                    {0, 1}
                            },
                    }
            };

    //</editor-fold>

    //[clockwise/counterclockwise][currentRotation][testNumber][rowOffset/colOffset]
    //<editor-fold desc="Standard Piece Kick Data">
    public static final int[][][][] standardKickData = new int[][][][]{
            //Clockwise
            {
                    //Current rotation
                    {

                            //testNumber
                            //{rowOffset, colOffset}
                            {0, 0},
                            {0, -1},
                            {1, -1},
                            {-2, 0},
                            {-2, -1}

                    },
                    {
                            {0, 0},
                            {0, 1},
                            {-1, 1},
                            {2, 0},
                            {2, 1}
                    },
                    {
                            {0, 0},
                            {0, 1},
                            {1, 1},
                            {-2, 0},
                            {-2, 1}
                    },
                    {
                            {0, 0},
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
                            {0, 0},
                            {0, 1},
                            {1, 1},
                            {-2, 0},
                            {-2, 1}
                    },
                    {
                            {0, 0},
                            {0, 1},
                            {-1, 1},
                            {2, 0},
                            {2, 1}
                    },
                    {
                            {0, 0},
                            {0, -1},
                            {1, -1},
                            {-2, 1},
                            {-2, -1}
                    },
                    {
                            {0, 0},
                            {0, -1},
                            {-1, -1},
                            {2, 0},
                            {2, -1}
                    }
            }
    };
    //</editor-fold>

    public static final int[][][][] iKickData = new int[][][][]{
            //Clockwise
            {
                    //Current rotation
                    {
                            //testNumber
                            //{rowOffset, colOffset}
                            {0, 0},
                            {0, -2},
                            {0, 1},
                            {-1, -2},
                            {2, 1}
                    },
                    {
                            {0, 0},
                            {0, -1},
                            {0, 2},
                            {2, -1},
                            {-1, 2}
                    },
                    {
                            {0, 0},
                            {0, 2},
                            {0, -1},
                            {1, 2},
                            {-2, -1}
                    },
                    {
                            {0, 0},
                            {0, 1},
                            {0, -2},
                            {-2, 1},
                            {1, -2}
                    }
            },
            //counterClockwise
            {
                    //Current rotation
                    {
                            //testNumber
                            //{rowOffset, colOffset}
                            {0, 0},
                            {0, -1},
                            {0, 2},
                            {2, -1},
                            {-1, 2}
                    },
                    {
                            {0, 0},
                            {0, 2},
                            {0, -1},
                            {1, 2},
                            {-2, -1}
                    },
                    {
                            {0, 0},
                            {0, 1},
                            {0, -2},
                            {-2, 1},
                            {1, -2}
                    },
                    {
                            {0, 0},
                            {0, -2},
                            {0, 1},
                            {-1, -2},
                            {2, 1}
                    }
            }
    };
}