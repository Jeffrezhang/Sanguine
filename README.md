******
SANGUINE, Final part of HW project for Program and Design @ Northeastern University
******

HW05:
We based the Code off the Model, View, Controller design principles, we started off with the Model
representing a card for the game of sanguine in one interface and implementation, and the model of
sanguine itself in an interface and implementation. The controller directory holds the Textual
Controller of the game of sanguine, the model directory holds the model for the card and game of
sanguine, the view directory holds the textual view of the game of sanguine.

We didn't have any player representation and instead had the players hands along with the
players pawns represented in the model. We then had the Controller keep track of the players
turn which is how I knew which player to make changes to in the model.

This information of the board is also accessible through access methods in the model which is then
used in the textual representation of the game of Sanguine.

The Sanguine file is used to run a game of sanguine and in the tests folder you can find tests for
the controller, the model, and the view of the game of sanguine.

HW06
made gui implementations for Sanguine, broke into two parts GUI controller and GUI view.
These two parts are broken down further, The view is given different panels to represent each 'portion' of the game
ie the hand panel, the board panel, and the score panel. The hand panel comprises Card widgets that represent
a card. The controller comprises the GUI controller that gets and interprets mouse clicks from the view,
which is passed through a proxy model which interprets the information passed and passes it through a communicator
which interprets and executes the commands on the proxy controller which is then sent to the real model.

We then implemented the AI adaptation of the game. Made the first AI behavior of just filling up the rows, then added another
AI to try to get the maximum score on a row. Added multiple AI classes and interfaces as well as added new PlayerTypes. Added
tests to test the AI behavior. Determining if the player is playing against AI or Human is determined in the GUI controller
with the enum PlayerType.

HW07
Added player classes that had both human and AI behavior.
Added tests for these classes as well as patched up javadoc for the previous tests
The human version now pulls up 2 separate GUIs; one for the red player and one for the blue.
Images of this version are now in the images folder this time and we removed the images from hw06.
The main file is in sanguine.gui.SanguineGame

Invariants from hw5:
BasicSanguine invariants
The board is always 3 rows and 5 columns.
The score board is always 3 rows and 2 columns.
Player decks (p1deck, p2deck) are never null.
After the game starts, gameStarted is true forever.
p1hand and p2hand never contain illegal values (only valid Cards).
A board cell never has both a card and pawns at the same time.
textBoard cells are always '_', 'R', or 'B'.
pawnBoard cells are always null or a valid pawn belonging to player 1 or 2.
Any pawn on the board always has a count between 1 and 3.
A player’s score is always 0 or higher.
concurrPasses is always 0, 1, or 2+, and never negative.
getCell always returns one of: 'R', 'B', '_', or a digit '1'–'3'.

ReadOnlyBasicSanguine invariants
The wrapped model is never null.
The readonly model never changes the game state, only reads it.
Every method returns exactly what the real model currently has.

SanguineCard invariants
A card’s cost is always 1, 2, or 3.
A card’s value is always a positive number.
The AOE grid is always 5×5.
There is exactly one center tile ('C') and it is always at position (2,2).
The AOE values are always only 0, 1, or 2
getAoe() always returns a deep copy, never the original array.

SanguinePawn invariants
A pawn’s owner is always 1 or 2.
A pawn’s count is always ≥ 1.
SanguinePawn is immutable—its fields never change.
withCount always returns a new pawn, not the same one.
