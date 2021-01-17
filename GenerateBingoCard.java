// The absense of a package declaration, and all the static methods. Yikes.

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.StringBuffer;

public class GenerateBingoCard {
  private static final int BINGO_SIZE = 5;

  private static final int STROKE_WIDTH_HALF = 2;
  private static final int STROKE_WIDTH_FULL = 2 * STROKE_WIDTH_HALF;

  private static final int TILE_CELL_QRTR = 32;
  private static final int TILE_CELL_HALF = 2 * TILE_CELL_QRTR;
  private static final int TILE_CELL_FULL = 2 * TILE_CELL_HALF;
  private static final int TILE_CELL_SQRT = 3 * TILE_CELL_HALF; // 1.50x, which is greater than the needed 1.41x

  private static final long COLOR_BACKGROUND = 0x505050;
  private static final long COLOR_BINGO_TILE = 0x000000;
  private static final long COLOR_BINGO_TILE_TEXT = 0xC0A000;
  private static final long COLOR_P1 = 0x007030;
  private static final long COLOR_P2 = 0x500070;
  private static final long COLOR_RESTRICTED = 0xE00000;

  private static final String HIGHLIGHT_OPACITY = "0.375";

  /*
   * I used Java to write out the HTML/CSS/JS because I got tired of making the same
   * edit multiple times for each column/row. There is a better way to template this
   */
  public static void main(String[] args) throws IOException {
    StringBuffer cssOutput = new StringBuffer();
    generateCss(cssOutput);
    writeToFile(cssOutput, "bingo-card.css");

    StringBuffer jsOutput = new StringBuffer();
    generateJs(jsOutput);
    writeToFile(jsOutput, "bingo-card.js");

    StringBuffer htmlOutput = new StringBuffer();
    generateHtml(htmlOutput);
    writeToFile(htmlOutput, "bingo-card.html");
  }
  private static void writeToFile(StringBuffer content, String filename) throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(filename));
    out.write(content.toString());
    out.flush();
    out.close();
  }


  private static void generateCss(StringBuffer cssOutput) {
    generateCommonCss(cssOutput);
    cssOutput.append("\n");
    generateBorderTileCss(cssOutput);
    cssOutput.append("\n");
    generateBingoTileCss(cssOutput);
  }
  private static void generateCommonCss(StringBuffer cssOutput) {
    cssOutput.append("body {\n");
    cssOutput.append("  background-color: #" + String.format("%06X", COLOR_BACKGROUND) + ";\n");
    cssOutput.append("}\n");
    cssOutput.append(".child-div {\n");
    cssOutput.append("  position: absolute;\n");
    cssOutput.append("}\n");
    cssOutput.append(".not-visible {\n");
    cssOutput.append("  display: none;\n");
    cssOutput.append("}\n");
    cssOutput.append(".outline {\n");
    cssOutput.append("  fill-opacity: 0.0;\n");
    cssOutput.append("  stroke-opacity: 1.0;\n");
    cssOutput.append("  stroke-width: " + STROKE_WIDTH_FULL + ";\n");
    cssOutput.append("}\n");
    cssOutput.append("\n");
    cssOutput.append(".captured-p1 {\n");
    cssOutput.append("  stroke: #" + String.format("%06X", COLOR_P1) + ";\n");
    cssOutput.append("}\n");
    cssOutput.append(".captured-p2 {\n");
    cssOutput.append("  stroke: #" + String.format("%06X", COLOR_P2) + ";\n");
    cssOutput.append("}\n");
    cssOutput.append(".required {\n");
    cssOutput.append("  stroke: #" + String.format("%06X", COLOR_BINGO_TILE) + ";\n");
    cssOutput.append("}\n");
    cssOutput.append(".restricted {\n");
    cssOutput.append("  stroke: #" + String.format("%06X", COLOR_RESTRICTED) + ";\n");
    cssOutput.append("}\n");
  }

  private static void generateBorderTileCss(StringBuffer cssOutput) {
    generateBorderTileDefinitions(cssOutput);
    cssOutput.append("\n");
    generateBorderTilePlayersDefault(cssOutput);
    cssOutput.append("\n");
    generateBorderTilePlayersRequired(cssOutput);
  }
  private static void generateBorderTileDefinitions(StringBuffer cssOutput) {
    cssOutput.append(".border-column {\n");
    cssOutput.append("  height: " + TILE_CELL_QRTR + "px;\n");
    cssOutput.append("  width: " + TILE_CELL_FULL + "px;\n");
    cssOutput.append("}\n");
    cssOutput.append(".border-diagonal {\n");
    cssOutput.append("  height: " + TILE_CELL_QRTR + "px;\n");
    cssOutput.append("  width: " + TILE_CELL_QRTR + "px;\n");
    cssOutput.append("}\n");
    cssOutput.append(".border-row {\n");
    cssOutput.append("  height: " + TILE_CELL_FULL + "px;\n");
    cssOutput.append("  width: " + TILE_CELL_QRTR + "px;\n");
    cssOutput.append("}\n");
  }
  private static void generateBorderTilePlayersDefault(StringBuffer cssOutput) {
    cssOutput.append(".border-default-p1 {\n");
    cssOutput.append(String.format("  background: #%06X;\n", COLOR_P1));
    cssOutput.append("}\n");
    cssOutput.append(".border-default-p2 {\n");
    cssOutput.append(String.format("  background: #%06X;\n", COLOR_P2));
    cssOutput.append("}\n");
  }
  private static void generateBorderTilePlayersRequired(StringBuffer cssOutput) {
    cssOutput.append(".border-required-p1 {\n");
    cssOutput.append(String.format("  background: radial-gradient(circle at center, #%06X 0px, #%06X 4px, #%06X 7px, #%06X 10px, #%06X 100%%);\n",
      COLOR_P1, COLOR_P1, COLOR_BINGO_TILE, COLOR_P1, COLOR_P1));
    cssOutput.append("}\n");
    cssOutput.append(".border-required-p2 {\n");
    cssOutput.append(String.format("  background: radial-gradient(circle at center, #%06X 0px, #%06X 4px, #%06X 7px, #%06X 10px, #%06X 100%%);\n",
      COLOR_P2, COLOR_P2, COLOR_BINGO_TILE, COLOR_P2, COLOR_P2));
    cssOutput.append("}\n");
  }

  private static void generateBingoTileCss(StringBuffer cssOutput) {
    cssOutput.append(".bingo-tile {\n");
    cssOutput.append("  height: " + TILE_CELL_FULL + "px;\n");
    cssOutput.append("  width: " + TILE_CELL_FULL + "px;\n");
    cssOutput.append("}\n");
    cssOutput.append("td {\n");
    cssOutput.append("  background-color: #" + String.format("%06X", COLOR_BINGO_TILE) + ";\n");
    cssOutput.append("}\n");
    cssOutput.append(".bingo-tile-text {\n");
    cssOutput.append("  color: #" + String.format("%06X", COLOR_BINGO_TILE_TEXT) + ";\n");
    cssOutput.append("  display: grid;\n");
    cssOutput.append("  place-items:center;\n");
    cssOutput.append("  text-align: center;\n");
    cssOutput.append("  user-select: none;\n");
    cssOutput.append("}\n");
  }


  private static void generateJs(StringBuffer jsOutput) {
    generateJsCommon(jsOutput);
    jsOutput.append("\n");
    generateJsFunctionsMouse(jsOutput);
    jsOutput.append("\n");
    generateJsFunctionsClick(jsOutput);
    jsOutput.append("\n");
    generateJsFunctionsMultiTile(jsOutput);
  }

  private static void generateJsCommon(StringBuffer jsOutput) {
    generateJsConstants(jsOutput);
    generateJsFunctionClickType(jsOutput);
    generateJsFunctionPlayerType(jsOutput);
  }
  private static void generateJsFunctionsMouse(StringBuffer jsOutput) {
    generateJsFunctionBorderTileMouse(jsOutput);
    generateJsFunctionBingoTileMouse(jsOutput);
    generateJsFunctionHighlightBingoTile(jsOutput);
  }
  private static void generateJsFunctionsClick(StringBuffer jsOutput) {
    generateJsFunctionBorderTileClick(jsOutput);
    generateJsFunctionBingoTileClick(jsOutput);
    generateJsFunctionAdvanceTileState(jsOutput);
  }
  private static void generateJsFunctionsMultiTile(StringBuffer jsOutput) {
    generateJsFunctionUpdateBingos(jsOutput);
    generateJsFunctionUpdateBingo(jsOutput);
    jsOutput.append("\n");
    generateJsFunctionNewBorderStatus(jsOutput);
    generateJsFunctionBingoStatus(jsOutput);
    jsOutput.append("\n");
    generateJsFunctionOutlineName(jsOutput);
    jsOutput.append("\n");
    generateJsFunctionBorderId(jsOutput);
    jsOutput.append("\n");
    generateJsFunctionUpdateBorderOutline(jsOutput);
    jsOutput.append("\n");
    generateJsFunctionImportCard(jsOutput);
  }

  private static void generateJsConstants(StringBuffer jsOutput) {
    final String BINGO_SIZE_JS = String.format("const BingoSize = %d;\n", BINGO_SIZE);
    final String BINGO_TYPES = String.format("const BingoTypes = { COLUMN: %d, ROW: 1, TL_BR: %d, TR_BL: %d };\n", BINGO_SIZE, (BINGO_SIZE + 1), (BINGO_SIZE - 1));
    final String DEFAULT = getRgba(COLOR_BINGO_TILE, "1");
    final String P1_HIGHLIGHT = getRgba(COLOR_P1, HIGHLIGHT_OPACITY);
    final String P2_HIGHLIGHT = getRgba(COLOR_P2, HIGHLIGHT_OPACITY);

    final String P1_COLORS = String.format("const Player1Colors = { DEFAULT : \"%s\", HIGHLIGHT : \"%s\" };\n", DEFAULT, P1_HIGHLIGHT);
    final String P2_COLORS = String.format("const Player2Colors = { DEFAULT : \"%s\", HIGHLIGHT : \"%s\" };\n", DEFAULT, P2_HIGHLIGHT);

    jsOutput.append(BINGO_SIZE_JS);
    jsOutput.append("Object.freeze(BingoSize);\n");
    jsOutput.append("const BingoStates = { GOAL_NOT_MET : \"0\", GOAL_MET : \"1\", GOAL_CANNOT_BE_MET_ANYMORE : \"2\" };\n");
    jsOutput.append("Object.freeze(BingoStates);\n");
    jsOutput.append(BINGO_TYPES);
    jsOutput.append("Object.freeze(BingoTypes);\n");
    jsOutput.append("\n");
    jsOutput.append("const ClickType = { NORMAL : \"0\", SHIFT : \"1\", CONTROL : \"2\" };\n");
    jsOutput.append("Object.freeze(ClickType);\n");
    jsOutput.append("\n");
    jsOutput.append("const PlayerType = { NONE : \"0\", P1 : \"1\", P2 : \"2\" };\n");
    jsOutput.append("Object.freeze(PlayerType);\n");
    jsOutput.append(P1_COLORS);
    jsOutput.append("Object.freeze(Player1Colors);\n");
    jsOutput.append(P2_COLORS);
    jsOutput.append("Object.freeze(Player2Colors);\n");
  }
  private static String getRgba(long rgb, String alpha) {
    int r = (int)((rgb & 0xFF0000) >> 16);
    int g = (int)((rgb & 0x00FF00) >> 8);
    int b = (int)((rgb & 0x0000FF));

    return String.format("rgba(%d, %d, %d, %s)", r, g, b, alpha);
  }
  private static void generateJsFunctionClickType(StringBuffer jsOutput) {
    jsOutput.append("function classifyClick(event) {\n");
    jsOutput.append("  if (event.ctrlKey) {\n");
    jsOutput.append("    return ClickType.CONTROL;\n");
    jsOutput.append("  }\n");
    jsOutput.append("\n");
    jsOutput.append("  if (event.shiftKey) {\n");
    jsOutput.append("    return ClickType.SHIFT;\n");
    jsOutput.append("  }\n");
    jsOutput.append("\n");
    jsOutput.append("  return ClickType.NORMAL;\n");
    jsOutput.append("}\n");
  }
  private static void generateJsFunctionPlayerType(StringBuffer jsOutput) {
    jsOutput.append("function classifyPlayer(element, event) {\n");
    jsOutput.append("  relativeX = event.clientX - element.getBoundingClientRect().left;\n");
    jsOutput.append("  relativeY = event.clientY - element.getBoundingClientRect().top;\n");
    jsOutput.append("\n");
    jsOutput.append("  sum = relativeX + relativeY;\n");
    jsOutput.append("  withinX = (0 < relativeX) && (relativeX <= " + TILE_CELL_FULL + ");\n");
    jsOutput.append("  withinY = (0 < relativeY) && (relativeY <= " + TILE_CELL_FULL + ");\n");
    jsOutput.append("  player1 = (sum < " + TILE_CELL_FULL + ") && withinX && withinY;\n");
    jsOutput.append("  player2 = (sum > " + TILE_CELL_FULL + ") && withinX && withinY;\n");
    jsOutput.append("\n");
    jsOutput.append("  if (player1) {\n");
    jsOutput.append("    return PlayerType.P1;\n");
    jsOutput.append("  } else if (player2) {\n");
    jsOutput.append("    return PlayerType.P2;\n");
    jsOutput.append("  } else {\n");
    jsOutput.append("    return PlayerType.NONE;\n");
    jsOutput.append("  }\n");
    jsOutput.append("}\n");
  }


  private static void generateJsFunctionBorderTileMouse(StringBuffer jsOutput) {
    jsOutput.append("function mouseBorderTile(element, highlight) {\n");
    jsOutput.append("  var borderId = element.id;\n");
    jsOutput.append("  var borderNumber = parseInt(borderId.substring(\"border-\".length));\n");
    jsOutput.append("\n");
    jsOutput.append("  var playerType;\n");
    jsOutput.append("  if (borderNumber < (2 * BingoTypes.TL_BR)) {\n");
    jsOutput.append("    playerType = PlayerType.P1;\n");
    jsOutput.append("  } else {\n");
    jsOutput.append("    playerType = PlayerType.P2;\n");
    jsOutput.append("  }\n");
    jsOutput.append("\n");
    jsOutput.append("  var modDiagonal = borderNumber % BingoTypes.TL_BR;\n");
    jsOutput.append("  var divDiagonal = Math.floor(borderNumber / BingoTypes.TL_BR);\n");
    jsOutput.append("\n");
    jsOutput.append("  var start;\n");
    jsOutput.append("  var delta;\n");
    jsOutput.append("  if (modDiagonal == 0) {\n");
    jsOutput.append("    if ((divDiagonal % 2) == 0) {\n");
    jsOutput.append("      delta = BingoTypes.TR_BL;\n");
    jsOutput.append("      start = BingoTypes.TR_BL;\n");
    jsOutput.append("    } else {\n");
    jsOutput.append("      delta = BingoTypes.TL_BR;\n");
    jsOutput.append("      start = 0;\n");
    jsOutput.append("    }\n");
    jsOutput.append("  } else {\n");
    jsOutput.append("    if ((divDiagonal % 2) == 0) {\n");
    jsOutput.append("      delta = BingoTypes.ROW;\n");
    jsOutput.append("      if (playerType == PlayerType.P1) {\n");
    jsOutput.append("        start = (BingoTypes.COLUMN - modDiagonal) * BingoTypes.COLUMN;\n");
    jsOutput.append("      } else { // PlayerType.P2\n");
    jsOutput.append("        start = (modDiagonal - BingoTypes.ROW) * BingoTypes.COLUMN;\n");
    jsOutput.append("      }\n");
    jsOutput.append("    } else {\n");
    jsOutput.append("      delta = BingoTypes.COLUMN;\n");
    jsOutput.append("      if (playerType == PlayerType.P1) {\n");
    jsOutput.append("        start = (modDiagonal - BingoTypes.ROW) * BingoTypes.ROW;\n");
    jsOutput.append("      } else { // PlayerType.P2\n");
    jsOutput.append("        start = (BingoTypes.COLUMN - modDiagonal) * BingoTypes.ROW;\n");
    jsOutput.append("      }\n");
    jsOutput.append("    }\n");
    jsOutput.append("  }\n");
    jsOutput.append("\n");
    jsOutput.append("  if (!highlight) {\n");
    jsOutput.append("    playerType = PlayerType.NONE;\n");
    jsOutput.append("  }\n");
    jsOutput.append("\n");
    jsOutput.append("  var counter = 0;\n");
    jsOutput.append("  var tileNumberLoop = start;\n");
    jsOutput.append("  while (counter < BingoSize) {\n");
    jsOutput.append("    var workingTile = document.getElementById(\"tile-\" + tileNumberLoop);\n");
    jsOutput.append("    highlightBingoTile(workingTile, playerType);\n");
    jsOutput.append("\n");
    jsOutput.append("    counter++;\n");
    jsOutput.append("    tileNumberLoop += delta;\n");
    jsOutput.append("  }\n");
    jsOutput.append("}\n");
  }
  private static void generateJsFunctionBingoTileMouse(StringBuffer jsOutput) {
    jsOutput.append("function mouseBingoTile(element, event) {\n");
    jsOutput.append("  var playerType = classifyPlayer(element, event);\n");
    jsOutput.append("  highlightBingoTile(element, playerType);\n");
    jsOutput.append("}\n");
  }
  private static void generateJsFunctionHighlightBingoTile(StringBuffer jsOutput) {
    final int HALF_45_P1 = (int)(TILE_CELL_HALF * Math.sqrt(2));
    final int HALF_45_P2 = (int)(TILE_CELL_FULL * Math.sqrt(2)) - HALF_45_P1;
    final String linearGradient = String.format("  var tileBackground = \"linear-gradient(135deg, \" + colorP1 + \" 0px, \" + colorP1 + \" %dpx, #%06X %dpx, #%06X %dpx, \" + colorP2 + \" %dpx, \" + colorP2 + \" %dpx)\";\n",
    (HALF_45_P1), COLOR_BINGO_TILE, (HALF_45_P1 + 1), COLOR_BINGO_TILE, (HALF_45_P2 + 2), (HALF_45_P2 + 3), TILE_CELL_SQRT); // These offsets of +1 +2 etc. are just what looks best to me

    jsOutput.append("function highlightBingoTile(element, playerType) {\n");
    jsOutput.append("  var colorP1 = Player1Colors.DEFAULT;\n");
    jsOutput.append("  var colorP2 = Player2Colors.DEFAULT;\n");
    jsOutput.append("\n");
    jsOutput.append("  if (playerType == PlayerType.P1) {\n");
    jsOutput.append("    colorP1 = Player1Colors.HIGHLIGHT;\n");
    jsOutput.append("  } else if (playerType == PlayerType.P2) {\n");
    jsOutput.append("    colorP2 = Player2Colors.HIGHLIGHT;\n");
    jsOutput.append("  }\n");
    jsOutput.append("\n");
    jsOutput.append(linearGradient);
    jsOutput.append("  element.style.background = tileBackground;\n");
    jsOutput.append("}\n");
  }

  private static void generateJsFunctionBorderTileClick(StringBuffer jsOutput) {
    jsOutput.append("function clickBorderTile(element, player) {\n");
    jsOutput.append("  var classList = element.classList;\n");
    jsOutput.append("  var parentDiv = element.children.namedItem(\"parent-div\");\n");
    jsOutput.append("\n");
    jsOutput.append("  var stateDiv = parentDiv.children.namedItem(\"state-div\");\n");
    jsOutput.append("  var form = stateDiv.children.namedItem(\"form\");\n");
    jsOutput.append("  var required = form.elements[\"required\"];\n");
    jsOutput.append("\n");
    jsOutput.append("  if (required.value == \"0\") {\n");
    jsOutput.append("    classList.remove(\"border-default-\" + player);\n");
    jsOutput.append("    classList.add(\"border-required-\" + player);\n");
    jsOutput.append("    required.value = \"1\";\n");
    jsOutput.append("  } else {\n");
    jsOutput.append("    classList.remove(\"border-required-\" + player);\n");
    jsOutput.append("    classList.add(\"border-default-\" + player);\n");
    jsOutput.append("    required.value = \"0\";\n");
    jsOutput.append("  }\n");
    jsOutput.append("}\n");
  }
  private static void generateJsFunctionBingoTileClick(StringBuffer jsOutput) {
    jsOutput.append("function clickBingoTile(element, event) {\n");
    jsOutput.append("  var parentDiv = element.children.namedItem(\"parent-div\");\n");
    jsOutput.append("  var stateDiv = parentDiv.children.namedItem(\"state-div\");\n");
    jsOutput.append("  var svgsDiv = parentDiv.children.namedItem(\"outlines-div\");\n");
    jsOutput.append("\n");
    jsOutput.append("  var form = stateDiv.children.namedItem(\"form\");\n");
    jsOutput.append("  var svg = svgsDiv.children.namedItem(\"outlines\");\n");
    jsOutput.append("\n");
    jsOutput.append("  var clickType = classifyClick(event);\n");
    jsOutput.append("  var playerType = classifyPlayer(element, event);\n");
    jsOutput.append("  switch (playerType) {\n");
    jsOutput.append("    case PlayerType.P1:\n");
    jsOutput.append("      var state = form.elements[\"player1\"];\n");
    jsOutput.append("      var outline = svg.children.namedItem(\"player1\");\n");
    jsOutput.append("      advanceTileState(clickType, state, true, outline);\n");
    jsOutput.append("    break;\n");
    jsOutput.append("    case PlayerType.P2:\n");
    jsOutput.append("      var state = form.elements[\"player2\"];\n");
    jsOutput.append("      var outline = svg.children.namedItem(\"player2\");\n");
    jsOutput.append("      advanceTileState(clickType, state, false, outline);\n");
    jsOutput.append("    break;\n");
    jsOutput.append("    case PlayerType.NONE:\n");
    jsOutput.append("    break;\n");
    jsOutput.append("  }\n");
    jsOutput.append("\n");
    jsOutput.append("  updateBingos(element.id, playerType);\n");
    jsOutput.append("}\n");
  }
  private static void generateJsFunctionAdvanceTileState(StringBuffer jsOutput) {
    jsOutput.append("function advanceTileState(clickType, state, isPlayer1, outline) {\n");
    jsOutput.append("  var svgHref = document.createAttribute(\"href\");\n");
    jsOutput.append("\n");
    jsOutput.append("  switch (clickType) {\n");
    jsOutput.append("    case ClickType.NORMAL:\n");
    jsOutput.append("      switch (state.value) {\n");
    jsOutput.append("        case BingoStates.GOAL_NOT_MET:\n");
    jsOutput.append("          state.value = BingoStates.GOAL_MET;\n");
    jsOutput.append("          svgHref.value = (player1 ? \"#p1-goal-met\" : \"#p2-goal-met\");\n");
    jsOutput.append("        break;\n");
    jsOutput.append("        case BingoStates.GOAL_MET:\n");
    jsOutput.append("          state.value = BingoStates.GOAL_CANNOT_BE_MET_ANYMORE;\n");
    jsOutput.append("          svgHref.value = (player1 ? \"#p1-goal-cannot-be-met\" : \"#p2-goal-cannot-be-met\");\n");
    jsOutput.append("        break;\n");
    jsOutput.append("        case BingoStates.GOAL_CANNOT_BE_MET_ANYMORE:\n");
    jsOutput.append("          state.value = BingoStates.GOAL_NOT_MET;\n");
    jsOutput.append("          svgHref.value = \"#empty\";\n");
    jsOutput.append("        break;\n");
    jsOutput.append("      }\n");
    jsOutput.append("    break;\n");
    jsOutput.append("    case ClickType.CONTROL:\n");
    jsOutput.append("      if (state.value == BingoStates.GOAL_MET) {\n");
    jsOutput.append("        state.value = BingoStates.GOAL_NOT_MET;\n");
    jsOutput.append("        svgHref.value = \"#empty\";\n");
    jsOutput.append("      } else {\n");
    jsOutput.append("        state.value = BingoStates.GOAL_MET;\n");
    jsOutput.append("        svgHref.value = (player1 ? \"#p1-goal-met\" : \"#p2-goal-met\");\n");
    jsOutput.append("      }\n");
    jsOutput.append("    break;\n");
    jsOutput.append("    case ClickType.SHIFT:\n");
    jsOutput.append("      if (state.value == BingoStates.GOAL_CANNOT_BE_MET_ANYMORE) {\n");
    jsOutput.append("        state.value = BingoStates.GOAL_NOT_MET;\n");
    jsOutput.append("        svgHref.value = \"#empty\";\n");
    jsOutput.append("      } else {\n");
    jsOutput.append("        state.value = BingoStates.GOAL_CANNOT_BE_MET_ANYMORE;\n");
    jsOutput.append("        svgHref.value = (player1 ? \"#p1-goal-cannot-be-met\" : \"#p2-goal-cannot-be-met\");\n");
    jsOutput.append("      }\n");
    jsOutput.append("    break;\n");
    jsOutput.append("  }\n");
    jsOutput.append("\n");
    jsOutput.append("  outline.attributes.setNamedItem(svgHref);\n");
    jsOutput.append("}\n");
  }

  private static void generateJsFunctionUpdateBingos(StringBuffer jsOutput) {
    jsOutput.append("function updateBingos(tileId, player) {\n");
    jsOutput.append("  tileNumber = parseInt(tileId.substring(\"tile-\".length));\n");
    jsOutput.append("  column = tileNumber % BingoTypes.COLUMN;\n");
    jsOutput.append("  row = Math.floor(tileNumber / BingoTypes.COLUMN);\n");
    jsOutput.append("\n");
    jsOutput.append("  updateBingo(BingoTypes.ROW * column, BingoTypes.COLUMN, player, tileNumber);\n");
    jsOutput.append("  updateBingo(BingoTypes.COLUMN * row, BingoTypes.ROW, player, tileNumber);\n");
    jsOutput.append("  if (row == column) {\n");
    jsOutput.append("    updateBingo(0, BingoTypes.TL_BR, player, tileNumber);\n");
    jsOutput.append("  }\n");
    jsOutput.append("  if ((row + column) == BingoTypes.TL_BR) {\n");
    jsOutput.append("    // row and column are each offset by 1, 1..5 instead of 0..4, so (BingoSize + 1) instead of (BingoSize - 1)\n");
    jsOutput.append("    updateBingo(BingoTypes.TR_BL, BingoTypes.TR_BL, player, tileNumber)\n");
    jsOutput.append("  }\n");
    jsOutput.append("}\n");
  }
  private static void generateJsFunctionUpdateBingo(StringBuffer jsOutput) {
    jsOutput.append("function updateBingo(start, delta, player, tileNumber) {\n");
    jsOutput.append("  var newStatus = getNewBorderStatus(start, delta, player);\n");
    jsOutput.append("  var outlineName = getOutlineName(newStatus, delta);\n");
    jsOutput.append("  var borderId = getBorderId(delta, player, tileNumber)\n");
    jsOutput.append("\n");
    jsOutput.append("  updateBorderOutline(borderId, outlineName);\n");
    jsOutput.append("}\n");
  }
  private static void generateJsFunctionNewBorderStatus(StringBuffer jsOutput) {
    jsOutput.append("function getNewBorderStatus (start, delta, player) {\n");
    jsOutput.append("  var notMet = false;\n");
    jsOutput.append("  var cannotBeMet = false;\n");
    jsOutput.append("\n");
    jsOutput.append("  var counter = 0;\n");
    jsOutput.append("  var tileNumberLoop = start;\n");
    jsOutput.append("  while ((counter < BingoSize) && (!notMet || !cannotBeMet)) {\n");
    jsOutput.append("    var workingTile = document.getElementById(\"tile-\" + tileNumberLoop);\n");
    jsOutput.append("    var status = getBingoStatus(workingTile, player)\n");
    jsOutput.append("    if (status == BingoStates.GOAL_CANNOT_BE_MET_ANYMORE) {\n");
    jsOutput.append("      cannotBeMet = true;\n");
    jsOutput.append("    } else if (status == BingoStates.GOAL_NOT_MET) {\n");
    jsOutput.append("      notMet = true;\n");
    jsOutput.append("    }\n");
    jsOutput.append("\n");
    jsOutput.append("    counter++;\n");
    jsOutput.append("    tileNumberLoop += delta;\n");
    jsOutput.append("  }\n");
    jsOutput.append("\n");
    jsOutput.append("  if (cannotBeMet) {\n");
    jsOutput.append("    return BingoStates.GOAL_CANNOT_BE_MET_ANYMORE;\n");
    jsOutput.append("  } else if (notMet) {\n");
    jsOutput.append("    return BingoStates.GOAL_NOT_MET;\n");
    jsOutput.append("  } else {\n");
    jsOutput.append("    return BingoStates.GOAL_MET;\n");
    jsOutput.append("  }\n");
    jsOutput.append("}\n");
  }
  private static void generateJsFunctionBingoStatus(StringBuffer jsOutput) {
    jsOutput.append("function getBingoStatus(element, player) {\n");
    jsOutput.append("  var parentDiv = element.children.namedItem(\"parent-div\");\n");
    jsOutput.append("  var stateDiv = parentDiv.children.namedItem(\"state-div\");\n");
    jsOutput.append("\n");
    jsOutput.append("  var form = stateDiv.children.namedItem(\"form\");\n");
    jsOutput.append("  switch (player) {\n");
    jsOutput.append("    case PlayerType.P1:\n");
    jsOutput.append("      return form.elements[\"player1\"].value;\n");
    jsOutput.append("    break;\n");
    jsOutput.append("    case PlayerType.P2:\n");
    jsOutput.append("      return form.elements[\"player2\"].value;\n");
    jsOutput.append("    break;\n");
    jsOutput.append("    case PlayerType.NONE:\n");
    jsOutput.append("      return -1;\n");
    jsOutput.append("  }\n");
    jsOutput.append("}\n");
  }
  private static void generateJsFunctionOutlineName(StringBuffer jsOutput) {
    jsOutput.append("function getOutlineName(newStatus, delta) {\n");
    jsOutput.append("  var outlineName = \"#empty\";\n");
    jsOutput.append("  if (newStatus != BingoStates.GOAL_NOT_MET) {\n");
    jsOutput.append("    var borderType;\n");
    jsOutput.append("    switch (delta) {\n");
    jsOutput.append("      case BingoTypes.ROW: borderType = \"row\";\n");
    jsOutput.append("      break;\n");
    jsOutput.append("      case BingoTypes.COLUMN: borderType = \"column\";\n");
    jsOutput.append("      break;\n");
    jsOutput.append("      case BingoTypes.TL_BR:\n");
    jsOutput.append("      case BingoTypes.TR_BL:\n");
    jsOutput.append("        borderType = \"diagonal\";\n");
    jsOutput.append("      break;\n");
    jsOutput.append("    }\n");
    jsOutput.append("\n");
    jsOutput.append("    var borderColor;\n");
    jsOutput.append("    switch (newStatus) {\n");
    jsOutput.append("      case BingoStates.GOAL_MET:\n");
    jsOutput.append("        borderColor = \"captured\";\n");
    jsOutput.append("      break;\n");
    jsOutput.append("      case BingoStates.GOAL_CANNOT_BE_MET_ANYMORE:\n");
    jsOutput.append("        borderColor = \"restricted\";\n");
    jsOutput.append("      break;\n");
    jsOutput.append("    }\n");
    jsOutput.append("\n");
    jsOutput.append("    outlineName = \"#\" + borderType + \"-\" + borderColor;\n");
    jsOutput.append("  }\n");
    jsOutput.append("\n");
    jsOutput.append("  return outlineName;\n");
    jsOutput.append("}\n");
  }
  private static void generateJsFunctionBorderId(StringBuffer jsOutput) {
    jsOutput.append("function getBorderId(delta, player, tileNumber) {\n");
    jsOutput.append("  if (PlayerType.NONE == player) {\n");
    jsOutput.append("    return -1; // this should never happen\n");
    jsOutput.append("  }\n");
    jsOutput.append("  switch (delta) {\n");
    jsOutput.append("    case BingoTypes.COLUMN:\n");
    jsOutput.append("      column = 1 + (tileNumber % BingoSize);\n");
    jsOutput.append("      if (PlayerType.P1 == player) {\n");
    jsOutput.append("        return (1 * BingoTypes.TL_BR) + column;\n");
    jsOutput.append("      } else { // PlayerType.P2\n");
    jsOutput.append("        return (4 * BingoTypes.TL_BR) - column;\n");
    jsOutput.append("      }\n");
    jsOutput.append("    case BingoTypes.ROW:\n");
    jsOutput.append("      row = 1 + Math.floor(tileNumber / BingoSize);\n");
    jsOutput.append("      if (PlayerType.P1 == player) {\n");
    jsOutput.append("        return (1 * BingoTypes.TL_BR) - row;\n");
    jsOutput.append("      } else { // PlayerType.P2\n");
    jsOutput.append("        return (2 * BingoTypes.TL_BR) + row;\n");
    jsOutput.append("      }\n");
    jsOutput.append("    case BingoTypes.TR_BL:\n");
    jsOutput.append("      if (PlayerType.P1 == player) {\n");
    jsOutput.append("        return (0 * BingoTypes.TL_BR);\n");
    jsOutput.append("      } else { // PlayerType.P2\n");
    jsOutput.append("        return (2 * BingoTypes.TL_BR);\n");
    jsOutput.append("      }\n");
    jsOutput.append("    case BingoTypes.TL_BR:\n");
    jsOutput.append("      if (PlayerType.P1 == player) {\n");
    jsOutput.append("        return (1 * BingoTypes.TL_BR);\n");
    jsOutput.append("      } else { // PlayerType.P2\n");
    jsOutput.append("        return (3 * BingoTypes.TL_BR);\n");
    jsOutput.append("      }\n");
    jsOutput.append("  }\n");
    jsOutput.append("}\n");
  }
  private static void generateJsFunctionUpdateBorderOutline(StringBuffer jsOutput) {
    jsOutput.append("function updateBorderOutline (borderId, outlineName) {\n");
    jsOutput.append("  var borderTile = document.getElementById(\"border-\" + borderId);\n");
    jsOutput.append("  var parentDiv = borderTile.children.namedItem(\"parent-div\");\n");
    jsOutput.append("  var svgsDiv = parentDiv.children.namedItem(\"outline-div\");\n");
    jsOutput.append("  var svg = svgsDiv.children.namedItem(\"outline\");\n");
    jsOutput.append("  var outline = svg.children.namedItem(\"bingo\");\n");
    jsOutput.append("\n");
    jsOutput.append("  var svgHref = document.createAttribute(\"href\");\n");
    jsOutput.append("  svgHref.value = outlineName;\n");
    jsOutput.append("  outline.attributes.setNamedItem(svgHref);\n");
    jsOutput.append("}\n");
  }

  private static void generateJsFunctionImportCard(StringBuffer jsOutput) {
    jsOutput.append("function importBingoCard(textArray) {\n");
    jsOutput.append("  var counter = 0;\n");
    jsOutput.append("  while (counter < (BingoSize * BingoSize)) {\n");
    jsOutput.append("    var text = textArray[counter];\n");
    jsOutput.append("    var workingTile = document.getElementById(\"tile-\" + counter);\n");
    jsOutput.append("    counter++;\n");
    jsOutput.append("\n");
    jsOutput.append("    var parentDiv = workingTile.children.namedItem(\"parent-div\");\n");
    jsOutput.append("    var textDiv = parentDiv.children.namedItem(\"text-div\");\n");
    jsOutput.append("    textDiv.textContent = text;\n");
    jsOutput.append("  }\n");
    jsOutput.append("}\n");
  }


  private static void generateHtml(StringBuffer htmlOutput) {
    htmlOutput.append("<!DOCTYPE html>\n");
    htmlOutput.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">\n");
    htmlOutput.append("  <head>\n");
    htmlOutput.append("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n");
    htmlOutput.append("    <title>Advanced Bingo Card</title>\n");
    htmlOutput.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"bingo-card.css\">\n");
    htmlOutput.append("    <script src=\"bingo-card.js\"></script>\n");
    htmlOutput.append("  </head>\n");
    htmlOutput.append("  <body>\n");
    generateBody(htmlOutput);
    htmlOutput.append("  </body>\n");
    htmlOutput.append("</html>\n");
  }
  private static void generateBody(StringBuffer htmlOutput) {
    generateSvgDefs(htmlOutput);
    htmlOutput.append("\n");
    htmlOutput.append("    <table>\n");

    for (int row = 0; row < (1 + BINGO_SIZE + 1); row++) {
      htmlOutput.append("      <tr>\n");
      for (int col = 0; col < (1 + BINGO_SIZE + 1); col++) {
        generateBingoTableCell(htmlOutput, row, col);
      }
      htmlOutput.append("      </tr>\n");
    }

    htmlOutput.append("    </table>\n");
  }
  private static void generateSvgDefs(StringBuffer htmlOutput) {
    final String INDENT = "    ";

    final int BORDER_NONE = STROKE_WIDTH_HALF;
    final int BORDER_QRTR = (TILE_CELL_QRTR - STROKE_WIDTH_HALF);
    final int BORDER_FULL = (TILE_CELL_FULL - STROKE_WIDTH_HALF);

    final String POINTS_COLUMN = String.format("%d,%d %d,%d %d,%d %d,%d", BORDER_NONE,BORDER_NONE, BORDER_FULL,BORDER_NONE, BORDER_FULL,BORDER_QRTR, BORDER_NONE,BORDER_QRTR);
    final String BORDER_DIAGONAL = String.format("%d,%d %d,%d %d,%d %d,%d", BORDER_NONE,BORDER_NONE, BORDER_QRTR,BORDER_NONE, BORDER_QRTR,BORDER_QRTR, BORDER_NONE,BORDER_QRTR);
    final String POINTS_ROW = String.format("%d,%d %d,%d %d,%d %d,%d", BORDER_NONE,BORDER_NONE, BORDER_QRTR,BORDER_NONE, BORDER_QRTR,BORDER_FULL, BORDER_NONE,BORDER_FULL);

    final int BINGO_P1_NONE = STROKE_WIDTH_HALF;
    final int BINGO_P1_HALF = (TILE_CELL_HALF - STROKE_WIDTH_HALF);
    final int BINGO_P1_FULL = (TILE_CELL_FULL - (STROKE_WIDTH_FULL + STROKE_WIDTH_HALF));

    final int BINGO_P2_NONE = (TILE_CELL_FULL - STROKE_WIDTH_HALF);
    final int BINGO_P2_HALF = (TILE_CELL_HALF + STROKE_WIDTH_HALF);
    final int BINGO_P2_FULL = (STROKE_WIDTH_FULL + STROKE_WIDTH_HALF);

    final String POINTS_P1_CAPTURED = String.format("%d,%d %d,%d %d,%d", BINGO_P1_NONE,BINGO_P1_NONE, BINGO_P1_FULL,BINGO_P1_NONE, BINGO_P1_NONE,BINGO_P1_FULL);
    final String POINTS_P2_CAPTURED = String.format("%d,%d %d,%d %d,%d", BINGO_P2_NONE,BINGO_P2_NONE, BINGO_P2_FULL,BINGO_P2_NONE, BINGO_P2_NONE,BINGO_P2_FULL);

    /*
     * Use path rather than polygon for the restricted ones because of a drawing quirk
     * The P2 restricted looked weird when the angled line is drawn after the triangle
     */
    final int PATH_DELTA_HALF = TILE_CELL_HALF - STROKE_WIDTH_FULL;
    final int PATH_DELTA_FULL = 2 * PATH_DELTA_HALF;

    final int PATH_P1_NONE = STROKE_WIDTH_HALF;
    final int PATH_P1_FULL = PATH_P1_NONE + PATH_DELTA_FULL;

    final int PATH_P2_NONE = TILE_CELL_FULL - PATH_P1_NONE;
    final int PATH_P2_FULL = PATH_P2_NONE - PATH_DELTA_FULL;

    final String PATH_P1_RESTRICTED = String.format("M%d,%d l%d,%d M%d,%d h%d v%d Z", PATH_P1_NONE, PATH_P1_NONE,  PATH_DELTA_HALF,  PATH_DELTA_HALF, PATH_P1_FULL, PATH_P1_NONE, -PATH_DELTA_FULL,  PATH_DELTA_FULL);
    final String PATH_P2_RESTRICTED = String.format("M%d,%d l%d,%d M%d,%d h%d v%d Z", PATH_P2_NONE, PATH_P2_NONE, -PATH_DELTA_HALF, -PATH_DELTA_HALF, PATH_P2_FULL, PATH_P2_NONE,  PATH_DELTA_FULL, -PATH_DELTA_FULL);

    htmlOutput.append(INDENT + "<svg class=\"not-visible\">\n");
    htmlOutput.append(INDENT + "  <defs>\n");
    htmlOutput.append(INDENT + "    <polygon id=\"empty\" points=\"\" />\n");
    htmlOutput.append("\n");
    htmlOutput.append(INDENT + "    <polygon id=\"column-captured\" class=\"border-column outline required\" points=\"" + POINTS_COLUMN +"\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"column-restricted\" class=\"border-column outline restricted\" points=\"" + POINTS_COLUMN +"\" />\n");
    htmlOutput.append("\n");
    htmlOutput.append(INDENT + "    <polygon id=\"diagonal-captured\" class=\"border-diagonal outline required\" points=\"" + BORDER_DIAGONAL +"\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"diagonal-restricted\" class=\"border-diagonal outline restricted\" points=\"" + BORDER_DIAGONAL +"\" />\n");
    htmlOutput.append("\n");
    htmlOutput.append(INDENT + "    <polygon id=\"row-captured\" class=\"border-row outline required\" points=\"" + POINTS_ROW + "\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"row-restricted\" class=\"border-row outline restricted\" points=\"" + POINTS_ROW + "\" />\n");
    htmlOutput.append("\n");
    htmlOutput.append(INDENT + "    <polygon id=\"p1-goal-met\" class=\"bingo-tile outline captured-p1\" points=\"" + POINTS_P1_CAPTURED + "\" />\n");
    htmlOutput.append(INDENT + "    <path id=\"p1-goal-cannot-be-met\" class=\"bingo-tile outline restricted\" d=\"" + PATH_P1_RESTRICTED + "\" />\n");
    htmlOutput.append("\n");
    htmlOutput.append(INDENT + "    <polygon id=\"p2-goal-met\" class=\"bingo-tile outline captured-p2\" points=\"" + POINTS_P2_CAPTURED + "\" />\n");
    htmlOutput.append(INDENT + "    <path id=\"p2-goal-cannot-be-met\" class=\"bingo-tile outline restricted\" d=\"" + PATH_P2_RESTRICTED + "\" />\n");
    htmlOutput.append(INDENT + "  </defs>\n");
    htmlOutput.append(INDENT + "</svg>\n");
  }
  private static void generateBingoTableCell(StringBuffer htmlOutput, int row, int col) {
    int divider = (BINGO_SIZE + 1);
    boolean isColumnBorder = ((col % divider) == 0);
    boolean isRowBorder = ((row % divider) == 0);

    int borderId = borderId(row, col, divider);
    char player = player(borderId, divider);

    if (isRowBorder) {
      if (isColumnBorder) {
        generateBorderTile(htmlOutput, borderId, "diagonal", player);
      } else {
        generateBorderTile(htmlOutput, borderId, "column", player);
      }
    } else {
      if (isColumnBorder) {
        generateBorderTile(htmlOutput, borderId, "row", player);
      } else {
        generateBingoTile(htmlOutput, row, col);
      }
    }
  }
  private static int borderId(int row, int col, int divider) {
    int borderId;

    boolean player1 = ((col == 0) || (row == 0 && col != divider));
    if (player1) {
      if (col == 0) {
        borderId = divider - row;
      } else {
        borderId = divider + col;
      }
    } else {
      if (col == divider) {
        borderId = (2 * divider) + row;
      } else {
        borderId = (4 * divider) - col;
      }
    }

    return borderId;
  }
  private static char player(int borderId, int divider) {
    boolean player1 = (borderId < (2 * divider));
    return (player1 ? '1' : '2');
  }

  private static void generateBorderTile(StringBuffer htmlOutput, int borderId, String borderType, char player) {
    String th = String.format("<th id=\"border-%d\" class=\"border-%s border-default-p%c\" onclick=\"clickBorderTile(this, 'p%c')\" onmouseenter=\"mouseBorderTile(this, true)\" onmouseleave=\"mouseBorderTile(this, false)\">\n",
      borderId, borderType, player, player);
    String parentDiv = String.format("  <div id=\"parent-div\" class=\"border-%s\">\n", borderType);
    String stateDiv = String.format("    <div id=\"state-div\"  class=\"border-%s child-div\">\n", borderType);
    String outlinesDiv = String.format("    <div id=\"outline-div\" class=\"border-%s child-div\">\n", borderType);

    int width = TILE_CELL_QRTR;
    int height = TILE_CELL_QRTR;
    if ("column".equals(borderType)) {
      width = TILE_CELL_FULL;
    } else if ("row".equals(borderType)) {
      height = TILE_CELL_FULL;
    }
    String svg = String.format("      <svg id=\"outline\" width=\"%dpx\" height=\"%dpx\">\n", width, height);

    final String INDENT = "          ";
    htmlOutput.append(INDENT + th);
    htmlOutput.append(INDENT + parentDiv);
    htmlOutput.append(INDENT + stateDiv);
    htmlOutput.append(INDENT + "      <form id=\"form\"  class=\"not-visible\">\n");
    htmlOutput.append(INDENT + "        <input id=\"required\" type=\"hidden\" value=\"0\" />\n");
    htmlOutput.append(INDENT + "      </form>\n");
    htmlOutput.append(INDENT + "    </div>\n");
    htmlOutput.append(INDENT + outlinesDiv);
    htmlOutput.append(INDENT + svg);
    htmlOutput.append(INDENT + "        <use id=\"bingo\" href=\"#empty\" />\n");
    htmlOutput.append(INDENT + "      </svg>\n");
    htmlOutput.append(INDENT + "    </div>\n");
    htmlOutput.append(INDENT + "  </div>\n");
    htmlOutput.append(INDENT + "</th>\n");
  }

  private static void generateBingoTile(StringBuffer htmlOutput, int row, int col) {
    String rowAndColumn = String.format("<!-- r%d-c%d -->\n", row, col);
    int tileNumber = (BINGO_SIZE * (row - 1)) + (col - 1);
    String td = String.format(" <td id=\"tile-%d\" class=\"bingo-tile\" onclick=\"clickBingoTile(this, event)\" onmouseenter=\"mouseBingoTile(this, event)\" onmousemove=\"mouseBingoTile(this, event)\" onmouseleave=\"mouseBingoTile(this, event)\">\n", tileNumber);
    String svg = String.format("      <svg id=\"outlines\" width=\"%dpx\" height=\"%dpx\">\n", TILE_CELL_FULL, TILE_CELL_FULL);
    String text = String.format("    <div id=\"text-div\" class=\"bingo-tile child-div bingo-tile-text\">R%d C%d</div>\n", row, col);

    final String INDENT = "          ";
    htmlOutput.append(INDENT + rowAndColumn);
    htmlOutput.append(INDENT + td);
    htmlOutput.append(INDENT + "  <div id=\"parent-div\" class=\"bingo-tile\">\n");
    htmlOutput.append(INDENT + "    <div id=\"state-div\"  class=\"bingo-tile child-div\">\n");
    htmlOutput.append(INDENT + "      <form id=\"form\"  class=\"not-visible\">\n");
    htmlOutput.append(INDENT + "        <input id=\"player1\" type=\"hidden\" value=\"0\" />\n");
    htmlOutput.append(INDENT + "        <input id=\"player2\" type=\"hidden\" value=\"0\" />\n");
    htmlOutput.append(INDENT + "      </form>\n");
    htmlOutput.append(INDENT + "    </div>\n");
    htmlOutput.append(INDENT + "    <div id=\"outlines-div\" class=\"bingo-tile child-div\">\n");
    htmlOutput.append(INDENT + svg);
    htmlOutput.append(INDENT + "        <use id=\"player1\" href=\"#empty\" />\n");
    htmlOutput.append(INDENT + "        <use id=\"player2\" href=\"#empty\" />\n");
    htmlOutput.append(INDENT + "      </svg>\n");
    htmlOutput.append(INDENT + "    </div>\n");
    htmlOutput.append(INDENT + text);
    htmlOutput.append(INDENT + "  </div>\n");
    htmlOutput.append(INDENT + "</td>\n");
  }
}