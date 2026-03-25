package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a Person's tags matches one of the tags given.
 */
public class PersonContainsTagsPredicate implements Predicate<Person> {
    private final List<String> tagNames;

    /**
     * Creates a {@code PersonContainsTagPredicate} using a list of tag names.
     * The tag names are converted to lowercase for case-insensitive comparison.
     *
     * @param tagNames The list of tag names
     */
    public PersonContainsTagsPredicate(List<String> tagNames) {
        requireAllNonNull(tagNames);
        this.tagNames = tagNames.stream()
                .map(String::toLowerCase)
                .toList();
    }

    @Override
    public boolean test(Person person) {
        return person.getTags().stream()
                .map(tag -> tag.tagName.toLowerCase(Locale.ROOT))
                .anyMatch(tagNames::contains);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonContainsTagsPredicate otherPersonContainsTagsPredicate)) {
            return false;
        }

        return tagNames.equals(otherPersonContainsTagsPredicate.tagNames);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("tags", tagNames).toString();
    }
}
