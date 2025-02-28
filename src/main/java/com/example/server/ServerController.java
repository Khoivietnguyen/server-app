package com.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {

	@Value("${vpn.script.path}")
	private String vpnScriptPath;

	@GetMapping("/startVpn")
	public String startVpn() {
		return startVPNConnection();
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
			res = res + "Reachable VPN gateway at 10.8.0.1:" + reachable;
		} catch (Exception e) {			
			e.printStackTrace();
		}

		return res;
	}

	@GetMapping("/stopVpn")
	public String stopVpn() {
		String res = "Stopping VPN result: ";

		try {
			Process process = new ProcessBuilder("/bin/sh", "-c", "pkill openvpn").start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s;
			while ((s = reader.readLine()) != null) {
				res = res + s;
			}
			int exitCode = process.waitFor();
			res = res + exitCode;			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			res = res + e.getMessage();
		}
		
		try {
			Thread.sleep(500);
			Process process = new ProcessBuilder("/bin/sh", "-c", "chmod -R 777 /var/log/openvpn").start();
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return res;
	}

	private String startVPNConnection() {
		String res = "Starting VPN result: ";
		try {
			Process process = new ProcessBuilder("/bin/sh", "-c", "/script/startup.sh").start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s;
			while ((s = reader.readLine()) != null) {
				res = res + s;
			}
			int exitCode = process.waitFor();
			if (exitCode == 0) {
				res = res + "OK!";
			} else {
				res = res + "FAILED!";
			}
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			res = res + e.getMessage();
		}

		return res;
	}
}
