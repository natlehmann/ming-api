package ar.com.marcelomingrone.vericast.reports.util;

import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class PasswordEncoder {

	public static void main(String[] args) {
		
		if (args == null || args.length == 0 || args[0].trim().length() == 0) {
			System.out.println("Uso:");
			System.out.println("java -cp ../lib/spring-security-core-3.1.4.RELEASE.jar:. com.bmat.digitalcharts.admin.util.PasswordEncoder [PASSWORD]");
			
		} else {
			
			String password = args[0].trim();
			StandardPasswordEncoder encoder = new StandardPasswordEncoder();
			System.out.println("---------------------------------------------------------");
			System.out.println("PASSWORD: " + encoder.encode(password));
			System.out.println("---------------------------------------------------------");

		}

	}

}
