import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CryptoAnalyzer {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Map<Character, Double> ENGLISH_LETTER_FREQUENCIES = new HashMap<>();

    static {
        // Заполняем карту частот английских букв (приблизительные значения)
        ENGLISH_LETTER_FREQUENCIES.put('E', 12.02);
        ENGLISH_LETTER_FREQUENCIES.put('T', 9.10);
        ENGLISH_LETTER_FREQUENCIES.put('A', 8.12);
        ENGLISH_LETTER_FREQUENCIES.put('O', 7.68);
        ENGLISH_LETTER_FREQUENCIES.put('I', 7.31);
        ENGLISH_LETTER_FREQUENCIES.put('N', 7.22);
        ENGLISH_LETTER_FREQUENCIES.put('S', 6.28);
        ENGLISH_LETTER_FREQUENCIES.put('R', 6.02);
        ENGLISH_LETTER_FREQUENCIES.put('H', 5.99);
        ENGLISH_LETTER_FREQUENCIES.put('L', 4.03);
        ENGLISH_LETTER_FREQUENCIES.put('D', 4.03);
        ENGLISH_LETTER_FREQUENCIES.put('C', 2.78);
        ENGLISH_LETTER_FREQUENCIES.put('U', 2.76);
        ENGLISH_LETTER_FREQUENCIES.put('M', 2.41);
        ENGLISH_LETTER_FREQUENCIES.put('W', 2.36);
        ENGLISH_LETTER_FREQUENCIES.put('F', 2.23);
        ENGLISH_LETTER_FREQUENCIES.put('G', 2.02);
        ENGLISH_LETTER_FREQUENCIES.put('Y', 1.97);
        ENGLISH_LETTER_FREQUENCIES.put('P', 1.93);
        ENGLISH_LETTER_FREQUENCIES.put('B', 1.49);
        ENGLISH_LETTER_FREQUENCIES.put('V', 1.11);
        ENGLISH_LETTER_FREQUENCIES.put('K', 0.69);
        ENGLISH_LETTER_FREQUENCIES.put('X', 0.17);
        ENGLISH_LETTER_FREQUENCIES.put('J', 0.10);
        ENGLISH_LETTER_FREQUENCIES.put('Q', 0.10);
        ENGLISH_LETTER_FREQUENCIES.put('Z', 0.07);
    }

    public static String decrypt(String text, int key) {
        StringBuilder decryptedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                int index = (ALPHABET.indexOf(Character.toUpperCase(c)) - key + ALPHABET.length()) % ALPHABET.length();
                decryptedText.append(ALPHABET.charAt(index));
            } else {
                decryptedText.append(c);
            }
        }
        return decryptedText.toString();
    }

    public static void bruteForceDecrypt(String text) {
        for (int key = 1; key <= 25; key++) {
            String decryptedText = decrypt(text, key);
            System.out.println("Ключ " + key + ": " + decryptedText);
        }
    }

    public static String statisticalDecrypt(String text) {
        Map<Character, Integer> letterFrequencies = new HashMap<>();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                c = Character.toUpperCase(c);
                letterFrequencies.put(c, letterFrequencies.getOrDefault(c, 0) + 1);
            }
        }

        // Находим наиболее часто встречающийся символ в тексте
        char mostFrequentChar = ' ';
        int maxFrequency = 0;
        for (Map.Entry<Character, Integer> entry : letterFrequencies.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostFrequentChar = entry.getKey();
            }
        }

        // Вычисляем наиболее вероятный ключ, используя частоту букв
        int key = getMostLikelyKey(mostFrequentChar);
        return decrypt(text, key);
    }

    // Возвращает наиболее вероятный ключ, основываясь на наиболее часто встречающейся букве
    private static int getMostLikelyKey(char mostFrequentChar) {
        double maxCorrelation = 0;
        int bestKey = 0;
        for (int key = 0; key <= 25; key++) {
            char shiftedChar = ALPHABET.charAt((ALPHABET.indexOf(mostFrequentChar) - key + ALPHABET.length()) % ALPHABET.length());
            double correlation = ENGLISH_LETTER_FREQUENCIES.getOrDefault(shiftedChar, 0.0);
            if (correlation > maxCorrelation) {
                maxCorrelation = correlation;
                bestKey = key;
            }
        }
        return bestKey;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите зашифрованный текст: ");
        String encryptedText = scanner.nextLine();

        System.out.println("\n--- Brute Force Дешифровка ---");
        bruteForceDecrypt(encryptedText);

        System.out.println("\n--- Статистическая Дешифровка ---");
        String decryptedText = statisticalDecrypt(encryptedText);
        System.out.println("Расшифрованный текст: " + decryptedText);
    }
}