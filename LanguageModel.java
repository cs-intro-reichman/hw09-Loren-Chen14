import java.util.HashMap;
import java.util.Random;
import java.util.random.RandomGenerator;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
		In in = new In(fileName);
        String windowString = "";           

        for (int i = 0; i < windowLength; i++){
            windowString += in.readChar(); 
        }
        while (!in.isEmpty()){
            char chr = in.readChar();                       // holds the charachter after the winddow 
            List keyList = CharDataMap.get(windowString);
            if (!CharDataMap.containsKey(windowString)){
                keyList = new List();
                CharDataMap.put(windowString, keyList);
            } 
            keyList.update(chr);
            windowString = windowString.substring(1) + chr; 
        }

        for (List Key : CharDataMap.values()){
            calculateProbabilities(Key);
        }   
	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	void calculateProbabilities(List probs) {				
		ListIterator itr = probs.listIterator(0);
        int charCount = 0;
        int index = 0;
        while (itr.hasNext()){
            charCount += itr.next().count;
        }
        itr = probs.listIterator(0);
        CharData prev = null;
        while (itr.hasNext()){
            CharData current = itr.next(); 
            current.p = ((double) current.count )/ charCount;
            if (index == 0){
                current.cp = current.p;
            } else {
                current.cp = prev.cp + current.p;
            } 
            prev = current;
            index++;
        }
	}

    // Returns a random character from the given probabilities list.
	char getRandomChar(List probs) {
		double r = randomGenerator.nextDouble();
        ListIterator itr = probs.listIterator(0);
        while (itr.hasNext()){
            CharData current = itr.next();
            if (current.cp > r){
                return current.chr;
            }
        }
		return ' ';
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) return initialText;
		String text = initialText;
        while (text.length() < textLength + initialText.length()){
            String window = text.substring(text.length() - windowLength);
            if (CharDataMap.get(window) == null){
                break;
            }
            text += getRandomChar(CharDataMap.get(window));
        }
        return text;
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        Boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];
        // Create the LanguageModel object
        LanguageModel lm;
        if (randomGeneration)
            lm = new LanguageModel(windowLength);
        else
            lm = new LanguageModel(windowLength, 20);
        // Trains the model, creating the map.
        lm.train(fileName);
        // Generates text, and prints it.
        System.out.println(lm.generate(initialText, generatedTextLength));
    }
 }