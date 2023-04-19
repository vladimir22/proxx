# proxx
Java implementation of  https://proxx.app game

## Usage example

### - Clone and build project:
```sh
git clone https://github.com/vladimir22/proxx.git
cd proxx
mvn clean install 
```

### - Play game:
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
