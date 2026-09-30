package net.tokyosu.apocalypselib.menu.layout;

import java.util.ArrayList;
import java.util.List;

/** Row-major slot positions shared by menus and ingredient-viewer adapters. */
public record SlotGridLayout(int x, int y, int rows, int columns, int spacing) {
    public SlotGridLayout {
        if (rows < 0 || columns < 0 || spacing < 0) {
            throw new IllegalArgumentException("Grid dimensions and spacing must be nonnegative");
        }
    }

    public int size() { return Math.multiplyExact(rows, columns); }

    public List<Position> positions() {
        List<Position> positions = new ArrayList<>(size());
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                positions.add(new Position(column + row * columns, x + column * spacing, y + row * spacing));
            }
        }
        return List.copyOf(positions);
    }

    public record Position(int index, int x, int y) {}
}
