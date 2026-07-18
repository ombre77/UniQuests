# How to create a quest?

## Where?

To create a quest, you need to go in the "quests" folder, inside the plugin's folder. You'll meet 3 options:
- daily
- weekly
- monthly
- global

Each of those 3 options represent the time the quest will be keep before being disabled then deleted (disable/enable deleting in the plugin config). daily is 1 day, weekly 7, monthly 30 and global infinite.

## How

To create a quest, you need to create a *quest file*. a quest file is a JSON file with this base structure:
```json
{
  "created":"dd/MM/yy",
  "quests":{}
}
```
(replace the date in "created" field to the matching day)

In order to add a quest, just add a field inside quests with the quest id (can be anything)

```json
{
"created":"dd/MM/yy",
"quests": {
  "brand_new_quest":{}
  }
}
```

Then, fill the value with another map containing  the quest parameters
```json
{
  "created":"dd/MM/yy",
  "quests":{
    "brand_new_quest":{
      "display_name":"Get started",
      "desc":"A quest to guide you on how to get started",
      "requirements":[
        {"type":"have","item":"dirt","amount":32},
        {"type":"killed","mob":"pig","amount":10},
        {"type":"placed","block":"stone","amount":25}
      ],
      "price":[
        {"item":"dirt","amount":32},
        {"exp":"level","amount":1}
      ],
      "reward":[
        {"type":"item","item":"emerald","amount":10},
        {"type":"exp","exp":"points","amount":100}
      ]
    }
  }
}
```

**Quest parameter**
- `display_name` : the name of the quest on the item in the quest menu
- `desc` : a quick description of the quest (showed in the lore of the item)
- `requirements` : the requirements to complete the quest
    - **Multiple type of requirements**
        - `have` ("item","amount") : the player must have in its inventory a certain quantity of an item
        - `killed` ("mob","amount") : the player must have killed a certain amount of a mob
        - `placed` ("block","amount"): the player must have placed a certain amount of a block
        - `quest` ("quest_id"): the player must have completed a certain quest
- `price` : the cost of the quest
    - **Multiple type of price**
        - `item` : take this a mount of item of the player inventory
            - If the player doesn't have enough of this item, the quest cannot be validated
        - `exp` : take a certain level/points of exp of the player
            - If the player doesn't have enough exp, the quest cannot be validated
- `reward` : the reward that is given to the player
    - **Multiple type of reward**
        - `item`
        - `exp`
    - **How to set a reward's chance**
        - add a parameter called `chance` with a value corresponding to the percentage of chance of this reward
        - *Note: for now, might be changed in the future, there is no way to block a reward if another is given. I might add a `fallback` system for it in the future*
    - **Reward Tables**
        - Hi king! I know you will be reading this, so dw, I am currently adding a reward table system so you can set predefined items like you wanted. Also, for the event coin, just put "coin" for the `type` of the reward and the code will understand. You can also adjust the `amount` and the `chance`

Note: you can create multiple quests in a same file, but they will share the same creation and so deletion date. All quests in a file must now be nested under the `quests` key. `created` and `quests` are the only two top-level fields.
### If there is any more questions, dm me!