public class RotationValues {
    //Coordinates are listed as {yOffset, xoffset}
    //use Global.X and Global.Y to grab the correct values
    public static int[][][] jValues = new int[][][]{
            {
                    {1, -1},
                    {0, -1},
                    {0, 0},
                    {0, 1}
            },
            {
                    {1,1},
                    {1,0},
                    {0,0},
                    {-1,0}
            },
            {
                    {1,-1},
                    {0,1},
                    {0,0},
                    {0,-1}
            },
            {
                    {-1,-1}
            }
    };
}
