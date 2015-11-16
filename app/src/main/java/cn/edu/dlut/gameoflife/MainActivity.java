package cn.edu.dlut.gameoflife;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {
    long iterationTime = 1 * 200;
    int[][] life = new int[][]{ {0,0,0,0,0,0,0,0,0,0},
                                {0,0,1,1,0,0,0,0,0,0},
                                {0,0,1,1,1,1,0,0,0,0},
                                {0,0,0,0,1,1,0,0,0,0},
                                {0,0,0,0,0,1,0,0,0,0},
                                {0,1,1,0,1,1,1,0,0,0},
                                {0,1,1,0,0,1,1,0,0,0},
                                {0,0,1,1,0,1,0,0,0,0},
                                {0,0,1,1,0,1,0,0,0,0},
                                {0,0,0,1,1,0,1,1,0,0},
                                {0,0,0,1,1,0,1,1,0,0},
                                {0,1,1,0,0,1,1,0,0,0},
                                {0,0,1,1,0,1,0,0,0,0},
                                {0,0,1,1,0,1,0,0,0,0},
                                {0,0,0,1,1,0,1,1,0,0},
                                {0,0,0,1,1,0,1,1,0,0},
                                {0,0,0,1,1,0,1,1,0,0},
                                {0,0,0,1,1,0,1,1,0,0},
                                {0,1,1,0,0,1,1,0,0,0},
                                {0,0,1,1,0,1,0,0,0,0},
                                {0,0,1,1,0,1,0,0,0,0},
                                {0,0,0,1,1,0,1,1,0,0},
                                {0,0,0,1,1,0,1,1,0,0},
                                {0,0,1,1,0,1,0,0,0,0},
                                {0,0,0,1,1,0,1,1,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                };
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //update view
            if(msg.what == 0x123){
                Log.v("===TAG===", "updateView()");
                updateView();
            }

        }
    } ;

    GridView mGridView;
    GridViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("===TAG life in ui===", life.toString());
        mGridView = (GridView)findViewById(R.id.cellGrid);
        adapter = new GridViewAdapter(this.life.length, this.life[0].length);
        mGridView.setAdapter(adapter);
        new Thread(new LifeThread()).start();
    }

    private void updateView(){
        adapter.notifyDataSetChanged();
    }

    class LifeThread implements Runnable{
        LifeGame lifeGame ;
        public LifeThread(){
            lifeGame = new LifeGame(MainActivity.this.life);
        }
        @Override
        public void run() {
            //
            Log.v("===TAG===", "run");
            while(true){
                MainActivity.this.life = lifeGame.getNextGenLife(MainActivity.this.life);
                try {
                    Thread.currentThread().sleep(MainActivity.this.iterationTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 0x123;
                mHandler.sendMessage(msg);
            }
        }
    }

    class GridViewAdapter extends BaseAdapter{
        int rowNums,colNums;
        public GridViewAdapter(int rowNums, int colNums){
            this.colNums = colNums;
            this.rowNums = rowNums;
        }
        @Override
        public int getCount() {
            Log.v("===TAG===","count is " + rowNums * colNums);
            return rowNums * colNums;
        }

        @Override
        public Object getItem(int position) {
            int rowIndex = position / colNums;
            int colIndex = position - (rowIndex * colNums);
            return MainActivity.this.life[rowIndex][colIndex];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.cell,null);
                viewHolder = new ViewHolder();
                viewHolder.view = convertView;
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }

            int rowIndex = position / colNums;
            int colIndex = position - (rowIndex * colNums);
            int res = MainActivity.this.life[rowIndex][colIndex] == 0 ?
                    R.color.black : R.color.white;
            Log.v("===TAG===","res is " + res);
            Log.v("===TAG===","position is " + position);
            Log.v("===TAG===","life[x][y] is " + MainActivity.this.life[rowIndex][colIndex]);
            viewHolder.view.setBackgroundColor(getResources().getColor(res));
            return convertView;
        }
    }

    static class ViewHolder{
        private View view;
    }

}
