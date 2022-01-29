all: main

main: dungeon.o
	g++ -g -Wall dungeon.o -o main -lncurses

dungeon.o: dungeon.cpp
	g++ -g -Wall dungeon.cpp -c

clean:
	rm -f main *.o room.h~ Makefile~ dungeon.cpp~ monster.h~ pc.h~
