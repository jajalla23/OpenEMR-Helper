import java.io.*;

public class Language_v1 {
	private static int i = 1;
	public static void main(String[] args) {
		String file = "C:\\Documents and Settings\\Jan\\workspace\\openemr\\OpenEmr-Helper\\src\\utils\\lang-data.txt";
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
			while ((line = br.readLine()) != null) {
			   updateline(line);
			   line = "";
			}
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
	
	private static void updateline(String line) {
		String[] tokenized = line.split("\t");
		String newline = "";
		
		if (tokenized.length > 1) {
			if (tokenized[1].trim().length() > 0) {				
				String code = tokenized[0];
				String language = tokenized[2];
				
				int paren = code.indexOf('(');
				if (paren > 0) {
					code = code.substring(0, paren-1);
				}

				newline = "#IfNotRow2Dx2 list_options list_id language option_id " + language.toLowerCase() + " title " + language + " seq " + i + " is_default 0 option_value 0 notes " +  code;
				System.out.println(newline);
				
				newline = "INSERT INTO list_options ( list_id, option_id, title, seq, is_default, option_value, notes ) VALUES ('language', '" + language.toLowerCase() + "', '" + language + "', " + i + ", 0, 0, '" + code + "');";
				System.out.println(newline);
				System.out.println("#EndIf");
				System.out.println();
				
				i++;
			}
		}
	}

}
