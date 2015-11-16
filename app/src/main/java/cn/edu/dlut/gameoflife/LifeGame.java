package cn.edu.dlut.gameoflife;

import android.util.Log;

/**
 * Created by wujie on 2015/11/15.
 */
public class LifeGame {
    private static int[][] life;

    public LifeGame(int[][] originLife){
        this.life = originLife;
        Log.v("===TAG life===", life.toString());
    }

    public int[][] getNextGenLife(int[][] oldGenLife){
        this.life = oldGenLife;
        int[][] nextGen = new int[this.life.length][this.life[0].length];
        for(int i = 0; i < life.length; i++)
            for(int j = 0; j < life[i].length; j++ )
                nextGen[i][j] = getNextGenStatus(i + 1, j + 1);
        return nextGen;
    }
    /**
     *
     * @param x cell's row index (range 1 to r)
     * @param y cell's clomun index (range 1 to c)
     * @return the next status of the cell
     */
    private  final int getNextGenStatus(int x, int y){
        //no life living off the edge
        if((y == 1) || (y == life[0].length) || (x == 1 || x == life.length ))
            return 0;
        int neighborNums = getLivingNeighborNums(x, y);
        //rule
        if(neighborNums < 2 || neighborNums > 3 )
            return 0;
        if(neighborNums == 3)
            return 1;
        return life[x - 1][y - 1];
    }

    /**
     * get the number of living neighbor by giving x, y
     * @param x :the row index
     * @param y : the column index
     * @return the number of living neighbor
     */
    private final int getLivingNeighborNums(int x, int y){
        int cnt = 0;
        int[][] dir = {{-1,-1}, {-1,0}, {-1,1}, {1,-1}, {1,0}, {1,1},{0,-1}, {0,1}};
        for(int i = 0; i < 8; i++){
            int xx = dir[i][0] + x - 1;
            int yy = dir[i][1] + y - 1;
            cnt += life[xx][yy];
        }
        return cnt;
    }
}
