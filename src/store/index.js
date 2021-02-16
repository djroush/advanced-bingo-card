import Vuex from 'vuex'
import Vue from 'vue'

import Tile from './tile.module'
import Border from './border.module'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    Tile, Border
  }
})
