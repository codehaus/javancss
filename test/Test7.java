public class Test7 {
    public void foo() {
        int i = 5;
        int j = 6;
        // ncss: 4
        if (i == j) {
            return;
        }
        // 6
        for(;i > j;) {
        }
        // 7
        for(;i < j;) {
            i++;
        }
        // 9
        while(i != j) {
        }
        // 10
        while(i != j) {
            ;
        }
        // 11
        while(i == j) {
            i--;
        }
        // 13
        //do ; while(i == j);
        do {
        } while(i == j);
        // 14
        do {
            ;
        } while(i == j);
        // 15
        do {
            i++;
        } while(i == j);
        // 17
        {
        }
        // 17
        {
            int k;
        }
        // 18
        if (i == j) {
            i = 0;
        } else {
            i = 1;
        }
        // 22
       label1: {
        }
        // 23
       label2: {
            int l;
        }
        // 25
        switch(i) {
           case i == 0: {
                i = 10;
            }
           default: {
                i = 11;
            }
        }
        // 30
    }
}
