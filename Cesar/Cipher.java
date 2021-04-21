/**
 * Travail réalisé en binome avec Louka Altdorf-Reynes 
 * et l'aide de Izana Khabouri
 */

import java.util.StringTokenizer;

/**
 * Un chiffreur/déchiffreur de messages.
 *
 * Les algorithmes utilisés sont les suivants :
 * <ul>
 * <li> pour le chiffrage :
 * <ol>
 *     <li> le message est transformé une première fois globalement par
 *         un encodeur circulaire avec un décalage aléatoire à droite ;
 *     </li>
 *     <li> le message transformé est modifié une nouvelle fois mais mot
 *         à mot, à l'aide d'un encodeur circulaire à droite dont le décalage
 *         prend les longueurs des mots successivement rencontrés.
 *     </li>
 * </ol>
 * </li>
 *
 * <li> pour le déchiffrage :
 * <ol>
 *     <li> le message est travaillé une première fois à l'aide d'un encodeur
 *         circulaire à gauche dont le décalage prend en compte la longueur des
 *         mots successifs ;
 *     </li>
 *     <li> pour annuler l'utilisation lors du chiffrage du premier encodeur
 *         circulaire on retrouve le décalage aléatoire utilisé, en s'appuyant
 *         sur le fait que la lettre la plus fréquente dans un texte français
 *         (ou anglais) est le 'e'.
 *     </li>
 * </ol>
 * </li>
 * </ul>
 * @inv <pre>
 *     getClearText() != null
 *     getCipherText() != null
 *     getClearText() est le déchiffré de getCipherText() </pre>
 */
public class Cipher {

    // CONSTANTES

    /**
     * Drapeau pour transformWords indiquant que l'on souhaite encoder.
     */
    private static final int ENCODE = 1;

    /**
     * Drapeau pour transformWords indiquant que l'on souhaite décoder.
     */
    private static final int DECODE = 2;

    /**
     * Chaîne contenant la séquence de tous les caractères séparateurs.
     */
    private static final String SEPARATORS = " \t\n\r\f\'\"([-,?;.:!])";

    /**
     * Taille de l'alphabet.
     */
    private static final int ALPHABET_SIZE = SubstCipher.ALPHABET_SIZE;

    // ATTRIBUTS

    private String clearText;

    private String cipherText;
    
    private static int shiftAlea;

    // CONSTRUCTEURS

    /**
     * Un chiffreur dont les messages getClearText() et getCipherText()
     *  retournent la chaîne vide.
     * @post <pre>
     *     getClearText().equals("")
     *     getCipherText().equals("") </pre>
     */
    public Cipher() {
        /***************/
        this.clearText = "";
        this.cipherText = "";
        this.shiftAlea = alea(1, ALPHABET_SIZE - 1);
        /***************/
    }
    
    /**
     * Un chiffreur dont les messages getClearText() et getCipherText()
     *  retournent la chaîne vide.
     * 
     * @post <pre>
     *     getClearText().equals("")
     *     getCipherText().equals("") </pre>
     *     this.shiftAlea == alea(1, number)
     */
    public Cipher(int shift) {
        this.clearText = "";
        this.cipherText = "";
        this.shiftAlea = transformShiftAlea(shift);
    }

    // REQUETES

    /**
     * Le message courant en clair. Sa valeur correspond au message décodé de
     *  getCipherText().
     */
    public String getClearText() {
        /***************/
        return this.clearText;
        /***************/
    }

    /**
     * Le message courant chiffré. Sa valeur correspond au message encodé de
     *  getClearText().
     */
    public String getCipherText() {
        /***************/
        return this.cipherText;
        /***************/
    }

    /**
     * Une représentation sous forme de chaîne de cet encodeur.
     * @post
     *     result.equals("Cipher[clear:" + getClearText()
     *         + ";cipher:" + getCipherText() + "]")
     */
    public String toString() {
        return "Cipher[clear:" + clearText + ";cipher:" + cipherText + "]";
    }

    // COMMANDES

    /**
     * Modification du message courant en clair.
     * @pre <pre>
     *     text != null </pre>
     * @post <pre>
     *     getClearText().equals(text) </pre>
     */
    public void setClearText(String text) {
        /***************/
        assert !text.equals("") && text != null:
            "Chaine vide";  
        
        this.clearText = text; 
        this.cipherText = encodageCesar(text);
        /***************/
    }

    /**
     * Modification du message courant chiffré.
     * @pre <pre>
     *     text != null </pre>
     * @post <pre>
     *     getCipherText().equals(text) </pre>
     */
    public void setCipherText(String text) {
        /***************/
        assert !text.equals("") && text != null:
            "Chaine vide";  
        
        this.cipherText = text; 
        this.clearText = decodageCesar(text);
        /***************/
    }

    // OUTILS

