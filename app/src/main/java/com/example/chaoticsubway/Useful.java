package com.example.chaoticsubway;

public class Useful {
    private static Useful instance = null;

    private Useful()
    {
    }

    public static Useful getInstance()
    {
        if (instance == null) instance = new Useful();
        return instance;
    }

    public static int[] getSliceOfArray(int[] arr, int start, int end)
    {
        int[] slice = new int[end - start];
        for (int i = 0; i < slice.length; i++) {
            slice[i] = arr[start + i];
        }
        return slice;
    }
}
