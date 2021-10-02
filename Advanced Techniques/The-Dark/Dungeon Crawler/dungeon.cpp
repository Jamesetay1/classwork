#include <cstdio>
#include <stdlib.h> 
#include <time.h>
#include <unistd.h>
#include <string.h>
#include <endian.h>
#include <limits.h>
#include <ncurses.h>
#include <fstream>
#include <string>
#include <vector>

using namespace std;

class Monster {

 public:
  int canTunnel;
  char lastPos;
  int xpos;
  int ypos;
  int priority;
  int score;

  string name;
  string desc;
  char symb;
  string color;
  int speed;
  string abil;
  int hp;
  string dam;
  int rrty;

  Monster();
  Monster(string n, string de, char s, string c, int sp, string a, int h, string da, int r); 
  ~Monster() {};
};



class Object {

 public:
  char lastPos;
  int xpos;
  int ypos;

  string name;
  string desc;
  string type;
  char symb;
  string color;
  string dam;
  int hit;
  int dodge;
  int def;
  int weight;
  int speed;
  int attr;
  int val;
  int art;
  int rrty;

  Object();
  Object(string n, string d, string t, char s, string c, string da,
	 int h, int dod, int de, int w, int sp, int at, int v, int ar, int r); 
  ~Object() {};
};

class PC {
  
 public:
  int xpos;
  int ypos;
  char lastPos;
  int speed;
  int priority;
  int score;
  bool hasMoved;
  vector<Object> items;
};


class Room {

 public:
  int xsize;
  int ysize;
  int xpos;
  int ypos;
};

#define DUNGEON_X              80
#define DUNGEON_Y              21
#define ROOMS                  5
#define MONSTERS               10
#define OBJECTS                10

/**
 *  Global Variables
 */
char dungeon[DUNGEON_Y][DUNGEON_X];
int HMap[DUNGEON_Y][DUNGEON_X];
Room rooms[ROOMS];
PC pc;
int canTunnelPath[DUNGEON_Y][DUNGEON_X];
int cantTunnelPath[DUNGEON_Y][DUNGEON_X];
bool gameOver;
bool quit;
bool displayMonsterList;
int moveScroll;
char fogMap[DUNGEON_Y][DUNGEON_X];
bool displayFogMap = true;
bool teleporting;
vector<Monster> foundM;
vector<Monster> monsters;
vector<Object> objF;
vector<Object> objects;
vector<int> scores;


void movePC(int x, int y);
void printMonsterList(int move);
void printToTerminal(char board[DUNGEON_Y][DUNGEON_X]);
void toggleFog();
void updateFogMap();
void deleteNonVisibleCharacters();
void teleportPC();
void teleportRandomly();

void selM();
void initMonsters();
void moveMonster(unsigned order);
int rollerReturn(int b, int d, int s);
int rollDice(string poss);
short getColor(char s, int x, int y);
char getObjSymb(string line);
void selObj();
void initObj();
char getItem(char s);
void startScr();
int main(int argc, char * argv[]);

/**
 *  Generates the border around the dungeon board and fills
 *  the rest with spaces (' ')
 */
void initDungeon(char board[DUNGEON_Y][DUNGEON_X])
{
  int i, j;
  for (i = 0; i < DUNGEON_Y; i++) {
    for (j = 0; j < DUNGEON_X; j++) {
      
      if (i == 0 || i == DUNGEON_Y - 1)
	board[i][j] = '-';
      else if (j == 0 || j == DUNGEON_X - 1)
	board[i][j] = '|';
      else
	board[i][j] = ' ';
    }
  }
}

/**
 *  Swaps one room with another. Used for the shuffle function
 */
void swap(Room *x, Room *y)
{
  Room tmp;
  tmp = *x;
  *x = *y;
  *y = tmp;
}

/**
 *  Shuffles the rooms around
 */
void shuffle(Room rooms[ROOMS])
{
  swap(&rooms[0], &rooms[3]);
  swap(&rooms[1], &rooms[0]);
  swap(&rooms[2], &rooms[4]);
}

/**
 *  This function generates the rooms and corridors. After generating them,
 *  it puts them onto the dungeon board. It also randomly places a PC on
 *  a room or corridor.
 */
void genDungeon(char board[DUNGEON_Y][DUNGEON_X])
{ 
  //Randomly generates the room positions and sizes
  int i, lastx;
  rooms[0].xpos = (rand() % 5) + 2; //2-6
  for (i = 0; i < ROOMS; i++) {
    
    if (i != 0) {
      lastx = rooms[i-1].xpos + rooms[i-1].xsize;
      rooms[i].xpos = lastx + (rand() % 5) + 4; //Lastx + 4-8
    }    
    rooms[i].ypos = (rand() % (DUNGEON_Y/2-2)) + 2; //2-10
    rooms[i].xsize = (rand() % 4) + 5; //5-8
    rooms[i].ysize = (rand() % 6) + 4; //4-9
  }
  
  shuffle(rooms);
  
  //Puts the rooms on the board
  int row, col;
  for (i = 0; i < ROOMS; i++)
    for (row = rooms[i].ypos; row < rooms[i].ypos + rooms[i].ysize; row++)
      for (col = rooms[i].xpos; col < rooms[i].xpos + rooms[i].xsize; col++) 
	board[row][col] = '.';

  //Generates the corridors
  int curx, cury, nextx, nexty;
  for (i = 0; i < ROOMS-1; i++) {
    curx = rooms[i].xpos;
    cury = rooms[i].ypos;
    nextx = rooms[i+1].xpos;
    nexty = rooms[i+1].ypos;

    int j, xdir, ydir;
    if (abs(nextx - curx)) {

      xdir = (nextx-curx)/abs(nextx-curx);

      for (j = curx; j != nextx; j += xdir) 
	if (board[cury][j] != '.')
	  board[cury][j] = '#';
    }
    if (abs(nexty - cury)) {

      ydir = (nexty-cury)/abs(nexty-cury);

      for (j = cury; j != nexty; j += ydir)
	if (board[j][nextx] != '.')
	  board[j][nextx] = '#';
    }
  }
}

/**
 *  Generates the hardness for the dungeon board
 */
void genHardness(int hardness[DUNGEON_Y][DUNGEON_X])
{
  int i, j;
  for (i = 0; i < DUNGEON_Y; i++) {
    for (j = 0; j < DUNGEON_X; j++) {
      
      if (i == 0 || i == DUNGEON_Y-1 || j == 0 || j == DUNGEON_X-1)
	hardness[i][j] = 255;
      else if (dungeon[i][j] == '.' || dungeon[i][j] == '#')
	hardness[i][j] = 0;
      else
	hardness[i][j] = (rand() % 254) + 1;
    }
  }
}

/**
 *  Prints the board to the terminal
 */
void printDungeon(char board[DUNGEON_Y][DUNGEON_X])
{
  int i, j;
  for (i = 0; i < DUNGEON_Y; i++) {
    for (j = 0; j < DUNGEON_X; j++) {
      printf("%c", board[i][j]);
    }
    printf("\n");
  }

  if (gameOver)
    printf("RIP PC. You were brutally killed in %d turns. Game over :(\n", pc.score / 100 - 1);
}

/**
 *  Prints the hardness to the terminal. Used for testing purposes
 */
void printHardness(int hardness[DUNGEON_Y][DUNGEON_X])
{
  int i, j;
  for (i = 0; i < DUNGEON_Y; i++) {
    for (j = 0; j < DUNGEON_X; j++) {
      printf("%d", hardness[i][j] % 10);
    }
    printf("\n");
  }
}

/**
 *  Loads the state of the dungeon and hardness from a file
 */
