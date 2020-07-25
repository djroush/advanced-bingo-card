// The absense of a package declaration, and all the static methods. Yikes.

import java.lang.StringBuffer;

public class GenerateBingoCard {
  private static final int BINGO_SIZE = 5;

  private static final int STROKE_WIDTH_HALF = 2;
  private static final int STROKE_WIDTH_FULL = 2 * STROKE_WIDTH_HALF;

  private static final int TILE_CELL_QRTR = 24;
  private static final int TILE_CELL_HALF = 2 * TILE_CELL_QRTR;
  private static final int TILE_CELL_FULL = 2 * TILE_CELL_HALF;
  private static final int TILE_CELL_SQRT = 3 * TILE_CELL_HALF; // 1.50x, which is greater than the needed 1.41x

  private static final long COLOR_BACKGROUND = 0x2F2F2F;
  private static final long COLOR_BINGO_TILE = 0x000000;
  private static final long COLOR_BINGO_TILE_TEXT = 0xC0A000;
  private static final long COLOR_P1 = 0xA000C0;
  private static final long COLOR_P2 = 0x00C020;
  private static final long COLOR_RESTRICTED = 0xE02040;

  private static final String HIGHLIGHT_OPACITY = "0.25";
  private static final String STYLE_INDENT = "        ";
  private static final String SCRIPT_INDENT = "        ";

  /*
   * I used Java to write out the HTML/CSS/JS because I got tired of making the same
   * edit multiple times for each column/row. There is a better way to template this
   */
  public static void main(String[] args) {
    StringBuffer htmlOutput = new StringBuffer();

    htmlOutput.append("<!DOCTYPE html>\n");
    htmlOutput.append("<html>\n");
    htmlOutput.append("  <head>\n");
    htmlOutput.append("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n");
    htmlOutput.append("    <title>Java Generated Sample Bingo Card</title>\n");

    generateCss(htmlOutput);
    htmlOutput.append("\n");
    generateJs(htmlOutput);
    htmlOutput.append("  </head>\n");

    htmlOutput.append("  <body class=\"background\">\n");
    generateBody(htmlOutput);
    htmlOutput.append("  </body>\n");
    htmlOutput.append("</html>\n");

    System.out.print(htmlOutput.toString());
  }

