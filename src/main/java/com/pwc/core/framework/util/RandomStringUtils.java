package com.pwc.core.framework.util;

import com.hp.gagawa.java.Node;
import com.hp.gagawa.java.elements.*;
import com.pwc.core.framework.data.WebElementAttribute;

import java.util.*;

/**
 * Created by Julian Jacobs on 8/19/2015.
 */
public class RandomStringUtils {

    private static final int UPPER_BOUND = 4;
    private static final int SCHEME_UPPER_BOUND = UPPER_BOUND;
    private static final boolean CONSONANT = true;
    private static final boolean VOWEL = false;
    private static final int BODY_ELEMENT_UPPER_BOUND = 5;
    private static final int DEFAULT_PARAGRAPH_LENGTH = 20;
    private static final int DEFAULT_STRING_LENGTH = 5;
    private static final int DEFAULT_NUMBER_OF_ELEMENTS = 4;
    private static final List<String> webAttributesList = new ArrayList<>();

    /**
     * Get a random string of alphabetic letters of variable length
     *
     * @param length The length of the string of random letters
     * @return A string of random letters
     */
    public static String randomAlphabetic(final int length) {
        return org.apache.commons.lang.RandomStringUtils.randomAlphabetic(length);
    }

    /**
     * Get a random string of alphanumeric characters of variable length
     *
     * @param length The length of the string of random alphanumeric characters
     * @return A String of random alphanumeric letters
     */
    public static String randomAlphanumeric(final int length) {
        return org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * Get a random string of alphanumeric characters of variable length
     *
     * @param length The length of the string of random numbers
     * @return A String of random numbers
     */
    public static String randomNumeric(final int length) {
        return org.apache.commons.lang.RandomStringUtils.randomNumeric(length);
    }

    private static Node getRandomBodyElement(final int numberOfElements) {
        Random rand = new Random();
        int randSelector = rand.nextInt(BODY_ELEMENT_UPPER_BOUND);

        switch (randSelector) {
            case 1:
                P paragraph = new P();
                paragraph.appendChild(new Text(randomAlphanumeric(DEFAULT_PARAGRAPH_LENGTH)));
                return paragraph;
            case 2:
                Ul unorderedList = new Ul();
                for (int index = 0; index < numberOfElements; index++) {
                    Li listItem = new Li();
                    unorderedList.appendChild(listItem);
                    listItem.appendChild(new Text(randomName()));
                    if (index == numberOfElements - 1) {
                        return unorderedList;
                    }
                }
            case 3:
                Ol orderedList = new Ol();
                for (int index = 0; index < numberOfElements; index++) {
                    Li listItem = new Li();
                    orderedList.appendChild(listItem);
                    listItem.appendChild(new Text(randomName()));
                    if (index == numberOfElements - 1) {
                        return orderedList;
                    }
                }
            case 4:
                return new Img(randomAlphanumeric(DEFAULT_STRING_LENGTH),
                        randomAlphanumeric(DEFAULT_STRING_LENGTH));
            default:
                Button button = new Button();
                button.appendChild(new Text(RandomStringUtils.randomAlphabetic(DEFAULT_STRING_LENGTH)));
                return button;
        }
    }

    /**
     * Get a segment of of HTML
     *
     * @param numberOfElements The number of HTML elements
     * @return A List of randomly generated HTML elements and content
     */
    private static List<Node> getNodeSegment(final int numberOfElements) {
        List<Node> nodeSegment = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < numberOfElements; i++) {
            nodeSegment.add(getRandomBodyElement(rand.nextInt(DEFAULT_NUMBER_OF_ELEMENTS)));
        }

        return nodeSegment;
    }