void load_state(char board[DUNGEON_Y][DUNGEON_X], int hardness[DUNGEON_Y][DUNGEON_X])
{
  FILE *f;
  char *path;
  char *home = getenv("HOME");
  char semantic[12];
  int version, size, length,*s, *v, i, j;

  //Gets the address and reads the file
  s = &size;
  v = &version;
  length = strlen(home) + strlen("/.rlg327/dungeon") + 1;
  path = (char*) malloc(length);
  strcpy(path, home);
  strcat(path, "/.rlg327/dungeon");
  f = fopen(path, "r");
  free(path);

  //Read in semantic, version, and size of file
  fread(&semantic, 12, 1, f);
  fread(v, 4, 1, f);
  version = be32toh(version);
  fread(s, 4, 1, f);
  size = be32toh(size);
  int numRooms = (*s-1700) / 4;

  //Read in the hardness. Makes rooms and corridors a '#'
  for (i = 0; i < DUNGEON_Y; i++) {
    for (j = 0; j < DUNGEON_X; j++) {
      
      fread(&hardness[i][j], 1, 1, f);
      
      if ((hardness[i][j] == 255 && i == 0) || (hardness[i][j] == 255 && i == DUNGEON_Y - 1))
	board[i][j] = '-';
      else if ((hardness[i][j] == 255 && j == 0) || (hardness[i][j] == 255 && j == DUNGEON_X - 1))
	board[i][j] = '|';
      else if (hardness[i][j] == 0)
	board[i][j] = '#';
      else
	board[i][j] = ' ';
    }
  }

  //Read in the rooms
  for (i = 0; i < numRooms; i++) {
    fread(&rooms[i].xpos, 1, 1, f);
    fread(&rooms[i].ypos, 1, 1, f);
    fread(&rooms[i].xsize, 1, 1, f);
    fread(&rooms[i].ysize, 1, 1, f);
  }

  //Change '#' to '.' for the room locations
  int row, col;
  for (i = 0; i < numRooms; i++) 
    for (row = rooms[i].ypos; row < rooms[i].ypos + rooms[i].ysize; row++) 
      for (col = rooms[i].xpos; col < rooms[i].xpos + rooms[i].xsize; col++) 
	board[row][col] = '.';

  fclose(f);
}

/**
 *  This method saves the state of the dungeon and the hardness map associated with it.
 *  It will write the necessary data to a file
 */
void save_state(char board[DUNGEON_Y][DUNGEON_X], int hardness[DUNGEON_Y][DUNGEON_X])
{
  FILE *f;

  //Gets the directory address for save file
  char *home = getenv("HOME");
  int length = strlen(home) + strlen("/.rlg327/dungeon") + 1;
  char *path = (char*) malloc(length);
  strcpy(path, home);
  strcat(path, "/.rlg327/dungeon");
  printf("Saved to path: %s\n", path);

  //Open the file
  f = fopen(path, "w");
  free(path);

  char semantic[] = "RLG327-s2018";
  fwrite(semantic, 12, 1, f);
  
  //Gets the version
  int version = 0;
  int be;
  be = htobe32(version);
  fwrite(&be, sizeof(int), 1, f);

  //Saves the size of the file
  int size = 12 + 4 + 4 + DUNGEON_X*DUNGEON_Y + 4*ROOMS;
  be = htobe32(size);
  fwrite(&be, sizeof(int), 1, f);

  //Saves the hardness
  int i, j;
  for (i = 0; i < DUNGEON_Y; i++)
    for (j = 0; j < DUNGEON_X; j++)
      fwrite(&hardness[i][j], 1, 1, f);

  //Saves the dungeon rooms
  for (i = 0; i < ROOMS; i++) {  
    fwrite(&rooms[i].xpos, 1, 1, f);
    fwrite(&rooms[i].ypos, 1, 1, f);
    fwrite(&rooms[i].xsize, 1, 1, f);
    fwrite(&rooms[i].ysize, 1, 1, f);
  }
  
  fclose(f);
}

/**
 *  Generates the PC randomly into a room on the dungeon.
 *  Uses recursion to find a suitable spot. Sets the PC's x, y,, speed, score, and priority.
 */
void genPC(char board[DUNGEON_Y][DUNGEON_X])
{
  int pc_x, pc_y;

  pc_x = rand() % DUNGEON_X;
  pc_y = rand() % DUNGEON_Y;
  
  if (board[pc_y][pc_x] == '.') {
    pc.lastPos = board[pc_y][pc_x];
    pc.xpos = pc_x;
    pc.ypos = pc_y;
    pc.speed = 10;
    pc.score = 1000/pc.speed;
    pc.priority = 1000/pc.speed;
    board[pc_y][pc_x] = '@';
    HMap[pc_y][pc_x] = 0;
    return;
  }
  else
    genPC(board);
}

/**
 *  Generates recursively the path for monsters that can tunnel
 */
void recCanTunnelPath(int y, int x, int val, int hardness[DUNGEON_Y][DUNGEON_X])
{
  
  if (canTunnelPath[y+1][x] > val + (hardness[y+1][x]/85)+1 && hardness[y+1][x] != 255) {
    canTunnelPath[y+1][x] = val + (hardness[y+1][x]/85)+1;
    recCanTunnelPath(y+1, x, canTunnelPath[y+1][x], hardness);
  }
  if (canTunnelPath[y-1][x] > val + (hardness[y-1][x]/85)+1 && hardness[y-1][x] != 255) {
    canTunnelPath[y-1][x] = val + (hardness[y-1][x]/85)+1;
    recCanTunnelPath(y-1, x, canTunnelPath[y-1][x], hardness);
  }
  if (canTunnelPath[y][x+1] > val + (hardness[y][x+1]/85)+1 && hardness[y][x+1] != 255) {
    canTunnelPath[y][x+1] = val + (hardness[y][x+1]/85)+1;
    recCanTunnelPath(y, x+1, canTunnelPath[y][x+1], hardness);
  }
  if (canTunnelPath[y][x-1] > val + (hardness[y][x-1]/85)+1 && hardness[y][x-1] != 255) {
    canTunnelPath[y][x-1] = val + (hardness[y][x-1]/85)+1;
    recCanTunnelPath(y, x-1, canTunnelPath[y][x-1], hardness);
  }
  if (canTunnelPath[y+1][x+1] > val + (hardness[y+1][x+1]/85)+1 && hardness[y+1][x+1] != 255) {
    canTunnelPath[y+1][x+1] = val + (hardness[y+1][x+1]/85)+1;
    recCanTunnelPath(y+1, x+1, canTunnelPath[y+1][x+1], hardness);
  }
  if (canTunnelPath[y+1][x-1] > val + (hardness[y+1][x-1]/85)+1 && hardness[y+1][x-1] != 255) {
    canTunnelPath[y+1][x-1] = val + (hardness[y+1][x-1]/85)+1;
    recCanTunnelPath(y+1, x-1, canTunnelPath[y+1][x-1], hardness);
  }
  if (canTunnelPath[y-1][x+1] > val + (hardness[y-1][x+1]/85)+1 && hardness[y-1][x+1] != 255) {
    canTunnelPath[y-1][x+1] = val + (hardness[y-1][x+1]/85)+1;
    recCanTunnelPath(y-1, x+1, canTunnelPath[y-1][x+1], hardness);
  }
  if (canTunnelPath[y-1][x-1] > val + (hardness[y-1][x-1]/85)+1 && hardness[y-1][x-1] != 255) {
    canTunnelPath[y-1][x-1] = val + (hardness[y-1][x-1]/85)+1;
    recCanTunnelPath(y-1, x-1, canTunnelPath[y-1][x-1], hardness);
  }
}

/**
 *  Initializes the path for monsters that can tunnel. Calls the recursive function above.
 */
void genCanTunnelPath()
{
  int i, j;
  for (i = 0; i < DUNGEON_Y; i++) {
    for (j = 0; j < DUNGEON_X; j++) {
      canTunnelPath[i][j] = INT_MAX;
      if (HMap[i][j] == 255)
	canTunnelPath[i][j] = ' ';
    }
  }

  recCanTunnelPath(pc.ypos, pc.xpos, 0, HMap);
  canTunnelPath[pc.ypos][pc.xpos] = '@';
}

/**
 *  Prints the path for monsters that can tunnel
 */
void printCanTunnelPath()
{
  int i, j;
  for (i = 0; i < DUNGEON_Y; i++) {
    for (j = 0; j < DUNGEON_X; j++) {
      if (HMap[i][j] == 255 || (pc.ypos == i && pc.xpos == j))
	printf("%c", canTunnelPath[i][j]);
      else
	printf("%d", canTunnelPath[i][j] % 10);
    }
    printf("\n");
  }
}

/**
 *  Generates recursively the path for monsters that can't tunnel
 */
