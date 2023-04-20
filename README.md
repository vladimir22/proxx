# proxx
Java implementation of  https://proxx.app game

## Usage example

### - Clone and build project:
```sh
git clone https://github.com/vladimir22/proxx.git
cd proxx
mvn clean install 
```

### - Run game:
- Linux OS:
```shell
MAX_BOARD_SIZE=10
NUM_BLACK_HOLES=10
echo "Going to run proxx game with params: maxSize=$MAX_BOARD_SIZE, numBlackHoles=$NUM_BLACK_HOLES"
mvn compile exec:java -Dexec.mainClass="com.vladimir22.Play" -Dexec.args="$MAX_BOARD_SIZE $NUM_BLACK_HOLES"
```

- Win OS:
```shell

set MAX_BOARD_SIZE=10
set NUM_BLACK_HOLES=10
echo "Going to run proxx game with params: maxSize=%MAX_BOARD_SIZE%, numBlackHoles=%NUM_BLACK_HOLES%"
mvn compile exec:java -Dexec.mainClass="com.vladimir22.Play" -Dexec.args="%MAX_BOARD_SIZE% %NUM_BLACK_HOLES%"
```

### - Play game
Output example:
```sh
Enter X and Y coordinates from 0 to 9
10 10
Wrong coordinates


Enter X and Y coordinates from 0 to 9
5 5

XY	x0	x1	x2	x3	x4	x5	x6	x7	x8	x9	
y0	.	1	0	0	0	0	0	1	.	.
y1	.	1	1	1	1	0	0	1	.	.
y2	.	.	.	.	2	0	0	1	1	1
y3	.	.	.	.	3	0	0	0	0	0
y4	.	.	.	.	2	0	0	0	0	0
y5	.	.	.	.	2	[0]	0	0	0	0
y6	.	.	.	.	1	1	1	1	0	0
y7	.	.	.	.	.	.	.	2	0	0
y8	.	.	.	.	.	.	.	2	0	0
y9	.	.	.	.	.	.	.	1	0	0


Enter X and Y coordinates from 0 to 9
0 9

XY	x0	x1	x2	x3	x4	x5	x6	x7	x8	x9	
y0	.	1	0	0	0	0	0	1	.	.
y1	.	1	1	1	1	0	0	1	.	.
y2	.	.	.	.	2	0	0	1	1	1
y3	.	.	.	.	3	0	0	0	0	0
y4	.	.	.	.	2	0	0	0	0	0
y5	.	.	.	.	2	0	0	0	0	0
y6	.	.	.	.	1	1	1	1	0	0
y7	1	1	1	2	.	.	.	2	0	0
y8	0	0	0	1	.	.	.	2	0	0
y9	[0]	0	0	1	.	.	.	1	0	0


Enter X and Y coordinates from 0 to 9
5 tret
Incorrect input, please try again


Enter X and Y coordinates from 0 to 9
4 7

XY	x0	x1	x2	x3	x4	x5	x6	x7	x8	x9	
y0	.	1	0	0	0	0	0	1	.	.
y1	.	1	1	1	1	0	0	1	.	.
y2	.	.	.	.	2	0	0	1	1	1
y3	.	.	.	.	3	0	0	0	0	0
y4	.	.	.	.	2	0	0	0	0	0
y5	.	.	.	.	2	0	0	0	0	0
y6	.	.	.	.	1	1	1	1	0	0
y7	1	1	1	2	[2]	.	.	2	0	0
y8	0	0	0	1	.	.	.	2	0	0
y9	0	0	0	1	.	.	.	1	0	0


Enter X and Y coordinates from 0 to 9
5 7

XY	x0	x1	x2	x3	x4	x5	x6	x7	x8	x9	
y0	.	1	0	0	0	0	0	1	.	.
y1	.	1	1	1	1	0	0	1	.	.
y2	.	.	.	.	2	0	0	1	1	1
y3	.	.	.	.	3	0	0	0	0	0
y4	.	.	.	.	2	0	0	0	0	0
y5	.	.	.	.	2	0	0	0	0	0
y6	.	.	.	.	1	1	1	1	0	0
y7	1	1	1	2	2	[3]	.	2	0	0
y8	0	0	0	1	.	.	.	2	0	0
y9	0	0	0	1	.	.	.	1	0	0


Enter X and Y coordinates from 0 to 9
6 7

XY	x0	x1	x2	x3	x4	x5	x6	x7	x8	x9	
y0	1	1	0	0	0	0	0	1	1	1
y1	*	1	1	1	1	0	0	1	*	1
y2	1	1	2	*	2	0	0	1	1	1
y3	0	0	3	*	3	0	0	0	0	0
y4	0	0	2	*	2	0	0	0	0	0
y5	1	1	2	2	2	0	0	0	0	0
y6	*	1	1	*	1	1	1	1	0	0
y7	1	1	1	2	2	3	[*]	2	0	0
y8	0	0	0	1	*	3	*	2	0	0
y9	0	0	0	1	1	2	1	1	0	0
Game Over! Black Hole has been opened ...

```
