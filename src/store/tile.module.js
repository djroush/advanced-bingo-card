import Vue from 'vue'
import { CLICK_TILE } from './actions'
import { SET_TILE, SET_BORDER } from './mutations'
const state = {
  tileStatuses: Array(25).fill({
    p1: 0, p2: 0
  })
}
const getters = {
  tileStatus: (state) => (index) => {
    return state.tileStatuses[index]
  },
  tileStatuses: state => state.tileStatuses
}
const actions = {
  [CLICK_TILE] (store, payload) {
    const { isP1, index, shiftKey } = payload
    var tiles = store.state.tileStatuses
    const tile = tiles[index]
    const tileStatus = isP1 ? tile.p1 : tile.p2
    var newValue = 0
    if (tileStatus === 0) {
      newValue = shiftKey ? 2 : 1
    } else if (tileStatus === 1) {
      newValue = shiftKey ? 2 : 0
    } else {
      newValue = shiftKey ? 0 : 1
    }
    const newValueObj = isP1 ? { p1: newValue } : { p2: newValue }
    const setTileParams = { index, newValueObj }
    store.commit(SET_TILE, setTileParams)
    // Update local copy of tiles after mutation
    tiles = store.getters.tileStatuses
    // Check and set border values - update tiles after mutation
    const row = parseInt(index / 5)
    const col = index % 5
    const rowTiles = tiles.filter((_, index) => parseInt(index / 5) === row)
    const colTiles = tiles.filter((_, index) => index % 5 === col)
    const rowBorderValueObj = getBorderValue(rowTiles, isP1)
    const colBorderValueObj = getBorderValue(colTiles, isP1)

    const rowBorderIndex = row + (isP1 ? 0 : 12)
    const colBorderIndex = col + 5 + (isP1 ? 0 : 12)

    store.commit(SET_BORDER, { index: rowBorderIndex, newValueObj: rowBorderValueObj })
    store.commit(SET_BORDER, { index: colBorderIndex, newValueObj: colBorderValueObj })
    if (index % 6 === 0) {
      const diag1Tiles = tiles.filter((_, index) => index % 6 === 0)
      const diag1BorderObj = getBorderValue(diag1Tiles, isP1)
      const diag1BorderIndex = 10 + (isP1 ? 0 : 12)
      store.commit(SET_BORDER, { index: diag1BorderIndex, newValueObj: diag1BorderObj })
    }
    if (index % 4 === 0 && index !== 0 && index !== 24) {
      const diag2Tiles = tiles.filter((_, index) => index % 4 === 0 && index !== 0 && index !== 24)
      const diag2BorderObj = getBorderValue(diag2Tiles, isP1)
      const diag2BorderIndex = 11 + (isP1 ? 0 : 12)
      store.commit(SET_BORDER, { index: diag2BorderIndex, newValueObj: diag2BorderObj })
    }
  }
}
function getBorderValue (tiles, isP1) {
  const tilesHaveBingo = tiles.filter(tile => (isP1 ? tile.p1 : tile.p2) === 1).length === 5
  const tilesAreSkipped = tiles.filter(tile => (isP1 ? tile.p1 : tile.p2) === 2).length > 0
  var borderValue = 0
  if (tilesAreSkipped) {
    borderValue = 2
  } else if (tilesHaveBingo) {
    borderValue = 1
  }
  return { fill: borderValue }
}
const mutations = {
  [SET_TILE] (state, params) {
    const { index, newValueObj } = params
    const tile = state.tileStatuses[index]
    Vue.set(state.tileStatuses, index, {...tile, ...newValueObj})
  }
}
export default {
  state, actions, mutations, getters
}
