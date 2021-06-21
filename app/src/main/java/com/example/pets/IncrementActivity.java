package com.example.pets;

public class IncrementActivity {
    int count;

    public IncrementActivity(int count) {
        this.count = count;
    }

    public int getCount() {
        return count+1;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
