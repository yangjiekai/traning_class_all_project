package com.project.ultra.jack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.ultra.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class http_get_picture extends AppCompatActivity {
    Bitmap bitmap;
    Handler handler;
    ImageView img;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_get_picture);
        tv= (TextView) findViewById(R.id.textView21);
        handler=new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                double s= (double) msg.obj;
                        tv.setText("progress"+s);


            }
        };
        img= (ImageView) findViewById(R.id.imageView3);



    }

    public void click123(View v) {
        new Thread() {

            public void run() {
                String strurl = "http://www.pcschool.com.tw/2015/images/logo.png";
              //  String result = "";
                 ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte b[] = new byte[64];
              //  URL url = null;
                try {
                    URL   url = new URL(strurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    Log.d("IMG", "conected");
                    InputStream is=conn.getInputStream();
                   double sum=0;
                    int readSize=0;
                    double totalLength= conn.getContentLength();
                  //  conn.disconnect();

                    while((readSize=is.read(b))>0)
                    {
                     bos.write(b,0,readSize);
                        sum+=readSize;
                        Message message=handler.obtainMessage(1,sum/totalLength*100);
                        handler.sendMessage(message);
                    }
                    byte[] result=bos.toByteArray();
                    Log.d("IMG","to byte array finished array length:"+result.length);
                    bitmap =BitmapFactory.decodeByteArray(result,0,result.length);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            img.setImageBitmap(bitmap);
                        }
                    });



                }catch(ProtocolException e){
                    e.printStackTrace();
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        }.start();
    }
}