    /**
     * Get a random string of HTML with real tags and randomly generated content
     *
     * @param numberOfElementsInBody The number of elements in the segment for segment only html,
     *                               or the number of "Div" elements in the body with random number of tags
     *                               and content contained within the tags
     * @param fullPage               A Boolean determining whether to return only a small segment of HTML or a
     *                               whole page of HTML with the standard format of a basic HTML page
     * @return A randomly generated string of HTML
     */
    public static String randomHTML(final int numberOfElementsInBody, boolean fullPage) {
        String generatedHtml = "";
        List<Div> divElements = new ArrayList<>();
        List<Node> nodeSegmentList = getNodeSegment(numberOfElementsInBody);


        if (fullPage) {
            Div div = new Div();
            div.appendChild(nodeSegmentList);
            divElements.add(div);
            for (int i = 1; i < numberOfElementsInBody; i++) {
                divElements.add(new Div().appendChild(getNodeSegment(DEFAULT_NUMBER_OF_ELEMENTS)));
            }

            Html html = new Html();
            Head head = new Head();
            html.appendChild(head);

            Title title = new Title();
            title.appendChild(new Text(RandomStringUtils.randomAlphabetic(DEFAULT_STRING_LENGTH)));
            head.appendChild(title);

            Body body = new Body();
            for (int i = 0; i < numberOfElementsInBody; i++) {
                body.appendChild(divElements.get(i));
            }
            html.appendChild(body);

            generatedHtml = html.write();
        } else {
            for (Node node : nodeSegmentList) {
                generatedHtml += node.write();
            }
        }
        return generatedHtml;
    }

    /**
     * Get a random string of XML with real HTML tags for
     * nodes and randomly generated content
     *
     * @return A String of random XML
     */
    public static String randomXML() {
        refreshAttributesList();
        Collections.shuffle(webAttributesList);
        String node = webAttributesList.get(0);
        String content = randomAlphanumeric(5);
        return String.format("<%s>%s</%s>", node, content, node);
    }

    /**
     * Get a randomly generated first and last name
     *
     * @return A String consisting of a first and last name
     */
    public static String randomName() {
        StringBuilder firstName = new StringBuilder();
        StringBuilder lastName = new StringBuilder();

        Random rand = new Random();
        int schemeFirstName = rand.nextInt(SCHEME_UPPER_BOUND);
        int schemeLastName = rand.nextInt(SCHEME_UPPER_BOUND);

        firstName.append(generateName(schemeFirstName, true));
        lastName.append(generateName(schemeLastName, false));

        return String.format("%s %s", firstName, lastName);
    }

    /**
     * Get a randomly generated first name
     *
     * @return A String consisting of a first name
     */
    public static String randomFirstName() {
        Random rand = new Random();
        return generateName(rand.nextInt(SCHEME_UPPER_BOUND), true);
    }

    /**
     * Get a randomly generated last name
     *
     * @return A String consisting of a last name
     */
    public static String randomLastName() {
        Random rand = new Random();
        return generateName(rand.nextInt(SCHEME_UPPER_BOUND), false);
    }

    /**
     * Fills this class's web attributes list with the web attributes enum from
     * WebElementAttribute.java for the random HTML, XML and JSON generators
     */
    private static void refreshAttributesList() {
        if (webAttributesList.size() == 0) {
            for (WebElementAttribute attr : WebElementAttribute.values()) {
                webAttributesList.add(attr.attribute);
            }
        }
    }

    /**
     * Generates a variety of variations first or last names
     *
     * @param scheme        An Int that will select one of the variations of first or last names
     * @param firstNameOnly A Boolean that determines whether a first name or last name is generated
     * @return A String that is a first name or a last name
     */
    protected static String generateName(final int scheme, boolean firstNameOnly) {
        StringBuilder name = new StringBuilder();
        if (firstNameOnly) {
            switch (scheme) {
                case 1:
                case 2:         //c-v-c-v-c or c-v-v-c-v-c
                    name.append(generateLetterByProbability(1, CONSONANT).toUpperCase());
                    name.append(generateLetterByProbability(scheme, VOWEL));
                    name.append((generateLetterByProbability(1, CONSONANT)));
                    name.append(generateLetterByProbability(1, VOWEL));
                    name.append(generateLetterByProbability(1, CONSONANT));
                    break;
                case 3:         //c-v-c-c-v
                    name.append(generateLetterByProbability(1, CONSONANT).toUpperCase());
                    name.append(generateLetterByProbability(1, VOWEL));
                    name.append((generateLetterByProbability(2, CONSONANT)));
                    name.append(generateLetterByProbability(1, VOWEL));
                    break;
                default:        //c-v-c-v
                    name.append(generateLetterByProbability(1, CONSONANT).toUpperCase());
                    name.append(generateLetterByProbability(1, VOWEL));
                    name.append(generateLetterByProbability(1, CONSONANT));
                    name.append(generateLetterByProbability(1, VOWEL));
                    break;
            }
        } else {
            switch (scheme) {
                case 1:
                case 2:  //c-v-c-c-v-c or c-v-v-c-c-v-c
                    name.append(generateLetterByProbability(1, CONSONANT).toUpperCase());
                    name.append(generateLetterByProbability(scheme, VOWEL));
                    name.append(generateLetterByProbability(2, CONSONANT));
                    name.append(generateLetterByProbability(1, VOWEL));
                    name.append(generateLetterByProbability(1, CONSONANT));
                    break;
                case 3:         //c-v-c-v-c-c
                    name.append(generateLetterByProbability(1, CONSONANT).toUpperCase());
                    name.append(generateLetterByProbability(1, VOWEL));
                    name.append(generateLetterByProbability(1, CONSONANT));
                    name.append(generateLetterByProbability(1, VOWEL));
                    name.append(generateLetterByProbability(2, CONSONANT));
                    break;
                default:        //c-v-c-v-c
                    name.append(generateLetterByProbability(1, CONSONANT).toUpperCase());
                    name.append(generateLetterByProbability(1, VOWEL));
                    name.append(generateLetterByProbability(1, CONSONANT));
                    name.append(generateLetterByProbability(1, VOWEL));
                    name.append(generateLetterByProbability(1, CONSONANT));
                    break;
            }
        }
        return name.toString();
    }

