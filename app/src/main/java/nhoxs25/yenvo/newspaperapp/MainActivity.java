package nhoxs25.yenvo.newspaperapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;

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

import nhoxs25.yenvo.newspaperapp.appmenu.IteamAdapter;
import nhoxs25.yenvo.newspaperapp.appmenu.XMLDOMParser;
import nhoxs25.yenvo.newspaperapp.model.RssItem;
import nhoxs25.yenvo.newspaperapp.variable.Variables;

import static nhoxs25.yenvo.newspaperapp.R.id.listTrangWeb;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    List<RssItem> itemList1 = new ArrayList<>();
    private RecyclerView recyclerViewWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewWeb = (RecyclerView) findViewById(R.id.listTrangWeb);



        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            dialog = ProgressDialog.show(this, "", "Loading...");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new ReadXML(0).execute(Variables.LINKS[0][0]);
                }
            });
        }else {
            String name = "Bạn không có kết nối internet. \n" +
                    "Hãy bật Wifi hoặc 3G để sử dụng!";
//            listItemSlideMenu = new ArrayList<>();
//            listItemSlideMenu.add(new ItemSlideMenu(Variables.ICONS[0], "Kiki"));
            List<RssItem> cmt = new ArrayList<>();
            cmt.add(new RssItem());
            IteamAdapter adapter = new IteamAdapter(getApplicationContext(), cmt);
            recyclerViewWeb.setAdapter(adapter);

        }

    }
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

        private  String getXmlFromUrl(String link) {
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
                // defaultHttpClient
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
        @Override
        protected void onPostExecute(String s) {
            List<RssItem> itemList = new ArrayList<RssItem>();
            String kq = "";
            //RssItem item = new RssItem();
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

                item.setDescription(kq);
                itemList.add(item);
            }
            // Toast.makeText(ListRssActivity.this,""+itemList.get(0).getDescription(),Toast.LENGTH_LONG).show();
            if (dialog != null) {
                dialog.dismiss();
            }
            itemList1 = itemList;
            IteamAdapter adapter = new IteamAdapter(getApplicationContext(),paper, itemList);
            recyclerViewWeb.setAdapter(adapter);
        }
    }
}
