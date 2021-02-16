<template>
  <div id="app">
    <BingoCard/>
  </div>
</template>

<script>
import BingoCard from './components/BingoCard'

export default {
  name: 'App',
  components: {
    BingoCard
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
