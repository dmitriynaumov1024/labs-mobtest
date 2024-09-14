package com.techyourchance.unittestingfundamentals.exercise1;

import org.junit.*;
import static org.hamcrest.CoreMatchers.is;

public class NegativeNumberValidatorTest {

    /*
    NegativeNumberValidator
    + isNegative (number: int): boolean
    ^ we assume it returns false if and only if number < 0
     */
    NegativeNumberValidator sut;

    @Before
    public void setup () {
        this.sut = new NegativeNumberValidator();
    }

    @Test
    public void onPositiveNumber () {
        int number = 1;
        Assert.assertThat(sut.isNegative(number), is(false));
    }

    @Test
    public void onNegativeNumber () {
        int number = -1;
        Assert.assertThat(sut.isNegative(number), is(true));
    }

    @Test
    public void onZero () {
        int number = 0;
        Assert.assertThat(sut.isNegative(number), is(false));
    }

}