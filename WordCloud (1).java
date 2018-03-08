/**
 * Name: WordCloud
 * 
 * @author Chris Eardensohn (ceardensohn@sandiego.edu)
 * @author Om Kanwar (okanwar@sandiego.edu)
 * 
 * Date: 12/4/17
 * 
 * Description: WordCloud contains code to generate a java
 * fx window that will increase size of the word based on
 * the number of times it appears in the passed in text.
 */
import java.io.*;
import java.util.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WordCloud extends Application {
	
	//function to store file into a string
	private static String loadIntoString (String filename)
			throws Exception {
		BufferedReader input = new BufferedReader(new FileReader(filename));
		String fileString = "", line;
		while ((line = input.readLine()) != null)
			fileString += line;
		input.close();
		return fileString;
	}
	
	//function to ask for a file from user and put the unique words
	//into a tree map
	private static TreeMap<String, Integer> getWordsFromFile()
				throws Exception {
		//prompt user for text file to convert to word cloud
		System.out.print("Enter name of text file: ");
		Scanner in = new Scanner(System.in);
		String file = new String (in.nextLine());
		in.close();
		
		//read in lines from text file and change to lower case
		String oneString = loadIntoString(file);
		String lowerString = oneString.toLowerCase();
		
		//stop words in a list
		File stopfile = new File("stop-words.txt");
		Scanner input = new Scanner(stopfile);
		ArrayList<String> stopList = new ArrayList<String>();
		String str = new String();
		while (input.hasNextLine() == true) {
			str = input.nextLine();
			str = str.toLowerCase();
			stopList.add(str);
		}
		input.close();
		
		//maybe use split("[ .,?!]+") or [^a-zA-Z']
		String[] wordsArray = lowerString.split("[ .,?!]+");
		
		//put arrays into a set
		Set<String> uniqueSet = new HashSet<String>(Arrays.asList(wordsArray));
		Set<String> stopSet = new HashSet<String>(stopList);
		//set containing words without stop words
		uniqueSet.removeAll(stopSet);
		System.out.println(uniqueSet);
		System.out.println(stopSet);
		TreeMap<String, Integer> uniqueMap = new TreeMap<String, Integer>();
		//store word count for each string in uniqueSet tree map
		for(String s: uniqueSet) {
			for (int i = 0; i < wordsArray.length; i++) {
				if (s.equals(wordsArray[i])) {
					if (uniqueMap.containsKey(s) != true) {
						uniqueMap.put(s, 1);
					}
					else {
						uniqueMap.put(s,uniqueMap.get(s) + 1);
					}
				}
			}
		}
		return uniqueMap;
	}
	
	//function to create flow plane to display words
	//in map. Size based on its word count
	public static void displayWords(TreeMap<String, Integer> uniqueMap, Stage cloudStage){
		FlowPane wordCloud = new FlowPane();
		cloudStage.setTitle("Word Cloud");
		cloudStage.setWidth(750);
		cloudStage.setHeight(750);
		Scene cloudScene = new Scene(wordCloud);
		wordCloud.setVgap(2);
		wordCloud.setHgap(2);
		wordCloud.setPrefWrapLength(500);
		Set<String> set = uniqueMap.keySet();
		//for each word in the set, make a text of that string
		//change size based on word count
		for(String s: set) {
			Text text = new Text(s);
			text.setFont(Font.font("Arial", (10 * uniqueMap.get(s))));
			wordCloud.getChildren().add(text);
		}
		cloudStage.setScene(cloudScene);
		cloudStage.show();
	}
	
	//run functions and show word cloud
	@Override
	public void start(Stage cloudStage) throws Exception {
		TreeMap<String, Integer> wordMap = getWordsFromFile();
		displayWords(wordMap, cloudStage);
	}
	public static void main(String[] args){
		launch(args);
	}
}