void recCantTunnelPath(int y, int x, int val, int hardness[DUNGEON_Y][DUNGEON_X])
{
  if (cantTunnelPath[y+1][x] > val+1 && hardness[y+1][x] == 0) {
    cantTunnelPath[y+1][x] = val+1;
    recCantTunnelPath(y+1, x, val+1, hardness);
  }
  if (cantTunnelPath[y-1][x] > val+1 && hardness[y-1][x] == 0) {
    cantTunnelPath[y-1][x] = val+1;
    recCantTunnelPath(y-1, x, val+1, hardness);
  }
  if (cantTunnelPath[y][x+1] > val+1 && hardness[y][x+1] == 0) {
    cantTunnelPath[y][x+1] = val+1;
    recCantTunnelPath(y, x+1, val+1, hardness);
  }
  if (cantTunnelPath[y][x-1] > val+1 && hardness[y][x-1] == 0) {
    cantTunnelPath[y][x-1] = val+1;
    recCantTunnelPath(y, x-1, val+1, hardness);
  }
  if (cantTunnelPath[y+1][x+1] > val+1 && hardness[y+1][x+1] == 0) {
    cantTunnelPath[y+1][x+1] = val+1;
    recCantTunnelPath(y+1, x+1, val+1, hardness);
  }
  if (cantTunnelPath[y+1][x-1] > val+1 && hardness[y+1][x-1] == 0) {
    cantTunnelPath[y+1][x-1] = val+1;
    recCantTunnelPath(y+1, x-1, val+1, hardness);
  }
  if (cantTunnelPath[y-1][x+1] > val+1 && hardness[y-1][x+1] == 0) {
    cantTunnelPath[y-1][x+1] = val+1;
    recCantTunnelPath(y-1, x+1, val+1, hardness);
  }
  if (cantTunnelPath[y-1][x-1] > val+1 && hardness[y-1][x-1] == 0) {
    cantTunnelPath[y-1][x-1] = val+1;
    recCantTunnelPath(y-1, x-1, val+1, hardness);
  }
}

/**
 *  Initializes the path for monsters that can't tunnel. Calls the recursive function above.
 */
void genCantTunnelPath()
{
  int i, j;
  for (i = 0; i < DUNGEON_Y; i++) {
    for (j = 0; j < DUNGEON_X; j++) {
      cantTunnelPath[i][j] = -1;
      if (HMap[i][j] == 0)
	cantTunnelPath[i][j] = INT_MAX;
    }
  }

  recCantTunnelPath(pc.ypos, pc.xpos, 0, HMap);
  cantTunnelPath[pc.ypos][pc.xpos] = '@';
}

/**
 *  Prints the path for monsters that can't tunnel
 */
void printCantTunnelPath()
{
  int i, j;
  for (i = 0; i < DUNGEON_Y; i++) {
    for (j = 0; j < DUNGEON_X; j++) {
      if (cantTunnelPath[i][j] == -1) {
	cantTunnelPath[i][j] = ' ';
	printf("%c", cantTunnelPath[i][j]);
      }
      else if (cantTunnelPath[i][j] == '@')
	printf("%c", cantTunnelPath[i][j]);
      else
	printf("%d", cantTunnelPath[i][j] % 10);
    }
    printf("\n");
  }
}

/**
 *  This function makes it so monsters do NOT spawn in the same room as the PC. mon_x and mon_y are the
 *  monster's x and y coordinates and they get checked if they are in the same room as the PC.
 */
bool isValidRoom(int mon_x, int mon_y)
{
  int i, x, y, pcRoom;

  x = pc.xpos;
  y = pc.ypos;

  for (i = 0; i < ROOMS; i++) {
    if (x >= rooms[i].xpos && x < rooms[i].xpos + rooms[i].xsize 
	&& y >= rooms[i].ypos && y < rooms[i].ypos + rooms[i].ysize) {
      pcRoom = i;
    }
  }

  if (mon_x >= rooms[pcRoom].xpos && mon_x <= rooms[pcRoom].xpos + rooms[pcRoom].xsize
      && mon_y >= rooms[pcRoom].ypos && mon_y <= rooms[pcRoom].ypos + rooms[pcRoom].ysize) {
    return false;
  }
  return true;
}

/**
 *  Initializes the score which determines who should go next whether it's a monster or the PC.
 */
void initScore()
{
  unsigned i;
  scores.erase(scores.begin(), scores.end());
  for (i = 0; i < monsters.size(); i++) {
    scores.push_back(monsters.at(i).priority);
  }
  scores.push_back(pc.priority);
}



/**
 *  Generates count/2 '>' and count/2 '<' randomly in a room or corridor.
 */
void genStairs(int count)
{
  int stairs_x, stairs_y;

  if (count < 1)
    return;

  stairs_x = rand() % DUNGEON_X;
  stairs_y = rand() % DUNGEON_Y;
  
  if (dungeon[stairs_y][stairs_x] == '.' || dungeon[stairs_y][stairs_x] == '#') {
    if (count % 2 == 0)
      dungeon[stairs_y][stairs_x] = '>';
    else
      dungeon[stairs_y][stairs_x] = '<';
    
    genStairs(count-1);
  }
  else
    genStairs(count);
}

/**
 *  Initializes a new dungeon when the PC steps on a stair represented by '>' or '<'
 */
void initStairDungeon()
{
  initDungeon(dungeon);
  genDungeon(dungeon);
  genHardness(HMap);
  genPC(dungeon);
  genStairs(4);
  genCantTunnelPath();
  genCanTunnelPath();
  selM();
  initMonsters();
  selObj();
  initObj();
  initScore();
  initDungeon(fogMap);
}

/**
 *  Handles input coming from the keyboard. Recursively calling keyboardListener() will prevent
 *  making the PC use up a turn
 */
void keyboardListener()
{
  switch(getch())
    {
    case '1':              //Lower Left
      movePC(pc.xpos - 1, pc.ypos + 1);
      break;
    case '2':              //Down
      movePC(pc.xpos, pc.ypos + 1);
      break;
    case '3':              //Lower Right
      movePC(pc.xpos + 1, pc.ypos + 1);
      break; 
    case '4':              //Left
      movePC(pc.xpos - 1, pc.ypos);
      break;
    case '5':              //Center
      movePC(pc.xpos, pc.ypos);
      break;
    case '6':              //Right
      movePC(pc.xpos + 1, pc.ypos);
      break;
    case '7':              //Upper Left
      movePC(pc.xpos - 1, pc.ypos - 1);
      break;
    case '8':              //Up
      movePC(pc.xpos, pc.ypos - 1);
      break;
    case '9':              //Upper Right
      movePC(pc.xpos + 1, pc.ypos - 1);
      break;
    case 'Q':              //Quit
      quit = true;
      break;
    case 'm':              //Display monster list
      moveScroll = 0;
      displayMonsterList = true;
      printMonsterList(moveScroll);
      break;
    case 27:               //Don't display monster list
      displayMonsterList = false;
      if (!displayFogMap)
	printToTerminal(dungeon);
      else 
	printToTerminal(fogMap);
      break;
    case KEY_UP:           //Scroll up
      if (moveScroll < 0)
	moveScroll++;
      printMonsterList(moveScroll);
      break;
    case KEY_DOWN:         //Scroll down
      if (moveScroll > -1 * ((int)monsters.size() - DUNGEON_Y - ((int)monsters.size()-DUNGEON_Y-76)))
	moveScroll--;
      printMonsterList(moveScroll);
      break;
    case 'f':              //Toggle fog of war
      toggleFog();
      break;
    case 't':              //Teleport PC and choose location
      teleportPC();
      break;
    case 'r':              //Teleport randomly if teleport is true
      teleportRandomly();
      break;
    case 92:
      if (pc.lastPos == 92) {
	getItem(92);
      }
      break;
    case '|':              //Pick up Objects
      if (pc.lastPos == '|') {
	getItem('|');
      }
      break;
    case ')':
      if (pc.lastPos == ')') {
	getItem(')');
      }
      break;
    case '}':
      if (pc.lastPos == '}') {
	getItem('}');
      }
      break;
    case '[':
      if (pc.lastPos == '[') {
	getItem('[');
      }
      break;
    case ']':
      if (pc.lastPos == ']') {
	getItem(']');
      }
      break;
    case '(':
      if (pc.lastPos == '(') {
	getItem('(');
      }
      break;
    case '{':
      if (pc.lastPos == '{') {
	getItem('{');
      }
      break;
    case '=':
      if (pc.lastPos == '=') {
	getItem('=');
      }
      break;
    case '"':
      if (pc.lastPos == '"') {
	getItem('"');
      }
      break;
    case '_':
      if (pc.lastPos == '_') {
	getItem('_');
      }
      break;
    case '~':
      if (pc.lastPos == '~') {
	getItem('~');
      }
      break;
    case '?':
      if (pc.lastPos == '?') {
	getItem('?');
      }
      break;
    case '!':
      if (pc.lastPos == '!') {
	getItem('!');
      }
      break;
    case '$':
      if (pc.lastPos == '$') {
	getItem('$');
      }
      break;
    case '/':
      if (pc.lastPos == '/') {
	getItem('/');
      }
      break;
    case ',':
      if (pc.lastPos == ',') {
	getItem(',');
      }
      break;
    case '-':
      if (pc.lastPos == '-') {
	getItem('-');
      }
      break;
    case '%':
      if (pc.lastPos == '%') {
	getItem('%');
      }
      break;
    default:               //Don't do anything if incorrect key was pressed
      break;
    } 
}

