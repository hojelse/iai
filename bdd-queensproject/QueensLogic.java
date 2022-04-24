import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;

/**
 * This class implements a basic logic for the n-queens problem to get you started. 
 * Actually, when inserting a queen, it only puts the queen where requested
 * and does not keep track of which other positions are made illegal by this move.
 * 
 * @author Mai Ajspur
 * @version 16.02.2018
 */

public class QueensLogic implements IQueensLogic{
    private int size;		// Size of quadratic game board (i.e. size = #rows = #columns)
    private int[][] board;	// Content of the board. Possible values: 0 (empty), 1 (queen), -1 (no queen allowed)

    private BDD bdd;
    private BDDFactory fac;

    private BDD TRUE;
    private BDD FALSE;
    
    public void initializeBoard(int size) {
        this.size = size;
        this.board = new int[size][size];

        int numNodes = 2000000;
        int cacheSize = 200000;
        int varNum = size*size;

        // setup factory
        this.fac = JFactory.init(numNodes, cacheSize);
        fac.setVarNum(varNum);

        // setup aliases
        this.TRUE = fac.one();
        this.FALSE = fac.zero();

        // create BDD
        this.bdd = setupBDD();

        crossOut();
    }

    private BDD setupBDD() {
        BDD bdd = cnf();

        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++)
                bdd = bdd.and(tileBDD(col, row));

        return bdd;
    }

    /**
     * Create Conjunctive Normal Form of rows:
     * 
     * ( x0 OR  x1 OR  x2 OR  x3) AND
     * ( x4 OR  x5 OR  x6 OR  x7) AND
     * ( x8 OR  x9 OR x10 OR x11) AND
     * (x12 OR x13 OR x14 OR x15)
     * 
     */
    private BDD cnf() {
        BDD cnf = fac.one();
        for (int row = 0; row < size; row++) {
            BDD rowBDD = fac.zero();
            for (int col = 0; col < size; col++) {
                rowBDD = rowBDD.or(x(col, row));
            }
            cnf = cnf.and(rowBDD);
        }
        return cnf;
    }

    private BDD tileBDD(int col, int row) {
        BDD sameRow = sameRowBDD(col, row);
        BDD sameCol = sameColBDD(col, row);
        BDD sameDiag1 = sameDiag1(col, row);
        BDD sameDiag2 = sameDiag2(col, row);

        BDD acc = fac.one()
            .and(sameRow)
            .and(sameCol)
            .and(sameDiag1)
            .and(sameDiag2);

        BDD curr = x(col, row);
        return curr.imp(acc);
    }

    private BDD sameColBDD(int col, int row) {
        BDD bdd = fac.one();
        for (int r = 0; r < size; r++) {
            if (r == row) continue;
            bdd = bdd.and(x(col, r).not());
        }
        return bdd;
    }

    private BDD sameRowBDD(int col, int row) {
        BDD bdd = fac.one();
        for (int c = 0; c < size; c++) {
            if (c == col) continue;
            bdd = bdd.and(x(c, row).not());
        }
        return bdd;
    }

    private BDD sameDiag1(int col, int row) {
        BDD bdd = fac.one();
        
        int c = col - Math.min(col, row);
        int r = row - Math.min(col, row);

        while (c < size && r < size) {
            if (c != col || r != row)
                bdd = bdd.and(x(c, r).not());

            c++; r++;
        }

        return bdd;
    }

    private BDD sameDiag2(int col, int row) {
        BDD bdd = fac.one();
        
        int c = col - Math.min(col, size - 1 - row);
        int r = row + Math.min(col, size - 1 - row);

        while (c < size && r >= 0) {
            if (c != col || r != row)
                bdd = bdd.and(x(c, r).not());

            c++; r--;
        }

        return bdd;
    }

    public int[][] getBoard() {
        return board;
    }

    public void insertQueen(int col, int row) {
        if (board[col][row] == -1) return;

        board[col][row] = 1;
        bdd = bdd.restrict(x(col, row));

        crossOut();
    }

    private void crossOut() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[col][row] != 0) continue;

                BDD hasQueen = bdd.restrict(x(col, row));
                BDD notHasQueen = bdd.restrict(x(col, row).not());
                if (hasQueen.isZero()) {
                    board[col][row] = -1;
                    bdd = notHasQueen;
                } else if (notHasQueen.isZero()) {
                    board[col][row] = 1;
                    bdd = hasQueen;
                }
            }
        }
    }

    public BDD x(int index) {
        return fac.ithVar(index);
    }

    public BDD x(int col, int row) {
        return fac.ithVar(index(col, row));
    }

    public int index(int col, int row) {
        if (-1 >= col || col >= size)
            throw new RuntimeException(String.format("arg col=%s out of bounds for board of size %s in varFromCoords()", col, size));

        if (-1 >= row || row >= size)
            throw new RuntimeException(String.format("arg row=%s out of bounds for board of size %s in varFromCoords()", row, size));

        return row * size + col;
    }

    public int[] coords(int i) {
        if (i >= size*size)
            throw new RuntimeException(String.format("arg i=%s out of bounds for board of size %s in coordsFromVar()", i, size));

        int row = Math.floorDiv(i, size);
        int col = i - row * size;
        return new int[] { col, row };
    }
}
