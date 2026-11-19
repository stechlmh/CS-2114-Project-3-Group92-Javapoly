# CS-2114-Project-3-Group92-Javapoly

Javapoly is a console Monopoly-style game for 2-4 players built using a circular singly linked list for the board and singly linked FIFO queues for the Chance and Community Chest card decks.

## Team

- Maple
- Kyle
- Pratham Singh
- Thomas

## How to Compile and Run the Program and Tests

All source and test files are in `src/Javapoly` under the `Javapoly` package.

To compile and run the console program through the controller and view:
- In Eclipse or VS Code: right-click `src/Javapoly/GameController.java` and select **Run As > Java Application**.
- From the command line:

    javac -d bin src/Javapoly/*.java
    java -cp bin Javapoly.GameController

To compile and run the JUnit tests (which exercise the Queue-backed model independently of the view):
- In Eclipse or VS Code (with `CS2-Support` / `student.jar` on the build path): right-click `src/Javapoly/QueueTest.java`, `src/Javapoly/BoardStateTest.java`, `src/Javapoly/GameStateTest.java`, `src/Javapoly/GameControllerTest.java`, or `src/Javapoly/LinkedChainTest.java` and select **Run As > JUnit Test**.
- From the command line:

    javac -cp path/to/student.jar -d bin src/Javapoly/*.java
    java -cp bin:path/to/student.jar junit.textui.TestRunner Javapoly.QueueTest
    java -cp bin:path/to/student.jar junit.textui.TestRunner Javapoly.BoardStateTest
    java -cp bin:path/to/student.jar junit.textui.TestRunner Javapoly.GameStateTest
    java -cp bin:path/to/student.jar junit.textui.TestRunner Javapoly.GameControllerTest
    java -cp bin:path/to/student.jar junit.textui.TestRunner Javapoly.LinkedChainTest

## Reflection

How our design absorbed the Deliverable 4 change feature:

- **Change feature implemented:** We implemented the **Jail & "Get Out of Jail Free" / Skip-Turn System** named in our Deliverable 3 Next Steps, along with the pass-GO $200 bonus and full 40-space board. Players are sent to `"Jail / Just Visiting"` when landing on `"Go To Jail"` or drawing `"Go to Jail. Go directly to Jail."` from the `Queue<EventCard>` Chance deck. On a jailed turn, the player can pay $50 bail, use a held `"Get Out of Jail Free"` card drawn from the Community Chest `Queue<EventCard>`, or wait up to 3 turns before automatic release.
- **Classes actually touched vs. our Deliverable 3 prediction:**
  - *Touched:* `Player` (added `inJail`, `jailTurns`, `getOutOfJailCards`, `isInJail()`, `setInJail(boolean)`, `getJailTurns()`, `incrementJailTurns()`, `getGetOutOfJailCards()`, and `setGetOutOfJailCards(int)`), `BoardState` (added `getSpaceByName(String)` and the pass-GO $200 check in `movePlayer`), `GameState` (added `sendToJail(Player)` and `handleJailTurn(Player, boolean)`, and updated `moveCurrentPlayer(int)`, `drawCard(String)`, and `initializeDeck(String)`), and `GameController` (updated `handleTurn()` to prompt jailed players and report jail transitions via `GameView`).
  - *Unchanged:* Every class our Deliverable 3 prediction said should stay unchanged — `Queue`, `BoardNode`, and `PropertySpace` (as well as `LinkedChain`, `Space`, `CardSpace`, and `EventCard`) — required **zero edits**.
  - *What matched and what surprised us:* Our D3 prediction matched the exact model and controller classes and method names we needed. The only mild surprise was that `GameView.java` did not need a new jail-specific method because `GameController` could pass the jail prompt and status strings directly to `GameView.getInput(String)`, `GameView.showMessage(String)`, and `GameView.showError(String)`.

Did the change cross the model/view line?

- No. All jail state (`inJail`, `jailTurns`, `getOutOfJailCards`) and rules ($50 bail validation, automatic card usage, 3-turn wait counter, and moving the player to the Jail space) live strictly in `Player`, `BoardState`, and `GameState`. `GameView` keeps no copy of model state and has no references to `Player`, `GameState`, or `BoardState`. `GameController.handleTurn()` reads the model state, prompts the user through `GameView`, invokes `gameState.handleJailTurn(...)`, and sends formatted output back to `GameView`.

What changed from our Deliverable 3 plan, and why:

- In `Player`, in addition to `isInJail()`, `setInJail(boolean)`, and `incrementJailTurns()`, we added `getJailTurns()`, `getGetOutOfJailCards()`, and `setGetOutOfJailCards(int)` (which throws `IllegalArgumentException` on negative counts) so `GameState` and `GameStateTest` can manage and verify held cards and turn counts without exposing fields.
- In `GameController.handleTurn()`, if a player in jail chooses `"y"` to pay the $50 bail when they have less than $50, `GameState.handleJailTurn(current, true)` throws `IllegalArgumentException("Insufficient funds to pay $50 bail")`; `GameController` catches that bad-input error, displays it via `gameView.showError(...)`, and falls back to `handleJailTurn(current, false)` so the player does not get trapped in an unexitable prompt loop.
- What design change would make the next feature cheaper: currently `GameState.moveCurrentPlayer(int)` and `GameState.drawCard(String)` inspect string descriptions/effect tags (`"GO_TO_JAIL"`, `"go to jail"`, `"get out of jail free"`). Refactoring space and card effects into a polymorphic `applyEffect(GameState, Player)` method on `Space` and `EventCard` would make adding future spaces or cards (such as tax spaces or "Advance to GO" cards) cheaper because new effects would not require new `if/else` checks in `GameState`.

What doesn't work yet:

- Landing on `Income Tax` and `Luxury Tax` spaces currently logs the landing space without deducting a tax amount.
- The property development stretch goal (buying houses/hotels) has data fields and rent tiers in `PropertySpace`, but the controller prompt to build houses during a player's turn is not wired into `GameController` yet.

## Next Steps

What is left for the final build (in order):

1. Add tax space deductions (`Income Tax` $200 and `Luxury Tax` $100) in `GameState` when landing on `"TAX"` effect spaces, including bankruptcy handling if a player cannot afford the tax.
2. Add a house/hotel build method in `GameState` and an optional build prompt in `GameController.handleTurn()` so players who own properties can increment `PropertySpace.setHouses(int)` and scale rent.
3. Integrate `GameView.showAllPlayers(String[])` and `GameView.showPropertyInfo(...)` into `GameController.updateView()` for cleaner end-of-round summaries.
4. Run final multi-player rehearsal playthroughs and lock in the 3-minute live demo script.

Stretch goals:

- **Committing to:** Customizable Game Rules / Jail mechanics (completed in Deliverable 4) and Property Development System (houses/hotels using `PropertySpace` rent tiers).
- **Dropping:** Dynamic Disaster Events and Full Player-to-Player Trading/Auctions, so we can keep the final build well-tested and reliable.

What our 3-minute demo will show (timed path):

- **0:00–0:35 (Setup & Bad-Input Survival):** Launch `GameController.main`, enter bad player counts (`""`, `"abc"`, `1`, `5`) to show `GameController` and `GameView` catching errors and re-prompting without crashing, then start a 2-player game with $1500 each at `GO`.
- **0:35–1:25 (Core Circular Board & Queue Deck MVP):** Roll to move across `BoardState`, purchase an unowned `PropertySpace`, show the next player paying rent on that property, and land on `Chance` / `Community Chest` to show `GameState.drawCard` dequeuing an `EventCard`, applying its balance change, and re-enqueuing it to the back of `Queue<EventCard>`.
- **1:25–2:25 (Deliverable 4 Change Feature — Jail System):** Move a player onto `Go To Jail` (or draw the `"Go to Jail"` card) to show `GameState.sendToJail` moving them to `"Jail / Just Visiting"` via `BoardState.getSpaceByName`. On their next turn, enter an invalid jail input (`"maybe"`) to show bad-input recovery, decline bail (`"n"`) to show the jail turn counter (`Turn 1/3`), and then pay the $50 bail (`"y"`) or use a `"Get Out of Jail Free"` card on the next turn to leave jail and roll normally.
- **2:25–3:00 (Pass-GO, Bankruptcy & JUnit Suite):** Show a player wrapping past `GO` to collect $200, trigger a bankruptcy to show properties resetting and winner announcement, and run `QueueTest`, `BoardStateTest`, `GameStateTest`, `GameControllerTest`, and `LinkedChainTest` passing in JUnit.

Who does what:

| Task | Owner |
| --- | --- |
| Tax space deductions in `GameState` + unit tests | Kyle & Pratham |
| House/hotel building logic and `PropertySpace` tests | Maple |
| Wire `GameView` summary helpers into `GameController.updateView()` | Thomas |
| Final 3-minute demo script rehearsal and full regression check | All team members |

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
| QueueTest.java | Pratham Singh | wrote and added interleaved enqueue/dequeue order tests, GenAI-assisted |
| BoardNode.java | Pratham Singh | wrote, GenAI-assisted |
| BoardState.java | Pratham Singh | wrote and added pass-GO $200 logic and `getSpaceByName`, GenAI-assisted |
| BoardStateTest.java | Pratham Singh | wrote and added pass-GO and `getSpaceByName` normal/bad-input tests, GenAI-assisted |
| Player.java | Pratham Singh | wrote and added D4 jail state fields and methods, GenAI-assisted |
| GameState.java | Kyle, Pratham Singh | Kyle wrote core structure and 40-space board; Pratham added D4 jail methods (`sendToJail`, `handleJailTurn`) and jail cards, GenAI-assisted |
| GameStateTest.java | Pratham Singh | wrote and added D4 jail and card cycling normal/bad-input tests, GenAI-assisted |
| GameController.java | Kyle, Pratham Singh | Kyle wrote class structure; Pratham wired turn loop, D4 jail bail prompts, and bad-input handling, GenAI-assisted |
| GameControllerTest.java | Pratham Singh | wrote and added D4 jail flow and view coverage tests, GenAI-assisted |
| GameView.java | Thomas, Maple, Pratham Singh | Thomas & Maple designed and expanded view display methods; Pratham implemented `Scanner` console methods |

GenAI: We used ChatGPT during the scope and specification phases to review our MVC boundaries, method signatures, and Big-O complexities. For Deliverable 3 and Deliverable 4, we used AI to help check which classes our Jail change feature would touch across the model/view boundary, draft candidate JUnit tests (including interleaved enqueue/dequeue checks on `Queue`, `getSpaceByName`, `sendToJail`, and `handleJailTurn` bad-input cases), and verify branch coverage. We reviewed and tested all code ourselves to confirm the tests target our model contract rather than view details.
