package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

public class NameEmailTagPredicateTest {
    @Test
    public void equals() {
        List<String> firstNameKeywordList = List.of("first");
        List<String> firstEmailKeywordList = List.of("u.nus.edu");
        Set<Tag> firstTagSet = Set.of(new Tag("friends", TagType.GENERAL));

        // different name keywords
        List<String> secondNameKeywordList = List.of("first", "second");
        List<String> secondEmailKeywordList = List.of("u.nus.edu");
        Set<Tag> secondTagSet = Set.of(new Tag("friends", TagType.GENERAL));

        // differemt email keywords
        List<String> thirdNameKeywordList = List.of("first");
        List<String> thirdEmailKeywordList = List.of("gmail.com");
        Set<Tag> thirdTagSet = Set.of(new Tag("friends", TagType.GENERAL));

        // different tags
        List<String> fourthNameKeywordList = List.of("first");
        List<String> fourthEmailKeywordList = List.of("u.nus.edu");
        Set<Tag> fourthTagSet = Set.of(new Tag("cs2103", TagType.GENERAL));

        NameEmailTagPredicate firstPredicate = new NameEmailTagPredicate(
                firstNameKeywordList, firstEmailKeywordList, firstTagSet);
        NameEmailTagPredicate secondPredicate = new NameEmailTagPredicate(
                secondNameKeywordList, secondEmailKeywordList, secondTagSet);
        NameEmailTagPredicate thirdPredicate = new NameEmailTagPredicate(
                thirdNameKeywordList, thirdEmailKeywordList, thirdTagSet);
        NameEmailTagPredicate fourthPredicate = new NameEmailTagPredicate(
                fourthNameKeywordList, fourthEmailKeywordList, fourthTagSet);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameEmailTagPredicate firstPredicateCopy = new NameEmailTagPredicate(
                firstNameKeywordList, firstEmailKeywordList, firstTagSet);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different persons -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));

        // different emails -> returns false
        assertFalse(firstPredicate.equals(thirdPredicate));

        // different tags -> returns false
        assertFalse(firstPredicate.equals(fourthPredicate));
    }

    @Test
    public void test_allFieldsMatch_returnsTrue() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of("Alice"),
                List.of("example.com"),
                Set.of(new Tag("friends", TagType.GENERAL)));

        assertTrue(predicate.test(ALICE)); // matches all

        predicate = new NameEmailTagPredicate(
                List.of("Benson"),
                List.of("example.com"),
                Set.of(new Tag("owesMoney", TagType.GENERAL)));
        assertTrue(predicate.test(BENSON));
    }

    @Test
    public void test_nameOnlyMatch_returnsTrue() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of("Alice"), List.of(), Set.of());

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_emailOnlyMatch_returnsTrue() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of(), List.of("example.com"), Set.of());

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_tagOnlyMatch_returnsTrue() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of(), List.of(), Set.of(new Tag("friends", TagType.GENERAL)));

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_multipleNameKeywordsMatch_returnsTrue() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of("Alice", "Benson"), List.of(), Set.of());

        assertTrue(predicate.test(ALICE));
        assertTrue(predicate.test(BENSON));
    }

    @Test
    public void test_emptyNameKeywords_returnsTrue() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of(), List.of("example.com"), Set.of(new Tag("friends", TagType.GENERAL)));

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_emptyEmailKeywords_returnsTrue() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of("Alice"), List.of(), Set.of(new Tag("friends", TagType.GENERAL)));

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_emptyTags_returnsTrue() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of("Alice"), List.of("example.com"), Set.of());

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_nameOnlyNoMatch_returnsFalse() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of("Alice"), List.of(), Set.of());

        assertFalse(predicate.test(BENSON));
    }

    @Test
    public void test_tagOnlyNoMatch_returnsFalse() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of(), List.of(), Set.of(new Tag("student", TagType.GENERAL)));

        assertFalse(predicate.test(BENSON));
    }

    @Test
    public void test_oneFieldFails_returnsFalse() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of("Alice"),
                List.of("nonexistent"),
                Set.of(new Tag("friends", TagType.GENERAL)));

        assertFalse(predicate.test(ALICE));
    }

    @Test
    public void test_emailOnlyNoMatch_returnsFalse() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of(), List.of("alice"), Set.of());

        assertFalse(predicate.test(BENSON));
    }

    @Test
    public void toStringMethod() {
        List<String> nameKeywords = List.of("person1", "person2");
        List<String> emailKeywords = List.of("email1", "email2");
        Set<Tag> tagSet = Set.of(new Tag("friends", TagType.GENERAL));

        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                nameKeywords, emailKeywords, tagSet);

        String expected = NameEmailTagPredicate.class.getCanonicalName()
                + "{nameKeywords=" + nameKeywords
                + ", emailKeywords=" + emailKeywords
                + ", tags=" + tagSet + "}";

        assertEquals(expected, predicate.toString());
    }
}
