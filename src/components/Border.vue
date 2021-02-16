<template>
    <th :id="'border-' + id" :data-id="id" :class="borderClass()"  @click="onClick">
      <div v-if="clicked" class="clicked-border">
        <div class="sphere"></div>
      </div>
    </th>
</template>

<script>
import { CLICK_BORDER } from '@/store/actions'

export default {
  name: 'Border',
  props: {
    id: {
      type: Number, required: true
    }
  },
  computed: {
    fill () {
      return this.$store.getters.borderStatus(this.id).fill
    },
    clicked () {
      return this.$store.getters.borderStatus(this.id).clicked
    }
  },
  methods: {
    borderClass: function () {
      var borderClass = this.id < 12 ? 'p1' : 'p2'
      const id = this.id % 12

      if (id < 5 || id > 9) {
        borderClass += ' border-row'
      }
      if (id >= 5) {
        borderClass += ' border-column'
      }
      if (this.fill === 1) {
        borderClass += ' completed'
      } else if (this.fill === 2) {
        borderClass += ' abandoned'
      } else {
        borderClass += ' default'
      }

      return borderClass
    },
    onClick: function (event) {
      const payload = { index: this.id }
      this.$store.dispatch(CLICK_BORDER, payload)
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
th {
 width: 32px;
 height: 32px;
 border-style: solid;
 border-width: medium;
 background-color: #000000;
 padding: 0;
}
th.p1 {
 border-color: #500070;
}
th.p2 {
 border-color:  #007030;
}

.abandoned {
  background-color: #400000;
}
th.p1.completed {
  background-color: #500070;
}
.p2.completed {
  background-color: #007030;
}

.clicked-border {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.sphere {
  border-radius: 20px;
}
.p1 .clicked-border .sphere {
  background-color: #cc00cc;
}
.p2 .clicked-border .sphere {
  background-color: #00cc00;
}

.clicked-border .sphere {
  width: 70%;
  height: 70%;
}

.border-col .clicked-border .sphere {
  width: 70%;
  height: 30%;
  margin: 0;
}

</style>
