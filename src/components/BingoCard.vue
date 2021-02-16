<template>
  <div id="bingoCard">
    <table>
      <tr>
        <Border :id=10 />
        <Border :id=5 />
        <Border :id=6 />
        <Border :id=7 />
        <Border :id=8 />
        <Border :id=9 />
        <Border :id=23 />
      </tr>
      <tr>
        <Border :id=0 />
        <Tile :id=0 text="R1 C1" />
        <Tile :id=1 text="R1 C2" />
        <Tile :id=2 text="R1 C3" />
        <Tile :id=3 text="R1 C4" />
        <Tile :id=4 text="R1 C5" />
        <Border :id=12 />
      </tr>
      <tr>
        <Border :id=1 />
          <Tile :id=5 text="R2 C1" />
          <Tile :id=6 text="R2 C2" />
          <Tile :id=7 text="R2 C3" />
          <Tile :id=8 text="R2 C4" />
          <Tile :id=9 text="R2 C5" />
        <Border :id=13 />
      </tr>
      <tr>
        <Border :id=2 />
        <Tile :id=10 text="R3 C1" />
        <Tile :id=11 text="R3 C2" />
        <Tile :id=12 text="R3 C3" />
        <Tile :id=13 text="R3 C4" />
        <Tile :id=14 text="R3 C5" />
        <Border :id=14 />
      </tr>
      <tr>
        <Border :id=3 />
        <Tile :id=15 text="R4 C1" />
        <Tile :id=16 text="R4 C2" />
        <Tile :id=17 text="R4 C3" />
        <Tile :id=18 text="R4 C4" />
        <Tile :id=19 text="R4 C5" />
        <Border :id=15 />
      </tr>
      <tr>
        <Border :id=4 />
        <Tile :id=20 text="R5 C1" />
        <Tile :id=21 text="R5 C2" />
        <Tile :id=22 text="R5 C3" />
        <Tile :id=23 text="R5 C4" />
        <Tile :id=24 text="R5 C5" />
        <Border :id=16 />
      </tr>
      <tr>
        <Border :id=11 />
        <Border :id=17 />
        <Border :id=18 />
        <Border :id=19 />
        <Border :id=20 />
        <Border :id=21 />
        <Border :id=22 />
      </tr>
    </table>
  </div>
</template>

!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#bingoCard {
 margin: 2rem;
}
</style>

<script>
import Tile from './Tile'
import Border from './Border'

export default {
  name: 'BingoCard',
  components: {
    Tile, Border
  }
}
</script>
