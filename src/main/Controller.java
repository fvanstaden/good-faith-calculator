package main;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.FutureTask;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Controller {

	Properties properties = new Properties();
	FileInputStream inputStream;

	private static String codeFileLocation = "";
	public static String templatesFolderLocation = "";
	private static String contactsFileLocation = "";

	public static Scanner templateScanner = null;
	public static JFileChooser fileChooser;
    public static ArrayList<String> insuranceNames = null;
    public static TreeMap<String, String> contactsList = null;
	public static HashMap<String, HashMap<String, ArrayList>> codes = null;
	public static TreeMap<String, String> codeDescriptions = null;
	public static TreeMap<String, String> favoriteCodeDescriptions = null;
	public static HashMap<String, ArrayList> insuranceCompaniesMap = null;
	public static int favoritesLimit = 40;

	public String userName = "";
	public String userTitle = "";
	public locationOfCare locationOfCare = new locationOfCare();
	
	public static boolean hideCodesSetting = false;

	public class locationOfCare {

			String name = "Southeast Neuroscience Center";
			String address = "128 Neuroscience Ct, Gray, LA 70359";
			String phone = "(985)917-3007";
			String fax = "(985)917-3010";

			public locationOfCare() {

			}

	}

	public Controller() {
		readConfigFile();
		readCodeFile();
		readContactsFile();
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e) {
			
		}
		
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("PDF (*.pdf)", "pdf", "PDF"));
		
		
	}

	public void readConfigFile() {

		try {
			inputStream = new FileInputStream("config.properties");
			properties.load(inputStream);
			codeFileLocation = properties.getProperty("codeFileLocation");
			templatesFolderLocation = properties.getProperty("templatesFolderLocation");
			contactsFileLocation = properties.getProperty("contactsFileLocation");
			if(properties.containsKey(System.getProperty("user.name"))) {
				userName = properties.getProperty(System.getProperty("user.name")).split("\\|")[0];
				userTitle = properties.getProperty(System.getProperty("user.name")).split("\\|")[1];
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There was an error trying to find the config file.");
			return;
		}

	}

	public void readCodeFile() {

		Scanner scanner;
	    File feeDBFile;

		insuranceNames = new ArrayList<>();

		/*

		CODES HASHMAP (Explanation)

		<K,V> (K (String) = code Number : V (HashMap<String, ArrayList>)= <K2, V2>) ->

			-> (K2 (String) = insurance Name : V2 (Arraylist<String>) = [0] = code Description : [1] = code Cost (for that ins))

		*/


		codes = new HashMap<>();
        codeDescriptions = new TreeMap<>();
        favoriteCodeDescriptions = new TreeMap<>();

        try {
        	feeDBFile = new File(codeFileLocation);
        	scanner = new Scanner(feeDBFile);
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There was an error trying to read from the code.csv file. The file is either not found, or it is having issues being read.");
			return;
		}

        String currentCode = "";
        String currentDescription = "";
        //String procedure = "";

        String[] input = scanner.nextLine().split(",");

        for (int i = 3; i < input.length; i++) {
            insuranceNames.add(input[i]);
            //System.out.print(input[i] + " | ");
        }

        while (scanner.hasNextLine()) {
        	//Redeclare this map so that a new (empty) map is made for each code
        	insuranceCompaniesMap = new HashMap<>();

            input = scanner.nextLine().split(",");

            //Input[] === CPT Code[0], CPT Description[1], Procedure/Department[2], Cost[3+]

            currentCode = input[0];
            currentDescription = input[1];
            //procedure = input[2];

            if(input.length < insuranceNames.size() + 3){
            	//This ensures that if there is a missing element on the end, it will still make an array with the correct size.
                String[] temp = Arrays.copyOf(input, insuranceNames.size() + 3);
                input = temp;
            }



            int insuranceNamesIndex = 0;

            for (int i = 3; i < insuranceNames.size() + 3; i++) {
            //for loop to add each code information to each individual ins. company.
                if(input[i] == null || input[i].trim() == ""){
                	//if the input at I is blank or null there is no data -> No amount
                    input[i] = "0";
                }

                ArrayList<String> currentCodeInfo = new ArrayList<>();
                currentCodeInfo.add(currentDescription);
                currentCodeInfo.add(input[i]);

                //System.out.println(cptCode+ " " + tempInput);

                insuranceCompaniesMap.put(insuranceNames.get(insuranceNamesIndex++), currentCodeInfo);
                codes.put(currentCode, insuranceCompaniesMap);
            }

            codeDescriptions.put(currentCode,currentDescription);
            
       

            if(favoriteCodeDescriptions.size() < favoritesLimit) {
            	favoriteCodeDescriptions.put(currentCode, currentDescription);
            }

        }

        Collections.sort(insuranceNames);

        scanner.close();
	}

	public void readContactsFile() {
		
		File contactsFile;
		Scanner contactScanner;
		List<String> input = new ArrayList();
		
		try {
			
			contactsFile = new File(contactsFileLocation);
			contactScanner = new Scanner(contactsFile);
			contactsList = new TreeMap();

			int nameIndex = -1;
			int addressIndex = -1;
			int csIndex = -1;
			int zipIndex = -1;
			int phoneIndex = -1;
			//int faxIndex;

			while(contactScanner.hasNext()) {
				input = Arrays.asList(contactScanner.nextLine().split("\\|"));
				if((input.contains("Name") && nameIndex == -1)) {
					//Find the index(s) of needed data
					nameIndex = input.indexOf("Name");
					addressIndex = input.indexOf("Address");
					csIndex = input.indexOf("City/State");
					zipIndex = input.indexOf("Zip");
					phoneIndex = input.indexOf("Phone");
					//faxIndex = input.indexOf("Fax");
					break;
				}
			}
			
			while(contactScanner.hasNext()) {
				
				input = Arrays.asList(Arrays.copyOf(contactScanner.nextLine().split("\\|"), phoneIndex + 1));

				Scanner tempScanner = contactScanner;
				List<String> tempInput = null;

				if(tempScanner.hasNext()) {
					tempInput = Arrays.asList(Arrays.copyOf(tempScanner.nextLine().split("\\|"), phoneIndex + 1));
					if(input.get(phoneIndex) == null) {
						input.set(phoneIndex, tempInput.get(phoneIndex));
					}
				}

				if(input.get(nameIndex) == "" || input.contains(null)) {
					continue;
				}

				String address;

				if(tempInput != null && tempInput.get(nameIndex) == "" && tempInput.get(addressIndex) != "") {
					//If a suite # or additional address information is on the next line
					address = input.get(addressIndex) + ", " + tempInput.get(addressIndex) + ", " + input.get(csIndex) + input.get(csIndex+1) + ", " + input.get(zipIndex);
				}else if(input.get(addressIndex) == "") {
					address = "";
				}else {
					address = input.get(addressIndex)  + ", " + input.get(csIndex) + input.get(csIndex+1) + ", " + input.get(zipIndex);
				}



				String tempAttorney = address + "|" + input.get(phoneIndex); //+ "|" + input.get(faxIndex);

				contactsList.put(input.get(nameIndex).trim(), tempAttorney.trim());

			}
			


		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There was an error trying to load the contacts file.");
			return;
		}
		
		contactScanner.close();
		
	}

	public void setUserName(String x) {

		userName = x;

	}

	public void setTitle(String x) {

		userTitle = x;

	}

	public void saveUserInfo(String x, String y) {

		FileOutputStream outputStream;

		try {
			outputStream = new FileOutputStream("config.properties");

			if(!properties.containsKey(System.getProperty("user.name"))) {

				properties.put(System.getProperty("user.name"), x + "|" + y);
				properties.store(outputStream, null);
				System.out.println("(saveUserInfo) saving new user");

			}else {

				properties.setProperty(System.getProperty("user.name"), x + "|" + y);
				properties.store(outputStream, null);
				System.out.println("(saveUserInfo) saving current user");

			}
		} catch (Exception e) {
			//JOptionPane.showMessageDialog(null, "There was an error trying to save to the config file.");
			
		}




	}


	}
