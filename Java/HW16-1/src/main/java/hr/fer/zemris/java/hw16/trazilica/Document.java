package hr.fer.zemris.java.hw16.trazilica;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Representation of a text document as a "bag of words". Only the words and
 * their frequency is stored disregarding everything else. <br>
 * For creating instances of this class, static method
 * {@link #createDocument(String, Collection)} is available. <br>
 * When all planned {@code Document} objects are created, TF-IDF vectors can be
 * created for each {@code Document} using the
 * {@link #buildVector(Map, Collection)} method.<br>
 * Finally, when vectors are created, {@code Document} objects can be compared
 * with {@link #calculateSimilarity(Document)} method.
 * 
 * @author Dan
 */
public class Document {

	/** {@code Path} to the file this {@code Document} was created from. */
	private Path path;

	/**
	 * {@code Map} of words in this document with their respective frequencies.
	 */
	private Map<String, Integer> wordFreq;

	/** TF-IDF vector of this {@code Document}. */
	private double[] tfidfVector;

	/** Norm of the {@link #tfidfVector}. */
	private Double vectorNorm;

	/**
	 * Private constructor.
	 * 
	 * @param wordFreq
	 *            {@code Map} of words with their respective frequencies
	 */
	private Document(Map<String, Integer> wordFreq) {
		this.wordFreq = wordFreq;
	}

	/**
	 * Creates a new {@code Document} object from given text.<br>
	 * Text is split into words and if a word is not in a {@code Collection} of
	 * stopwords its added to this {@code Document}.
	 * 
	 * @param text
	 *            text of the whole document
	 * @param stopwords
	 *            {@code Collection} of stopwords that should be ignored while
	 *            parsing the given text
	 * @return a new {@code Document} containing words from given text
	 */
	public static Document createDocument(String text, Collection<String> stopwords) {
		Pattern pattern = Pattern.compile("[\\W|\\d|_]+", Pattern.UNICODE_CHARACTER_CLASS);
		String[] words = pattern.split(text);

		Map<String, Integer> wordFreq = new LinkedHashMap<>();
		for (String word : words) {
			word = word.toLowerCase();
			if (stopwords.contains(word)) {
				continue;
			}
			wordFreq.merge(word, 1, Integer::sum);
		}

		return new Document(wordFreq);
	}

	/**
	 * Gets all words in this {@code Document}.
	 * 
	 * @return a {@code Collection} of words
	 */
	public Collection<String> getWords() {
		return Collections.unmodifiableSet(wordFreq.keySet());
	}

	/**
	 * Calculates the TF-IDF vector for this {@code Document} and stores it
	 * internally. Vector contains values for all words in given vocabulary.
	 * <br>
	 * This method should be called once all {@code Document} objects are
	 * created and they are all in given {@code Collection}, because otherwise
	 * vectors will not match.
	 * 
	 * @param vocabulary
	 *            {@code Collection} of all words in all documents
	 * @param documents
	 *            {@code Collection} of all documents
	 */
	public void buildVector(Map<String, Integer> vocabulary, Collection<Document> documents) {
		int n = vocabulary.size();
		tfidfVector = new double[n];

		vocabulary.forEach((word, index) -> {
			if (wordFreq.containsKey(word)) {
				double docsContaining = documents.stream().filter(d -> d.wordFreq.containsKey(word)).count();
				double idf = Math.log10(documents.size() / docsContaining);
				tfidfVector[index] = wordFreq.get(word) * idf;
			} else {
				tfidfVector[index] = 0;
			}
		});
	}

	/**
	 * Removes words from this {@code Document} if they are not contained in
	 * given vocabulary.
	 * 
	 * @param vocabulary
	 *            {@code Collection} of all words in all documents
	 */
	public void removeIfNotInVocabulary(Collection<String> vocabulary) {
		wordFreq.entrySet().removeIf(e -> !vocabulary.contains(e.getKey()));
	}

	/**
	 * Calculates the similarity value of this and given {@code Document} based
	 * on TF-IDF vectors.
	 * 
	 * @param doc
	 *            another {@code Document}
	 * @return similarity value in [0,1] interval; bigger value means more
	 *         similarity
	 */
	public double calculateSimilarity(Document doc) {
		double dotProduct = 0;
		for (int i = 0; i < tfidfVector.length; i++) {
			dotProduct += tfidfVector[i] * doc.tfidfVector[i];
		}

		return dotProduct / (getVectorNorm() * doc.getVectorNorm());
	}

	/**
	 * Getter for path.
	 * 
	 * @return path of this {@code Document}; can be {@code null}
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * Setter for path of this {@code Document}.
	 * 
	 * @param path
	 *            the path to set
	 */
	public void setPath(Path path) {
		this.path = path;
	}

	/**
	 * Returns the norm of the TF-IDF vector. It's only calculated the first
	 * time this method is called.
	 * 
	 * @return norm of the TF-IDF vector of this {@code Document}
	 */
	private double getVectorNorm() {
		if (vectorNorm == null) {
			double sum = 0;
			for (double d : tfidfVector) {
				sum += d * d;
			}
			vectorNorm = Math.sqrt(sum);
		}

		return vectorNorm;
	}
}
