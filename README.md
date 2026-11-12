# CS-2114-Project-3-Group92-Javapoly

Javapoly is a console Monopoly-style game for 2-4 players built using a circular singly linked list for the board and singly linked FIFO queues for the Chance and Community Chest card decks.

## Team

- Maple
- Kyle
- Pratham Singh
- Thomas

## How to Run the Model and Thin View

All source and test files are in `src/Javapoly` under the `Javapoly` package.

To run the thin console view through the controller:
- In Eclipse: right-click `src/Javapoly/GameController.java` and select **Run As > Java Application**.
- From the command line:

    javac -d bin src/Javapoly/*.java
    java -cp bin Javapoly.GameController

To exercise the Queue-backed model directly without any view:
- In Eclipse (with `CS2-Support` on the build path): right-click `src/Javapoly/QueueTest.java`, `src/Javapoly/BoardStateTest.java`, or `src/Javapoly/GameStateTest.java` and select **Run As > JUnit Test**.
- From the command line:

    javac -cp path/to/student.jar -d bin src/Javapoly/*.java
    java -cp bin:path/to/student.jar junit.textui.TestRunner Javapoly.QueueTest
    java -cp bin:path/to/student.jar junit.textui.TestRunner Javapoly.BoardStateTest
    java -cp bin:path/to/student.jar junit.textui.TestRunner Javapoly.GameStateTest

## Reflection

What changed from our specification:

- **Classes:** The specification listed `EventSpace` and `EventCard (Event)` alongside `Main`. In our repository we already had `CardSpace.java` and `Event.java` created during early setup, so we kept `CardSpace` as the event-space subclass of `Space` and added `EventCard` with the exact `getDescription()` and `getAmount()` methods from the spec so the `Queue<EventCard>` decks and `GameState` work as designed. Instead of a separate `Main` class, `GameController.main` starts the game directly, which prevented an extra wrapper file.
- **Method signatures:** In `GameState`, Kyle initially wrote `buyProperty(Player, PropertySpace)` and `payRent(Player, PropertySpace)` while the spec table listed `buyProperty(PropertySpace)` and `payRent(PropertySpace)` for the active player. We kept both overloads so the single-parameter spec methods delegate to the active player without breaking existing calls. In `PropertySpace`, Maple initially added `getPrice()` and `int[] rent` for future house/hotel stretch goals; we added `getCost()`, `getRent()` (returning the current rent `int`), and a simpler constructor `PropertySpace(String, int, int, int)` so the MVP model matches the specification signatures. In `Queue`, we also added `pop()` as a helper alias for `dequeue()` because `GameState.drawCard` originally called `pop()`.
- **Where validation lives:** Validation stayed where the specification placed it. `GameState.startGame` validates player count (2-4) with `IllegalArgumentException`, `BoardState.movePlayer` validates non-null player and die steps (1-6), `Queue.enqueue` rejects null items with `IllegalArgumentException`, `Queue.dequeue` and `Queue.peek` throw `NoSuchElementException` when empty, and `GameState.drawCard` throws `IllegalStateException` when a deck is empty. `GameController` catches bad console inputs (empty strings, non-numeric player counts, out-of-range counts, and invalid `y/n` or turn commands) and re-prompts through `GameView`.
- **Test cases:** We added `QueueTest`, `BoardStateTest`, `GameStateTest`, and `GameControllerTest` following the test plan table, testing normal FIFO queueing, board wrap-around traversal, property purchases, rent bankruptcy, card cycling, and invalid inputs.

Did the model really stand alone?

- Initially, `PropertySpace.triggerEffect` and `CardSpace.triggerEffect` had static calls to `GameView` (`GameView.triggerPurchase`, `GameView.triggerAuction`, `GameView.triggerPayment`) from an early draft before we finalized the MVC boundary in our spec. We removed those `GameView` calls from the model classes and moved user prompts into `GameController.handleTurn()`. Now `Queue`, `BoardNode`, `BoardState`, `GameState`, `Player`, `Space`, `PropertySpace`, `CardSpace`, and `EventCard` have zero references to `GameView` and run completely on their own in JUnit tests.

What happened to our unknowns?

- **Randomized die application towards linked list & moving into spaces based off a linked list:** Figured out. `GameController.rollDie()` generates a 1-6 value and passes it to `GameState.moveCurrentPlayer(steps)`, which calls `BoardState.movePlayer(player, steps)` to traverse `next` references node-by-node around the circular list and update the player's `Space` reference.
- **How to implement an engaging GUI for a normally physical board game:** Still open; we are using the text-based console view for the MVP first.
- **How to implement the get out of jail system, keep Chance cards for later use, and create a trading/selling system:** Still open, as these are stretch goals beyond the MVP.

What doesn't work yet?

- Passing GO does not yet award a $200 bonus when wrapping around the board.
- The board currently initializes a smaller set of spaces in `GameState.initializeBoard()` rather than the full Monopoly board layout.
- Stretch goals (houses/hotels, auctions, trading, and jail) are not implemented yet.

## Next Steps

What is left to finish the MVP (in order):

1. Expand `GameState.initializeBoard()` and `initializeDeck()` with the full set of board properties, tax spaces, and Chance/Community Chest cards.
2. Add the pass-GO check in `BoardState.movePlayer()` / `GameState.moveCurrentPlayer()` so players collect money when wrapping past `start`.
3. Refine `GameView` console formatting so all player balances and owned property lists print clearly at the end of each round.
4. Run full multi-player console playthroughs to verify all bad-input prompts and bankruptcy edge cases.

Change feature for Deliverable 4:

- **Feature:** Jail & "Get Out of Jail Free" / Skip-Turn System (with a held card or 3-turn wait / $50 bail option). Landing on a "Go To Jail" space or drawing a "Go to Jail" `EventCard` moves the player to the Jail space and marks them as in jail; on their turn they can pay bail, use a held card, or roll to get out.
- **Classes and methods that WILL need to change:**
  - `Player`: add `inJail` (`boolean`), `jailTurns` (`int`), and `getOutOfJailCards` (`int`) fields along with `isInJail()`, `setInJail(boolean)`, and `incrementJailTurns()`.
  - `BoardState`: add `getSpaceByName(String name)` so the model can move a player directly to the Jail space.
  - `GameState`: add `sendToJail(Player)` and `handleJailTurn(Player, boolean payBail)` and update `moveCurrentPlayer(int)` / `drawCard(String)` to check for jail effects.
  - `GameController`: update `handleTurn()` to check `current.isInJail()` and prompt the player via `GameView` whether to pay bail or roll before normal movement.
  - `GameView`: display the jail prompt message passed from `GameController` using the existing `getInput(String)` and `showMessage(String)` methods.
- **Classes that SHOULD NOT need to change:**
  - `Queue`: the generic FIFO queue implementation stores `EventCard` objects regardless of their effect and does not need to change.
  - `BoardNode`: node linking in the circular list does not change.
  - `PropertySpace`: property cost, rent, and ownership rules are independent of jail state.

Stretch goals:

- **Committing to:** Customizable Game Rules / Jail mechanics (as our Deliverable 4 change feature) and Property Development System (houses/hotels, since fields are already stubbed in `PropertySpace`).
- **Dropping:** Dynamic Disaster Events and Full Player-to-Player Trading/Auctions, to keep the scope manageable.

New tests needed:

- `GameStateTest`: test sending a player to jail, paying bail to leave jail, attempting to pay bail with insufficient funds (bad input), and cycling a "Go to Jail" card through `Queue<EventCard>`.
- `BoardStateTest`: test `getSpaceByName` for existing spaces, missing spaces, and null input.
- `PropertySpaceTest`: test building houses on a completed color set, rejecting house purchases with insufficient funds, and rejecting building past a hotel (5 houses).

Who does what:

| Task | Owner |
| --- | --- |
| Expand board spaces and card decks in `GameState` | Kyle |
| Pass-GO logic and `BoardState` lookup helpers + tests | Pratham |
| Jail feature model methods (`Player`, `GameState`) | Pratham & Kyle |
| Property development (houses/hotels) + `PropertySpace` tests | Maple |
| Console UI polish and controller jail prompts + tests | Thomas |

## Attribution

| File | Who | How |
| --- | --- | --- |
| LinkedChain.java | CS 2114 Staff / Maple | Given starter file, moved to `Javapoly` package |
| LinkedChainTest.java | Maple | wrote |
| Space.java | Maple | wrote |
| PropertySpace.java | Maple, Pratham Singh | Maple wrote class; Pratham added spec getters (`getCost`, `getRent`) and decoupled from `GameView` |
| CardSpace.java | Maple, Pratham Singh | Maple created; Pratham revised to use `Queue<EventCard>` without `GameView` calls |
| Event.java | Maple | created skeleton |
| EventCard.java | Pratham Singh | wrote from spec, GenAI-assisted |
| Queue.java | Pratham Singh | wrote, GenAI-assisted |
| QueueTest.java | Pratham Singh | wrote, GenAI-assisted |
| BoardNode.java | Pratham Singh | wrote, GenAI-assisted |
| BoardState.java | Pratham Singh | wrote, GenAI-assisted |
| BoardStateTest.java | Pratham Singh | wrote, GenAI-assisted |
| Player.java | Pratham Singh | wrote from spec fields and methods, GenAI-assisted |
| GameState.java | Kyle, Pratham Singh | Kyle wrote core structure and methods; Pratham connected `Queue`, `BoardState`, and deck initialization, GenAI-assisted |
| GameStateTest.java | Pratham Singh | wrote, GenAI-assisted |
| GameController.java | Kyle, Pratham Singh | Kyle wrote class structure and method stubs; Pratham wired turn loop and bad-input handling, GenAI-assisted |
| GameControllerTest.java | Pratham Singh | wrote, GenAI-assisted |
| GameView.java | Thomas, Maple, Pratham Singh | Thomas & Maple designed view methods; Pratham implemented `Scanner` console methods from spec |

GenAI: We used ChatGPT during the scope and specification phases to review our MVC boundaries, method signatures, and Big-O complexities. For Deliverable 3, we used AI to help implement `Queue`, `BoardNode`, `BoardState`, and the unit tests against the project specification, and to check which classes our Deliverable 4 Jail change feature would touch across the model/view boundary (confirming `Queue`, `BoardNode`, and `PropertySpace` would not need to change once `PropertySpace` was decoupled from `GameView`). We reviewed the code and ran the JUnit tests ourselves.
