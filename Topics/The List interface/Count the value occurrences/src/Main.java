class Counter {

    public static boolean checkTheSameNumberOfTimes(int elem, List<Integer> list1, List<Integer> list2) {
        int count=0;
        for (Integer val:list1) {
            if(val == elem)
                count++;
        }
        int count2=0;
        for (Integer val:list2) {
            if(val == elem)
                count2++;
        }
        if(count==count2)
            return true;
        return false;
    }
}