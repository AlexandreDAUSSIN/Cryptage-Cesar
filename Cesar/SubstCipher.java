/**
 * Travail réalisé en binome avec Louka Altdorf-Reynes 
 * et l'aide de Izana Khabouri
 */

import java.util.StringTokenizer;

/**
 * Un encodeur selon le principe du décalage circulaire (aussi appelé technique
 *  de "César").
 * La méthode <code>getShiftedText</code> permet d'encoder un message
 *  (décalage à droite) ou de le décoder (décalage à gauche) selon la valeur
 *  actuelle du décalage.
 * @inv <pre>
 *     -ALPHABET_SIZE < getShift() < ALPHABET_SIZE
 *     getLastShiftedText() != null </pre>
 */
public class SubstCipher {

    // CONSTANTES

    /**
     * Taille de l'alphabet.
     */
    public static final int ALPHABET_SIZE = 26;

    /**
     * Caractère (majuscule) le plus fréquent en français (et en anglais).
     */
    public static final char MOST_FREQUENT_CHAR = 'E';

    // ATTRIBUTS

    private int currentShift;

    private String lastShiftedText;

    // CONSTRUCTEURS

    /**
     * Un encodeur de décalage 0 (i.e. l'identité).
     * @post <pre>
     *     getShift() == 0
     *     getLastShiftedText().equals("") </pre>
     */
    public SubstCipher() {
        /***************/
        this.currentShift = 0;
        this.lastShiftedText = "";
        /***************/
    }

    /**
     * Un encodeur de décalage <code>shift</code>.
     * @pre <pre>
     *     -ALPHABET_SIZE < shift < ALPHABET_SIZE </pre>
     * @post <pre>
     *     getShift() == shift
     *     getLastShiftedText().equals("") </pre>
     */
    public SubstCipher(int shift) {
        /***************/
        if (shift <= (-1) * (ALPHABET_SIZE) || shift >= ALPHABET_SIZE) {
            throw new AssertionError("Mauvais intervalle");
        }
        this.currentShift = shift;
        this.lastShiftedText = "";
        /***************/
    }

    // REQUETES

    /**
     * Le dernier texte encodé par cet encodeur avec un décalage de getShift().
     */
    public String getLastShiftedText() {
        /***************/
        return this.lastShiftedText;
        /***************/
    }

    /**
     * Le décalage courant de cet encodeur.
     */
    public int getShift() {
        /***************/
        return this.currentShift;
        /***************/
    }

    /**
     * Une représentation sous forme de chaîne de cet encodeur.
     * @post
     *     result.equals("SubstCipher[shift:" + getShift() + "]")
     */
    public String toString() {
        /***************/
        return new StringBuffer().append("SubstCipher[shift:")
            .append(getShift()).append("]").toString();
        /***************/
    }

    // COMMANDES

    /**
     * Affecte un nouveau décalage à l'encodeur.
     * @pre <pre>
     *     -ALPHABET_SIZE < shift < ALPHABET_SIZE </pre>
     * @post <pre>
     *     getShift() == shift
     *     getLastShiftedText().equals("") </pre>
     */
    public void setShift(int shift) {
        /***************/
        if ((ALPHABET_SIZE < shift) || (shift < (-(ALPHABET_SIZE)))) {
            throw new AssertionError("Valeur de décalage incorrecte");
        }
        this.currentShift = shift;
        this.lastShiftedText = "";
        /***************/
    }

    /**
     * Construit une chaîne à partir de celle fournie en paramètre en décalant
     *  circulairement les caractères alphabétiques selon le décalage donné par
     *  <code>getShift()</code>.
     * Le décalage se fait à droite si <code>getShift() &geq; 0</code>, ou
     *  à gauche si <code>getShift() &lt; 0</code>.
     * @pre <pre>
     *     text != null </pre>
     * @post <pre>
     *     getLastShiftedText() != null
     *     getLastShiftedText().length() == text.length()
     *     forall i in [0 .. text.length()[ :
     *         Let ci :: text.charAt(i)
     *             xi :: getLastShiftedText().charAt(i)
     *         isNonAccentedLetter(ci) ==> xi == ci décalé de getShift()
     *         !isNonAccentedLetter(ci) ==> xi == ci </pre>
     */
    public void buildShiftedTextFor(String text) {
        /***************/
        if (text == null){
            throw new AssertionError("Pas de texte");
        }
        StringBuffer shiftedText = new StringBuffer();
        final int l = text.length();
        if (getShift() > 0) {
            for (int i = 0; i < l; i++) {
                char c = text.charAt(i);
                if (isNonAccentedLetter(c)) {
                    c = shiftChar(c);
                    shiftedText.append(c);
                } else {
                    shiftedText.append(c);
                }
            }
        } else if (getShift() < 0){
            for (int i = 0; i < l; i++) {
                char c = text.charAt(i);
                if (isNonAccentedLetter(c)) {
                    c = shiftChar(c);
                    shiftedText.append(c);
                } else {
                    shiftedText.append(c);
                }
            }
        } 
        this.lastShiftedText = shiftedText.toString();
        /***************/
    }

    /**
     * Configure cet encodeur pour un encodage.
     * @post <pre>
     *     getShift() == +abs(old getShift())
     *     getLastShiftedText().equals("") </pre>
     */
    public void ensurePositiveShift() {
        /***************/
        this.currentShift = Math.abs(this.currentShift);
        this.lastShiftedText = "";
        /***************/
    }