    /**
     * Get a randomly generated letter based on the frequency that it appears in names.
     *
     * @param length      The length of generated letters
     * @param isConsonant A Boolean determining whether to return a consonant or a vowel
     * @return A String of randomly generated vowels or consonants
     */
    protected static String generateLetterByProbability(final int length, boolean isConsonant) {
        StringBuilder generatedLetters = new StringBuilder("");

        LinkedHashMap<String, Double> hm = new LinkedHashMap<>();
        if (isConsonant) {
            hm.put("b", (1.49));
            hm.put("c", (2.71));
            hm.put("d", (4.32));
            hm.put("f", (2.30));
            hm.put("g", (2.03));
            hm.put("h", (5.92));
            hm.put("j", (0.10));
            hm.put("k", (0.69));
            hm.put("l", (3.98));
            hm.put("m", (2.61));
            hm.put("n", (6.95));
            hm.put("p", (1.82));
            hm.put("q", (0.11));
            hm.put("r", (6.02));
            hm.put("s", (6.28));
            hm.put("t", (9.12));
            hm.put("v", (1.11));
            hm.put("w", (2.09));
            hm.put("x", (0.17));
            hm.put("z", (0.07));
        } else {
            hm.put("a", (17.4));
            hm.put("e", (12.02));
            hm.put("i", (7.31));
            hm.put("o", (7.68));
            hm.put("u", (2.88));
            hm.put("y", (2.11));

        }

        for (int index = 0; index < length; index++) {
            Iterator<String> keySetIterator = hm.keySet().iterator();
            String key = keySetIterator.next();
            String keyStepAhead = keySetIterator.next();
            Double sum = 0.0;
            while (keySetIterator.hasNext()) {
                Double currentValue = hm.get(key);
                Double nextValue = hm.get(keyStepAhead);
                sum = currentValue + nextValue;
                hm.put(keyStepAhead, sum);

                // align the last iteration safely
                if (keyStepAhead.equals("y") || keyStepAhead.equals("x")) {
                    key = keyStepAhead;
                    keyStepAhead = keySetIterator.next();
                } else {
                    //KSA is now on x/y: move it to y/z, then update the value
                    key = keyStepAhead;
                    keyStepAhead = keySetIterator.next();
                    currentValue = hm.get(key);
                    nextValue = hm.get(keyStepAhead);
                    sum = currentValue + nextValue;
                    hm.put(keyStepAhead, sum);
                }
            }

            // Generate a random number to map to a value
            long roundedSum = Math.round(sum);
            Random rand = new Random();
            int randLetterChooser = rand.nextInt((int) roundedSum);

            // Find the letter corresponding to the random number
            Iterator<String> valueFinder = hm.keySet().iterator();
            String keyFinder = valueFinder.next();
            Double probabilitySum = hm.get(keyFinder);
            while (probabilitySum < randLetterChooser) {
                keyFinder = valueFinder.next();
                probabilitySum = hm.get(keyFinder);
            }
            generatedLetters.append(keyFinder);
        }
        return generatedLetters.toString();
    }
}
