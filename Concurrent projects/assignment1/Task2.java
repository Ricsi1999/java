package sorting;

import java.util.Arrays;

/* Task2: no slicing, no bullshit memcopy, parralelized merge */
public class Task2 {

    /* Create new sorted array by merging 2 smaller sorted arrays */
    private static void merge(int[] src, int[] dst, int idx1, int idx2, int end) {
        
        int border = idx2;
        int mergeIndex = idx1;
        
        while(idx1 < border && idx2 < end) {
            if(src[idx1] < src[idx2]) {
                dst[mergeIndex] = src[idx1++];
            }
            else {
                dst[mergeIndex] = src[idx2++];
            }
            ++mergeIndex;
        }
        
        while(idx1 < border) {
            dst[mergeIndex++] = src[idx1++];
        }

        while(idx2 < end) {
            dst[mergeIndex++] = src[idx2++];
        }
    }

    /* Recursive core, calls a sibling thread until max depth is reached */
    public static void kernel(int[] src, int[] dst, int start, int end, int depth) {
        
        if(depth == 0) {
            /* Single thread sort if bottom has been reached */
            Arrays.sort(src, start, end);
        }
        else {
            /* Otherwise split task into two recursively */
            int middleIndex = start + ((end - start) / 2);
            
            Thread t = new Thread(() -> {
                kernel(src, dst, start, middleIndex, depth - 1);
            });
            
            t.start();
            
            kernel(src, dst, middleIndex, end, depth - 1);
            
            try {
                t.join();
            }
            catch (InterruptedException ex) {
                System.out.println(ex);
            }
            
            if(depth % 2 == 0) {
                merge(dst, src, start, middleIndex, end);
            }
            else {
                merge(src, dst, start, middleIndex, end);
            }
        }
    }

    /* Creates a sorted version of any int array */
    public static int[] sort(int[] array) {

        /* Initialize variables */
        int length = array.length;
        int[] src = new int[length];
        int[] dst = new int[length];
        
        for(int i = 0; i < length; i++) {
            src[i] = array[i];
            dst[i] = 0;
        }
        
        int start = 0, end = length;

        /* Calculate optimal depth */
        int minSize = 1000;
        int procNum = Runtime.getRuntime().availableProcessors();
        int procDepth = (int) Math.ceil(Math.log(procNum) / Math.log(2));
        int arrDepth = (int) (Math.log(array.length / minSize) / Math.log(2));
        int optDepth = Math.max(0, Math.min(procDepth, arrDepth));

        /* Launch recursive core */
        kernel(src, dst, start, end, optDepth);
        
        if(optDepth % 2 == 0) {
            return src;
        }
        else {
            return dst;
        }
    }
}