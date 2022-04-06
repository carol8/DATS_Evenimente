package com.carol8.datsevenimente.model;

public class CheckBoxItem implements Comparable<CheckBoxItem>{
    private final String key;
    private int value;

    public CheckBoxItem(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(CheckBoxItem checkBoxItem) {
        if(this.getValue() != checkBoxItem.getValue()){
            return this.getValue() - checkBoxItem.getValue();
        }
        else{
            return this.getKey().compareTo(checkBoxItem.getKey());
        }
    }
}
