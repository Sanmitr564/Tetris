public class RotationValues {
    //Coordinates are listed as {rowOffset, colOffset}/{yOffset, xOffset}
    //All values are positions relative to the center of rotation of the tetromino
    //Arrays are called as [facingDirection][blockNumber][row/colOffset]
    //For blocks that rotate around a corner, (0,0) is the block to the bottom left of the corner

    public static int[][][] jValues = new int[][][]{
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
    };

    public static int[][][] lValues = new int[][][]{
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
    };
}
