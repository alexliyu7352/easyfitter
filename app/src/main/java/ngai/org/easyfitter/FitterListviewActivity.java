package ngai.org.easyfitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ngai.org.esayfit.Fitter;

public class FitterListviewActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitter_listview);

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<String> datas = new ArrayList<String>();
        for (int i=0;i<60;i++){
            datas.add(String.valueOf(i));
        }
        listView.setAdapter(new FitterAdapter(this,datas));

                ListView listView2 = (ListView) findViewById(R.id.listView2);
        listView2.setAdapter(new FitterAdapter(this,datas));

    }

    public class FitterAdapter extends BaseAdapter {
        ArrayList<String> dataS;

        public FitterAdapter(FitterListviewActivity fitterListviewActivity, ArrayList<String> datas) {
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
                convertView = LayoutInflater.from(FitterListviewActivity.this).inflate(R.layout.item_fitter_listview,null);
            return convertView;
        }

        class ViewHolder{

        }
    }
}