/**
 *  Moves the PC according to the keyboardListener() function
 */
void movePC(int x, int y)
{
  char tempChar;
  unsigned i;

  if (!displayMonsterList) {
    if (teleporting && HMap[y][x] != 255) {
      tempChar = dungeon[y][x];
      if (tempChar=='*')
	tempChar = pc.lastPos;
      dungeon[pc.ypos][pc.xpos] = pc.lastPos;
      pc.xpos = x;
      pc.ypos = y;
      dungeon[pc.ypos][pc.xpos] = '*';
      pc.lastPos = tempChar;
      printToTerminal(dungeon);
    }
    else if (dungeon[y][x] == '>' || dungeon[y][x] == '<' || pc.lastPos == '>' || pc.lastPos == '<') {
      initStairDungeon();
      pc.hasMoved = true;
    }
    else if (dungeon[y][x] == '.' || dungeon[y][x] == '#' || dungeon[y][x] == '@') {
      tempChar = dungeon[y][x];
      if (tempChar=='@')
	tempChar = pc.lastPos;
      dungeon[pc.ypos][pc.xpos] = pc.lastPos;
      pc.xpos = x;
      pc.ypos = y;
      dungeon[y][x] = '@';
      pc.lastPos = tempChar;
      pc.hasMoved = true;
    }
    else {
      for (i = 0; i < monsters.size(); i++) {
	if (y == monsters.at(i).ypos && x == monsters.at(i).xpos && !teleporting) {
	  dungeon[y][x] = monsters.at(i).symb;
	  dungeon[pc.ypos][pc.xpos] = pc.lastPos;
	  gameOver = true;
	  pc.hasMoved = true;
	}
      }
      for (i = 0; i < objects.size(); i++) {
	if (dungeon[y][x] == objects.at(i).symb) {
	  tempChar = dungeon[y][x];
	  if (tempChar=='@')
	    tempChar = pc.lastPos;
	  dungeon[pc.ypos][pc.xpos] = pc.lastPos;
	  pc.xpos = x;
	  pc.ypos = y;
	  dungeon[y][x] = '@';
	  pc.lastPos = tempChar;
	  pc.hasMoved = true;
	}
      }
    }
  }
}

/**
 *  Generates and prints the monster positions relative to the PC in ncurses terminal.
 *  The parameter 'move' is used for scrolling the list up and down.
 */
void printMonsterList(int move)
{
  int mon_x, mon_y, print_x, print_y;
  unsigned i;

  if (displayMonsterList) {
    
    //Clear out the dungeon
    wmove(stdscr, 0, 0);
    clrtoeol();
    for (i = 0; i < DUNGEON_Y; i++) {
      wmove(stdscr, i + 1, 0);
      clrtoeol();
    }

    //Display the monster list
    mvprintw(0, 0, "Displaying Monsters List");
    for (i = 0; i < monsters.size(); i++) {
      mon_x = monsters.at(i).xpos;
      mon_y = monsters.at(i).ypos;
      print_x = abs(mon_x - pc.xpos);
      print_y = abs(mon_y - pc.ypos);

      mvprintw(i + 1 + move, 0, "%3d. %c:", i+1,  monsters.at(i).symb);
      if (mon_y - pc.ypos <= 0) {
	mvprintw(i + 1 + move, 7, "%3d north", print_y);
      }
      else if (mon_y - pc.ypos > 0) {
	mvprintw(i + 1 + move, 7, "%3d south", print_y);
      }
      if (mon_x - pc.xpos <= 0) {
	mvprintw(i + 1 + move, 16, ", %2d west ", print_x);
      }
      else if (mon_x - pc.xpos > 0) {
	mvprintw(i + 1 + move, 16, ", %2d east ", print_x);
      }
    }
  }
  refresh();
}

/**
 *  Prints the terminal using ncurses. Also handles displaying game over
 */
void printToTerminal(char board[DUNGEON_Y][DUNGEON_X])
{
  unsigned i, j;
  short cFind;

  if (!displayMonsterList) {
    
    //Clear out monster list
    wmove(stdscr, 0, 0);
    clrtoeol();
    for (i = 0; i < 40; i++) {
      wmove(stdscr, i + 1, 0);
      clrtoeol();
    }

    //Display the board
    if (teleporting)
      mvprintw(0, 0, "Teleporting");
    else if (displayFogMap)
      mvprintw(0, 0, "Displaying Fog of War");
    else
      mvprintw(0, 0, "Displaying Dungeon");

    for (i = 0; i < DUNGEON_Y; i++) {
      for (j = 0; j < DUNGEON_X; j++) {
	//PC
	if (board[i][j] == '@' || board[i][j] == '*') {
	  init_pair(COLOR_CYAN, COLOR_CYAN, COLOR_BLACK);
	  attron(COLOR_PAIR(COLOR_CYAN));
	  mvaddch(i + 1, j, board[i][j]);
	  attroff(COLOR_PAIR(COLOR_CYAN));
	}
	//Monster or object
	else if (board[i][j] != '.' && board[i][j] != '#' && board[i][j] != ' ' && 
		 i != 0 && i != DUNGEON_Y-1 && j != 0 && j != DUNGEON_X-1 && 
		 board[i][j] != '>' && board[i][j] != '<') {
	  cFind = getColor(board[i][j], j, i);
	  init_pair(cFind, cFind, COLOR_BLACK);
	  attron(COLOR_PAIR(cFind));
	  mvaddch(i + 1, j, board[i][j]);
	  attroff(COLOR_PAIR(cFind));
	}
	//Rest of the board
	else
	  mvaddch(i + 1, j, board[i][j]);
      }
    }
    if (gameOver) {
      wmove(stdscr, 0, 0);
      clrtoeol();
      mvprintw(0, 0, "YOU ARE DEAD. DEAD. DEAD. DEAD.");
      refresh();
      sleep(3);
    }
  }
}

/**
 *  Toggles to display the fogMap of the Dungeon
 */
void toggleFog()
{
  if (displayFogMap) {
    displayFogMap = false;
    printToTerminal(dungeon);
  }
  else {
    displayFogMap = true;
    printToTerminal(fogMap);
  }
}

/**
 *  Updates the fogMap whenever the PC moves
 */
void updateFogMap()
{
  int i, j;
  for (i = pc.ypos-2; i <= pc.ypos+2; i++) {
    for (j = pc.xpos-2; j <= pc.xpos+2; j++) {
      fogMap[i][j] = dungeon[i][j];
    }
  }

  deleteNonVisibleCharacters();
}

/**
 *  Deletes characters that are not within view of the PC for fogMap
 */
void deleteNonVisibleCharacters()
{
  unsigned i, j, k;
  for (i = 0; i < DUNGEON_Y; i++) {
    for (j = 0; j < DUNGEON_X; j++) {
      for (k = 0; k < monsters.size(); k++) {
	if ((fogMap[i][j] == monsters.at(k).symb || fogMap[i][j] == '@') && 
	    fogMap[i][j] != dungeon[i][j]) {
	  fogMap[i][j] = dungeon[i][j];
	}
      }
    }
  }

  for (i = 0; i < monsters.size(); i++) {
    if ((monsters.at(i).xpos < pc.xpos-2 || monsters.at(i).xpos > pc.xpos+2 || 
	 monsters.at(i).ypos < pc.ypos-2 || monsters.at(i).ypos > pc.ypos+2) && 
	fogMap[monsters.at(i).ypos][monsters.at(i).xpos] != ' ') {
      fogMap[monsters.at(i).ypos][monsters.at(i).xpos] = monsters.at(i).lastPos;
    }
  }
}

