# proxx
Java implementation of  https://proxx.app game

## Usage example

- Build & run project:
```sh
mvn clean install 

MAX_BOARD_SIZE=10
NUM_BLACK_HOLES=10

mvn compile exec:java -Dexec.mainClass="com.vladimir22.Main" -Dexec.args="$MAX_BOARD_SIZE $NUM_BLACK_HOLES"
```
  

Play game, example:

```sh
Enter X and Y
2 4

XY	x0	x1	x2	x3	x4	x5	x6	x7	x8	x9	
y0	0	0	0	0	0	0	0	0	0	0
y1	0	0	0	0	0	0	0	1	1	1
y2	0	0	0	0	0	0	0	1	.	.
y3	0	0	0	0	0	0	0	1	1	.
y4	0	0	[0]	0	0	0	0	0	0	0
y5	0	0	0	1	2	2	1	0	0	0
y6	0	0	0	1	.	.	1	0	0	0
y7	0	0	0	1	2	2	1	0	0	0
y8	0	0	0	0	0	0	0	1	2	2
y9	0	0	0	0	0	0	0	1	.	.


Enter X and Y
9 3

XY	x0	x1	x2	x3	x4	x5	x6	x7	x8	x9	
y0	0	0	0	0	0	0	0	0	0	0
y1	0	0	0	0	0	0	0	1	1	1
y2	0	0	0	0	0	0	0	1	.	.
y3	0	0	0	0	0	0	0	1	1	[1]
y4	0	0	0	0	0	0	0	0	0	0
y5	0	0	0	1	2	2	1	0	0	0
y6	0	0	0	1	.	.	1	0	0	0
y7	0	0	0	1	2	2	1	0	0	0
y8	0	0	0	0	0	0	0	1	2	2
y9	0	0	0	0	0	0	0	1	.	.


Enter X and Y
9 9

XY	x0	x1	x2	x3	x4	x5	x6	x7	x8	x9	
y0	0	0	0	0	0	0	0	0	0	0
y1	0	0	0	0	0	0	0	1	1	1
y2	0	0	0	0	0	0	0	1	*	1
y3	0	0	0	0	0	0	0	1	1	1
y4	0	0	0	0	0	0	0	0	0	0
y5	0	0	0	1	2	2	1	0	0	0
y6	0	0	0	1	*	*	1	0	0	0
y7	0	0	0	1	2	2	1	0	0	0
y8	0	0	0	0	0	0	0	1	2	2
y9	0	0	0	0	0	0	0	1	*	[*]
Game Over! Black Hole has been opened ...

```
