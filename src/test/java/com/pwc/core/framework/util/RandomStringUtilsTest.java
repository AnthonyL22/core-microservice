package com.pwc.core.framework.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.Assert;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.not;

@RunWith(MockitoJUnitRunner.class)
public class RandomStringUtilsTest {

    private static final int ALPHABET_COUNT = 50;
    private static final int SMALLEST_FIRST_NAME = 4;
    private static final int LARGEST_FIRST_NAME = 6;
    private static final int SMALLEST_LAST_NAME = 5;
    private static final int LARGEST_LAST_NAME = 7;
    private static final String[] vowels = {"a", "e", "i", "o", "u", "y"};
    private static final String TEST_SENTENCE = "Hello my name is Anthony, how are you?";


    @Test
    public void getRandomVowelTest() {
        for (int i = 0; i < ALPHABET_COUNT; i++) {
            String vowel = RandomStringUtils.generateLetterByProbability(1, false);
            assertThat(vowel, isOneOf(vowels));
        }
    }

    @Test
    public void getRandomVowelShouldFailTest() {
        String notVowel = RandomStringUtils.generateLetterByProbability(1, true);
        assertThat(notVowel, not(isOneOf(vowels)));
    }

    @Test
    public void getRandomConsonantTest() {
        for (int i = 0; i < ALPHABET_COUNT; i++) {
            String consonant = RandomStringUtils.generateLetterByProbability(1, true);
            assertThat(consonant, not(isOneOf(vowels)));
        }
    }

    @Test
    public void getRandomConsonantShouldFailTest() {
        String consonant = RandomStringUtils.generateLetterByProbability(1, true);
        Assert.assertFalse(StringUtils.containsAny(consonant, 'a', 'e', 'i', 'o', 'u', 'y'));
    }

    @Test
    public void getRandomFirstNameTest() {
        Random rand = new Random();
        String firstTime = RandomStringUtils.generateName(rand.nextInt(3), true);
        String secondTime = RandomStringUtils.generateName(rand.nextInt(3), true);
        assertThat(firstTime, is(not(equalTo(secondTime))));
        assertThat(firstTime.length(), CoreMatchers.is(CoreMatchers.both(Matchers.greaterThanOrEqualTo((Comparable) SMALLEST_FIRST_NAME)).and(Matchers.lessThanOrEqualTo((Comparable) LARGEST_FIRST_NAME))));
        assertThat(secondTime.length(), CoreMatchers.is(CoreMatchers.both(Matchers.greaterThanOrEqualTo((Comparable) SMALLEST_FIRST_NAME)).and(Matchers.lessThanOrEqualTo((Comparable) LARGEST_FIRST_NAME))));
    }

    @Test
    public void randomFirstNameTest() {
        String firstTime = RandomStringUtils.randomFirstName();
        String secondTime = RandomStringUtils.randomFirstName();
        assertThat(firstTime, is(not(equalTo(secondTime))));
    }

    @Test
    public void randomSecondNameTest() {
        String firstTime = RandomStringUtils.randomLastName();
        String secondTime = RandomStringUtils.randomLastName();
        assertThat(firstTime, is(not(equalTo(secondTime))));
    }

    @Test
    public void getRandomLastNameTest() {
        Random rand = new Random();
        String firstTime = RandomStringUtils.generateName(rand.nextInt(3), false);
        String secondTime = RandomStringUtils.generateName(rand.nextInt(3), false);
        assertThat(firstTime, is(not(equalTo(secondTime))));
        assertThat(firstTime.length(), CoreMatchers.is(CoreMatchers.both(Matchers.greaterThanOrEqualTo((Comparable) SMALLEST_LAST_NAME)).and(Matchers.lessThanOrEqualTo((Comparable) LARGEST_LAST_NAME))));
        assertThat(secondTime.length(), CoreMatchers.is(CoreMatchers.both(Matchers.greaterThanOrEqualTo((Comparable) SMALLEST_LAST_NAME)).and(Matchers.lessThanOrEqualTo((Comparable) LARGEST_LAST_NAME))));
    }

    @Test
    public void getRandomFullNameTest() {
        String firstTime = RandomStringUtils.randomName();
        String secondTime = RandomStringUtils.randomName();
        assertThat(firstTime, is(not(equalTo(secondTime))));
    }

    @Test
    public void randomAlphabeticTest() {
        String firstTime = RandomStringUtils.randomAlphabetic(3);
        String secondTime = RandomStringUtils.randomAlphabetic(2);
        assertThat(firstTime, is(not(equalTo(secondTime))));
        assertThat(firstTime.length(), is(equalTo(3)));
        assertThat(secondTime.length(), is(equalTo(2)));
    }

    @Test
    public void randomHTMLFullPageTest() {
        String page = RandomStringUtils.randomHTML(4, true);
        Assert.assertNotEquals(page, "");
        assertThat(page, is(not(equalTo(""))));
    }

    @Test
    public void randomHTMLSegmentTest() {
        String page = RandomStringUtils.randomHTML(5, false);
        assertThat(page, is(not(equalTo(""))));
    }

    @Test
    public void zeroLengthRandomHTMLSegmentTest() {
        String page = RandomStringUtils.randomHTML(0, false);
        assertThat(page, is(equalTo("")));
    }

    @Test
    public void randomAlphanumericTest() {
        String firstTime = RandomStringUtils.randomAlphanumeric(3);
        String secondTime = RandomStringUtils.randomAlphanumeric(2);
        assertThat(firstTime, is(not(equalTo(secondTime))));
        assertThat(firstTime.length(), is(equalTo(3)));
        assertThat(secondTime.length(), is(equalTo(2)));
    }

    @Test
    public void randomNumericTest() {
        String firstTime = RandomStringUtils.randomNumeric(3);
        String secondTime = RandomStringUtils.randomNumeric(2);
        assertThat(firstTime, is(not(equalTo(secondTime))));
        assertThat(firstTime.length(), is(equalTo(3)));
        assertThat(secondTime.length(), is(equalTo(2)));
    }

    @Test
    public void randomXMLTest() {
        String firstTime = RandomStringUtils.randomXML();
        String secondTime = RandomStringUtils.randomXML();
        assertThat(firstTime, is(not(equalTo(secondTime))));
    }


    @Test
    public void getRandomBeginningSubStringTests() {
        String result = RandomStringUtils.getRandomSubStringFromBeginning(TEST_SENTENCE);
        Assert.assertTrue(result.length() > 0);
    }

    @Test
    public void getRandomSubSentenceFragmentTests() {
        String result = RandomStringUtils.getRandomSubSentenceFromBeginning("Hello my name is Anthony");
        Assert.assertTrue(result.length() > 0);
    }

    @Test
    public void getRandomEndingSubStringTests() {
        String result = RandomStringUtils.getRandomEndingSubString("Hello my name is Anthony");
        Assert.assertTrue(result.length() > 0);
    }


}
