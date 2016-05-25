# EasyFitter(轻松适配) #
> Android屏幕适配方案，直接对接设计稿参数设置进行开发，过程中，当应用所在设备与设置设计稿有所不同时不需要考虑其转化问题，Fitter将最大限度地完成适配。

效果图
---
    ........................

使用方法 
---
    // 加入项目
    dependencies {
     compile project(':library')
    }
    或
    // 直接引用
    dependencies {
     compile 'com.zhy:autolayout:1.4.3'
    }

    Eclipse ? //直接Copy代码咯~

 初始化
----
     public class App extends Application {
         @Override
         public void onCreate() {
             super.onCreate();
             // 这里设置标准设计稿
             Fitter.init(this,1280,720);
             // do something ...
         }
     }
 基类
----
     @Override
    public void setContentView(@LayoutRes int layoutResID) {
          super.setContentView(layoutResID);
          Fitter.doAdjust(this);
    }
    
    @Override
    public void setContentView(View view) {
         super.setContentView(view);
         Fitter.doAdjust(this);
    }
    
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
         super.setContentView(view, params);
         Fitter.doAdjust(this);
    }

> 至此，你可以正常使用原生控件，根据设计稿px参数，愉快的进行开发。当然，此适配未必是最优的一种解决方案，如果你有更好想法，不忘交流学习下哦。

其他适配方案：[AndroidAutoLayout](https://github.com/hongyangAndroid/AndroidAutoLayout) 
----
> 此适配解决方案也是不错的，开发过程中，只需要套用二次容器即可。与Fitter其中的优劣，你来补充... 。
    
    
    
    
    
    
    
    
    
    