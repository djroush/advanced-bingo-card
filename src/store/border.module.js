import Vue from 'vue'
import { SET_BORDER } from './mutations'
import { CLICK_BORDER, FILL_BORDER } from './actions'

const state = {
  // (row 1, 2, 3, 4, 5, col 1, 2, 3, 4, 5, diag1, diag2) x 2
  borderStatuses: Array(24).fill({
    clicked: false, fill: 0
  })
}

const getters = {
  borderStatus: (state) => (index) => {
    return state.borderStatuses[index]
  }
}

const actions = {
  [CLICK_BORDER] (store, payload) {
    const { index } = payload
    const border = store.getters.borderStatus(index)
    const newValueObj = {clicked: !border.clicked}
    const params = {
      index, newValueObj
    }
    store.commit(SET_BORDER, params)
  },
  [FILL_BORDER] (store, payload) {
    store.commit(SET_BORDER, payload)
  }
}

const mutations = {
  [SET_BORDER] (state, params) {
    const { index, newValueObj } = params
    const border = state.borderStatuses[index]
    Vue.set(state.borderStatuses, index, {...border, ...newValueObj})
  }
}

export default {
  state, actions, mutations, getters
}