/**
 *  Handles teleporting the PC. When 't' gets pressed the first
 *  time, teleport is set to true. If 't' gets pressed again, teleport becomes false
 */
void teleportPC()
{
  unsigned i;

  if (teleporting) {
    //Teleported on top of a monster
    for (i = 0; i < monsters.size(); i++) {
      if (pc.lastPos == monsters.at(i).symb) {
	gameOver = true;
	dungeon[pc.ypos][pc.xpos] = pc.lastPos;
	return;
      }
    }
    dungeon[pc.ypos][pc.xpos] = '@';
    teleporting = false;
    updateFogMap();
    if(displayFogMap)
      printToTerminal(fogMap);
    else
      printToTerminal(dungeon);
  }
  else {
    teleporting = true;
    dungeon[pc.ypos][pc.xpos] = '*';
    printToTerminal(dungeon);
  }
}

/**
 *  Handles randomly teleporting the PC. Only works if teleport
 *  is true
 */
void teleportRandomly()
{
  unsigned i;

  if (teleporting) {
    dungeon[pc.ypos][pc.xpos] = pc.lastPos;
    pc.xpos = (rand() % (DUNGEON_X-2)) + 1; //1-79
    pc.ypos = (rand() % (DUNGEON_Y-2)) + 1; //1-20
    pc.lastPos = dungeon[pc.ypos][pc.xpos];
    //Teleported on top of a monster
    for (i = 0; i < monsters.size(); i++) {
      if (pc.lastPos == monsters.at(i).symb) {
	gameOver = true;
	dungeon[pc.ypos][pc.xpos] = pc.lastPos;
	return;
      }
    }
    dungeon[pc.ypos][pc.xpos] = '@';
    teleporting = false;
    updateFogMap();

    if(displayFogMap)
      printToTerminal(fogMap);
    else
      printToTerminal(dungeon);
  }
}

//Puts Monsters on the board.
void initMonsters()
{
  int  mon_x, mon_y;
  bool ok;
  unsigned i;

  for (i = 0; i < monsters.size(); i++) {
    
    monsters.at(i).canTunnel = monsters.at(i).abil.find("TUNNEL") != string::npos;
    monsters.at(i).score = 1000/monsters.at(i).speed;
    monsters.at(i).priority = 1000/monsters.at(i).speed;
    
    ok = false;
    while (!ok) {
      mon_x = rand() % DUNGEON_X;
      mon_y = rand() % DUNGEON_Y;
  
      if (dungeon[mon_y][mon_x] == '.' && isValidRoom(mon_x, mon_y)) {
	monsters.at(i).lastPos = dungeon[mon_y][mon_x];
	monsters.at(i).xpos = mon_x;
	monsters.at(i).ypos = mon_y;
	dungeon[mon_y][mon_x] = monsters.at(i).symb;
	ok = true;
      }
    }
  }
}
/**
 *  Moves a monster closer to the PC and does so in a random fashion. The parameter 'order' is used to
 *  define which monster to move in the array i.e. monsters[order].
 */
void moveMonster(unsigned order)
{
  int x, y, lastPos, newPos, newX, newY;
  bool ok;
  char tempChar;
  unsigned i;

  //If the monster can tunnel. Move accordingly.
  if (monsters.at(order).canTunnel) {
    x = monsters.at(order).xpos;
    y = monsters.at(order).ypos;
    lastPos = canTunnelPath[y][x];
    
    ok = false;
    while (!ok) {
      newX = x + (rand() % 3) - 1;
      newY = y + (rand() % 3) - 1;
      newPos = canTunnelPath[newY][newX];
      
      for (i = 0; i < monsters.size(); i++) {
	if (newY == monsters.at(i).ypos && newX == monsters.at(i).xpos)
	  return;
      }

      if (lastPos > newPos && HMap[newY][newX] != 255) {
	if (HMap[newY][newX] > 85) {
	  HMap[newY][newX] -= 85;
	  ok = true;
	  continue;
	}
	else
	  HMap[newY][newX] = 0;

	monsters.at(order).xpos = newX;
	monsters.at(order).ypos = newY;
	if (dungeon[newY][newX] == ' ')
	  tempChar = '#';
	else
	  tempChar = dungeon[newY][newX];

	dungeon[newY][newX] = monsters.at(order).symb;
	HMap[newY][newX] = 0;
	dungeon[y][x] = monsters.at(order).lastPos;
	monsters.at(order).lastPos = tempChar;
	ok = true;
      }

      if (newX==pc.xpos && newY==pc.ypos) {
	monsters.at(order).xpos = newX;
	monsters.at(order).ypos = newY;
	dungeon[newY][newX] = monsters.at(order).symb;
	dungeon[y][x] = monsters.at(order).lastPos;
	gameOver = true;
	return;
      }
    }
  }
  //Otherwise the monster can't tunnel. Move accordingly.
  else {
    x = monsters.at(order).xpos;
    y = monsters.at(order).ypos;
    lastPos = cantTunnelPath[y][x];

    ok = false;
    while (!ok) {
      newX = x + (rand() % 3) - 1;
      newY = y + (rand() % 3) - 1;
      newPos = cantTunnelPath[newY][newX];

      for (i = 0; i < monsters.size(); i++) {
	if (newY == monsters.at(i).ypos && newX == monsters.at(i).xpos)
	  return;
      }

      if (lastPos > newPos && (dungeon[newY][newX] == '#' || dungeon[newY][newX] == '.' || 
	  dungeon[newY][newX] == '>' || dungeon[newY][newX] == '<')) {

	monsters.at(order).xpos = newX;
	monsters.at(order).ypos = newY;
	tempChar = dungeon[newY][newX];
	dungeon[newY][newX] = monsters.at(order).symb;
	HMap[newY][newX] = 0;
	dungeon[y][x] = monsters.at(order).lastPos;
	monsters.at(order).lastPos = tempChar;
	ok = true;
      }

      if (newX==pc.xpos && newY==pc.ypos) {
	monsters.at(order).xpos = newX;
	monsters.at(order).ypos = newY;
	dungeon[newY][newX] = monsters.at(order).symb;
	dungeon[y][x] = monsters.at(order).lastPos;
	gameOver = true;
	return;
      }
    }
  }
}
/**
 *  Makes a move according to the monster's and PC's score. If the PC moves, the dungeon gets refreshed.
 */
void makeMove()
{
  int turn;
  unsigned i, order;

  //Figure out whose turn it is
  turn = scores.at(0);
  order = 0;
  for (i = 0; i < scores.size()-1; i++) {
    if (turn > scores.at(i+1)) {
      turn = scores.at(i+1);
      order = i+1;
    }
  }
  //Move the player
  if (order == scores.size()-1 || pc.score == 100) {
    
    updateFogMap();
    if(displayFogMap)
      printToTerminal(fogMap);
    else
      printToTerminal(dungeon);

    while (!pc.hasMoved && !quit && !gameOver) {
      keyboardListener();
    }
    pc.hasMoved = false;
    pc.score += pc.priority;
    scores.at(scores.size()-1) = pc.score;

    genCanTunnelPath();
    genCantTunnelPath();
  }
  //Move monster
  else {
    moveMonster(order);
    monsters.at(order).score += monsters.at(order).priority;
    scores.at(order) = monsters.at(order).score;
  }
}

/**
 *  Constructs a monster from parseMonFile()
 */
Monster::Monster(string n, string de, char s, string c, int sp, string a, int h, string da, int r)
{
  name = n;
  desc = de;
  symb = s;
  color = c;
  speed = sp;
  abil = a;
  hp = h;
  dam = da;
  rrty = r;
}

