<template>
	<div class="page">
		<div class="topAppBar">
			<table><tr>
				<th class="menuIconCell"><img src='../../assets/menu-icon.png' class="menuIcon"/></th>
				<th><p class="appBarText">Serwisy</p></th>
				<th class="filterIconCell"><img src='../../assets/filter-icon.png' class="filterIcon"/></th>
			</tr></table>
			
		</div>

		<div class="listBox">
			<ul>
				<li><input class="searchBox" type="text" placeholder="Szukaj.." v-model="phrase"></li>
				<li v-for="task in taskList"  v-bind:key="task.id">
					<TaskCard :task="task"/> 
				</li>
				
			</ul>
		</div>
	</div>
</template>


<script>
import TaskCard from './TaskCard.vue'
import tasksRepo from '../../repos/taskRepo'

export default {
	name: "TasksList",

	components: {
		TaskCard
	},

	data() {
    return {
      phrase: ""
    };
  },

	computed: {
		taskList(){
			return tasksRepo.getAllTasks(this.phrase)
		}
	}
}
</script>



<style scoped>
ul {
	max-height: 100%;
	list-style-type: none;
	padding: 0;
}
li {
	margin-top: 1em;
	padding-left: .8em;
	padding-right: .8em;
}
table {
	height: 100%;
	width: 100%;
}

.page {
	max-height: 100%;
	width: 100%;
}
.listBox{
	height: calc(100% - 3.5em);
	width: 100%;
	overflow-y:auto;
	position: absolute;
}
.topAppBar {
	background-color: rgb(54, 108, 223);
	height: 3.5em;
	width: 100%;
	top: 0;
}
.appBarText {
	color: white;
	font-weight: bold;
	text-align: left;
	margin-left: .2em;
	font-size: 1.15em;
}
.menuIcon {
	height: 2.4em;
}
.menuIconCell {
	width: 3.5em;
	padding-left: .1em;
}
.filterIcon {
	height: 2.2em;
}
.filterIconCell {
	width: 3em;
	padding-right: .7em;
}
.searchBox{
	background-color: rgb(180, 180, 223);
	border-radius: .9em;
	width: 90%;
	font-size: 1.2em;
	height: 1.9em;

	padding: 12px 20px;
	margin: 8px 0;
	display: inline-block;
	border: 0px;
	box-sizing: border-box;
}


</style>
