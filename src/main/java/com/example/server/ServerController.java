package com.example.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;


@RestController
public class ServerController {

    @Value("${vpn.script.path}")
    private String vpnScriptPath;

    @GetMapping("/startVpn")
    public String startVpn() {
//    	return "Starting VPN connection in " + vpnScriptPath;
        if (startVPNConnection()) {
            return "VPN connection established.";
        } else {
            return "Failed to establish VPN connection.";
        }
    }
    
    @GetMapping("/pingVpn")
    public String pingVpn() {
    	String res = "";
//    	try {    		
//    		Process process = new ProcessBuilder("/bin/bash", "ping -c 4 10.8.0.1").start();
//    		BufferedReader reader = 
//                    new BufferedReader(new InputStreamReader(process.getInputStream()));
//		    StringBuilder builder = new StringBuilder();
//		    String line = null;
//		    while ( (line = reader.readLine()) != null) {
//		       builder.append(line);
//		       builder.append(System.getProperty("line.separator"));
//		    }
//		    res = builder.toString();
//          
//        } catch (Exception e) {
//          
//        }
    	
    	InetAddress address;
		try {
			address = InetAddress.getByName("10.8.0.1");
			boolean reachable = address.isReachable(10000);
			res = res + " reachable VPN host:" + reachable;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
               
    	return res;
    }

    private boolean startVPNConnection() {
        try {
            Process process = new ProcessBuilder("/bin/bash", "-c", vpnScriptPath).start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}

