package nhoxs25.yenvo.newspaperapp.appmenu;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import nhoxs25.yenvo.newspaperapp.R;
import nhoxs25.yenvo.newspaperapp.adapter.NewsAdapter;
import nhoxs25.yenvo.newspaperapp.adapter.SlidingMenuAdapter;
import nhoxs25.yenvo.newspaperapp.model.ItemSlideMenu;
import nhoxs25.yenvo.newspaperapp.model.RssItem;
import nhoxs25.yenvo.newspaperapp.variable.Variables;

/**
 * Created by yenvo on 16/01/2017.
 */

public class ListRssActivity extends AppCompatActivity {
    private List<ItemSlideMenu> listItemSlideMenu;
    private List<ItemSlideMenu> listItemDanhMuc;

    private SlidingMenuAdapter adapter, adapterDanhMuc;

    private ListView listViewSliding;
    private ListView listTrangWeb;
    private ListView listChiTietWeb;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private int paper = 0;
    private String titleChoose;

    List<RssItem> itemList1 = new ArrayList<>();
    private ProgressDialog dialog;

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rss);

        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listTrangWeb = (ListView) findViewById(R.id.listTrangWeb);
        listChiTietWeb = (ListView) findViewById(R.id.lv_right);
        listViewSliding.setTag(0);
        listChiTietWeb.setTag(1);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if (isConnected) {

            dialog = ProgressDialog.show(this, "", "Loading...");
            //runOnUiThread chạy một AsyncTask
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new ReadXML(0).execute(Variables.LINKS[0][0]);
                }
            });

            listItemSlideMenu = new ArrayList<>();

            for (int i = 0; i < Variables.PAPERS.length; i++) {
                listItemSlideMenu.add(new ItemSlideMenu(Variables.ICONS[i], Variables.PAPERS[i]));
            }
            adapter = new SlidingMenuAdapter(this, listItemSlideMenu);
            listViewSliding.setAdapter(adapter);

            listItemDanhMuc = new ArrayList<>();
            for (int i = 0; i < Variables.CATEGORIES[0].length; i++) {
                listItemDanhMuc.add(new ItemSlideMenu(Variables.ICON_ITEM[0][i], Variables.CATEGORIES[0][i]));
            }


            adapterDanhMuc = new SlidingMenuAdapter(this, listItemDanhMuc);
            listChiTietWeb.setAdapter(adapterDanhMuc);

            // hien thi icon open/close sliding list
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // set Tilte
            setTitle(listItemSlideMenu.get(0).getTitle());
            titleChoose = listItemSlideMenu.get(0).getTitle();
            //item selected
            listViewSliding.setItemChecked(0, true);

            // close menu
            drawerLayout.closeDrawer(listViewSliding);
            //replaceFragment(0);
            listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    setTitle(listItemSlideMenu.get(position).getTitle());
                    titleChoose = (listItemSlideMenu.get(position).getTitle());
                    listViewSliding.setItemChecked(position, true);
                    paper = position;


                    listItemDanhMuc = new ArrayList<>();
                    for (int i = 0; i < Variables.CATEGORIES[position].length; i++) {
                        listItemDanhMuc.add(new ItemSlideMenu(Variables.ICON_ITEM[position][i], Variables.CATEGORIES[position][i]));
                    }
                    adapterDanhMuc = new SlidingMenuAdapter(ListRssActivity.this, listItemDanhMuc);
                    listChiTietWeb.setAdapter(adapterDanhMuc);


                    listTrangWeb.setAdapter(null);
                    dialog = ProgressDialog.show(ListRssActivity.this, "", "Loading " + Variables.CATEGORIES[position][0] + "...");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new ReadXML(position).execute(Variables.LINKS[position][0]);
                        }
                    });
                    listTrangWeb.clearTextFilter();
                    drawerLayout.closeDrawer(listViewSliding);

                }
            });

            listChiTietWeb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    listViewSliding.setItemChecked(position, true);
                    setTitle(Variables.CATEGORIES[paper][position] + " | " + titleChoose);
                    listTrangWeb.setAdapter(null);
                    dialog = ProgressDialog.show(ListRssActivity.this, "", "Loading " + Variables.CATEGORIES[paper][position] + " ...");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new ReadXML(paper).execute(Variables.LINKS[paper][position]);
                        }
                    });
                    listTrangWeb.clearTextFilter();
                    drawerLayout.closeDrawer(listChiTietWeb);
                }
            });

            listTrangWeb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ListRssActivity.this, DisplayBaiBao.class);
                    intent.putExtra("link", itemList1.get(position).getLink());
                    intent.putExtra("title", itemList1.get(position).getTitle());
                    startActivity(intent);
                }
            });
        } else {
            String name = "Bạn không có kết nối internet. \n" +
                    "Hãy bật Wifi hoặc 3G để sử dụng!";
            listItemSlideMenu = new ArrayList<>();
            List<String> cmt = new ArrayList<String>();
            cmt.add(name);
            ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cmt);
            listTrangWeb.setAdapter(ad);
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_opened) {
            @Override
            public void onDrawerOpened(View drawerView) {

                int state = (int) drawerView.getTag();
                if (state == 0) {
                    super.onDrawerOpened(drawerView);
                    setTitle("Chọn Website");
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                int state = (int) drawerView.getTag();
                if (state == 0) {
                    super.onDrawerClosed(drawerView);
                    setTitle(listItemSlideMenu.get(paper).getTitle());
                }
//                else if (state ==1){
//                    setTitle(listItemSlideMenu.get(paper).getTitle());
//                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                int state = (int) drawerView.getTag();
                if (state == 0) {
                    super.onDrawerSlide(drawerView, slideOffset);
                }
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    // url, % tai trang về, kết quả nhận được.
    public class ReadXML extends AsyncTask<String, Integer, String> {
        private int paper;

        public ReadXML(int paper) {

            this.paper = paper;
        }

        @Override
        protected String doInBackground(String... params) {
            String kq = "";
            if (paper < 3) {
                kq = getXmlFromUrlNomal(params[0]);
            } else {
                kq = getXmlFromUrl(params[0]);
            }
            return kq;
        }

        @Override
        protected void onPostExecute(String s) {
            List<RssItem> itemList = new ArrayList<RssItem>();
            String kq = "";
            //DOM cho phép lấy: NodeLists, .getElementsByTagName() ,
            // .item(i), .getName() , .getValue() , .getFirstChild() , .getAttributes(),…
            XMLDOMParser parser = new XMLDOMParser();
            Document doc = parser.getDocument(s);
            NodeList nodeList = doc.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                RssItem item = new RssItem();
                Element e = (Element) nodeList.item(i);

                NodeList titleNode = e.getElementsByTagName("title");
                Element titleElement = (Element) titleNode.item(0);
                item.setTitle(titleElement.getFirstChild().getNodeValue());

                NodeList linkNode = e.getElementsByTagName("link");
                Element linkElement = (Element) linkNode.item(0);
                item.setLink(linkElement.getFirstChild().getNodeValue());

                NodeList pubDateNode = e.getElementsByTagName("pubDate");
                Element pubDateElement = (Element) pubDateNode.item(0);
                item.setDate(pubDateElement.getFirstChild().getNodeValue());

                NodeList node = e.getElementsByTagName("description");
                Element desElment = (Element) node.item(0);
                kq = setDescription(desElment.getFirstChild().getNodeValue());

                item.setImg(kq);
                itemList.add(item);
            }
            if (dialog != null) {
                dialog.dismiss();
            }
            itemList1 = itemList;
            NewsAdapter adapter = new NewsAdapter(ListRssActivity.this, paper, itemList);
            listTrangWeb.setAdapter(adapter);
        }
    }

    public String setDescription(String description) {
        //so sanh src co ton tai trong description khong
        if (description.contains("src=")) {
            int indexOf = 0;
            //lay vi tri xuat hien src + voi 5, sau do lay tu vi tri do cho den het chuoi
            description = description.substring(description.indexOf("src=") + 5, description.length());
            if (description.contains(".jpg'")) {
                indexOf = description.indexOf(".jpg'" + 4);
            } else {
                indexOf = description.indexOf("\"");
            }
            description = description.substring(0, indexOf);
        }
        return description;
    }

    //2 kieu lấy toàn bộ xml về
    private static String getXmlFromUrl(String link) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(link);

            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public String getXmlFromUrlNomal(String url) {
        String xml = null;

        try {
            // defaultHttpClient //lấy toàn bộ dữ l trong http đổ vào 1 chuỗi Str
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            if (drawerLayout.isDrawerOpen(listChiTietWeb)) {
                drawerLayout.closeDrawer(listChiTietWeb);
            }
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.home_menu) {
            if (drawerLayout.isDrawerOpen(listChiTietWeb)) {
                drawerLayout.closeDrawer(listChiTietWeb);
            } else {
                drawerLayout.openDrawer(listChiTietWeb);
                drawerLayout.closeDrawer(listViewSliding);
            }
            return true;
        }
        if (id == R.id.home) {
            Toast.makeText(this, "Design By Yen Vo", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
}
