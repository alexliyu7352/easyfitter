package ngai.org.easyfitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

import ngai.org.esayfit.Fitter;

public class FitterGridviewActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitter_gridview);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        ArrayList<String> datas = new ArrayList<String>();
        for (int i=0;i<60;i++){
            datas.add(String.valueOf(i));
        }
        gridView.setAdapter(new FitterAdapter(datas));


        GridView gridView2 = (GridView) findViewById(R.id.gridView2);
        gridView2.setAdapter(new FitterAdapter(datas));
    }


    public class FitterAdapter extends BaseAdapter {
        ArrayList<String> dataS;

        public FitterAdapter(ArrayList<String> datas) {
            this.dataS = datas;
        }

        @Override
        public int getCount() {
            return dataS.size();
        }

        @Override
        public Object getItem(int position) {
            return dataS.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = LayoutInflater.from(FitterGridviewActivity.this).inflate(R.layout.item_fitter_gridview,null);
            return convertView;
        }

        class ViewHolder{

        }
    }
}
