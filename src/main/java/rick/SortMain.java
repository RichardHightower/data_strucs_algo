package rick;

import java.util.*;


import static java.lang.System.out;


public class SortMain {

    public static void main(String[] args) {
        Sort qSorter = new QuickSort();
        Sort mergeSorter = new MergeSort();
        Sort comboSorter = new QuickMergeSort(2);
        List<Sort> sorters = Arrays.asList(qSorter, mergeSorter, comboSorter);
        sorters.forEach(sorter -> {
            Collections.shuffle(Arrays.asList(names));
            //out.printf("Before sorter=%s, %s\n", sorter.getClass().getSimpleName(), Arrays.toString(names));
            sorter.sort(names);
            out.printf("After sorter=%s, %s\n", sorter.getClass().getSimpleName(), Arrays.toString(names));
        });
    }


    private static String[] names = {"Bob", "Rick", "Aaron", "Nick", "Zoo", "Zze", "Ann", "Joe", "Sam", "Dora", "Maya"};

    public static interface Sort {
        void sort(String[] array);
    }


    static void mergeSortToOutput(String[] leftArray, String[] rightArray, String[] outputArray, int offset) {


        int idxLeft = 0;
        int idxRight = 0;
        int index = offset;

        while (idxLeft < leftArray.length && idxRight < rightArray.length) {
            String leftValue = leftArray[idxLeft];
            String rightValue = rightArray[idxRight];
            if (leftValue.compareTo(rightValue) < 0) {
                outputArray[index] = leftArray[idxLeft];
                idxLeft++;
            } else {
                outputArray[index] = rightArray[idxRight];
                idxRight++;
            }
            index++;
        }

        while (idxLeft < leftArray.length) {
            outputArray[index] = leftArray[idxLeft];
            idxLeft++;
            index++;
        }

        while (idxRight < rightArray.length) {
            outputArray[index] = rightArray[idxRight];
            idxRight++;
            index++;
        }
    }



    public static class MergeSort implements Sort {

        @Override
        public void sort(String[] array) {
            mergeSort(array, 0, array.length - 1);
        }

        void mergeSort(String[] array, int leftIndex, int rightIndex) {
            if (leftIndex < rightIndex) {
                int middleIndex = (leftIndex + rightIndex) / 2;

                mergeSort(array, middleIndex + 1, rightIndex);
                mergeSort(array, leftIndex, middleIndex);

                merge(array, leftIndex, rightIndex);
            }
        }

        void merge(String[] array, int leftIndex, int rightIndex) {

            int middleIndex = (leftIndex + rightIndex) / 2;


            String[] leftArray = new String[middleIndex - leftIndex + 1];
            String[] rightArray = new String[rightIndex - middleIndex];

            System.arraycopy(array, leftIndex, leftArray, 0, leftArray.length);
            System.arraycopy(array, middleIndex + 1, rightArray, 0, rightArray.length);

            mergeSortToOutput(leftArray, rightArray, array, leftIndex);

        }




    }

    public static class QuickSort implements Sort {



        @Override
        public void sort(String[] array) {
            quickSort(array, 0, array.length - 1);
        }

        static int quickPartition(String[] array, final int startIndex, final int untilIndex) {


            String pivotValue = array[untilIndex];
            int leftIndex = (startIndex - 1);
            for (int rightIndex = startIndex; rightIndex < untilIndex; rightIndex++) {
                String currentValue = array[rightIndex];
                if (currentValue.compareTo(pivotValue) < 0) {
                    leftIndex++;

                    if (leftIndex != rightIndex)
                        swap(array, leftIndex, rightIndex, currentValue);
                }
            }
            swap(array, leftIndex + 1, untilIndex, array[untilIndex]);


            return leftIndex + 1;
        }

        private static void swap(String[] array, int leftIndex, int rightIndex, String currentValue) {


            String leftValue = array[leftIndex];
            //out.printf("SWAP %s, %s, %s, %s \n", leftIndex, rightIndex, currentValue, leftValue);
            array[leftIndex] = currentValue;
            array[rightIndex] = leftValue;
        }


        void quickSort(String[] array, final int startIndex, final int untilIndex) {
            if (startIndex < untilIndex) {

//                out.println("Before quickSort Call " +  " for " +
//                        " start " + startIndex + " until " + untilIndex + " " + " pivot value "
//                        + Arrays.toString(names));

                int partitionIndex = quickPartition(array, startIndex, untilIndex);

                quickSort(array, startIndex, partitionIndex - 1);
                quickSort(array, partitionIndex + 1, untilIndex);
//                out.println("After quickSort Call " +  " for " +
//                        " start " + startIndex + " until " + untilIndex + " " + " pivot value "
//                        + Arrays.toString(names));
            }


        }
    }


    public static class QuickMergeSort implements Sort {


        private final int partitions;

        private final Sort sorter = new QuickSort();

        public QuickMergeSort(int partitions) {
            this.partitions = partitions;
        }

        @Override
        public void sort(String[] array) {

            int sizeOfEachArray = array.length / partitions;
            int leftOver = array.length % partitions;

            String[][] arrays = new String[partitions][];

            int used = 0;

            for (int count =0; count < partitions; count++) {

                final int startIndex = count * sizeOfEachArray;
                final int endIndex;
                final int size;

                if (count +1 == partitions) {
                    endIndex =  startIndex+sizeOfEachArray + leftOver;
                    size = sizeOfEachArray + leftOver;

                } else {
                    endIndex = startIndex+sizeOfEachArray;
                    size = sizeOfEachArray;
                }

                arrays[count] = new String[size];
                System.arraycopy(array, startIndex, arrays[count], 0, size);

                sorter.sort(arrays[count]);

                //out.printf("%s %s %s %s\n", startIndex, endIndex, size, Arrays.toString(arrays[count]));

            }


            for (int count =1; count < partitions; count++) {
                var leftArray = arrays[count-1];
                var rightArray = arrays[count];

                final int offset = (count-1) * sizeOfEachArray;

                mergeSortToOutput(leftArray, rightArray, array, offset);
            }
        }
    }




}
