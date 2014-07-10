import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import java.util.Comparator;

public class Language_Script {
	private static int i = 10;
	public static void main(String[] args) {
		String file = "C:\\Documents and Settings\\Jan\\workspace\\openemr\\OpenEmr-Helper\\src\\utils\\lang-data_v2.txt";
		//String file = "C:\\Documents and Settings\\Jan\\workspace\\openemr\\OpenEmr-Helper\\src\\utils\\lang-data_test.txt";
		String write_file = "C:\\Documents and Settings\\Jan\\workspace\\openemr\\OpenEmr-Helper\\src\\utils\\sql_lang_script_v1.txt";
		String write_upgrade_file = "C:\\Documents and Settings\\Jan\\workspace\\openemr\\OpenEmr-Helper\\src\\utils\\sql_lang_script_v2.txt";

		Vector<Language> langList = new Vector<Language>();
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String line;
		
		try {
			

			while ((line = br.readLine()) != null) {
				Language lang = createLanguage(line);
				
				if (lang != null) {
					langList.add(createLanguage(line));
				}
				
				line = "";
			}
			;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		langList = sortLanguage(langList);
		
		try {
			//printListForDBScript(langList, write_file);
			printListForDBUpgradeScript(langList, write_upgrade_file);
		} catch (Exception ex) {
			System.err.print(ex.getMessage());
		}

	}
	
	private static Language createLanguage(String line) {		
		String code = "";
		String name = "";
		
		String[] tokenized = line.split("\t");

		if (tokenized.length > 1) {
			if (tokenized[1].trim().length() > 0) {				
				code = tokenized[0];
				
				code = code.replace(" ", "");
				code = code.replace(",", "|");
				
				name = tokenized[2];
				
				return new Language(name, code);
			}
		} 
		
		return null;
	}

	private static Vector<Language> sortLanguage(Vector<Language> languages) {
		Vector<Language> sortedList = languages;
		
		Collections.sort(sortedList, new LanguageComparator());
		
		return sortedList;
		
	}

	private static void printListForDBScript(Vector<Language> languages, String filename) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		
		Iterator<Language> i = languages.iterator();
		int counter = 10;
		while (i.hasNext()) {
			Language current = (Language) i.next();
			
			String option_id = current.OptionId;
			String name = current.Name;
			String code = current.ISO6392_Code;
			
			if (name.length() > 0) {
				String line = "INSERT INTO list_options ( list_id, option_id, title, seq, is_default, option_value, notes ) VALUES ('language', '" + 
					option_id + "', '" + name + "', " + counter + ", 0, 0, '" + code + "');";
				writer.println(line);
				counter += 10;
			}
		}
		
		writer.close();
	}
	
	private static void printListForDBUpgradeScript(Vector<Language> languages, String filename) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		
		Iterator<Language> i = languages.iterator();
		int counter = 10;	
		while (i.hasNext()) {
			Language current = (Language) i.next();
			
			String option_id = current.OptionId;
			String name = current.Name;
			String code = current.ISO6392_Code;
			
			if (name.length() > 0) {
				String line = "#IfNotRow2Dx2 list_options list_id language option_id " + option_id + " title " + name;
				writer.println(line);
				line = "INSERT INTO 'list_options' ( list_id, option_id, title, seq, is_default, option_value ) VALUES ('language', '" + 
					option_id + "', '" + name + "', " + counter + ", 0, 0);";
				writer.println(line);
				writer.println("#EndIf");
				writer.println();
				
				line = "#IfRow2D list_options list_id language option_id " + option_id;
				writer.println(line);
				line = "UPDATE 'list_options' SET 'list_options'.'notes' = '" + code + "' WHERE 'list_options'.'option_id' = '" + option_id + "';";
				writer.println(line);
				writer.println("#EndIf");
				writer.println();
				
				line = "#IfRow2D list_options list_id language title " + name;
				writer.println(line);
				line = "UPDATE 'list_options' SET 'list_options'.'notes' = '" + code + "' WHERE 'list_options'.'title' = '" + name + "';";
				writer.println(line);
				writer.println("#EndIf");
				writer.println();
				
				counter += 10;
			}
		}
		
		writer.close();
	}
}

class Language implements Comparable<Language> {
	public String Name;
	public String OptionId;
	public String ISO6392_Code;
	
	public Language(String Name, String ISO6392) {
		this.Name = Name;
		this.OptionId = convertToOptionId(Name);
		this.ISO6392_Code = convertCode(ISO6392);
	}
	
	private String convertToOptionId(String name) {
		String option_id = name.toLowerCase().replace(";", "");
		option_id = option_id.replace(' ', '_');
		option_id = option_id.replace(",", "");
		option_id = option_id.replace("'", "");
		option_id = option_id.replace("(", "");
		option_id = option_id.replace(")", "");
		
		if (option_id.length() > 30) {
			option_id = option_id.substring(0, 30);
		}
		
		return option_id;
	}
	
	private String convertCode(String code) {
		code = code.replace(" ", "");
		code = code.replace(",", "|");
		
		return code;
	}

	@Override
	public int compareTo(Language language) {

		return this.OptionId.compareToIgnoreCase(language.OptionId);
	}
	
	public static Comparator<Language> OPTIONID_SORT 
		= new Comparator<Language>() {
		
		public int compare(Language lang1, Language lang2) {
			/*String lang_name1 = lang1.OptionId.toUpperCase();
			String lang_name2 = lang2.OptionId.toUpperCase();
			
			return lang_name1.compareTo(lang_name2);*/
			
			return lang1.compareTo(lang2);
			
		}
	};
}

class LanguageComparator implements Comparator<Language> {

	@Override
	public int compare(Language lang1, Language lang2) {
		String lang_name1 = lang1.OptionId.toUpperCase();
		String lang_name2 = lang2.OptionId.toUpperCase();
		
		return lang_name1.compareTo(lang_name2);
	}
	
}
