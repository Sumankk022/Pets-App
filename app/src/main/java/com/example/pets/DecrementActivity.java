package com.example.pets;

public class DecrementActivity {
    int count;

    public DecrementActivity(int count) {
        this.count = count;
    }

    public int getCount() {
        return count-1;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
