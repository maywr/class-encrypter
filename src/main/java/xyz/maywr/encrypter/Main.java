package xyz.maywr.encrypter;

import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author maywr
 * 24.05.2022 1:19
 */
public class Main {

	static String inputFilePath;
	static String outputFilePath;
	static String configFilePath;

	public static void main(String[] args) {

		/*
		parsing arguments
		https://stackoverflow.com/questions/367706/how-do-i-parse-command-line-arguments-in-java
		 */

		Options options = new Options();

		Option inputOption = new Option("i", "input", true, "input file");
		inputOption.setRequired(true);
		options.addOption(inputOption);

		Option outputOption = new Option("o", "output", true, "output file");
		outputOption.setRequired(true);
		options.addOption(outputOption);

		Option configOption = new Option("cfg","config", true, "path to config file");
		configOption.setRequired(true);
		options.addOption(configOption);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);
			if (Config.INSTANCE.saveDefault(new File("config.json"))) //making an example config
			System.out.println("default config created");
			System.exit(1);
		}

		inputFilePath = cmd.getOptionValue("input");
		outputFilePath = cmd.getOptionValue("output");
		configFilePath = cmd.getOptionValue("config");

		try {
			File out = new File(outputFilePath);
			out.createNewFile();
			ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(out));
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(inputFilePath));

			ZipEntry zipEntry = null;
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
					zipOutputStream.putNextEntry(new ZipEntry(zipEntry.getName()));
					//checking if the entry is class and do we need to crypt it then crypt if yes
				if (zipEntry.getName().endsWith(".class") && Config.INSTANCE.shouldCrypt(zipEntry.getName().replace(".class", ""))) {
					zipOutputStream.write(AES.INSTANCE.encrypt(IOUtils.toByteArray(zipInputStream), Config.INSTANCE.getKey()));
					//it says its nullable but not really
				} else {
					//just writing regular bytes to an entry
					zipOutputStream.write(IOUtils.toByteArray(zipInputStream));
				}
					zipOutputStream.closeEntry();
				}

			zipOutputStream.close();
			zipInputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			//мы обосрались
			System.out.println("encryption failed");
		}
	}

}