//parses the monster file
void parseMonFile()
{
  string path;
  string line;
  bool beginMonFound, nameFound, symbFound, colorFound, descFound, speedFound, damFound, hpFound,
    rrtyFound, abilFound, endFound, isDesc;
  
  //Monster variables to be used later
  string name, color, desc, speed, dam, hp, abil;
  int hpInt, speedInt, rrty;
  char symb;

  //Get the file
  path = getenv("HOME");
  path.append("/rlg327/monster_desc.txt");
  ifstream infile(path.c_str());
  
  //Make sure it's open
  if (!infile.is_open()) {
    fprintf(stderr, "File in path: %s  was not found. Exited\n", path.c_str());
    return;
  }

  //Exit if the file doesn't begin with statement below
  getline(infile, line);
  if (line.find("RLG327 MONSTER DESCRIPTION 1") == string::npos) {
    fprintf(stderr, "Monster file did not contain \"RLG327 MONSTER DESCRIPTION 1\" on line 1. Exited\n");
    return;
  }

  while (getline(infile, line)) {
    if (line.empty()){}
    else if (line.find("BEGIN MONSTER") != string::npos) {
      beginMonFound = true;
    }
    else if (line.find("NAME") != string::npos) {
      nameFound = true;
      name = line.substr(5);
    }
    else if (line.find("SYMB") != string::npos) {
      symbFound = true;
      symb = line[5];
    }
    else if (line.find("COLOR") != string::npos) {
      colorFound = true;
      color = line.substr(6);
    }
    else if (line.find("SPEED") != string::npos) {
      speedFound = true;
      speed = line.substr(6);
    }
    else if (line.find("DAM") != string::npos) {
      damFound = true;
      dam = line.substr(4);
    }
    else if (line.find("HP") != string::npos) {
      hpFound = true;
      hp = line.substr(3);
    }
    else if (line.find("RRTY") != string::npos) {
      rrtyFound = true;
      rrty = atoi(line.substr(5).c_str());
    }
    else if (line.find("ABIL") != string::npos) {
      abilFound = true;
      abil = line.substr(5);
    }
    else if (isDesc || line.find("DESC") != string::npos) {
      descFound = true;
      if (!strcmp(line.c_str(), ".")) {
	isDesc = false;
      }
      else if (!strcmp(line.c_str(), "DESC")) {
	isDesc = true;
      }
      else {
	desc.append(line);
	desc.push_back('\n');
      }
    }
    else if (line.find("END") != string::npos) {
      endFound = true;

      //Print out monster values if it can be successfully constructed
      if (beginMonFound && nameFound && symbFound && colorFound && descFound && speedFound && damFound &&
	  hpFound && rrtyFound && abilFound && endFound) {
	
	hpInt = rollDice(hp);
	speedInt = rollDice(speed);
	Monster monster(name, desc, symb, color, speedInt, abil, hpInt, dam, rrty);
	foundM.push_back(monster);
      }
      else {
	fprintf(stderr, "The current monster being parsed contained an error or was missing an attribute\n\n");
      }

      //Reset monster parse
      beginMonFound = nameFound = symbFound = colorFound = descFound = speedFound = damFound 
	= hpFound = rrtyFound = abilFound = endFound = isDesc = false;
      desc = "";
    }
  } //end while
  infile.close();
}

//gets information out of item file.
void parseObjFile()
{
  string path;
  string line;
  bool beginObjFound, nameFound, descFound, typeFound, colorFound, hitFound, damFound, dodgeFound,
    defFound, weightFound, speedFound, attrFound, valFound, artFound, rrtyFound, endFound, isDesc;
  
  //Object variables to be used later
  string name, desc, type, color, hit, dam, dodge, def, weight, speed, attr, val, art;
  int speedInt, hitInt, dodgeInt, weightInt, defInt, attrInt, valInt, artInt, rrty;
  char symbChar;

  //Get the file
  path = getenv("HOME");
  path.append("/rlg327/object_desc.txt");
  ifstream infile(path.c_str());

  //Make sure it's open
  if (!infile.is_open()) {
    fprintf(stderr, "File in path: %s  was not found. Exited\n", path.c_str());
    return;
  }

  //Exit if the file doesn't begin with statement below
  getline(infile, line);
  if (line.find("RLG327 OBJECT DESCRIPTION 1") == string::npos) {
    fprintf(stderr, "Object file did not contain \"RLG327 OBJECT DESCRIPTION 1\" on line 1. Exited\n");
    return;
  }

  while (getline(infile, line)) {
    if (line.empty()){}
    else if (line.find("BEGIN OBJECT") != string::npos) {
      beginObjFound = true;
    }
    else if (line.find("NAME") != string::npos) {
      nameFound = true;
      name = line.substr(5);
    }
    else if (line.find("TYPE") != string::npos) {
      typeFound = true;
      type = line.substr(5);
    }
    else if (line.find("COLOR") != string::npos) {
      colorFound = true;
      color = line.substr(6);
    }
    else if (line.find("HIT") != string::npos) {
      hitFound = true;
      hit = line.substr(4);
    }
    else if (line.find("DAM") != string::npos) {
      damFound = true;
      dam = line.substr(4);
    }
    else if (line.find("DODGE") != string::npos) {
      dodgeFound = true;
      dodge = line.substr(6);
    }
    else if (line.find("DEF") != string::npos) {
      defFound = true;
      def = line.substr(4);
    }
    else if (line.find("WEIGHT") != string::npos) {
      weightFound = true;
      weight = line.substr(7);
    }
    else if (line.find("SPEED") != string::npos) {
      speedFound = true;
      speed = line.substr(6);
    }
    else if (line.find("ATTR") != string::npos) {
      attrFound = true;
      attr = line.substr(5);
    }
    else if (line.find("VAL") != string::npos) {
      valFound = true;
      val = line.substr(4);
    }
    else if (line.find("ART") != string::npos) {
      artFound = true;
      art = line.substr(4);
    }
    else if (line.find("RRTY") != string::npos) {
      rrtyFound = true;
      rrty = atoi(line.substr(5).c_str());
    }
    else if (isDesc || line.find("DESC") != string::npos) {
      descFound = true;
      if (!strcmp(line.c_str(), ".")) {
	isDesc = false;
      }
      else if (!strcmp(line.c_str(), "DESC")) {
	isDesc = true;
      }
      else {
	desc.append(line);
	desc.push_back('\n');
      }
    }
    else if (line.find("END") != string::npos) {
      endFound = true;
	
      //Print out object values if it can be successfully constructed
      if (beginObjFound && nameFound && descFound && typeFound && colorFound && hitFound && damFound &&
	  dodgeFound && defFound && weightFound && speedFound && attrFound && valFound && artFound &&
	  rrtyFound && endFound) {
	
	symbChar = getObjSymb(type);
	hitInt = rollDice(hit);
	speedInt = rollDice(speed);
	dodgeInt = rollDice(dodge);
	weightInt = rollDice(weight);
	defInt = rollDice(def);
	attrInt = rollDice(attr);
	valInt = rollDice(val);
	artInt = rollDice(art);

	Object obj(name, desc, type, symbChar, color, dam, hitInt, dodgeInt, defInt, weightInt,
		   speedInt, attrInt, valInt, artInt, rrty);
	objF.push_back(obj);
	
      }
      else {
	fprintf(stderr, "The current object being parsed contained an error or was missing an attribute\n");
      }

      //Reset object parse
      beginObjFound = nameFound = descFound = typeFound = colorFound = hitFound = damFound
	= dodgeFound = defFound = weightFound = speedFound = attrFound = valFound = artFound
	= rrtyFound = endFound = false;
      desc = "";
    }
  } //end while
  infile.close();
}


//takes monsters from parsed vector. rarity makes it more or less likely to be chosen.
void selM()
{
  int rare, num;
  
  while (monsters.size() < MONSTERS && foundM.size() > 0) {
    
    rare = rand() % 100;
    num = rand() % (int)foundM.size();
    
    if (foundM.at(num).rrty>rare) {
      monsters.push_back(foundM.at(num));
      foundM.erase(foundM.begin() + num);
    }
  }
}

//Constructs an object
Object::Object(string nameF, string descF, string typeF, char symbF, string colorF, string damageF,
               int hitF, int dodgeF, int defF, int weightF, int speedF, int attrF, int valF, int artF, int rrtyF)
{
  name = nameF;
  desc = descF;
  symb = symbF;
  type = typeF;
  color = colorF;
  dam = damageF;
  hit = hitF;
  dodge = dodgeF;
  def = defF;
  weight = weightF;
  speed = speedF;
  attr = attrF;
  val = valF;
  art = artF;
  rrty = rrtyF;
}



