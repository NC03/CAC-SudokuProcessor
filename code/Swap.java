
// 7->1
// 2->3
// 5->6
// 5->8
// 8->1
// 2->1
// 2->3

// 7->1
// 0.24639186769087879
// 0.07072205183215297
// 2->3
// 0.03498492474601281
// 0.01616139860042448
// 5->6
// 0.0010936953026916552
// 0.07981420250879157
// 5->8
// 0.019985932512369575
// 0.07318081895219236
// 8->1
// 0.04152769595420802
// 0.10249384020939589
// 2->1
// 0.010533332933729433
// 0.36507319092296675
// 2->3
// 0.023360007072092848
// 0.02633991341122377

public class Swap {

    public static final Swap[] swaps = { new Swap(7, 1, 0.24639186769087879, 0.07072205183215297),
            new Swap(7, 1, 0.32324087733949225, 1.5404781047356775),
            new Swap(2, 3, 0.20398507926436138, 0.303118881089654),
            new Swap(5, 6, 0.16253473485165917, 0.3369035361921297),
            new Swap(5, 8, 0.1798985227637665, 0.37177937169885095),
            new Swap(8, 1, 0.2025381372436522, 0.8297976270609912),
            new Swap(2, 1, 0.2903100101813998, 1.6820715665335142),
            new Swap(2, 3, 0.36387014577998267, 0.7474590689378917)

    };

    private int start;
    private int end;
    private double rowError;
    private double colError;

    public Swap(int start, int end, double rowError, double colError) {
        this.start = start;
        this.end = end;
        this.rowError = rowError;
        this.colError = colError;
    }

    public boolean valid(double row, double col) {
        return withinRange(row, rowError, 0.01) && withinRange(col, colError, 0.01);
    }

    public boolean change(int num) {
        return num == start;
    }

    public boolean withinRange(double a, double b, double range) {
        return Math.abs(a - b) < range;
    }

    public int endNum() {
        return end;
    }
}