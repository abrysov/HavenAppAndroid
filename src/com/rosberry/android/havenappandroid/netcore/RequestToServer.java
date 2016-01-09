/** 
 * Created by abrysov at Mar 10, 2012
 */
package com.rosberry.android.havenappandroid.netcore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author abrysov
 *
 */
public class RequestToServer {
	
	private final String sLoginURL  		= "http://havenapp.nl/api/login.php";
	private final String sLocateURL 		= "http://havenapp.nl/api/locate.php";
	private final String sBestekURL 		= "http://havenapp.nl/api/bestek.php";
	private final String sCrowURL   		= "http://havenapp.nl/api/crow.php";
	private final String sControleURL   	= "http://havenapp.nl/api/controle.php";
	private final String sHelpURL   		= "http://havenapp.nl/api/help.php";
	private final String sDeelgebiedURL   	= "http://havenapp.nl/api/deelgebied.php";
	private final String sAddressURL   		= "http://havenapp.nl/api/adres.php";
	private final String sStatusURL   		= "http://havenapp.nl/api/status.php";
//	private final String sPostURL			= "http://havenapp.nl/api/post.php";
	
	private final String KEY = "8ie4lusplayoe67uties";
	private String requestBody = null;
	/**
	 * Login API
	* Constructor for a login request.
	*/
	public RequestToServer (String sLogin, String sPassword ) {
		
		requestBody = sLoginURL;
		requestBody += "?";
		requestBody += "key=";
		requestBody += KEY;
		requestBody += "&";
		requestBody += "email=";
		requestBody += sLogin;
		requestBody += "&";
		requestBody += "pwd=";
		requestBody += md5(sPassword);
		
	}
	
	/**
	 * Locate API
	* Constructor for a GPS request.
	*/
	public RequestToServer (Double _dLatitude, Double _dLogitude ) {
		
		requestBody = sLocateURL;
		requestBody += "?";
		requestBody += "key=";
		requestBody += KEY;
		requestBody += "&";
		requestBody += "lat=";
		
		if (_dLatitude != null){
			requestBody += _dLatitude.toString();	
		}else{
			requestBody += "0.0";
		}
		
		requestBody += "&";
		requestBody += "long=";
		
		if (_dLogitude != null) {
			requestBody += _dLogitude.toString();
		}else{
			requestBody += "";
		}

	}
	
	/**
	 * Besteck API
	* Constructor for a subregion(deelgebied) request.
	*/
	public RequestToServer (RequestAPIType _type, String _sDeelgebied ) { //?key=8ie4lusplayoe67uties&deelgebied=DEELGEBIED
		
		requestBody = sBestekURL;
		requestBody += "?";
		requestBody += "key=";
		requestBody += KEY;
		requestBody += "&";
		requestBody += "deelgebied=";
		requestBody += _sDeelgebied;
		
		
	}	
	/**
	 * Crow API
	 * @param _type
	 * @param _deelgebied
	 * @param _bestek
	 */
	public RequestToServer (RequestAPIType _type, String _deelgebied, String _bestek ) {
		
		requestBody = sCrowURL;
		requestBody += "?";
		requestBody += "key=";
		requestBody += KEY;
		requestBody += "&deelgebied=";
		requestBody += _deelgebied;
		requestBody += "&bestek=";
		requestBody += _bestek;
		
	}
	/**
	 * Status API
	 * @param _identifier
	 * @param _type
	 */
	public RequestToServer (String _identifier, RequestAPIType _type){
		
		requestBody = sStatusURL;
		requestBody += "?";
		requestBody += "key=";
		requestBody += KEY;
		requestBody += "&identifier=";
		requestBody += _identifier;
		
	}
	/**
	 * Control API
	 * @param _identifier
	 * @param _deelgebied
	 * @param _type
	 */
	public RequestToServer (String _identifier, String _deelgebied, RequestAPIType _type){
		
		requestBody = sControleURL;
		requestBody += "?";
		requestBody += "key=";
		requestBody += KEY;
		requestBody += "&identifier=";
		requestBody += _identifier;
		requestBody += "&deelgebied=";
		requestBody += _deelgebied;
	
	}
	
