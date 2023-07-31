package com.sakura.request;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.sakura.types.AnimeItem;
import com.sakura.types.AnimeItemDetail;
import com.sakura.utils.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The class is intended for the webSpider-related work.
 */
public class Request {
    public static final int MODE_JAPAN = 0;
    public static final int MODE_CHINA = 1;

    /**
     * The method is intended to fetch the anime list on the website.<br/>
     * The method should run on net thread.
     *
     * @param mode      Require the request mode. Using Request.MODE_JAPAN or Request.MODE_CHINA
     * @param pageIndex The request index of the page.
     * @return The list of the AnimeItem lists.
     */
    public static List<AnimeItem> getAnimeList(int mode, int pageIndex) {
        StringBuilder bfs = new StringBuilder();
        if (mode == MODE_JAPAN)
            bfs.append(Constants.YHDM_URL_JP);
        else bfs.append(Constants.YHDM_URL_CN);
        if (pageIndex != 1)
            bfs.append(pageIndex).append(".html");
        return grabData(bfs.toString());
    }

    public static List<AnimeItem> searchAnimeList(String content) {
        return grabData(Constants.YHDM_URL_SEARCH + content);
    }

    private static List<AnimeItem> grabData(String url) {
        ArrayList<AnimeItem> res = null;
        try {
            res = new ArrayList<>();
            Document doc = Jsoup.connect(url).get();
            res = new ArrayList<>();
            for (Element e : doc.select(Constants.YHDM_LIST_SELECTOR)) {
                AnimeItem unit = new AnimeItem();
                unit.img = e.select("img").attr("src");
                unit.url = Constants.YHDM_URL + e.select("a").get(0).attr("href");
                unit.title = e.select("img").attr("alt");
                unit.state = e.select("font").text();
                res.add(unit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static AnimeItemDetail getAnimeItemDetail(String root) {
        AnimeItemDetail res = new AnimeItemDetail();
        try {
            Document doc = Jsoup.connect(root).get();
            res.title = doc.select(Constants.YHDM_DETAIL_TITLE_SELECTOR).text();
            res.image = doc.select(Constants.YHDM_DETAIL_IMAGE_SELECTOR).attr("src");
            res.description = doc.select(Constants.YHDM_DETAIL_DESCRIPTION_SELECTOR).text();
            res.status = doc.select(Constants.YHDM_DETAIL_STATUS_SELECTOR).text();
            ArrayList<AnimeItemDetail.Columns> columns = new ArrayList<>();
            for (Element x : doc.select(Constants.YHDM_DETAIL_COLUMNS_SELECTOR))
                columns.add(new AnimeItemDetail.Columns(x.text(), Constants.YHDM_URL + x.attr("href")));
            res.columns = columns;
        } catch (IOException e) {
            res.availability=false;
            e.printStackTrace();
        }
        return res;
    }

    public static String getPlayUrl(String origin) {
        String res = "";
        try {
            Document doc = Jsoup.connect(origin).get();
            res = doc.select(Constants.YHDM_VIDEO_URL_SELECTOR)
                    .attr("data-vid");
            res = res.substring(0, res.lastIndexOf('$'));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static boolean isWebSupported(String origin) {
        try {
            String playUrl = getPlayUrl(origin);
            String m3u8 = Jsoup.connect(playUrl).ignoreContentType(true).maxBodySize(Integer.MAX_VALUE).execute().body();
            /*
             * 预案：m3u8可能同源，采用相对路径，后缀可能为.m3u8
             */
            for (String x : m3u8.split("\n"))
                if (x.startsWith("https://") || x.startsWith("http://"))
                    if (x.endsWith(".ts")) return true;
                    else break;
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static void badResponse(Context context){
        Looper.prepare();
        Toast.makeText(context, "网络请求失败", Toast.LENGTH_SHORT).show();
    }
}

