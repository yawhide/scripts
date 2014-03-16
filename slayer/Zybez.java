package scripts.slayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Zybez {
	
	private static String zybezString = "";
	
	public static int getPrice(String item) {
		int averagePrice = 0;
		openZybezItemData(item);
		averagePrice = getAveragePrice();
		return averagePrice;
	}
	
	private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        URLConnection uc = null;
        try {
            URL url = new URL(urlString);
            uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            uc.connect();
            reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
	
	private static void openZybezItemData(String ItemName){
		
		try {
			zybezString = readUrl("http://forums.zybez.net/runescape-2007-prices/api/"+ItemName.replaceAll("\\s","+"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static int getAveragePrice() {
		
        Pattern pattern = Pattern.compile("(?<=\"average\":\")[0-9]+");
        Matcher matcher = pattern.matcher(zybezString);

        while (matcher.find())
            return(Integer.parseInt(matcher.group()));
		return 0;
	}
}