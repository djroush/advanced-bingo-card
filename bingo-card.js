const BingoSize = 5;
Object.freeze(BingoSize);
const BingoStates = { GOAL_NOT_MET : "0", GOAL_MET : "1", GOAL_CANNOT_BE_MET_ANYMORE : "2" };
Object.freeze(BingoStates);
const BingoTypes = { COLUMN: 5, ROW: 1, TL_BR: 6, TR_BL: 4 };
Object.freeze(BingoTypes);

const ClickType = { NORMAL : "0", SHIFT : "1", CONTROL : "2" };
Object.freeze(ClickType);

const PlayerType = { NONE : "0", P1 : "1", P2 : "2" };
Object.freeze(PlayerType);
const Player1Colors = { DEFAULT : "rgba(0, 0, 0, 1)", HIGHLIGHT : "rgba(0, 112, 48, 0.375)" };
Object.freeze(Player1Colors);
const Player2Colors = { DEFAULT : "rgba(0, 0, 0, 1)", HIGHLIGHT : "rgba(80, 0, 112, 0.375)" };
Object.freeze(Player2Colors);
function classifyClick(event) {
  if (event.ctrlKey) {
    return ClickType.CONTROL;
  }

  if (event.shiftKey) {
    return ClickType.SHIFT;
  }

  return ClickType.NORMAL;
}
function classifyPlayer(element, event) {
  relativeX = event.clientX - element.getBoundingClientRect().left;
  relativeY = event.clientY - element.getBoundingClientRect().top;

  sum = relativeX + relativeY;
  withinX = (0 < relativeX) && (relativeX <= 128);
  withinY = (0 < relativeY) && (relativeY <= 128);
  player1 = (sum < 128) && withinX && withinY;
  player2 = (sum > 128) && withinX && withinY;

  if (player1) {
    return PlayerType.P1;
  } else if (player2) {
    return PlayerType.P2;
  } else {
    return PlayerType.NONE;
  }
}

function mouseBorderTile(element, highlight) {
  var borderId = element.id;
  var borderNumber = parseInt(borderId.substring("border-".length));

  var playerType;
  if (borderNumber < (2 * BingoTypes.TL_BR)) {
    playerType = PlayerType.P1;
  } else {
    playerType = PlayerType.P2;
  }

  var modDiagonal = borderNumber % BingoTypes.TL_BR;
  var divDiagonal = Math.floor(borderNumber / BingoTypes.TL_BR);

  var start;
  var delta;
  if (modDiagonal == 0) {
    if ((divDiagonal % 2) == 0) {
      delta = BingoTypes.TR_BL;
      start = BingoTypes.TR_BL;
    } else {
      delta = BingoTypes.TL_BR;
      start = 0;
    }
  } else {
    if ((divDiagonal % 2) == 0) {
      delta = BingoTypes.ROW;
      if (playerType == PlayerType.P1) {
        start = (BingoTypes.COLUMN - modDiagonal) * BingoTypes.COLUMN;
      } else { // PlayerType.P2
        start = (modDiagonal - BingoTypes.ROW) * BingoTypes.COLUMN;
      }
    } else {
      delta = BingoTypes.COLUMN;
      if (playerType == PlayerType.P1) {
        start = (modDiagonal - BingoTypes.ROW) * BingoTypes.ROW;
      } else { // PlayerType.P2
        start = (BingoTypes.COLUMN - modDiagonal) * BingoTypes.ROW;
      }
    }
  }

  if (!highlight) {
    playerType = PlayerType.NONE;
  }

  var counter = 0;
  var tileNumberLoop = start;
  while (counter < BingoSize) {
    var workingTile = document.getElementById("tile-" + tileNumberLoop);
    highlightBingoTile(workingTile, playerType);

    counter++;
    tileNumberLoop += delta;
  }
}
function mouseBingoTile(element, event) {
  var playerType = classifyPlayer(element, event);
  highlightBingoTile(element, playerType);
}
function highlightBingoTile(element, playerType) {
  var colorP1 = Player1Colors.DEFAULT;
  var colorP2 = Player2Colors.DEFAULT;

  if (playerType == PlayerType.P1) {
    colorP1 = Player1Colors.HIGHLIGHT;
  } else if (playerType == PlayerType.P2) {
    colorP2 = Player2Colors.HIGHLIGHT;
  }

  var tileBackground = "linear-gradient(135deg, " + colorP1 + " 0px, " + colorP1 + " 90px, #000000 91px, #000000 93px, " + colorP2 + " 94px, " + colorP2 + " 192px)";
  element.style.background = tileBackground;
}

