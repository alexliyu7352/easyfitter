package ngai.org.easyfitter;

import android.app.Application;

import ngai.org.esayfit.Fitter;

/**
 * Created by Administrator on 2016/5/20.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fitter.init(this,720,1280);
    }
}
