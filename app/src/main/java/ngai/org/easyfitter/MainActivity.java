package ngai.org.easyfitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ngai.org.esayfit.Fitter;

public class MainActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void fitterListview(View view){
        startActivity(new Intent(this,FitterListviewActivity.class));
    }

    public void fitterGridview(View view){
        startActivity(new Intent(this,FitterGridviewActivity.class));
    }
}
