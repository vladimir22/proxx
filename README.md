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
