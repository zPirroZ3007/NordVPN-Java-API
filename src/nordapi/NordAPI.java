/**
 * @author zPirroZ3007
 */
package nordapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.JSONObject;

public class NordAPI
{
	/*
	 * Get the individual server load
	 */
	public String serverLoad(String server) throws MalformedURLException
	{
		URL url = new URL("https://api.nordvpn.com/server/stats/" + server);

		return httpRequest(url, null);
	}

	/*
	 * Get user account details, using verified token
	 */
	public String userDetails(String email, String password) throws NoSuchAlgorithmException, IOException
	{
		URL token = new URL("https://api.nordvpn.com/token/token/" + email);
		JSONObject tokenJson = new JSONObject(httpRequest(token, null));

		String firsthash = hash(tokenJson.getString("salt") + password);
		String secondhash = hash(firsthash + tokenJson.getString("key"));

		URL validateToken = new URL("https://api.nordvpn.com/token/verify/" + tokenJson.getString("token") + "/" + secondhash);

		URL dataByToken = new URL("https://api.nordvpn.com/user/databytoken");

		try
		{
			validateToken.openStream();
		}
		catch (Exception ex)
		{
			return "unauthorized";
		}

		return httpRequest(dataByToken, tokenJson.getString("token"));
	}

	/*
	 * Check if user account is registered
	 */
	public boolean isRegistered(String email, String password) throws MalformedURLException, NoSuchAlgorithmException
	{
		URL token = new URL("https://api.nordvpn.com/token/token/" + email);
		JSONObject tokenJson = new JSONObject(httpRequest(token, null));

		String firsthash = hash(tokenJson.getString("salt") + password);
		String secondhash = hash(firsthash + tokenJson.getString("key"));

		URL verifyUrl = new URL("https://api.nordvpn.com/token/verify/" + tokenJson.getString("token") + "/" + secondhash);

		if (httpRequest(verifyUrl, null).contains("true"))
		{
			return true;
		}

		return false;
	}

	/*
	 * Get current loads for all servers
	 */
	public String serverStats() throws MalformedURLException
	{
		URL url = new URL("https://api.nordvpn.com/server/stats");

		return httpRequest(url, null);
	}

	/*
	 * Get detailed information on all servers
	 */
	public String serverDetails() throws MalformedURLException
	{
		URL url = new URL("https://api.nordvpn.com/server");

		return httpRequest(url, null);
	}

	/*
	 * Get user's current IP address
	 */
	public String address() throws MalformedURLException
	{
		URL url = new URL("https://api.nordvpn.com/user/address");

		return httpRequest(url, null);
	}

	/*
	 * Display NordVPN nameservers
	 */
	public String nameServers() throws MalformedURLException
	{
		URL url = new URL("https://api.nordvpn.com/dns/smart");

		return httpRequest(url, null);
	}
	
	
	
	/*
	 * Do an HTTP request
	 */
	private static String httpRequest(URL url, String nToken)
	{
		try
		{
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			con.setRequestProperty("User-Agent", "NordVPN_Client_5.56.780.0");
			con.setRequestProperty("Host", "api.nordvpn.com");
			con.setRequestProperty("Connection", "Close");

			if (nToken != null)
			{
				con.setRequestProperty("nToken", nToken);
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();

			return response.toString();
		}
		catch (Exception ex)
		{
			return "unauthorized";
		}
	}

	/*
	 * Hash a String
	 */
	private static String hash(String toHash) throws NoSuchAlgorithmException
	{

		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(toHash.getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++)
		{
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}

		// convert the byte to hex format method 2
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < byteData.length; i++)
		{
			String hex = Integer.toHexString(0xff & byteData[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}

		return hexString.toString();
	}
}