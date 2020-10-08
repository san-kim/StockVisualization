package csci310.service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class PasswordAuthentication{
	
	public String MD5(String md5) throws NoSuchAlgorithmException {
		java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		byte[] array = md.digest(md5.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
	   
	}
	
	public String hash(String rawPassword, Long salt, Integer rounds) throws NoSuchAlgorithmException {
		salt = salt != null ? salt : new Date().getTime();
		rounds = rounds != null ? rounds : 10;
		String hashedString = MD5(rawPassword + salt);
		for (int i = 0; i < rounds; ++i) {
			hashedString = MD5(hashedString);
		}
		return Long.toString(salt) + "$" + Integer.toString(rounds) + "$" + hashedString;
	}
	
	public boolean verify(String rawPassword, String hashedPassword) throws NoSuchAlgorithmException {
		String[] splittedList= hashedPassword.split(Pattern.quote("$"));
		long saltLong = Long.valueOf(splittedList[0]);
		int rounds = Integer.valueOf(splittedList[1]);
		String hashedRawPassword = hash(rawPassword, saltLong, rounds);
		
		return hashedPassword.equals(hashedRawPassword);
	}
	
}