function clickBorderTile(element, player) {
  var classList = element.classList;
  var parentDiv = element.children.namedItem("parent-div");

  var stateDiv = parentDiv.children.namedItem("state-div");
  var form = stateDiv.children.namedItem("form");
  var required = form.elements["required"];

  if (required.value == "0") {
    classList.remove("border-default-" + player);
    classList.add("border-required-" + player);
    required.value = "1";
  } else {
    classList.remove("border-required-" + player);
    classList.add("border-default-" + player);
    required.value = "0";
  }
}
function clickBingoTile(element, event) {
  var parentDiv = element.children.namedItem("parent-div");
  var stateDiv = parentDiv.children.namedItem("state-div");
  var svgsDiv = parentDiv.children.namedItem("outlines-div");

  var form = stateDiv.children.namedItem("form");
  var svg = svgsDiv.children.namedItem("outlines");

  var clickType = classifyClick(event);
  var playerType = classifyPlayer(element, event);
  switch (playerType) {
    case PlayerType.P1:
      var state = form.elements["player1"];
      var outline = svg.children.namedItem("player1");
      advanceTileState(clickType, state, true, outline);
    break;
    case PlayerType.P2:
      var state = form.elements["player2"];
      var outline = svg.children.namedItem("player2");
      advanceTileState(clickType, state, false, outline);
    break;
    case PlayerType.NONE:
    break;
  }

  updateBingos(element.id, playerType);
}
function advanceTileState(clickType, state, isPlayer1, outline) {
  var svgHref = document.createAttribute("href");

  switch (clickType) {
    case ClickType.NORMAL:
      switch (state.value) {
        case BingoStates.GOAL_NOT_MET:
          state.value = BingoStates.GOAL_MET;
          svgHref.value = (player1 ? "#p1-goal-met" : "#p2-goal-met");
        break;
        case BingoStates.GOAL_MET:
          state.value = BingoStates.GOAL_CANNOT_BE_MET_ANYMORE;
          svgHref.value = (player1 ? "#p1-goal-cannot-be-met" : "#p2-goal-cannot-be-met");
        break;
        case BingoStates.GOAL_CANNOT_BE_MET_ANYMORE:
          state.value = BingoStates.GOAL_NOT_MET;
          svgHref.value = "#empty";
        break;
      }
    break;
    case ClickType.CONTROL:
      if (state.value == BingoStates.GOAL_MET) {
        state.value = BingoStates.GOAL_NOT_MET;
        svgHref.value = "#empty";
      } else {
        state.value = BingoStates.GOAL_MET;
        svgHref.value = (player1 ? "#p1-goal-met" : "#p2-goal-met");
      }
    break;
    case ClickType.SHIFT:
      if (state.value == BingoStates.GOAL_CANNOT_BE_MET_ANYMORE) {
        state.value = BingoStates.GOAL_NOT_MET;
        svgHref.value = "#empty";
      } else {
        state.value = BingoStates.GOAL_CANNOT_BE_MET_ANYMORE;
        svgHref.value = (player1 ? "#p1-goal-cannot-be-met" : "#p2-goal-cannot-be-met");
      }
    break;
  }

  outline.attributes.setNamedItem(svgHref);
}

function updateBingos(tileId, player) {
  tileNumber = parseInt(tileId.substring("tile-".length));
  column = tileNumber % BingoTypes.COLUMN;
  row = Math.floor(tileNumber / BingoTypes.COLUMN);

  updateBingo(BingoTypes.ROW * column, BingoTypes.COLUMN, player, tileNumber);
  updateBingo(BingoTypes.COLUMN * row, BingoTypes.ROW, player, tileNumber);
  if (row == column) {
    updateBingo(0, BingoTypes.TL_BR, player, tileNumber);
  }
  if ((row + column) == BingoTypes.TL_BR) {
    // row and column are each offset by 1, 1..5 instead of 0..4, so (BingoSize + 1) instead of (BingoSize - 1)
    updateBingo(BingoTypes.TR_BL, BingoTypes.TR_BL, player, tileNumber)
  }
}
function updateBingo(start, delta, player, tileNumber) {
  var newStatus = getNewBorderStatus(start, delta, player);
  var outlineName = getOutlineName(newStatus, delta);
  var borderId = getBorderId(delta, player, tileNumber)

  updateBorderOutline(borderId, outlineName);
}

