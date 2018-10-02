package genericGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;

public class GameSecurity {

	//G-mail account info
	private static final String USERNAME = "boardgameware.securesystems@gmail.com";
	private static final String PASSWORD = "gm47A24%pph@21aX";

	//Filenames
	private static final String READMELOC = "ReadMe.txt";
	private static final String SECURELOC = "ReqData.txt";

	private static final int SECURELINE = 23;	//Line in security file with stored path.
	private static final int SECURELENGTH = 50;	//Length of generated security file.

	private String filePath;			//what the file path should be according to the file

	/**
	 * Checks whether the game is running on Nathaniel's computer.
	 * @return boolean indicating the above.
	 */
	private static boolean isBuildMachine() {
		return new File(SECURELOC).getAbsolutePath().contains("Nathaniel");
	}

	/**
	 * Checks that the required ReadMe file is present.
	 * @return whether the ReadMe was found
	 */
	private static boolean checkReadMe() {
		try {
			Scanner in = new Scanner(new File(READMELOC));
			String line = "";
			while(in.hasNext()) {
				line = in.nextLine();
			}
			in.close();
			return line.startsWith("I agree to the above conditions:");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Checks whether the ReadMe file has received agreement confirmation from the user.
	 * @return whether the ReadMe agreement confirmation was found.
	 */
	private static boolean checkAgreement() {
		try {
			if(isBuildMachine()) {
				return true;
			}
			Scanner in = new Scanner(new File(READMELOC));
			String line = "";
			while(in.hasNext()) {
				line = in.nextLine();
			}
			in.close();
			return line.contains("Yes");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Generates a security file and places the computer name inside.
	 */
	private static void generateSecurityFile() {
		FileWriter fw;
		try {
			fw = new FileWriter(SECURELOC);
			PrintWriter pw = new PrintWriter(fw);
			pw.print(generateRandomLines(SECURELINE - 1));
			pw.println(encrypt(new File(SECURELOC).getAbsolutePath()));
			pw.print(generateRandomLines(SECURELENGTH - SECURELINE));
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates random lines of text of varying lengths.
	 * @param numLines the number of lines of text to generate.
	 * @return String containing the text.
	 */
	private static String generateRandomLines(int numLines) {
		String output = "";
		int charsThisLine = 0;
		for(int lines = 1; lines <= numLines;) {
			int rand = 30 + (int)(Math.random()*97);
			if(rand < 32) {
				output += "\n";
				lines++;
				charsThisLine = 0;
			}
			else {
				charsThisLine++;
				output += ((char)rand);
			}
			if(charsThisLine > 150) {
				output += "\n";
				charsThisLine = 0;
				lines++;
			}
		}
		return output;
	}

	/**
	 * Encrypts the input string
	 * @param un unencrypted string
	 * @return encrypted version of the input
	 */
	private static String encrypt(String un) {
		String output = "";
		for(int i = 0; i < un.length(); i++) {
			int c = (int) un.charAt(i);
			c -= 45;
			if(c < 32) {
				c += 95;
			}
			output += (char)c;
		}
		return output;
	}

	/**
	 * Decrypts the input string
	 * @param en encrypted string
	 * @return decrypted version of the input
	 */
	private static String decrypt(String en) {
		String output = "";
		for(int i = 0; i < en.length(); i++) {
			int c = (int) en.charAt(i);
			c += 45;
			if(c > 126) {
				c -= 95;
			}
			output += (char)c;
		}
		return output;

	}
	
	/**
	 * Checks to determine whether the proper computer is running the game.
	 * @return if the computer name correctly matches the stored one.
	 */
	private boolean checkFilepath() {
		try {
			Scanner in = new Scanner(new File(SECURELOC));	
			for(int line = 1; line < SECURELINE && in.hasNext(); line++) {
				in.nextLine();
			}			
			String key = new File(SECURELOC).getAbsolutePath();
			filePath = decrypt(in.nextLine());
			in.close();
			if(filePath.contains("Nathaniel") || isBuildMachine()) {
				generateSecurityFile();
				return true;
			}
			return filePath.startsWith(key);			
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Sends an error message via email with the problem encountered.
	 */
	private boolean sendError(String error) {

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(USERNAME));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("nathaniel.e.brooke@gmail.com"));
			message.setSubject("Game Error Detected");

			String text = "Hi! \n\nAn error has been detected: \n\nError Message:\n"+ 
					error + " on computer named ";
			try {
				text += encrypt(InetAddress.getLocalHost().getHostName());
			} catch (UnknownHostException e) {
				text += encrypt(ManagementFactory.getRuntimeMXBean().getName().substring(
						ManagementFactory.getRuntimeMXBean().getName().indexOf("@") + 1));
			}
			text += "\n" + "Program designed to be run from " + filePath + "\n";
			text += "Was run from " + new File(SECURELOC).getAbsolutePath() + "\n\n";
			try {
				Scanner in = new Scanner(new File(READMELOC));
				in.close();
			} catch (FileNotFoundException e) {
				text += "ReadMe Missing or Empty\n";
				e.printStackTrace();
			}

			try {
				Scanner in = new Scanner(new File(SECURELOC));
				in.close();
			} catch (FileNotFoundException e) {
				text += "Security File Missing or Empty\n";
				e.printStackTrace();
			}

			message.setText(text);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return true;
	}
	
	/**
	 * runs GUI to have user agree to readme.
	 */

	private static void agreeToReadMe() {
		String[] options = {"Disagree", "Agree"};
		TextArea readMe = new TextArea();
		try {
			Scanner in = new Scanner(new File(READMELOC));
			while(in.hasNext()) {
				readMe.appendText(in.nextLine() + "\n");
			}
			in.close();
			HashMap<ButtonType, Integer> choices = new HashMap<>();
			Alert alert = new Alert(AlertType.NONE);
			alert.setGraphic(readMe);
			alert.setTitle("Security");
			alert.getButtonTypes().clear();
			for(int i = 0; i < options.length; i++) {
				ButtonType btn = new ButtonType(options[i]);
				choices.put(btn, i);
				alert.getButtonTypes().add(btn);
			}
			if(1 == choices.get(alert.showAndWait().get())) {
				FileWriter fw = new FileWriter(READMELOC);
				PrintWriter pw = new PrintWriter(fw);
				pw.print(readMe.getText(0, readMe.getText().length() - 1));
				pw.print("Yes");
				pw.close();
				return;
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.exit(0);
	}

	/**
	 * Used externally to confirm that the game is running from the same file location.
	 */
	public static void confirmLocation() {
		GameSecurity gc = new GameSecurity();
		if(!gc.checkFilepath()) {
			gc.sendError("game file location did not match");
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Security");
			alert.setHeaderText("Invalid Software Copy");
			alert.setContentText("This is not your valid copy of the software");
			alert.showAndWait();
			System.exit(0);
		}	
	}

	/**
	 * Used externally to confirm that the readme file is present and has been agreed to.
	 */
	public static void confirmReadMeAgree() {
		if(!checkReadMe()) {
			GameSecurity gc = new GameSecurity();
			gc.checkFilepath();
			gc.sendError("no ReadMe file found");
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Security");
			alert.setHeaderText("No ReadMe File");
			alert.setContentText("The ReadMe file is missing");
			alert.showAndWait();
			System.exit(0);
		}
		else if(!checkAgreement()) {
			agreeToReadMe();
		}
	}

	/**
	 * Runs the GameSecurity for decryption.
	 * Also regenerates the security file.
	 * @param args command arguments.  Unused. 
	 */
	public static void main(String[] args) {
		generateSecurityFile();
		while(true) {
			System.out.println("Enter text to decrypt");
			Scanner in = new Scanner(System.in);
			System.out.println(decrypt(in.nextLine()) + "\n");
			in.close();
		}
	}

}