package nextcloud_setting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.aarboard.nextcloud.api.NextcloudConnector;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

public class Main {
	
	@Parameter(names = "-u", required = true, description = "Nextcloud service url, e.g. https://nextcloud.instance.com:8443/cloud")
	String serviceUrl;
	
	@Parameter(names = "-l", required = true, description = "username")
	String user;
	
	@Parameter(names = "-p", required = true, description = "password")
	String password;
	
	@Parameter(names = "-t", description = "Trust all HTTPS certificates")
	boolean trustSelfSignedCertificate = false;
	
	@Parameter(names = "-v", description = "Verbose output")
	boolean verbose = false;
	
	@Parameter(names = "-s", description = "values to set as key=value; multiple occurences allowed")
	List<String> set = new ArrayList<>();
	
	@Parameter
	List<String> parameters = new ArrayList<>();
	
	private static NextcloudConnector nextcloud;
	
	public static void main(String... argv) throws IOException{
		Main main = new Main();
		JCommander commander = JCommander.newBuilder().addObject(main).build();
		try {
			commander.parse(argv);
			main.run();
		} catch (ParameterException e) {
			System.out.println(e.getMessage() + "\n");
			commander.usage();
		} finally {
			if (nextcloud != null) {
				nextcloud.shutdown();
			}
		}
	}
	
	private void run() throws IOException{
		nextcloud = new NextcloudConnector(serviceUrl, user, password);
		nextcloud.trustAllCertificates(trustSelfSignedCertificate);
		
		StringBuilder output = new StringBuilder();
		
		for (String setParamPair : set) {
			if (setParamPair.contains("=")) {
				String[] keyValue = setParamPair.split("=");
				String key = keyValue[0];
				String value = (keyValue.length > 1) ? keyValue[1] : "";
				try {
					setParameter(output, key, value);
				} catch (IOException ioe) {
					output.append("setting [" + key + "] failed: " + ioe.getMessage() + "\n");
				}
			} else {
				if (verbose) {
					output.append("Invalid set param pair " + setParamPair + "\n");
				}
			}
		}
		
		for (String getParamKey : parameters) {
			String value = nextcloud.getAppConfigAppKeyValue(getParamKey);
			if (value != null) {
				if (verbose) {
					output.append("[" + getParamKey + "] " + value + "\n");
				} else {
					output.append(value + "\n");
				}
				
			} else {
				output.append("setting [" + getParamKey + "] not found\n");
			}
		}
		
		if (output != null) {
			System.out.println(output.toString());
		}
	}
	
	private void setParameter(StringBuilder output, String key, String value) throws IOException{
		
		Object _value;
		
		if (Boolean.TRUE.toString().equalsIgnoreCase(value)
			|| Boolean.FALSE.toString().equalsIgnoreCase(value)) {
			_value = Boolean.parseBoolean(value);
		} else {
			_value = value;
		}
		
		nextcloud.editAppConfigAppKeyValue(key, _value);
		if (verbose) {
			output.append("[" + key + "] -> " + _value + "\n");
		}
	}
	
}
