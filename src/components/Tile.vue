<template>
    <td v-bind:id="'tile-' + id">
      <div class="tile">
        <div class="container">
          <div v-bind:data-id="id" :class="p1TileClass()" @click="onClick"></div>
          <div v-bind:data-id="id" :class="p2TileClass()" @click="onClick"></div>
        </div>
        <div class="container tileText">
          <div>
            <span>{{text}}</span>
          </div>
        </div>
      </div>
    </td>
</template>

<script>
import { CLICK_TILE } from '@/store/actions'

export default {
  name: 'Tile',
  props: {
    id: {
      type: Number, required: true
    },
    text: {
      type: String, required: true
    }
  },
  computed: {
    status () {
      return this.$store.getters.tileStatus(this.id)
    }
  },
  methods: {
    p1TileClass: function () {
      switch (this.status.p1) {
        case 1: return 'p1 clicked'
        case 2: return 'p1 not-clicked'
        default: return 'p1'
      }
    },
    p2TileClass: function () {
      switch (this.status.p2) {
        case 1: return 'p2 clicked'
        case 2: return 'p2 not-clicked'
        default: return 'p2'
      }
    },
    onClick: function (event) {
      const element = event.target
      const shiftKey = event.shiftKey
      const isP1 = element.classList.contains('p1')
      const index = parseInt(element.getAttribute('data-id'))
      const payload = { isP1, index, shiftKey }
      this.$store.dispatch(CLICK_TILE, payload)
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
td {
  background-color: #000000;
  width: 130px;
  height: 130px;
  overflow: hidden;
  padding: 0;
}

.tile {
  width: 100%;
  height: 100%;
}

.container {
    top: auto;
    position: absolute;
    width: 130px;
    height: 130px;
    overflow: hidden;
}

.p1, .p2 {
  width: 100%;
  height: 100%;
  transform-origin: 0% 0%;
  transform: skewY(-45deg);
}

.p1.clicked {
  background-color: #300048;
}
.p2.clicked {
  background-color: #004820;
}
.not-clicked {
  background-color: rgba(128,0,0, 0.5);
}

.p1:hover {
   background: rgb(78, 55, 91);
}
.p2:hover {
   background: rgb(68, 92, 71);
}

.tileText {
  display: flex;
  justify-content: center;
  flex-direction: column;
  color: #C0A000;
  justify-items: center;
  margin: auto;
  visibility: hidden;
}

.tileText div {
  align-self: center;
  text-align: center;
  display: inline-flex;
}

.tileText span {
  pointer-events: none;
  visibility: visible;
}
</style>
