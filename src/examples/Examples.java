package examples;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import nordapi.NordAPI;

public class Examples
{
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException
    {
    	NordAPI api = new NordAPI();
    	
    	// Get current loads of all servers
    	System.out.println(api.serverStats());
    	
    	// Get detailed information on all servers
    	System.out.println(api.serverDetails());
    	
    	// Check if user account "test@example.com" with password "test" is registered.
    	System.out.println(api.isRegistered("test@example.com", "test"));
    	
    	// Get the details of user account "test@example.com" with password "test"
    	System.out.println(api.userDetails("test@example.com", "test"));
    	
    	// Get the load of "it3.nordvpn.com" server
    	System.out.println(api.serverLoad("it3.nordvpn.com"));
    	
    	// Get users current IP address
    	System.out.println(api.address());
    	
    	// Display NordVPN nameservers
    	System.out.println(api.nameServers());
    }
}