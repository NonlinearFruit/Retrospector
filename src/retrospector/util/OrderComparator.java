/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;

public class OrderComparator implements Comparator<String> {

    public int compare(String o1, String o2) {
        NaturalOrderComparableGroup o1Comparable = new NaturalOrderComparableGroup(o1);
        NaturalOrderComparableGroup o2Comparable = new NaturalOrderComparableGroup(o2);
        return o1Comparable.compareTo(o2Comparable);
    }

}



class NaturalOrderComparableGroup implements Comparable<NaturalOrderComparableGroup> {

    private final List<NaturalOrderComparableElement> elements;

    public NaturalOrderComparableGroup(String string) {
        List<NaturalOrderComparableElement> elements = new ArrayList<>();
        int i = 0;
        while (string.length() > i) {
            Character character = string.charAt(i);
            if (isAlphabetic(character)) {
                elements.add(new AlphabeticComparableElement(character));
                i++;
            } else if (isDigit(character)) {
                NumericComparableElement numericComparableElement = new NumericComparableElement(character);
                i++;
                while (string.length() >= i + 1 && isDigit(string.charAt(i))) {
                    numericComparableElement.add(string.charAt(i));
                    i++;
                }
                elements.add(numericComparableElement);
            } else {
                elements.add(new NonAlphanumericComparableElement(character));
                i++;
            }
        }
        this.elements = elements;
    }

    public int compareTo(NaturalOrderComparableGroup o) {
        int i = 0;
        while (!anyOfTheGroupsDoesntHaveEnoughElements(o, i)) {
            if (elements.get(i).compareTo(o.elements.get(i)) != 0) {
                return elements.get(i).compareTo(o.elements.get(i));
            }
            i++;
        }
        return shortest(o);
    }

    private boolean anyOfTheGroupsDoesntHaveEnoughElements(NaturalOrderComparableGroup o, int i) {
        return elements.size() < i + 1 || o.elements.size() < i + 1;
    }

    private int shortest(NaturalOrderComparableGroup o) {
        return ((Integer) elements.size()).compareTo(o.elements.size());
    }
}


interface NaturalOrderComparableElement<T extends NaturalOrderComparableElement> extends Comparable<NaturalOrderComparableElement> {

    Integer getPriority();

    @Override
    @SuppressWarnings("unchecked")
    default int compareTo(NaturalOrderComparableElement other) {
        int typeComparisonResult = this.getPriority().compareTo(other.getPriority());
        if (typeComparisonResult != 0) {
            return typeComparisonResult;
        } else {
            // Safe check because at this point we know that other is an instance of this same class
            return compareToInstanceOfSameType((T) other);
        }
    }

    int compareToInstanceOfSameType(T other);
}


class AlphabeticComparableElement implements NaturalOrderComparableElement<AlphabeticComparableElement> {

    private static final Integer PRIORITY = 3;

    private final Character character;

    public AlphabeticComparableElement(Character character) {
        this.character = character;
    }

    @Override
    public Integer getPriority() {
        return PRIORITY;
    }

    @Override
    public int compareToInstanceOfSameType(AlphabeticComparableElement other) {
        return this.character.compareTo(other.character);
    }
}


class NumericComparableElement implements NaturalOrderComparableElement<NumericComparableElement> {

    private static final Integer PRIORITY = 2;

    private Integer integer;

    public NumericComparableElement(Character character) {
        this.integer = Integer.valueOf(character.toString());
    }

    @Override
    public Integer getPriority() {
        return PRIORITY;
    }

    @Override
    public int compareToInstanceOfSameType(NumericComparableElement other) {
        return this.integer.compareTo(other.integer);
    }

    public void add(char c) {
        this.integer = this.integer * 10 + (int) c;
    }
}


class NonAlphanumericComparableElement implements NaturalOrderComparableElement<NonAlphanumericComparableElement> {

    private static final Integer PRIORITY = 1;

    private final Character character;

    public NonAlphanumericComparableElement(Character character) {
        this.character = character;
    }

    @Override
    public Integer getPriority() {
        return PRIORITY;
    }

    @Override
    public int compareToInstanceOfSameType(NonAlphanumericComparableElement other) {
        return this.character.compareTo(other.character);
    }
}