//finds object and its symbol.
char getObjSymb(string symb)
{
  char ch;

  if (symb.find("WEAPON") != string::npos) {ch = '|';
  }else if (symb.find("BOOTS") != string::npos) {ch = 92;
  }else if (symb.find("OFFHAND") != string::npos) {ch = ')';
  }else if (symb.find("RANGED") != string::npos) {ch = '}';
  }else if (symb.find("ARMOR") != string::npos) {ch = '[';
  }else if (symb.find("HELMET") != string::npos) {ch = ']';
  }else if (symb.find("CLOAK") != string::npos) {ch  = '(';
  }else if (symb.find("GLOVES") != string::npos) {ch = '{';
  }else if (symb.find("RING") != string::npos) {ch = '=';
  }else if (symb.find("AMULET") != string::npos) {ch = '"';
  }else if (symb.find("LIGHT") != string::npos) {ch = '_';
  }else if (symb.find("SCROLL") != string::npos) {ch = '~';
  }else if (symb.find("BOOK") != string::npos) {ch = '?';
  }else if (symb.find("FLASK") != string::npos) {ch  = '!';
  }else if (symb.find("GOLD") != string::npos) {ch = '$';
  }else if (symb.find("AMMUNITION") != string::npos) {ch = '/';
  }else if (symb.find("FOOD") != string::npos) {ch  = ',';
  }else if (symb.find("WAND") != string::npos) {ch  = '-';
  }else if (symb.find("CONTAINER") != string::npos) {ch  = '%';
  }else {ch  = '*';
  } 
  return ch;
}

//picks objects for dungeon.
void selObj()
{
  int rare, Obj;
  
  while (objects.size() < OBJECTS && objF.size() >=  1) {

    rare = rand() % 100;
    Obj = rand() % (int)objF.size();
    
    if (rare < objF.at(Obj).rrty) {
      objects.push_back(objF.at(Obj));
      objF.erase(objF.begin() + Obj);
    }
  }
}

//initiates objects
void initObj()
{
  int  xobj, yobj;
  bool ok;
  
 unsigned i;
  for (i = 0; i < objects.size(); i++) {
    
    ok = false;
    while (!ok) {
      xobj = rand() % DUNGEON_X;
      yobj = rand() % DUNGEON_Y;
  
      if (dungeon[yobj][xobj] == '.' ) {
		objects.at(i).lastPos = dungeon[yobj][xobj];
		objects.at(i).xpos = xobj;
		objects.at(i).ypos = yobj;
		dungeon[yobj][xobj] = objects.at(i).symb;
		ok = true;
      }
	  else if (dungeon[yobj][xobj] == '#') {
		objects.at(i).lastPos = dungeon[yobj][xobj];
		objects.at(i).xpos = xobj;
		objects.at(i).ypos = yobj;
		dungeon[yobj][xobj] = objects.at(i).symb;
		ok = true;
      }
    }
  }
}

//gets the value of dice.
int rollDice(string poss)
{
  int b, d, s;
  
  b = atoi(poss.substr(0, poss.find('+')).c_str());
  poss.erase(0, poss.find('+')+1);
  d = atoi(poss.substr(0, poss.find('d')).c_str());
  poss.erase(0, poss.find('d')+1);
  s = atoi(poss.c_str());

  return rollerReturn(b, d, s);
}

//helper to rollDice()
int rollerReturn(int b, int d, int s)
{
  return b + (rand() % (d * s - d + 1)) + d;
}


short getColor(char s, int x, int y)
{
  unsigned i;
  string cFind;
  short c = COLOR_WHITE;

  for (i = 0; i < monsters.size(); i++) {
    if (monsters.at(i).symb == s && monsters.at(i).xpos == x && monsters.at(i).ypos == y) {
      cFind = monsters.at(i).color.substr(0, monsters.at(i).color.find(' '));
      break;
    }
  }

  for (i = 0; i < objects.size(); i++) {
    if (objects.at(i).symb == s){
		if(objects.at(i).xpos == x){
			if(objects.at(i).ypos == y){
      cFind = objects.at(i).color.substr(0, objects.at(i).color.find(' '));
      break;
                       }
	         }
	}
  }

  if (cFind.find("BLACK") != string::npos) { c = COLOR_BLACK;
  }else if (cFind.find("RED") != string::npos) { c = COLOR_RED;
  }else if (cFind.find("BLUE") != string::npos) { c = COLOR_BLUE;
  }else if (cFind.find("YELLOW") != string::npos) { c = COLOR_YELLOW;
  }else if (cFind.find("CYAN") != string::npos) { c = COLOR_CYAN;
  }else if (cFind.find("GREEN") != string::npos) { c = COLOR_GREEN;
  }else if (cFind.find("MAGENTA") != string::npos) { c = COLOR_MAGENTA;
  } else { c = COLOR_WHITE;
  } 
  return c;
}

//Lets PC pick up objects.
char getItem(char s)
{
  unsigned i;
  char toRemove;

  if (!teleporting) {
    for (i = 0; i < objects.size(); i++) {
      if (objects.at(i).symb == s){
	if(pc.xpos == objects.at(i).xpos){
	  if(pc.ypos == objects.at(i).ypos) {
         	pc.items.push_back(objects.at(i));
	        pc.lastPos = objects.at(i).lastPos;
         	toRemove = objects.at(i).symb; 
        	objects.erase(objects.begin() + i);
           }
	}
      }
    }
  }
  else
    toRemove = '*';

  return toRemove;
}
/**
 *  Initailizes the terminal using ncurses.
 */
void initTerminal()
{
  initscr();
  raw();
  noecho();
  curs_set(0);
  keypad(stdscr, 1);
  start_color();
}
void showGraveYard(){
  clear();
  mvprintw(1 , 0, "                                                                        ");
  mvprintw(2 , 0, "      YOU ARE                                                           ");
  mvprintw(3 , 0, "                                                                        ");
  mvprintw(4 , 0, "                                                                        ");
  mvprintw(5 , 0, "       ******        *********    **********     ******                 ");  
  mvprintw(6 , 0, "       ********      *********    **********     ********               ");
  mvprintw(7 , 0, "       **     **     **           **      **     **     **              ");
  mvprintw(8 , 0, "       **      **    **           **      **     **      **             ");  
  mvprintw(9 , 0, "       **      **    *******      **********     **      **             ");
  mvprintw(10, 0, "       **      **    *******      **********     **      **             ");
  mvprintw(11, 0, "       **     **     **           **      **     **     **              ");
  mvprintw(12, 0, "       **    **      **           **      **     **    **               ");
  mvprintw(13, 0, "       *******       *********    **      **     *******                ");  
  mvprintw(14, 0, "       *****         *********    **      **     *****                  ");
  mvprintw(15, 0, "                                                                        ");
  mvprintw(16, 0, "           Press 'Q' button to close the game.                          ");
  mvprintw(17, 0, "                                                                        ");
  mvprintw(18, 0, "                                                                        ");
  mvprintw(19, 0, "                                                                        ");
  mvprintw(20, 0, "                                                                        ");
  refresh();
    if(getch()=='Q'){
      endwin();
    }else showGraveYard();
}
void showQuit(){
  clear();
  mvprintw(1 , 0, "                                                                        ");
  mvprintw(2 , 0, "      YOU                                                            ");
  mvprintw(3 , 0, "                                                                        ");
  mvprintw(4 , 0, "                                                                        ");
  mvprintw(5 , 0, "       *********     **     **    ***********    ***********            ");  
  mvprintw(6 , 0, "       *********     **     **    ***********    ***********            ");
  mvprintw(7 , 0, "       **     **     **     **        ***            ***                ");
  mvprintw(8 , 0, "       **     **     **     **        ***            ***                ");  
  mvprintw(9 , 0, "       **     **     **     **        ***            ***                ");
  mvprintw(10, 0, "       **     **     **     **        ***            ***                ");
  mvprintw(11, 0, "       **     **     **     **        ***            ***                ");
  mvprintw(12, 0, "       **     **     **     **        ***            ***                ");
  mvprintw(13, 0, "       *********     *********    ***********        ***                ");  
  mvprintw(14, 0, "       ***********   *********    ***********        ***                ");
  mvprintw(15, 0, "                ***                                                     ");
  mvprintw(16, 0, "                                                                         ");
  mvprintw(17, 0, "          Press 'Q' button to close the game, and try again.                                                  ");
  mvprintw(18, 0, "                                                                        ");
  mvprintw(19, 0, "                                                                        ");
  mvprintw(20, 0, "                                                                        ");
 refresh();
  if(getch()=='Q'){
    endwin();
  }else showQuit();
}
int startGame(){
     initDungeon(dungeon);
      genDungeon(dungeon);
      genHardness(HMap);
      genPC(dungeon);
      genStairs(4);
      parseMonFile();
      selM();
      initMonsters();
      initScore();
      parseObjFile();
      selObj();
      initObj();
      initTerminal();
      initDungeon(fogMap);
      updateFogMap();
      printToTerminal(fogMap);
      
      while (!gameOver) {
	
	makeMove();

	if (quit) {
	  showQuit();
	  endwin();
	  return 0;
	}	
      }
      //Game over, print out terminal
      printToTerminal(dungeon);
      showGraveYard();
      endwin();
      return 1;
}