  private static void generateCss(StringBuffer htmlOutput) {
    htmlOutput.append("      <style>\n");

    generateCommonCss(htmlOutput);
    htmlOutput.append("\n");
    generateBorderTileCss(htmlOutput);
    htmlOutput.append("\n");
    generateBingoTileCss(htmlOutput);

    htmlOutput.append("      </style>\n");
  }
  private static void generateCommonCss(StringBuffer htmlOutput) {
    htmlOutput.append(STYLE_INDENT + ".background {\n");
    htmlOutput.append(STYLE_INDENT + "  background-color: #" + String.format("%06X", COLOR_BACKGROUND) + ";\n");
    htmlOutput.append(STYLE_INDENT + "}\n");

    htmlOutput.append(STYLE_INDENT + ".child-div {\n");
    htmlOutput.append(STYLE_INDENT + "  position: absolute;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".not-visible {\n");
    htmlOutput.append(STYLE_INDENT + "  display: none;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".outline {\n");
    htmlOutput.append(STYLE_INDENT + "  fill-opacity: 0.0;\n");
    htmlOutput.append(STYLE_INDENT + "  stroke-opacity: 1.0;\n");
    htmlOutput.append(STYLE_INDENT + "  stroke-width: " + STROKE_WIDTH_FULL + ";\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append("\n");
    htmlOutput.append(STYLE_INDENT + ".captured-p1 {\n");
    htmlOutput.append(STYLE_INDENT + "  stroke: #" + String.format("%06X", COLOR_P1) + ";\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".captured-p2 {\n");
    htmlOutput.append(STYLE_INDENT + "  stroke: #" + String.format("%06X", COLOR_P2) + ";\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".required {\n");
    htmlOutput.append(STYLE_INDENT + "  stroke: #" + String.format("%06X", COLOR_BACKGROUND) + ";\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".restricted {\n");
    htmlOutput.append(STYLE_INDENT + "  stroke: #" + String.format("%06X", COLOR_RESTRICTED) + ";\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
  }

  private static void generateBorderTileCss(StringBuffer htmlOutput) {
    generateBorderTileDefinitions(htmlOutput);
    htmlOutput.append("\n");
    generateBorderTilePlayersDefault(htmlOutput);
    htmlOutput.append("\n");
    generateBorderTilePlayersRequired(htmlOutput);
  }
  private static void generateBorderTileDefinitions(StringBuffer htmlOutput) {
    htmlOutput.append(STYLE_INDENT + ".border-column {\n");
    htmlOutput.append(STYLE_INDENT + "  height: " + TILE_CELL_QRTR + "px;\n");
    htmlOutput.append(STYLE_INDENT + "  width: " + TILE_CELL_FULL + "px;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".border-diagonal {\n");
    htmlOutput.append(STYLE_INDENT + "  height: " + TILE_CELL_QRTR + "px;\n");
    htmlOutput.append(STYLE_INDENT + "  width: " + TILE_CELL_QRTR + "px;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".border-row {\n");
    htmlOutput.append(STYLE_INDENT + "  height: " + TILE_CELL_FULL + "px;\n");
    htmlOutput.append(STYLE_INDENT + "  width: " + TILE_CELL_QRTR + "px;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
  }
  private static void generateBorderTilePlayersDefault(StringBuffer htmlOutput) {
    htmlOutput.append(STYLE_INDENT + ".border-default-p1 {\n");
    htmlOutput.append(STYLE_INDENT + String.format("  background: #%06X;\n", COLOR_P1));
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".border-default-p2 {\n");
    htmlOutput.append(STYLE_INDENT + String.format("  background: #%06X;\n", COLOR_P2));
    htmlOutput.append(STYLE_INDENT + "}\n");
  }
  private static void generateBorderTilePlayersRequired(StringBuffer htmlOutput) {
    htmlOutput.append(STYLE_INDENT + ".border-required-p1 {\n");
    htmlOutput.append(STYLE_INDENT + String.format("  background: radial-gradient(circle at center, #%06X 0px, #%06X 4px, #%06X 7px, #%06X 10px, #%06X 100%%);\n",
      COLOR_P1, COLOR_P1, COLOR_BACKGROUND, COLOR_P1, COLOR_P1));
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".border-required-p2 {\n");
    htmlOutput.append(STYLE_INDENT + String.format("  background: radial-gradient(circle at center, #%06X 0px, #%06X 4px, #%06X 7px, #%06X 10px, #%06X 100%%);\n",
      COLOR_P2, COLOR_P2, COLOR_BACKGROUND, COLOR_P2, COLOR_P2));
    htmlOutput.append(STYLE_INDENT + "}\n");
  }

  private static void generateBingoTileCss(StringBuffer htmlOutput) {
    htmlOutput.append(STYLE_INDENT + ".bingo-tile {\n");
    htmlOutput.append(STYLE_INDENT + "  height: " + TILE_CELL_FULL + "px;\n");
    htmlOutput.append(STYLE_INDENT + "  width: " + TILE_CELL_FULL + "px;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".bingo-tile-background {\n");
    htmlOutput.append(STYLE_INDENT + "  background-color: #" + String.format("%06X", COLOR_BINGO_TILE) + ";\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".bingo-tile-text {\n");
    htmlOutput.append(STYLE_INDENT + "  color: #" + String.format("%06X", COLOR_BINGO_TILE_TEXT) + ";\n");
    htmlOutput.append(STYLE_INDENT + "  display: grid;\n");
    htmlOutput.append(STYLE_INDENT + "  place-items:center;\n");
    htmlOutput.append(STYLE_INDENT + "  text-align: center;\n");
    htmlOutput.append(STYLE_INDENT + "  user-select: none;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
  }


  private static void generateJs(StringBuffer htmlOutput) {
    htmlOutput.append("      <script>\n");
    generateJsCommon(htmlOutput);
    htmlOutput.append("\n");
    generateJsFunctionsMouse(htmlOutput);
    htmlOutput.append("\n");
    generateJsFunctionsClick(htmlOutput);
    htmlOutput.append("\n");
    generateJsFunctionsMultiTile(htmlOutput);
    htmlOutput.append("      </script>\n");
  }

  private static void generateJsCommon(StringBuffer htmlOutput) {
    generateJsConstants(htmlOutput);
    generateJsFunctionClickType(htmlOutput);
    generateJsFunctionPlayerType(htmlOutput);
  }
  private static void generateJsFunctionsMouse(StringBuffer htmlOutput) {
    generateJsFunctionBorderTileMouse(htmlOutput);
    generateJsFunctionBingoTileMouse(htmlOutput);
    generateJsFunctionHighlightBingoTile(htmlOutput);
  }
  private static void generateJsFunctionsClick(StringBuffer htmlOutput) {
    generateJsFunctionBorderTileClick(htmlOutput);
    generateJsFunctionBingoTileClick(htmlOutput);
    generateJsFunctionAdvanceTileState(htmlOutput);
  }
  private static void generateJsFunctionsMultiTile(StringBuffer htmlOutput) {
    generateJsFunctionUpdateBingos(htmlOutput);
    generateJsFunctionUpdateBingo(htmlOutput);
    htmlOutput.append("\n");
    generateJsFunctionNewBorderStatus(htmlOutput);
    generateJsFunctionBingoStatus(htmlOutput);
    htmlOutput.append("\n");
    generateJsFunctionOutlineName(htmlOutput);
    htmlOutput.append("\n");
    generateJsFunctionBorderId(htmlOutput);
    htmlOutput.append("\n");
    generateJsFunctionUpdateBorderOutline(htmlOutput);
  }

  private static void generateJsConstants(StringBuffer htmlOutput) {
    final String BINGO_SIZE_JS = String.format("const BingoSize = %d;\n", BINGO_SIZE);
    final String BINGO_TYPES = String.format("const BingoTypes = { COLUMN: %d, ROW: 1, TL_BR: %d, TR_BL: %d };\n", BINGO_SIZE, (BINGO_SIZE + 1), (BINGO_SIZE - 1));
    final String DEFAULT = getRgba(COLOR_BINGO_TILE, "1");
    final String P1_HIGHLIGHT = getRgba(COLOR_P1, HIGHLIGHT_OPACITY);
    final String P2_HIGHLIGHT = getRgba(COLOR_P2, HIGHLIGHT_OPACITY);

    final String P1_COLORS = String.format("const Player1Colors = { DEFAULT : \"%s\", HIGHLIGHT : \"%s\" };\n", DEFAULT, P1_HIGHLIGHT);
    final String P2_COLORS = String.format("const Player2Colors = { DEFAULT : \"%s\", HIGHLIGHT : \"%s\" };\n", DEFAULT, P2_HIGHLIGHT);

    htmlOutput.append(SCRIPT_INDENT + BINGO_SIZE_JS);
    htmlOutput.append(SCRIPT_INDENT + "Object.freeze(BingoSize);\n");
    htmlOutput.append(SCRIPT_INDENT + "const BingoStates = { GOAL_NOT_MET : \"0\", GOAL_MET : \"1\", GOAL_CANNOT_BE_MET_ANYMORE : \"2\" };\n");
    htmlOutput.append(SCRIPT_INDENT + "Object.freeze(BingoStates);\n");
    htmlOutput.append(SCRIPT_INDENT + BINGO_TYPES);
    htmlOutput.append(SCRIPT_INDENT + "Object.freeze(BingoTypes);\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "const ClickType = { NORMAL : \"0\", SHIFT : \"1\", CONTROL : \"2\" };\n");
    htmlOutput.append(SCRIPT_INDENT + "Object.freeze(ClickType);\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "const PlayerType = { NONE : \"0\", P1 : \"1\", P2 : \"2\" };\n");
    htmlOutput.append(SCRIPT_INDENT + "Object.freeze(PlayerType);\n");
    htmlOutput.append(SCRIPT_INDENT + P1_COLORS);
    htmlOutput.append(SCRIPT_INDENT + "Object.freeze(Player1Colors);\n");
    htmlOutput.append(SCRIPT_INDENT + P2_COLORS);
    htmlOutput.append(SCRIPT_INDENT + "Object.freeze(Player2Colors);\n");
  }
  private static String getRgba(long rgb, String alpha) {
    int r = (int)((rgb & 0xFF0000) >> 16);
    int g = (int)((rgb & 0x00FF00) >> 8);
    int b = (int)((rgb & 0x0000FF));

    return String.format("rgba(%d, %d, %d, %s)", r, g, b, alpha);
  }
  private static void generateJsFunctionClickType(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function classifyClick(event) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  if (event.ctrlKey) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    return ClickType.CONTROL;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  if (event.shiftKey) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    return ClickType.SHIFT;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  return ClickType.NORMAL;\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }
  private static void generateJsFunctionPlayerType(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function classifyPlayer(element, event) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  relativeX = event.clientX - element.getBoundingClientRect().left;\n");
    htmlOutput.append(SCRIPT_INDENT + "  relativeY = event.clientY - element.getBoundingClientRect().top;\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  sum = relativeX + relativeY;\n");
    htmlOutput.append(SCRIPT_INDENT + "  withinX = (0 < relativeX) && (relativeX <= " + TILE_CELL_FULL + ");\n");
    htmlOutput.append(SCRIPT_INDENT + "  withinY = (0 < relativeY) && (relativeY <= " + TILE_CELL_FULL + ");\n");
    htmlOutput.append(SCRIPT_INDENT + "  player1 = (sum < " + TILE_CELL_FULL + ") && withinX && withinY;\n");
    htmlOutput.append(SCRIPT_INDENT + "  player2 = (sum > " + TILE_CELL_FULL + ") && withinX && withinY;\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  if (player1) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    return PlayerType.P1;\n");
    htmlOutput.append(SCRIPT_INDENT + "  } else if (player2) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    return PlayerType.P2;\n");
    htmlOutput.append(SCRIPT_INDENT + "  } else {\n");
    htmlOutput.append(SCRIPT_INDENT + "    return PlayerType.NONE;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }


  private static void generateJsFunctionBorderTileMouse(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function mouseBorderTile(element, highlight) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  var borderId = element.id;\n");
    htmlOutput.append(SCRIPT_INDENT + "  var borderNumber = parseInt(borderId.substring(\"border-\".length));\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  var playerType;\n");
    htmlOutput.append(SCRIPT_INDENT + "  if (borderNumber < (2 * BingoTypes.TL_BR)) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    playerType = PlayerType.P1;\n");
    htmlOutput.append(SCRIPT_INDENT + "  } else {\n");
    htmlOutput.append(SCRIPT_INDENT + "    playerType = PlayerType.P2;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  var modDiagonal = borderNumber % BingoTypes.TL_BR;\n");
    htmlOutput.append(SCRIPT_INDENT + "  var divDiagonal = Math.floor(borderNumber / BingoTypes.TL_BR);\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  var start;\n");
    htmlOutput.append(SCRIPT_INDENT + "  var delta;\n");
    htmlOutput.append(SCRIPT_INDENT + "  if (modDiagonal == 0) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    if ((divDiagonal % 2) == 0) {\n");
    htmlOutput.append(SCRIPT_INDENT + "      delta = BingoTypes.TR_BL;\n");
    htmlOutput.append(SCRIPT_INDENT + "      start = BingoTypes.TR_BL;\n");
    htmlOutput.append(SCRIPT_INDENT + "    } else {\n");
    htmlOutput.append(SCRIPT_INDENT + "      delta = BingoTypes.TL_BR;\n");
    htmlOutput.append(SCRIPT_INDENT + "      start = 0;\n");
    htmlOutput.append(SCRIPT_INDENT + "    }\n");
    htmlOutput.append(SCRIPT_INDENT + "  } else {\n");
    htmlOutput.append(SCRIPT_INDENT + "    if ((divDiagonal % 2) == 0) {\n");
    htmlOutput.append(SCRIPT_INDENT + "      delta = BingoTypes.ROW;\n");
    htmlOutput.append(SCRIPT_INDENT + "      if (playerType == PlayerType.P1) {\n");
    htmlOutput.append(SCRIPT_INDENT + "        start = (BingoTypes.COLUMN - modDiagonal) * BingoTypes.COLUMN;\n");
    htmlOutput.append(SCRIPT_INDENT + "      } else { // PlayerType.P2\n");
    htmlOutput.append(SCRIPT_INDENT + "        start = (modDiagonal - BingoTypes.ROW) * BingoTypes.COLUMN;\n");
    htmlOutput.append(SCRIPT_INDENT + "      }\n");
    htmlOutput.append(SCRIPT_INDENT + "    } else {\n");
    htmlOutput.append(SCRIPT_INDENT + "      delta = BingoTypes.COLUMN;\n");
    htmlOutput.append(SCRIPT_INDENT + "      if (playerType == PlayerType.P1) {\n");
    htmlOutput.append(SCRIPT_INDENT + "        start = (modDiagonal - BingoTypes.ROW) * BingoTypes.ROW;\n");
    htmlOutput.append(SCRIPT_INDENT + "      } else { // PlayerType.P2\n");
    htmlOutput.append(SCRIPT_INDENT + "        start = (BingoTypes.COLUMN - modDiagonal) * BingoTypes.ROW;\n");
    htmlOutput.append(SCRIPT_INDENT + "      }\n");
    htmlOutput.append(SCRIPT_INDENT + "    }\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  if (!highlight) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    playerType = PlayerType.NONE;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  var counter = 0;\n");
    htmlOutput.append(SCRIPT_INDENT + "  var tileNumberLoop = start;\n");
    htmlOutput.append(SCRIPT_INDENT + "  while (counter < BingoSize) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    var workingTile = document.getElementById(\"tile-\" + tileNumberLoop);\n");
    htmlOutput.append(SCRIPT_INDENT + "    highlightBingoTile(workingTile, playerType);\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "    counter++;\n");
    htmlOutput.append(SCRIPT_INDENT + "    tileNumberLoop += delta;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }
  private static void generateJsFunctionBingoTileMouse(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function mouseBingoTile(element, event) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  var playerType = classifyPlayer(element, event);\n");
    htmlOutput.append(SCRIPT_INDENT + "  highlightBingoTile(element, playerType);\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }
  private static void generateJsFunctionHighlightBingoTile(StringBuffer htmlOutput) {
    final int HALF_45_P1 = (int)(TILE_CELL_HALF * Math.sqrt(2));
    final int HALF_45_P2 = (int)(TILE_CELL_FULL * Math.sqrt(2)) - HALF_45_P1;
    final String linearGradient = String.format("  var tileBackground = \"linear-gradient(135deg, \" + colorP1 + \" 0px, \" + colorP1 + \" %dpx, #%06X %dpx, #%06X %dpx, \" + colorP2 + \" %dpx, \" + colorP2 + \" %dpx)\";\n",
    (HALF_45_P1), COLOR_BINGO_TILE, (HALF_45_P1 + 1), COLOR_BINGO_TILE, (HALF_45_P2 + 2), (HALF_45_P2 + 3), TILE_CELL_SQRT); // These offsets of +1 +2 etc. are just what looks best to me

    htmlOutput.append(SCRIPT_INDENT + "function highlightBingoTile(element, playerType) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  var colorP1 = Player1Colors.DEFAULT;\n");
    htmlOutput.append(SCRIPT_INDENT + "  var colorP2 = Player2Colors.DEFAULT;\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  if (playerType == PlayerType.P1) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    colorP1 = Player1Colors.HIGHLIGHT;\n");
    htmlOutput.append(SCRIPT_INDENT + "  } else if (playerType == PlayerType.P2) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    colorP2 = Player2Colors.HIGHLIGHT;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + linearGradient);
    htmlOutput.append(SCRIPT_INDENT + "  element.style.background = tileBackground;\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }

  private static void generateJsFunctionBorderTileClick(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function clickBorderTile(element, player) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  var classList = element.classList;\n");
    htmlOutput.append(SCRIPT_INDENT + "  var parentDiv = element.children.namedItem(\"parent-div\");\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  var stateDiv = parentDiv.children.namedItem(\"state-div\");\n");
    htmlOutput.append(SCRIPT_INDENT + "  var form = stateDiv.children.namedItem(\"form\");\n");
    htmlOutput.append(SCRIPT_INDENT + "  var required = form.elements[\"required\"];\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  if (required.value == \"0\") {\n");
    htmlOutput.append(SCRIPT_INDENT + "    classList.remove(\"border-default-\" + player);\n");
    htmlOutput.append(SCRIPT_INDENT + "    classList.add(\"border-required-\" + player);\n");
    htmlOutput.append(SCRIPT_INDENT + "    required.value = \"1\";\n");
    htmlOutput.append(SCRIPT_INDENT + "  } else {\n");
    htmlOutput.append(SCRIPT_INDENT + "    classList.remove(\"border-required-\" + player);\n");
    htmlOutput.append(SCRIPT_INDENT + "    classList.add(\"border-default-\" + player);\n");
    htmlOutput.append(SCRIPT_INDENT + "    required.value = \"0\";\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }
  private static void generateJsFunctionBingoTileClick(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function clickBingoTile(element, event) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  var parentDiv = element.children.namedItem(\"parent-div\");\n");
    htmlOutput.append(SCRIPT_INDENT + "  var stateDiv = parentDiv.children.namedItem(\"state-div\");\n");
    htmlOutput.append(SCRIPT_INDENT + "  var svgsDiv = parentDiv.children.namedItem(\"outlines-div\");\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  var form = stateDiv.children.namedItem(\"form\");\n");
    htmlOutput.append(SCRIPT_INDENT + "  var svg = svgsDiv.children.namedItem(\"outlines\");\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  var clickType = classifyClick(event);\n");
    htmlOutput.append(SCRIPT_INDENT + "  var playerType = classifyPlayer(element, event);\n");
    htmlOutput.append(SCRIPT_INDENT + "  switch (playerType) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    case PlayerType.P1:\n");
    htmlOutput.append(SCRIPT_INDENT + "      var state = form.elements[\"player1\"];\n");
    htmlOutput.append(SCRIPT_INDENT + "      var outline = svg.children.namedItem(\"player1\");\n");
    htmlOutput.append(SCRIPT_INDENT + "      advanceTileState(clickType, state, true, outline);\n");
    htmlOutput.append(SCRIPT_INDENT + "    break;\n");
    htmlOutput.append(SCRIPT_INDENT + "    case PlayerType.P2:\n");
    htmlOutput.append(SCRIPT_INDENT + "      var state = form.elements[\"player2\"];\n");
    htmlOutput.append(SCRIPT_INDENT + "      var outline = svg.children.namedItem(\"player2\");\n");
    htmlOutput.append(SCRIPT_INDENT + "      advanceTileState(clickType, state, false, outline);\n");
    htmlOutput.append(SCRIPT_INDENT + "    break;\n");
    htmlOutput.append(SCRIPT_INDENT + "    case PlayerType.NONE:\n");
    htmlOutput.append(SCRIPT_INDENT + "    break;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  updateBingos(element.id, playerType);\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }
  private static void generateJsFunctionAdvanceTileState(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function advanceTileState(clickType, state, isPlayer1, outline) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  var svgHref = document.createAttribute(\"href\");\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  switch (clickType) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    case ClickType.NORMAL:\n");
    htmlOutput.append(SCRIPT_INDENT + "      switch (state.value) {\n");
    htmlOutput.append(SCRIPT_INDENT + "        case BingoStates.GOAL_NOT_MET:\n");
    htmlOutput.append(SCRIPT_INDENT + "          state.value = BingoStates.GOAL_MET;\n");
    htmlOutput.append(SCRIPT_INDENT + "          svgHref.value = (player1 ? \"#p1-goal-met\" : \"#p2-goal-met\");\n");
    htmlOutput.append(SCRIPT_INDENT + "        break;\n");
    htmlOutput.append(SCRIPT_INDENT + "        case BingoStates.GOAL_MET:\n");
    htmlOutput.append(SCRIPT_INDENT + "          state.value = BingoStates.GOAL_CANNOT_BE_MET_ANYMORE;\n");
    htmlOutput.append(SCRIPT_INDENT + "          svgHref.value = (player1 ? \"#p1-goal-cannot-be-met\" : \"#p2-goal-cannot-be-met\");\n");
    htmlOutput.append(SCRIPT_INDENT + "        break;\n");
    htmlOutput.append(SCRIPT_INDENT + "        case BingoStates.GOAL_CANNOT_BE_MET_ANYMORE:\n");
    htmlOutput.append(SCRIPT_INDENT + "          state.value = BingoStates.GOAL_NOT_MET;\n");
    htmlOutput.append(SCRIPT_INDENT + "          svgHref.value = \"#empty\";\n");
    htmlOutput.append(SCRIPT_INDENT + "        break;\n");
    htmlOutput.append(SCRIPT_INDENT + "      }\n");
    htmlOutput.append(SCRIPT_INDENT + "    break;\n");
    htmlOutput.append(SCRIPT_INDENT + "    case ClickType.CONTROL:\n");
    htmlOutput.append(SCRIPT_INDENT + "      if (state.value == BingoStates.GOAL_MET) {\n");
    htmlOutput.append(SCRIPT_INDENT + "        state.value = BingoStates.GOAL_NOT_MET;\n");
    htmlOutput.append(SCRIPT_INDENT + "        svgHref.value = \"#empty\";\n");
    htmlOutput.append(SCRIPT_INDENT + "      } else {\n");
    htmlOutput.append(SCRIPT_INDENT + "        state.value = BingoStates.GOAL_MET;\n");
    htmlOutput.append(SCRIPT_INDENT + "        svgHref.value = (player1 ? \"#p1-goal-met\" : \"#p2-goal-met\");\n");
    htmlOutput.append(SCRIPT_INDENT + "      }\n");
    htmlOutput.append(SCRIPT_INDENT + "    break;\n");
    htmlOutput.append(SCRIPT_INDENT + "    case ClickType.SHIFT:\n");
    htmlOutput.append(SCRIPT_INDENT + "      if (state.value == BingoStates.GOAL_CANNOT_BE_MET_ANYMORE) {\n");
    htmlOutput.append(SCRIPT_INDENT + "        state.value = BingoStates.GOAL_NOT_MET;\n");
    htmlOutput.append(SCRIPT_INDENT + "        svgHref.value = \"#empty\";\n");
    htmlOutput.append(SCRIPT_INDENT + "      } else {\n");
    htmlOutput.append(SCRIPT_INDENT + "        state.value = BingoStates.GOAL_CANNOT_BE_MET_ANYMORE;\n");
    htmlOutput.append(SCRIPT_INDENT + "        svgHref.value = (player1 ? \"#p1-goal-cannot-be-met\" : \"#p2-goal-cannot-be-met\");\n");
    htmlOutput.append(SCRIPT_INDENT + "      }\n");
    htmlOutput.append(SCRIPT_INDENT + "    break;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  outline.attributes.setNamedItem(svgHref);\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }

  private static void generateJsFunctionUpdateBingos(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function updateBingos(tileId, player) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  tileNumber = parseInt(tileId.substring(\"tile-\".length));\n");
    htmlOutput.append(SCRIPT_INDENT + "  column = tileNumber % BingoTypes.COLUMN;\n");
    htmlOutput.append(SCRIPT_INDENT + "  row = Math.floor(tileNumber / BingoTypes.COLUMN);\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  updateBingo(BingoTypes.ROW * column, BingoTypes.COLUMN, player, tileNumber);\n");
    htmlOutput.append(SCRIPT_INDENT + "  updateBingo(BingoTypes.COLUMN * row, BingoTypes.ROW, player, tileNumber);\n");
    htmlOutput.append(SCRIPT_INDENT + "  if (row == column) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    updateBingo(0, BingoTypes.TL_BR, player, tileNumber);\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append(SCRIPT_INDENT + "  if ((row + column) == BingoTypes.TL_BR) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    // row and column are each offset by 1, 1..5 instead of 0..4, so (BingoSize + 1) instead of (BingoSize - 1)\n");
    htmlOutput.append(SCRIPT_INDENT + "    updateBingo(BingoTypes.TR_BL, BingoTypes.TR_BL, player, tileNumber)\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }
  private static void generateJsFunctionUpdateBingo(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function updateBingo(start, delta, player, tileNumber) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  var newStatus = getNewBorderStatus(start, delta, player);\n");
    htmlOutput.append(SCRIPT_INDENT + "  var outlineName = getOutlineName(newStatus, delta, player);\n");
    htmlOutput.append(SCRIPT_INDENT + "  var borderId = getBorderId(delta, player, tileNumber)\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  updateBorderOutline(borderId, outlineName);\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }
  private static void generateJsFunctionNewBorderStatus(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function getNewBorderStatus (start, delta, player) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  var notMet = false;\n");
    htmlOutput.append(SCRIPT_INDENT + "  var cannotBeMet = false;\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  var counter = 0;\n");
    htmlOutput.append(SCRIPT_INDENT + "  var tileNumberLoop = start;\n");
    htmlOutput.append(SCRIPT_INDENT + "  while ((counter < BingoSize) && (!notMet || !cannotBeMet)) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    var workingTile = document.getElementById(\"tile-\" + tileNumberLoop);\n");
    htmlOutput.append(SCRIPT_INDENT + "    var status = getBingoStatus(workingTile, player)\n");
    htmlOutput.append(SCRIPT_INDENT + "    if (status == BingoStates.GOAL_CANNOT_BE_MET_ANYMORE) {\n");
    htmlOutput.append(SCRIPT_INDENT + "      cannotBeMet = true;\n");
    htmlOutput.append(SCRIPT_INDENT + "    } else if (status == BingoStates.GOAL_NOT_MET) {\n");
    htmlOutput.append(SCRIPT_INDENT + "      notMet = true;\n");
    htmlOutput.append(SCRIPT_INDENT + "    }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "    counter++;\n");
    htmlOutput.append(SCRIPT_INDENT + "    tileNumberLoop += delta;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  if (cannotBeMet) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    return BingoStates.GOAL_CANNOT_BE_MET_ANYMORE;\n");
    htmlOutput.append(SCRIPT_INDENT + "  } else if (notMet) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    return BingoStates.GOAL_NOT_MET;\n");
    htmlOutput.append(SCRIPT_INDENT + "  } else {\n");
    htmlOutput.append(SCRIPT_INDENT + "    return BingoStates.GOAL_MET;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }
  private static void generateJsFunctionBingoStatus(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function getBingoStatus(element, player) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  var parentDiv = element.children.namedItem(\"parent-div\");\n");
    htmlOutput.append(SCRIPT_INDENT + "  var stateDiv = parentDiv.children.namedItem(\"state-div\");\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  var form = stateDiv.children.namedItem(\"form\");\n");
    htmlOutput.append(SCRIPT_INDENT + "  switch (player) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    case PlayerType.P1:\n");
    htmlOutput.append(SCRIPT_INDENT + "      return form.elements[\"player1\"].value;\n");
    htmlOutput.append(SCRIPT_INDENT + "    break;\n");
    htmlOutput.append(SCRIPT_INDENT + "    case PlayerType.P2:\n");
    htmlOutput.append(SCRIPT_INDENT + "      return form.elements[\"player2\"].value;\n");
    htmlOutput.append(SCRIPT_INDENT + "    break;\n");
    htmlOutput.append(SCRIPT_INDENT + "    case PlayerType.NONE:\n");
    htmlOutput.append(SCRIPT_INDENT + "      return -1;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }
  private static void generateJsFunctionOutlineName(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function getOutlineName(newStatus, delta, player) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  var outlineName = \"#empty\";\n");
    htmlOutput.append(SCRIPT_INDENT + "  if (newStatus != BingoStates.GOAL_NOT_MET) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    var borderType;\n");
    htmlOutput.append(SCRIPT_INDENT + "    switch (delta) {\n");
    htmlOutput.append(SCRIPT_INDENT + "      case BingoTypes.ROW: borderType = \"row\";\n");
    htmlOutput.append(SCRIPT_INDENT + "      break;\n");
    htmlOutput.append(SCRIPT_INDENT + "      case BingoTypes.COLUMN: borderType = \"column\";\n");
    htmlOutput.append(SCRIPT_INDENT + "      break;\n");
    htmlOutput.append(SCRIPT_INDENT + "      case BingoTypes.TL_BR:\n");
    htmlOutput.append(SCRIPT_INDENT + "      case BingoTypes.TR_BL:\n");
    htmlOutput.append(SCRIPT_INDENT + "        borderType = \"diagonal\";\n");
    htmlOutput.append(SCRIPT_INDENT + "      break;\n");
    htmlOutput.append(SCRIPT_INDENT + "    }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "    var borderColor;\n");
    htmlOutput.append(SCRIPT_INDENT + "    switch (newStatus) {\n");
    htmlOutput.append(SCRIPT_INDENT + "      case BingoStates.GOAL_MET:\n");
    htmlOutput.append(SCRIPT_INDENT + "        if (player == PlayerType.P1) {\n");
    htmlOutput.append(SCRIPT_INDENT + "          borderColor = \"p1\";\n");
    htmlOutput.append(SCRIPT_INDENT + "        } else if (player == PlayerType.P2) {\n");
    htmlOutput.append(SCRIPT_INDENT + "          borderColor = \"p2\";\n");
    htmlOutput.append(SCRIPT_INDENT + "        }\n");
    htmlOutput.append(SCRIPT_INDENT + "      break;\n");
    htmlOutput.append(SCRIPT_INDENT + "      case BingoStates.GOAL_CANNOT_BE_MET_ANYMORE:\n");
    htmlOutput.append(SCRIPT_INDENT + "        borderColor = \"restricted\";\n");
    htmlOutput.append(SCRIPT_INDENT + "      break;\n");
    htmlOutput.append(SCRIPT_INDENT + "    }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "    outlineName = \"#\" + borderType + \"-\" + borderColor;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  return outlineName;\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }
  private static void generateJsFunctionBorderId(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function getBorderId(delta, player, tileNumber) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  if (PlayerType.NONE == player) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    return -1; // this should never happen\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append(SCRIPT_INDENT + "  switch (delta) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    case BingoTypes.COLUMN:\n");
    htmlOutput.append(SCRIPT_INDENT + "      column = 1 + (tileNumber % BingoSize);\n");
    htmlOutput.append(SCRIPT_INDENT + "      if (PlayerType.P1 == player) {\n");
    htmlOutput.append(SCRIPT_INDENT + "        return (1 * BingoTypes.TL_BR) + column;\n");
    htmlOutput.append(SCRIPT_INDENT + "      } else { // PlayerType.P2\n");
    htmlOutput.append(SCRIPT_INDENT + "        return (4 * BingoTypes.TL_BR) - column;\n");
    htmlOutput.append(SCRIPT_INDENT + "      }\n");
    htmlOutput.append(SCRIPT_INDENT + "    case BingoTypes.ROW:\n");
    htmlOutput.append(SCRIPT_INDENT + "      row = 1 + Math.floor(tileNumber / BingoSize);\n");
    htmlOutput.append(SCRIPT_INDENT + "      if (PlayerType.P1 == player) {\n");
    htmlOutput.append(SCRIPT_INDENT + "        return (1 * BingoTypes.TL_BR) - row;\n");
    htmlOutput.append(SCRIPT_INDENT + "      } else { // PlayerType.P2\n");
    htmlOutput.append(SCRIPT_INDENT + "        return (2 * BingoTypes.TL_BR) + row;\n");
    htmlOutput.append(SCRIPT_INDENT + "      }\n");
    htmlOutput.append(SCRIPT_INDENT + "    case BingoTypes.TR_BL:\n");
    htmlOutput.append(SCRIPT_INDENT + "      if (PlayerType.P1 == player) {\n");
    htmlOutput.append(SCRIPT_INDENT + "        return (0 * BingoTypes.TL_BR);\n");
    htmlOutput.append(SCRIPT_INDENT + "      } else { // PlayerType.P2\n");
    htmlOutput.append(SCRIPT_INDENT + "        return (2 * BingoTypes.TL_BR);\n");
    htmlOutput.append(SCRIPT_INDENT + "      }\n");
    htmlOutput.append(SCRIPT_INDENT + "    case BingoTypes.TL_BR:\n");
    htmlOutput.append(SCRIPT_INDENT + "      if (PlayerType.P1 == player) {\n");
    htmlOutput.append(SCRIPT_INDENT + "        return (1 * BingoTypes.TL_BR);\n");
    htmlOutput.append(SCRIPT_INDENT + "      } else { // PlayerType.P2\n");
    htmlOutput.append(SCRIPT_INDENT + "        return (3 * BingoTypes.TL_BR);\n");
    htmlOutput.append(SCRIPT_INDENT + "      }\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }
  private static void generateJsFunctionUpdateBorderOutline(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function updateBorderOutline (borderId, outlineName) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  var borderTile = document.getElementById(\"border-\" + borderId);\n");
    htmlOutput.append(SCRIPT_INDENT + "  var parentDiv = borderTile.children.namedItem(\"parent-div\");\n");
    htmlOutput.append(SCRIPT_INDENT + "  var svgsDiv = parentDiv.children.namedItem(\"outline-div\");\n");
    htmlOutput.append(SCRIPT_INDENT + "  var svg = svgsDiv.children.namedItem(\"outline\");\n");
    htmlOutput.append(SCRIPT_INDENT + "  var outline = svg.children.namedItem(\"bingo\");\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  var svgHref = document.createAttribute(\"href\");\n");
    htmlOutput.append(SCRIPT_INDENT + "  svgHref.value = outlineName;\n");
    htmlOutput.append(SCRIPT_INDENT + "  outline.attributes.setNamedItem(svgHref);\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
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
    final String POINTS_P1_RESTRICTED = String.format("%d,%d %d,%d %d,%d %d,%d %d,%d", BINGO_P1_NONE,BINGO_P1_NONE, BINGO_P1_FULL,BINGO_P1_NONE, BINGO_P1_NONE,BINGO_P1_FULL, BINGO_P1_NONE,BINGO_P1_NONE, BINGO_P1_HALF,BINGO_P1_HALF);
    final String POINTS_P2_CAPTURED = String.format("%d,%d %d,%d %d,%d", BINGO_P2_NONE,BINGO_P2_NONE, BINGO_P2_FULL,BINGO_P2_NONE, BINGO_P2_NONE,BINGO_P2_FULL);
    final String POINTS_P2_RESTRICTED = String.format("%d,%d %d,%d %d,%d %d,%d %d,%d", BINGO_P2_NONE,BINGO_P2_NONE, BINGO_P2_FULL,BINGO_P2_NONE, BINGO_P2_NONE,BINGO_P2_FULL, BINGO_P2_NONE,BINGO_P2_NONE, BINGO_P2_HALF,BINGO_P2_HALF);

    htmlOutput.append(INDENT + "<svg class=\"not-visible\">\n");
    htmlOutput.append(INDENT + "  <defs>\n");
    htmlOutput.append(INDENT + "    <polygon id=\"empty\" points=\"\" />\n");
    htmlOutput.append("\n");
    htmlOutput.append(INDENT + "    <polygon id=\"column-p1\" class=\"border-column outline required\" points=\"" + POINTS_COLUMN +"\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"column-p2\" class=\"border-column outline required\" points=\"" + POINTS_COLUMN +"\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"column-restricted\" class=\"border-column outline restricted\" points=\"" + POINTS_COLUMN +"\" />\n");
    htmlOutput.append("\n");
    htmlOutput.append(INDENT + "    <polygon id=\"diagonal-p1\" class=\"border-diagonal outline required\" points=\"" + BORDER_DIAGONAL +"\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"diagonal-p2\" class=\"border-diagonal outline required\" points=\"" + BORDER_DIAGONAL +"\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"diagonal-restricted\" class=\"border-diagonal outline restricted\" points=\"" + BORDER_DIAGONAL +"\" />\n");
    htmlOutput.append("\n");
    htmlOutput.append(INDENT + "    <polygon id=\"row-p1\" class=\"border-row outline required\" points=\"" + POINTS_ROW + "\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"row-p2\" class=\"border-row outline required\" points=\"" + POINTS_ROW + "\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"row-restricted\" class=\"border-row outline restricted\" points=\"" + POINTS_ROW + "\" />\n");
    htmlOutput.append("\n");
    htmlOutput.append(INDENT + "    <polygon id=\"p1-goal-met\" class=\"bingo-tile outline captured-p1\" points=\"" + POINTS_P1_CAPTURED + "\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"p1-goal-cannot-be-met\" class=\"bingo-tile outline restricted\" points=\"" + POINTS_P1_RESTRICTED + "\" />\n");
    htmlOutput.append("\n");
    htmlOutput.append(INDENT + "    <polygon id=\"p2-goal-met\" class=\"bingo-tile outline captured-p2\" points=\"" + POINTS_P2_CAPTURED + "\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"p2-goal-cannot-be-met\" class=\"bingo-tile outline restricted\" points=\"" + POINTS_P2_RESTRICTED + "\" />\n");
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
        String title = "P" + player + " ";
        title += ((row == col) ? "TL-BR" : "TR-BL");
        generateBorderTile(htmlOutput, borderId, "diagonal", player, title);
      } else {
        String title = "P" + player + " C" + col;
        generateBorderTile(htmlOutput, borderId, "column", player, title);
      }
    } else {
      if (isColumnBorder) {
        String title = "P" + player + " R" + row;
        generateBorderTile(htmlOutput, borderId, "row", player, title);
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

  private static void generateBorderTile(StringBuffer htmlOutput, int borderId, String borderType, char player, String title) {
    String th = String.format("<th id=\"border-%d\" class=\"border-%s border-default-p%c\" title=\"%s\" onclick=\"clickBorderTile(this, 'p%c')\" onmouseenter=\"mouseBorderTile(this, true)\" onmouseleave=\"mouseBorderTile(this, false)\">\n",
      borderId, borderType, player, title, player);
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
    String td = String.format(" <td id=\"tile-%d\" class=\"bingo-tile bingo-tile-background\" onclick=\"clickBingoTile(this, event)\" onmouseenter=\"mouseBingoTile(this, event)\" onmousemove=\"mouseBingoTile(this, event)\" onmouseleave=\"mouseBingoTile(this, event)\">\n", tileNumber);
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