	/**
	 * Help Api
	 * DEELGEBIED API
	 * ADDRESSES API
	 * @param type
	 */
	public RequestToServer (RequestAPIType type) {
		
		switch (type) {
		case HELP:
			
			requestBody = sHelpURL;
			requestBody += "?";
			requestBody += "key=";
			requestBody += KEY;
			
			break;
			
		case DEELGEBIED:
			
			requestBody = sDeelgebiedURL;
			requestBody += "?";
			requestBody += "key=";
			requestBody += KEY;
			
			break;
			
		case ADDRESSES:
			
			requestBody = sAddressURL;
			requestBody += "?";
			requestBody += "key=";
			requestBody += KEY;
			
			break;
			
		default:
			break;
		}
		
	}
	
	public String getREQUEST (){
		
		return requestBody;
	}
	
	private String md5(String _in) {
		    MessageDigest digest;
		    try {
		        digest = MessageDigest.getInstance("MD5");
		        digest.reset();
		        digest.update(_in.getBytes());
		        byte[] a = digest.digest();
		        int len = a.length;
		        StringBuilder sb = new StringBuilder(len << 1);
		        for (int i = 0; i < len; i++) {
		            sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
		            sb.append(Character.forDigit(a[i] & 0x0f, 16));
		        }
		        return sb.toString();
		    } catch (NoSuchAlgorithmException e) {
		    	e.printStackTrace(); 
		    }
		    return null;
		}

}

/*

API (JSON): http://havenapp.nl/api/login.php?key=8ie4lusplayoe67uties&user=USERNAME&pwd=PASSWORD

For testing purposes use user: admin and password: test (make sure to md5 encrypt password). For example:
http://havenapp.nl/api/login.php?key=8ie4lusplayoe67uties&user=admin&pwd=098f6bcd4621d373cade4e832627b4f6
returns:
{"check":"OK","name":"Jan Smit","role":"Beheerder"}

 ---
 
 API (JSON): http://havenapp.nl/api/locate.php?key=8ie4lusplayoe67uties&lat=LATITUDE&long=LONGITUDE

For testing purposes you can use any coordinates. API will return sample content:

{"check":"OK","street":"Coenhavenweg","number":"2","deelgebied":"101"}

 ---

API (JSON): http://havenapp.nl/api/bestek.php?key=8ie4lusplayoe67uties&deelgebied=DEELGEBIED

For testing purposes use deelgebied: 101. API will return sample content:

{"check":"OK","list":{"110210":"Groen-beplanting-grof vuil","110220":"Groen-beplanting-zwerfafval grof","110230":"Groen-beplanting-zwerfafval fijn","110310":"Groen-boomspiegel-zwerfafval fijn","110320":"Groen-boomspiegel-zwerfafval grof"}}

 ---

API (JSON): http://havenapp.nl/api/crow.php?key=8ie4lusplayoe67uties&deelgebied=DEELGEBIED&bestek=BESTEK

For testing purposes use deelgebied: 101 and bestek: any of the numbers from the list on previous page (for example 110210). API will return sample content:
{"check":"OK","acceptable":"A+","images":{"A+":"http:\/\/havenapp.nl\/img\/crow\/test1.png","A":"http:\/\/havenapp.nl\/img\/crow\/test2.png","B":"http:\/\/havenapp.nl\/img\/crow\/test3.png","C":"http:\/\/havenapp.nl\/img\/crow\/test4.png","D":"http:\/\/havenapp.nl\/img\/crow\/test5.png"}}


======

API (JSON):
http://havenapp.nl/api/status.php?key=8ie4lusplayoe67uties

Sample output:

{"1":{"street":"Coenhavenweg","number":"2","date":"08\/02\/2012 13:00","status":"OPEN","bebied":"101","bestek":"110210 Groen-beplanting-grof vuil","acceptable":"A+","reported":"A","name":"Jan Smit","photo":"http:\/\/havenapp.nl\/img\/photo\/photo1.jpg","comments":"Geen opmerkingen"},"2":{"street":"La Guardiaweg","number":"3","date":"06\/02\/2012 15:23","status":"IN BEHANDELING","bebied":"101","bestek":"110210 Groen-beplanting-grof vuil","acceptable":"A+","reported":"B","name":"Jan Smit","photo":"http:\/\/havenapp.nl\/img\/photo\/photo2.jpg","comments":"Geen opmerkingen"}}


======

*/
 