void showRules(){
  clear();
  mvprintw(0,  0, "Press Q to Return to the Menu");
  mvprintw(1 , 0, "                                                                        ");
  mvprintw(2 , 0, "            The Basic Rules Are Simple:                                 ");
  mvprintw(3 , 0, "                1) Look At The Controls                                 ");
  mvprintw(4 , 0, "                2) Don't Die.                                           ");
  mvprintw(5 , 0, "                                                                        ");  
  mvprintw(6 , 0, "      +++------------------------------------------------+++            ");
  mvprintw(7 , 0, "                                                                        ");
  mvprintw(8 , 0, "         The Actual Rules:                                              ");  
  mvprintw(9 , 0, "             You ('@') must kill all of the monsters                    ");
  mvprintw(10, 0, "             on any given floor.  Monsters are denoted by               ");
  mvprintw(11, 0, "             a colored letter.  The game is turn based.                 ");
  mvprintw(12, 0, "             Move into a monster to attack it.  Items,                  ");
  mvprintw(13, 0, "             denoted by symbols are also available.  These              ");  
  mvprintw(14, 0, "             items will help you to defeat the monsters.                ");
  mvprintw(15, 0, "             The staircases, denoted by < and > will take               ");
  mvprintw(16, 0, "             you to different levels of the dungeon.                    ");
  mvprintw(17, 0, "                                                                        ");
  mvprintw(18, 0, "             Have Fun!                                                  ");
  mvprintw(19, 0, "                                                                        ");
  mvprintw(20, 0, "                                                                        ");
  refresh();

  if(getch()=='Q'){
    startScr();
  }else showRules();
  
}

void showControls(){
  clear();
  mvprintw(0, 0, "Press Q to Return to the Menu");
  mvprintw(1 , 0, "                                                                        ");
  mvprintw(2 , 0, "    +=+CONTROLS+=+                                                      ");
  mvprintw(3 , 0, "        Movement & Combat:                 Items:                       ");
  mvprintw(4 , 0, "       (Numpad) 7  NW                - To Pick up and Item,             ");
  mvprintw(5 , 0, "                8  N                   Stand on Top and Press Symbol    ");  
  mvprintw(6 , 0, "                9  NE                       w  Wear item               ");
  mvprintw(7 , 0, "                6  E                        t  Take off item           ");
  mvprintw(8 , 0, "                3  SE                       d  Drop item               ");  
  mvprintw(9 , 0, "                2  S                        x  Destroy item             ");
  mvprintw(10, 0, "                1  SW                       i  List inventory          ");
  mvprintw(11, 0, "                4  W                        e  List equipment           ");
  mvprintw(12, 0, "                5  Rest                     I  inspect item             ");
  mvprintw(13, 0, "             Monsters:                   Dev Tools:                    ");  
  mvprintw(14, 0, "                m  Show Monster List        t  teleport, again to land ");
  mvprintw(15, 0, "         Up Arrow  Scroll Up List           r  land at random spot     ");
  mvprintw(16, 0, "       Down Arrow  Scroll Down List                 when teleporting    ");
  mvprintw(17, 0, "              esc  Close List               f  toggle fog off/on        ");
  mvprintw(18, 0, "                L  Inspect Monster                                      ");
  mvprintw(19, 0, "                                                                        ");
  mvprintw(20, 0, "                                                                        ");
  refresh();

  if(getch()=='Q'){
    startScr();
  }else showControls();
  
}

void showCredits(){
  clear();
  mvprintw(0, 0, "Press Q to Return to the Menu");
  mvprintw(1 , 0, "                                                                        ");
  mvprintw(2 , 0, "      Hello!                                                            ");
  mvprintw(3 , 0, "      Thanks for trying out my first game, THE DARK.                    ");
  mvprintw(4 , 0, "      Countless hours have been spent on this game,                      ");
  mvprintw(5 , 0, "      as part of a class I am taking at University.                     ");  
  mvprintw(6 , 0, "      While not perfect, this game has a lot of flair.                  ");
  mvprintw(7 , 0, "      It is not easy, or perfect clear exactly what                     ");
  mvprintw(8 , 0, "      is going on each run, it is A LOT of fun.                         ");  
  mvprintw(9 , 0, "                                                                        ");
  mvprintw(10, 0, "                                                                       ");
  mvprintw(11, 0, "                                                                      ");
  mvprintw(12, 0, "      If you have any questions, comments, or bug reports,           ");
  mvprintw(13, 0, "      contact me via email at Jamestay@iastate.edu.                    ");  
  mvprintw(14, 0, "      I will get back to you as soon as possible!.                      ");
  mvprintw(15, 0, "                                                                        ");
  mvprintw(16, 0, "                                                                        ");
  mvprintw(17, 0, "                          Happy Dying!                                  ");
  mvprintw(18, 0, "                                    - James                             ");
  mvprintw(19, 0, "                                                                        ");
  mvprintw(20, 0, "                                                                        ");
  refresh();

  if(getch()=='Q'){
    startScr();
  }else showCredits();
  
}


void startScr(){
  clear();
  mvprintw(1 , 0, "                                                                        ");
  mvprintw(2 , 0, "            *********  ***  ***  *******                                ");
  mvprintw(3 , 0, "               ***     ***  ***  ***                                    ");
  mvprintw(4 , 0, "               ***     ********  *****                                   ");
  mvprintw(5 , 0, "               ***     ***  ***  ***                                    ");  
  mvprintw(6 , 0, "               ***     ***  ***  *******                                ");
  mvprintw(7 , 0, "                                                                        ");
  mvprintw(8 , 0, "                     ------      -------   -------    ---   ---         ");  
  mvprintw(9 , 0, "                     --   ---   ---   ---  ---  ---   --- ----          ");
  mvprintw(10, 0, "                     --    ---  ---   ---  ---  ---   -----             ");
  mvprintw(11, 0, "                     --    ---  ---------  ------     -----             ");
  mvprintw(12, 0, "                     --   ---   ---   ---  --- ---    --- ----          ");
  mvprintw(13, 0, "                     ------     ---   ---  --- -----  ---   ---         ");  
  mvprintw(14, 0, "                                                                        ");
  mvprintw(15, 0, "                          Press A Number to Continue                    ");
  mvprintw(16, 0, "                                   Play (1)                             ");
  mvprintw(17, 0, "                                  Rules (2)                             ");
  mvprintw(18, 0, "                                 Controls (3)                           ");
  mvprintw(19, 0, "                              Credits & About(4)                        ");
  mvprintw(20, 0, "                                  Exit ('Q')                            ");
  refresh();


  switch(getch()){
  case '1': startGame();
    break;
  case '2': showRules();
    break;
  case '3': showControls();
    break;
  case '4': showCredits();
    break;
  case 'Q': endwin();
    break;
  default: startScr();
    break;
  }

  endwin();
}
// "./main" to play the game.
int main(int argc, char * argv[])
{
  srand(time(NULL));

  if (argc > 2) {
    fprintf(stderr, "Too many arguments\n");
    return 1;
  }
  
  //No arguments, make and print the dungeon
  if (argc == 1) {
    initTerminal();
    startScr();
     return 0;
  }

  if (!strcmp(argv[1], "--art")) {
      initDungeon(dungeon);
    genDungeon(dungeon);
    genHardness(HMap);
    printDungeon(dungeon);
    } 
  return 0;
}