function getNewBorderStatus (start, delta, player) {
  var notMet = false;
  var cannotBeMet = false;

  var counter = 0;
  var tileNumberLoop = start;
  while ((counter < BingoSize) && (!notMet || !cannotBeMet)) {
    var workingTile = document.getElementById("tile-" + tileNumberLoop);
    var status = getBingoStatus(workingTile, player)
    if (status == BingoStates.GOAL_CANNOT_BE_MET_ANYMORE) {
      cannotBeMet = true;
    } else if (status == BingoStates.GOAL_NOT_MET) {
      notMet = true;
    }

    counter++;
    tileNumberLoop += delta;
  }

  if (cannotBeMet) {
    return BingoStates.GOAL_CANNOT_BE_MET_ANYMORE;
  } else if (notMet) {
    return BingoStates.GOAL_NOT_MET;
  } else {
    return BingoStates.GOAL_MET;
  }
}
function getBingoStatus(element, player) {
  var parentDiv = element.children.namedItem("parent-div");
  var stateDiv = parentDiv.children.namedItem("state-div");

  var form = stateDiv.children.namedItem("form");
  switch (player) {
    case PlayerType.P1:
      return form.elements["player1"].value;
    break;
    case PlayerType.P2:
      return form.elements["player2"].value;
    break;
    case PlayerType.NONE:
      return -1;
  }
}

function getOutlineName(newStatus, delta) {
  var outlineName = "#empty";
  if (newStatus != BingoStates.GOAL_NOT_MET) {
    var borderType;
    switch (delta) {
      case BingoTypes.ROW: borderType = "row";
      break;
      case BingoTypes.COLUMN: borderType = "column";
      break;
      case BingoTypes.TL_BR:
      case BingoTypes.TR_BL:
        borderType = "diagonal";
      break;
    }

    var borderColor;
    switch (newStatus) {
      case BingoStates.GOAL_MET:
        borderColor = "captured";
      break;
      case BingoStates.GOAL_CANNOT_BE_MET_ANYMORE:
        borderColor = "restricted";
      break;
    }

    outlineName = "#" + borderType + "-" + borderColor;
  }

  return outlineName;
}

function getBorderId(delta, player, tileNumber) {
  if (PlayerType.NONE == player) {
    return -1; // this should never happen
  }
  switch (delta) {
    case BingoTypes.COLUMN:
      column = 1 + (tileNumber % BingoSize);
      if (PlayerType.P1 == player) {
        return (1 * BingoTypes.TL_BR) + column;
      } else { // PlayerType.P2
        return (4 * BingoTypes.TL_BR) - column;
      }
    case BingoTypes.ROW:
      row = 1 + Math.floor(tileNumber / BingoSize);
      if (PlayerType.P1 == player) {
        return (1 * BingoTypes.TL_BR) - row;
      } else { // PlayerType.P2
        return (2 * BingoTypes.TL_BR) + row;
      }
    case BingoTypes.TR_BL:
      if (PlayerType.P1 == player) {
        return (0 * BingoTypes.TL_BR);
      } else { // PlayerType.P2
        return (2 * BingoTypes.TL_BR);
      }
    case BingoTypes.TL_BR:
      if (PlayerType.P1 == player) {
        return (1 * BingoTypes.TL_BR);
      } else { // PlayerType.P2
        return (3 * BingoTypes.TL_BR);
      }
  }
}

function updateBorderOutline (borderId, outlineName) {
  var borderTile = document.getElementById("border-" + borderId);
  var parentDiv = borderTile.children.namedItem("parent-div");
  var svgsDiv = parentDiv.children.namedItem("outline-div");
  var svg = svgsDiv.children.namedItem("outline");
  var outline = svg.children.namedItem("bingo");

  var svgHref = document.createAttribute("href");
  svgHref.value = outlineName;
  outline.attributes.setNamedItem(svgHref);
}

function importBingoCard(textArray) {
  var counter = 0;
  while (counter < (BingoSize * BingoSize)) {
    var text = textArray[counter];
    var workingTile = document.getElementById("tile-" + counter);
    counter++;

    var parentDiv = workingTile.children.namedItem("parent-div");
    var textDiv = parentDiv.children.namedItem("text-div");
    textDiv.textContent = text;
  }
}
