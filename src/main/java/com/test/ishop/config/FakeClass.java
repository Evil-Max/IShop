package com.test.ishop.config;

public class FakeClass {
    private FakeData fakeData;

    public FakeData getFakeData() {
        return fakeData;
    }

    public void setFakeData(FakeData fakeData) {
        this.fakeData = fakeData;
    }

    @Override
    public String toString() {
        return "FakeClass{" +
                "fakeData=" + fakeData +
                '}';
    }
}
