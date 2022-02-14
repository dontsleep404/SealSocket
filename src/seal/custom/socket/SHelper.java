package seal.custom.socket;

import com.google.gson.Gson;

public class SHelper {
	public static Object jsonParse(String data, Class<?> _class){
		Gson gson = new Gson();		
		return gson.fromJson(data, _class);
	}
	public static String stringify(Object object) {
		Gson gson = new Gson();	
		return gson.toJson(object);
	}
}