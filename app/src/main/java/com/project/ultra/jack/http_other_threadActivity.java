package com.project.ultra.jack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project.ultra.myapplication.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class http_other_threadActivity extends AppCompatActivity {
    ArrayList<String > news,links;
    ArrayList<NewsItem> news2;
    ListView lv;
    ArrayAdapter adapter;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_other_thread);
        news= new ArrayList<>();
        news2=new ArrayList<>();
        lv= (ListView) findViewById(R.id.listView4);
        handler=new Handler();
        links =new ArrayList<>();

        new Thread(){

            public void run(){
                String sturl="http://udn.com/rssfeed/news/1";
                String result="";
                try {
                    URL url=new URL(sturl);
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    InputStream is= conn.getInputStream();
                    BufferedReader r=new BufferedReader(new InputStreamReader(is));
                    String x="";
                    StringBuilder sb=new StringBuilder();

                        Log.d("READ",x);
                        x=r.readLine();
                        while(x!=null)
                        {
                            sb.append(x);
                            x=r.readLine();

                        }
                    Log.d("READ",sb.toString());

                     result=sb.toString();



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(result));

                    int eventType = xpp.getEventType();
                    boolean  isTitle = false;
                    boolean isItem=false;
                    boolean isLink=false;
                    NewsItem currentItem=null;



                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if(eventType == XmlPullParser.START_DOCUMENT) {
                           Log.d("READ","Start document");
                        } else if(eventType == XmlPullParser.START_TAG) {

                            //Log.d("READ","Start tag "+xpp.getName());
                        if(xpp.getName().equals("title"))
                        {

                            isTitle=true;
                        }
                         if(xpp.getName().equals("item"))
                         {
                             isItem=true;
                             currentItem=new NewsItem();

                         }
                        if(xpp.getName().equals("link"))
                        {
                        isLink=true;
                        }
                        } else if(eventType == XmlPullParser.END_TAG) {
                          //  Log.d("READ","End tag "+xpp.getName());
                        if(xpp.getName().equals("title"))
                        {
                            isTitle=false;
                        }
                            if(xpp.getName().equals("item"))
                            {
                                isItem=false;
                                news2.add(currentItem);

                            }
                            if(xpp.getName().equals("link"))
                            {
                                isLink=false;
                            }

                        } else if(eventType == XmlPullParser.TEXT) {
                            if (isTitle == true && isItem==true)
                            {
                                Log.d("READ", "Text "+xpp.getText());
                                news.add(xpp.getText());
                                currentItem.mTitle=xpp.getText();
                            }
                            if (isLink == true && isItem==true)
                            {
                                Log.d("READ", "Text "+xpp.getText());
                                links.add(xpp.getText());
                                currentItem.mLink=xpp.getText();
                            }

                        }
                        eventType = xpp.next();
                    }
                    System.out.println("End document");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       String []s=new String[news.size()];
                        for(int i=0;i<news2.size();i++)
                        {
                            s[i]=news2.get(i).mTitle;
                        }

                       // adapter=new ArrayAdapter(http_other_threadActivity.this,android.R.layout.simple_list_item_1,news);
                        adapter=new ArrayAdapter(http_other_threadActivity.this,android.R.layout.simple_list_item_1,s);
                        lv.setAdapter(adapter);

                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                               // Uri uri= Uri.parse(links.get(position));
                                Uri uri= Uri.parse(news2.get(position).mLink);
                                Intent i=new Intent(Intent.ACTION_VIEW,uri);
                                startActivity(i);

                            }
                        });

                    }
                });

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