    /**
     * Un nombre aléatoire compris entre a et b (au sens large).
     * @pre <pre>
     *     0 < a <= b </pre>
     * @post <pre>
     *     a <= result <= b </pre>
     */
    private static int alea(int a, int b) {
        assert (a > 0) && (b >= a);

        return a + (int) (Math.random() * (b - a + 1));
    }
    
    /**
     * Modification du d�calage al�atoire courant. 
     * @pre <pre>
     *   0 < number < SIZE_ALPHABET </pre>
     * @post <pre>
     *     getShiftAlea == alea(-number, number) </pre>
     */
    private int transformShiftAlea(int number) {
        /***************/
        assert (number > 0) && (number < ALPHABET_SIZE);
        
        return alea(1, number) ;
        /***************/
    }
    
    /**
     * Methode qui encode un texte de la facon de Cesar et qui modifie le message courant chiffree
     * Ajoutee par Izana
     */
    private String encodageCesar(String text) {
        if (text == null || text.equals("")) {
            throw new AssertionError("Aucun texte n'est saisie");
        }
        SubstCipher encodingText = new SubstCipher(this.shiftAlea);
        encodingText.buildShiftedTextFor(text);
        String encoding = encodingText.getLastShiftedText();
        return encodageMot(encoding);
    }
    
    /**
     * Methode qui decode un texte de la facon de Cesar et qui modifie le message courant en clair
     * Ajoutee par Izana
     */
    private String decodageCesar(String text) {
        if (text == null || text.equals("")) {
            throw new AssertionError("Aucun texte n'est saisie");
        }
        String decoding = decodageMot(text);
        SubstCipher decodingText = new SubstCipher(this.shiftAlea);
        decodingText.ensureNegativeShift();
        decodingText.buildShiftedTextFor(decoding);
        return decodingText.getLastShiftedText();
    }
    
    /**
     * Retourne l'encodage du mot a mot
     * Ajoutee par l'etudiante Izana
     */
    private String encodageMot(String message) {
        if (message == null || message.equals("")) {
            throw new AssertionError("Texte non saisie");
        } 
        return transformWords(message, 1); 
    }
    
    /**
     * Retourne le decodage du mot a mot
     * Ajoutee par l'etudiante Izana
     */
    private String decodageMot(String message) {
        if (message == null || message.equals("")) {
            throw new AssertionError("Texte non saisie");
        } 
        return transformWords(message, 2);
    }

    /**
     * indique si le mot word correspond à un séparateur.
     * @pre <pre>
     *     word != null </pre>
     * @post <pre>
     *     result <==>
     *         word.length() == 1
     *         word.charAt(0) est dans SEPARATORS </pre>
     */
    private static boolean isSeparator(String word) {
        assert (word != null);

        return word.length() == 1 && SEPARATORS.indexOf(word.charAt(0)) != -1;
    }

    /**
     * Encodage mot à mot de message avec un décalage donné par la longueur
     *  des mots (décalage à droite si type == ENCODE, décalage à gauche si
     *  type == DECODE).
     * @pre <pre>
     *     message != null </pre>
     * @post <pre>
     *     type == ENCODE
     *         ==> result est l'encodage de message mot à mot
     *     type == DECODE
     *         ==> result est le décodage de message mot à mot </pre>
     */
    private static String transformWords(String message, int type) {
        /***************/
        if (message == null){
            throw new AssertionError("Aucun texte n'est saisie\n");
        }
        if (type != ENCODE && type != DECODE) {
            throw new AssertionError("Mauvaise valeur du type");
        }
        String resultMessage = "";
        if (type == ENCODE) {
            StringTokenizer initMessage = new StringTokenizer(message, " ");
            while (initMessage.hasMoreTokens()) {
                String word = initMessage.nextToken();
                SubstCipher encodeBoutMessage = new SubstCipher(word.length());
                encodeBoutMessage.buildShiftedTextFor(word);
                resultMessage += encodeBoutMessage.getLastShiftedText() + " ";
            }
        } else if (type == DECODE) {
            StringTokenizer initMessage = new StringTokenizer(message, " ");
            while (initMessage.hasMoreTokens()) {
                String word = initMessage.nextToken();
                SubstCipher decodeMessage = new SubstCipher(word.length());
                decodeMessage.ensureNegativeShift();
                decodeMessage.buildShiftedTextFor(word);
                resultMessage += decodeMessage.getLastShiftedText() + " ";
            }
        }
        return resultMessage;
        /***************/
    }

    // POINT D'ENTREE

    public static void main(String[] args) {
        // dans un sens
        Cipher c = new Cipher();
        String clear = "Les Grecs attaquent par derrière !";
        c.setClearText(clear);
        System.out.println(c);
        // dans l'autre sens (peut ne pas fonctionner)
        String cyphered = c.getCipherText();
        c.setCipherText(cyphered);
        System.out.println(c);
        // un exemple qui ne donne pas le bon résultat !
        c.setCipherText("Cvj zkxvl xqqxnrbkq gri zanneèna !");
        System.out.println(c);
    }
}