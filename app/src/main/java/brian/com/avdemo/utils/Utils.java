package brian.com.avdemo.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import brian.com.avdemo.model.ItemModel;

public class Utils {

    static MediaPlayer   player=null;
    private static char[] charA = { 'à', 'á', 'ạ', 'ả', 'ã',
            'â', 'ầ', 'ấ', 'ậ', 'ẩ', 'ẫ', 'ă', 'ằ', 'ắ', 'ặ', 'ẳ', 'ẵ' };
    private static char[] charE = { 'ê', 'ề', 'ế', 'ệ', 'ể', 'ễ',
            'è', 'é', 'ẹ', 'ẻ', 'ẽ' };
    private static char[] charI = { 'ì', 'í', 'ị', 'ỉ', 'ĩ' };
    private static char[] charO = { 'ò', 'ó', 'ọ', 'ỏ', 'õ',
            'ô', 'ồ', 'ố', 'ộ', 'ổ', 'ỗ',
            'ơ', 'ờ', 'ớ', 'ợ', 'ở', 'ỡ' };
    private static char[] charU = { 'ù', 'ú', 'ụ', 'ủ', 'ũ',
            'ư', 'ừ', 'ứ', 'ự', 'ử', 'ữ' };
    private static char[] charY = { 'ỳ', 'ý', 'ỵ', 'ỷ', 'ỹ' };
    private static char[] charD = { 'đ', ' ' };

 static    String charact = String.valueOf(charA, 0, charA.length)
            + String.valueOf(charE, 0, charE.length)
            + String.valueOf(charI, 0, charI.length)
            + String.valueOf(charO, 0, charO.length)
            + String.valueOf(charU, 0, charU.length)
            + String.valueOf(charY, 0, charY.length)
            + String.valueOf(charD, 0, charD.length);

    public static char getAlterChar(char pC) {
        if ((int) pC == 32) {
            return ' ';
        }

        char tam = pC;// Character.toLowerCase(pC);

        int i = 0;
        while (i < charact.length() && charact.charAt(i) != tam) {
            i++;
        }
        if (i < 0 || i > 67)
            return pC;

        if (i == 66) {
            return 'd';
        }
        if (i >= 0 && i <= 16) {
            return 'a';
        }
        if (i >= 17 && i <= 27) {
            return 'e';
        }
        if (i >= 28 && i <= 32) {
            return 'i';
        }
        if (i >= 33 && i <= 49) {
            return 'o';
        }
        if (i >= 50 && i <= 60) {
            return 'u';
        }
        if (i >= 61 && i <= 65) {
            return 'y';
        }
        return pC;
    }

    public static String convertString(String pStr) {
        String convertString = pStr.toLowerCase();
//        Character[] returnString = new Character[convertString.length()];
        for (int i = 0; i < convertString.length(); i++) {
            char temp = convertString.charAt(i);
            if ((int) temp < 97 || temp > 122) {
                char tam1 = getAlterChar(temp);
                if ((int) temp != 32)
                    convertString = convertString.replace(temp, tam1);
            }
        }
        return convertString;
    }

    public static List<ItemModel> loadJSONFromAsset(Context mContext, String folderName, String fileName) {

        List<ItemModel> mItemList;
        try {

            String path = folderName + "/" + fileName + ".js";
            InputStream is = mContext.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String   json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            mItemList = Arrays.asList(gson.fromJson(json, ItemModel[].class));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return mItemList;
    }

    public static void playSound(Context mContext, int soundNum) {
        String path = "audios/" + (soundNum - 100) + ".mp3";
        try {
            if (player!=null&&player.isPlaying()) {
                player.stop();
                player.release();
            }
            AssetFileDescriptor afd = mContext.getAssets().openFd(path);
            player = new  MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();

            player.prepare();
            player.setVolume(100, 100);
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean filter(String search, String text) {
        List<String> tokensText = new ArrayList<>();
        List<String> tokensSearch = new ArrayList<>();

        //remove sign
        String newText = text.replace(",","").replace("?","").replace(".","").replace("!","").replace("'","").replace("’","");
        String newSearch = search.replace(",","").replace("?","").replace(".","").replace("!","").replace(" ","").replace("'","");
        StringTokenizer stText = new StringTokenizer(newText);
        //("---- Split by space ------");
        while (stText.hasMoreElements()) {
            tokensText.add(stText.nextElement().toString());
        }

        StringTokenizer stSearch = new StringTokenizer(search);
        //("---- Split by space ------");
        while (stSearch.hasMoreElements()) {
            tokensSearch.add(stSearch.nextElement().toString());
        }

        if (tokensSearch.size() < 2) {
            for (int i = 0; i < tokensText.size(); i++) {
                if (tokensText.get(i).equals(newSearch)) {
                    return true;
                }
            }
        } else {
            String reNewSearch = search.replace(",","").replace("?","").replace(".","").replace("!","").replace("'","");
            if (newText.contains(reNewSearch)) {
                return true;
            }
        }

        return false;
    }
}