    /**
     * Configure cet encodeur pour un décodage.
     * @post <pre>
     *     getShift() == -abs(old getShift())
     *     getLastShiftedText().equals("") </pre>
     */
    public void ensureNegativeShift() {
        /***************/
        this.currentShift = -(Math.abs(this.currentShift));
        this.lastShiftedText = "";
        /***************/
    }

    // OUTILS

    /**
     * Le caractère c décalé circulairement de shift positions dans l'alphabet.
     * @pre <pre>
     *     isNonAccentedLetter(c) </pre>
     * @post <pre>
     *     currentShift >= 0
     *         ==> result == décalé vers la droite de c
     *     currentShift < 0
     *         ==> result == décalé vers la gauche de c </pre>
     */
    private char shiftChar(char c) {
        /***************/
        if (!isNonAccentedLetter(c)) {
            throw new AssertionError("La lettre est accentuée");
        } 
        char letterShifted = c;
        if (this.currentShift >= 0) {
            for (int k = 1; k <= this.currentShift; k++) {
                if (letterShifted == 'z') {
                    letterShifted = 'a';
                } else if (letterShifted == 'Z') {
                    letterShifted = 'A';
                } else {
                    letterShifted = (char) (letterShifted + 1);
                }
            }
        } else if (this.currentShift < 0) {
            for (int k = this.currentShift; k < 0; k++) {
                if (letterShifted == 'a') {
                    letterShifted = 'z';
                } else if (letterShifted == 'A') {
                    letterShifted = 'Z';
                } else {
                    letterShifted = (char) (letterShifted - 1);
                }
            }
        }
        return letterShifted;
        /***************/
    }

    /**
     * Indique si c est une lettre minuscule ou majuscule non accentuée.
     * @post <pre>
     *     result <==> c est une lettre non accentuée </pre>
     */
    public static boolean isNonAccentedLetter(char c) {
        return (('a' <= c) && (c <= 'z')) || (('A' <= c) && (c <= 'Z'));
    }

    /**
     * Calcule un décalage à partir du message donné en paramètre selon
     *  l'algorithme qui suit.
     * <ul>
     *   <li>compter l'occurrence de chaque lettre non accentuée (minuscule ou
     *       majuscule) du message ;</li>
     *   <li>déterminer un décalage pour encoder MOST_FREQUENT_CHAR par la
     *       lettre apparaissant le plus souvent dans le message.</li>
     * </ul>
     * En cas d'égalité de fréquence de plusieurs lettres du message, la plus
     *  petite parmi ces lettres (pour l'ordre alphabétique) est retournée.
     * Pour un message chiffré égal à la chaîne vide, la valeur retournée est 0.
     * @pre <pre>
     *     text != null </pre>
     * @post <pre>
     *     text.equals("") ==> result == 0
     *     !text.equals("")
     *         ==> Let f :: la lettre la plus fréquente du message
     *             result ==
     *                     ((f - MOST_FREQUENT_CHAR) + ALPHABET_SIZE)
     *                     % ALPHABET_SIZE </pre>
     */
    public static int guessShiftFrom(String text) {
        if (text == null) {
            throw new AssertionError("No text");
        }

        if (text.equals("")) {
            return 0;
        }
        return guessShiftFromNonEmptyMessage(text);
    }

    /**
     * Calcule un décalage à partir du message (non vide) donné en paramètre
     *  selon l'algorithme qui suit.
     * <ul>
     *   <li>compter l'occurrence de chaque lettre non accentuée (minuscule ou
     *       majuscule) du message ;</li>
     *   <li>déterminer un décalage pour encoder MOST_FREQUENT_CHAR par la
     *       lettre apparaissant le plus souvent dans le message.</li>
     * </ul>
     * En cas d'égalité de fréquence de plusieurs lettres du message, la plus
     *  petite parmi ces lettres (pour l'ordre alphabétique) est retournée.
     * @pre <pre>
     *     text != null && !text.equals("")</pre>
     * @post <pre>
     *     Let f :: la lettre la plus fréquente du message
     *     result == ((f - MOST_FREQUENT_CHAR) + ALPHABET_SIZE) % ALPHABET_SIZE
     * </pre>
     */
    private static int guessShiftFromNonEmptyMessage(String text) {
        /***************/
        int[] counter = countOccurences(text);
        int f = getIndexOfMax(counter);
        return ((counter[f] - MOST_FREQUENT_CHAR) + ALPHABET_SIZE) % ALPHABET_SIZE;
        /***************/
    }
    
    private static int[] countOccurences(String s) {
        int[] counter = new int[ALPHABET_SIZE];
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (isNonAccentedLetter(c)) {
                if (isMin(c)) {
                    counter[c - 'a'] += 1;
                } else {
                    counter[c - 'A'] += 1;
                }
            }
        }
        return counter;
    }
    
    private static boolean isMin(char c) {
        return (c>='a' && c<='z');
    }
    
    private static int getIndexOfMax(int[] t) {
        int max = 0 ;
        int p_max = 0;
        for ( int i = 0; i < t.length; ++i ) {
            if (t[i] > max) {
                p_max = i;
            }
        }
        return p_max;
    }
}