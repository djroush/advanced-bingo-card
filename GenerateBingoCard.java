// The absense of a package declaration, and all the static methods. Yikes.

import java.lang.StringBuffer;

public class GenerateBingoCard {
  private static final int BINGO_SIZE = 5;
  private static final int BORDER_CELL_SIZE = 24;
  private static final int TILE_CELL_SIZE = 4 * BORDER_CELL_SIZE;

  private static final long COLOR_BINGO_TILE_DEFAULT = 0x000000;
  private static final long COLOR_REQUIRED = 0x5F5F5F;
  private static final long COLOR_RESTRICTED = 0xE02040;
  private static final long COLOR_TEXT = 0xC0A000;

  private static final PlayerColors P1 = new PlayerColors(0xA000C0, 0xC020E0);
  private static final PlayerColors P2 = new PlayerColors(0x00C020, 0x20E040);

  private static final String HIGHLIGHT_OPACITY = "0.75";
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
    htmlOutput.append("    <title>Java Generated Sample Bingo Card</title>\n");
    generateCss(htmlOutput);
    htmlOutput.append("\n");
    generateJs(htmlOutput);
    htmlOutput.append("  </head>\n");

    htmlOutput.append("  <body>\n");
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
    htmlOutput.append(STYLE_INDENT + ".child-div {\n");
    htmlOutput.append(STYLE_INDENT + "  position: absolute;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".not-visible {\n");
    htmlOutput.append(STYLE_INDENT + "  display: none;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".outline {\n");
    htmlOutput.append(STYLE_INDENT + "  fill-opacity: 0.0;\n");
    htmlOutput.append(STYLE_INDENT + "  stroke-opacity: 1.0;\n");
    htmlOutput.append(STYLE_INDENT + "  stroke-width: 4;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append("\n");
    htmlOutput.append(STYLE_INDENT + ".captured-p1 {\n");
    htmlOutput.append(STYLE_INDENT + "  stroke: #" + String.format("%06X", P1.getColorDefault()) + ";\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".captured-p2 {\n");
    htmlOutput.append(STYLE_INDENT + "  stroke: #" + String.format("%06X", P2.getColorDefault()) + ";\n");
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
    htmlOutput.append(STYLE_INDENT + "  height: " + BORDER_CELL_SIZE + "px;\n");
    htmlOutput.append(STYLE_INDENT + "  width: " + TILE_CELL_SIZE + "px;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".border-diagonal {\n");
    htmlOutput.append(STYLE_INDENT + "  height: " + BORDER_CELL_SIZE + "px;\n");
    htmlOutput.append(STYLE_INDENT + "  width: " + BORDER_CELL_SIZE + "px;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".border-row {\n");
    htmlOutput.append(STYLE_INDENT + "  height: " + TILE_CELL_SIZE + "px;\n");
    htmlOutput.append(STYLE_INDENT + "  width: " + BORDER_CELL_SIZE + "px;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
  }
  private static void generateBorderTilePlayersDefault(StringBuffer htmlOutput) {
    htmlOutput.append(STYLE_INDENT + ".border-default-p1 {\n");
    htmlOutput.append(STYLE_INDENT + String.format("  background: #%06X;\n", P1.getColorDefault()));
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".border-default-p1:hover {\n");
    htmlOutput.append(STYLE_INDENT + "  opacity: " + HIGHLIGHT_OPACITY + ";\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append("\n");
    htmlOutput.append(STYLE_INDENT + ".border-default-p2 {\n");
    htmlOutput.append(STYLE_INDENT + String.format("  background: #%06X;\n", P2.getColorDefault()));
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".border-default-p2:hover {\n");
    htmlOutput.append(STYLE_INDENT + "  opacity: " + HIGHLIGHT_OPACITY + ";\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
  }
  private static void generateBorderTilePlayersRequired(StringBuffer htmlOutput) {
    htmlOutput.append(STYLE_INDENT + ".border-required-p1 {\n");
    htmlOutput.append(STYLE_INDENT + String.format("  background: radial-gradient(circle at center, #%06X 0px, #%06X 4px, #%06X 7px, #%06X 10px, #%06X 100%%);\n",
      COLOR_REQUIRED, COLOR_REQUIRED, P1.getColorDefault(), COLOR_REQUIRED, COLOR_REQUIRED));
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".border-required-p1:hover {\n");
    htmlOutput.append(STYLE_INDENT + String.format("  background: radial-gradient(circle at center, #%06X 0px, #%06X 4px, #%06X 7px, #%06X 10px, #%06X 100%%);\n",
      COLOR_REQUIRED, COLOR_REQUIRED, P1.getColorHighlight(), COLOR_REQUIRED, COLOR_REQUIRED));
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append("\n");
    htmlOutput.append(STYLE_INDENT + ".border-required-p2 {\n");
    htmlOutput.append(STYLE_INDENT + String.format("  background: radial-gradient(circle at center, #%06X 0px, #%06X 4px, #%06X 7px, #%06X 10px, #%06X 100%%);\n",
      COLOR_REQUIRED, COLOR_REQUIRED, P2.getColorDefault(), COLOR_REQUIRED, COLOR_REQUIRED));
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".border-required-p2:hover {\n");
    htmlOutput.append(STYLE_INDENT + String.format("  background: radial-gradient(circle at center, #%06X 0px, #%06X 4px, #%06X 7px, #%06X 10px, #%06X 100%%);\n",
      COLOR_REQUIRED, COLOR_REQUIRED, P2.getColorHighlight(), COLOR_REQUIRED, COLOR_REQUIRED));
    htmlOutput.append(STYLE_INDENT + "}\n");
  }

  private static void generateBingoTileCss(StringBuffer htmlOutput) {
    htmlOutput.append(STYLE_INDENT + ".bingo-tile {\n");
    htmlOutput.append(STYLE_INDENT + "  height: " + TILE_CELL_SIZE + "px;\n");
    htmlOutput.append(STYLE_INDENT + "  width: " + TILE_CELL_SIZE + "px;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".bingo-tile-background {\n");
    htmlOutput.append(STYLE_INDENT + "  background-color: #" + String.format("%06X", COLOR_BINGO_TILE_DEFAULT) + ";\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
    htmlOutput.append(STYLE_INDENT + ".bingo-tile-text {\n");
    htmlOutput.append(STYLE_INDENT + "  color: #" + String.format("%06X", COLOR_TEXT) + ";\n");
    htmlOutput.append(STYLE_INDENT + "  display: grid;\n");
    htmlOutput.append(STYLE_INDENT + "  place-items:center;\n");
    htmlOutput.append(STYLE_INDENT + "  text-align: center;\n");
    htmlOutput.append(STYLE_INDENT + "  user-select: none;\n");
    htmlOutput.append(STYLE_INDENT + "}\n");
  }


  private static void generateJs(StringBuffer htmlOutput) {
    htmlOutput.append("      <script>\n");
    generateJsConstants(htmlOutput);
    htmlOutput.append("\n");
    generateJsFunctionClickType(htmlOutput);
    generateJsFunctionPlayerType(htmlOutput);
    htmlOutput.append("\n");
    generateJsFunctionBorderTileClick(htmlOutput);
    generateJsFunctionBingoTileClick(htmlOutput);
    generateJsFunctionAdvanceTileState(htmlOutput);
    generateJsFunctionBingoTileMouse(htmlOutput);
    htmlOutput.append("      </script>\n");
  }

  private static void generateJsConstants(StringBuffer htmlOutput) {
    final String DEFAULT = getRgba(COLOR_BINGO_TILE_DEFAULT, "1");
    final String P1_HIGHLIGHT = getRgba(P1.getColorHighlight(), HIGHLIGHT_OPACITY);
    final String P2_HIGHLIGHT = getRgba(P2.getColorHighlight(), HIGHLIGHT_OPACITY);
    final String P1_COLORS = String.format("const Player1Colors = { DEFAULT : \"%s\", HIGHLIGHT : \"%s\" };\n", DEFAULT, P1_HIGHLIGHT);
    final String P2_COLORS = String.format("const Player2Colors = { DEFAULT : \"%s\", HIGHLIGHT : \"%s\" };\n", DEFAULT, P2_HIGHLIGHT);

    htmlOutput.append(SCRIPT_INDENT + "const ClickType = { NORMAL : \"0\", SHIFT : \"1\", CONTROL : \"2\" };\n");
    htmlOutput.append(SCRIPT_INDENT + "Object.freeze(ClickType);\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "const PlayerType = { NONE : \"0\", P1 : \"1\", P2 : \"2\" };\n");
    htmlOutput.append(SCRIPT_INDENT + "Object.freeze(PlayerType);\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + P1_COLORS);
    htmlOutput.append(SCRIPT_INDENT + "Object.freeze(Player1Colors);\n");
    htmlOutput.append(SCRIPT_INDENT + P2_COLORS);
    htmlOutput.append(SCRIPT_INDENT + "Object.freeze(Player2Colors);\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "const PlayerTileStates = { GOAL_NOT_MET : \"0\", GOAL_MET : \"1\", GOAL_CANNOT_BE_MET_ANYMORE : \"2\" };\n");
    htmlOutput.append(SCRIPT_INDENT + "Object.freeze(PlayerTileStates);\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "const PlayerBorderStates = { BINGO_UNCLAIMED : \"0\", BINGO_REQUIRED : \"1\", BINGO_CANNOT_HAPPEN_ANYMORE : \"2\" };\n");
    htmlOutput.append(SCRIPT_INDENT + "Object.freeze(PlayerBorderStates);\n");
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
    htmlOutput.append(SCRIPT_INDENT + "  withinX = (0 < relativeX) && (relativeX <= " + TILE_CELL_SIZE + ");\n");
    htmlOutput.append(SCRIPT_INDENT + "  withinY = (0 < relativeY) && (relativeY <= " + TILE_CELL_SIZE + ");\n");
    htmlOutput.append(SCRIPT_INDENT + "  player1 = (sum < " + TILE_CELL_SIZE + ") && withinX && withinY;\n");
    htmlOutput.append(SCRIPT_INDENT + "  player2 = (sum > " + TILE_CELL_SIZE + ") && withinX && withinY;\n");
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
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }
  private static void generateJsFunctionAdvanceTileState(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function advanceTileState(clickType, state, isPlayer1, outline) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  var svgHref = document.createAttribute(\"href\");\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  switch (clickType) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    case ClickType.NORMAL:\n");
    htmlOutput.append(SCRIPT_INDENT + "      switch (state.value) {\n");
    htmlOutput.append(SCRIPT_INDENT + "        case PlayerTileStates.GOAL_NOT_MET:\n");
    htmlOutput.append(SCRIPT_INDENT + "          state.value = PlayerTileStates.GOAL_MET;\n");
    htmlOutput.append(SCRIPT_INDENT + "          svgHref.value = (player1 ? \"#p1-goal-met\" : \"#p2-goal-met\");\n");
    htmlOutput.append(SCRIPT_INDENT + "        break;\n");
    htmlOutput.append(SCRIPT_INDENT + "        case PlayerTileStates.GOAL_MET:\n");
    htmlOutput.append(SCRIPT_INDENT + "          state.value = PlayerTileStates.GOAL_CANNOT_BE_MET_ANYMORE;\n");
    htmlOutput.append(SCRIPT_INDENT + "          svgHref.value = (player1 ? \"#p1-goal-cannot-be-met\" : \"#p2-goal-cannot-be-met\");\n");
    htmlOutput.append(SCRIPT_INDENT + "        break;\n");
    htmlOutput.append(SCRIPT_INDENT + "        case PlayerTileStates.GOAL_CANNOT_BE_MET_ANYMORE:\n");
    htmlOutput.append(SCRIPT_INDENT + "          state.value = PlayerTileStates.GOAL_NOT_MET;\n");
    htmlOutput.append(SCRIPT_INDENT + "          svgHref.value = \"#empty\";\n");
    htmlOutput.append(SCRIPT_INDENT + "        break;\n");
    htmlOutput.append(SCRIPT_INDENT + "      }\n");
    htmlOutput.append(SCRIPT_INDENT + "    break;\n");
    htmlOutput.append(SCRIPT_INDENT + "    case ClickType.CONTROL:\n");
    htmlOutput.append(SCRIPT_INDENT + "      if (state.value == PlayerTileStates.GOAL_MET) {\n");
    htmlOutput.append(SCRIPT_INDENT + "        state.value = PlayerTileStates.GOAL_NOT_MET;\n");
    htmlOutput.append(SCRIPT_INDENT + "        svgHref.value = \"#empty\";\n");
    htmlOutput.append(SCRIPT_INDENT + "      } else {\n");
    htmlOutput.append(SCRIPT_INDENT + "        state.value = PlayerTileStates.GOAL_MET;\n");
    htmlOutput.append(SCRIPT_INDENT + "        svgHref.value = (player1 ? \"#p1-goal-met\" : \"#p2-goal-met\");\n");
    htmlOutput.append(SCRIPT_INDENT + "      }\n");
    htmlOutput.append(SCRIPT_INDENT + "    break;\n");
    htmlOutput.append(SCRIPT_INDENT + "    case ClickType.SHIFT:\n");
    htmlOutput.append(SCRIPT_INDENT + "      if (state.value == PlayerTileStates.GOAL_CANNOT_BE_MET_ANYMORE) {\n");
    htmlOutput.append(SCRIPT_INDENT + "        state.value = PlayerTileStates.GOAL_NOT_MET;\n");
    htmlOutput.append(SCRIPT_INDENT + "        svgHref.value = \"#empty\";\n");
    htmlOutput.append(SCRIPT_INDENT + "      } else {\n");
    htmlOutput.append(SCRIPT_INDENT + "        state.value = PlayerTileStates.GOAL_CANNOT_BE_MET_ANYMORE;\n");
    htmlOutput.append(SCRIPT_INDENT + "        svgHref.value = (player1 ? \"#p1-goal-cannot-be-met\" : \"#p2-goal-cannot-be-met\");\n");
    htmlOutput.append(SCRIPT_INDENT + "      }\n");
    htmlOutput.append(SCRIPT_INDENT + "    break;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  outline.attributes.setNamedItem(svgHref);\n");
    htmlOutput.append(SCRIPT_INDENT + "}\n");
  }
  private static void generateJsFunctionBingoTileMouse(StringBuffer htmlOutput) {
    htmlOutput.append(SCRIPT_INDENT + "function mouseBingoTile(element, event) {\n");
    htmlOutput.append(SCRIPT_INDENT + "  var playerType = classifyPlayer(element, event);\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  var colorP1 = Player1Colors.DEFAULT;\n");
    htmlOutput.append(SCRIPT_INDENT + "  var colorP2 = Player2Colors.DEFAULT;\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  if (playerType == PlayerType.P1) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    colorP1 = Player1Colors.HIGHLIGHT;\n");
    htmlOutput.append(SCRIPT_INDENT + "  } else if (playerType == PlayerType.P2) {\n");
    htmlOutput.append(SCRIPT_INDENT + "    colorP2 = Player2Colors.HIGHLIGHT;\n");
    htmlOutput.append(SCRIPT_INDENT + "  }\n");
    htmlOutput.append("\n");
    htmlOutput.append(SCRIPT_INDENT + "  var tileBackground = \"linear-gradient(135deg, \" + colorP1 + \" 0px, \" + colorP1 + \" 68px, #" + String.format("%06X", COLOR_BINGO_TILE_DEFAULT) + " 69px, #" + String.format("%06X", COLOR_BINGO_TILE_DEFAULT) + " 70px, \" + colorP2 + \" 71px, \" + colorP2 + \" 140px)\";\n");
    htmlOutput.append(SCRIPT_INDENT + "  element.style.background = tileBackground;\n");
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

    htmlOutput.append(INDENT + "<svg class=\"not-visible\">\n");
    htmlOutput.append(INDENT + "  <defs>\n");
    htmlOutput.append(INDENT + "    <polygon id=\"empty\" points=\"\" />\n");

    // TODO: Re-write these as a function of TILE_CELL_SIZE and stroke-width
    htmlOutput.append("\n");
    htmlOutput.append(INDENT + "    <polygon id=\"column\" class=\"border-column outline\" points=\"2,2 94,2 94,22, 2,22\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"diagonal\" class=\"border-diagonal outline\" points=\"2,2 22,2 22,22, 2,22\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"row\" class=\"border-row outline\" points=\"2,2 22,2 22,94, 2,94\" />\n");
    htmlOutput.append("\n");
    htmlOutput.append(INDENT + "    <polygon id=\"p1-goal-met\" class=\"bingo-tile outline captured-p1\" points=\"2,2 92,2, 2,92\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"p1-goal-cannot-be-met\" class=\"bingo-tile outline restricted\" points=\"2,2, 92,2, 2,92, 2,2 46,46\" />\n");
    htmlOutput.append("\n");
    htmlOutput.append(INDENT + "    <polygon id=\"p2-goal-met\" class=\"bingo-tile outline captured-p2\" points=\"94,94, 6,94 94,6\" />\n");
    htmlOutput.append(INDENT + "    <polygon id=\"p2-goal-cannot-be-met\" class=\"bingo-tile outline restricted\" points=\"94,94, 6,94 94,6, 94,94, 50,50\" />\n");

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
    String th = String.format("<th id=\"border-%d\" class=\"border-%s border-default-p%c\" title=\"%s\" onclick=\"clickBorderTile(this, 'p%c')\">\n",
      borderId, borderType, player, title, player);
    String parentDiv = String.format("  <div id=\"parent-div\" class=\"border-%s\">\n", borderType);
    String stateDiv = String.format("    <div id=\"state-div\"  class=\"border-%s child-div\">\n", borderType);
    String outlinesDiv = String.format("    <div id=\"outlines-div\" class=\"border-%s child-div\">\n", borderType);

    int width = BORDER_CELL_SIZE;
    int height = BORDER_CELL_SIZE;
    if ("column".equals(borderType)) {
      width = TILE_CELL_SIZE;
    } else if ("row".equals(borderType)) {
      height = TILE_CELL_SIZE;
    }
    String svg = String.format("      <svg id=\"outlines\" width=\"%dpx\" height=\"%dpx\">\n", width, height);

    final String INDENT = "          ";
    htmlOutput.append(INDENT + th);
    htmlOutput.append(INDENT + parentDiv);
    htmlOutput.append(INDENT + stateDiv);
    htmlOutput.append(INDENT + "      <form id=\"form\"  class=\"not-visible\">\n");
    htmlOutput.append(INDENT + "        <input id=\"player\" type=\"hidden\" value=\"0\" />\n");
    htmlOutput.append(INDENT + "        <input id=\"required\" type=\"hidden\" value=\"0\" />\n");
    htmlOutput.append(INDENT + "      </form>\n");
    htmlOutput.append(INDENT + "    </div>\n");
    htmlOutput.append(INDENT + outlinesDiv);
    htmlOutput.append(INDENT + svg);
    htmlOutput.append(INDENT + "        <use id=\"player\" href=\"#empty\" />\n");
    htmlOutput.append(INDENT + "      </svg>\n");
    htmlOutput.append(INDENT + "    </div>\n");
    htmlOutput.append(INDENT + "  </div>\n");
    htmlOutput.append(INDENT + "</th>\n");
  }

  private static void generateBingoTile(StringBuffer htmlOutput, int row, int col) {
    String rowAndColumn = String.format("<!-- r%d-c%d -->\n", row, col);
    int tileNumber = (BINGO_SIZE * (row - 1)) + (col - 1);
    String td = String.format(" <td id=\"tile-%d\" class=\"bingo-tile bingo-tile-background\" onclick=\"clickBingoTile(this, event)\" onmouseenter=\"mouseBingoTile(this, event)\" onmousemove=\"mouseBingoTile(this, event)\" onmouseleave=\"mouseBingoTile(this, event)\">\n", tileNumber);
    String svg = String.format("      <svg id=\"outlines\" width=\"%dpx\" height=\"%dpx\">\n", TILE_CELL_SIZE, TILE_CELL_SIZE);
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

  private static class PlayerColors {
    private long colorDefault;
    private long colorHighlight;

    PlayerColors(long colorDefault, long colorHighlight) {
      this.colorDefault = colorDefault;
      this.colorHighlight = colorHighlight;
    }

    long getColorDefault() {
      return this.colorDefault;
    }
    long getColorHighlight() {
      return this.colorHighlight; // still in use by borders when bingo row/column/diagonal is required
    }
  }
}