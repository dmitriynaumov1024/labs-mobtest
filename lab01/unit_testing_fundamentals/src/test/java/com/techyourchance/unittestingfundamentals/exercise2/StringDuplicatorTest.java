package com.techyourchance.unittestingfundamentals.exercise2;

import org.junit.*;
import static org.hamcrest.CoreMatchers.is;

public class StringDuplicatorTest {

    StringDuplicator sut;

    public void checkDuplication(String original, String duplicated) {
        if (original == null) {
            Assert.assertNull(duplicated);
            return;
        }
        Assert.assertThat(duplicated.length(), is(original.length() * 2));
        int size = original.length();
        for (int i=0; i<size; i++) {
            Assert.assertThat(duplicated.charAt(i), is(original.charAt(i)));
            Assert.assertThat(duplicated.charAt(i+size), is(original.charAt(i)));
        }
    }

    @Before
    public void setup() {
        this.sut = new StringDuplicator();
    }

    @Test
    public void onSomeString() {
        String str = "12345abc";
        checkDuplication(str, sut.duplicate(str));
    }

    @Test
    public void onSomeOtherString() {
        String str = "0123456789";
        checkDuplication(str, sut.duplicate(str));
    }

    @Test
    public void onEmptyString() {
        String str = "";
        checkDuplication(str, sut.duplicate(str));
    }

    @Test
    public void onNullString() {
        String str = null;
        checkDuplication(str, sut.duplicate(str));
    }
}