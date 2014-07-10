import java.io.*;

public class Language_v3 {
	private static int i = 10;
	public static void main(String[] args) {
		String file = "C:\\Documents and Settings\\Jan\\workspace\\openemr\\OpenEmr-Helper\\src\\utils\\lang-data_v2.txt";
		String write_file = "C:\\Documents and Settings\\Jan\\workspace\\openemr\\OpenEmr-Helper\\src\\utils\\sql_lang_v2.txt";
		//String file = "C:\\Documents and Settings\\Jan\\workspace\\openemr\\OpenEmr-Helper\\src\\utils\\lang-data_test.txt";

		BufferedReader br = null;
		int i = 1;
		
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String line;
		
		try {
			PrintWriter writer = new PrintWriter(write_file, "UTF-8");

			while ((line = br.readLine()) != null) {
			   updateline(line, writer);
			   line = "";
			}
			
			writer.close();
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

	}
	
	private static void updateline(String line, PrintWriter writer) {
		String[] tokenized = line.split("\t");
		String newline = "";

		if (tokenized.length > 1) {
			if (tokenized[1].trim().length() > 0) {				
				String code = tokenized[0];
				
				code = code.replace(" ", "");
				code = code.replace(",", "|");
				
				String language = tokenized[2];
				
				String option_id = language.toLowerCase().replace(";", "");
				option_id = option_id.replace(' ', '_');
				option_id = option_id.replace(",", "");
				option_id = option_id.replace("'", "");
				option_id = option_id.replace("(", "");
				option_id = option_id.replace(")", "");
				
				if (option_id.length() > 30) {
					option_id = option_id.substring(0, 30);
				}
				
				/*int paren = code.indexOf('(');
				if (paren > 0) {
					code = code.substring(0, paren-1);
				}*/

				//newline = "#IfNotRow2Dx2 list_options list_id language option_id " + language.toLowerCase() + " title " + language + " seq " + i + " is_default 0 option_value 0 notes " +  code;
				//newline = "#IfNotRow2D list_options list_id language option_id " + language.toLowerCase();
				//System.out.println(newline);
				
				newline = "INSERT INTO list_options ( list_id, option_id, title, seq, is_default, option_value, notes ) VALUES ('language', '" + option_id + "', '" + language + "', " + i + ", 0, 0, '" + code + "');";
				//newline = "INSERT INTO 'list_options' ( list_id, option_id, title, seq, is_default, option_value ) VALUES ('language', '" + language.toLowerCase() + "', '" + language + "', " + i + ", 0, 0);";
				writer.println(newline);
				/*System.out.println("#EndIf");
				System.out.println();
				
				newline = "#IfNotRow3D list_options list_id language option_id " + language.toLowerCase() + " notes " + code;
				System.out.println(newline);
				
				newline = "UPDATE 'list_options' SET 'list_options'.'notes' = '" + code + "' WHERE 'list_options'.'option_id' = '" + language.toLowerCase() + "';";
				System.out.println(newline);
				System.out.println("#EndIf");
				System.out.println();*/
				
				i+=10;
			}
		} else {
			System.out.println("ERROR");
		}
	}

}
