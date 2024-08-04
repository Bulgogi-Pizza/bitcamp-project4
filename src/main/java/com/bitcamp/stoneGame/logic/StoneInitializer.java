package com.bitcamp.stoneGame.logic;

import com.bitcamp.stoneGame.vo.Stone;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class StoneInitializer {

    public List<Stone> initializeStones(int startX, int startY, int cellSize) {
        List<Stone> stones = new ArrayList<>();
        stones.add(new Stone(getXFromColumn('D', startX, cellSize), getYFromRow(16, startY, cellSize), Color.BLACK, 1));
        stones.add(new Stone(getXFromColumn('G', startX, cellSize), getYFromRow(16, startY, cellSize), Color.BLACK, 1));
        stones.add(new Stone(getXFromColumn('J', startX, cellSize), getYFromRow(16, startY, cellSize), Color.BLACK, 1));
        stones.add(new Stone(getXFromColumn('M', startX, cellSize), getYFromRow(16, startY, cellSize), Color.BLACK, 1));
        stones.add(new Stone(getXFromColumn('P', startX, cellSize), getYFromRow(16, startY, cellSize), Color.BLACK, 1));
        stones.add(new Stone(getXFromColumn('D', startX, cellSize), getYFromRow(4, startY, cellSize), Color.WHITE, 2));
        stones.add(new Stone(getXFromColumn('G', startX, cellSize), getYFromRow(4, startY, cellSize), Color.WHITE, 2));
        stones.add(new Stone(getXFromColumn('J', startX, cellSize), getYFromRow(4, startY, cellSize), Color.WHITE, 2));
        stones.add(new Stone(getXFromColumn('M', startX, cellSize), getYFromRow(4, startY, cellSize), Color.WHITE, 2));
        stones.add(new Stone(getXFromColumn('P', startX, cellSize), getYFromRow(4, startY, cellSize), Color.WHITE, 2));
        return stones;
    }

    private int getXFromColumn(char column, int startX, int cellSize) {
        return startX + (column - 'A') * cellSize;
    }

    private int getYFromRow(int row, int startY, int cellSize) {
        return startY + (19 - row) * cellSize;
    }
}
