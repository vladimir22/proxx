package com.vladimir22;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Cell {
    private boolean isOpened;
    private boolean isBlackHole;
    private int adjacentBlackHoles; // number of neighbour black holes
}
